package com.bizzfilling.app;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class GenericPlaceholderFragment extends Fragment {

    private String title;

    public GenericPlaceholderFragment(String title) {
        this.title = title;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_generic_placeholder, container, false);
        TextView tvTitle = view.findViewById(R.id.tvTitle);
        tvTitle.setText(title);
        return view;
    }
}
