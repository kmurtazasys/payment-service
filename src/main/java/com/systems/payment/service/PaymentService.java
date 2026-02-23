package com.systems.payment.service;

import com.systems.payment.dto.PaymentRequest;
import com.systems.payment.dto.PaymentResponse;
import com.systems.payment.dto.SettlementEvent;
import com.systems.payment.entity.Payment;
import com.systems.payment.entity.PaymentStatus;
import com.systems.payment.exception.BusinessException;
import com.systems.payment.exception.ErrorCode;
import com.systems.payment.repository.PaymentRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@Slf4j
public class PaymentService {
    private final PaymentRepository paymentRepository;
    private final KafkaTemplate<String, SettlementEvent> kafkaTemplate;
    
    public PaymentService(PaymentRepository paymentRepository, 
                         KafkaTemplate<String, SettlementEvent> kafkaTemplate) {
        this.paymentRepository = paymentRepository;
        this.kafkaTemplate = kafkaTemplate;
    }
    
    @Transactional
    public PaymentResponse processPayment(PaymentRequest request) {
        log.info("Processing payment for order: {}", request.orderId());
        
        try {
            Payment payment = new Payment();
            payment.setPaymentId(UUID.randomUUID().toString());
            payment.setOrderId(request.orderId());
            payment.setCustomerId(request.customerId());
            payment.setAmount(request.amount());
            payment.setStatus(PaymentStatus.SUCCESS);
            
            payment = paymentRepository.save(payment);
            log.info("Payment saved successfully: {}", payment.getPaymentId());
            
            // Publish to Kafka for settlement
            SettlementEvent event = new SettlementEvent(
                payment.getPaymentId(),
                payment.getOrderId(),
                payment.getCustomerId(),
                payment.getAmount()
            );
            
            kafkaTemplate.send("settlements", event);
            log.info("Settlement event published for payment: {}", payment.getPaymentId());
            
            return new PaymentResponse(payment.getPaymentId(), payment.getStatus().name());
        } catch (Exception ex) {
            log.error("Payment processing failed for order: {}", request.orderId(), ex);
            throw new BusinessException(ErrorCode.PAYMENT_PROCESSING_FAILED, "Failed to process payment");
        }
    }
}
