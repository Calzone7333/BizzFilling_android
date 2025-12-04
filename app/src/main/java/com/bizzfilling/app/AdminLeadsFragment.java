package com.bizzfilling.app;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.bizzfilling.app.adapters.LeadAdapter;
import com.bizzfilling.app.api.ApiClient;
import com.bizzfilling.app.api.ApiService;
import com.bizzfilling.app.api.models.Lead;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import java.util.ArrayList;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AdminLeadsFragment extends Fragment {

    private RecyclerView recyclerView;
    private LeadAdapter adapter;
    private ProgressBar progressBar;
    private FloatingActionButton fabAdd;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_admin_leads, container, false);

        recyclerView = view.findViewById(R.id.recyclerViewLeads);
        progressBar = view.findViewById(R.id.progressBar);
        fabAdd = view.findViewById(R.id.fabAddLead);

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new LeadAdapter(new ArrayList<>());
        recyclerView.setAdapter(adapter);

        fabAdd.setOnClickListener(v -> {
            Toast.makeText(getContext(), "Add Lead Feature Coming Soon", Toast.LENGTH_SHORT).show();
        });

        fetchLeads();

        return view;
    }

    private void fetchLeads() {
        progressBar.setVisibility(View.VISIBLE);
        ApiService apiService = ApiClient.getClient(getContext()).create(ApiService.class);
        apiService.getAllLeads().enqueue(new Callback<List<Lead>>() {
            @Override
            public void onResponse(Call<List<Lead>> call, Response<List<Lead>> response) {
                if (getView() == null) return;
                progressBar.setVisibility(View.GONE);
                if (response.isSuccessful() && response.body() != null) {
                    adapter.updateData(response.body());
                } else {
                    Toast.makeText(getContext(), "Failed to load leads", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Lead>> call, Throwable t) {
                if (getView() == null) return;
                progressBar.setVisibility(View.GONE);
                Toast.makeText(getContext(), "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
