package com.example.restaurantmanagement.ui.guest;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.restaurantmanagement.R;
import com.example.restaurantmanagement.data.local.MenuRepository;

public class GuestHomeFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_guest_home, container, false);

        RecyclerView rv = v.findViewById(R.id.rvNewAdditions);
        rv.setLayoutManager(new LinearLayoutManager(requireContext()));

        MenuAdapter adapter = new MenuAdapter();
        rv.setAdapter(adapter);

        MenuRepository repo = new MenuRepository(requireContext());
        adapter.setItems(repo.getNewAdditions(10)); // show top 10

        return v;
    }
}

