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
import com.bizzfilling.app.adapters.EmployeeAdapter;
import com.bizzfilling.app.api.ApiClient;
import com.bizzfilling.app.api.ApiService;
import com.bizzfilling.app.api.models.EmployeeListResponse;
import com.bizzfilling.app.api.models.User;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import java.util.ArrayList;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AdminEmployeesFragment extends Fragment implements EmployeeAdapter.OnEmployeeActionListener {

    private RecyclerView recyclerView;
    private EmployeeAdapter adapter;
    private ProgressBar progressBar;
    private FloatingActionButton fabAdd;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_admin_employees, container, false);

        recyclerView = view.findViewById(R.id.recyclerViewEmployees);
        progressBar = view.findViewById(R.id.progressBar);
        fabAdd = view.findViewById(R.id.fabAddEmployee);

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new EmployeeAdapter(new ArrayList<>(), this);
        recyclerView.setAdapter(adapter);

        fabAdd.setOnClickListener(v -> {
            showAddEmployeeDialog();
        });

        fetchEmployees();

        return view;
    }

    private void fetchEmployees() {
        progressBar.setVisibility(View.VISIBLE);
        ApiService apiService = ApiClient.getClient(getContext()).create(ApiService.class);
        apiService.listEmployees().enqueue(new Callback<EmployeeListResponse>() {
            @Override
            public void onResponse(Call<EmployeeListResponse> call, Response<EmployeeListResponse> response) {
                if (getView() == null) return;
                progressBar.setVisibility(View.GONE);
                if (response.isSuccessful() && response.body() != null) {
                    adapter.updateData(response.body().getEmployees());
                } else {
                    Toast.makeText(getContext(), "Failed to load employees", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<EmployeeListResponse> call, Throwable t) {
                if (getView() == null) return;
                progressBar.setVisibility(View.GONE);
                Toast.makeText(getContext(), "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onEdit(User employee) {
        showEditEmployeeDialog(employee);
    }

    @Override
    public void onDelete(User employee) {
        new androidx.appcompat.app.AlertDialog.Builder(getContext())
            .setTitle("Delete Employee")
            .setMessage("Are you sure you want to delete " + employee.getFullName() + "?")
            .setPositiveButton("Delete", (dialog, which) -> deleteEmployee(employee.getId()))
            .setNegativeButton("Cancel", null)
            .show();
    }

    @Override
    public void onView(User employee) {
        new androidx.appcompat.app.AlertDialog.Builder(getContext())
            .setTitle("Employee Details")
            .setMessage("Name: " + employee.getFullName() + "\nEmail: " + employee.getEmail() + "\nRole: " + employee.getRole() + "\nStatus: " + employee.getStatus())
            .setPositiveButton("Close", null)
            .show();
    }

    private void deleteEmployee(String id) {
        ApiService apiService = ApiClient.getClient(getContext()).create(ApiService.class);
        apiService.deleteEmployee(id).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(getContext(), "Employee deleted", Toast.LENGTH_SHORT).show();
                    fetchEmployees();
                } else {
                    Toast.makeText(getContext(), "Failed to delete", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(getContext(), "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void showAddEmployeeDialog() {
        // Implement Add Dialog here (e.g., using a DialogFragment or AlertDialog with custom layout)
        Toast.makeText(getContext(), "Add Employee Dialog - To Be Implemented", Toast.LENGTH_SHORT).show();
    }

    private void showEditEmployeeDialog(User employee) {
        // Implement Edit Dialog here
        Toast.makeText(getContext(), "Edit Employee Dialog - To Be Implemented", Toast.LENGTH_SHORT).show();
    }
}
