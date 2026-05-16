import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.List;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class ViewAllBookingsWindow extends JFrame {
    private BookingSystem bookingSystem;

    public ViewAllBookingsWindow(BookingSystem bookingSystem) {
        this.bookingSystem = bookingSystem;
        setTitle("All Bookings");
        setSize(800, 400);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        initComponents();
        setVisible(true);
    }

    private void initComponents() {
        JPanel panel = new JPanel(new BorderLayout());
        JButton closeButton = new JButton("Close");
        closeButton.addActionListener(this::handleClose);

        JTable bookingsTable = new JTable();
        JScrollPane scrollPane = new JScrollPane(bookingsTable);
        panel.add(scrollPane, BorderLayout.CENTER);
        panel.add(closeButton, BorderLayout.SOUTH);

        List<Booking> bookings = bookingSystem.getAllBookings();
        String[] columnNames = {
            "Booking Ref", "Customer ID", "Flight No",
            "Seat Type", "Seat No", "Status", "Payment Status"
        };
        DefaultTableModel model = new DefaultTableModel(columnNames, 0);

        for (Booking booking : bookings) {
            Object[] row = {
                booking.getBookingReference(),
                booking.getCustomerId(),
                booking.getFlightNumber(),
                booking.getSeatClass(),
                booking.getSeatNumber(),
                booking.getStatus(),
                booking.getPaymentStatus()
            };
            model.addRow(row);
        }

        bookingsTable.setModel(model);
        add(panel);
    }

    private void handleClose(ActionEvent e) {
        dispose();
    }
}