package com.pluralsight;

import java.util.Scanner;

public class APP {
    private static Ledger ledger = new Ledger(); // Ledger object to manage transactions
    private static Scanner keyboard = new Scanner(System.in); // Scanner for user-input

    // Clear the console by printing blank lines
    private static void clearScreen() {
        for (int i = 0; i < 50; i++) {
            System.out.println();
        }
    }

    public static void main(String[] args) {
        boolean running = true;

        while (running) {
            showHomeScreen();
            String choice = keyboard.nextLine().toUpperCase();

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
        }
    }

    private static void showHomeScreen() {
        System.out.println("\n=== Home Screen ===");
        System.out.println("D) Add Deposit");
        System.out.println("P) Make Payment (Debit)");
        System.out.println("L) Ledger");
        System.out.println("X) Exit");
        System.out.print("Select an option: ");
    }

    private static void showLedgerMenu() {
        boolean inLedgerMenu = true;

        while (inLedgerMenu) {
            clearScreen();
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

    private static void addDeposit() {
        System.out.println("Enter description: ");
        String description = keyboard.nextLine();
        System.out.println("Enter vendor: ");
        String vendor = keyboard.nextLine();
        System.out.println("Enter amount: ");
        double amount = Double.parseDouble(keyboard.nextLine());

        // Get current date and time
        String date = java.time.LocalDate.now().toString();
        String time = java.time.LocalTime.now().withNano(0).toString(); // Correct way to get time

        Transactions deposit = new Transactions(date, time, description, vendor, amount);
        ledger.addTransactions(deposit);

        System.out.println("Deposit added!");
    }

    private static void makePayment() {
        System.out.println("Enter description: ");
        String description = keyboard.nextLine();
        System.out.println("Enter vendor: ");
        String vendor = keyboard.nextLine();
        System.out.println("Enter amount: ");
        double amount = Double.parseDouble(keyboard.nextLine());

        amount = -Math.abs(amount); // Ensure it's a negative amount for payment

        String date = java.time.LocalDate.now().toString();
        String time = java.time.LocalTime.now().withNano(0).toString();

        Transactions payment = new Transactions(date, time, description, vendor, amount);
        ledger.addTransactions(payment);

        System.out.println("Payment successfully recorded!");
    }

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
