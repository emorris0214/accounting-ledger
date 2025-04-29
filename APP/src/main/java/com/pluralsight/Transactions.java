package com.pluralsight;

// Represents a single transaction (deposit or payment)
public class Transactions {

    // === INSTANCE VARIABLES ===
    private String date;         // The date the transaction occurred (format: YYYY-MM-DD)
    private String time;         // The time the transaction occurred (format: HH:MM:SS)
    private String description;  // A short description of the transaction (e.g., "Grocery shopping")
    private String vendor;       // The name of the vendor or recipient (e.g., "Walmart")
    private double amount;       // The amount of the transaction (positive for deposits, negative for payments)

    // === CONSTRUCTOR ===
    // Initializes a transaction object with the provided values
    public Transactions(String date, String time, String description, String vendor, double amount) {
        this.date = date;               // Assigns the date to the instance variable
        this.time = time;               // Assigns the time to the instance variable
        this.description = description; // Assigns the description
        this.vendor = vendor;           // Assigns the vendor name
        this.amount = amount;           // Assigns the transaction amount
    }

    // === GETTERS ===
    // These methods provide read-only access to the object's data

    public String getDate() {
        return date; // Returns the transaction date
    }

    public String getTime() {
        return time; // Returns the transaction time
    }

    public String getDescription() {
        return description; // Returns the description of the transaction
    }

    public String getVendor() {
        return vendor; // Returns the vendor name
    }

    public double getAmount() {
        return amount; // Returns the transaction amount (positive or negative)
    }

    // === OVERRIDE & TO STRING METHOD ===
    // The @Override annotation tells the compiler that this method overrides the default toString()
    // Converts the transaction into a pipe-delimited string formatted for storage in a CSV file.
    // Includes formatting to show amount with 2 decimal places and a dollar sign.
    @Override
    public String toString() {
        return date + "|" + time + "|" + description + "|" + vendor + "| $" + String.format("%.2f", amount);
    }
}
