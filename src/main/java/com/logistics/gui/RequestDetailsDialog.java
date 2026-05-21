package com.logistics.gui;

import com.logistics.model.*;
import com.logistics.service.ReturnService;
import com.logistics.pattern.strategy.*;

import javax.swing.*;
import java.awt.*;

/**
 * Dialog showing request details. In admin mode, allows processing actions.
 * Demonstrates Strategy Pattern through refund method selection.
 */
public class RequestDetailsDialog extends JDialog {
    private final ReturnRequest request;
    private final ReturnService service;
    private final boolean isAdmin;

    private static final Color BG = new Color(241, 245, 249);
    private static final Color CARD_BG = Color.WHITE;
    private static final Color TEXT_DARK = new Color(15, 23, 42);
    private static final Color TEXT_SEC = new Color(100, 116, 139);
    private static final Color ACCENT = new Color(99, 102, 241);
    private static final Color SUCCESS = new Color(34, 197, 94);
    private static final Color DANGER = new Color(239, 68, 68);
    private static final Color INFO = new Color(59, 130, 246);

    public RequestDetailsDialog(MainFrame parent, ReturnRequest request, ReturnService service, boolean isAdmin) {
        super(parent, "Request Details - " + request.getRequestId(), true);
        this.request = request;
        this.service = service;
        this.isAdmin = isAdmin;
        setSize(550, 620);
        setLocationRelativeTo(parent);
        setResizable(false);
        getContentPane().setBackground(BG);
        initComponents();
    }

    private void initComponents() {
        JPanel main = new JPanel(new BorderLayout(0, 15));
        main.setBackground(BG);
        main.setBorder(BorderFactory.createEmptyBorder(20, 25, 20, 25));

        // Header
        JPanel header = new JPanel(new BorderLayout());
        header.setOpaque(false);
        JLabel titleLbl = new JLabel("Request " + request.getRequestId());
        titleLbl.setFont(new Font("Segoe UI", Font.BOLD, 22));
        titleLbl.setForeground(TEXT_DARK);

        JLabel statusLbl = new JLabel(request.getStatus().getDisplayName());
        statusLbl.setFont(new Font("Segoe UI", Font.BOLD, 14));
        statusLbl.setForeground(request.getStatus().getColor());
        statusLbl.setHorizontalAlignment(SwingConstants.RIGHT);

        header.add(titleLbl, BorderLayout.WEST);
        header.add(statusLbl, BorderLayout.EAST);
        main.add(header, BorderLayout.NORTH);

        // Details card
        JPanel card = new JPanel(new GridBagLayout()) {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(CARD_BG); g2.fillRoundRect(0, 0, getWidth(), getHeight(), 16, 16);
                g2.dispose();
            }
        };
        card.setOpaque(false);
        card.setBorder(BorderFactory.createEmptyBorder(20, 25, 20, 25));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 15);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        int row = 0;
        addDetail(card, gbc, row++, "Customer:", request.getCustomer().getName());
        addDetail(card, gbc, row++, "Email:", request.getCustomer().getEmail());
        addDetail(card, gbc, row++, "Phone:", request.getCustomer().getPhone());
        addSep(card, gbc, row++);
        addDetail(card, gbc, row++, "Product:", request.getProduct().getName());
        addDetail(card, gbc, row++, "Category:", request.getProduct().getCategory());
        addDetail(card, gbc, row++, "Unit Price:", "$" + String.format("%.2f", request.getProduct().getPrice()));
        addDetail(card, gbc, row++, "Quantity:", String.valueOf(request.getQuantity()));
        addSep(card, gbc, row++);
        addDetail(card, gbc, row++, "Return Type:", request.getReturnType());
        addDetail(card, gbc, row++, "Reason:", request.getReason());
        addDetail(card, gbc, row++, "Refund Amount:", "$" + String.format("%.2f", request.getRefundAmount()));
        addDetail(card, gbc, row++, "Refund Method:", request.getRefundMethod());
        addSep(card, gbc, row++);
        addDetail(card, gbc, row++, "Created:", request.getCreatedAtFormatted());
        addDetail(card, gbc, row++, "Updated:", request.getUpdatedAtFormatted());
        if (!request.getAdminNotes().isEmpty()) {
            addDetail(card, gbc, row++, "Admin Notes:", request.getAdminNotes());
        }

        JScrollPane sp = new JScrollPane(card);
        sp.setBorder(BorderFactory.createEmptyBorder());
        sp.getViewport().setBackground(BG);
        main.add(sp, BorderLayout.CENTER);

        // Action buttons
        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 5));
        btnPanel.setOpaque(false);

        if (isAdmin && request.getStatus() == RequestStatus.APPROVED) {
            // Strategy Pattern - Refund method selection
            JLabel strategyLabel = new JLabel("Refund Method (Strategy Pattern):");
            strategyLabel.setFont(new Font("Segoe UI", Font.BOLD, 12));
            strategyLabel.setForeground(ACCENT);
            btnPanel.add(strategyLabel);

            JButton bankBtn = styledBtn("Bank Transfer", INFO);
            bankBtn.addActionListener(e -> processRefund(new BankTransferRefund()));
            btnPanel.add(bankBtn);

            JButton creditBtn = styledBtn("Store Credit", SUCCESS);
            creditBtn.addActionListener(e -> processRefund(new StoreCreditRefund()));
            btnPanel.add(creditBtn);

            JButton origBtn = styledBtn("Original Payment", new Color(139, 92, 246));
            origBtn.addActionListener(e -> processRefund(new OriginalPaymentRefund()));
            btnPanel.add(origBtn);
        }

        JButton closeBtn = styledBtn("Close", new Color(71, 85, 105));
        closeBtn.addActionListener(e -> dispose());
        btnPanel.add(closeBtn);

        main.add(btnPanel, BorderLayout.SOUTH);
        setContentPane(main);
    }

    private void processRefund(RefundStrategy strategy) {
        String result = service.processRefund(request, strategy);
        JOptionPane.showMessageDialog(this, result, "Refund Processed - " + strategy.getMethodName(),
                JOptionPane.INFORMATION_MESSAGE);
        dispose();
    }

    private void addDetail(JPanel p, GridBagConstraints gbc, int row, String label, String value) {
        gbc.gridy = row;
        gbc.gridx = 0; gbc.weightx = 0;
        JLabel lbl = new JLabel(label);
        lbl.setFont(new Font("Segoe UI", Font.BOLD, 12));
        lbl.setForeground(TEXT_SEC);
        p.add(lbl, gbc);

        gbc.gridx = 1; gbc.weightx = 1;
        JLabel val = new JLabel("<html>" + value + "</html>");
        val.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        val.setForeground(TEXT_DARK);
        p.add(val, gbc);
    }

    private void addSep(JPanel p, GridBagConstraints gbc, int row) {
        gbc.gridy = row; gbc.gridx = 0; gbc.gridwidth = 2;
        JSeparator sep = new JSeparator();
        sep.setForeground(new Color(226, 232, 240));
        p.add(sep, gbc);
        gbc.gridwidth = 1;
    }

    private JButton styledBtn(String text, Color bg) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 11));
        btn.setForeground(Color.WHITE); btn.setBackground(bg);
        btn.setFocusPainted(false); btn.setBorderPainted(false);
        btn.setPreferredSize(new Dimension(130, 34));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return btn;
    }
}
