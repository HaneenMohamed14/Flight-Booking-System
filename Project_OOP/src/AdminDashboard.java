import java.awt.*;
import java.awt.event.ActionEvent;
import javax.swing.*;

public class AdminDashboard extends JFrame {
    private BookingSystem bookingSystem;

    public AdminDashboard(BookingSystem bookingSystem) {
        this.bookingSystem = bookingSystem;
        setTitle("Admin Dashboard");
        setSize(400, 300);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        initComponents();
        setVisible(true);
    }

    private void initComponents() {
        JPanel panel = new JPanel(new GridLayout(6, 1, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(30, 50, 30, 50));

        JButton createUserButton = new JButton("Create User");
        JButton viewAllBookingsButton = new JButton("View All Bookings");
        JButton addFlightButton = new JButton("Add Flight");
        JButton logoutButton = new JButton("Logout");

        createUserButton.addActionListener(this::handleCreateUser);
        viewAllBookingsButton.addActionListener(this::handleViewAllBookings);
        addFlightButton.addActionListener(this::handleAddFlight);
        logoutButton.addActionListener(this::handleLogout);

        panel.add(new JLabel("Welcome, " + bookingSystem.getCurrentUser().getName()));
        panel.add(createUserButton);
        panel.add(viewAllBookingsButton);
        panel.add(addFlightButton);
        panel.add(logoutButton);

        add(panel);
    }

    private void handleCreateUser(ActionEvent e) {
        new CreateUserWindow(bookingSystem);
    }

    private void handleViewAllBookings(ActionEvent e) {
        // عرض جميع الحجوزات
        new ViewAllBookingsWindow(bookingSystem);
    }

    private void handleAddFlight(ActionEvent e) {
        // إضافة رحلة جديدة
        new AddFlightWindow(bookingSystem);
    }

    private void handleLogout(ActionEvent e) {
        bookingSystem.logout();
        dispose();
        new LoginWindow(bookingSystem);
    }
}