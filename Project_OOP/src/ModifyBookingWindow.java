import java.awt.*;
import java.awt.event.ActionEvent;
import javax.swing.*;

public class ModifyBookingWindow extends JFrame {
    private BookingSystem bookingSystem;

    private JTextField bookingRefField;
    private JComboBox<String> seatTypeComboBox;
    private JComboBox<String> statusComboBox;
    private JComboBox<String> paystatComboBox;

    public ModifyBookingWindow(BookingSystem bookingSystem) {
        this.bookingSystem = bookingSystem;
        setTitle("Modify Booking");
        setSize(400, 300);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        initComponents();
        setVisible(true);
    }

    private void initComponents() {
        JPanel panel = new JPanel(new GridLayout(5, 2, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        bookingRefField = new JTextField();
        seatTypeComboBox = new JComboBox<>(new String[] {"Economy", "Business", "First Class"});
        statusComboBox = new JComboBox<>(new String[] {"Reserved", "Confirmed", "Cancelled"});
        paystatComboBox = new JComboBox<>(new String[] {"Pending", "Paid", "Failed"});

        panel.add(new JLabel("Booking Reference:"));
        panel.add(bookingRefField);
        panel.add(new JLabel("Seat Type:"));
        panel.add(seatTypeComboBox);
        panel.add(new JLabel("Booking Status:"));
        panel.add(statusComboBox);
        panel.add(new JLabel("Payment Status:"));
        panel.add(paystatComboBox);

        JButton modifyButton = new JButton("Modify Booking");
        modifyButton.addActionListener(this::handleModify);

        panel.add(modifyButton);

        add(panel);
    }

    private void handleModify(ActionEvent e) {
        String bookingRef = bookingRefField.getText().trim();
        String seatType = (String) seatTypeComboBox.getSelectedItem();
        String status = (String) statusComboBox.getSelectedItem();
        String paystat = (String) statusComboBox.getSelectedItem();

        boolean success = bookingSystem.modifyBooking(bookingRef, seatType, status , paystat);

        if (success) {
            JOptionPane.showMessageDialog(this, "Booking modified successfully!");
            dispose(); // Close the window after modification
        } else {
            JOptionPane.showMessageDialog(this, "Error modifying booking.");
        }
    }
}