package com.tastehaven.rms.adapters;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.tastehaven.rms.R;
import com.tastehaven.rms.models.Reservation;

import java.util.List;

public class ReservationAdapter extends RecyclerView.Adapter<ReservationAdapter.ReservationViewHolder> {

    private List<Reservation> reservationList;
    private OnItemClickListener listener;
    private static final String TAG = "ReservationAdapter";

    public interface OnItemClickListener {
        void onItemClick(Reservation reservation);
    }

    public ReservationAdapter(List<Reservation> reservationList, OnItemClickListener listener) {
        this.reservationList = reservationList;
        this.listener = listener;
    }

    public ReservationAdapter(List<Reservation> reservationList) {
        this(reservationList, null);
    }

    @NonNull
    @Override
    public ReservationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_reservation, parent, false);
        return new ReservationViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ReservationViewHolder holder, int position) {
        Reservation reservation = reservationList.get(position);
        Log.d(TAG, "Binding reservation at position " + position + ": " + (reservation != null ? reservation.getReservationId() : "null"));
        if (reservation == null) {
            holder.tableNumberTextView.setText("Table: N/A");
            holder.dateTextView.setText("Date: N/A");
            holder.timeTextView.setText("Time: N/A");
            holder.statusTextView.setText("Status: N/A");
            holder.actionButtonsLayout.setVisibility(View.GONE);
            return;
        }

        holder.tableNumberTextView.setText("Table: " + (reservation.getTableNumber() != null ? reservation.getTableNumber() : "N/A"));
        holder.dateTextView.setText("Date: " + (reservation.getDate() != null ? reservation.getDate() : "N/A"));
        holder.timeTextView.setText("Time: " + (reservation.getTime() != null ? reservation.getTime() : "N/A"));
        holder.statusTextView.setText("Status: " + (reservation.getStatus() != null ? reservation.getStatus() : "N/A"));

        // Show action buttons only for pending reservations
        if ("pending".equals(reservation.getStatus())) {
            holder.actionButtonsLayout.setVisibility(View.VISIBLE);
            Log.d(TAG, "Showing action buttons for pending reservation: " + reservation.getReservationId());
        } else {
            holder.actionButtonsLayout.setVisibility(View.GONE);
        }

        // Handle Accept button click
        holder.acceptButton.setOnClickListener(v -> {
            if (reservation.getReservationId() != null) {
                updateReservationStatus(reservation.getReservationId(), "accepted", holder);
            }
        });

        // Handle Decline button click
        holder.declineButton.setOnClickListener(v -> {
            if (reservation.getReservationId() != null) {
                updateReservationStatus(reservation.getReservationId(), "declined", holder);
            }
        });

        // Handle item click
        if (listener != null) {
            holder.itemView.setOnClickListener(v -> listener.onItemClick(reservation));
        }
    }

    private void updateReservationStatus(String reservationId, String status, ReservationViewHolder holder) {
        DatabaseReference reservationRef = FirebaseDatabase.getInstance()
                .getReference("reservations").child(reservationId);
        reservationRef.child("status").setValue(status)
                .addOnSuccessListener(aVoid -> {
                    Log.d(TAG, "Updated status for " + reservationId + " to " + status);
                })
                .addOnFailureListener(e -> {
                    Log.e(TAG, "Failed to update status for " + reservationId + ": " + e.getMessage());
                    Toast.makeText(holder.itemView.getContext(),
                            "Failed to update status: " + e.getMessage(),
                            Toast.LENGTH_SHORT).show();
                });
    }

    @Override
    public int getItemCount() {
        Log.d(TAG, "Item count: " + reservationList.size());
        return reservationList.size();
    }

    static class ReservationViewHolder extends RecyclerView.ViewHolder {
        TextView tableNumberTextView, dateTextView, timeTextView, statusTextView;
        LinearLayout actionButtonsLayout;
        Button acceptButton, declineButton;

        public ReservationViewHolder(@NonNull View itemView) {
            super(itemView);
            tableNumberTextView = itemView.findViewById(R.id.tableNumberTextView);
            dateTextView = itemView.findViewById(R.id.dateTextView);
            timeTextView = itemView.findViewById(R.id.timeTextView);
            statusTextView = itemView.findViewById(R.id.statusTextView);
            actionButtonsLayout = itemView.findViewById(R.id.actionButtonsLayout);
            acceptButton = itemView.findViewById(R.id.acceptButton);
            declineButton = itemView.findViewById(R.id.declineButton);
        }
    }
}
