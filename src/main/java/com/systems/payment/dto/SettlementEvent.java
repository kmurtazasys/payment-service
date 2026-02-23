package com.systems.payment.dto;

import java.math.BigDecimal;

public record SettlementEvent(String paymentId, Long orderId, String customerId, BigDecimal amount) {}
