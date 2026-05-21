package com.logistics.gui;

import com.logistics.model.*;
import com.logistics.service.ReturnService;

import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.util.List;

/**
 * Customer panel for viewing and managing personal return requests.
 */
public class CustomerPanel extends JPanel {
    private final MainFrame mainFrame;
    private final ReturnService service;
    private Customer currentCustomer;
    private JTable requestTable;
    private DefaultTableModel tableModel;
    private JLabel welcomeLabel;

    private static final Color BG_COLOR = new Color(241, 245, 249);
    private static final Color CARD_BG = Color.WHITE;
    private static final Color TEXT_DARK = new Color(15, 23, 42);
    private static final Color TEXT_SECONDARY = new Color(100, 116, 139);
    private static final Color ACCENT = new Color(99, 102, 241);
    private static final Color ACCENT_HOVER = new Color(129, 140, 248);
    private static final Color SUCCESS = new Color(34, 197, 94);
    private static final Color WARNING = new Color(245, 158, 11);
    private static final Color DANGER = new Color(239, 68, 68);
    private static final Color INFO = new Color(59, 130, 246);

    public CustomerPanel(MainFrame mainFrame, ReturnService service) {
        this.mainFrame = mainFrame;
        this.service = service;
        setBackground(BG_COLOR);
        setLayout(new BorderLayout(0, 20));
        setBorder(BorderFactory.createEmptyBorder(25, 25, 25, 25));
        initComponents();
    }

    private void initComponents() {
        // Header panel
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setOpaque(false);

        welcomeLabel = new JLabel("My Return Requests");
        welcomeLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        welcomeLabel.setForeground(TEXT_DARK);

        JButton newRequestBtn = createStyledButton("+ New Return Request", ACCENT);
        newRequestBtn.addActionListener(e -> openNewRequestDialog());

        headerPanel.add(welcomeLabel, BorderLayout.WEST);
        headerPanel.add(newRequestBtn, BorderLayout.EAST);
        add(headerPanel, BorderLayout.NORTH);

        // Table panel
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
        tablePanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        String[] columns = {"Request ID", "Product", "Type", "Reason", "Status", "Amount", "Date"};
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

        // Bottom buttons
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        buttonPanel.setOpaque(false);

        JButton viewBtn = createStyledButton("View Details", new Color(71, 85, 105));
        viewBtn.addActionListener(e -> viewSelectedRequest());
        JButton refreshBtn = createStyledButton("Refresh", INFO);
        refreshBtn.addActionListener(e -> refreshData());

        buttonPanel.add(viewBtn);
        buttonPanel.add(refreshBtn);
        tablePanel.add(buttonPanel, BorderLayout.SOUTH);

        add(tablePanel, BorderLayout.CENTER);
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

        // Status renderer
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
                label.setBackground(isSelected ? new Color(99, 102, 241, 30) : CARD_BG);
                return label;
            }
        });
    }

    public void setCustomer(Customer customer) {
        this.currentCustomer = customer;
        welcomeLabel.setText("Welcome, " + customer.getName() + " — My Return Requests");
        refreshData();
    }

    public void refreshData() {
        if (currentCustomer == null) return;
        tableModel.setRowCount(0);
        List<ReturnRequest> requests = service.getRequestsByCustomer(currentCustomer);
        for (ReturnRequest r : requests) {
            tableModel.addRow(new Object[]{
                r.getRequestId(),
                r.getProduct().getName(),
                r.getReturnType(),
                r.getReason().length() > 40 ? r.getReason().substring(0, 40) + "..." : r.getReason(),
                r.getStatus().getDisplayName(),
                String.format("$%.2f", r.getRefundAmount()),
                r.getCreatedAtFormatted()
            });
        }
    }

    private void viewSelectedRequest() {
        int selectedRow = requestTable.getSelectedRow();
        if (selectedRow < 0) {
            JOptionPane.showMessageDialog(this, "Please select a request to view.",
                    "No Selection", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        String requestId = (String) tableModel.getValueAt(selectedRow, 0);
        ReturnRequest request = service.getAllRequests().stream()
                .filter(r -> r.getRequestId().equals(requestId))
                .findFirst().orElse(null);
        if (request != null) {
            new RequestDetailsDialog(mainFrame, request, service, false).setVisible(true);
        }
    }

    private void openNewRequestDialog() {
        if (currentCustomer == null) return;
        new NewRequestDialog(mainFrame, service, currentCustomer, this).setVisible(true);
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
        button.setFont(new Font("Segoe UI", Font.BOLD, 13));
        button.setPreferredSize(new Dimension(180, 38));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return button;
    }
}
