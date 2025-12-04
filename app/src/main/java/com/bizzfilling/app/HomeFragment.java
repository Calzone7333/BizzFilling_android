package com.bizzfilling.app;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.google.android.material.card.MaterialCardView;

public class HomeFragment extends Fragment {

    private GridLayout servicesGrid;
    private TextView tvWelcomeUser;
    private ImageView imgUserProfile;
    private LinearLayout bannerContainer;
    private android.widget.HorizontalScrollView bannerScrollView;
    private Handler autoScrollHandler = new Handler(Looper.getMainLooper());
    private Runnable autoScrollRunnable;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        servicesGrid = view.findViewById(R.id.servicesGrid);
        tvWelcomeUser = view.findViewById(R.id.tvWelcomeUser);
        imgUserProfile = view.findViewById(R.id.imgUserProfile);
        bannerContainer = view.findViewById(R.id.bannerContainer);
        bannerScrollView = view.findViewById(R.id.bannerScrollView);

        // Load User Data
        loadUserData(view.getContext());

        // Load Banners
        loadBanners(inflater);
        setupAutoScroll();

        // Setup Service Clicks (for the hardcoded cards in XML)
        setupServiceClicks();

        // See All Services Navigation
        TextView tvSeeAllServices = view.findViewById(R.id.tvSeeAllServices);
        if (tvSeeAllServices != null) {
            tvSeeAllServices.setOnClickListener(v -> {
                if (getActivity() != null) {
                    getActivity().getSupportFragmentManager().beginTransaction()
                            .replace(R.id.content_frame, new ServicesFragment())
                            .addToBackStack(null)
                            .commit();
                }
            });
        }
        
