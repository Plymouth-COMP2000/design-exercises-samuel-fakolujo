package com.example.restaurantmanagement.ui.staff;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.example.restaurantmanagement.R;
import android.content.Intent;
import com.example.restaurantmanagement.ui.auth.LoginActivity;
import com.example.restaurantmanagement.utils.PrefsManager;

public class StaffMainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // ✅ GUARD (must be inside onCreate)
        PrefsManager prefs = new PrefsManager(this);
        String type = prefs.getUsertype();

        if (type == null || !type.equalsIgnoreCase("staff")) {
            Intent i = new Intent(this, LoginActivity.class);
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(i);
            finish();
            return;
        }

        setContentView(R.layout.activity_staff_main);

        BottomNavigationView nav = findViewById(R.id.staffBottomNav);
        // Default fragment
        loadFragment(new StaffHomeFragment());

        nav.setOnItemSelectedListener(item -> {
            Fragment selected;
            int id = item.getItemId();

            if (id == R.id.nav_home) selected = new StaffHomeFragment();
            else if (id == R.id.nav_menu) selected = new StaffMenuFragment();
            else if (id == R.id.nav_reservations) selected = new StaffReservationsFragment();
            else selected = new StaffProfileFragment();

            loadFragment(selected);
            return true;
        });
    }

    private void loadFragment(Fragment f) {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.staff_fragment_container, f)
                .commit();
    }
}
