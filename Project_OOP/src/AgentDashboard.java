import java.awt.*;
import java.awt.event.ActionEvent;
import javax.swing.*;

public class AgentDashboard extends JFrame {
    private BookingSystem bookingSystem;

    public AgentDashboard(BookingSystem bookingSystem) {
        this.bookingSystem = bookingSystem;
        setTitle("Agent Dashboard");
        setSize(400, 300);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        initComponents();
        setVisible(true);
    }

    private void initComponents() {
        JPanel panel = new JPanel(new GridLayout(4, 1, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(30, 50, 30, 50));

        JButton createBookingBtn = new JButton("Create Booking for Customer");
        JButton modifyBookingBtn = new JButton("Modify Booking");
        JButton generateReportsBtn = new JButton("Generate Reports");
        JButton logoutBtn = new JButton("Logout");

        createBookingBtn.addActionListener(e -> {
            int customerId = Integer.parseInt(JOptionPane.showInputDialog(this, "Enter Customer ID"));
            new CreateBookingWindow(bookingSystem, customerId);  
        });
        modifyBookingBtn.addActionListener(e -> new ModifyBookingWindow(bookingSystem));
        generateReportsBtn.addActionListener(e -> new ReportWindow());
        logoutBtn.addActionListener(this::handleLogout);

        panel.add(createBookingBtn);
        panel.add(modifyBookingBtn);
        panel.add(generateReportsBtn);
        panel.add(logoutBtn);

        add(panel);
    }

    private void handleLogout(ActionEvent e) {
        bookingSystem.logout();
        dispose();
        new LoginWindow(bookingSystem);
    }
}