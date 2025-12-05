package com.bizzfilling.app;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.card.MaterialCardView;

public class PublicHomeActivity extends AppCompatActivity {

    private GridLayout servicesGrid;
    private LinearLayout bannerContainer;
    private android.widget.HorizontalScrollView bannerScrollView;
    private android.os.Handler autoScrollHandler = new android.os.Handler(android.os.Looper.getMainLooper());
    private Runnable autoScrollRunnable;

    private void loadBanners() {
        if (bannerContainer == null) return;
        bannerContainer.removeAllViews();

        String[] titles = {"Start Your Business", "File Your Taxes", "Legal Compliance"};
        String[] descs = {"Register your company in minutes.", "Hassle-free tax filing services.", "Expert legal advice & support."};
        int[] bannerImages = {R.drawable.img_banner_startup, R.drawable.img_banner_tax, R.drawable.img_banner_legal};
        
        for (int i = 0; i < titles.length; i++) {
            View bannerView = getLayoutInflater().inflate(R.layout.item_public_banner, bannerContainer, false);
            
            TextView tvTitle = bannerView.findViewById(R.id.tvBannerTitle);
            TextView tvDesc = bannerView.findViewById(R.id.tvBannerDesc);
            View btnAction = bannerView.findViewById(R.id.btnBannerAction);
            ImageView imgBackground = bannerView.findViewById(R.id.imgBannerBackground);
            
            tvTitle.setText(titles[i]);
            tvDesc.setText(descs[i]);
            if (imgBackground != null) {
                imgBackground.setImageResource(bannerImages[i]);
            }
            
            btnAction.setOnClickListener(v -> {
                 startActivity(new Intent(PublicHomeActivity.this, LoginActivity.class));
            });

            bannerContainer.addView(bannerView);
        }
    }

    private void loadServices() {
        if (servicesGrid == null) return;
        servicesGrid.removeAllViews();

        String[] services = {
            "GST Registration", "Tax Filing", "Company Registration", 
            "ISO Certification", "FSSAI Registration", "Import Export Code"
        };
        
        int[] icons = {
            R.drawable.ic_service_gst,
            R.drawable.ic_service_tax,
            R.drawable.ic_service_company,
            R.drawable.ic_service_iso,
            R.drawable.ic_service_fssai,
            R.drawable.ic_service_ie_code
        };

        for (int i = 0; i < services.length; i++) {
            int finalI = i;
            View serviceView = getLayoutInflater().inflate(R.layout.item_public_service, servicesGrid, false);
            
            TextView tvTitle = serviceView.findViewById(R.id.tvServiceTitle);
            ImageView imgIcon = serviceView.findViewById(R.id.imgServiceIcon);
            
            tvTitle.setText(services[i]);
            if (i < icons.length) {
                imgIcon.setImageResource(icons[i]);
                imgIcon.setColorFilter(Color.parseColor("#2F5C46")); // Apply brand color tint
                imgIcon.setBackgroundResource(R.drawable.bg_icon_circle);
                imgIcon.setPadding(20, 20, 20, 20); // Adjust padding for custom icons
            }
            
            // Layout params for Grid
            GridLayout.LayoutParams params = new GridLayout.LayoutParams();
            params.width = 0;
            params.height = GridLayout.LayoutParams.WRAP_CONTENT;
            params.columnSpec = GridLayout.spec(GridLayout.UNDEFINED, 1f);
            params.setMargins(16, 16, 16, 32); // Increased margins
            serviceView.setLayoutParams(params);
            
            serviceView.setOnClickListener(v -> {
                new androidx.appcompat.app.AlertDialog.Builder(this)
                    .setTitle(services[finalI])
                    .setMessage("Please login to access " + services[finalI] + " services.")
                    .setPositiveButton("Login", (dialog, which) -> {
                        startActivity(new Intent(PublicHomeActivity.this, LoginActivity.class));
                    })
                    .setNegativeButton("Cancel", null)
                    .show();
            });

            servicesGrid.addView(serviceView);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_public_home);

        // Initialize Views
        servicesGrid = findViewById(R.id.servicesGrid);
        bannerContainer = findViewById(R.id.bannerContainer);
        bannerScrollView = findViewById(R.id.bannerScrollView);
        View btnLogin = findViewById(R.id.btnLogin);
        View btnContactExpert = findViewById(R.id.btnContactExpert);

        // Click Listeners
        if (btnLogin != null) {
            btnLogin.setOnClickListener(v -> {
                startActivity(new Intent(PublicHomeActivity.this, LoginActivity.class));
            });
        }

        if (btnContactExpert != null) {
            btnContactExpert.setOnClickListener(v -> {
                Toast.makeText(this, "Please Login to Contact Experts", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(PublicHomeActivity.this, LoginActivity.class));
            });
        }

        // Load Content
        loadBanners();
        loadServices();
        setupAutoScroll();
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
                
                autoScrollHandler.postDelayed(this, 3000); // Scroll every 3 seconds
            }
        };
        autoScrollHandler.postDelayed(autoScrollRunnable, 3000);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (autoScrollHandler != null && autoScrollRunnable != null) {
            autoScrollHandler.removeCallbacks(autoScrollRunnable);
        }
    }
}
