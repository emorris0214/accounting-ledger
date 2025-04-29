package com.pluralsight;

import java.util.Scanner;

public class APP {

    // Create a Ledger instance to manage all transaction-related operations
    private static Ledger ledger = new Ledger();

    // Scanner to read user input from the console
    private static Scanner keyboard = new Scanner(System.in);

    /**
     * Utility method to simulate clearing the screen.
     * This is done by printing 50 blank lines.
     * (Note: This doesn't actually clear the console buffer.)
     */
    private static void clearScreen() {
        for (int i = 0; i < 50; i++) {
            System.out.println();
        }
    }

    /**
     * Main entry point of the application.
     * Handles navigation between major parts of the app based on user input.
     */
    public static void main(String[] args) {
        boolean running = true;

        // Application loop that keeps running until user chooses to exit
        while (running) {
            showHomeScreen(); // Display home menu
            String choice = keyboard.nextLine().toUpperCase(); // Read and normalize input

            // Handle user input to navigate or take action
            switch (choice) {
                case "D":
                    addDeposit();
                    break;
                case "P":
                    makePayment();
                    break;
                case "L":
                    showLedgerMenu();
                    break;
                case "X":
                    running = false;
                    System.out.println("Goodbye!");
                    break;
                default:
                    System.out.println("Invalid option. Try again.");
            }
        } keyboard.close();
    }

    /**
     * Displays the main home screen menu options to the user.
     */
    private static void showHomeScreen() {
        System.out.println("Welcome to Mori's Ledgendary Ledger!");
        System.out.println("\n=== Home Screen ===");
        System.out.println("D) Add Deposit");
        System.out.println("P) Make Payment (Debit)");
        System.out.println("L) Ledger");
        System.out.println("X) Exit");
        System.out.print("Select an option: ");
    }

    /**
     * Displays the Ledger menu and handles user navigation.
     * Allows viewing different transaction types or reports.
     */
    private static void showLedgerMenu() {
        boolean inLedgerMenu = true;

        while (inLedgerMenu) {
            clearScreen(); // Clear the screen for better readability
            System.out.println("\n=== Ledger Menu ===");
            System.out.println("A) All Transactions");
            System.out.println("D) Deposits Only");
            System.out.println("P) Payments Only");
            System.out.println("R) Reports");
            System.out.println("H) Home");

            System.out.print("Select an option: ");
            String choice = keyboard.nextLine().toUpperCase();

            switch (choice) {
                case "A":
                    ledger.displayAllTransactions();
                    break;
                case "D":
                    ledger.displayDeposits();
                    break;
                case "P":
                    ledger.displayPayments();
                    break;
                case "R":
                    showReportsMenu();
                    break;
                case "H":
                    inLedgerMenu = false;
                    break;
                default:
                    System.out.println("Invalid option. Try again.");
            }
        }
    }

    /**
     * Displays the Reports menu and allows users to filter transactions by time or vendor.
     */
    private static void showReportsMenu() {
        boolean inReportsMenu = true;

        while (inReportsMenu) {
            clearScreen();
            System.out.println("\n=== Reports Menu ===");
            System.out.println("M) Month-to-Date");
            System.out.println("P) Previous Month");
            System.out.println("Y) Year-to-Date");
            System.out.println("V) Search by Vendor");
            System.out.println("B) Back to Ledger Menu");

            System.out.print("Select an option: ");
            String choice2 = keyboard.nextLine().toUpperCase();

            switch (choice2) {
                case "M":
                    ledger.displayMonthToDate();
                    break;
                case "P":
                    ledger.displayPreviousMonth();
                    break;
                case "Y":
                    ledger.displayYearToDate();
                    break;
                case "V":
                    searchByVendor();
                    break;
                case "B":
                    inReportsMenu = false;
                    break;
                default:
                    System.out.println("Invalid option. Try again.");
            }
        }
    }

    /**
     * Handles input and creation of a deposit transaction.
     * Prompts the user for description, vendor, and amount, then logs the transaction.
     */
    private static void addDeposit() {
        System.out.println("Enter description: ");
        String description = keyboard.nextLine();
        System.out.println("Enter vendor: ");
        String vendor = keyboard.nextLine();
        System.out.println("Enter amount: ");
        double amount = Double.parseDouble(keyboard.nextLine());

        // Capture current date and time
        String date = java.time.LocalDate.now().toString();
        String time = java.time.LocalTime.now().withNano(0).toString(); // time only, no nanoseconds

        // Create a new transaction object and add it to the ledger
        Transactions deposit = new Transactions(date, time, description, vendor, amount);
        ledger.addTransactions(deposit);

        System.out.println("Deposit added!");
    }

    /**
     * Handles input and creation of a payment (debit) transaction.
     * Ensures the amount is stored as a negative number.
     */
    private static void makePayment() {
        System.out.println("Enter description: ");
        String description = keyboard.nextLine();
        System.out.println("Enter vendor: ");
        String vendor = keyboard.nextLine();
        System.out.println("Enter amount: ");
        double amount = Double.parseDouble(keyboard.nextLine());

        // Ensure the amount is negative to represent a payment/debit
        amount = -Math.abs(amount);

        // Capture current date and time
        String date = java.time.LocalDate.now().toString();
        String time = java.time.LocalTime.now().withNano(0).toString();

        // Create a new transaction object and add it to the ledger
        Transactions payment = new Transactions(date, time, description, vendor, amount);
        ledger.addTransactions(payment);

        System.out.println("Payment successfully recorded!");
    }

    /**
     * Allows the user to search transactions by vendor name.
     * Case-insensitive search that displays any matching entries.
     */
    private static void searchByVendor() {
        System.out.println("Enter vendor name to search: ");
        String vendorName = keyboard.nextLine().toLowerCase();

        for (Transactions t : ledger.getTransactions()) {
            if (t.getVendor().toLowerCase().contains(vendorName)) {
                System.out.println(t);
            }
        }
    }
}
