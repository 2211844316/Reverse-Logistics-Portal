package com.logistics.gui;

import com.logistics.model.*;
import com.logistics.service.ReturnService;

import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.util.List;

/**
 * Admin panel for managing all return requests.
 * Supports filtering, approving, rejecting, and processing refunds.
 */
public class AdminPanel extends JPanel {
    private final MainFrame mainFrame;
    private final ReturnService service;
    private JTable requestTable;
    private DefaultTableModel tableModel;
    private JComboBox<String> filterCombo;

    private static final Color BG_COLOR = new Color(241, 245, 249);
    private static final Color CARD_BG = Color.WHITE;
    private static final Color TEXT_DARK = new Color(15, 23, 42);
    private static final Color TEXT_SECONDARY = new Color(100, 116, 139);
    private static final Color ACCENT = new Color(99, 102, 241);
    private static final Color SUCCESS = new Color(34, 197, 94);
    private static final Color WARNING = new Color(245, 158, 11);
    private static final Color DANGER = new Color(239, 68, 68);
    private static final Color INFO = new Color(59, 130, 246);

    public AdminPanel(MainFrame mainFrame, ReturnService service) {
        this.mainFrame = mainFrame;
        this.service = service;
        setBackground(BG_COLOR);
        setLayout(new BorderLayout(0, 15));
        setBorder(BorderFactory.createEmptyBorder(25, 25, 25, 25));
        initComponents();
    }

