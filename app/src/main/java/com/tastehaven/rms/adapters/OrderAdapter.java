package com.tastehaven.rms.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import com.tastehaven.rms.R;
import com.tastehaven.rms.models.Order;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.OrderViewHolder> {
    private List<Order> orders;
    private OnStatusUpdateListener listener;

    public interface OnStatusUpdateListener {
        void onStatusUpdate(Order order);
    }

    public OrderAdapter(List<Order> orders, OnStatusUpdateListener listener) {
        this.orders = orders;
        this.listener = listener;
    }

    @Override
    public OrderViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_order, parent, false);
        return new OrderViewHolder(view);
    }

    @Override
    public void onBindViewHolder(OrderViewHolder holder, int position) {
        Order order = orders.get(position);
        holder.tvTableNumber.setText("Table: " + order.getTableNumber());
        holder.tvStatus.setText("Status: " + order.getStatus());
        holder.tvNotes.setText("Notes: " + (order.getNotes() != null ? order.getNotes() : "None"));
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.US);
        holder.tvTime.setText("Time: " + sdf.format(order.getOrderTime()));
        holder.btnUpdateStatus.setVisibility(order.getStatus().equals("served") ? View.GONE : View.VISIBLE);
        holder.btnUpdateStatus.setText(order.getStatus().equals("received") ? "Start Preparing" : "Mark Ready");
        holder.btnUpdateStatus.setOnClickListener(v -> listener.onStatusUpdate(order));
    }

    @Override
    public int getItemCount() {
        return orders.size();
    }

    static class OrderViewHolder extends RecyclerView.ViewHolder {
        TextView tvTableNumber, tvStatus, tvNotes, tvTime;
        Button btnUpdateStatus;

        OrderViewHolder(View itemView) {
            super(itemView);
            tvTableNumber = itemView.findViewById(R.id.tvTableNumber);
            tvStatus = itemView.findViewById(R.id.tvStatus);
            tvNotes = itemView.findViewById(R.id.tvNotes);
            tvTime = itemView.findViewById(R.id.tvTime);
            btnUpdateStatus = itemView.findViewById(R.id.btnUpdateStatus);
        }
    }
}