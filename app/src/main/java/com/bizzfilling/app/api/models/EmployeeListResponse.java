package com.bizzfilling.app.api.models;

import java.util.List;

public class EmployeeListResponse {
    private List<User> employees;
    private Stats stats;

    public List<User> getEmployees() {
        return employees;
    }

    public Stats getStats() {
        return stats;
    }

    public static class Stats {
        private int totalEmployees;
        private int activeEmployees;
        private int inactiveEmployees;

        public int getTotalEmployees() {
            return totalEmployees;
        }

        public int getActiveEmployees() {
            return activeEmployees;
        }

        public int getInactiveEmployees() {
            return inactiveEmployees;
        }
    }
}
