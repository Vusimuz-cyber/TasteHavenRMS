package com.tastehaven.rms.activities;

import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.tastehaven.rms.R;
import com.tastehaven.rms.adapters.MenuAdapter;
import com.tastehaven.rms.models.MenuItem;
import java.util.ArrayList;
import java.util.List;

public class ManageMenuActivity extends AppCompatActivity {
    TextInputEditText etItemName, etDescription, etPrice;
    Button btnAddItem;
    RecyclerView rvMenuItems;
    MenuAdapter adapter;
    List<MenuItem> menuItems;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_menu);

        etItemName = findViewById(R.id.etItemName);
        etDescription = findViewById(R.id.etDescription);
        etPrice = findViewById(R.id.etPrice);
        btnAddItem = findViewById(R.id.btnAddItem);
        rvMenuItems = findViewById(R.id.rvMenuItems);

        menuItems = new ArrayList<>();
        adapter = new MenuAdapter(menuItems);
        rvMenuItems.setLayoutManager(new LinearLayoutManager(this));
        rvMenuItems.setAdapter(adapter);

        btnAddItem.setOnClickListener(v -> addMenuItem());
        loadMenuItems();
    }

    private void addMenuItem() {
        String name = etItemName.getText().toString().trim();
        String description = etDescription.getText().toString().trim();
        String priceStr = etPrice.getText().toString().trim();

        if (name.isEmpty() || description.isEmpty() || priceStr.isEmpty()) {
            Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        double price = Double.parseDouble(priceStr);
        MenuItem item = new MenuItem(name, description, price, true);
        FirebaseDatabase.getInstance().getReference("menu_items").push()
                .setValue(item)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Toast.makeText(this, "Menu item added", Toast.LENGTH_SHORT).show();
                        etItemName.setText("");
                        etDescription.setText("");
                        etPrice.setText("");
                    } else {
                        Toast.makeText(this, "Failed to add item", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void loadMenuItems() {
        FirebaseDatabase.getInstance().getReference("menu_items")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot snapshot) {
                        menuItems.clear();
                        for (DataSnapshot itemSnapshot : snapshot.getChildren()) {
                            MenuItem item = itemSnapshot.getValue(MenuItem.class);
                            menuItems.add(item);
                        }
                        adapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onCancelled(DatabaseError error) {}
                });
    }
}
