# accounting-ledger
Accounting Ledger Capstone (Start: 4/28/25) (Finish: 4/29/25)

1.  Introduction

Hello everyone. I’m going to walk you through the process of building a basic ledger application in Java. 

This project is split into three main Java classes: Transactions, Ledger, and APP. Each class plays a unique role in handling user input, storing transaction data, and managing the interface.

2. Project Overview

The goal of this application is to allow users to record financial transactions, whether deposits or payments, and later view reports such as deposits-only, payments-only, monthly reports, and more. The data is persistently stored in a CSV file named transactions.csv using standard Java I/O.


3. Class 1 - Transactions.java

![Screenshot 2025-04-30 112522](https://github.com/user-attachments/assets/8c4205b3-4da3-4dae-9568-82927d1d7262)


We begin with the Transactions class. This class models the data for each transaction.

date and time: store when the transaction occurred.

description and vendor: provide context for the transaction.

amount: stores the transaction value, where positive means deposit and negative means payment.

![Screenshot 2025-04-30 112656](https://github.com/user-attachments/assets/17f03b6f-0451-4ee0-b8e6-9f98097b42ac)


We include a constructor to initialize all fields, getter methods for data access, and an overridden toString() method that formats the transaction as a string with (|) delimiters for easy saving and reading from a file.


4.  Class 2 - Ledger.java

Next, we have the Ledger class, which manages the list of all Transactions.

![Screenshot 2025-04-30 113038](https://github.com/user-attachments/assets/3c716ba2-0c4d-4738-8f05-20b9c4edd410)


The constructor calls loadTransactions() to read saved data from transactions.csv.

addTransactions(): appends a new transaction both to memory and to the CSV file.

saveTransactions(): uses a BufferedWriter to append formatted strings from the Transactions class to the file.

We also have several display methods like displayAllTransactions(), displayDeposits(), and displayPayments(), which help filter and present data in a user-friendly way.


5. Ledger.java - Reports and Date Filtering

For more advanced functionality, we added reporting features:

displayMonthToDate() shows all transactions from the current month.

displayPreviousMonth() focuses on the last month.

displayYearToDate() filters transactions for the current calendar year.

These methods parse the date string into LocalDate and compare them using Java’s built-in date/time API.


6. Class 3 - APP.java
Finally, the APP class serves as our main program entry point and user interface logic.

It uses a Scanner for user input.

It shows a home menu with options for adding deposits, making payments, viewing the ledger, or exiting.

Submenus include the ledger view and the reports menu.

The user is guided through a series of prompts, where they enter transaction details like description, vendor, and amount. Based on the input, the app formats the data and stores it via the Ledger class.

7. Data Persistence and File Handling

An important feature is that all user input is persistently stored. When the app closes and restarts, it reads from transactions.csv. We improved the code to ensure all file readers and writers are closed properly using try-with-resources, which also prevents memory leaks.


8. Lessons Learned
   
Through this project, we practiced:

  1. Object-oriented design by separating responsibilities across classes.
  2. Java I/O for persistent data storage.
  3. (My new favorite built-in method for building apps with multiple screens: clearScreen(); )
![Screenshot 2025-04-30 113604](https://github.com/user-attachments/assets/a5268f03-a901-42e8-9d74-6feb860d79c4)

  4. Basic data filtering using collections and Java's date API.
  5. Creating a text-based menu-driven application with nested logic.
