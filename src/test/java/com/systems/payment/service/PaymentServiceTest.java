package com.systems.payment.service;

import com.systems.payment.dto.PaymentRequest;
import com.systems.payment.dto.SettlementEvent;
import com.systems.payment.entity.Payment;
import com.systems.payment.entity.PaymentStatus;
import com.systems.payment.repository.PaymentRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.kafka.core.KafkaTemplate;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PaymentServiceTest {
    
    @Mock
    private PaymentRepository paymentRepository;
    
    @Mock
    private KafkaTemplate<String, SettlementEvent> kafkaTemplate;
    
    @InjectMocks
    private PaymentService paymentService;
    
    @Test
    void processPayment_Success() {
        PaymentRequest request = new PaymentRequest(1L, BigDecimal.TEN);
        Payment payment = new Payment();
        payment.setPaymentId("pay-123");
        payment.setStatus(PaymentStatus.SUCCESS);
        
        when(paymentRepository.save(any(Payment.class))).thenReturn(payment);
        
        var result = paymentService.processPayment(request, "1L");
        
        assertNotNull(result);
        assertEquals("pay-123", result.paymentId());
        assertEquals("SUCCESS", result.status());
        verify(paymentRepository).save(any(Payment.class));
        verify(kafkaTemplate).send(eq("settlements"), any(SettlementEvent.class));
    }
}
