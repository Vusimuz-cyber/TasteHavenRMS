package com.tastehaven.rms.activities;

import android.os.Bundle;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.tastehaven.rms.R;

public class ViewReportsActivity extends AppCompatActivity {
    TextView tvSales, tvReservations, tvFeedback;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_reports);

        tvSales = findViewById(R.id.tvSales);
        tvReservations = findViewById(R.id.tvReservations);
        tvFeedback = findViewById(R.id.tvFeedback);

        loadReports();
    }

    private void loadReports() {
        // Total Sales (simplified, assumes order totals are stored)
        FirebaseDatabase.getInstance().getReference("orders")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot snapshot) {
                        long count = snapshot.getChildrenCount();
                        // Placeholder: Assume each order is $50 for demo
                        tvSales.setText("Total Sales: $" + (count * 50));
                    }

                    @Override
                    public void onCancelled(DatabaseError error) {}
                });

        // Total Reservations
        FirebaseDatabase.getInstance().getReference("reservations")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot snapshot) {
                        long count = snapshot.getChildrenCount();
                        tvReservations.setText("Total Reservations: " + count);
                    }

                    @Override
                    public void onCancelled(DatabaseError error) {}
                });

        // Average Feedback Rating
        FirebaseDatabase.getInstance().getReference("feedback")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot snapshot) {
                        float totalRating = 0;
                        long count = 0;
                        for (DataSnapshot feedbackSnapshot : snapshot.getChildren()) {
                            Float rating = feedbackSnapshot.child("rating").getValue(Float.class);
                            if (rating != null) {
                                totalRating += rating;
                                count++;
                            }
                        }
                        float average = count > 0 ? totalRating / count : 0;
                        tvFeedback.setText(String.format("Average Rating: %.1f", average));
                    }

                    @Override
                    public void onCancelled(DatabaseError error) {}
                });
    }
}
