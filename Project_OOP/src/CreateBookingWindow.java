import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.List;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class CreateBookingWindow extends JFrame {
    private BookingSystem bookingSystem;
    private int userId;
    private JTable flightsTable;
    private JComboBox<String> seatTypeCombo;

    public CreateBookingWindow(BookingSystem bookingSystem, int userId) {
        this.bookingSystem = bookingSystem;
        this.userId = userId;
        setTitle("Create Booking");
        setSize(700, 400);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        initComponents();
        setVisible(true);
    }

    private void initComponents() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));

        // جدول الرحلات
        flightsTable = new JTable();
        loadFlights();

        JScrollPane scrollPane = new JScrollPane(flightsTable);
        panel.add(scrollPane, BorderLayout.CENTER);

        // لوحة أسفل الجدول لاختيار نوع المقعد وزر الحجز
        JPanel bottomPanel = new JPanel(new FlowLayout());

        seatTypeCombo = new JComboBox<>(new String[]{"Economy", "Business" , "First Class"});
        JButton bookButton = new JButton("Book");
        bookButton.addActionListener(this::handleBooking);

        bottomPanel.add(new JLabel("Seat Type:"));
        bottomPanel.add(seatTypeCombo);
        bottomPanel.add(bookButton);

        panel.add(bottomPanel, BorderLayout.SOUTH);
        add(panel);
    }

    private void loadFlights() {
        List<Flight> flights = bookingSystem.getAllFlights();
        String[] columnNames = {"Flight Number", "Airline", "Origin", "Destination", "Departure Time" ,"Arrival Time"};
        DefaultTableModel model = new DefaultTableModel(columnNames, 0);

        for (Flight flight : flights) {
            Object[] row = {
                flight.getFlightNumber(),
                flight.getAirline(),
                flight.getOrigin(),
                flight.getDestination(),
                flight.getDepartureTime(),
                flight.getArrivalTime()
            };
            model.addRow(row);
        }

        flightsTable.setModel(model);
    }

    private void handleBooking(ActionEvent e) {
        int selectedRow = flightsTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a flight first.");
            return;
        }

        String flightNum = (String) flightsTable.getValueAt(selectedRow, 0);
        String seatType = (String) seatTypeCombo.getSelectedItem();

        boolean success = bookingSystem.createBooking(flightNum, userId, seatType);
        if (success) {
            JOptionPane.showMessageDialog(this, "Booking successful!");
            dispose();
        } else {
            JOptionPane.showMessageDialog(this, "Booking failed. Please try again.");
        }
    }
}