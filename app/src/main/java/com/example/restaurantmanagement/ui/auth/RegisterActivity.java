package com.example.restaurantmanagement.ui.auth;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.restaurantmanagement.R;
import com.example.restaurantmanagement.data.api.UserApiService;

import org.json.JSONObject;

public class RegisterActivity extends AppCompatActivity {

    private EditText etUsername, etPassword, etFirst, etLast, etEmail, etContact;
    private RadioButton rbGuest, rbStaff;
    private ProgressBar progress;


    private static final String STAFF_EMAIL_SUFFIX = "@cielorogue.com";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        etUsername = findViewById(R.id.etUsername);
        etPassword = findViewById(R.id.etPassword);
        etFirst = findViewById(R.id.etFirst);
        etLast = findViewById(R.id.etLast);
        etEmail = findViewById(R.id.etEmail);
        etContact = findViewById(R.id.etContact);

        rbGuest = findViewById(R.id.rbGuest);
        rbStaff = findViewById(R.id.rbStaff);

        Button btnRegister = findViewById(R.id.btnRegister);
        progress = findViewById(R.id.progress);

        btnRegister.setOnClickListener(v -> doRegister());
    }

    private void doRegister() {
        String username = etUsername.getText().toString().trim();
        String password = etPassword.getText().toString();
        String first = etFirst.getText().toString().trim();
        String last = etLast.getText().toString().trim();
        String email = etEmail.getText().toString();
        String emailClean = email.trim().toLowerCase();
        String contact = etContact.getText().toString().trim();

        String usertype = rbStaff.isChecked() ? "staff" : "guest";

        if (username.isEmpty() || password.isEmpty() || first.isEmpty() || last.isEmpty() || email.isEmpty() || contact.isEmpty()) {
            Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
            return;
        }


        if ("staff".equals(usertype) && !emailClean.endsWith("@cielorogue.com")) {
            Toast.makeText(this, "Staff must use a staff email (e.g. @cielorogue.com)", Toast.LENGTH_LONG).show();
            return;
        }


        setLoading(true);

        try {
            JSONObject body = new JSONObject();
            body.put("username", username);
            body.put("password", password);
            body.put("firstname", first);
            body.put("lastname", last);
            body.put("email", emailClean);
            body.put("contact", contact);
            body.put("usertype", usertype);

            UserApiService.createUser(this, body, new UserApiService.JsonCallback() {
                @Override
                public void onSuccess(JSONObject res) {
                    setLoading(false);
                    Toast.makeText(RegisterActivity.this, "Account created! Please login.", Toast.LENGTH_SHORT).show();
                    finish();
                }

                @Override
                public void onError(String msg) {
                    setLoading(false);
                    Toast.makeText(RegisterActivity.this, "Registration failed: " + msg, Toast.LENGTH_LONG).show();

                }
            });

        } catch (Exception e) {
            setLoading(false);
            Toast.makeText(this, "Error creating user JSON", Toast.LENGTH_SHORT).show();
        }
    }

    private void setLoading(boolean loading) {
        progress.setVisibility(loading ? View.VISIBLE : View.GONE);
    }
}