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

public class EmployeeAdapter extends RecyclerView.Adapter<EmployeeAdapter.EmployeeViewHolder> {

    private List<User> employees;
    private OnEmployeeActionListener listener;

    public interface OnEmployeeActionListener {
        void onEdit(User employee);
        void onDelete(User employee);
        void onView(User employee);
    }

    public EmployeeAdapter(List<User> employees, OnEmployeeActionListener listener) {
        this.employees = employees;
        this.listener = listener;
    }

    @NonNull
    @Override
    public EmployeeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_employee, parent, false);
        return new EmployeeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull EmployeeViewHolder holder, int position) {
        User employee = employees.get(position);
        holder.tvName.setText(employee.getFullName());
        holder.tvEmail.setText(employee.getEmail());
        holder.tvRole.setText(employee.getRole());
        holder.tvStatus.setText(employee.getStatus());
        
        if ("ACTIVE".equalsIgnoreCase(employee.getStatus())) {
            holder.tvStatus.setTextColor(holder.itemView.getContext().getResources().getColor(android.R.color.holo_green_dark));
        } else {
            holder.tvStatus.setTextColor(holder.itemView.getContext().getResources().getColor(android.R.color.holo_red_dark));
        }

        holder.btnEdit.setOnClickListener(v -> {
            if (listener != null) listener.onEdit(employee);
        });

        holder.btnDelete.setOnClickListener(v -> {
            if (listener != null) listener.onDelete(employee);
        });

        holder.btnView.setOnClickListener(v -> {
            if (listener != null) listener.onView(employee);
        });
    }

    @Override
    public int getItemCount() {
        return employees != null ? employees.size() : 0;
    }

    public void updateData(List<User> newEmployees) {
        this.employees = newEmployees;
        notifyDataSetChanged();
    }

    static class EmployeeViewHolder extends RecyclerView.ViewHolder {
        TextView tvName, tvEmail, tvRole, tvStatus;
        android.widget.ImageButton btnEdit, btnDelete, btnView;

        public EmployeeViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tvEmployeeName);
            tvEmail = itemView.findViewById(R.id.tvEmployeeEmail);
            tvRole = itemView.findViewById(R.id.tvEmployeeRole);
            tvStatus = itemView.findViewById(R.id.tvEmployeeStatus);
            btnEdit = itemView.findViewById(R.id.btnEdit);
            btnDelete = itemView.findViewById(R.id.btnDelete);
            btnView = itemView.findViewById(R.id.btnView);
        }
    }
}
