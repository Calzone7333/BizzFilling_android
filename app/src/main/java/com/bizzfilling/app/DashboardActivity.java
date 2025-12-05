package com.bizzfilling.app;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import com.google.android.material.navigation.NavigationView;

public class DashboardActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout drawer;
    private com.google.android.material.bottomnavigation.BottomNavigationView bottomNav;

    private String userRole = "USER"; // Default to USER

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            setContentView(R.layout.activity_dashboard);

            // Get Role from Intent (Simulated)
            if (getIntent().hasExtra("ROLE")) {
                userRole = getIntent().getStringExtra("ROLE");
            }

            Toolbar toolbar = findViewById(R.id.toolbar);
            setSupportActionBar(toolbar);

            drawer = findViewById(R.id.drawer_layout);
            NavigationView navigationView = findViewById(R.id.nav_view);
            navigationView.setNavigationItemSelectedListener(this);

            // Update Header with User Info
            android.view.View headerView = navigationView.getHeaderView(0);
            android.widget.TextView tvName = headerView.findViewById(R.id.tvName);
            android.widget.TextView tvEmail = headerView.findViewById(R.id.tvEmail);
            android.widget.ImageView imageViewProfile = headerView.findViewById(R.id.imageViewProfile);

            com.bizzfilling.app.utils.SessionManager sessionManager = new com.bizzfilling.app.utils.SessionManager(this);
            if (sessionManager.isLoggedIn()) {
                tvName.setText(sessionManager.getUserName());
                tvEmail.setText(sessionManager.getUserEmail());
                
                // Fetch Profile Image
                com.bizzfilling.app.api.ApiService apiService = com.bizzfilling.app.api.ApiClient.getClient(this).create(com.bizzfilling.app.api.ApiService.class);
                apiService.getProfileImage().enqueue(new retrofit2.Callback<okhttp3.ResponseBody>() {
                    @Override
                    public void onResponse(retrofit2.Call<okhttp3.ResponseBody> call, retrofit2.Response<okhttp3.ResponseBody> response) {
                        if (response.isSuccessful() && response.body() != null) {
                            android.util.Log.d("DashboardActivity", "Profile image response successful, size: " + response.body().contentLength());
                            android.graphics.Bitmap bmp = android.graphics.BitmapFactory.decodeStream(response.body().byteStream());
                            if (bmp != null) {
                                // Make it circular
                                androidx.core.graphics.drawable.RoundedBitmapDrawable circularBitmapDrawable =
                                        androidx.core.graphics.drawable.RoundedBitmapDrawableFactory.create(getResources(), bmp);
                                circularBitmapDrawable.setCircular(true);
                                imageViewProfile.setImageDrawable(circularBitmapDrawable);
                            } else {
                                android.util.Log.e("DashboardActivity", "Failed to decode bitmap");
                            }
                        } else {
                            android.util.Log.e("DashboardActivity", "Profile image fetch failed: " + response.code());
                        }
                    }

                    @Override
                    public void onFailure(retrofit2.Call<okhttp3.ResponseBody> call, Throwable t) {
                        android.util.Log.e("DashboardActivity", "Profile image network error: " + t.getMessage());
                    }
                });
            }

            // Dynamic Menu Inflation based on Role
            navigationView.getMenu().clear();
            if ("ADMIN".equals(userRole)) {
                navigationView.inflateMenu(R.menu.activity_admin_dashboard_drawer);
            } else if ("EMPLOYEE".equals(userRole)) {
                navigationView.inflateMenu(R.menu.activity_employee_dashboard_drawer);
            } else if ("AGENT".equals(userRole)) {
                navigationView.inflateMenu(R.menu.activity_agent_dashboard_drawer);
            } else {
                navigationView.inflateMenu(R.menu.activity_dashboard_drawer);
            }

            ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar,
                    R.string.navigation_drawer_open, R.string.navigation_drawer_close);
            drawer.addDrawerListener(toggle);
            toggle.syncState();

            if (savedInstanceState == null) {
                try {
                    // Default Fragment based on Role
                    if ("ADMIN".equals(userRole)) {
                        getSupportFragmentManager().beginTransaction().replace(R.id.content_frame, new AdminWelcomeFragment()).commit();
                        navigationView.setCheckedItem(R.id.nav_admin_home);
                    } else if ("EMPLOYEE".equals(userRole)) {
                        getSupportFragmentManager().beginTransaction().replace(R.id.content_frame, new EmployeeHomeFragment()).commit();
                        navigationView.setCheckedItem(R.id.nav_employee_home);
                    } else if ("AGENT".equals(userRole)) {
                        getSupportFragmentManager().beginTransaction().replace(R.id.content_frame, new AgentHomeFragment()).commit();
                        navigationView.setCheckedItem(R.id.nav_agent_home);
                    } else {
                        getSupportFragmentManager().beginTransaction().replace(R.id.content_frame, new HomeFragment()).commit();
                        navigationView.setCheckedItem(R.id.nav_home);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(this, "Error loading initial view: " + e.getMessage(), Toast.LENGTH_LONG).show();
                }
            }
            // Bottom Navigation
            bottomNav = findViewById(R.id.bottom_navigation);
            if (bottomNav != null) {
                // Set Menu based on Role
                bottomNav.getMenu().clear();
                if ("ADMIN".equals(userRole)) {
                    bottomNav.inflateMenu(R.menu.menu_bottom_nav_admin);
                } else {
                    bottomNav.inflateMenu(R.menu.menu_bottom_nav_user);
                }

                bottomNav.setOnNavigationItemSelectedListener(this::onBottomNavItemSelected);
            }
        } catch (Exception e) {
            e.printStackTrace();
            new androidx.appcompat.app.AlertDialog.Builder(this)
                .setTitle("Dashboard Error")
                .setMessage("Failed to load dashboard.\n\nError: " + e.getMessage())
                .setPositiveButton("Retry", (dialog, which) -> recreate())
                .setNegativeButton("Close", (dialog, which) -> finish())
                .setCancelable(false)
                .show();
        }
    }

    public void setBottomNavigationItem(int itemId) {
        if (bottomNav != null && bottomNav.getSelectedItemId() != itemId) {
            bottomNav.setOnNavigationItemSelectedListener(null);
            bottomNav.setSelectedItemId(itemId);
            bottomNav.setOnNavigationItemSelectedListener(this::onBottomNavItemSelected);
        }
    }

    private boolean onBottomNavItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        androidx.fragment.app.Fragment selectedFragment = null;

        // --- USER NAV ---
        if (id == R.id.nav_home) {
            if ("EMPLOYEE".equals(userRole)) selectedFragment = new EmployeeHomeFragment();
            else if ("AGENT".equals(userRole)) selectedFragment = new AgentHomeFragment();
            else selectedFragment = new HomeFragment();
        } else if (id == R.id.nav_dashboard) {
            selectedFragment = new UserReportsFragment(); // Using Reports as Dashboard for now
        } else if (id == R.id.nav_settings) {
            selectedFragment = new ProfileFragment(); // Using Profile as Settings for now
        } else if (id == R.id.nav_orders) {
            selectedFragment = new OrdersFragment();
        } 
        
        // --- ADMIN NAV ---
        else if (id == R.id.nav_admin_dashboard) {
            selectedFragment = new AdminHomeFragment(); // Dashboard Stats
        } else if (id == R.id.nav_admin_home) {
            selectedFragment = new AdminWelcomeFragment(); // Welcome Screen
        } else if (id == R.id.nav_admin_settings) {
            selectedFragment = new AdminSettingsFragment();
        }

        if (selectedFragment != null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.content_frame, selectedFragment).commit();
            return true;
        }
        return false;
    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        androidx.fragment.app.Fragment selectedFragment = null;

        // --- USER ROUTES ---
        if (id == R.id.nav_orders) {
            selectedFragment = new OrdersFragment();
        } else if (id == R.id.nav_compliances) {
            selectedFragment = new CompliancesFragment();

        } else if (id == R.id.nav_documents) {
            selectedFragment = new DocumentsFragment();
        } else if (id == R.id.nav_calendar) {
            selectedFragment = new UserCalendarFragment();
        } else if (id == R.id.nav_reports) {
            selectedFragment = new UserReportsFragment();
        } else if (id == R.id.nav_consult) {
            selectedFragment = new ConsultFragment();
        } else if (id == R.id.nav_company) {
            selectedFragment = new CompanyDetailsFragment();
        } 
        
        // --- ADMIN ROUTES ---
        else if (id == R.id.nav_admin_home) {
            selectedFragment = new AdminWelcomeFragment(); // Using Welcome Fragment as requested
        } else if (id == R.id.nav_admin_orders) {
            selectedFragment = new AdminOrdersFragment();
        } else if (id == R.id.nav_admin_employees) {
            selectedFragment = new AdminEmployeesFragment();

        } else if (id == R.id.nav_admin_leads) {
            selectedFragment = new AdminLeadsFragment();
        } else if (id == R.id.nav_admin_deals) {
            selectedFragment = new AdminDealsFragment();
        } else if (id == R.id.nav_admin_agents) {
            selectedFragment = new AdminAgentsFragment();
        } else if (id == R.id.nav_admin_experts) {
            selectedFragment = new AdminExpertsFragment();
        } else if (id == R.id.nav_admin_attendance) {
            selectedFragment = new AdminAttendanceFragment();
        } else if (id == R.id.nav_admin_performance) {
            selectedFragment = new AdminPerformanceFragment();
        } else if (id == R.id.nav_admin_sales_reports) {
            selectedFragment = new AdminSalesReportsFragment();
        } else if (id == R.id.nav_admin_reports) {
            selectedFragment = new AdminReportsFragment();
        } else if (id == R.id.nav_admin_crm) {
            selectedFragment = new AdminCrmFragment();
        } else if (id == R.id.nav_admin_customers) {
            selectedFragment = new AdminCustomersFragment();
        } else if (id == R.id.nav_admin_companies) {
            selectedFragment = new AdminCompaniesFragment();
        }

        // --- EMPLOYEE ROUTES ---
        else if (id == R.id.nav_employee_home) {
            selectedFragment = new EmployeeHomeFragment();
        } else if (id == R.id.nav_employee_tasks) {
            selectedFragment = new EmployeeTasksFragment();
        } else if (id == R.id.nav_employee_calendar) {
            selectedFragment = new EmployeeCalendarFragment();
        } else if (id == R.id.nav_employee_attendance) {
            selectedFragment = new EmployeeAttendanceFragment();
        } else if (id == R.id.nav_employee_sales) {
            selectedFragment = new EmployeeSalesFragment();
        } else if (id == R.id.nav_employee_reports) {
            selectedFragment = new EmployeeReportsFragment();
        } else if (id == R.id.nav_employee_leads) {
            selectedFragment = new EmployeeLeadsFragment();
        } else if (id == R.id.nav_employee_deals) {
            selectedFragment = new AdminDealsFragment(); // Reuse Admin Deals
        } else if (id == R.id.nav_employee_customers) {
            selectedFragment = new AdminCustomersFragment(); // Reuse Admin Customers
        } else if (id == R.id.nav_employee_companies) {
            selectedFragment = new AdminCompaniesFragment(); // Reuse Admin Companies
        } else if (id == R.id.nav_employee_contact) {
            selectedFragment = new EmployeeContactFragment();
        } else if (id == R.id.nav_employee_company) {
            selectedFragment = new EmployeeCompanyFragment();
        }

        // --- AGENT ROUTES ---
        else if (id == R.id.nav_agent_home) {
            selectedFragment = new AgentHomeFragment();
        } else if (id == R.id.nav_agent_wallet) {
            selectedFragment = new AgentWalletFragment();
        } else if (id == R.id.nav_agent_orders) {
            selectedFragment = new AgentOrdersFragment();
        }

        // --- SHARED ROUTES ---
        // Profile and Logout removed from Drawer


        if (selectedFragment != null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.content_frame, selectedFragment).commit();
        }

        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
