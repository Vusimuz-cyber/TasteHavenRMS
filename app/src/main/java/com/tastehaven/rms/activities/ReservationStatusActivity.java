package com.tastehaven.rms.activities;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.tastehaven.rms.R;
import com.tastehaven.rms.adapters.ReservationAdapter;
import com.tastehaven.rms.models.Reservation;

import java.util.ArrayList;
import java.util.List;

public class ReservationStatusActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private TextView emptyStateTextView;
    private ReservationAdapter adapter;
    private List<Reservation> reservations;
    private DatabaseReference reservationsRef;
    private FirebaseAuth mAuth;
    private static final String TAG = "ReservationStatus";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reservation_status);

        // Initialize Firebase
        mAuth = FirebaseAuth.getInstance();
        reservationsRef = FirebaseDatabase.getInstance().getReference("reservations");

        // Initialize UI
        recyclerView = findViewById(R.id.reservationsRecyclerView);
        emptyStateTextView = findViewById(R.id.emptyStateTextView);
        if (recyclerView == null) {
            Log.e(TAG, "RecyclerView not found");
            Toast.makeText(this, "Error: UI initialization failed", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        // Setup RecyclerView
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        reservations = new ArrayList<>();
        adapter = new ReservationAdapter(reservations, reservation -> {
            if (reservation != null) {
                Toast.makeText(this, "Selected: Table " + (reservation.getTableNumber() != null ? reservation.getTableNumber() : "N/A") + ", Status: " + (reservation.getStatus() != null ? reservation.getStatus() : "N/A"), Toast.LENGTH_SHORT).show();
            }
        });
        recyclerView.setAdapter(adapter);

        // Load user's reservations
        loadReservations();
    }

    private void loadReservations() {
        String userId = mAuth.getCurrentUser() != null ? mAuth.getCurrentUser().getUid() : null;
        if (userId == null) {
            Log.e(TAG, "User not authenticated");
            Toast.makeText(this, "Error: User not authenticated", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        reservationsRef.orderByChild("userId").equalTo(userId)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        reservations.clear();
                        Log.d(TAG, "DataSnapshot exists: " + dataSnapshot.exists());
                        if (dataSnapshot.exists()) {
                            for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                try {
                                    Reservation reservation = snapshot.getValue(Reservation.class);
                                    if (reservation != null) {
                                        reservation.setReservationId(snapshot.getKey());
                                        reservations.add(reservation);
                                        Log.d(TAG, "Added reservation: " + reservation.getReservationId());
                                    } else {
                                        Log.w(TAG, "Null reservation for key: " + snapshot.getKey());
                                    }
                                } catch (Exception e) {
                                    Log.e(TAG, "Error parsing reservation: " + e.getMessage());
                                }
                            }
                        } else {
                            Log.d(TAG, "No reservations found for user: " + userId);
                        }

                        // Update UI
                        adapter.notifyDataSetChanged();
                        emptyStateTextView.setVisibility(reservations.isEmpty() ? View.VISIBLE : View.GONE);
                        recyclerView.setVisibility(reservations.isEmpty() ? View.GONE : View.VISIBLE);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Log.e(TAG, "Database error: " + databaseError.getMessage());
                        Toast.makeText(ReservationStatusActivity.this,
                                "Failed to load reservations: " + databaseError.getMessage(),
                                Toast.LENGTH_LONG).show();
                    }
                });
    }
}
