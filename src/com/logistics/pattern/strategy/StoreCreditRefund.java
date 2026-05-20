package com.logistics.pattern.strategy;

import com.logistics.model.ReturnRequest;

/**
 * Strategy Pattern - Concrete Strategy
 * 
 * Processes refunds as store credit with a 10% bonus incentive.
 * Credit is available immediately for use on future purchases.
 */
public class StoreCreditRefund implements RefundStrategy {

    private static final double BONUS_RATE = 0.10; // 10% bonus

    @Override
    public String processRefund(ReturnRequest request) {
        double originalAmount = request.getRefundAmount();
        double bonusAmount = originalAmount * BONUS_RATE;
        double totalCredit = originalAmount + bonusAmount;
        request.setRefundMethod("Store Credit");
        return String.format(
            "✓ Store credit of $%.2f issued for request %s.\n" +
            "  Original amount: $%.2f + 10%% bonus: $%.2f\n" +
            "  Customer: %s\n" +
            "  Credit is available immediately for future purchases.",
            totalCredit, request.getRequestId(),
            originalAmount, bonusAmount,
            request.getCustomer().getName());
    }

    @Override
    public String getMethodName() {
        return "Store Credit";
    }

    @Override
    public String getDescription() {
        return "Store credit with 10% bonus (available immediately)";
    }
}
