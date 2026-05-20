package com.logistics.model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Represents a return/refund/exchange request submitted by a customer.
 */
public class ReturnRequest {
    private static int counter = 1000;
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    private String requestId;
    private Customer customer;
    private Product product;
    private String returnType; // "Return", "Exchange", "Refund"
    private String reason;
    private RequestStatus status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private String refundMethod;
    private double refundAmount;
    private String adminNotes;
    private int quantity;

    public ReturnRequest(Customer customer, Product product, String returnType, String reason, int quantity) {
        this.requestId = "RR-" + (++counter);
        this.customer = customer;
        this.product = product;
        this.returnType = returnType;
        this.reason = reason;
        this.quantity = quantity;
        this.status = RequestStatus.PENDING;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
        this.refundAmount = product.getPrice() * quantity;
        this.refundMethod = "Not Processed";
        this.adminNotes = "";
    }

    // Getters and Setters
    public String getRequestId() {
        return requestId;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public String getReturnType() {
        return returnType;
    }

    public void setReturnType(String returnType) {
        this.returnType = returnType;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public RequestStatus getStatus() {
        return status;
    }

    public void setStatus(RequestStatus status) {
        this.updatedAt = LocalDateTime.now();
        this.status = status;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public String getCreatedAtFormatted() {
        return createdAt.format(FORMATTER);
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public String getUpdatedAtFormatted() {
        return updatedAt.format(FORMATTER);
    }

    public String getRefundMethod() {
        return refundMethod;
    }

    public void setRefundMethod(String refundMethod) {
        this.refundMethod = refundMethod;
    }

    public double getRefundAmount() {
        return refundAmount;
    }

    public void setRefundAmount(double refundAmount) {
        this.refundAmount = refundAmount;
    }

    public String getAdminNotes() {
        return adminNotes;
    }

    public void setAdminNotes(String adminNotes) {
        this.adminNotes = adminNotes;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    @Override
    public String toString() {
        return requestId + " - " + product.getName() + " (" + status.getDisplayName() + ")";
    }
}
