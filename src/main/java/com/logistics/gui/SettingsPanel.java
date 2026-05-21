package com.logistics.gui;

import com.logistics.pattern.singleton.ConfigurationManager;

import javax.swing.*;
import java.awt.*;

/**
 * Settings panel using the Singleton ConfigurationManager.
 */
public class SettingsPanel extends JPanel {
    private final ConfigurationManager config;
    private JTextField companyField, emailField, phoneField, warehouseField, maxRefundField;
    private JSpinner policySpinner;
    private JCheckBox autoApproveCheck;

    private static final Color BG = new Color(241, 245, 249);
    private static final Color CARD_BG = Color.WHITE;
    private static final Color TEXT_DARK = new Color(15, 23, 42);
    private static final Color TEXT_SEC = new Color(100, 116, 139);
    private static final Color ACCENT = new Color(99, 102, 241);
    private static final Color FIELD_BG = new Color(248, 250, 252);
    private static final Color BORDER = new Color(226, 232, 240);

    public SettingsPanel() {
        this.config = ConfigurationManager.getInstance();
        setBackground(BG);
        setLayout(new BorderLayout(0, 20));
        setBorder(BorderFactory.createEmptyBorder(25, 25, 25, 25));
        initComponents();
    }

    private void initComponents() {
        JLabel title = new JLabel("Application Settings");
        title.setFont(new Font("Segoe UI", Font.BOLD, 24));
        title.setForeground(TEXT_DARK);
        add(title, BorderLayout.NORTH);

        JPanel card = new JPanel(new GridBagLayout()) {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(CARD_BG); g2.fillRoundRect(0, 0, getWidth(), getHeight(), 16, 16);
                g2.dispose();
            }
        };
        card.setOpaque(false);
        card.setBorder(BorderFactory.createEmptyBorder(30, 40, 30, 40));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 10, 8, 10);
        gbc.anchor = GridBagConstraints.WEST;

        // Section: Company Information
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        JLabel section1 = new JLabel("Company Information");
        section1.setFont(new Font("Segoe UI", Font.BOLD, 16));
        section1.setForeground(ACCENT);
        card.add(section1, gbc);

        gbc.gridwidth = 1; gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridy = 1; gbc.gridx = 0; card.add(createLabel("Company Name"), gbc);
        gbc.gridx = 1; gbc.weightx = 1;
        companyField = createField(config.getCompanyName()); card.add(companyField, gbc);

        gbc.gridy = 2; gbc.gridx = 0; gbc.weightx = 0; card.add(createLabel("Support Email"), gbc);
        gbc.gridx = 1; gbc.weightx = 1;
        emailField = createField(config.getSupportEmail()); card.add(emailField, gbc);

        gbc.gridy = 3; gbc.gridx = 0; gbc.weightx = 0; card.add(createLabel("Support Phone"), gbc);
        gbc.gridx = 1; gbc.weightx = 1;
        phoneField = createField(config.getSupportPhone()); card.add(phoneField, gbc);

        gbc.gridy = 4; gbc.gridx = 0; gbc.weightx = 0; card.add(createLabel("Warehouse Address"), gbc);
        gbc.gridx = 1; gbc.weightx = 1;
        warehouseField = createField(config.getWarehouseAddress()); card.add(warehouseField, gbc);

        // Section: Return Policy
        gbc.gridy = 5; gbc.gridx = 0; gbc.gridwidth = 2; gbc.weightx = 0;
        gbc.insets = new Insets(20, 10, 8, 10);
        JLabel section2 = new JLabel("Return Policy");
        section2.setFont(new Font("Segoe UI", Font.BOLD, 16));
        section2.setForeground(ACCENT);
        card.add(section2, gbc);

        gbc.gridwidth = 1; gbc.insets = new Insets(8, 10, 8, 10);
        gbc.gridy = 6; gbc.gridx = 0; card.add(createLabel("Return Policy (Days)"), gbc);
        gbc.gridx = 1; gbc.weightx = 1;
        policySpinner = new JSpinner(new SpinnerNumberModel(config.getReturnPolicyDays(), 1, 365, 1));
        policySpinner.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        policySpinner.setPreferredSize(new Dimension(300, 36));
        card.add(policySpinner, gbc);

        gbc.gridy = 7; gbc.gridx = 0; gbc.weightx = 0; card.add(createLabel("Max Refund Amount ($)"), gbc);
        gbc.gridx = 1; gbc.weightx = 1;
        maxRefundField = createField(String.format("%.2f", config.getMaxRefundAmount()));
        card.add(maxRefundField, gbc);

        gbc.gridy = 8; gbc.gridx = 0; gbc.gridwidth = 2;
        autoApproveCheck = new JCheckBox("Auto-approve return requests", config.isAutoApproveReturns());
        autoApproveCheck.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        autoApproveCheck.setOpaque(false); autoApproveCheck.setForeground(TEXT_DARK);
        card.add(autoApproveCheck, gbc);

        // Singleton info
        gbc.gridy = 9; gbc.insets = new Insets(20, 10, 8, 10);
        JLabel singletonInfo = new JLabel("<html><i>Settings managed by ConfigurationManager (Singleton Pattern)</i></html>");
        singletonInfo.setFont(new Font("Segoe UI", Font.ITALIC, 12));
        singletonInfo.setForeground(TEXT_SEC);
        card.add(singletonInfo, gbc);

        // Save button
        gbc.gridy = 10; gbc.insets = new Insets(20, 10, 8, 10);
        gbc.fill = GridBagConstraints.NONE; gbc.anchor = GridBagConstraints.EAST;
        JButton saveBtn = new JButton("Save Settings");
        saveBtn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        saveBtn.setForeground(Color.WHITE); saveBtn.setBackground(ACCENT);
        saveBtn.setFocusPainted(false); saveBtn.setBorderPainted(false);
        saveBtn.setPreferredSize(new Dimension(160, 40));
        saveBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        saveBtn.addActionListener(e -> saveSettings());
        card.add(saveBtn, gbc);

        add(card, BorderLayout.CENTER);
    }

    private void saveSettings() {
        try {
            config.setCompanyName(companyField.getText());
            config.setSupportEmail(emailField.getText());
            config.setSupportPhone(phoneField.getText());
            config.setWarehouseAddress(warehouseField.getText());
            config.setReturnPolicyDays((int) policySpinner.getValue());
            config.setMaxRefundAmount(Double.parseDouble(maxRefundField.getText()));
            config.setAutoApproveReturns(autoApproveCheck.isSelected());
            JOptionPane.showMessageDialog(this, "Settings saved successfully!\n\n" + config,
                    "Settings Saved", JOptionPane.INFORMATION_MESSAGE);
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Invalid number format for Max Refund Amount.",
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private JLabel createLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Segoe UI", Font.BOLD, 13));
        label.setForeground(TEXT_DARK);
        return label;
    }

    private JTextField createField(String text) {
        JTextField field = new JTextField(text);
        field.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        field.setBackground(FIELD_BG); field.setForeground(TEXT_DARK);
        field.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(BORDER, 1, true),
            BorderFactory.createEmptyBorder(8, 12, 8, 12)));
        field.setPreferredSize(new Dimension(300, 36));
        return field;
    }
}
