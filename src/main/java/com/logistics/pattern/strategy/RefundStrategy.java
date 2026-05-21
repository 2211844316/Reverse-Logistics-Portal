package com.logistics.pattern.strategy;

import com.logistics.model.ReturnRequest;

/**
 * Strategy Pattern - Strategy Interface
 * 
 * Defines the interface for different refund processing algorithms.
 * Each concrete strategy implements a different refund method
 * (bank transfer, store credit, or original payment).
 */
public interface RefundStrategy {
    /**
     * Process a refund for the given return request.
     * @param request The return request to process
     * @return A message describing the refund result
     */
    String processRefund(ReturnRequest request);

    /**
     * Get the name of this refund method.
     * @return The method name
     */
    String getMethodName();

    /**
     * Get a description of this refund method.
     * @return The method description
     */
    String getDescription();
}
