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
 * Updates the GUI dashboard when a return request status changes.
 * Uses a callback interface to communicate with Swing components.
 */
public class DashboardNotifier implements StatusObserver {
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    private final List<String> notificationLog = new ArrayList<>();
    private NotificationListener listener;

    /**
     * Callback interface for GUI updates.
     */
    public interface NotificationListener {
        void onNotification(String message, String type);
    }

    /**
     * Set the GUI notification listener.
     * @param listener The listener to receive notifications
     */
    public void setListener(NotificationListener listener) {
        this.listener = listener;
    }

    @Override
    public void update(ReturnRequest request, RequestStatus oldStatus, RequestStatus newStatus) {
        String timestamp = LocalDateTime.now().format(FORMATTER);
        String message = String.format(
            "[%s] 🖥 DASHBOARD: Request %s (%s) - %s → %s | Customer: %s",
            timestamp,
            request.getRequestId(),
            request.getProduct().getName(),
            oldStatus.getDisplayName(),
            newStatus.getDisplayName(),
            request.getCustomer().getName());
        notificationLog.add(message);
        System.out.println(message);

        // Notify GUI listener if registered
        if (listener != null) {
            String type = determineNotificationType(newStatus);
            listener.onNotification(message, type);
        }
    }

    private String determineNotificationType(RequestStatus status) {
        switch (status) {
            case APPROVED: return "success";
            case REJECTED: return "error";
            case REFUNDED: return "info";
            case COMPLETED: return "success";
            default: return "warning";
        }
    }

    @Override
    public String getObserverName() {
        return "Dashboard Notifier";
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
