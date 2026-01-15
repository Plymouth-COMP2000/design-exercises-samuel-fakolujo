package com.example.restaurantmanagement.ui.staff;


import android.app.AlertDialog;
import android.os.Bundle;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.restaurantmanagement.R;
import com.example.restaurantmanagement.data.local.MenuRepository;
import com.example.restaurantmanagement.models.MenuItem;

public class StaffMenuFragment extends Fragment {

    private MenuRepository repo;
    private StaffMenuAdapter adapter;

    private Spinner spCategory;

    private final String[] categories = new String[]{
            "Starters",
            "Main Course",
            "Sides",
            "Desserts",
            "Drinks"
    };

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_staff_menu, container, false);

        repo = new MenuRepository(requireContext());

        spCategory = v.findViewById(R.id.spCategory);
        ArrayAdapter<String> catAdapter = new ArrayAdapter<>(
                requireContext(),
                android.R.layout.simple_spinner_dropdown_item,
                categories
        );
        spCategory.setAdapter(catAdapter);

        RecyclerView rv = v.findViewById(R.id.rvStaffMenu);
        rv.setLayoutManager(new LinearLayoutManager(requireContext()));

        adapter = new StaffMenuAdapter(new StaffMenuAdapter.Listener() {
            @Override
            public void onEdit(MenuItem item) {
                showEditDialog(item);
            }

            @Override
            public void onDelete(MenuItem item) {
                repo.deleteItem(item.id);
                Toast.makeText(requireContext(), "Deleted", Toast.LENGTH_SHORT).show();
                refresh();
            }
        });

        rv.setAdapter(adapter);

        v.findViewById(R.id.btnAdd).setOnClickListener(view -> showAddDialog());

        // refresh whenever category changes
        spCategory.setOnItemSelectedListener(new android.widget.AdapterView.OnItemSelectedListener() {
            @Override public void onItemSelected(android.widget.AdapterView<?> parent, View view, int position, long id) {
                refresh();
            }
            @Override public void onNothingSelected(android.widget.AdapterView<?> parent) { }
        });

        refresh();
        return v;
    }

    private void refresh() {
        String cat = spCategory.getSelectedItem().toString();
        adapter.setItems(repo.getByCategory(cat));
    }

    private void showAddDialog() {
        String cat = spCategory.getSelectedItem().toString();
        showDialog("Add menu item", null, cat);
    }

    private void showEditDialog(MenuItem item) {
        showDialog("Edit menu item", item, item.category);
    }

    private void showDialog(String title, @Nullable MenuItem existing, String defaultCategory) {

        LinearLayout layout = new LinearLayout(requireContext());
        layout.setOrientation(LinearLayout.VERTICAL);
        int pad = (int) (16 * getResources().getDisplayMetrics().density);
        layout.setPadding(pad, pad, pad, pad);

        EditText etName = new EditText(requireContext());
        etName.setHint("Name");
        layout.addView(etName);

        EditText etPrice = new EditText(requireContext());
        etPrice.setHint("Price (e.g. 6.50)");
        etPrice.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
        layout.addView(etPrice);

        Spinner spCat = new Spinner(requireContext());
        ArrayAdapter<String> catAdapter = new ArrayAdapter<>(requireContext(),
                android.R.layout.simple_spinner_dropdown_item, categories);
        spCat.setAdapter(catAdapter);
        layout.addView(spCat);

        CheckBox cbNew = new CheckBox(requireContext());
        cbNew.setText("Mark as New Addition");
        layout.addView(cbNew);

        // prefill if editing
        if (existing != null) {
            etName.setText(existing.name);
            etPrice.setText(String.valueOf(existing.price));
            cbNew.setChecked(existing.isNew == 1);

            // set category selection
            for (int i = 0; i < categories.length; i++) {
                if (categories[i].equals(existing.category)) {
                    spCat.setSelection(i);
                    break;
                }
            }
        } else {
            // default category
            for (int i = 0; i < categories.length; i++) {
                if (categories[i].equals(defaultCategory)) {
                    spCat.setSelection(i);
                    break;
                }
            }
        }

        new AlertDialog.Builder(requireContext())
                .setTitle(title)
                .setView(layout)
                .setPositiveButton("Save", (d, which) -> {

                    String name = etName.getText().toString().trim();
                    String priceStr = etPrice.getText().toString().trim();
                    String cat = spCat.getSelectedItem().toString();
                    int isNew = cbNew.isChecked() ? 1 : 0;

                    if (name.isEmpty() || priceStr.isEmpty()) {
                        Toast.makeText(requireContext(), "Name and price required", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    double price;
                    try {
                        price = Double.parseDouble(priceStr);
                    } catch (Exception e) {
                        Toast.makeText(requireContext(), "Price must be a number", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    if (existing == null) {
                        repo.addItem(name, cat, price, isNew);
                        Toast.makeText(requireContext(), "Added", Toast.LENGTH_SHORT).show();
                    } else {
                        repo.updateItem(existing.id, name, cat, price, isNew);
                        Toast.makeText(requireContext(), "Updated", Toast.LENGTH_SHORT).show();
                    }

                    // if category changed, update spinner to show it
                    for (int i = 0; i < categories.length; i++) {
                        if (categories[i].equals(cat)) {
                            spCategory.setSelection(i);
                            break;
                        }
                    }

                    refresh();
                })
                .setNegativeButton("Cancel", null)
                .show();
    }
}
