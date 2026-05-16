import java.awt.*;
import java.awt.event.ActionEvent;
import javax.swing.*;

public class CreateUserWindow extends JFrame {
    private BookingSystem bookingSystem;

    private JTextField usernameField, passwordField , emailField;
    private JComboBox<String> roleComboBox;

    public CreateUserWindow(BookingSystem bookingSystem) {
        this.bookingSystem = bookingSystem;
        setTitle("Create User");
        setSize(500, 600);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        initComponents();
        setVisible(true);
    }

    private void initComponents() {
        JPanel panel = new JPanel(new GridLayout(5, 1, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        usernameField = new JTextField();
        passwordField = new JTextField();
        emailField = new JTextField();
        roleComboBox = new JComboBox<>(new String[] {"customer", "agent", "admin"});

        panel.add(new JLabel("Username:"));
        panel.add(usernameField);
        panel.add(new JLabel("Email:"));
        panel.add(emailField);
        panel.add(new JLabel("Password:"));
        panel.add(passwordField);
        panel.add(new JLabel("Role:"));
        panel.add(roleComboBox);

        JButton createButton = new JButton("Create User");
        createButton.addActionListener(this::handleCreateUser);

        panel.add(createButton);

        add(panel);
    }

    private void handleCreateUser(ActionEvent e) {
        String username = usernameField.getText().trim();
        String password = passwordField.getText().trim();
        String email = emailField.getText().trim();
        String role = (String) roleComboBox.getSelectedItem();
        boolean success = bookingSystem.createUser(username, password,  username, email,  role);

        if (success) {
            JOptionPane.showMessageDialog(this, "User created successfully!");
            dispose();
        } else {
            JOptionPane.showMessageDialog(this, "Error creating user.");
        }
    }
}
