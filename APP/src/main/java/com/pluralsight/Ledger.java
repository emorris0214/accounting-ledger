package com.pluralsight;

import java.io.*;
import java.util.*;
import java.util.regex.Pattern;

// Manages a collection of transactions (reading, writing, and displaying them)
public class Ledger {

    // Name of the CSV file where transactions are stored persistently
    private static final String FILE_NAME = "transactions.csv";

    // In-memory list that holds all transaction records loaded from or added to the file
    private List<Transactions> transactions = new ArrayList<>();

    // === CONSTRUCTOR ===
    // Instantiates a Ledger object and automatically loads existing transactions from file
    public Ledger() {
        loadTransactions();
    }

    // === LOAD TRANSACTIONS ===
    // Reads each line from the CSV file, parses it, and populates the `transactions` list
    private void loadTransactions() {
        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_NAME))) {
            String line;

            // Read each line of the file
            while ((line = reader.readLine()) != null) {
                // Split the line using '|' as delimiter, escaped via Pattern.quote
                String[] parts = line.split(Pattern.quote("|"));

                // Ensure the line has exactly 5 components (date, time, description, vendor, amount)
                if (parts.length == 5) {
                    // Clean the amount string to remove dollar sign or commas and convert to double
                    double amount = Double.parseDouble(parts[4].replace("$", "").replace(",", ""));

                    // Create a new Transactions object and add to the list
                    Transactions t = new Transactions(parts[0], parts[1], parts[2], parts[3], amount);
                    transactions.add(t); // <- You were missing this line originally
                }
            }
        } catch (IOException e) {
            // File not found or unable to read; this is fine on first run
            System.out.println("No existing transactions found. A new file will be created.");
        }
    }

    // === ADD TRANSACTION ===
    // Adds a transaction to memory and persists it to the file
    public void addTransactions(Transactions t) {
        transactions.add(t);       // Store in-memory
        saveTransactions(t);       // Append to file
    }

    // === SAVE TRANSACTION ===
    // Appends a single transaction to the end of the CSV file
    private void saveTransactions(Transactions t) {
        try {
            FileWriter fw = new FileWriter(FILE_NAME, true);      // 'true' = append mode
            BufferedWriter bw = new BufferedWriter(fw);

            bw.write(t.toString());   // Save formatted transaction line
            bw.newLine();             // Add newline after the record
            bw.flush();               // Forces any buffered output to the file system
            bw.close();               // Close writer
        } catch (IOException e) {
            // Print error if writing to file fails
            e.printStackTrace();
        }
    }

    // === DISPLAY ALL TRANSACTIONS ===
    // Prints all transactions to the console, newest first
    public void displayAllTransactions() {
        List<Transactions> sorted = new ArrayList<>(transactions);
        Collections.reverse(sorted); // Show most recent first
        for (Transactions t : sorted) {
            System.out.println(t);
        }
    }

    // === GETTER METHOD ===
    // Returns the full list of transactions for external access
    public List<Transactions> getTransactions() {
        return transactions;
    }

    // === DISPLAY DEPOSITS ONLY ===
    // Filters and shows only positive-value transactions (deposits)
    public void displayDeposits() {
        List<Transactions> deposits = new ArrayList<>();
        for (Transactions t : transactions) {
            if (t.getAmount() > 0) {
                deposits.add(t);
            }
        }
        Collections.reverse(deposits); // Newest first
        for (Transactions t : deposits) { // For-each loop, iterates through deposits arraylist, t being the transaction it's on
            System.out.println(t);
        }
    }

    // === DISPLAY PAYMENTS ONLY ===
    // Filters and shows only negative-value transactions (payments)
    public void displayPayments() {
        List<Transactions> payments = new ArrayList<>();
        for (Transactions t : transactions) { // For-each loop, iterates through payments arraylist, t being the transaction it's on
            if (t.getAmount() < 0) {
                payments.add(t);
            }
        }
        Collections.reverse(payments); // Newest first
        for (Transactions t : payments) {
            System.out.println(t);
        }
    }

    // === DISPLAY CURRENT MONTH TRANSACTIONS ===
    public void displayMonthToDate() {
        String currentMonth = java.time.LocalDate.now().getMonth().toString(); // e.g. "APRIL"
        int currentYear = java.time.LocalDate.now().getYear();

        for (Transactions t : transactions) { // For-each loop, iterates through transactions t (current transaction) arraylist, and gets the date
            java.time.LocalDate date = java.time.LocalDate.parse(t.getDate());
            if (date.getMonth().toString().equals(currentMonth) && date.getYear() == currentYear) {
                System.out.println(t);
            }
        }
    }

    // === DISPLAY PREVIOUS MONTH TRANSACTIONS ===
    public void displayPreviousMonth() {
        java.time.LocalDate now = java.time.LocalDate.now();
        java.time.LocalDate previousMonthDate = now.minusMonths(1);
        int previousMonth = previousMonthDate.getMonthValue();
        int yearOfPreviousMonth = previousMonthDate.getYear();

        for (Transactions t : transactions) {
            java.time.LocalDate date = java.time.LocalDate.parse(t.getDate());
            if (date.getMonthValue() == previousMonth && date.getYear() == yearOfPreviousMonth) {
                System.out.println(t);
            }
        }
    }

    // === DISPLAY CURRENT YEAR TRANSACTIONS ===
    public void displayYearToDate() {
        int currentYear = java.time.LocalDate.now().getYear();

        for (Transactions t : transactions) {
            java.time.LocalDate date = java.time.LocalDate.parse(t.getDate());
            if (date.getYear() == currentYear) {
                System.out.println(t);
            }
        }
    }

    // === ASK USER FOR SPECIFICS TO MAKE A CUSTOM SEARCH ===
    public void customSearch(String startDate, String endDate, String description, String vendor, Double amount){
        // Loop through each transaction and evaluate if it meets the criteria
        for (Transactions t : transactions) {
            boolean matches = true; // Marked (upcoming) false if any filter doesn't match

            /* DATE FILTERS
            === Filter by Start Date (inclusively) === */
            if (!startDate.isEmpty()) {
                java.time.LocalDate start = java.time.LocalDate.parse(startDate);
                java.time.LocalDate entryDate = java.time.LocalDate.parse(t.getDate());
                if (entryDate.isBefore(start)) {
                    matches = false; // Transaction is too old
                }
            }
            // --- Filter by End Date (inclusive) ---
            if (!endDate.isEmpty()) {
                java.time.LocalDate end = java.time.LocalDate.parse(endDate);
                java.time.LocalDate entryDate = java.time.LocalDate.parse(t.getDate());
                if (entryDate.isAfter(end)) {
                    matches = false; // Transaction is too recent
                }
            }

            // --- Filter by Description (case-insensitive contains) ---
            if (!description.isEmpty() && !t.getDescription().toLowerCase().contains(description)) {
                matches = false; // Description doesn’t match
            }

            // --- Filter by Vendor (case-insensitive contains) ---
            if (!vendor.isEmpty() && !t.getVendor().toLowerCase().contains(vendor)) {
                matches = false; // Vendor doesn’t match
            }

            // --- Filter by Amount (exact match) ---
            if (amount != null && t.getAmount() != amount) {
                matches = false; // Amount doesn’t match exactly
            }

            // --- If all checks passed, print the transaction ---
            if (matches) {
                System.out.println(t);
            }
        }
    }
}
