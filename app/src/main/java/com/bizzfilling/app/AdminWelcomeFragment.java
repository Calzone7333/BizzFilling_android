package com.bizzfilling.app;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class AdminWelcomeFragment extends Fragment {

    private TextView tvAdminName;
    private ImageView ivProfileImage;
    private android.widget.Button btnGoToDashboard;
    private androidx.swiperefreshlayout.widget.SwipeRefreshLayout swipeRefreshLayout;
    private android.view.View errorView;
    private android.view.View contentScrollView;
    private android.widget.Button btnRetry;
    private ImageView ivErrorIcon;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_admin_welcome, container, false);

        tvAdminName = view.findViewById(R.id.tvAdminName);
        ivProfileImage = view.findViewById(R.id.ivProfileImage);
        btnGoToDashboard = view.findViewById(R.id.btnGoToDashboard);
        swipeRefreshLayout = view.findViewById(R.id.swipeRefreshLayout);
        
        // Error View Bindings
        errorView = view.findViewById(R.id.errorView);
        contentScrollView = view.findViewById(R.id.contentScrollView);
        btnRetry = view.findViewById(R.id.btnRetry);
        ivErrorIcon = view.findViewById(R.id.ivErrorIcon);
        
        // Settings / Logout
        View cardSettings = view.findViewById(R.id.cardSettings);
        if (cardSettings != null) {
            cardSettings.setOnClickListener(v -> showLogoutDialog());
        }

        loadUserData();

        btnGoToDashboard.setOnClickListener(v -> {
            if (getActivity() instanceof DashboardActivity) {
                com.google.android.material.bottomnavigation.BottomNavigationView bottomNav = getActivity().findViewById(R.id.bottom_navigation);
                if (bottomNav != null) {
                    bottomNav.setSelectedItemId(R.id.nav_admin_dashboard);
                }
            }
        });
        
        if (swipeRefreshLayout != null) {
            swipeRefreshLayout.setColorSchemeColors(getResources().getColor(R.color.colorPrimary));
            swipeRefreshLayout.setOnRefreshListener(this::loadUserData);
        }
        
        if (btnRetry != null) {
            btnRetry.setOnClickListener(v -> {
                showContent();
                swipeRefreshLayout.setRefreshing(true);
                loadUserData();
            });
        }

        return view;
    }

    private void showError() {
        if (errorView != null) errorView.setVisibility(View.VISIBLE);
        if (contentScrollView != null) contentScrollView.setVisibility(View.GONE);
        
        // Start Animation
        if (ivErrorIcon != null) {
            android.view.animation.Animation shake = android.view.animation.AnimationUtils.loadAnimation(getContext(), R.anim.error_shake);
            ivErrorIcon.startAnimation(shake);
        }
    }

    private void showContent() {
        if (errorView != null) errorView.setVisibility(View.GONE);
        if (contentScrollView != null) contentScrollView.setVisibility(View.VISIBLE);
    }

    private void showLogoutDialog() {
        if (getContext() == null) return;
        
        new androidx.appcompat.app.AlertDialog.Builder(getContext())
            .setTitle("Logout")
            .setMessage("Are you sure you want to logout?")
            .setPositiveButton("Logout", (dialog, which) -> {
                com.bizzfilling.app.utils.SessionManager sessionManager = new com.bizzfilling.app.utils.SessionManager(getContext());
                sessionManager.logout();
                
                android.content.Intent intent = new android.content.Intent(getContext(), com.bizzfilling.app.PublicHomeActivity.class);
                intent.setFlags(android.content.Intent.FLAG_ACTIVITY_NEW_TASK | android.content.Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                if (getActivity() != null) getActivity().finish();
            })
            .setNegativeButton("Cancel", null)
            .show();
    }

    private void loadUserData() {
        if (getContext() == null) return;
        
        com.bizzfilling.app.utils.SessionManager sessionManager = new com.bizzfilling.app.utils.SessionManager(getContext());
        String name = sessionManager.getUserName();
        tvAdminName.setText(name != null ? name : "Admin");

        com.bizzfilling.app.api.ApiService apiService = com.bizzfilling.app.api.ApiClient.getClient(getContext()).create(com.bizzfilling.app.api.ApiService.class);
        apiService.getProfileImage().enqueue(new retrofit2.Callback<okhttp3.ResponseBody>() {
            @Override
            public void onResponse(retrofit2.Call<okhttp3.ResponseBody> call, retrofit2.Response<okhttp3.ResponseBody> response) {
                if (swipeRefreshLayout != null) swipeRefreshLayout.setRefreshing(false);
                if (getView() == null) return;
                
                showContent(); // Success

                if (response.isSuccessful() && response.body() != null) {
                    android.graphics.Bitmap bmp = android.graphics.BitmapFactory.decodeStream(response.body().byteStream());
                    if (bmp != null) {
                        androidx.core.graphics.drawable.RoundedBitmapDrawable circularBitmapDrawable =
                                androidx.core.graphics.drawable.RoundedBitmapDrawableFactory.create(getResources(), bmp);
                        circularBitmapDrawable.setCircular(true);
                        ivProfileImage.setImageDrawable(circularBitmapDrawable);
                    }
                }
            }

            @Override
            public void onFailure(retrofit2.Call<okhttp3.ResponseBody> call, Throwable t) {
                if (swipeRefreshLayout != null) swipeRefreshLayout.setRefreshing(false);
                // Only show error screen if we really can't load anything and it's a network error
                // For profile image, maybe we shouldn't block the whole UI?
                // But user asked for "offline error show". Let's show it if it's a network error.
                showError();
            }
        });
    }
}
