package com.tastehaven.rms.activities;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.tastehaven.rms.R;
import com.tastehaven.rms.models.Reservation;

public class ReservationActivity extends AppCompatActivity {

    private EditText tableNumberEditText, dateEditText, timeEditText;
    private Button submitButton;
    private FirebaseAuth mAuth;
    private DatabaseReference reservationsRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reservation);

        // Initialize Firebase
        mAuth = FirebaseAuth.getInstance();
        reservationsRef = FirebaseDatabase.getInstance().getReference("reservations");

        // Initialize UI
        tableNumberEditText = findViewById(R.id.tableNumberEditText);
        dateEditText = findViewById(R.id.dateEditText);
        timeEditText = findViewById(R.id.timeEditText);
        submitButton = findViewById(R.id.submitButton);

        // Submit reservation
        submitButton.setOnClickListener(v -> submitReservation());
    }

    private void submitReservation() {
        String tableNumber = tableNumberEditText.getText().toString().trim();
        String date = dateEditText.getText().toString().trim();
        String time = timeEditText.getText().toString().trim();

        if (tableNumber.isEmpty() || date.isEmpty() || time.isEmpty()) {
            Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        String userId = mAuth.getCurrentUser().getUid();
        Reservation reservation = new Reservation();
        reservation.setUserId(userId);
        reservation.setTableNumber(tableNumber);
        reservation.setDate(date);
        reservation.setTime(time);
        reservation.setStatus("pending");

        String reservationId = reservationsRef.push().getKey();
        reservationsRef.child(reservationId).setValue(reservation)
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(this, "Reservation submitted", Toast.LENGTH_SHORT).show();
                    finish();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Failed to submit reservation: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }
}
