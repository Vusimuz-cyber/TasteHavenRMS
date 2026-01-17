package com.tastehaven.rms.models;

public class InventoryItem {
    private String itemName;
    private int quantityInStock;
    private int reorderThreshold;
    private long lastUpdated;

    public InventoryItem() {}

    public InventoryItem(String itemName, int quantityInStock, int reorderThreshold, long lastUpdated) {
        this.itemName = itemName;
        this.quantityInStock = quantityInStock;
        this.reorderThreshold = reorderThreshold;
        this.lastUpdated = lastUpdated;
    }

    public String getItemName() { return itemName; }
    public void setItemName(String itemName) { this.itemName = itemName; }
    public int getQuantityInStock() { return quantityInStock; }
    public void setQuantityInStock(int quantityInStock) { this.quantityInStock = quantityInStock; }
    public int getReorderThreshold() { return reorderThreshold; }
    public void setReorderThreshold(int reorderThreshold) { this.reorderThreshold = reorderThreshold; }
    public long getLastUpdated() { return lastUpdated; }
    public void setLastUpdated(long lastUpdated) { this.lastUpdated = lastUpdated; }
}
