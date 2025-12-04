package com.bizzfilling.app.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bizzfilling.app.R;
import com.bizzfilling.app.api.models.Deal;
import java.util.List;

public class DealAdapter extends RecyclerView.Adapter<DealAdapter.DealViewHolder> {

    private List<Deal> deals;

    public DealAdapter(List<Deal> deals) {
        this.deals = deals;
    }

    @NonNull
    @Override
    public DealViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_deal, parent, false);
        return new DealViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DealViewHolder holder, int position) {
        Deal deal = deals.get(position);
        holder.tvName.setText(deal.getName());
        holder.tvCustomer.setText(deal.getCustomer());
        holder.tvAmount.setText("₹" + (deal.getAmount() != null ? deal.getAmount() : 0));
        holder.tvStage.setText(deal.getStage());
        holder.tvProbability.setText(deal.getProbability() + "%");
    }

    @Override
    public int getItemCount() {
        return deals != null ? deals.size() : 0;
    }

    public void updateData(List<Deal> newDeals) {
        this.deals = newDeals;
        notifyDataSetChanged();
    }

    static class DealViewHolder extends RecyclerView.ViewHolder {
        TextView tvName, tvCustomer, tvAmount, tvStage, tvProbability;

        public DealViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tvDealName);
            tvCustomer = itemView.findViewById(R.id.tvDealCustomer);
            tvAmount = itemView.findViewById(R.id.tvDealAmount);
            tvStage = itemView.findViewById(R.id.tvDealStage);
            tvProbability = itemView.findViewById(R.id.tvDealProbability);
        }
    }
}
