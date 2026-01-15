package com.example.restaurantmanagement.ui.guest;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import com.example.restaurantmanagement.R;
import com.example.restaurantmanagement.models.Reservation;

public class ReservationAdapter extends RecyclerView.Adapter<ReservationAdapter.VH> {

    public interface OnCancelClick {
        void onCancel(Reservation r);
    }

    private final List<Reservation> items = new ArrayList<>();
    private final boolean showCancelButton;
    private final OnCancelClick cancelClick;

    public ReservationAdapter(boolean showCancelButton, OnCancelClick cancelClick) {
        this.showCancelButton = showCancelButton;
        this.cancelClick = cancelClick;
    }

    public void setItems(List<Reservation> newItems) {
        items.clear();
        if (newItems != null) items.addAll(newItems);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_reservation, parent, false);
        return new VH(v);
    }

    @Override
    public void onBindViewHolder(@NonNull VH h, int position) {
        Reservation r = items.get(position);

        h.tvTopLine.setText(r.date + " | " + r.timeSlot);
        h.tvGuests.setText("Guests: " + r.guests);

        String special = (r.specialRequest == null || r.specialRequest.trim().isEmpty())
                ? "None"
                : r.specialRequest;
        h.tvSpecial.setText("Special request: " + special);

        h.tvStatus.setText("Status: " + r.status);

        if (showCancelButton && "BOOKED".equalsIgnoreCase(r.status)) {
            h.btnCancel.setVisibility(View.VISIBLE);
            h.btnCancel.setOnClickListener(v -> {
                if (cancelClick != null) cancelClick.onCancel(r);
            });
        } else {
            h.btnCancel.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    static class VH extends RecyclerView.ViewHolder {
        TextView tvTopLine, tvGuests, tvSpecial, tvStatus;
        Button btnCancel;

        VH(@NonNull View itemView) {
            super(itemView);
            tvTopLine = itemView.findViewById(R.id.tvTopLine);
            tvGuests = itemView.findViewById(R.id.tvGuests);
            tvSpecial = itemView.findViewById(R.id.tvSpecial);
            tvStatus = itemView.findViewById(R.id.tvStatus);
            btnCancel = itemView.findViewById(R.id.btnCancel);
        }
    }
}

