package ExpanseTracker;
/*
 * Smart Expense Tracker - Professional Version with Toggle View
 * Clean, scalable, and interview-ready Swing + JDBC project
 * Developed by: Binit Kumar Gupta (Sample for portfolio)
 */

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ExpenseTrackerGUI extends JFrame {
    // Database configuration
    private static final String DB_URL = "jdbc:mysql://localhost:3306/expense_tracker";
    private static final String USER = "root";
    private static final String PASSWORD = "Binitsql72a$"; // Update as needed

    // UI Components
    private JSpinner dateSpinner;
    private JTextField amountField, noteField, userIdField;
    private JComboBox<String> categoryField;
    private JButton addButton, toggleButton, clearButton;
    private JTable table;
    private DefaultTableModel tableModel;
    private JScrollPane scrollPane;

    private boolean isTableVisible = false;

    public ExpenseTrackerGUI() {
        setTitle("💼 Smart Expense Tracker");
        setSize(950, 600);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

        initializeInputPanel();
        initializeTablePanel();
        initializeListeners();
    }

    private void initializeInputPanel() {
        JPanel inputPanel = new JPanel(new GridLayout(6, 2, 15, 15));
        inputPanel.setBorder(BorderFactory.createTitledBorder("➕ Add New Expense"));
        inputPanel.setBackground(new Color(240, 248, 255));

        inputPanel.add(new JLabel("🆔 User ID / Name:"));
        userIdField = new JTextField();
        inputPanel.add(userIdField);

        inputPanel.add(new JLabel("📅 Date:"));
        dateSpinner = new JSpinner(new SpinnerDateModel());
        dateSpinner.setEditor(new JSpinner.DateEditor(dateSpinner, "dd-MM-yyyy"));
        inputPanel.add(dateSpinner);

        inputPanel.add(new JLabel("📂 Category:"));
        categoryField = new JComboBox<>(new String[]{"Food", "Travel", "Shopping", "Utilities", "Health", "Education", "Entertainment", "Other"});
        inputPanel.add(categoryField);

        inputPanel.add(new JLabel("💰 Amount:"));
        amountField = new JTextField();
        inputPanel.add(amountField);

        inputPanel.add(new JLabel("📝 Note:"));
        noteField = new JTextField();
        inputPanel.add(noteField);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 5));
        addButton = new JButton("➕ Add Expense");
        toggleButton = new JButton("📊 Show Expenses");
        clearButton = new JButton("🧹 Clear All Expenses");
        buttonPanel.add(addButton);
        buttonPanel.add(toggleButton);
        buttonPanel.add(clearButton);

        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.add(inputPanel, BorderLayout.CENTER);
        topPanel.add(buttonPanel, BorderLayout.SOUTH);
        add(topPanel, BorderLayout.NORTH);
    }

    private void initializeTablePanel() {
        tableModel = new DefaultTableModel(new String[]{"ID", "User", "Date", "Category", "Amount", "Note"}, 0);
        table = new JTable(tableModel);
        table.setRowHeight(28);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 16));
        scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createTitledBorder("📋 Expense History"));
        scrollPane.setVisible(false);
        add(scrollPane, BorderLayout.CENTER);
    }

    private void initializeListeners() {
        addButton.addActionListener(e -> addExpense());
        toggleButton.addActionListener(e -> toggleExpenses());
        clearButton.addActionListener(e -> clearExpenses());

        KeyAdapter enterKeyListener = new KeyAdapter() {
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) addExpense();
            }
        };
        userIdField.addKeyListener(enterKeyListener);
        amountField.addKeyListener(enterKeyListener);
        noteField.addKeyListener(enterKeyListener);
    }

    private void addExpense() {
        String userId = userIdField.getText().trim();
        Date selectedDate = (Date) dateSpinner.getValue();
        String category = (String) categoryField.getSelectedItem();
        String amountStr = amountField.getText().trim();
        String note = noteField.getText().trim();

        if (userId.isEmpty() || amountStr.isEmpty()) {
            showMessage("❗ User ID and Amount are required.");
            return;
        }

        try {
            double amount = Double.parseDouble(amountStr);
            java.sql.Date sqlDate = new java.sql.Date(selectedDate.getTime());

            try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASSWORD)) {
                String sql = "INSERT INTO expenses (user_id, date, category, amount, note) VALUES (?, ?, ?, ?, ?)";
                PreparedStatement stmt = conn.prepareStatement(sql);
                stmt.setString(1, userId);
                stmt.setDate(2, sqlDate);
                stmt.setString(3, category);
                stmt.setDouble(4, amount);
                stmt.setString(5, note);
                stmt.executeUpdate();
                showMessage("✅ Expense added successfully!");
                clearInputFields();
                if (isTableVisible) loadExpenses();
            }
        } catch (NumberFormatException e) {
            showMessage("❗ Invalid amount format.");
        } catch (SQLException e) {
            showMessage("Database Error: " + e.getMessage());
        }
    }

    private void loadExpenses() {
        tableModel.setRowCount(0);
        try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASSWORD);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM expenses ORDER BY date DESC")) {

            while (rs.next()) {
                Object[] row = {
                        rs.getInt("id"),
                        rs.getString("user_id"),
                        new SimpleDateFormat("dd-MM-yyyy").format(rs.getDate("date")),
                        rs.getString("category"),
                        rs.getDouble("amount"),
                        rs.getString("note")
                };
                tableModel.addRow(row);
            }
        } catch (SQLException e) {
            showMessage("Database Error: " + e.getMessage());
        }
    }

    private void toggleExpenses() {
        if (!isTableVisible) {
            loadExpenses();
            scrollPane.setVisible(true);
            toggleButton.setText("📊 Hide Expenses");
        } else {
            scrollPane.setVisible(false);
            toggleButton.setText("📊 Show Expenses");
        }
        isTableVisible = !isTableVisible;
        revalidate();
        repaint();
    }

    private void clearExpenses() {
        int choice = JOptionPane.showConfirmDialog(this, "Are you sure you want to delete all expenses?", "Confirm", JOptionPane.YES_NO_OPTION);
        if (choice == JOptionPane.YES_OPTION) {
            try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASSWORD);
                 Statement stmt = conn.createStatement()) {

                stmt.executeUpdate("DELETE FROM expenses");
                stmt.executeUpdate("ALTER TABLE expenses AUTO_INCREMENT = 1");  // Reset ID

                tableModel.setRowCount(0);
                showMessage("🧹 All expenses cleared successfully.");
                scrollPane.setVisible(false);
                toggleButton.setText("📊 Show Expenses");
                isTableVisible = false;
            } catch (SQLException e) {
                showMessage("Database Error: " + e.getMessage());
            }
        }
    }


    private void clearInputFields() {
        userIdField.setText("");
        amountField.setText("");
        noteField.setText("");
        categoryField.setSelectedIndex(0);
    }

    private void showMessage(String message) {
        JOptionPane.showMessageDialog(this, message);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception ignored) {}
            new ExpenseTrackerGUI().setVisible(true);
        });
    }
}