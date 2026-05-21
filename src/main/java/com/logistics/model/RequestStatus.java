package com.logistics.model;

import java.awt.Color;

/**
 * Enum representing the possible statuses of a return request.
 * Each status has a display name and associated color for GUI rendering.
 */
public enum RequestStatus {
    PENDING("Pending", new Color(255, 165, 0)),
    APPROVED("Approved", new Color(76, 175, 80)),
    REJECTED("Rejected", new Color(244, 67, 54)),
    REFUNDED("Refunded", new Color(33, 150, 243)),
    COMPLETED("Completed", new Color(156, 39, 176));

    private final String displayName;
    private final Color color;

    RequestStatus(String displayName, Color color) {
        this.displayName = displayName;
        this.color = color;
    }

    public String getDisplayName() {
        return displayName;
    }

    public Color getColor() {
        return color;
    }

    @Override
    public String toString() {
        return displayName;
    }
}
