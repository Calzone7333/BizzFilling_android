package com.bizzfilling.app;

import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ServicesFragment extends Fragment {

    private LinearLayout container;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_services, container, false);
        this.container = view.findViewById(R.id.servicesContainer);
        
        loadServices();
        
        // Schedule layout animation
        container.post(() -> container.scheduleLayoutAnimation());

        return view;
    }

    private void loadServices() {
        // 1. Consult an Expert
        addCategory("Consult an Expert", new String[]{
            "Talk to Lawyer", "Talk to CA", "Talk to CS", "Talk to IP Expert"
        });

        // 2. Business Setup
        addCategory("Start a Business", new String[]{
            "Pvt Ltd Company", "LLP Registration", "One Person Company", "Sole Proprietorship",
            "Nidhi Company", "Producer Company", "Partnership Firm", "Startup India"
        });

        // 3. International
        addCategory("International Expansion", new String[]{
            "US Incorporation", "Singapore Company", "UK Company", "Netherlands Company",
            "Hong Kong Company", "Dubai Company", "International Trademark"
        });

        // 4. Licenses
        addCategory("Licenses & Registrations", new String[]{
            "DSC", "Udyam Registration", "MSME Registration", "ISO Certification",
            "FSSAI License", "IEC Code", "Spice Board", "FIEO Registration",
            "Legal Metrology", "Hallmark Registration", "BIS Registration", "Liquor License",
            "CLRA License", "AD Code", "IRDAI License", "Drug & Cosmetic",
            "APEDA Registration", "Customs Clearance"
        });

        // 5. Fundraising
        addCategory("Fundraising", new String[]{
            "Pitch Deck", "Business Loan", "DPR Preparation"
        });

        // 6. NGO
        addCategory("NGO & Trust", new String[]{
            "Section 8 Company", "Trust Registration", "Society Registration", "NGO Compliance",
            "CSR-1 Registration", "80G & 12A", "Darpan Registration", "FCRA Registration"
        });
    }

    private void addCategory(String title, String[] items) {
        // Category Title
        TextView tvTitle = new TextView(getContext());
        tvTitle.setText(title);
        tvTitle.setTextSize(18);
        tvTitle.setTypeface(null, android.graphics.Typeface.BOLD);
        tvTitle.setTextColor(Color.parseColor("#1A7F7D"));
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.setMargins(0, 24, 0, 16);
        tvTitle.setLayoutParams(params);
        container.addView(tvTitle);

        // Grid Layout for Items
        GridLayout grid = new GridLayout(getContext());
        grid.setColumnCount(2);
        grid.setAlignmentMode(GridLayout.ALIGN_MARGINS);
        grid.setColumnOrderPreserved(false);
        
        for (String item : items) {
            CardView card = createServiceCard(item);
            GridLayout.LayoutParams gridParams = new GridLayout.LayoutParams();
            gridParams.width = 0;
            gridParams.height = ViewGroup.LayoutParams.WRAP_CONTENT;
            gridParams.columnSpec = GridLayout.spec(GridLayout.UNDEFINED, 1f);
            gridParams.setMargins(8, 8, 8, 8);
            card.setLayoutParams(gridParams);
            grid.addView(card);
        }

        container.addView(grid);
    }

    private CardView createServiceCard(String title) {
        CardView card = new CardView(getContext());
        card.setRadius(16);
        card.setCardElevation(4);
        card.setCardBackgroundColor(Color.WHITE);

        LinearLayout layout = new LinearLayout(getContext());
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setGravity(Gravity.CENTER);
        layout.setPadding(32, 32, 32, 32);

        ImageView icon = new ImageView(getContext());
        icon.setImageResource(android.R.drawable.ic_menu_agenda); // Placeholder icon
        icon.setColorFilter(Color.parseColor("#1A7F7D"));
        LinearLayout.LayoutParams iconParams = new LinearLayout.LayoutParams(64, 64);
        iconParams.setMargins(0, 0, 0, 16);
        icon.setLayoutParams(iconParams);

        TextView tvText = new TextView(getContext());
        tvText.setText(title);
        tvText.setGravity(Gravity.CENTER);
        tvText.setTypeface(null, android.graphics.Typeface.BOLD);
        tvText.setTextColor(Color.parseColor("#111827"));

        layout.addView(icon);
        layout.addView(tvText);
        card.addView(layout);

        card.setOnClickListener(v -> {
            getParentFragmentManager().beginTransaction()
                    .replace(R.id.content_frame, ServiceDetailFragment.newInstance(title, "Get expert assistance with " + title + ". We handle all the paperwork and compliance for you."))
                    .addToBackStack(null)
                    .commit();
        });

        return card;
    }
}
