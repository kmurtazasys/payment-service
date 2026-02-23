package com.systems.payment.exception;

import lombok.Getter;

@Getter
public enum ErrorCode {
    INVALID_PAYMENT_REQUEST("PAY_001", "Invalid payment request"),
    PAYMENT_PROCESSING_FAILED("PAY_002", "Payment processing failed"),
    KAFKA_PUBLISH_FAILED("PAY_003", "Failed to publish settlement event"),
    INTERNAL_ERROR("PAY_999", "Internal server error");
    
    private final String code;
    private final String message;
    
    ErrorCode(String code, String message) {
        this.code = code;
        this.message = message;
    }
}
