package com.example.restaurantmanagement.ui.guest;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import com.example.restaurantmanagement.R;
import com.example.restaurantmanagement.models.MenuItem;

public class MenuAdapter extends RecyclerView.Adapter<MenuAdapter.VH> {

    private final List<MenuItem> items = new ArrayList<>();

    public void setItems(List<MenuItem> newItems) {
        items.clear();
        if (newItems != null) items.addAll(newItems);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_menu, parent, false);
        return new VH(v);
    }

    @Override
    public void onBindViewHolder(@NonNull VH holder, int position) {
        MenuItem m = items.get(position);
        holder.tvName.setText(m.name);
        holder.tvCategory.setText(m.category);
        holder.tvPrice.setText(String.format(Locale.UK, "£%.2f", m.price));
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    static class VH extends RecyclerView.ViewHolder {
        TextView tvName, tvCategory, tvPrice;

        VH(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tvName);
            tvCategory = itemView.findViewById(R.id.tvCategory);
            tvPrice = itemView.findViewById(R.id.tvPrice);
        }
    }
}
