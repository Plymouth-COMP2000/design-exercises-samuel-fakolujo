package com.example.restaurantmanagement.ui.guest;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.restaurantmanagement.R;
import com.example.restaurantmanagement.data.local.MenuRepository;

public class GuestMenuFragment extends Fragment {

    private MenuRepository repo;
    private MenuAdapter adapter;
    private TextView tvSelected;

    private static final String CAT_STARTERS = "Starters";
    private static final String CAT_MAIN = "Main Course";
    private static final String CAT_SIDES = "Sides";
    private static final String CAT_DESSERTS = "Desserts";
    private static final String CAT_DRINKS = "Cocktails & Drinks";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_guest_menu, container, false);

        repo = new MenuRepository(requireContext());
        tvSelected = v.findViewById(R.id.tvSelectedCategory);

        RecyclerView rv = v.findViewById(R.id.rvMenu);
        rv.setLayoutManager(new LinearLayoutManager(requireContext()));
        adapter = new MenuAdapter();
        rv.setAdapter(adapter);

        Button btnStarters = v.findViewById(R.id.btnStarters);
        Button btnMain = v.findViewById(R.id.btnMain);
        Button btnSides = v.findViewById(R.id.btnSides);
        Button btnDesserts = v.findViewById(R.id.btnDesserts);
        Button btnDrinks = v.findViewById(R.id.btnDrinks);

        btnStarters.setOnClickListener(view -> loadCategory(CAT_STARTERS));
        btnMain.setOnClickListener(view -> loadCategory(CAT_MAIN));
        btnSides.setOnClickListener(view -> loadCategory(CAT_SIDES));
        btnDesserts.setOnClickListener(view -> loadCategory(CAT_DESSERTS));
        btnDrinks.setOnClickListener(view -> loadCategory(CAT_DRINKS));

        // default category
        loadCategory(CAT_STARTERS);

        return v;
    }

    private void loadCategory(String category) {
        tvSelected.setText(category);
        adapter.setItems(repo.getByCategory(category));
    }
}
