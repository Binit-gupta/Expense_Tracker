-- Create Database (if not exists)
CREATE DATABASE IF NOT EXISTS expense_tracker;

-- Use the Database
USE expense_tracker;

-- Create Table
CREATE TABLE IF NOT EXISTS expenses (
    id INT AUTO_INCREMENT PRIMARY KEY,      -- Auto Increment ID
    user_id VARCHAR(100) NOT NULL,          -- User Name / Unique ID
    date DATE NOT NULL,                     -- Expense Date
    category VARCHAR(50) NOT NULL,          -- Expense Category
    amount DECIMAL(10, 2) NOT NULL,         -- Amount spent
    note VARCHAR(255),                      -- Optional Note
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP -- Auto Timestamp
);