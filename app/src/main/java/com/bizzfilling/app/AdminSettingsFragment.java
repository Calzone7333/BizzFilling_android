package com.bizzfilling.app;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class AdminSettingsFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_admin_settings, container, false);

        TextView btnMyProfile = view.findViewById(R.id.btnMyProfile);
        TextView btnNotificationSettings = view.findViewById(R.id.btnNotificationSettings);
        TextView btnChangePassword = view.findViewById(R.id.btnChangePassword);
        TextView btnPrivacyPolicy = view.findViewById(R.id.btnPrivacyPolicy);

        btnMyProfile.setOnClickListener(v -> {
            // Navigate to ProfileFragment or similar
            getParentFragmentManager().beginTransaction()
                .replace(R.id.content_frame, new ProfileFragment())
                .addToBackStack(null)
                .commit();
        });

        btnNotificationSettings.setOnClickListener(v -> {
            Toast.makeText(getContext(), "Notification Settings - Coming Soon", Toast.LENGTH_SHORT).show();
        });

        btnChangePassword.setOnClickListener(v -> {
            // Navigate to ChangePasswordFragment (if exists) or show toast
             Toast.makeText(getContext(), "Change Password - Coming Soon", Toast.LENGTH_SHORT).show();
        });

        btnPrivacyPolicy.setOnClickListener(v -> {
             Toast.makeText(getContext(), "Privacy Policy - Coming Soon", Toast.LENGTH_SHORT).show();
        });

        return view;
    }
}
