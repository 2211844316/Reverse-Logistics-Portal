package com.logistics.pattern.strategy;

import com.logistics.model.ReturnRequest;

/**
 * Strategy Pattern - Concrete Strategy
 * 
 * Processes refunds via bank transfer.
 * Funds are deposited directly to the customer's bank account.
 */
public class BankTransferRefund implements RefundStrategy {

    @Override
    public String processRefund(ReturnRequest request) {
        double amount = request.getRefundAmount();
        request.setRefundMethod("Bank Transfer");
        return String.format(
            "✓ Refund of $%.2f processed via Bank Transfer for request %s.\n" +
            "  Customer: %s\n" +
            "  Funds will be deposited to bank account in 3-5 business days.",
            amount, request.getRequestId(), request.getCustomer().getName());
    }

    @Override
    public String getMethodName() {
        return "Bank Transfer";
    }

    @Override
    public String getDescription() {
        return "Refund to customer's bank account (3-5 business days)";
    }
}
