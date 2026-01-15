package com.example.restaurantmanagement.ui.guest;


import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.Calendar;
import java.util.Locale;

import com.example.restaurantmanagement.R;
import com.example.restaurantmanagement.data.local.ReservationRepository;
import com.example.restaurantmanagement.utils.PrefsManager;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;




public class GuestReservationsFragment extends Fragment {

    private ReservationAdapter upcomingAdapter;
    private ReservationAdapter previousAdapter;

    private ReservationRepository repo;
    private PrefsManager prefs;

    private TextView tvSelectedDate, tvConfirmation;
    private Spinner spTimeSlot;
    private EditText etGuests, etSpecial;

    private String selectedDate = null; // YYYY-MM-DD

    private final String[] timeSlots = new String[]{
            "12:00-14:00",
            "14:00-16:00",
            "16:00-18:00",
            "18:00-20:00",
            "20:00-22:00"
    };

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_guest_reservations, container, false);

        repo = new ReservationRepository(requireContext());
        prefs = new PrefsManager(requireContext());

        Button btnPickDate = v.findViewById(R.id.btnPickDate);
        Button btnBook = v.findViewById(R.id.btnBook);
        tvSelectedDate = v.findViewById(R.id.tvSelectedDate);
        tvConfirmation = v.findViewById(R.id.tvConfirmation);

        spTimeSlot = v.findViewById(R.id.spTimeSlot);
        etGuests = v.findViewById(R.id.etGuests);
        etSpecial = v.findViewById(R.id.etSpecialRequest);

        // spinner setup
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                requireContext(),
                android.R.layout.simple_spinner_dropdown_item,
                timeSlots
        );
        spTimeSlot.setAdapter(adapter);

        btnPickDate.setOnClickListener(view -> showDatePicker());

        btnBook.setOnClickListener(view -> bookReservation());

        RecyclerView rvUpcoming = v.findViewById(R.id.rvUpcoming);
        RecyclerView rvPrevious = v.findViewById(R.id.rvPrevious);

        rvUpcoming.setLayoutManager(new LinearLayoutManager(requireContext()));
        rvPrevious.setLayoutManager(new LinearLayoutManager(requireContext()));

        upcomingAdapter = new ReservationAdapter(true, r -> {
            repo.cancelReservation(r.id);
            Toast.makeText(requireContext(), "Reservation cancelled", Toast.LENGTH_SHORT).show();
            refreshLists();
        });

        previousAdapter = new ReservationAdapter(false, null);

        rvUpcoming.setAdapter(upcomingAdapter);
        rvPrevious.setAdapter(previousAdapter);

// Load lists now
        refreshLists();

        return v;
    }

    private void showDatePicker() {
        Calendar cal = Calendar.getInstance();
        int y = cal.get(Calendar.YEAR);
        int m = cal.get(Calendar.MONTH);
        int d = cal.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog dp = new DatePickerDialog(requireContext(), (datePicker, year, month, day) -> {
            // month is 0-based
            int realMonth = month + 1;
            selectedDate = String.format(Locale.UK, "%04d-%02d-%02d", year, realMonth, day);
            tvSelectedDate.setText("Selected: " + selectedDate);
        }, y, m, d);

        // prevent picking past dates
        dp.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);

        dp.show();
    }

    private void bookReservation() {
        String username = prefs.getUsername();
        if (username == null) {
            Toast.makeText(requireContext(), "Please login again.", Toast.LENGTH_SHORT).show();
            return;
        }

        if (selectedDate == null) {
            Toast.makeText(requireContext(), "Please pick a date.", Toast.LENGTH_SHORT).show();
            return;
        }

        String timeSlot = spTimeSlot.getSelectedItem().toString();

        String guestsStr = etGuests.getText().toString().trim();
        if (guestsStr.isEmpty()) {
            Toast.makeText(requireContext(), "Enter number of guests.", Toast.LENGTH_SHORT).show();
            return;
        }

        int guests;
        try {
            guests = Integer.parseInt(guestsStr);
        } catch (Exception e) {
            Toast.makeText(requireContext(), "Guests must be a number.", Toast.LENGTH_SHORT).show();
            return;
        }

        if (guests < 1 || guests > 20) {
            Toast.makeText(requireContext(), "Guests must be between 1 and 20.", Toast.LENGTH_SHORT).show();
            return;
        }

        String special = etSpecial.getText().toString().trim();

        long id = repo.createReservation(username, selectedDate, timeSlot, guests, special);

        if (id > 0) {
            tvConfirmation.setText("Thank you! Reservation booked.\n"
                    + "Date: " + selectedDate + "\n"
                    + "Time: " + timeSlot + "\n"
                    + "Guests: " + guests);

            // clear inputs (optional)
            etGuests.setText("");
            etSpecial.setText("");
            Toast.makeText(requireContext(), "Reservation booked!",
            Toast.LENGTH_SHORT).show();
            refreshLists();

        } else {
            Toast.makeText(requireContext(), "Booking failed. Try again.", Toast.LENGTH_SHORT).show();
        }
    }

    private void refreshLists() {
        String username = prefs.getUsername();
        if (username == null) return;

        List<com.example.restaurantmanagement.models.Reservation> all = repo.getUserReservations(username);

        LocalDate today = LocalDate.now();

        List<com.example.restaurantmanagement.models.Reservation> upcoming = new ArrayList<>();
        List<com.example.restaurantmanagement.models.Reservation> previous = new ArrayList<>();

        for (com.example.restaurantmanagement.models.Reservation r : all) {
            // If cancelled -> previous
            if ("CANCELLED".equalsIgnoreCase(r.status)) {
                previous.add(r);
                continue;
            }

            // Date format is YYYY-MM-DD, so LocalDate.parse works
            LocalDate rDate;
            try {
                rDate = LocalDate.parse(r.date);
            } catch (Exception e) {
                // If date is malformed, put into previous so it doesn't break UI
                previous.add(r);
                continue;
            }

            if (rDate.isBefore(today)) previous.add(r);
            else upcoming.add(r);
        }

        upcomingAdapter.setItems(upcoming);
        previousAdapter.setItems(previous);
    }


}
