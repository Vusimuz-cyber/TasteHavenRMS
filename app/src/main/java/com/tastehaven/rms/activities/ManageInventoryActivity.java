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
import com.tastehaven.rms.adapters.InventoryAdapter;
import com.tastehaven.rms.models.InventoryItem;
import java.util.ArrayList;
import java.util.List;

public class ManageInventoryActivity extends AppCompatActivity {
    TextInputEditText etItemName, etQuantity, etThreshold;
    Button btnUpdateInventory;
    RecyclerView rvInventory;
    InventoryAdapter adapter;
    List<InventoryItem> inventoryItems;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_inventory);

        etItemName = findViewById(R.id.etItemName);
        etQuantity = findViewById(R.id.etQuantity);
        etThreshold = findViewById(R.id.etThreshold);
        btnUpdateInventory = findViewById(R.id.btnUpdateInventory);
        rvInventory = findViewById(R.id.rvInventory);

        inventoryItems = new ArrayList<>();
        adapter = new InventoryAdapter(inventoryItems);
        rvInventory.setLayoutManager(new LinearLayoutManager(this));
        rvInventory.setAdapter(adapter);

        btnUpdateInventory.setOnClickListener(v -> updateInventory());
        loadInventory();
    }

    private void updateInventory() {
        String itemName = etItemName.getText().toString().trim();
        String quantityStr = etQuantity.getText().toString().trim();
        String thresholdStr = etThreshold.getText().toString().trim();

        if (itemName.isEmpty() || quantityStr.isEmpty() || thresholdStr.isEmpty()) {
            Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        int quantity = Integer.parseInt(quantityStr);
        int threshold = Integer.parseInt(thresholdStr);
        InventoryItem item = new InventoryItem(itemName, quantity, threshold, System.currentTimeMillis());
        FirebaseDatabase.getInstance().getReference("inventory").push()
                .setValue(item)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Toast.makeText(this, "Inventory updated", Toast.LENGTH_SHORT).show();
                        etItemName.setText("");
                        etQuantity.setText("");
                        etThreshold.setText("");
                    } else {
                        Toast.makeText(this, "Failed to update inventory", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void loadInventory() {
        FirebaseDatabase.getInstance().getReference("inventory")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot snapshot) {
                        inventoryItems.clear();
                        for (DataSnapshot itemSnapshot : snapshot.getChildren()) {
                            InventoryItem item = itemSnapshot.getValue(InventoryItem.class);
                            inventoryItems.add(item);
                        }
                        adapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onCancelled(DatabaseError error) {}
                });
    }
}
