package com.example.restaurantmanagement.ui.staff;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.List;

import com.example.restaurantmanagement.R;
import com.example.restaurantmanagement.data.local.TaskRepository;

public class StaffHomeFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_staff_home, container, false);

        TaskRepository repo = new TaskRepository(requireContext());
        repo.seedIfEmpty();

        LinearLayout tasksContainer = v.findViewById(R.id.tasksContainer);
        tasksContainer.removeAllViews();


        List<TaskRepository.Task> tasks = repo.getAll();

        for (TaskRepository.Task t : tasks) {
            CheckBox cb = new CheckBox(requireContext());
            cb.setText(t.text);
            cb.setChecked(t.done == 1);

            cb.setOnCheckedChangeListener((buttonView, isChecked) -> {
                repo.setDone(t.id, isChecked ? 1 : 0);
            });

            tasksContainer.addView(cb);
        }

        return v;
    }
}
