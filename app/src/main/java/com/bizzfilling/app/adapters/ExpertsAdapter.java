package com.bizzfilling.app.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bizzfilling.app.R;
import com.bizzfilling.app.api.models.Expert;
import java.util.List;

public class ExpertsAdapter extends RecyclerView.Adapter<ExpertsAdapter.ExpertViewHolder> {

    private List<Expert> experts;

    public ExpertsAdapter(List<Expert> experts) {
        this.experts = experts;
    }

    @NonNull
    @Override
    public ExpertViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_expert, parent, false);
        return new ExpertViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ExpertViewHolder holder, int position) {
        Expert expert = experts.get(position);
        holder.tvName.setText(expert.getName());
        holder.tvQualification.setText(expert.getQualification());
        if (expert.getSpecialization() != null && !expert.getSpecialization().isEmpty()) {
            holder.tvSpecialization.setText(expert.getSpecialization().get(0));
        } else {
            holder.tvSpecialization.setText("General");
        }
        holder.tvRating.setText(expert.getRating() + " ★");
    }

    @Override
    public int getItemCount() {
        return experts != null ? experts.size() : 0;
    }

    public void updateData(List<Expert> newExperts) {
        this.experts = newExperts;
        notifyDataSetChanged();
    }

    static class ExpertViewHolder extends RecyclerView.ViewHolder {
        TextView tvName, tvQualification, tvSpecialization, tvRating;

        public ExpertViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tvExpertName);
            tvQualification = itemView.findViewById(R.id.tvExpertQualification);
            tvSpecialization = itemView.findViewById(R.id.tvExpertSpecialization);
            tvRating = itemView.findViewById(R.id.tvExpertRating);
        }
    }
}
