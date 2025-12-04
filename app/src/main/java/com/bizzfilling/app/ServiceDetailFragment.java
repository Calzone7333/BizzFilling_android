package com.bizzfilling.app;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class ServiceDetailFragment extends Fragment {

    private static final String ARG_TITLE = "title";
    private static final String ARG_DESC = "description";

    public static ServiceDetailFragment newInstance(String title, String description) {
        ServiceDetailFragment fragment = new ServiceDetailFragment();
        Bundle args = new Bundle();
        args.putString(ARG_TITLE, title);
        args.putString(ARG_DESC, description);
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_service_detail, container, false);

        TextView tvTitle = view.findViewById(R.id.tvServiceTitle);
        TextView tvDesc = view.findViewById(R.id.tvServiceDescription);
        Button btnApply = view.findViewById(R.id.btnApply);

        if (getArguments() != null) {
            tvTitle.setText(getArguments().getString(ARG_TITLE));
            String desc = getArguments().getString(ARG_DESC);
            if (desc != null && !desc.isEmpty()) {
                tvDesc.setText(desc);
            }
        }

        btnApply.setOnClickListener(v -> {
            Toast.makeText(getContext(), "Application Started for " + tvTitle.getText(), Toast.LENGTH_SHORT).show();
            // Logic to start application flow
        });

        return view;
    }
}
