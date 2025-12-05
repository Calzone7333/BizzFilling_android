package com.bizzfilling.app;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class ProfileFragment extends Fragment {

    // UI Components
    private android.widget.ImageView ivProfileImage;
    private android.widget.TextView tvUserName;
    private android.widget.TextView tvUserEmail;
    private android.widget.TextView tvUserPhone;
    private android.widget.TextView tvEditProfile;
    private com.bizzfilling.app.api.models.User currentUser;
    private android.view.View menuMyDocuments;
    private android.view.View menuChangePassword;
    private android.view.View menuPrivacyPolicy;
    private android.view.View menuLogout;

    private final androidx.activity.result.ActivityResultLauncher<String> pickImage = registerForActivityResult(
        new androidx.activity.result.contract.ActivityResultContracts.GetContent(),
        uri -> {
            if (uri != null) {
                uploadProfileImage(uri);
            }
        }
    );
    


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        bindViews(view);
        setupListeners();
        loadUserData();
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (getActivity() instanceof androidx.appcompat.app.AppCompatActivity) {
            androidx.appcompat.app.ActionBar actionBar = ((androidx.appcompat.app.AppCompatActivity) getActivity()).getSupportActionBar();
            if (actionBar != null) {
                actionBar.hide();
            }
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (getActivity() instanceof androidx.appcompat.app.AppCompatActivity) {
            androidx.appcompat.app.ActionBar actionBar = ((androidx.appcompat.app.AppCompatActivity) getActivity()).getSupportActionBar();
            if (actionBar != null) {
                actionBar.show();
            }
        }
    }

    private void bindViews(View view) {
        ivProfileImage = view.findViewById(R.id.ivProfileImage);
        tvUserName = view.findViewById(R.id.tvUserName);
        tvUserEmail = view.findViewById(R.id.tvUserEmail);
        tvUserPhone = view.findViewById(R.id.tvUserPhone);
        tvEditProfile = view.findViewById(R.id.tvEditProfile);
        
        menuMyDocuments = view.findViewById(R.id.menuMyDocuments);
        menuChangePassword = view.findViewById(R.id.menuChangePassword);
        menuPrivacyPolicy = view.findViewById(R.id.menuPrivacyPolicy);
        menuLogout = view.findViewById(R.id.menuLogout);
    }

    private void setupListeners() {
        // Edit Profile (Name/Phone)
        tvEditProfile.setOnClickListener(v -> showEditProfileDialog());
        
        // Change Profile Image
        ivProfileImage.setOnClickListener(v -> pickImage.launch("image/*"));

        // Menu Actions


        menuMyDocuments.setOnClickListener(v -> {
             // Navigate to Documents
             android.widget.Toast.makeText(requireContext(), "My Documents clicked", android.widget.Toast.LENGTH_SHORT).show();
        });

        menuChangePassword.setOnClickListener(v -> {
            // Redirect to Forgot Password Activity as requested
            android.content.Intent intent = new android.content.Intent(requireContext(), ForgotPasswordActivity.class);
            com.bizzfilling.app.utils.SessionManager sessionManager = new com.bizzfilling.app.utils.SessionManager(requireContext());
            String email = sessionManager.getUserEmail();
            if (email != null) {
                intent.putExtra("email", email);
            }
            startActivity(intent);
        });

        menuPrivacyPolicy.setOnClickListener(v -> {
             // Open Privacy Policy URL or Fragment
             android.widget.Toast.makeText(requireContext(), "Privacy Policy clicked", android.widget.Toast.LENGTH_SHORT).show();
        });

        menuLogout.setOnClickListener(v -> logout());
    }

    private void loadUserData() {
        com.bizzfilling.app.utils.SessionManager sessionManager = new com.bizzfilling.app.utils.SessionManager(requireContext());
        tvUserName.setText(sessionManager.getUserName());
        tvUserEmail.setText(sessionManager.getUserEmail());
        
        // Fetch latest
        com.bizzfilling.app.api.ApiService apiService = com.bizzfilling.app.api.ApiClient.getClient(requireContext()).create(com.bizzfilling.app.api.ApiService.class);
        apiService.getUserProfile().enqueue(new retrofit2.Callback<com.bizzfilling.app.api.models.User>() {
            @Override
            public void onResponse(retrofit2.Call<com.bizzfilling.app.api.models.User> call, retrofit2.Response<com.bizzfilling.app.api.models.User> response) {
                if (response.isSuccessful() && response.body() != null) {
                    com.bizzfilling.app.api.models.User user = response.body();
                    tvUserName.setText(user.getFullName());
                    tvUserEmail.setText(user.getEmail());
                    tvUserPhone.setText(user.getPhone() != null ? user.getPhone() : "No Phone");
                    
                    currentUser = user;
                    sessionManager.updateUserDetails(user.getFullName(), user.getEmail());
                }
            }
            @Override
            public void onFailure(retrofit2.Call<com.bizzfilling.app.api.models.User> call, Throwable t) {}
        });

        loadProfileImage();
    }

    private void loadProfileImage() {
        com.bizzfilling.app.api.ApiService apiService = com.bizzfilling.app.api.ApiClient.getClient(requireContext()).create(com.bizzfilling.app.api.ApiService.class);
        apiService.getProfileImage().enqueue(new retrofit2.Callback<okhttp3.ResponseBody>() {
            @Override
            public void onResponse(retrofit2.Call<okhttp3.ResponseBody> call, retrofit2.Response<okhttp3.ResponseBody> response) {
                if (response.isSuccessful() && response.body() != null) {
                    android.graphics.Bitmap bmp = android.graphics.BitmapFactory.decodeStream(response.body().byteStream());
                    if (bmp != null) {
                        ivProfileImage.setImageBitmap(bmp);
                    }
                }
            }
            @Override
            public void onFailure(retrofit2.Call<okhttp3.ResponseBody> call, Throwable t) {}
        });
    }

    private void showEditProfileDialog() {
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(requireContext());
        builder.setTitle("Edit Profile");

        android.widget.LinearLayout layout = new android.widget.LinearLayout(requireContext());
        layout.setOrientation(android.widget.LinearLayout.VERTICAL);
        layout.setPadding(50, 40, 50, 10);

        final android.widget.EditText inputName = new android.widget.EditText(requireContext());
        inputName.setHint("Full Name");
        com.bizzfilling.app.utils.SessionManager sessionManager = new com.bizzfilling.app.utils.SessionManager(requireContext());
        inputName.setText(sessionManager.getUserName());
        layout.addView(inputName);

        final android.widget.EditText inputPhone = new android.widget.EditText(requireContext());
        inputPhone.setHint("Phone Number");
        inputPhone.setInputType(android.text.InputType.TYPE_CLASS_PHONE);
        // Pre-fill phone if available
        if (tvUserPhone.getText() != null && !tvUserPhone.getText().toString().equals("No Phone")) {
            inputPhone.setText(tvUserPhone.getText());
        }
        layout.addView(inputPhone);
        
        // Fetch current phone to pre-fill? (Skipping for simplicity or need to store in session)

        builder.setView(layout);

        builder.setPositiveButton("Save", (dialog, which) -> {
            String newName = inputName.getText().toString().trim();
            String newPhone = inputPhone.getText().toString().trim();
            updateProfile(newName, newPhone, null);
        });
        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.cancel());

        builder.show();
    }

    // Removed showChangePasswordDialog as it's replaced by ForgotPasswordActivity navigation

    private void uploadProfileImage(android.net.Uri imageUri) {
         android.app.ProgressDialog progressDialog = new android.app.ProgressDialog(requireContext());
         progressDialog.setMessage("Uploading image...");
         progressDialog.show();

         try {
            android.content.ContentResolver resolver = requireContext().getContentResolver();
            java.io.InputStream inputStream = resolver.openInputStream(imageUri);
            
            // Read full stream correctly
            java.io.ByteArrayOutputStream buffer = new java.io.ByteArrayOutputStream();
            int nRead;
            byte[] data = new byte[16384];
            while ((nRead = inputStream.read(data, 0, data.length)) != -1) {
                buffer.write(data, 0, nRead);
            }
            buffer.flush();
            byte[] bytes = buffer.toByteArray();
            
            String mimeType = resolver.getType(imageUri);
            if (mimeType == null) mimeType = "image/jpeg";
            
            okhttp3.RequestBody requestFile = okhttp3.RequestBody.create(okhttp3.MediaType.parse(mimeType), bytes);
            
            String fileName = "profile_image." + (mimeType.contains("png") ? "png" : "jpg");
            okhttp3.MultipartBody.Part imagePart = okhttp3.MultipartBody.Part.createFormData("profileImage", fileName, requestFile);

            com.bizzfilling.app.utils.SessionManager sessionManager = new com.bizzfilling.app.utils.SessionManager(requireContext());
            String token = sessionManager.getToken();
            android.util.Log.d("ProfileFragment", "Uploading image with token: " + token);

            com.bizzfilling.app.api.ApiService apiService = com.bizzfilling.app.api.ApiClient.getClient(requireContext()).create(com.bizzfilling.app.api.ApiService.class);
            
            // Pass current user details or empty strings to ensure parts are present
            String name = currentUser != null ? currentUser.getFullName() : (sessionManager.getUserName() != null ? sessionManager.getUserName() : "");
            String phone = currentUser != null ? currentUser.getPhone() : "";
            if (phone == null) phone = "";

            // Always send name and phone parts, even if empty
            okhttp3.RequestBody namePart = okhttp3.RequestBody.create(okhttp3.MediaType.parse("text/plain"), name);
            okhttp3.RequestBody phonePart = okhttp3.RequestBody.create(okhttp3.MediaType.parse("text/plain"), phone);

            apiService.updateProfile(namePart, phonePart, null, imagePart).enqueue(new retrofit2.Callback<com.bizzfilling.app.api.models.UpdateProfileResponse>() {
                @Override
                public void onResponse(retrofit2.Call<com.bizzfilling.app.api.models.UpdateProfileResponse> call, retrofit2.Response<com.bizzfilling.app.api.models.UpdateProfileResponse> response) {
                    progressDialog.dismiss();
                    if (response.isSuccessful()) {
                        ivProfileImage.setImageURI(imageUri);
                        android.widget.Toast.makeText(requireContext(), "Profile image updated", android.widget.Toast.LENGTH_SHORT).show();
                        loadUserData(); // Refresh data
                    } else {
                        android.widget.Toast.makeText(requireContext(), "Failed to upload: " + response.code() + " " + response.message(), android.widget.Toast.LENGTH_SHORT).show();
                    }
                }
                @Override
                public void onFailure(retrofit2.Call<com.bizzfilling.app.api.models.UpdateProfileResponse> call, Throwable t) {
                    progressDialog.dismiss();
                    android.widget.Toast.makeText(requireContext(), "Error: " + t.getMessage(), android.widget.Toast.LENGTH_SHORT).show();
                }
            });

         } catch (Exception e) {
             progressDialog.dismiss();
             e.printStackTrace();
             android.widget.Toast.makeText(requireContext(), "File Error: " + e.getMessage(), android.widget.Toast.LENGTH_SHORT).show();
         }
    }

    private void updateProfile(String name, String phone, String password) {
        android.app.ProgressDialog progressDialog = new android.app.ProgressDialog(requireContext());
        progressDialog.setMessage("Updating...");
        progressDialog.show();

        // Use current values if new ones are null
        com.bizzfilling.app.utils.SessionManager sessionManager = new com.bizzfilling.app.utils.SessionManager(requireContext());
        String finalName = name != null ? name : (currentUser != null ? currentUser.getFullName() : (sessionManager.getUserName() != null ? sessionManager.getUserName() : ""));
        String finalPhone = phone != null ? phone : (currentUser != null ? currentUser.getPhone() : "");
        if (finalPhone == null) finalPhone = "";

        // Always send name and phone parts
        okhttp3.RequestBody namePart = okhttp3.RequestBody.create(okhttp3.MediaType.parse("text/plain"), finalName);
        okhttp3.RequestBody phonePart = okhttp3.RequestBody.create(okhttp3.MediaType.parse("text/plain"), finalPhone);
        
        // Password is optional, send only if provided
        okhttp3.RequestBody passwordPart = (password != null && !password.isEmpty()) ? okhttp3.RequestBody.create(okhttp3.MediaType.parse("text/plain"), password) : null;

        com.bizzfilling.app.api.ApiService apiService = com.bizzfilling.app.api.ApiClient.getClient(requireContext()).create(com.bizzfilling.app.api.ApiService.class);
        apiService.updateProfile(namePart, phonePart, passwordPart, null).enqueue(new retrofit2.Callback<com.bizzfilling.app.api.models.UpdateProfileResponse>() {
            @Override
            public void onResponse(retrofit2.Call<com.bizzfilling.app.api.models.UpdateProfileResponse> call, retrofit2.Response<com.bizzfilling.app.api.models.UpdateProfileResponse> response) {
                progressDialog.dismiss();
                if (response.isSuccessful() && response.body() != null) {
                    com.bizzfilling.app.api.models.User user = response.body().getUser();
                    if (name != null) {
                        tvUserName.setText(user.getFullName());
                        com.bizzfilling.app.utils.SessionManager sessionManager = new com.bizzfilling.app.utils.SessionManager(requireContext());
                        sessionManager.updateUserDetails(user.getFullName(), user.getEmail());
                    }
                    android.widget.Toast.makeText(requireContext(), response.body().getMessage(), android.widget.Toast.LENGTH_SHORT).show();
                } else {
                    android.widget.Toast.makeText(requireContext(), "Update failed", android.widget.Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(retrofit2.Call<com.bizzfilling.app.api.models.UpdateProfileResponse> call, Throwable t) {
                progressDialog.dismiss();
                android.widget.Toast.makeText(requireContext(), "Error: " + t.getMessage(), android.widget.Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void logout() {
        new androidx.appcompat.app.AlertDialog.Builder(requireContext())
            .setTitle("Logout")
            .setMessage("Are you sure you want to logout?")
            .setPositiveButton("Logout", (dialog, which) -> {
                com.bizzfilling.app.utils.SessionManager sessionManager = new com.bizzfilling.app.utils.SessionManager(requireContext());
                sessionManager.logout();
                android.content.Intent intent = new android.content.Intent(requireContext(), LoginActivity.class);
                intent.setFlags(android.content.Intent.FLAG_ACTIVITY_NEW_TASK | android.content.Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            })
            .setNegativeButton("Cancel", null)
            .show();
    }
}
