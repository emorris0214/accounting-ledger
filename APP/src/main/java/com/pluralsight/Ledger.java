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
        for (Transactions t : deposits) {
            System.out.println(t);
        }
    }

    // === DISPLAY PAYMENTS ONLY ===
    // Filters and shows only negative-value transactions (payments)
    public void displayPayments() {
        List<Transactions> payments = new ArrayList<>();
        for (Transactions t : transactions) {
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

        for (Transactions t : transactions) {
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
}
