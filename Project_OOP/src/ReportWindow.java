import java.awt.*;
import java.sql.*;
import javax.swing.*;

public class ReportWindow extends JFrame {
    public ReportWindow() {
        setTitle("Booking Report");
        setSize(700, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        JTextArea textArea = new JTextArea();
        textArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(textArea);
        add(scrollPane, BorderLayout.CENTER);

        try {
            Connection connection = DatabaseManager.getConnection(); // استخدم الاتصال من DatabaseManager
            String query = "SELECT booking_reference, customer_id, flight_number, seat_class, status, payment_status FROM bookings";
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery(query);

            StringBuilder sb = new StringBuilder();
            sb.append("Booking Ref | Customer ID | Flight No | Seat Class | Status | Payment\n");
            sb.append("-----------------------------------------------------------------------\n");

            while (rs.next()) {
                sb.append(rs.getString("booking_reference")).append(" | ")
                  .append(rs.getInt("customer_id")).append(" | ")
                  .append(rs.getString("flight_number")).append(" | ")
                  .append(rs.getString("seat_class")).append(" | ")
                  .append(rs.getString("status")).append(" | ")
                  .append(rs.getString("payment_status")).append("\n");
            }

            textArea.setText(sb.toString());

        } catch (SQLException e) {
            textArea.setText("Error loading report: " + e.getMessage());
        }

        setVisible(true);
    }
}