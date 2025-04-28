package com.pluralsight;
import java.io.*;
import java.util.*;
import java.util.regex.Pattern;

// Manages a collection of transactions (reading, writing, displaying)
public class Ledger {
    private static final String FILE_NAME = "transactions.csv"; // CSV file we will write to
    private List<Transactions> transactions = new ArrayList<>(); // List of transactions in memory
    
    // Constructor that loads existing transactions from file
    public Ledger() {
        loadTransactions();
    }

    // Load transactions from the CSV file into the list
    private void loadTransactions() {
        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_NAME))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(Pattern.quote("|"));
                if (parts.length == 5) {
                    Transactions t = new Transactions(parts[0], parts[1], parts[2], parts[3], Double.parseDouble(parts[4]));
                    transactions.add(t);
                }
            }
        } catch (IOException e) {
            System.out.println("No existing transactions found. A new file will be created.");
        }
    }

    // Add a new transaction to the list and save it to the file
    public void addTransactions(Transactions t) {
        transactions.add(t);
        saveTransactions(t);
    }

    // Save a single transaction to the CSV file (append)
    private void saveTransactions(Transactions t) {
        try {
            FileWriter fw = new FileWriter(FILE_NAME, true);
            BufferedWriter bw = new BufferedWriter(fw);

            bw.write(t.toString());
            bw.newLine();
            bw.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void displayAllTransactions() {
        List<Transactions> sorted = new ArrayList<>(transactions);
        Collections.reverse(sorted); // Reverse's to show newest first
        for (Transactions t : sorted) {
            System.out.println(t);
        }
    }

    public List<Transactions> getTransactions() {
        return transactions;
    }

    public void displayDeposits() {
        List<Transactions> deposits = new ArrayList<>();
        for (Transactions t : transactions) {
            if (t.getAmount() > 0) {
                deposits.add(t);
            }
        }
        Collections.reverse(deposits);
        for (Transactions t : deposits) {
            System.out.println(t);
        }
    }

    public void displayPayments() {
        List<Transactions> payments = new ArrayList<>();
        for (Transactions t : transactions) {
            if (t.getAmount() < 0) {
                payments.add(t);
            }
        }
        Collections.reverse(payments);
        for (Transactions t : payments) {
            System.out.println(t);
        }
    }

    // Display transactions for the current month
    public void displayMonthToDate() {
        String currentMonth = java.time.LocalDate.now().getMonth().toString();
        int currentYear = java.time.LocalDate.now().getYear();

        for (Transactions t : transactions) {
            java.time.LocalDate date = java.time.LocalDate.parse(t.getDate());
            if (date.getMonth().toString().equals(currentMonth) && date.getYear() == currentYear) {
                System.out.println(t);
            }
        }
    }

    // Display transactions for the previous month
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

    // Display transactions for the current year
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
