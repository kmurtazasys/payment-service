package com.systems.payment.controller;

import com.systems.payment.dto.PaymentRequest;
import com.systems.payment.dto.PaymentResponse;
import com.systems.payment.service.PaymentService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/payments")
@Slf4j
public class PaymentController {
    private final PaymentService paymentService;
    
    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }
    
    @PostMapping
    @PreAuthorize("hasAnyRole('USER', 'ADMIN') or hasAuthority('SCOPE_PAYMENT.INTERNAL')")
    public PaymentResponse processPayment(@RequestBody @Valid PaymentRequest request,
                                          @AuthenticationPrincipal Jwt jwt) {
        String customerId = jwt.getClaim("id");
        log.info("Request to process payment for order: {}", request.orderId());
        return paymentService.processPayment(request, customerId);
    }
}
