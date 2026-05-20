package com.logistics.pattern.singleton;

/**
 * Singleton Pattern - Configuration Manager
 * 
 * Thread-safe Singleton that manages application-wide configuration.
 * Uses double-checked locking for lazy initialization.
 * Provides centralized access to company settings and application preferences.
 */
public class ConfigurationManager {
    private static volatile ConfigurationManager instance;

    // Company settings
    private String companyName;
    private int returnPolicyDays;
    private String supportEmail;
    private String supportPhone;
    private boolean autoApproveReturns;
    private double maxRefundAmount;
    private String warehouseAddress;

    /**
     * Private constructor - prevents external instantiation.
     * Initializes with default configuration values.
     */
    private ConfigurationManager() {
        companyName = "Reverse Logistics Inc.";
        returnPolicyDays = 30;
        supportEmail = "support@reverselogistics.com";
        supportPhone = "+1-800-RETURNS";
        autoApproveReturns = false;
        maxRefundAmount = 5000.00;
        warehouseAddress = "123 Logistics Way, Warehouse District";
    }

    /**
     * Get the singleton instance using double-checked locking.
     * @return The single ConfigurationManager instance
     */
    public static ConfigurationManager getInstance() {
        if (instance == null) {
            synchronized (ConfigurationManager.class) {
                if (instance == null) {
                    instance = new ConfigurationManager();
                    System.out.println("[Singleton] ConfigurationManager instance created.");
                }
            }
        }
        return instance;
    }

    // Getters and Setters
    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public int getReturnPolicyDays() {
        return returnPolicyDays;
    }

    public void setReturnPolicyDays(int returnPolicyDays) {
        this.returnPolicyDays = returnPolicyDays;
    }

    public String getSupportEmail() {
        return supportEmail;
    }

    public void setSupportEmail(String supportEmail) {
        this.supportEmail = supportEmail;
    }

    public String getSupportPhone() {
        return supportPhone;
    }

    public void setSupportPhone(String supportPhone) {
        this.supportPhone = supportPhone;
    }

    public boolean isAutoApproveReturns() {
        return autoApproveReturns;
    }

    public void setAutoApproveReturns(boolean autoApproveReturns) {
        this.autoApproveReturns = autoApproveReturns;
    }

    public double getMaxRefundAmount() {
        return maxRefundAmount;
    }

    public void setMaxRefundAmount(double maxRefundAmount) {
        this.maxRefundAmount = maxRefundAmount;
    }

    public String getWarehouseAddress() {
        return warehouseAddress;
    }

    public void setWarehouseAddress(String warehouseAddress) {
        this.warehouseAddress = warehouseAddress;
    }

    @Override
    public String toString() {
        return "ConfigurationManager {\n" +
               "  companyName='" + companyName + "'\n" +
               "  returnPolicyDays=" + returnPolicyDays + "\n" +
               "  supportEmail='" + supportEmail + "'\n" +
               "  supportPhone='" + supportPhone + "'\n" +
               "  autoApproveReturns=" + autoApproveReturns + "\n" +
               "  maxRefundAmount=$" + String.format("%.2f", maxRefundAmount) + "\n" +
               "  warehouseAddress='" + warehouseAddress + "'\n" +
               "}";
    }
}
