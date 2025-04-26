package com.ase.service;

import com.stripe.exception.StripeException;
import com.ase.model.Order;
import com.ase.model.PaymentOrder;
import com.ase.model.User;

import java.util.Set;

public interface PaymentService {

    PaymentOrder createOrder(User user, Set<Order> orders);

    PaymentOrder getPaymentOrderById(Long id) throws Exception;

    PaymentOrder getPaymentOrderByPaymentId(String paymentLinkId) throws Exception;

    String createStripePaymentLink(User user, Long amount, Long orderId) throws StripeException;

    Boolean completePaymentOrder(PaymentOrder paymentOrder);
}
