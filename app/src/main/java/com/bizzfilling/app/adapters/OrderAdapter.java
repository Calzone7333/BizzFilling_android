package com.bizzfilling.app.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bizzfilling.app.R;
import com.bizzfilling.app.api.models.Order;
import java.util.List;

public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.OrderViewHolder> {

    private List<Order> orders;

    public OrderAdapter(List<Order> orders) {
        this.orders = orders;
    }

    @NonNull
    @Override
    public OrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_order, parent, false);
        return new OrderViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderViewHolder holder, int position) {
        Order order = orders.get(position);
        holder.tvId.setText("#" + order.getId());
        holder.tvService.setText(order.getServiceName());
        holder.tvCustomer.setText(order.getCustomerEmail());
        holder.tvAmount.setText("₹" + (order.getTotalAmount() != null ? order.getTotalAmount() : 0));
        holder.tvStatus.setText(order.getStatus());
    }

    @Override
    public int getItemCount() {
        return orders != null ? orders.size() : 0;
    }

    public void updateData(List<Order> newOrders) {
        this.orders = newOrders;
        notifyDataSetChanged();
    }

    static class OrderViewHolder extends RecyclerView.ViewHolder {
        TextView tvId, tvService, tvCustomer, tvAmount, tvStatus;

        public OrderViewHolder(@NonNull View itemView) {
            super(itemView);
            tvId = itemView.findViewById(R.id.tvOrderId);
            tvService = itemView.findViewById(R.id.tvServiceName);
            tvCustomer = itemView.findViewById(R.id.tvCustomerEmail);
            tvAmount = itemView.findViewById(R.id.tvOrderAmount);
            tvStatus = itemView.findViewById(R.id.tvOrderStatus);
        }
    }
}
