package com.logistics.gui;

import com.logistics.model.Customer;
import com.logistics.service.ReturnService;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * Main application frame with sidebar navigation and card-based content.
 */
public class MainFrame extends JFrame {
    private final ReturnService service;
    private CardLayout mainCardLayout;
    private JPanel mainCardPanel;
    private CardLayout contentCardLayout;
    private JPanel contentCardPanel;

    private DashboardPanel dashboardPanel;
    private CustomerPanel customerPanel;
    private AdminPanel adminPanel;
    private NotificationPanel notificationPanel;
    private SettingsPanel settingsPanel;

    private JPanel sidebarPanel;
    private JLabel userInfoLabel;
    private Customer currentCustomer;

    private static final Color SIDEBAR_BG = new Color(15, 23, 42);
    private static final Color SIDEBAR_HOVER = new Color(30, 41, 59);
    private static final Color SIDEBAR_ACTIVE = new Color(99, 102, 241);
    private static final Color SIDEBAR_TEXT = new Color(203, 213, 225);
    private static final Color SIDEBAR_TEXT_ACTIVE = Color.WHITE;
    private static final Color CONTENT_BG = new Color(241, 245, 249);

    private JPanel activeNavButton = null;

    public MainFrame() {
        service = new ReturnService();
        setTitle("Reverse Logistics Portal");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1200, 750);
        setMinimumSize(new Dimension(1000, 650));
        setLocationRelativeTo(null);
        initComponents();
    }

    private void initComponents() {
        mainCardLayout = new CardLayout();
        mainCardPanel = new JPanel(mainCardLayout);

        // App panel (sidebar + content)
        JPanel appPanel = new JPanel(new BorderLayout());
        sidebarPanel = createSidebar();
        appPanel.add(sidebarPanel, BorderLayout.WEST);

        contentCardLayout = new CardLayout();
        contentCardPanel = new JPanel(contentCardLayout);
        contentCardPanel.setBackground(CONTENT_BG);

        dashboardPanel = new DashboardPanel(service);
        customerPanel = new CustomerPanel(this, service);
        adminPanel = new AdminPanel(this, service);
        notificationPanel = new NotificationPanel(service);
        settingsPanel = new SettingsPanel();

        contentCardPanel.add(dashboardPanel, "dashboard");
        contentCardPanel.add(customerPanel, "customer");
        contentCardPanel.add(adminPanel, "admin");
        contentCardPanel.add(notificationPanel, "notifications");
        contentCardPanel.add(settingsPanel, "settings");

        appPanel.add(contentCardPanel, BorderLayout.CENTER);
        mainCardPanel.add(appPanel, "app");

        setContentPane(mainCardPanel);
        
        // Skip login and show app directly with default customer assigned
        currentCustomer = service.getAllCustomers().get(0);
        userInfoLabel.setText("  👤 Demo User: " + currentCustomer.getName());
        customerPanel.setCustomer(currentCustomer);
        
        buildNavItems();
        mainCardLayout.show(mainCardPanel, "app");
        showContent("dashboard");
        if (sidebarPanel.getComponentCount() > 4) {
            Component c = sidebarPanel.getComponent(4);
            if (c instanceof JPanel) setActiveNav((JPanel) c);
        }
    }

    private JPanel createSidebar() {
        JPanel sidebar = new JPanel();
        sidebar.setLayout(new BoxLayout(sidebar, BoxLayout.Y_AXIS));
        sidebar.setBackground(SIDEBAR_BG);
        sidebar.setPreferredSize(new Dimension(240, 0));
        sidebar.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));

        // Logo section
        JPanel logoPanel = new JPanel(new BorderLayout());
        logoPanel.setOpaque(false);
        logoPanel.setBorder(BorderFactory.createEmptyBorder(25, 20, 25, 20));
        logoPanel.setMaximumSize(new Dimension(240, 90));

        Icon boxIcon = new Icon() {
            @Override
            public void paintIcon(Component c, Graphics g, int x, int y) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                // Base Orange Color
                g2.setColor(new Color(255, 107, 53)); 
                int[] xs = {x+14, x+28, x+28, x+14, x, x};
                int[] ys = {y, y+7, y+21, y+28, y+21, y+7};
                g2.fillPolygon(xs, ys, 6);
                
                // Top Highlight
                g2.setColor(new Color(255, 255, 255, 80)); 
                int[] topXs = {x+14, x+28, x+14, x};
                int[] topYs = {y, y+7, y+14, y+7};
                g2.fillPolygon(topXs, topYs, 4);
                
                // Right Shadow
                g2.setColor(new Color(0, 0, 0, 40)); 
                int[] rightXs = {x+14, x+28, x+28, x+14};
                int[] rightYs = {y+14, y+7, y+21, y+28};
                g2.fillPolygon(rightXs, rightYs, 4);
                g2.dispose();
            }
            @Override public int getIconWidth() { return 28; }
            @Override public int getIconHeight() { return 28; }
        };
        JLabel logoIcon = new JLabel(boxIcon);
        // Add spacing between the box icon and the text
        logoIcon.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 8));
        JLabel logoText = new JLabel("  RLP");
        logoText.setFont(new Font("Segoe UI", Font.BOLD, 22));
        // Changing logo text color to an attractive orange (#FF6B35)
        logoText.setForeground(new Color(255, 107, 53));

        JPanel logoRow = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        logoRow.setOpaque(false);
        logoRow.add(logoIcon);
        logoRow.add(logoText);
        logoPanel.add(logoRow, BorderLayout.CENTER);

        sidebar.add(logoPanel);
        sidebar.add(createSeparator());

        // User info
        userInfoLabel = new JLabel("  Not logged in");
        userInfoLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        userInfoLabel.setForeground(new Color(148, 163, 184));
        userInfoLabel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        userInfoLabel.setMaximumSize(new Dimension(240, 35));
        sidebar.add(userInfoLabel);
        sidebar.add(Box.createVerticalStrut(10));

        return sidebar;
    }

    private void buildNavItems() {
        // Remove old nav items (keep logo, separator, userInfo, strut = first 4)
        while (sidebarPanel.getComponentCount() > 4) {
            sidebarPanel.remove(4);
        }

        addNavItem("📊", "Dashboard", "dashboard");
        addNavItem("🛒", "My Requests (Customer)", "customer");
        addNavItem("⚙", "Manage Requests (Admin)", "admin");
        addNavItem("🔔", "Notifications", "notifications");
        addNavItem("⚙", "Settings", "settings");

        sidebarPanel.add(Box.createVerticalGlue());
        sidebarPanel.add(Box.createVerticalStrut(15));

        sidebarPanel.revalidate();
        sidebarPanel.repaint();
    }

    private void addNavItem(String icon, String label, String cardName) {
        JPanel btn = createNavButton(icon, label);
        btn.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                showContent(cardName);
                setActiveNav(btn);
            }
            @Override
            public void mouseEntered(MouseEvent e) {
                if (btn != activeNavButton) btn.setBackground(SIDEBAR_HOVER);
            }
            @Override
            public void mouseExited(MouseEvent e) {
                if (btn != activeNavButton) btn.setBackground(SIDEBAR_BG);
            }
        });
        sidebarPanel.add(btn);
    }

    private JPanel createNavButton(String icon, String label) {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 0));
        panel.setBackground(SIDEBAR_BG);
        panel.setMaximumSize(new Dimension(240, 44));
        panel.setPreferredSize(new Dimension(240, 44));
        panel.setCursor(new Cursor(Cursor.HAND_CURSOR));
        panel.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));

        JLabel iconLbl = new JLabel(icon);
        iconLbl.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 16));
        JLabel textLbl = new JLabel(label);
        textLbl.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        textLbl.setForeground(SIDEBAR_TEXT);

        panel.add(iconLbl);
        panel.add(textLbl);
        return panel;
    }

    private void setActiveNav(JPanel btn) {
        if (activeNavButton != null) {
            activeNavButton.setBackground(SIDEBAR_BG);
            for (Component c : activeNavButton.getComponents()) {
                if (c instanceof JLabel) ((JLabel) c).setForeground(SIDEBAR_TEXT);
            }
        }
        activeNavButton = btn;
        btn.setBackground(SIDEBAR_ACTIVE);
        for (Component c : btn.getComponents()) {
            if (c instanceof JLabel) ((JLabel) c).setForeground(SIDEBAR_TEXT_ACTIVE);
        }
    }

    private void showContent(String name) {
        contentCardLayout.show(contentCardPanel, name);
        if ("dashboard".equals(name)) dashboardPanel.refreshData();
        else if ("notifications".equals(name)) notificationPanel.loadNotifications();
        else if ("customer".equals(name)) customerPanel.refreshData();
        else if ("admin".equals(name)) adminPanel.refreshData();
    }

    private JSeparator createSeparator() {
        JSeparator sep = new JSeparator();
        sep.setMaximumSize(new Dimension(240, 1));
        sep.setForeground(new Color(51, 65, 85));
        return sep;
    }

    // --- Public methods for panel switching ---

    public void refreshAllPanels() {
        dashboardPanel.refreshData();
        notificationPanel.loadNotifications();
        if (currentCustomer != null) customerPanel.refreshData();
        adminPanel.refreshData();
    }
}