        // Handle "Call Now" button
        View btnContactExpert = view.findViewById(R.id.btnContactExpert);
        if (btnContactExpert != null) {
            btnContactExpert.setOnClickListener(v -> {
                if (getActivity() != null) {
                    getActivity().getSupportFragmentManager().beginTransaction()
                            .replace(R.id.content_frame, new ConsultFragment())
                            .addToBackStack(null)
                            .commit();
                }
            });
        }

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        // Hide the activity toolbar to show our custom header
        if (getActivity() instanceof androidx.appcompat.app.AppCompatActivity) {
            androidx.appcompat.app.ActionBar actionBar = ((androidx.appcompat.app.AppCompatActivity) getActivity()).getSupportActionBar();
            if (actionBar != null) {
                actionBar.hide();
            }
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        // Show the activity toolbar again when leaving this fragment
        if (getActivity() instanceof androidx.appcompat.app.AppCompatActivity) {
            androidx.appcompat.app.ActionBar actionBar = ((androidx.appcompat.app.AppCompatActivity) getActivity()).getSupportActionBar();
            if (actionBar != null) {
                actionBar.show();
            }
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (autoScrollHandler != null && autoScrollRunnable != null) {
            autoScrollHandler.removeCallbacks(autoScrollRunnable);
        }
    }

    private void loadUserData(android.content.Context context) {
        if (context == null) return;
        try {
            com.bizzfilling.app.utils.SessionManager sessionManager = new com.bizzfilling.app.utils.SessionManager(context);
            String name = sessionManager.getUserName();
            if (tvWelcomeUser != null) {
                tvWelcomeUser.setText("Welcome, " + (name != null ? name : "User"));
            }
            
            // Load Profile Image
            if (imgUserProfile != null) {
                com.bizzfilling.app.api.ApiService apiService = com.bizzfilling.app.api.ApiClient.getClient(context).create(com.bizzfilling.app.api.ApiService.class);
                apiService.getProfileImage().enqueue(new retrofit2.Callback<okhttp3.ResponseBody>() {
                    @Override
                    public void onResponse(retrofit2.Call<okhttp3.ResponseBody> call, retrofit2.Response<okhttp3.ResponseBody> response) {
                        if (response.isSuccessful() && response.body() != null) {
                            android.graphics.Bitmap bmp = android.graphics.BitmapFactory.decodeStream(response.body().byteStream());
                            if (bmp != null) {
                                androidx.core.graphics.drawable.RoundedBitmapDrawable circularBitmapDrawable =
                                        androidx.core.graphics.drawable.RoundedBitmapDrawableFactory.create(getResources(), bmp);
                                circularBitmapDrawable.setCircular(true);
                                imgUserProfile.setImageDrawable(circularBitmapDrawable);
                            }
                        }
                    }
                    @Override
                    public void onFailure(retrofit2.Call<okhttp3.ResponseBody> call, Throwable t) {}
                });
                
                imgUserProfile.setOnClickListener(v -> {
                     if (getActivity() != null) {
                        getActivity().getSupportFragmentManager().beginTransaction()
                                .replace(R.id.content_frame, new ProfileFragment())
                                .addToBackStack(null)
                                .commit();
                    }
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void loadBanners(LayoutInflater inflater) {
        if (bannerContainer == null) return;
        bannerContainer.removeAllViews();

        String[] titles = {"Start Your Business", "File Your Taxes", "Legal Compliance"};
        String[] descs = {"Register your company in minutes.", "Hassle-free tax filing services.", "Expert legal advice & support."};
        int[] bannerImages = {R.drawable.img_banner_startup, R.drawable.img_banner_tax, R.drawable.img_banner_legal};
        
        for (int i = 0; i < titles.length; i++) {
            View bannerView = inflater.inflate(R.layout.item_public_banner, bannerContainer, false);
            
            TextView tvTitle = bannerView.findViewById(R.id.tvBannerTitle);
            TextView tvDesc = bannerView.findViewById(R.id.tvBannerDesc);
            View btnAction = bannerView.findViewById(R.id.btnBannerAction);
            ImageView imgBackground = bannerView.findViewById(R.id.imgBannerBackground);
            
            tvTitle.setText(titles[i]);
            tvDesc.setText(descs[i]);
            if (imgBackground != null) {
                imgBackground.setImageResource(bannerImages[i]);
            }
            
            // For logged in user, banner action might go to specific service
            final String serviceName = titles[i];
            btnAction.setOnClickListener(v -> {
                 if (getActivity() != null) {
                    getActivity().getSupportFragmentManager().beginTransaction()
                            .replace(R.id.content_frame, ServiceDetailFragment.newInstance(serviceName, "Details about " + serviceName))
                            .addToBackStack(null)
                            .commit();
                }
            });

            bannerContainer.addView(bannerView);
        }
    }

    private void setupAutoScroll() {
        if (bannerScrollView == null || bannerContainer == null) return;

        autoScrollRunnable = new Runnable() {
            @Override
            public void run() {
                if (bannerScrollView == null || bannerContainer == null || bannerContainer.getChildCount() == 0) return;
                
                View firstChild = bannerContainer.getChildAt(0);
                int itemWidth = firstChild.getWidth();
                int totalItemWidth = itemWidth;
                
                if (firstChild.getLayoutParams() instanceof android.view.ViewGroup.MarginLayoutParams) {
                    android.view.ViewGroup.MarginLayoutParams params = (android.view.ViewGroup.MarginLayoutParams) firstChild.getLayoutParams();
                    totalItemWidth += params.leftMargin + params.rightMargin;
                }
                
                int scrollAmount = totalItemWidth;
                
                if (scrollAmount <= 0) {
                     autoScrollHandler.postDelayed(this, 3000);
                     return;
                }

                int maxScroll = bannerScrollView.getChildAt(0).getWidth() - bannerScrollView.getWidth();
                int currentScroll = bannerScrollView.getScrollX();
                
                int nextScroll = currentScroll + scrollAmount;

                if (nextScroll > maxScroll) {
                    nextScroll = 0; // Reset to start
                    bannerScrollView.scrollTo(0, 0);
                } else {
                    bannerScrollView.smoothScrollTo(nextScroll, 0);
                }
                
                autoScrollHandler.postDelayed(this, 3000);
            }
        };
        autoScrollHandler.postDelayed(autoScrollRunnable, 3000);
    }

    private void setupServiceClicks() {
        if (servicesGrid == null) return;
        
        // We expect 3 children as per XML
        for (int i = 0; i < servicesGrid.getChildCount(); i++) {
            View child = servicesGrid.getChildAt(i);
            final String serviceName;
            if (i == 0) serviceName = "GST Registration";
            else if (i == 1) serviceName = "Tax Filing";
            else serviceName = "Company Registration";
            
            child.setOnClickListener(v -> {
                if (getActivity() != null) {
                    getActivity().getSupportFragmentManager().beginTransaction()
                            .replace(R.id.content_frame, ServiceDetailFragment.newInstance(serviceName, "Details about " + serviceName))
                            .addToBackStack(null)
                            .commit();
                }
            });
        }
    }
}
