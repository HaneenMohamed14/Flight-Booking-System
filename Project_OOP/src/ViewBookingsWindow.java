import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.List;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class ViewBookingsWindow extends JFrame {
    private BookingSystem bookingSystem;
    private int customerId;

    public ViewBookingsWindow(BookingSystem bookingSystem, int customerId) {
        this.bookingSystem = bookingSystem;
        this.customerId = customerId;
        setTitle("View My Bookings");
        setSize(600, 400);
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

        // جلب حجوزات العميل من BookingSystem
        List<Booking> bookings = bookingSystem.getCustomerBookings(customerId);
        String[] columnNames = {"Booking Ref", "Flight No", "Seat Type", "Status", "Seat No", "Payment Status"};
        DefaultTableModel model = new DefaultTableModel(columnNames, 0);

        for (Booking booking : bookings) {
            Object[] row = {
                booking.getBookingReference(),
                booking.getFlightNumber(),
                booking.getSeatClass(),
                booking.getStatus(),
                booking.getSeatNumber(),
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