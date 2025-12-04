package com.bizzfilling.app;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bizzfilling.app.api.ApiClient;
import com.bizzfilling.app.api.ApiService;
import com.bizzfilling.app.api.models.DashboardStatsResponse;

import java.text.NumberFormat;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AdminHomeFragment extends Fragment {

    private TextView tvTotalEmployees, tvTotalAgents, tvTotalCustomers, tvTotalLeads, tvTotalDeals, tvTotalOrders, tvTotalRevenue;
    private ProgressBar progressBar;
    private GridLayout statsGrid;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_admin_home, container, false);

        tvTotalEmployees = view.findViewById(R.id.tvTotalEmployees);
        tvTotalAgents = view.findViewById(R.id.tvTotalAgents);
        tvTotalCustomers = view.findViewById(R.id.tvTotalCustomers);
        tvTotalLeads = view.findViewById(R.id.tvTotalLeads);
        tvTotalDeals = view.findViewById(R.id.tvTotalDeals);
        tvTotalOrders = view.findViewById(R.id.tvTotalOrders);
        tvTotalRevenue = view.findViewById(R.id.tvTotalRevenue);
        progressBar = view.findViewById(R.id.progressBar);
        statsGrid = view.findViewById(R.id.statsGrid);

        fetchDashboardStats();

        return view;
    }

    private void fetchDashboardStats() {
        if (getContext() == null) return;

        progressBar.setVisibility(View.VISIBLE);
        statsGrid.setVisibility(View.GONE);

        ApiService apiService = ApiClient.getClient(getContext()).create(ApiService.class);
        apiService.getAdminDashboardStats().enqueue(new Callback<DashboardStatsResponse>() {
            @Override
            public void onResponse(Call<DashboardStatsResponse> call, Response<DashboardStatsResponse> response) {
                if (isAdded()) {
                    progressBar.setVisibility(View.GONE);
                    if (response.isSuccessful() && response.body() != null) {
                        statsGrid.setVisibility(View.VISIBLE);
                        updateUI(response.body());
                        try {
                            statsGrid.scheduleLayoutAnimation();
                        } catch (Exception e) {
                            e.printStackTrace(); // Animation failure shouldn't crash app
                        }
                    } else {
                        Toast.makeText(getContext(), "Failed to load stats: " + response.message(), Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<DashboardStatsResponse> call, Throwable t) {
                if (isAdded()) {
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(getContext(), "Network Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void updateUI(DashboardStatsResponse stats) {
        if (stats == null) return;
        
        try {
            if (tvTotalEmployees != null) tvTotalEmployees.setText(String.valueOf(stats.getTotalEmployees()));
            if (tvTotalAgents != null) tvTotalAgents.setText(String.valueOf(stats.getTotalAgents()));
            if (tvTotalCustomers != null) tvTotalCustomers.setText(String.valueOf(stats.getTotalCustomers()));
            if (tvTotalLeads != null) tvTotalLeads.setText(String.valueOf(stats.getTotalLeads()));
            if (tvTotalDeals != null) tvTotalDeals.setText(String.valueOf(stats.getTotalDeals()));
            if (tvTotalOrders != null) tvTotalOrders.setText(String.valueOf(stats.getTotalOrders()));
            
            if (tvTotalRevenue != null) {
                NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(new Locale("en", "IN"));
                tvTotalRevenue.setText(currencyFormat.format(stats.getTotalRevenue()));
            }
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(getContext(), "Error updating UI: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }
}
