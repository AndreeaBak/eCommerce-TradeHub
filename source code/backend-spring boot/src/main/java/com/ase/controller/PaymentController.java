package com.ase.controller;

import com.ase.domain.PaymentMethod;
import com.ase.model.*;
import com.ase.repository.CartItemRepository;
import com.ase.repository.CartRepository;
import com.ase.response.ApiResponse;
import com.ase.response.PaymentLinkResponse;
import com.ase.service.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@RestController
@RequiredArgsConstructor
public class PaymentController {

    private final UserService userService;
    private final PaymentService paymentService;
    private final TransactionService transactionService;
    private final SellerReportService sellerReportService;
    private final SellerService sellerService;
    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;


    @PostMapping("/api/payment/{paymentMethod}/order/{orderId}")
    public ResponseEntity<PaymentLinkResponse> paymentHandler(
            @PathVariable PaymentMethod paymentMethod,
            @PathVariable Long orderId,
            @RequestHeader("Authorization") String jwt) throws Exception {

        User user = userService.findUserProfileByJwt(jwt);

        PaymentOrder order = paymentService.getPaymentOrderById(orderId);

        PaymentLinkResponse paymentResponse = new PaymentLinkResponse();

        // Acum creăm DOAR Stripe Payment Link
        String paymentUrl = paymentService.createStripePaymentLink(user,
                order.getAmount(),
                order.getId());

        paymentResponse.setPayment_link_url(paymentUrl);

        return new ResponseEntity<>(paymentResponse, HttpStatus.CREATED);
    }

    @GetMapping("/api/payment/{paymentId}")
    public ResponseEntity<ApiResponse> paymentSuccessHandler(
            @PathVariable String paymentId,
            @RequestParam String paymentLinkId,
            @RequestHeader("Authorization") String jwt) throws Exception {

        User user = userService.findUserProfileByJwt(jwt);

        PaymentOrder paymentOrder = paymentService.getPaymentOrderByPaymentId(paymentLinkId);

        boolean paymentSuccess = paymentService.completePaymentOrder(paymentOrder);

        if (paymentSuccess) {
            for (Order order : paymentOrder.getOrders()) {
                transactionService.createTransaction(order);
                Seller seller = sellerService.getSellerById(order.getSellerId());
                SellerReport report = sellerReportService.getSellerReport(seller);
                report.setTotalOrders(report.getTotalOrders() + 1);
                report.setTotalEarnings(report.getTotalEarnings() + order.getTotalSellingPrice());
                report.setTotalSales(report.getTotalSales() + order.getOrderItems().size());
                sellerReportService.updateSellerReport(report);
            }

            Cart cart = cartRepository.findByUserId(user.getId());
            cart.setCouponPrice(0);
            cart.setCouponCode(null);
            cartRepository.save(cart);
        }

        ApiResponse res = new ApiResponse();
        res.setMessage("Payment successful");
        res.setStatus(true);

        return new ResponseEntity<>(res, HttpStatus.CREATED);
    }

}
