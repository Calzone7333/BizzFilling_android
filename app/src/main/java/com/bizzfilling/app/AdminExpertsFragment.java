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
import com.bizzfilling.app.adapters.ExpertsAdapter;
import com.bizzfilling.app.api.ApiClient;
import com.bizzfilling.app.api.ApiService;
import com.bizzfilling.app.api.models.Expert;
import java.util.ArrayList;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AdminExpertsFragment extends Fragment {

    private RecyclerView recyclerView;
    private ExpertsAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_admin_experts, container, false);

        recyclerView = view.findViewById(R.id.recyclerViewExperts);
        
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new ExpertsAdapter(new ArrayList<>());
        recyclerView.setAdapter(adapter);

        fetchExperts();

        return view;
    }

    private void fetchExperts() {
        ApiService apiService = ApiClient.getClient(getContext()).create(ApiService.class);
        apiService.getExperts().enqueue(new Callback<List<Expert>>() {
            @Override
            public void onResponse(Call<List<Expert>> call, Response<List<Expert>> response) {
                if (getView() == null) return;
                if (response.isSuccessful() && response.body() != null) {
                    adapter.updateData(response.body());
                } else {
                    Toast.makeText(getContext(), "Failed to load experts", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Expert>> call, Throwable t) {
                if (getView() == null) return;
                Toast.makeText(getContext(), "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
