package com.logistics.pattern.observer;

import com.logistics.model.ReturnRequest;
import com.logistics.model.RequestStatus;

/**
 * Observer Pattern - Subject Interface
 * 
 * Defines the interface for objects that manage observers
 * and notify them of status changes.
 */
public interface StatusSubject {
    /**
     * Attach an observer to receive notifications.
     * @param observer The observer to attach
     */
    void attach(StatusObserver observer);

    /**
     * Detach an observer from receiving notifications.
     * @param observer The observer to detach
     */
    void detach(StatusObserver observer);

    /**
     * Notify all attached observers of a status change.
     * @param request   The return request that changed
     * @param oldStatus The previous status
     * @param newStatus The new status
     */
    void notifyObservers(ReturnRequest request, RequestStatus oldStatus, RequestStatus newStatus);
}
