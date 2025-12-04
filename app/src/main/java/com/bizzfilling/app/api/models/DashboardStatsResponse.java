package com.bizzfilling.app.api.models;

import java.util.List;

public class DashboardStatsResponse {
    @com.google.gson.annotations.SerializedName("totalEmployees")
    private int totalEmployees;
    @com.google.gson.annotations.SerializedName("totalAgents")
    private int totalAgents;
    @com.google.gson.annotations.SerializedName("totalCustomers")
    private int totalCustomers;
    @com.google.gson.annotations.SerializedName("totalLeads")
    private int totalLeads;
    @com.google.gson.annotations.SerializedName("totalDeals")
    private int totalDeals;
    @com.google.gson.annotations.SerializedName("totalOrders")
    private int totalOrders;
    @com.google.gson.annotations.SerializedName("totalRevenue")
    private double totalRevenue;
    @com.google.gson.annotations.SerializedName("leadsVsDealsChart")
    private List<ChartData> leadsVsDealsChart;
    @com.google.gson.annotations.SerializedName("orderStatusChart")
    private List<ChartData> orderStatusChart;

    public int getTotalEmployees() { return totalEmployees; }
    public int getTotalAgents() { return totalAgents; }
    public int getTotalCustomers() { return totalCustomers; }
    public int getTotalLeads() { return totalLeads; }
    public int getTotalDeals() { return totalDeals; }
    public int getTotalOrders() { return totalOrders; }
    public double getTotalRevenue() { return totalRevenue; }
    public List<ChartData> getLeadsVsDealsChart() { return leadsVsDealsChart; }
    public List<ChartData> getOrderStatusChart() { return orderStatusChart; }

    public static class ChartData {
        @com.google.gson.annotations.SerializedName("name")
        private String name;
        @com.google.gson.annotations.SerializedName("leads")
        private int leads;
        @com.google.gson.annotations.SerializedName("deals")
        private int deals;
        @com.google.gson.annotations.SerializedName("value")
        private int value; // For pie chart

        public String getName() { return name; }
        public int getLeads() { return leads; }
        public int getDeals() { return deals; }
        public int getValue() { return value; }
    }
}
