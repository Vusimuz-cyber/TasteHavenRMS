package com.tastehaven.rms.activities;

import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.tastehaven.rms.R;
import com.tastehaven.rms.models.Order;

public class UpdateOrderStatusActivity extends AppCompatActivity {
    TextInputEditText etTableNumber, etNotes;
    Button btnSubmitOrder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_order_status);

        etTableNumber = findViewById(R.id.etTableNumber);
        etNotes = findViewById(R.id.etNotes);
        btnSubmitOrder = findViewById(R.id.btnSubmitOrder);

        btnSubmitOrder.setOnClickListener(v -> placeOrder());
    }

    private void placeOrder() {
        String tableNumber = etTableNumber.getText().toString().trim();
        String notes = etNotes.getText().toString().trim();

        if (tableNumber.isEmpty()) {
            Toast.makeText(this, "Please enter table number", Toast.LENGTH_SHORT).show();
            return;
        }

        String waiterId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        Order order = new Order(tableNumber, waiterId, System.currentTimeMillis(), "received", notes);
        FirebaseDatabase.getInstance().getReference("orders").push()
                .setValue(order)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Toast.makeText(this, "Order placed", Toast.LENGTH_SHORT).show();
                        finish();
                    } else {
                        Toast.makeText(this, "Failed to place order", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
