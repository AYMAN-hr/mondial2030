package com.mondial.ticket.service;

import java.util.ArrayList;
import java.util.List;

/**
 * Service de notifications pour l'application.
 */
public class NotificationService {

    private static NotificationService instance;
    private List<Notification> notifications = new ArrayList<>();
    private int unreadCount = 0;

    public static class Notification {
        private String message;
        private String type; // INFO, SUCCESS, WARNING, ERROR
        private String timestamp;
        private boolean read;

        public Notification(String message, String type) {
            this.message = message;
            this.type = type;
            this.timestamp = java.time.LocalDateTime.now()
                .format(java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss"));
            this.read = false;
        }

        public String getMessage() { return message; }
        public String getType() { return type; }
        public String getTimestamp() { return timestamp; }
        public boolean isRead() { return read; }
        public void setRead(boolean read) { this.read = read; }

        public String getIcon() {
            switch (type) {
                case "SUCCESS": return "✅";
                case "WARNING": return "⚠️";
                case "ERROR": return "❌";
                default: return "ℹ️";
            }
        }

        @Override
        public String toString() {
            return getIcon() + " [" + timestamp + "] " + message;
        }
    }

    private NotificationService() {}

    public static NotificationService getInstance() {
        if (instance == null) {
            instance = new NotificationService();
        }
        return instance;
    }

    public void addNotification(String message, String type) {
        Notification notif = new Notification(message, type);
        notifications.add(0, notif); // Add at beginning
        unreadCount++;

        // Keep only last 50 notifications
        if (notifications.size() > 50) {
            notifications.remove(notifications.size() - 1);
        }
    }

    public void info(String message) {
        addNotification(message, "INFO");
    }

    public void success(String message) {
        addNotification(message, "SUCCESS");
    }

    public void warning(String message) {
        addNotification(message, "WARNING");
    }

    public void error(String message) {
        addNotification(message, "ERROR");
    }

    public List<Notification> getAllNotifications() {
        return new ArrayList<>(notifications);
    }

    public int getUnreadCount() {
        return unreadCount;
    }

    public void markAllAsRead() {
        for (Notification n : notifications) {
            n.setRead(true);
        }
        unreadCount = 0;
    }

    public void clearAll() {
        notifications.clear();
        unreadCount = 0;
    }
}

