package com.example.restaurantmanagement.ui.staff;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Switch;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.example.restaurantmanagement.R;
import com.example.restaurantmanagement.utils.PrefsManager;
import android.content.Intent;
import android.widget.Button;
import android.widget.TextView;
import com.example.restaurantmanagement.ui.auth.LoginActivity;

public class StaffProfileFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_staff_profile, container, false);

        Switch swNotif = v.findViewById(R.id.swNotif);
        PrefsManager prefs = new PrefsManager(requireContext());

        swNotif.setChecked(prefs.isNotificationsEnabled());
        swNotif.setOnCheckedChangeListener((buttonView, isChecked) ->
                prefs.setNotificationsEnabled(isChecked)

        );

        TextView tvUserInfo = v.findViewById(R.id.tvUserInfo);
        tvUserInfo.setText("Logged in as: " + prefs.getUsername() + " (" + prefs.getUsertype() + ")");

        Button btnLogout = v.findViewById(R.id.btnLogout);
        btnLogout.setOnClickListener(view -> {
            prefs.clear();

            Intent i = new Intent(requireContext(), LoginActivity.class);
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(i);
        });

        return v;
    }
}