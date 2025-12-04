package com.bizzfilling.app.api.models;

public class Order {
    private Long id;
    private String customerEmail;
    private String serviceName;
    private Double totalAmount;
    private String status;
    private String assigneeEmail;
    private String createdAt;

    public Long getId() { return id; }
    public String getCustomerEmail() { return customerEmail; }
    public String getServiceName() { return serviceName; }
    public Double getTotalAmount() { return totalAmount; }
    public String getStatus() { return status; }
    public String getAssigneeEmail() { return assigneeEmail; }
    public String getCreatedAt() { return createdAt; }
}
