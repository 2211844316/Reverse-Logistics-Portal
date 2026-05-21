package com.logistics.gui;

import com.logistics.model.*;
import com.logistics.service.ReturnService;

import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.util.List;

/**
 * Dashboard panel showing overview statistics and recent activity.
 */
public class DashboardPanel extends JPanel {
    private final ReturnService service;
    private JLabel totalLabel, pendingLabel, approvedLabel, refundedLabel, rejectedLabel, amountLabel;
    private JTable recentTable;
    private DefaultTableModel tableModel;

    private static final Color BG_COLOR = new Color(241, 245, 249);
    private static final Color CARD_BG = Color.WHITE;
    private static final Color TEXT_DARK = new Color(15, 23, 42);
    private static final Color TEXT_SECONDARY = new Color(100, 116, 139);
    private static final Color ACCENT = new Color(99, 102, 241);
    private static final Color SUCCESS = new Color(34, 197, 94);
    private static final Color WARNING = new Color(245, 158, 11);
    private static final Color DANGER = new Color(239, 68, 68);
    private static final Color INFO = new Color(59, 130, 246);

    public DashboardPanel(ReturnService service) {
        this.service = service;
        setBackground(BG_COLOR);
        setLayout(new BorderLayout(0, 20));
        setBorder(BorderFactory.createEmptyBorder(25, 25, 25, 25));
        initComponents();
    }

    private void initComponents() {
        // Header
        JLabel header = new JLabel("Dashboard Overview");
        header.setFont(new Font("Segoe UI", Font.BOLD, 24));
        header.setForeground(TEXT_DARK);
        add(header, BorderLayout.NORTH);

        // Stats cards
        JPanel statsPanel = new JPanel(new GridLayout(1, 6, 15, 0));
        statsPanel.setOpaque(false);

        totalLabel = new JLabel("0");
        pendingLabel = new JLabel("0");
        approvedLabel = new JLabel("0");
        refundedLabel = new JLabel("0");
        rejectedLabel = new JLabel("0");
        amountLabel = new JLabel("$0.00");

        statsPanel.add(createStatCard("Total Requests", totalLabel, "📊", ACCENT));
        statsPanel.add(createStatCard("Pending", pendingLabel, "⏳", WARNING));
        statsPanel.add(createStatCard("Approved", approvedLabel, "✅", SUCCESS));
        statsPanel.add(createStatCard("Refunded", refundedLabel, "💰", INFO));
        statsPanel.add(createStatCard("Rejected", rejectedLabel, "❌", DANGER));
        statsPanel.add(createStatCard("Total Refunded", amountLabel, "💵", new Color(16, 185, 129)));

        // Center panel with stats and table
        JPanel centerPanel = new JPanel(new BorderLayout(0, 20));
        centerPanel.setOpaque(false);
        centerPanel.add(statsPanel, BorderLayout.NORTH);

        // Recent requests table
        JPanel tablePanel = createTablePanel();
        centerPanel.add(tablePanel, BorderLayout.CENTER);

        add(centerPanel, BorderLayout.CENTER);
        refreshData();
    }

