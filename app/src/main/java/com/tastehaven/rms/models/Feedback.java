package com.tastehaven.rms.models;

public class Feedback {
    private String userId;
    private float rating;
    private String comment;
    private long timestamp;

    public Feedback() {}

    public Feedback(String userId, float rating, String comment, long timestamp) {
        this.userId = userId;
        this.rating = rating;
        this.comment = comment;
        this.timestamp = timestamp;
    }

    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }
    public float getRating() { return rating; }
    public void setRating(float rating) { this.rating = rating; }
    public String getComment() { return comment; }
    public void setComment(String comment) { this.comment = comment; }
    public long getTimestamp() { return timestamp; }
    public void setTimestamp(long timestamp) { this.timestamp = timestamp; }
}
