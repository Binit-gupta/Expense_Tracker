# 💼 Smart Expense Tracker (Java + MySQL + Swing)

A **professional desktop-based Expense Tracker** application developed using Java (Swing), MySQL, and JDBC.  
It helps users record, view, and manage their daily expenses in an easy and efficient way.

📌 Features

- 🎯 Add expenses with:
  - User ID / Name
  - Date selection using calendar
  - Category dropdown
  - Amount input
  - Optional note
- 📊 Show/Hide expense history using toggle button
- 🔄 Reset database with ID reset (Auto Increment starts from 1)
- ⌨️ Add expense directly by pressing Enter key
- 🖥️ Clean & modern GUI using Swing components
- 💾 Data persistence using MySQL database

📂 Tech Stack

- **Language:** Java (JDK 8+)
- **GUI:** Java Swing
- **Database:** MySQL
- **Database Connectivity:** JDBC
- **IDE Used:** IntelliJ / Eclipse / VS Code (any Java supported IDE)

⚙️ Database Setup

1️⃣ Open MySQL and run the following queries:

```sql
CREATE DATABASE IF NOT EXISTS expense_tracker;
USE expense_tracker;

CREATE TABLE IF NOT EXISTS expenses (
    id INT AUTO_INCREMENT PRIMARY KEY,
    user_id VARCHAR(100) NOT NULL,
    date DATE NOT NULL,
    category VARCHAR(50) NOT NULL,
    amount DECIMAL(10, 2) NOT NULL,
    note VARCHAR(255),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
![Screenshot 2025-06-11 132327](https://github.com/user-attachments/assets/6a04b4ad-eafd-4b09-b4c7-67d6ccd9cc90)