    private JPanel createStatCard(String title, JLabel valueLabel, String icon, Color accentColor) {
        JPanel card = new JPanel(new BorderLayout(8, 4)) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(CARD_BG);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 16, 16);
                // Left accent bar
                g2.setColor(accentColor);
                g2.fillRoundRect(0, 0, 4, getHeight(), 4, 4);
                g2.dispose();
            }
        };
        card.setOpaque(false);
        card.setBorder(BorderFactory.createEmptyBorder(16, 18, 16, 16));

        JLabel iconLabel = new JLabel(icon);
        iconLabel.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 24));

        JPanel textPanel = new JPanel(new BorderLayout(0, 2));
        textPanel.setOpaque(false);

        JLabel titleLbl = new JLabel(title);
        titleLbl.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        titleLbl.setForeground(TEXT_SECONDARY);

        valueLabel.setFont(new Font("Segoe UI", Font.BOLD, 22));
        valueLabel.setForeground(TEXT_DARK);

        textPanel.add(titleLbl, BorderLayout.NORTH);
        textPanel.add(valueLabel, BorderLayout.CENTER);

        card.add(iconLabel, BorderLayout.WEST);
        card.add(textPanel, BorderLayout.CENTER);

        return card;
    }

    private JPanel createTablePanel() {
        JPanel panel = new JPanel(new BorderLayout(0, 10)) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(CARD_BG);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 16, 16);
                g2.dispose();
            }
        };
        panel.setOpaque(false);
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel tableTitle = new JLabel("Recent Return Requests");
        tableTitle.setFont(new Font("Segoe UI", Font.BOLD, 16));
        tableTitle.setForeground(TEXT_DARK);
        panel.add(tableTitle, BorderLayout.NORTH);

        String[] columns = {"Request ID", "Customer", "Product", "Type", "Status", "Amount", "Date"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        recentTable = new JTable(tableModel);
        styleTable(recentTable);

        JScrollPane scrollPane = new JScrollPane(recentTable);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.getViewport().setBackground(CARD_BG);
        panel.add(scrollPane, BorderLayout.CENTER);

        return panel;
    }

    private void styleTable(JTable table) {
        table.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        table.setRowHeight(38);
        table.setShowGrid(false);
        table.setIntercellSpacing(new Dimension(0, 0));
        table.setSelectionBackground(new Color(99, 102, 241, 30));
        table.setSelectionForeground(TEXT_DARK);
        table.setBackground(CARD_BG);
        table.setForeground(TEXT_DARK);

        // Header styling
        JTableHeader header = table.getTableHeader();
        header.setFont(new Font("Segoe UI", Font.BOLD, 12));
        header.setBackground(new Color(248, 250, 252));
        header.setForeground(TEXT_SECONDARY);
        header.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(226, 232, 240)));
        header.setPreferredSize(new Dimension(header.getWidth(), 40));

        // Status column renderer
        table.getColumnModel().getColumn(4).setCellRenderer(new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value,
                    boolean isSelected, boolean hasFocus, int row, int column) {
                JLabel label = (JLabel) super.getTableCellRendererComponent(
                        table, value, isSelected, hasFocus, row, column);
                label.setHorizontalAlignment(SwingConstants.CENTER);
                label.setFont(new Font("Segoe UI", Font.BOLD, 11));
                if (value != null) {
                    String status = value.toString();
                    switch (status) {
                        case "Pending": label.setForeground(WARNING); break;
                        case "Approved": label.setForeground(SUCCESS); break;
                        case "Rejected": label.setForeground(DANGER); break;
                        case "Refunded": label.setForeground(INFO); break;
                        case "Completed": label.setForeground(new Color(139, 92, 246)); break;
                        default: label.setForeground(TEXT_DARK);
                    }
                }
                if (isSelected) {
                    label.setBackground(new Color(99, 102, 241, 30));
                } else {
                    label.setBackground(CARD_BG);
                }
                return label;
            }
        });
    }

    public void refreshData() {
        totalLabel.setText(String.valueOf(service.getTotalRequests()));
        pendingLabel.setText(String.valueOf(service.getRequestCountByStatus(RequestStatus.PENDING)));
        approvedLabel.setText(String.valueOf(service.getRequestCountByStatus(RequestStatus.APPROVED)));
        refundedLabel.setText(String.valueOf(service.getRequestCountByStatus(RequestStatus.REFUNDED)));
        rejectedLabel.setText(String.valueOf(service.getRequestCountByStatus(RequestStatus.REJECTED)));
        amountLabel.setText(String.format("$%.2f", service.getTotalRefundAmount()));

        tableModel.setRowCount(0);
        List<ReturnRequest> requests = service.getAllRequests();
        for (ReturnRequest r : requests) {
            tableModel.addRow(new Object[]{
                r.getRequestId(),
                r.getCustomer().getName(),
                r.getProduct().getName(),
                r.getReturnType(),
                r.getStatus().getDisplayName(),
                String.format("$%.2f", r.getRefundAmount()),
                r.getCreatedAtFormatted()
            });
        }
    }
}
