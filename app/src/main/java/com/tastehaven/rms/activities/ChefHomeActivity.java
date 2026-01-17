package com.tastehaven.rms.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.tastehaven.rms.R;

public class ChefHomeActivity extends AppCompatActivity {
    Button btnViewOrders, btnLogout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chef_home);

        btnViewOrders = findViewById(R.id.btnViewOrders);
        btnLogout = findViewById(R.id.btnLogout);

        btnViewOrders.setOnClickListener(v -> startActivity(new Intent(this, ViewOrdersActivity.class)));
        btnLogout.setOnClickListener(v -> {
            FirebaseAuth.getInstance().signOut();
            startActivity(new Intent(this, LoginActivity.class));
            finish();
        });
    }
}
