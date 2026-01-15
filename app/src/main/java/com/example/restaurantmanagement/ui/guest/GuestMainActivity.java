package com.example.restaurantmanagement.ui.guest;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.example.restaurantmanagement.R;
import com.example.restaurantmanagement.data.local.MenuRepository;
import android.content.Intent;
import com.example.restaurantmanagement.ui.auth.LoginActivity;
import com.example.restaurantmanagement.utils.PrefsManager;


public class GuestMainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // ✅ GUARD (must be inside onCreate)
        PrefsManager prefs = new PrefsManager(this);
        String type = prefs.getUsertype();

        if (type == null || !type.equalsIgnoreCase("guest")) {
            Intent i = new Intent(this, LoginActivity.class);
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(i);
            finish();
            return;
        }

        setContentView(R.layout.activity_guest_main);

        BottomNavigationView nav = findViewById(R.id.guestBottomNav);
        // Default fragment
        loadFragment(new GuestHomeFragment());

        nav.setOnItemSelectedListener(item -> {
            Fragment selected;
            int id = item.getItemId();

            if (id == R.id.nav_home) selected = new GuestHomeFragment();
            else if (id == R.id.nav_menu) selected = new GuestMenuFragment();
            else if (id == R.id.nav_reservations) selected = new GuestReservationsFragment();
            else selected = new GuestProfileFragment();

            loadFragment(selected);
            return true;
        });
    }
    private void seedMenuIfEmpty() {
        MenuRepository repo = new MenuRepository(this);

        // quick check: if "Starters" has items, assume seeded
        if (!repo.getByCategory("Starters").isEmpty()) return;

        repo.addItem("Bruschetta", "Starters", 6.50, 1);
        repo.addItem("Garlic Bread", "Starters", 5.00, 0);

        repo.addItem("Steak & Fries", "Main Course", 18.50, 1);
        repo.addItem("Grilled Salmon", "Main Course", 17.00, 0);

        repo.addItem("Chips", "Sides", 4.00, 0);
        repo.addItem("Side Salad", "Sides", 4.50, 0);

        repo.addItem("Cheesecake", "Desserts", 6.00, 1);
        repo.addItem("Chocolate Brownie", "Desserts", 6.50, 0);

        repo.addItem("Mojito", "Cocktails & Drinks", 8.50, 1);
        repo.addItem("Lemonade", "Cocktails & Drinks", 3.50, 0);
    }


    private void loadFragment(Fragment f) {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.guest_fragment_container, f)
                .commit();
    }
}

