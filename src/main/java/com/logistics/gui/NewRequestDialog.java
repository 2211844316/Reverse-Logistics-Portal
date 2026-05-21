package com.logistics.gui;

import com.logistics.model.*;
import com.logistics.service.ReturnService;

import javax.swing.*;
import java.awt.*;
import java.util.List;

/**
 * Dialog for creating a new return request.
 */
public class NewRequestDialog extends JDialog {
    private final ReturnService service;
    private final Customer customer;
    private final CustomerPanel parentPanel;
    private JComboBox<Product> productCombo;
    private JComboBox<String> typeCombo;
    private JSpinner quantitySpinner;
    private JTextArea reasonArea;

    private static final Color BG = new Color(241, 245, 249);
    private static final Color TEXT_DARK = new Color(15, 23, 42);
    private static final Color TEXT_SEC = new Color(100, 116, 139);
    private static final Color ACCENT = new Color(99, 102, 241);
    private static final Color FIELD_BG = new Color(248, 250, 252);
    private static final Color BORDER = new Color(226, 232, 240);

    public NewRequestDialog(MainFrame parent, ReturnService service, Customer customer, CustomerPanel panel) {
        super(parent, "New Return Request", true);
        this.service = service;
        this.customer = customer;
        this.parentPanel = panel;
        setSize(500, 520);
        setLocationRelativeTo(parent);
        setResizable(false);
        getContentPane().setBackground(BG);
        initComponents();
    }

    private void initComponents() {
        JPanel main = new JPanel(new GridBagLayout());
        main.setBackground(BG);
        main.setBorder(BorderFactory.createEmptyBorder(25, 30, 25, 30));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(6, 8, 6, 8);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Title
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        JLabel title = new JLabel("Submit Return Request");
        title.setFont(new Font("Segoe UI", Font.BOLD, 20));
        title.setForeground(TEXT_DARK);
        main.add(title, gbc);

        gbc.gridy = 1;
        JLabel subtitle = new JLabel("Customer: " + customer.getName());
        subtitle.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        subtitle.setForeground(TEXT_SEC);
        main.add(subtitle, gbc);

        // Product
        gbc.gridwidth = 1; gbc.gridy = 2; gbc.gridx = 0; gbc.weightx = 0;
        main.add(label("Product"), gbc);
        gbc.gridx = 1; gbc.weightx = 1;
        List<Product> products = service.getAllProducts();
        productCombo = new JComboBox<>(products.toArray(new Product[0]));
        productCombo.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        productCombo.setPreferredSize(new Dimension(280, 34));
        main.add(productCombo, gbc);

        // Return Type
        gbc.gridy = 3; gbc.gridx = 0; gbc.weightx = 0;
        main.add(label("Return Type"), gbc);
        gbc.gridx = 1; gbc.weightx = 1;
        typeCombo = new JComboBox<>(new String[]{"Return", "Exchange", "Refund"});
        typeCombo.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        typeCombo.setPreferredSize(new Dimension(280, 34));
        main.add(typeCombo, gbc);

        // Quantity
        gbc.gridy = 4; gbc.gridx = 0; gbc.weightx = 0;
        main.add(label("Quantity"), gbc);
        gbc.gridx = 1; gbc.weightx = 1;
        quantitySpinner = new JSpinner(new SpinnerNumberModel(1, 1, 100, 1));
        quantitySpinner.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        quantitySpinner.setPreferredSize(new Dimension(280, 34));
        main.add(quantitySpinner, gbc);

        // Reason
        gbc.gridy = 5; gbc.gridx = 0; gbc.weightx = 0;
        gbc.anchor = GridBagConstraints.NORTHWEST;
        main.add(label("Reason"), gbc);
        gbc.gridx = 1; gbc.weightx = 1; gbc.weighty = 1;
        gbc.fill = GridBagConstraints.BOTH;
        reasonArea = new JTextArea(5, 20);
        reasonArea.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        reasonArea.setBackground(FIELD_BG);
        reasonArea.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(BORDER), BorderFactory.createEmptyBorder(8, 10, 8, 10)));
        reasonArea.setLineWrap(true);
        reasonArea.setWrapStyleWord(true);
        JScrollPane sp = new JScrollPane(reasonArea);
        sp.setBorder(BorderFactory.createLineBorder(BORDER));
        main.add(sp, gbc);

        // Buttons
        gbc.gridy = 6; gbc.gridx = 0; gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.NONE; gbc.anchor = GridBagConstraints.EAST;
        gbc.weighty = 0;
        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        btnPanel.setOpaque(false);

        JButton cancelBtn = new JButton("Cancel");
        cancelBtn.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        cancelBtn.setPreferredSize(new Dimension(100, 36));
        cancelBtn.addActionListener(e -> dispose());

        JButton submitBtn = new JButton("Submit Request");
        submitBtn.setFont(new Font("Segoe UI", Font.BOLD, 13));
        submitBtn.setForeground(Color.WHITE);
        submitBtn.setBackground(ACCENT);
        submitBtn.setFocusPainted(false);
        submitBtn.setBorderPainted(false);
        submitBtn.setPreferredSize(new Dimension(150, 36));
        submitBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        submitBtn.addActionListener(e -> submitRequest());

        btnPanel.add(cancelBtn);
        btnPanel.add(submitBtn);
        main.add(btnPanel, gbc);

        setContentPane(main);
    }

    private void submitRequest() {
        Product product = (Product) productCombo.getSelectedItem();
        String type = (String) typeCombo.getSelectedItem();
        int qty = (int) quantitySpinner.getValue();
        String reason = reasonArea.getText().trim();

        if (reason.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please provide a reason.", "Validation", JOptionPane.WARNING_MESSAGE);
            return;
        }
        ReturnRequest req = service.createRequest(customer, product, type, reason, qty);
        JOptionPane.showMessageDialog(this,
            "Return request " + req.getRequestId() + " submitted successfully!\nAmount: $" + String.format("%.2f", req.getRefundAmount()),
            "Request Submitted", JOptionPane.INFORMATION_MESSAGE);
        parentPanel.refreshData();
        dispose();
    }

    private JLabel label(String text) {
        JLabel l = new JLabel(text);
        l.setFont(new Font("Segoe UI", Font.BOLD, 13));
        l.setForeground(TEXT_DARK);
        return l;
    }
}
