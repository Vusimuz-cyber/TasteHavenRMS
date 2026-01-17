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
import com.tastehaven.rms.adapters.OrderAdapter;
import com.tastehaven.rms.models.Order;
import java.util.ArrayList;
import java.util.List;

public class ViewOrdersActivity extends AppCompatActivity {
    RecyclerView rvOrders;
    OrderAdapter adapter;
    List<Order> orders;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_orders);

        rvOrders = findViewById(R.id.rvOrders);
        orders = new ArrayList<>();
        adapter = new OrderAdapter(orders, order -> {
            // Chef updates order status
            String newStatus = order.getStatus().equals("received") ? "preparing" : "ready";
            FirebaseDatabase.getInstance().getReference("orders").child(order.getId())
                    .child("status").setValue(newStatus)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            loadOrders(); // Refresh list
                        }
                    });
        });
        rvOrders.setLayoutManager(new LinearLayoutManager(this));
        rvOrders.setAdapter(adapter);

        loadOrders();
    }

    private void loadOrders() {
        FirebaseDatabase.getInstance().getReference("orders")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot snapshot) {
                        orders.clear();
                        for (DataSnapshot orderSnapshot : snapshot.getChildren()) {
                            Order order = orderSnapshot.getValue(Order.class);
                            order.setId(orderSnapshot.getKey());
                            orders.add(order);
                        }
                        adapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onCancelled(DatabaseError error) {}
                });
    }
}
