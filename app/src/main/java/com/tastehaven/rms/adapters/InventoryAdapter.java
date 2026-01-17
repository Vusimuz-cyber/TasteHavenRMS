package com.tastehaven.rms.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import com.tastehaven.rms.R;
import com.tastehaven.rms.models.InventoryItem;
import java.util.List;

public class InventoryAdapter extends RecyclerView.Adapter<InventoryAdapter.InventoryViewHolder> {
    private List<InventoryItem> inventoryItems;

    public InventoryAdapter(List<InventoryItem> inventoryItems) {
        this.inventoryItems = inventoryItems;
    }

    @Override
    public InventoryViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_inventory, parent, false);
        return new InventoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(InventoryViewHolder holder, int position) {
        InventoryItem item = inventoryItems.get(position);
        holder.tvItemName.setText(item.getItemName());
        holder.tvQuantity.setText("Stock: " + item.getQuantityInStock());
        holder.tvThreshold.setText("Threshold: " + item.getReorderThreshold());
        holder.tvStatus.setText(item.getQuantityInStock() <= item.getReorderThreshold() ? "Low Stock!" : "In Stock");
        holder.tvStatus.setTextColor(item.getQuantityInStock() <= item.getReorderThreshold() ?
                0xFFFF0000 : 0xFF008000);
    }

    @Override
    public int getItemCount() {
        return inventoryItems.size();
    }

    static class InventoryViewHolder extends RecyclerView.ViewHolder {
        TextView tvItemName, tvQuantity, tvThreshold, tvStatus;

        InventoryViewHolder(View itemView) {
            super(itemView);
            tvItemName = itemView.findViewById(R.id.tvItemName);
            tvQuantity = itemView.findViewById(R.id.tvQuantity);
            tvThreshold = itemView.findViewById(R.id.tvThreshold);
            tvStatus = itemView.findViewById(R.id.tvStatus);
        }
    }
}
