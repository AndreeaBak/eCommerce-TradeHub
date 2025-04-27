package com.ase.service.impl;

import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.checkout.Session;
import com.stripe.param.checkout.SessionCreateParams;
import com.ase.domain.PaymentOrderStatus;
import com.ase.domain.PaymentStatus;
import com.ase.model.Order;
import com.ase.model.PaymentOrder;
import com.ase.model.User;
import com.ase.repository.CartRepository;
import com.ase.repository.OrderRepository;
import com.ase.repository.PaymentOrderRepository;
import com.ase.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {

    @Value("${stripe.api.key}")
    private String stripeSecretKey;

    private final PaymentOrderRepository paymentOrderRepository;
    private final OrderRepository orderRepository;
    private final CartRepository cartRepository;

    @Override
    public PaymentOrder createOrder(User user, Set<Order> orders) {
        Long amount = orders.stream().mapToLong(Order::getTotalSellingPrice).sum();
        int couponPrice = cartRepository.findByUserId(user.getId()).getCouponPrice();

        PaymentOrder order = new PaymentOrder();
        order.setUser(user);
        order.setAmount(amount - couponPrice);
        order.setOrders(orders);
        order.setStatus(PaymentOrderStatus.PENDING);

        return paymentOrderRepository.save(order);
    }

    @Override
    public PaymentOrder getPaymentOrderById(Long id) throws Exception {
        Optional<PaymentOrder> optionalPaymentOrder = paymentOrderRepository.findById(id);
        if (optionalPaymentOrder.isEmpty()) {
            throw new Exception("Payment order not found with id " + id);
        }
        return optionalPaymentOrder.get();
    }

    @Override
    public PaymentOrder getPaymentOrderByPaymentId(String paymentLinkId) throws Exception {
        PaymentOrder paymentOrder = paymentOrderRepository.findByPaymentLinkId(paymentLinkId);
        if (paymentOrder == null) {
            throw new Exception("Payment order not found with payment link id " + paymentLinkId);
        }
        return paymentOrder;
    }

    @Override
    public String createStripePaymentLink(User user, Long amount, Long orderId) throws StripeException {
        Stripe.apiKey = stripeSecretKey;

        SessionCreateParams params = SessionCreateParams.builder()
                .addPaymentMethodType(SessionCreateParams.PaymentMethodType.CARD)
                .setMode(SessionCreateParams.Mode.PAYMENT)
                .setSuccessUrl("http://localhost:3000/payment-success/" + orderId)
                .setCancelUrl("http://localhost:3000/payment/cancel")
                .addLineItem(SessionCreateParams.LineItem.builder()
                        .setQuantity(1L)
                        .setPriceData(SessionCreateParams.LineItem.PriceData.builder()
                                .setCurrency("eur")
                                .setUnitAmount(amount * 100)
                                .setProductData(SessionCreateParams
                                        .LineItem
                                        .PriceData
                                        .ProductData
                                        .builder()
                                        .setName("Order payment")
                                        .build()
                                ).build()
                        ).build()
                ).build();

        Session session = Session.create(params);
        PaymentOrder paymentOrder = paymentOrderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));
        paymentOrder.setPaymentLinkId(session.getId());
        paymentOrderRepository.save(paymentOrder);

        return session.getUrl();
    }

    @Override
    public Boolean completePaymentOrder(PaymentOrder paymentOrder) {
        if (paymentOrder.getStatus().equals(PaymentOrderStatus.PENDING)) {
            Set<Order> orders = paymentOrder.getOrders();
            for (Order order : orders) {
                order.setPaymentStatus(PaymentStatus.COMPLETED);
                orderRepository.save(order);
            }
            paymentOrder.setStatus(PaymentOrderStatus.SUCCESS);
            paymentOrderRepository.save(paymentOrder);
            return true;
        }
        return false;
    }
}
