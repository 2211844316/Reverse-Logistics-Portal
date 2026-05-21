package com.logistics.pattern.observer;

import com.logistics.model.ReturnRequest;
import com.logistics.model.RequestStatus;

/**
 * Observer Pattern - Observer Interface
 * 
 * Defines the interface for objects that should be notified
 * when a return request's status changes.
 */
public interface StatusObserver {
    /**
     * Called when a return request's status changes.
     * @param request   The return request that changed
     * @param oldStatus The previous status
     * @param newStatus The new status
     */
    void update(ReturnRequest request, RequestStatus oldStatus, RequestStatus newStatus);

    /**
     * Get the name of this observer.
     * @return The observer name
     */
    String getObserverName();
}
