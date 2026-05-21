package com.logistics.pattern.observer;

import com.logistics.model.ReturnRequest;
import com.logistics.model.RequestStatus;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/**
 * Observer Pattern - Concrete Observer
 * 
 * Simulates sending email notifications when a return request
 * status changes. Maintains a log of all sent notifications.
 */
public class EmailNotifier implements StatusObserver {
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    private final List<String> notificationLog = new ArrayList<>();

    @Override
    public void update(ReturnRequest request, RequestStatus oldStatus, RequestStatus newStatus) {
        String timestamp = LocalDateTime.now().format(FORMATTER);
        String message = String.format(
            "[%s] 📧 EMAIL to %s: Request %s status changed from '%s' to '%s' (Product: %s)",
            timestamp,
            request.getCustomer().getEmail(),
            request.getRequestId(),
            oldStatus.getDisplayName(),
            newStatus.getDisplayName(),
            request.getProduct().getName());
        notificationLog.add(message);
        System.out.println(message);
    }

    @Override
    public String getObserverName() {
        return "Email Notifier";
    }

    /**
     * Get all notification log entries.
     * @return The notification log
     */
    public List<String> getNotificationLog() {
        return new ArrayList<>(notificationLog);
    }

    /**
     * Clear the notification log.
     */
    public void clearLog() {
        notificationLog.clear();
    }
}
