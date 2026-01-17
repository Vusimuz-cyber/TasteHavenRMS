package com.tastehaven.rms.activities;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.tastehaven.rms.R;
import com.tastehaven.rms.adapters.MenuAdapter;
import com.tastehaven.rms.models.MenuItem;
import java.util.ArrayList;
import java.util.List;

public class MenuActivity extends AppCompatActivity {
    RecyclerView rvMenu;
    MenuAdapter adapter;
    List<MenuItem> menuItems;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        rvMenu = findViewById(R.id.rvMenu);
        menuItems = new ArrayList<>();
        adapter = new MenuAdapter(menuItems);
        rvMenu.setLayoutManager(new LinearLayoutManager(this));
        rvMenu.setAdapter(adapter);

        loadMenuItems();
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