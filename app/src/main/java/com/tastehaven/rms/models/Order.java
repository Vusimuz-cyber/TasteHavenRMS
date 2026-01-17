package com.tastehaven.rms.models;

public class Order {
    private String id;
    private String tableNumber;
    private String waiterId;
    private long orderTime;
    private String status;
    private String notes;

    public Order() {}

    public Order(String tableNumber, String waiterId, long orderTime, String status, String notes) {
        this.tableNumber = tableNumber;
        this.waiterId = waiterId;
        this.orderTime = orderTime;
        this.status = status;
        this.notes = notes;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getTableNumber() { return tableNumber; }
    public void setTableNumber(String tableNumber) { this.tableNumber = tableNumber; }
    public String getWaiterId() { return waiterId; }
    public void setWaiterId(String waiterId) { this.waiterId = waiterId; }
    public long getOrderTime() { return orderTime; }
    public void setOrderTime(long orderTime) { this.orderTime = orderTime; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }
}
