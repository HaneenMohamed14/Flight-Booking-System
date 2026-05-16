import java.awt.*;
import java.awt.event.ActionEvent;
import javax.swing.*;

public class AddFlightWindow extends JFrame {
    private BookingSystem bookingSystem;

    private JTextField flightNoField, airlineField, originField, destinationField;
    private JTextField departureField, arrivalField;
    private JTextField economyPriceField, businessPriceField, firstClassPriceField;
    private JTextField economySeatsField, businessSeatsField, firstClassSeatsField;

    public AddFlightWindow(BookingSystem bookingSystem) {
        this.bookingSystem = bookingSystem;
        setTitle("Add Flight");
        setSize(500, 500);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        initComponents();
        setVisible(true);
    }

    private void initComponents() {
        JPanel panel = new JPanel(new GridLayout(13, 2, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        flightNoField = new JTextField();
        airlineField = new JTextField();
        originField = new JTextField();
        destinationField = new JTextField();
        departureField = new JTextField(); // Format: YYYY-MM-DD HH:MM
        arrivalField = new JTextField();
        economyPriceField = new JTextField();
        businessPriceField = new JTextField();
        firstClassPriceField = new JTextField();
        economySeatsField = new JTextField();
        businessSeatsField = new JTextField();
        firstClassSeatsField = new JTextField();

        panel.add(new JLabel("Flight Number:")); panel.add(flightNoField);
        panel.add(new JLabel("Airline:")); panel.add(airlineField);
        panel.add(new JLabel("Origin:")); panel.add(originField);
        panel.add(new JLabel("Destination:")); panel.add(destinationField);
        panel.add(new JLabel("Departure Time:")); panel.add(departureField);
        panel.add(new JLabel("Arrival Time:")); panel.add(arrivalField);
        panel.add(new JLabel("Economy Price:")); panel.add(economyPriceField);
        panel.add(new JLabel("Business Price:")); panel.add(businessPriceField);
        panel.add(new JLabel("First Class Price:")); panel.add(firstClassPriceField);
        panel.add(new JLabel("Total Economy Seats:")); panel.add(economySeatsField);
        panel.add(new JLabel("Total Business Seats:")); panel.add(businessSeatsField);
        panel.add(new JLabel("Total First Class Seats:")); panel.add(firstClassSeatsField);

        JButton addFlightButton = new JButton("Add Flight");
        addFlightButton.addActionListener(this::handleAddFlight);
        panel.add(addFlightButton);

        add(panel);
    }

    private void handleAddFlight(ActionEvent e) {
        try {
            String flightNo = flightNoField.getText().trim();
            String airline = airlineField.getText().trim();
            String origin = originField.getText().trim();
            String destination = destinationField.getText().trim();
            String departure = departureField.getText().trim();
            String arrival = arrivalField.getText().trim();
            double economyPrice = Double.parseDouble(economyPriceField.getText().trim());
            double businessPrice = Double.parseDouble(businessPriceField.getText().trim());
            double firstPrice = Double.parseDouble(firstClassPriceField.getText().trim());
            int economySeats = Integer.parseInt(economySeatsField.getText().trim());
            int businessSeats = Integer.parseInt(businessSeatsField.getText().trim());
            int firstSeats = Integer.parseInt(firstClassSeatsField.getText().trim());

            boolean success = bookingSystem.addFlight(
                    flightNo, airline, origin, destination,
                    departure, arrival,
                    economyPrice, businessPrice, firstPrice,
                    economySeats, businessSeats, firstSeats
            );
            if (success) {
                JOptionPane.showMessageDialog(this, "Flight added successfully!");
                dispose();
            } else {
                JOptionPane.showMessageDialog(this, "Error adding flight.");
            }

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Please enter valid numbers for prices and seat counts.");
        }
    }
}
