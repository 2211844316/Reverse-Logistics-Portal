package com.logistics.pattern.observer;

import com.logistics.model.ReturnRequest;
import com.logistics.model.RequestStatus;

import java.util.ArrayList;
import java.util.List;

/**
 * Observer Pattern - Concrete Subject
 * 
 * Manages a list of StatusObserver instances and notifies all
 * of them when a return request's status changes.
 */
public class ReturnRequestSubject implements StatusSubject {
    private final List<StatusObserver> observers = new ArrayList<>();

    @Override
    public void attach(StatusObserver observer) {
        if (!observers.contains(observer)) {
            observers.add(observer);
            System.out.println("[Subject] Observer attached: " + observer.getObserverName());
        }
    }

    @Override
    public void detach(StatusObserver observer) {
        observers.remove(observer);
        System.out.println("[Subject] Observer detached: " + observer.getObserverName());
    }

    @Override
    public void notifyObservers(ReturnRequest request, RequestStatus oldStatus, RequestStatus newStatus) {
        System.out.println("[Subject] Notifying " + observers.size() + " observers about status change: "
                + oldStatus.getDisplayName() + " → " + newStatus.getDisplayName());
        for (StatusObserver observer : observers) {
            observer.update(request, oldStatus, newStatus);
        }
    }

    /**
     * Get the number of attached observers.
     * @return The observer count
     */
    public int getObserverCount() {
        return observers.size();
    }
}
