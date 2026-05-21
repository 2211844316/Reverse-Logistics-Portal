package com.logistics.gui;

import com.logistics.service.ReturnService;
import com.logistics.pattern.observer.DashboardNotifier;

import javax.swing.*;
import java.awt.*;
import java.util.List;

/**
 * Notification panel displaying a live feed of all system notifications.
 */
public class NotificationPanel extends JPanel implements DashboardNotifier.NotificationListener {
    private final ReturnService service;
    private JPanel notificationListPanel;
    private JLabel countLabel;

    private static final Color BG = new Color(241, 245, 249);
    private static final Color CARD_BG = Color.WHITE;
    private static final Color TEXT_DARK = new Color(15, 23, 42);
    private static final Color TEXT_SEC = new Color(100, 116, 139);

    public NotificationPanel(ReturnService service) {
        this.service = service;
        setBackground(BG);
        setLayout(new BorderLayout(0, 15));
        setBorder(BorderFactory.createEmptyBorder(25, 25, 25, 25));
        initComponents();
        service.getDashboardNotifier().setListener(this);
    }

    private void initComponents() {
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setOpaque(false);
        JLabel titleLabel = new JLabel("Notifications");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        titleLabel.setForeground(TEXT_DARK);

        JPanel rightPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        rightPanel.setOpaque(false);
        countLabel = new JLabel("0 notifications");
        countLabel.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        countLabel.setForeground(TEXT_SEC);

        JButton clearBtn = new JButton("Clear All");
        styleBtn(clearBtn, new Color(239, 68, 68));
        clearBtn.addActionListener(e -> { service.getEmailNotifier().clearLog(); service.getSmsNotifier().clearLog(); service.getDashboardNotifier().clearLog(); loadNotifications(); });

        JButton refreshBtn = new JButton("Refresh");
        styleBtn(refreshBtn, new Color(99, 102, 241));
        refreshBtn.addActionListener(e -> loadNotifications());

        rightPanel.add(countLabel);
        rightPanel.add(refreshBtn);
        rightPanel.add(clearBtn);
        headerPanel.add(titleLabel, BorderLayout.WEST);
        headerPanel.add(rightPanel, BorderLayout.EAST);
        add(headerPanel, BorderLayout.NORTH);

        notificationListPanel = new JPanel();
        notificationListPanel.setLayout(new BoxLayout(notificationListPanel, BoxLayout.Y_AXIS));
        notificationListPanel.setBackground(BG);
        JScrollPane sp = new JScrollPane(notificationListPanel);
        sp.setBorder(BorderFactory.createEmptyBorder());
        sp.getViewport().setBackground(BG);
        sp.getVerticalScrollBar().setUnitIncrement(16);
        add(sp, BorderLayout.CENTER);
        loadNotifications();
    }

    public void loadNotifications() {
        notificationListPanel.removeAll();
        int count = 0;
        for (String log : service.getDashboardNotifier().getNotificationLog()) { addCard(log, "info"); count++; }
        for (String log : service.getEmailNotifier().getNotificationLog()) { addCard(log, "success"); count++; }
        for (String log : service.getSmsNotifier().getNotificationLog()) { addCard(log, "warning"); count++; }
        if (count == 0) {
            JLabel empty = new JLabel("No notifications yet", SwingConstants.CENTER);
            empty.setFont(new Font("Segoe UI", Font.PLAIN, 16));
            empty.setForeground(TEXT_SEC);
            notificationListPanel.add(empty);
        }
        countLabel.setText(count + " notification" + (count != 1 ? "s" : ""));
        notificationListPanel.revalidate();
        notificationListPanel.repaint();
    }

    private void addCard(String msg, String type) {
        Color bg = type.equals("success") ? new Color(220,252,231) : type.equals("error") ? new Color(254,226,226) : type.equals("info") ? new Color(219,234,254) : new Color(254,249,195);
        Color border = type.equals("success") ? new Color(34,197,94) : type.equals("error") ? new Color(239,68,68) : type.equals("info") ? new Color(59,130,246) : new Color(245,158,11);
        JPanel card = new JPanel(new BorderLayout(10,0)) {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2=(Graphics2D)g.create(); g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(bg); g2.fillRoundRect(0,0,getWidth(),getHeight(),12,12);
                g2.setColor(border); g2.fillRoundRect(0,0,4,getHeight(),4,4); g2.dispose();
            }
        };
        card.setOpaque(false);
        card.setBorder(BorderFactory.createEmptyBorder(10,14,10,14));
        card.setMaximumSize(new Dimension(Integer.MAX_VALUE, 60));
        JLabel lbl = new JLabel("<html><body style='width:600px'>"+msg.replace("<","&lt;")+"</body></html>");
        lbl.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lbl.setForeground(TEXT_DARK);
        card.add(lbl, BorderLayout.CENTER);
        notificationListPanel.add(card);
        notificationListPanel.add(Box.createVerticalStrut(6));
    }

    @Override public void onNotification(String message, String type) { SwingUtilities.invokeLater(this::loadNotifications); }

    private void styleBtn(JButton btn, Color c) {
        btn.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btn.setForeground(Color.WHITE); btn.setBackground(c);
        btn.setFocusPainted(false); btn.setBorderPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setPreferredSize(new Dimension(100, 32));
    }
}
