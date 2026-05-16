import java.sql.*;

public class Payment {
    private int paymentId;
    private String bookingRefrence;
    private double amount;
    private String currency;
    private String method; // Credit Card, PayPal, Bank Transfer, Cash On Delivery
    private String status; // Pending, Completed, Failed
    private String transactionDate;

    // Constructor
    public Payment(int paymentId, String bookingRefrence, double amount, String currency, String method, String status, String transactionDate) {
        this.paymentId = paymentId;
        this.bookingRefrence = bookingRefrence;
        this.amount = amount;
        this.currency = currency;
        this.method = method;
        this.status = status;
        this.transactionDate = transactionDate;
    }

    // Getters and Setters
    public int getPaymentId() {
        return paymentId;
    }

    public void setPaymentId(int paymentId) {
        this.paymentId = paymentId;
    }

    public String getbookingRefrence() {
        return bookingRefrence;
    }

    public void setbookingRefrence(String bookingRefrence) {
        this.bookingRefrence = bookingRefrence;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getTransactionDate() {
        return transactionDate;
    }

    public void setTransactionDate(String transactionDate) {
        this.transactionDate = transactionDate;
    }

    // Method to process the payment (payment processing logic depends on method)
    public boolean processPayment(Connection conn) throws SQLException {
        // Example of handling different payment methods
        if (this.method.equalsIgnoreCase("Credit Card")) {
            // Logic for credit card payment processing (simulated here)
            System.out.println("Processing credit card payment...");
        } else if (this.method.equalsIgnoreCase("PayPal")) {
            // Logic for PayPal payment processing (simulated here)
            System.out.println("Processing PayPal payment...");
        } else if (this.method.equalsIgnoreCase("Bank Transfer")) {
            // Logic for bank transfer payment processing (simulated here)
            System.out.println("Processing bank transfer...");
        } else if (this.method.equalsIgnoreCase("Cash On Delivery")) {
            // Logic for cash on delivery payment (no immediate processing needed)
            System.out.println("Cash on delivery, no immediate processing.");
        } else {
            System.out.println("Unknown payment method.");
            return false;
        }

        // After payment processing, insert the payment into the database
        String sql = "INSERT INTO payments ( amount, currency, method, status, transaction_date) VALUES ( ?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, this.bookingRefrence);
            stmt.setDouble(2, this.amount);
            stmt.setString(3, this.currency);
            stmt.setString(4, this.method);
            stmt.setString(5, "Pending");  // Set status as 'Pending' initially
            stmt.setString(6, this.transactionDate);
            stmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Method to validate payment details (validates based on the method)
    public boolean validatePaymentDetails() {
        if (this.amount <= 0) {
            System.out.println("Invalid payment amount.");
            return false;
        }
        if (this.method.equalsIgnoreCase("Credit Card")) {
            // Validate credit card details (you can add more sophisticated validation here)
            if (this.currency == null || this.currency.isEmpty()) {
                System.out.println("Currency is required for credit card payments.");
                return false;
            }
            System.out.println("Validating credit card details...");
            return true;
        } else if (this.method.equalsIgnoreCase("PayPal")) {
            // Validate PayPal payment (you could check for PayPal-specific details)
            if (this.currency == null || this.currency.isEmpty()) {
                System.out.println("Currency is required for PayPal payments.");
                return false;
            }
            System.out.println("Validating PayPal details...");
            return true;
        } else if (this.method.equalsIgnoreCase("Bank Transfer")) {
            // Validate Bank Transfer details (could be extended further)
            if (this.currency == null || this.currency.isEmpty()) {
                System.out.println("Currency is required for bank transfer.");
                return false;
            }
            System.out.println("Validating bank transfer details...");
            return true;
        } else if (this.method.equalsIgnoreCase("Cash On Delivery")) {
            // No need for validation for cash on delivery
            System.out.println("Validating cash on delivery...");
            return true;
        } else {
            System.out.println("Unknown payment method.");
            return false;
        }
    }

    // Method to update payment status (e.g., 'Completed', 'Failed')
    public void updateStatus(Connection conn, String newStatus) throws SQLException {
        String sql = "UPDATE payments SET status = ? WHERE payment_id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, newStatus);
            stmt.setInt(2, this.paymentId);
            stmt.executeUpdate();
        }
    }

    // Method to get payment details from the database
    public static Payment getPaymentDetails(Connection conn, int paymentId) throws SQLException {
        String sql = "SELECT * FROM payments WHERE payment_id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, paymentId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                String bookingRefrence = rs.getString("bookingRefrence");
                double amount = rs.getDouble("amount");
                String currency = rs.getString("currency");
                String method = rs.getString("method");
                String status = rs.getString("status");
                String transactionDate = rs.getString("transaction_date");
                return new Payment(paymentId, bookingRefrence, amount, currency, method, status, transactionDate);
            } else {
                return null; // Payment not found
            }
        }
    }
}
