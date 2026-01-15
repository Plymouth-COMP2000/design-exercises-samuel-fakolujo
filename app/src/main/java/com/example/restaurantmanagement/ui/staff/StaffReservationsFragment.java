package com.example.restaurantmanagement.ui.staff;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import com.example.restaurantmanagement.R;
import com.example.restaurantmanagement.data.local.ReservationRepository;
import com.example.restaurantmanagement.models.Reservation;

public class StaffReservationsFragment extends Fragment {

    private ReservationRepository repo;
    private StaffReservationAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_staff_reservations, container, false);

        repo = new ReservationRepository(requireContext());

        RecyclerView rv = v.findViewById(R.id.rvStaffReservations);
        rv.setLayoutManager(new LinearLayoutManager(requireContext()));

        adapter = new StaffReservationAdapter(r -> {
            repo.cancelReservation(r.id);
            Toast.makeText(requireContext(), "Cancelled reservation", Toast.LENGTH_SHORT).show();
            refresh();
        });

        rv.setAdapter(adapter);

        Button btnRefresh = v.findViewById(R.id.btnRefresh);
        btnRefresh.setOnClickListener(view -> refresh());

        refresh();
        return v;
    }

    private void refresh() {
        List<Reservation> all = repo.getAllReservations();
        adapter.setItems(all);
    }
}
