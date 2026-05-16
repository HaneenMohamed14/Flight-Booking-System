import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.List;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class SearchFlightsWindow extends JFrame {
    private BookingSystem bookingSystem;
    private JTextField originField, destinationField, dateField;
    private JTable resultsTable;

    public SearchFlightsWindow(BookingSystem bookingSystem) {
        this.bookingSystem = bookingSystem;
        setTitle("Search Flights");
        setSize(700, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        initComponents();
        setVisible(true);
    }

    private void initComponents() {
        JPanel searchPanel = new JPanel(new GridLayout(2, 4, 10, 10));
        searchPanel.setBorder(BorderFactory.createTitledBorder("Search Criteria"));

        originField = new JTextField();
        destinationField = new JTextField();
        dateField = new JTextField(); // Example: "2025-05-12"

        JButton searchButton = new JButton("Search");
        searchButton.addActionListener(this::handleSearch);

        searchPanel.add(new JLabel("Origin:"));
        searchPanel.add(originField);
        searchPanel.add(new JLabel("Destination:"));
        searchPanel.add(destinationField);
        searchPanel.add(new JLabel("Date (YYYY-MM-DD):"));
        searchPanel.add(dateField);
        searchPanel.add(new JLabel(""));
        searchPanel.add(searchButton);

        resultsTable = new JTable();
        JScrollPane scrollPane = new JScrollPane(resultsTable);

        setLayout(new BorderLayout());
        add(searchPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
    }

    private void handleSearch(ActionEvent e) {
        String origin = originField.getText().trim();
        String destination = destinationField.getText().trim();
        String date = dateField.getText().trim();

        List<Flight> flights = bookingSystem.searchFlights(origin, destination, date);

        if (flights.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No flights found.", "Search Result", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        // Fill table
        DefaultTableModel model = new DefaultTableModel();
        model.setColumnIdentifiers(new String[]{"Flight No", "Airline", "Origin", "Destination", "Departure", "Arrival", "Economy", "Business", "First"});

        for (Flight f : flights) {
            model.addRow(new Object[]{
                f.getFlightNumber(),
                f.getAirline(),
                f.getOrigin(),
                f.getDestination(),
                f.getDepartureTime(),
                f.getArrivalTime(),
                f.getTotalEconomySeats() + " $" + f.getEconomyPrice(),
                f.getTotalBusinessSeats() + " $" + f.getBusinessPrice(),
                f.getTotalFirstClassSeats() + " $" + f.getFirstClassPrice()
            });
        }

        resultsTable.setModel(model);
    }
}