package com.example.foodifyy;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Transaction {
    private String transactionId;

    private String userId;
    private String type;
    private int points;
    private String transactionKey; // New field for transactionKey
    private Date transactionDate; // New field for transactionDate



    public Transaction() {
        // Default constructor required for Firebase
    }

    public Transaction(String userId, String type, int points) {
        this.userId = userId;
        this.type = type;
        this.points = points;

    }

    public String getUserId() {
        return userId;
    }

    public String getType() {
        return type;
    }

    public int getPoints() {
        return points;
    }

    public String getTransactionKey() {
        return transactionKey;
    }

    public void setTransactionKey(String transactionKey) {
        this.transactionKey = transactionKey;
    }

    public Date getTransactionDate() {
        return transactionDate;
    }

    public void setTransactionDate(Date transactionDate) {
        this.transactionDate = transactionDate;
    }

    public String getFormattedTransactionDate() {
        if (transactionDate != null) {
            return SimpleDateFormat.getDateTimeInstance().format(transactionDate);
        } else {
            return "Date not available";
        }
    }
}

