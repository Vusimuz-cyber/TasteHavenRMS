package com.tastehaven.rms.activities;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
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

public class ManageReservationsActivity extends AppCompatActivity {

    private RecyclerView reservationsRecyclerView;
    private TextView emptyStateTextView;
    private ReservationAdapter reservationAdapter;
    private List<Reservation> reservationList;
    private DatabaseReference reservationsRef;
    private static final String TAG = "ManageReservations";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_reservations);

        reservationsRef = FirebaseDatabase.getInstance().getReference("reservations");

        reservationsRecyclerView = findViewById(R.id.reservationsRecyclerView);
        emptyStateTextView = findViewById(R.id.emptyStateTextView);
        if (reservationsRecyclerView == null) {
            Toast.makeText(this, "Error: RecyclerView not found", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        reservationsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        reservationList = new ArrayList<>();
        reservationAdapter = new ReservationAdapter(reservationList);
        reservationsRecyclerView.setAdapter(reservationAdapter);

        loadReservations();
    }

    private void loadReservations() {
        reservationsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                reservationList.clear();
                Log.d(TAG, "DataSnapshot exists: " + dataSnapshot.exists());
                if (dataSnapshot.exists()) {
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        try {
                            Reservation reservation = snapshot.getValue(Reservation.class);
                            if (reservation != null) {
                                reservation.setReservationId(snapshot.getKey());
                                reservationList.add(reservation);
                                Log.d(TAG, "Added reservation: " + reservation.getReservationId());
                            } else {
                                Log.w(TAG, "Null reservation for key: " + snapshot.getKey());
                            }
                        } catch (Exception e) {
                            Log.e(TAG, "Error parsing reservation: " + e.getMessage());
                        }
                    }
                } else {
                    Log.d(TAG, "No reservations found in database");
                }

                reservationAdapter.notifyDataSetChanged();
                emptyStateTextView.setVisibility(reservationList.isEmpty() ? View.VISIBLE : View.GONE);
                reservationsRecyclerView.setVisibility(reservationList.isEmpty() ? View.GONE : View.VISIBLE);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e(TAG, "Database error: " + databaseError.getMessage());
                Toast.makeText(ManageReservationsActivity.this,
                        "Failed to load reservations: " + databaseError.getMessage(),
                        Toast.LENGTH_LONG).show();
            }
        });
    }
}