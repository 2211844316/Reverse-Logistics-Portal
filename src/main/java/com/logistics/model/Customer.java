package com.logistics.model;

/**
 * Represents a customer in the e-commerce system.
 */
public class Customer {
    private String customerId;
    private String name;
    private String email;
    private String phone;
    private String password;

    public Customer(String customerId, String name, String email, String phone, String password) {
        this.customerId = customerId;
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.password = password;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String toString() {
        return name;
    }
}
