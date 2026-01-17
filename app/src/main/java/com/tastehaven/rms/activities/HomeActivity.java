package com.tastehaven.rms.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.tastehaven.rms.R;

public class HomeActivity extends AppCompatActivity {
    Button btnViewMenu, btnMakeReservation, btnViewReservations, btnFeedback, btnLogout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        btnViewMenu = findViewById(R.id.btnViewMenu);
        btnMakeReservation = findViewById(R.id.btnMakeReservation);
        btnViewReservations = findViewById(R.id.btnViewReservations);
        btnFeedback = findViewById(R.id.btnFeedback);
        btnLogout = findViewById(R.id.btnLogout);

        btnViewMenu.setOnClickListener(v -> startActivity(new Intent(this, MenuActivity.class)));
        btnMakeReservation.setOnClickListener(v -> startActivity(new Intent(this, ReservationActivity.class)));
        btnViewReservations.setOnClickListener(v -> startActivity(new Intent(this, ReservationStatusActivity.class)));
        btnFeedback.setOnClickListener(v -> startActivity(new Intent(this, FeedbackActivity.class)));
        btnLogout.setOnClickListener(v -> {
            FirebaseAuth.getInstance().signOut();
            startActivity(new Intent(this, LoginActivity.class));
            finish();
        });
    }
}
