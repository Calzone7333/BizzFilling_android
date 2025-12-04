package com.bizzfilling.app.api.models;

import java.util.List;

public class AgentListResponse {
    private List<User> agents;
    private Stats stats;

    public List<User> getAgents() {
        return agents;
    }

    public Stats getStats() {
        return stats;
    }

    public static class Stats {
        private int totalAgents;
        private int activeAgents;
        private int inactiveAgents;

        public int getTotalAgents() {
            return totalAgents;
        }

        public int getActiveAgents() {
            return activeAgents;
        }

        public int getInactiveAgents() {
            return inactiveAgents;
        }
    }
}
