package com.bizzfilling.app.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bizzfilling.app.R;
import com.bizzfilling.app.api.models.Lead;
import java.util.List;

public class LeadAdapter extends RecyclerView.Adapter<LeadAdapter.LeadViewHolder> {

    private List<Lead> leads;

    public LeadAdapter(List<Lead> leads) {
        this.leads = leads;
    }

    @NonNull
    @Override
    public LeadViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_lead, parent, false);
        return new LeadViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull LeadViewHolder holder, int position) {
        Lead lead = leads.get(position);
        holder.tvName.setText(lead.getName());
        holder.tvEmail.setText(lead.getEmail());
        holder.tvPhone.setText(lead.getPhone());
        holder.tvStatus.setText(lead.getStatus());
    }

    @Override
    public int getItemCount() {
        return leads != null ? leads.size() : 0;
    }

    public void updateData(List<Lead> newLeads) {
        this.leads = newLeads;
        notifyDataSetChanged();
    }

    static class LeadViewHolder extends RecyclerView.ViewHolder {
        TextView tvName, tvEmail, tvPhone, tvStatus;

        public LeadViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tvLeadName);
            tvEmail = itemView.findViewById(R.id.tvLeadEmail);
            tvPhone = itemView.findViewById(R.id.tvLeadPhone);
            tvStatus = itemView.findViewById(R.id.tvLeadStatus);
        }
    }
}
