package com.bizzfilling.app.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bizzfilling.app.R;
import com.bizzfilling.app.api.models.User;
import java.util.List;

public class AgentsAdapter extends RecyclerView.Adapter<AgentsAdapter.AgentViewHolder> {

    private List<User> agents;

    public AgentsAdapter(List<User> agents) {
        this.agents = agents;
    }

    @NonNull
    @Override
    public AgentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_employee, parent, false);
        return new AgentViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AgentViewHolder holder, int position) {
        User agent = agents.get(position);
        holder.tvName.setText(agent.getFullName());
        holder.tvEmail.setText(agent.getEmail());
        holder.tvRole.setText(agent.getRole());
        holder.tvStatus.setText(agent.getStatus());
        
        if ("ACTIVE".equalsIgnoreCase(agent.getStatus())) {
            holder.tvStatus.setTextColor(holder.itemView.getContext().getResources().getColor(android.R.color.holo_green_dark));
        } else {
            holder.tvStatus.setTextColor(holder.itemView.getContext().getResources().getColor(android.R.color.holo_red_dark));
        }
    }

    @Override
    public int getItemCount() {
        return agents != null ? agents.size() : 0;
    }

    public void updateData(List<User> newAgents) {
        this.agents = newAgents;
        notifyDataSetChanged();
    }

    static class AgentViewHolder extends RecyclerView.ViewHolder {
        TextView tvName, tvEmail, tvRole, tvStatus;

        public AgentViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tvEmployeeName);
            tvEmail = itemView.findViewById(R.id.tvEmployeeEmail);
            tvRole = itemView.findViewById(R.id.tvEmployeeRole);
            tvStatus = itemView.findViewById(R.id.tvEmployeeStatus);
        }
    }
}
