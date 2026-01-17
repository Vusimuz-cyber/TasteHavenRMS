package com.tastehaven.rms.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.tastehaven.rms.R;

public class ManagerHomeActivity extends AppCompatActivity {
    Button btnManageMenu, btnManageReservations, btnManageInventory, btnViewReports, btnLogout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manager_home);

        btnManageMenu = findViewById(R.id.btnManageMenu);
        btnManageReservations = findViewById(R.id.btnManageReservations);
        btnManageInventory = findViewById(R.id.btnManageInventory);
        btnViewReports = findViewById(R.id.btnViewReports);
        btnLogout = findViewById(R.id.btnLogout);

        btnManageMenu.setOnClickListener(v -> startActivity(new Intent(this, ManageMenuActivity.class)));
        btnManageReservations.setOnClickListener(v -> startActivity(new Intent(this, ManageReservationsActivity.class)));
        btnManageInventory.setOnClickListener(v -> startActivity(new Intent(this, ManageInventoryActivity.class)));
        btnViewReports.setOnClickListener(v -> startActivity(new Intent(this, ViewReportsActivity.class)));
        btnLogout.setOnClickListener(v -> {
            FirebaseAuth.getInstance().signOut();
            startActivity(new Intent(this, LoginActivity.class));
            finish();
        });
    }
}
