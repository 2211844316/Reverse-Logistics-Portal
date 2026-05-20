package com.logistics.pattern.strategy;

import com.logistics.model.ReturnRequest;

/**
 * Strategy Pattern - Context Class
 * 
 * Maintains a reference to a RefundStrategy object and delegates
 * the refund processing to the currently set strategy.
 * The strategy can be changed at runtime.
 */
public class RefundProcessor {
    private RefundStrategy strategy;

    public RefundProcessor() {
        this.strategy = null;
    }

    public RefundProcessor(RefundStrategy strategy) {
        this.strategy = strategy;
    }

    /**
     * Set the refund strategy to use.
     * @param strategy The refund strategy
     */
    public void setStrategy(RefundStrategy strategy) {
        this.strategy = strategy;
    }

    /**
     * Get the current refund strategy.
     * @return The current strategy, or null if none set
     */
    public RefundStrategy getStrategy() {
        return strategy;
    }

    /**
     * Execute the refund using the current strategy.
     * @param request The return request to process
     * @return A message describing the refund result
     * @throws IllegalStateException if no strategy has been set
     */
    public String executeRefund(ReturnRequest request) {
        if (strategy == null) {
            throw new IllegalStateException(
                "No refund strategy has been set. Please select a refund method before processing.");
        }
        return strategy.processRefund(request);
    }
}
