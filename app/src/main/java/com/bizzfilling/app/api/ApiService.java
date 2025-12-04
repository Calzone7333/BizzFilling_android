package com.bizzfilling.app.api;

import com.bizzfilling.app.api.models.LoginRequest;
import com.bizzfilling.app.api.models.LoginResponse;
import com.bizzfilling.app.api.models.SignupRequest;
import com.bizzfilling.app.api.models.SignupResponse;
import com.bizzfilling.app.api.models.DashboardStatsResponse;
import com.bizzfilling.app.api.models.Order;
import com.bizzfilling.app.api.models.EmployeeListResponse;
import com.bizzfilling.app.api.models.AgentListResponse;
import com.bizzfilling.app.api.models.Lead;
import com.bizzfilling.app.api.models.Deal;
import com.bizzfilling.app.api.models.Expert;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

import java.util.List;

public interface ApiService {

    @POST("api/auth/login")
    Call<LoginResponse> login(@Body LoginRequest request);

    @POST("api/auth/signup")
    Call<SignupResponse> signup(@Body SignupRequest request);

    @GET("api/admin/dashboard-stats")
    Call<DashboardStatsResponse> getAdminDashboardStats();

    @GET("api/orders")
    Call<List<Order>> getAllOrders();

    @GET("api/admin/employees")
    Call<EmployeeListResponse> listEmployees();

    @GET("api/admin/agents")
    Call<AgentListResponse> listAgents();

    @GET("api/leads")
    Call<List<Lead>> getAllLeads();

    @GET("api/deals")
    Call<List<Deal>> getAllDeals();

    @GET("api/experts")
    Call<List<Expert>> getExperts();

    @GET("api/user/me/profile-image")
    Call<okhttp3.ResponseBody> getProfileImage();

    @retrofit2.http.DELETE("api/admin/employees/{id}")
    Call<Void> deleteEmployee(@retrofit2.http.Path("id") String id);
}
