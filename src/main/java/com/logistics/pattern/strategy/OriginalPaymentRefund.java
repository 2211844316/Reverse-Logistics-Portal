package com.logistics.pattern.strategy;

import com.logistics.model.ReturnRequest;

/**
 * Strategy Pattern - Concrete Strategy
 * 
 * Processes refunds back to the original payment method used
 * for the purchase (e.g., credit card, PayPal, etc.).
 */
public class OriginalPaymentRefund implements RefundStrategy {

    @Override
    public String processRefund(ReturnRequest request) {
        double amount = request.getRefundAmount();
        request.setRefundMethod("Original Payment");
        return String.format(
            "✓ Refund of $%.2f processed to original payment method for request %s.\n" +
            "  Customer: %s\n" +
            "  Refund will appear on original payment statement in 5-7 business days.",
            amount, request.getRequestId(), request.getCustomer().getName());
    }

    @Override
    public String getMethodName() {
        return "Original Payment";
    }

    @Override
    public String getDescription() {
        return "Refund to original payment method (5-7 business days)";
    }
}
