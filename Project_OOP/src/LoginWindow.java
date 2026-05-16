import java.awt.*;
import java.awt.event.ActionEvent;
import javax.swing.*;

public class LoginWindow extends JFrame {
    private BookingSystem bookingSystem;

    private JTextField usernameField, passwordField;

    public LoginWindow(BookingSystem bookingSystem) {
        this.bookingSystem = bookingSystem;
        setTitle("Login");
        setSize(400, 300);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        initComponents();
        setVisible(true);
    }

    private void initComponents() {
        JPanel panel = new JPanel(new GridLayout(4, 2, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(30, 50, 30, 50));

        usernameField = new JTextField();
        passwordField = new JPasswordField();

        panel.add(new JLabel("Username:"));
        panel.add(usernameField);
        panel.add(new JLabel("Password:"));
        panel.add(passwordField);

        JButton loginButton = new JButton("Login");
        loginButton.addActionListener(this::handleLogin);

        panel.add(loginButton);

        add(panel);
    }

    private void handleLogin(ActionEvent e) {
        String username = usernameField.getText().trim();
        String password = passwordField.getText().trim();

        System.out.println("Attempting to log in with Username: " + username); // رسالة تتبع

        User user = bookingSystem.login(username, password);

        if (user != null) {
            System.out.println("Login successful for: " + user.getName()); // رسالة تتبع
            if (user.getRole().equals("admin")) {
                new AdminDashboard(bookingSystem);
            } else if (user.getRole().equals("agent")) {
                new AgentDashboard(bookingSystem);
            } else {
                // Customer Dashboard
                new CustomerDashboard(bookingSystem);
            }
            dispose();
        } else {
            System.out.println("Invalid login attempt"); // رسالة تتبع
            JOptionPane.showMessageDialog(this, "Invalid username or password");
        }
    }
}