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
import com.bizzfilling.app.adapters.DealAdapter;
import com.bizzfilling.app.api.ApiClient;
import com.bizzfilling.app.api.ApiService;
import com.bizzfilling.app.api.models.Deal;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import java.util.ArrayList;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AdminDealsFragment extends Fragment {

    private RecyclerView recyclerView;
    private DealAdapter adapter;
    private ProgressBar progressBar;
    private FloatingActionButton fabAdd;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_admin_deals, container, false);

        recyclerView = view.findViewById(R.id.recyclerViewDeals);
        progressBar = view.findViewById(R.id.progressBar);
        fabAdd = view.findViewById(R.id.fabAddDeal);

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new DealAdapter(new ArrayList<>());
        recyclerView.setAdapter(adapter);

        fabAdd.setOnClickListener(v -> {
            Toast.makeText(getContext(), "Add Deal Feature Coming Soon", Toast.LENGTH_SHORT).show();
        });

        fetchDeals();

        return view;
    }

    private void fetchDeals() {
        progressBar.setVisibility(View.VISIBLE);
        ApiService apiService = ApiClient.getClient(getContext()).create(ApiService.class);
        apiService.getAllDeals().enqueue(new Callback<List<Deal>>() {
            @Override
            public void onResponse(Call<List<Deal>> call, Response<List<Deal>> response) {
                if (getView() == null) return;
                progressBar.setVisibility(View.GONE);
                if (response.isSuccessful() && response.body() != null) {
                    adapter.updateData(response.body());
                } else {
                    Toast.makeText(getContext(), "Failed to load deals", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Deal>> call, Throwable t) {
                if (getView() == null) return;
                progressBar.setVisibility(View.GONE);
                Toast.makeText(getContext(), "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
