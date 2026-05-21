package com.logistics.service;

import com.logistics.model.*;
import com.logistics.pattern.observer.*;
import com.logistics.pattern.strategy.*;
import com.logistics.pattern.singleton.ConfigurationManager;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Service layer that coordinates business logic for the Reverse Logistics Portal.
 * Uses all three design patterns:
 * - Strategy Pattern via RefundProcessor for processing refunds
 * - Observer Pattern via ReturnRequestSubject for status change notifications
 * - Singleton Pattern via ConfigurationManager for application settings
 */
public class ReturnService {
    private final List<ReturnRequest> requests;
    private final List<Product> products;
    private final List<Customer> customers;
    private final ReturnRequestSubject subject;
    private final RefundProcessor refundProcessor;
    private final EmailNotifier emailNotifier;
    private final SMSNotifier smsNotifier;
    private final DashboardNotifier dashboardNotifier;

    public ReturnService() {
        requests = new ArrayList<>();
        products = new ArrayList<>();
        customers = new ArrayList<>();
        subject = new ReturnRequestSubject();
        refundProcessor = new RefundProcessor();

        // Create observers
        emailNotifier = new EmailNotifier();
        smsNotifier = new SMSNotifier();
        dashboardNotifier = new DashboardNotifier();

        // Attach observers to subject (Observer Pattern)
        subject.attach(emailNotifier);
        subject.attach(smsNotifier);
        subject.attach(dashboardNotifier);

        // Initialize demo data
        initializeDemoData();

        // Log configuration (Singleton Pattern)
        System.out.println("[Service] Configuration loaded:\n" + ConfigurationManager.getInstance());
    }

    private void initializeDemoData() {
        // Products
        products.add(new Product("P001", "Wireless Headphones", "Electronics", 79.99));
        products.add(new Product("P002", "Running Shoes", "Footwear", 129.99));
        products.add(new Product("P003", "Smart Watch", "Electronics", 249.99));
        products.add(new Product("P004", "Laptop Bag", "Accessories", 49.99));
        products.add(new Product("P005", "USB-C Hub", "Electronics", 39.99));
        products.add(new Product("P006", "Winter Jacket", "Clothing", 189.99));
        products.add(new Product("P007", "Bluetooth Speaker", "Electronics", 59.99));
        products.add(new Product("P008", "Yoga Mat", "Sports", 29.99));
        products.add(new Product("P009", "Gaming Mouse", "Electronics", 69.99));
        products.add(new Product("P010", "Coffee Maker", "Appliances", 99.99));

        // Customers
        customers.add(new Customer("C001", "Ahmed Ali", "ahmed@email.com", "+966-555-0001", "pass123"));
        customers.add(new Customer("C002", "Sara Mohamed", "sara@email.com", "+966-555-0002", "pass123"));
        customers.add(new Customer("C003", "Omar Hassan", "omar@email.com", "+966-555-0003", "pass123"));

        // Demo return requests
        requests.add(new ReturnRequest(customers.get(0), products.get(0), "Refund",
                "Defective product - audio cuts out intermittently", 1));
        requests.add(new ReturnRequest(customers.get(1), products.get(1), "Exchange",
                "Wrong size received - ordered 42, got 40", 1));
        requests.add(new ReturnRequest(customers.get(0), products.get(2), "Return",
                "Changed my mind - no longer needed", 1));
        requests.add(new ReturnRequest(customers.get(2), products.get(5), "Refund",
                "Color different from product image", 1));
        requests.add(new ReturnRequest(customers.get(1), products.get(6), "Return",
                "Received damaged packaging", 1));
    }

    // --- Request Management ---

    public ReturnRequest createRequest(Customer customer, Product product, String returnType,
                                        String reason, int quantity) {
        ReturnRequest request = new ReturnRequest(customer, product, returnType, reason, quantity);
        requests.add(request);
        System.out.println("[Service] New return request created: " + request.getRequestId());
        return request;
    }

    public void updateRequestStatus(ReturnRequest request, RequestStatus newStatus) {
        RequestStatus oldStatus = request.getStatus();
        request.setStatus(newStatus);
        // Notify all observers (Observer Pattern)
        subject.notifyObservers(request, oldStatus, newStatus);
    }

    public String processRefund(ReturnRequest request, RefundStrategy strategy) {
        // Set the strategy (Strategy Pattern)
        refundProcessor.setStrategy(strategy);
        String result = refundProcessor.executeRefund(request);
        // Update status to REFUNDED
        updateRequestStatus(request, RequestStatus.REFUNDED);
        return result;
    }

    public void approveRequest(ReturnRequest request, String adminNotes) {
        request.setAdminNotes(adminNotes);
        updateRequestStatus(request, RequestStatus.APPROVED);
    }

    public void rejectRequest(ReturnRequest request, String adminNotes) {
        request.setAdminNotes(adminNotes);
        updateRequestStatus(request, RequestStatus.REJECTED);
    }

    public void completeRequest(ReturnRequest request) {
        updateRequestStatus(request, RequestStatus.COMPLETED);
    }

    // --- Data Access ---

    public List<ReturnRequest> getAllRequests() {
        return new ArrayList<>(requests);
    }

    public List<ReturnRequest> getRequestsByCustomer(Customer customer) {
        return requests.stream()
                .filter(r -> r.getCustomer().getCustomerId().equals(customer.getCustomerId()))
                .collect(Collectors.toList());
    }

    public List<ReturnRequest> getRequestsByStatus(RequestStatus status) {
        return requests.stream()
                .filter(r -> r.getStatus() == status)
                .collect(Collectors.toList());
    }

    public List<Product> getAllProducts() {
        return new ArrayList<>(products);
    }

    public List<Customer> getAllCustomers() {
        return new ArrayList<>(customers);
    }

    public Customer authenticateCustomer(String name, String password) {
        return customers.stream()
                .filter(c -> c.getName().equalsIgnoreCase(name) && c.getPassword().equals(password))
                .findFirst()
                .orElse(null);
    }

    public boolean authenticateAdmin(String username, String password) {
        return "admin".equalsIgnoreCase(username) && "admin123".equals(password);
    }

    // --- Statistics ---

    public int getTotalRequests() {
        return requests.size();
    }

    public int getRequestCountByStatus(RequestStatus status) {
        return (int) requests.stream().filter(r -> r.getStatus() == status).count();
    }

    public double getTotalRefundAmount() {
        return requests.stream()
                .filter(r -> r.getStatus() == RequestStatus.REFUNDED || r.getStatus() == RequestStatus.COMPLETED)
                .mapToDouble(ReturnRequest::getRefundAmount)
                .sum();
    }

    // --- Observer Access ---

    public DashboardNotifier getDashboardNotifier() {
        return dashboardNotifier;
    }

    public EmailNotifier getEmailNotifier() {
        return emailNotifier;
    }

    public SMSNotifier getSmsNotifier() {
        return smsNotifier;
    }

    public ReturnRequestSubject getSubject() {
        return subject;
    }

    public RefundProcessor getRefundProcessor() {
        return refundProcessor;
    }
}
