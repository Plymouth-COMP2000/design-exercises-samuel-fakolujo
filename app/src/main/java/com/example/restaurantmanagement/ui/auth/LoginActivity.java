package com.example.restaurantmanagement.ui.auth;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.restaurantmanagement.R;
import com.example.restaurantmanagement.data.api.UserApiService;
import com.example.restaurantmanagement.ui.guest.GuestMainActivity;
import com.example.restaurantmanagement.ui.staff.StaffMainActivity;
import com.example.restaurantmanagement.utils.PrefsManager;

import org.json.JSONObject;

public class LoginActivity extends AppCompatActivity {

    private EditText etUsername, etPassword;
    private ProgressBar progress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        PrefsManager prefs = new PrefsManager(this);
        if (prefs.getUsername() != null && prefs.getUsertype() != null) {
            routeByRole(prefs.getUsertype());
            finish();
            return;
        }

        etUsername = findViewById(R.id.etUsername);
        etPassword = findViewById(R.id.etPassword);
        Button btnLogin = findViewById(R.id.btnLogin);
        TextView tvCreate = findViewById(R.id.tvCreateAccount);
        progress = findViewById(R.id.progress);

        tvCreate.setOnClickListener(v -> {
            startActivity(new Intent(this, RegisterActivity.class));
        });

        btnLogin.setOnClickListener(v -> doLogin());
    }

    private void doLogin() {
        String username = etUsername.getText().toString().trim();
        String password = etPassword.getText().toString();

        if (username.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Enter username and password", Toast.LENGTH_SHORT).show();
            return;
        }

        setLoading(true);

        UserApiService.readUser(this, username, new UserApiService.JsonCallback() {
            @Override
            public void onSuccess(JSONObject res) {
                setLoading(false);
                try {
                    JSONObject user = res.getJSONObject("user");
                    String apiPassword = user.getString("password");
                    String usertype = user.getString("usertype"); // guest/staff

                    if (!password.equals(apiPassword)) {
                        Toast.makeText(LoginActivity.this, "Incorrect password", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    // Save session
                    PrefsManager prefs = new PrefsManager(LoginActivity.this);
                    prefs.saveSession(username, usertype);

                    routeByRole(usertype);
                    finish();

                } catch (Exception e) {
                    Toast.makeText(LoginActivity.this, "Login parse error", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onError(String msg) {
                setLoading(false);
                Toast.makeText(LoginActivity.this, "Login failed (user not found?)", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void routeByRole(String usertype) {
        if ("staff".equalsIgnoreCase(usertype)) {
            startActivity(new Intent(this, StaffMainActivity.class));
        } else {
            startActivity(new Intent(this, GuestMainActivity.class));
        }
    }

    private void setLoading(boolean loading) {
        progress.setVisibility(loading ? View.VISIBLE : View.GONE);
    }
}