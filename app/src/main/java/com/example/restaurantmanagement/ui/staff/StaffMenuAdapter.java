package com.example.restaurantmanagement.ui.staff;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import com.example.restaurantmanagement.R;
import com.example.restaurantmanagement.models.MenuItem;

public class StaffMenuAdapter extends RecyclerView.Adapter<StaffMenuAdapter.VH> {

    public interface Listener {
        void onEdit(MenuItem item);
        void onDelete(MenuItem item);
    }

    private final List<MenuItem> items = new ArrayList<>();
    private final Listener listener;

    public StaffMenuAdapter(Listener listener) {
        this.listener = listener;
    }

    public void setItems(List<MenuItem> newItems) {
        items.clear();
        if (newItems != null) items.addAll(newItems);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_menu_staff, parent, false);
        return new VH(v);
    }

    @Override
    public void onBindViewHolder(@NonNull VH h, int position) {
        MenuItem m = items.get(position);

        h.tvName.setText(m.name);
        String meta = String.format(Locale.UK, "£%.2f • New: %s", m.price, (m.isNew == 1 ? "Yes" : "No"));
        h.tvMeta.setText(meta);

        h.btnEdit.setOnClickListener(v -> listener.onEdit(m));
        h.btnDelete.setOnClickListener(v -> listener.onDelete(m));
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    static class VH extends RecyclerView.ViewHolder {
        TextView tvName, tvMeta;
        Button btnEdit, btnDelete;

        VH(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tvName);
            tvMeta = itemView.findViewById(R.id.tvMeta);
            btnEdit = itemView.findViewById(R.id.btnEdit);
            btnDelete = itemView.findViewById(R.id.btnDelete);
        }
    }
}
