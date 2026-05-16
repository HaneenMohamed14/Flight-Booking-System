import java.awt.*;
import java.awt.event.ActionEvent;
import javax.swing.*;

public class CustomerDashboard extends JFrame {
    private BookingSystem bookingSystem;

    public CustomerDashboard(BookingSystem bookingSystem) {
        this.bookingSystem = bookingSystem;
        setTitle("Customer Dashboard");
        setSize(400, 300);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        initComponents();
        setVisible(true);
    }

    private void initComponents() {
        JPanel panel = new JPanel(new GridLayout(5, 1, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(30, 50, 30, 50));

        JButton searchButton = new JButton("Search Flights");
        JButton bookButton = new JButton("Create Booking");
        JButton viewBookingsButton = new JButton("View My Bookings");
        JButton logoutButton = new JButton("Logout");

        searchButton.addActionListener(this::handleSearch);
        bookButton.addActionListener(this::handleBooking);
        viewBookingsButton.addActionListener(this::handleViewBookings);
        logoutButton.addActionListener(this::handleLogout);

        panel.add(new JLabel("Welcome, " + bookingSystem.getCurrentUser().getName()));
        panel.add(searchButton);
        panel.add(bookButton);
        panel.add(viewBookingsButton);
        panel.add(logoutButton);

        add(panel);
    }

    private void handleSearch(ActionEvent e) {
        new SearchFlightsWindow(bookingSystem);
    }

    private void handleBooking(ActionEvent e) {
        // الحصول على العميل الحالي
        User user = bookingSystem.getCurrentUser();
        
        // التأكد من أن المستخدم هو عميل
        if (user instanceof Customer) {
            int customerId = ((Customer) user).getCustomerId();
            new CreateBookingWindow(bookingSystem, customerId);  // نمرر customerId الخاص بالعميل
        } else {
            JOptionPane.showMessageDialog(this, "Invalid user type.");
        }
    }

    private void handleViewBookings(ActionEvent e) {
        User user = bookingSystem.getCurrentUser();
        if (user instanceof Customer) {
            int customerId = ((Customer) user).getCustomerId();
            new ViewBookingsWindow(bookingSystem, customerId);
        } else {
            JOptionPane.showMessageDialog(this, "Invalid user type.");
        }
    }

    private void handleLogout(ActionEvent e) {
        bookingSystem.logout();
        dispose();
        new LoginWindow(bookingSystem);
    }
}