package com.systems.payment.controller;

import com.systems.payment.dto.PaymentRequest;
import com.systems.payment.dto.PaymentResponse;
import com.systems.payment.service.PaymentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/payments")
@Slf4j
public class PaymentController {
    private final PaymentService paymentService;
    
    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }
    
    @PostMapping("/process")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public PaymentResponse processPayment(@RequestBody PaymentRequest request) {
        log.info("Request to process payment for order: {}", request.orderId());
        return paymentService.processPayment(request);
    }
}
