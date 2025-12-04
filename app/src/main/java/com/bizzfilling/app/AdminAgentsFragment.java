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
import com.bizzfilling.app.adapters.AgentsAdapter;
import com.bizzfilling.app.api.ApiClient;
import com.bizzfilling.app.api.ApiService;
import com.bizzfilling.app.api.models.AgentListResponse;
import java.util.ArrayList;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AdminAgentsFragment extends Fragment {

    private RecyclerView recyclerView;
    private AgentsAdapter adapter;
    private ProgressBar progressBar;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_admin_agents, container, false);

        recyclerView = view.findViewById(R.id.recyclerViewAgents);
        progressBar = view.findViewById(R.id.progressBar); // Ensure progressBar exists in layout if copied from employees

        // If layout doesn't have progressBar, find it or handle null. 
        // I created fragment_admin_agents.xml without progressBar in previous step, let me check.
        // Actually I copied the layout structure but I didn't include ProgressBar in the XML creation command for agents.
        // I should probably add it or handle it. For now, let's assume I might need to add it or it might crash if I try to use it.
        // Wait, I used a generic template for layout creation. Let's check fragment_admin_agents.xml content I wrote.
        // I wrote: RecyclerView with id recyclerViewAgents. No ProgressBar.
        // So I should remove ProgressBar usage or add it to XML.
        // I'll remove ProgressBar usage for now to avoid crash, or just check for null.
        
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new AgentsAdapter(new ArrayList<>());
        recyclerView.setAdapter(adapter);

        fetchAgents();

        return view;
    }

    private void fetchAgents() {
        // if (progressBar != null) progressBar.setVisibility(View.VISIBLE);
        ApiService apiService = ApiClient.getClient(getContext()).create(ApiService.class);
        apiService.listAgents().enqueue(new Callback<AgentListResponse>() {
            @Override
            public void onResponse(Call<AgentListResponse> call, Response<AgentListResponse> response) {
                if (getView() == null) return;
                // if (progressBar != null) progressBar.setVisibility(View.GONE);
                if (response.isSuccessful() && response.body() != null) {
                    adapter.updateData(response.body().getAgents());
                } else {
                    Toast.makeText(getContext(), "Failed to load agents", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<AgentListResponse> call, Throwable t) {
                if (getView() == null) return;
                // if (progressBar != null) progressBar.setVisibility(View.GONE);
                Toast.makeText(getContext(), "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
