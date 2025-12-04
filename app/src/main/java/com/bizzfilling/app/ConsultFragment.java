package com.bizzfilling.app;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ConsultFragment extends Fragment {

    private RecyclerView rvExperts;
    private ExpertsAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_consult, container, false);
        
        rvExperts = view.findViewById(R.id.rvExperts);
        rvExperts.setLayoutManager(new LinearLayoutManager(getContext()));

        fetchExperts();

        return view;
    }

    private void fetchExperts() {
        ApiService apiService = ApiClient.getClient(getContext()).create(ApiService.class);
        apiService.getExperts().enqueue(new Callback<List<Expert>>() {
            @Override
            public void onResponse(Call<List<Expert>> call, Response<List<Expert>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    adapter = new ExpertsAdapter(response.body());
                    rvExperts.setAdapter(adapter);
                } else {
                    Toast.makeText(getContext(), "Failed to load experts", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Expert>> call, Throwable t) {
                Toast.makeText(getContext(), "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