    private void initComponents() {
        // Header
        JPanel headerPanel = new JPanel(new BorderLayout(15, 0));
        headerPanel.setOpaque(false);

        JLabel titleLabel = new JLabel("Manage Return Requests");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        titleLabel.setForeground(TEXT_DARK);

        // Filter panel
        JPanel filterPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        filterPanel.setOpaque(false);

        JLabel filterLabel = new JLabel("Filter by Status:");
        filterLabel.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        filterLabel.setForeground(TEXT_SECONDARY);

        filterCombo = new JComboBox<>(new String[]{"All", "Pending", "Approved", "Rejected", "Refunded", "Completed"});
        filterCombo.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        filterCombo.setPreferredSize(new Dimension(150, 34));
        filterCombo.addActionListener(e -> refreshData());

        filterPanel.add(filterLabel);
        filterPanel.add(filterCombo);

        headerPanel.add(titleLabel, BorderLayout.WEST);
        headerPanel.add(filterPanel, BorderLayout.EAST);
        add(headerPanel, BorderLayout.NORTH);

        // Table
        JPanel tablePanel = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(CARD_BG);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 16, 16);
                g2.dispose();
            }
        };
        tablePanel.setOpaque(false);
        tablePanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        String[] columns = {"Request ID", "Customer", "Product", "Type", "Reason", "Status", "Amount", "Date"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        requestTable = new JTable(tableModel);
        styleTable(requestTable);

        requestTable.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                if (e.getClickCount() == 2) {
                    viewSelectedRequest();
                }
            }
        });

        JScrollPane scrollPane = new JScrollPane(requestTable);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.getViewport().setBackground(CARD_BG);
        tablePanel.add(scrollPane, BorderLayout.CENTER);

        // Action buttons
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        buttonPanel.setOpaque(false);

        JButton viewBtn = createStyledButton("View Details", new Color(71, 85, 105));
        JButton approveBtn = createStyledButton("✓ Approve", SUCCESS);
        JButton rejectBtn = createStyledButton("✗ Reject", DANGER);
        JButton refundBtn = createStyledButton("💰 Process Refund", INFO);
        JButton completeBtn = createStyledButton("Complete", new Color(139, 92, 246));
        JButton refreshBtn = createStyledButton("↻ Refresh", new Color(71, 85, 105));

        viewBtn.addActionListener(e -> viewSelectedRequest());
        approveBtn.addActionListener(e -> approveSelected());
        rejectBtn.addActionListener(e -> rejectSelected());
        refundBtn.addActionListener(e -> refundSelected());
        completeBtn.addActionListener(e -> completeSelected());
        refreshBtn.addActionListener(e -> refreshData());

        buttonPanel.add(viewBtn);
        buttonPanel.add(approveBtn);
        buttonPanel.add(rejectBtn);
        buttonPanel.add(refundBtn);
        buttonPanel.add(completeBtn);
        buttonPanel.add(refreshBtn);

        tablePanel.add(buttonPanel, BorderLayout.SOUTH);
        add(tablePanel, BorderLayout.CENTER);

        refreshData();
    }

    private void styleTable(JTable table) {
        table.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        table.setRowHeight(40);
        table.setShowGrid(false);
        table.setIntercellSpacing(new Dimension(0, 0));
        table.setSelectionBackground(new Color(99, 102, 241, 30));
        table.setSelectionForeground(TEXT_DARK);
        table.setBackground(CARD_BG);
        table.setForeground(TEXT_DARK);

        JTableHeader header = table.getTableHeader();
        header.setFont(new Font("Segoe UI", Font.BOLD, 12));
        header.setBackground(new Color(248, 250, 252));
        header.setForeground(TEXT_SECONDARY);
        header.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(226, 232, 240)));
        header.setPreferredSize(new Dimension(header.getWidth(), 42));

        // Status column renderer
        table.getColumnModel().getColumn(5).setCellRenderer(new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value,
                    boolean isSelected, boolean hasFocus, int row, int column) {
                JLabel label = (JLabel) super.getTableCellRendererComponent(
                        table, value, isSelected, hasFocus, row, column);
                label.setHorizontalAlignment(SwingConstants.CENTER);
                label.setFont(new Font("Segoe UI", Font.BOLD, 11));
                if (value != null) {
                    switch (value.toString()) {
                        case "Pending": label.setForeground(WARNING); break;
                        case "Approved": label.setForeground(SUCCESS); break;
                        case "Rejected": label.setForeground(DANGER); break;
                        case "Refunded": label.setForeground(INFO); break;
                        case "Completed": label.setForeground(new Color(139, 92, 246)); break;
                        default: label.setForeground(TEXT_DARK);
                    }
                }
                label.setBackground(isSelected ? new Color(99, 102, 241, 30) : CARD_BG);
                return label;
            }
        });
    }

    public void refreshData() {
        tableModel.setRowCount(0);
        String filter = (String) filterCombo.getSelectedItem();
        List<ReturnRequest> requests;

        if ("All".equals(filter)) {
            requests = service.getAllRequests();
        } else {
            RequestStatus status = RequestStatus.valueOf(filter.toUpperCase());
            requests = service.getRequestsByStatus(status);
        }

        for (ReturnRequest r : requests) {
            tableModel.addRow(new Object[]{
                r.getRequestId(),
                r.getCustomer().getName(),
                r.getProduct().getName(),
                r.getReturnType(),
                r.getReason().length() > 30 ? r.getReason().substring(0, 30) + "..." : r.getReason(),
                r.getStatus().getDisplayName(),
                String.format("$%.2f", r.getRefundAmount()),
                r.getCreatedAtFormatted()
            });
        }
    }

    private ReturnRequest getSelectedRequest() {
        int selectedRow = requestTable.getSelectedRow();
        if (selectedRow < 0) {
            JOptionPane.showMessageDialog(this, "Please select a request first.",
                    "No Selection", JOptionPane.INFORMATION_MESSAGE);
            return null;
        }
        String requestId = (String) tableModel.getValueAt(selectedRow, 0);
        return service.getAllRequests().stream()
                .filter(r -> r.getRequestId().equals(requestId))
                .findFirst().orElse(null);
    }

    private void viewSelectedRequest() {
        ReturnRequest request = getSelectedRequest();
        if (request != null) {
            RequestDetailsDialog dialog = new RequestDetailsDialog(mainFrame, request, service, true);
            dialog.setVisible(true);
            refreshData();
            mainFrame.refreshAllPanels();
        }
    }

    private void approveSelected() {
        ReturnRequest request = getSelectedRequest();
        if (request == null) return;
        if (request.getStatus() != RequestStatus.PENDING) {
            JOptionPane.showMessageDialog(this, "Only pending requests can be approved.",
                    "Invalid Action", JOptionPane.WARNING_MESSAGE);
            return;
        }
        String notes = JOptionPane.showInputDialog(this, "Admin notes (optional):", "Approve Request", JOptionPane.PLAIN_MESSAGE);
        if (notes != null) {
            service.approveRequest(request, notes);
            refreshData();
            mainFrame.refreshAllPanels();
            JOptionPane.showMessageDialog(this, "Request " + request.getRequestId() + " approved.",
                    "Success", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void rejectSelected() {
        ReturnRequest request = getSelectedRequest();
        if (request == null) return;
        if (request.getStatus() != RequestStatus.PENDING) {
            JOptionPane.showMessageDialog(this, "Only pending requests can be rejected.",
                    "Invalid Action", JOptionPane.WARNING_MESSAGE);
            return;
        }
        String notes = JOptionPane.showInputDialog(this, "Rejection reason:", "Reject Request", JOptionPane.PLAIN_MESSAGE);
        if (notes != null) {
            service.rejectRequest(request, notes);
            refreshData();
            mainFrame.refreshAllPanels();
            JOptionPane.showMessageDialog(this, "Request " + request.getRequestId() + " rejected.",
                    "Done", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void refundSelected() {
        ReturnRequest request = getSelectedRequest();
        if (request == null) return;
        if (request.getStatus() != RequestStatus.APPROVED) {
            JOptionPane.showMessageDialog(this, "Only approved requests can be refunded.\nPlease approve the request first.",
                    "Invalid Action", JOptionPane.WARNING_MESSAGE);
            return;
        }
        // Open details dialog in admin mode for refund processing
        RequestDetailsDialog dialog = new RequestDetailsDialog(mainFrame, request, service, true);
        dialog.setVisible(true);
        refreshData();
        mainFrame.refreshAllPanels();
    }

    private void completeSelected() {
        ReturnRequest request = getSelectedRequest();
        if (request == null) return;
        if (request.getStatus() != RequestStatus.REFUNDED) {
            JOptionPane.showMessageDialog(this, "Only refunded requests can be completed.",
                    "Invalid Action", JOptionPane.WARNING_MESSAGE);
            return;
        }
        service.completeRequest(request);
        refreshData();
        mainFrame.refreshAllPanels();
        JOptionPane.showMessageDialog(this, "Request " + request.getRequestId() + " marked as completed.",
                "Success", JOptionPane.INFORMATION_MESSAGE);
    }

    private JButton createStyledButton(String text, Color bgColor) {
        JButton button = new JButton(text) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(getModel().isPressed() ? bgColor.darker() :
                            getModel().isRollover() ? bgColor.brighter() : bgColor);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 10, 10);
                g2.dispose();
                super.paintComponent(g);
            }
        };
        button.setOpaque(false);
        button.setContentAreaFilled(false);
        button.setBorderPainted(false);
        button.setFocusPainted(false);
        button.setForeground(Color.WHITE);
        button.setFont(new Font("Segoe UI", Font.BOLD, 12));
        button.setPreferredSize(new Dimension(145, 36));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return button;
    }
}
