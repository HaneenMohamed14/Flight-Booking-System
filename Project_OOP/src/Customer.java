import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class Customer extends User {
    private int customerId; // إضافة customerId
    private String customerAddress;
    private String preferences;

    // Constructor
    public Customer(int userId, String username, String password, String name, String email, String contactInfo, 
                    String customerAddress, String preferences, int customerId) {
        super(userId, username, password, name, email, contactInfo, "customer");
        this.customerAddress = customerAddress;
        this.preferences = preferences;
        this.customerId = customerId; // تعيين customerId
    }
    public Customer(int userId,String username,String password,String name,String email,String contactInfo){
        super(userId, username, password, name, email, contactInfo, "administrator");   
    }
    public Customer(int userId, String username, String password, String name, String email, String contactInfo, String role) {
        super(userId, username, password, name, email, contactInfo, role);
    }
    // Implementing abstract methods
    @Override
    public void login(Connection conn) {
        try {
            String sql = "SELECT * FROM customers WHERE username = ? AND password = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, getUsername());
            stmt.setString(2, getPassword());
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                System.out.println("Customer " + getUsername() + " logged in successfully.");
            } else {
                System.out.println("Login failed for Customer " + getUsername());
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void logout() {
        System.out.println("Customer " + getUsername() + " logged out.");
    }

    @Override
    public void updateProfile(Connection conn) {
        try {
            String sql = "UPDATE customers SET name = ?, email = ?, contactInfo = ? WHERE username = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, getName());
            stmt.setString(2, getEmail());
            stmt.setString(3, getContactInfo());
            stmt.setString(4, getUsername());
            int rowsUpdated = stmt.executeUpdate();

            if (rowsUpdated > 0) {
                System.out.println("Customer " + getUsername() + "'s profile updated.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // 1. Search Flights
    public List<Flight> searchFlights(Connection conn, String origin, String destination, String date) {
        List<Flight> flights = new ArrayList<>();
        try {
            String sql = "SELECT * FROM flights WHERE origin = ? AND destination = ? AND departureTime LIKE ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, origin);
            stmt.setString(2, destination);
            stmt.setString(3, "%" + date + "%");
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Flight flight = new Flight(
                    rs.getString("flightNumber"),
                    rs.getString("airline"),
                    rs.getString("origin"),
                    rs.getString("destination"),
                    rs.getString("departureTime"),
                    rs.getString("arrivalTime"),
                    rs.getDouble("economyPrice"),
                    rs.getDouble("businessPrice"),
                    rs.getDouble("firstClassPrice"),
                    rs.getInt("totalEconomySeats"),
                    rs.getInt("totalBusinessSeats"),
                    rs.getInt("totalFirstClassSeats")
                );
                flights.add(flight);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return flights;
    }

    // 2. Create Booking
    public void createBooking(Connection conn, Flight flight, String seatClass, String seatNumber) {
        try {
            String sql = "INSERT INTO bookings (booking_reference, customer_id, flight_id, seat_class, seat_number, status, payment_status) VALUES (?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, "BR" + System.currentTimeMillis());  // Generating unique booking reference
            stmt.setInt(2, getCustomerId());  // استخدام customerId هنا
            stmt.setString(4, seatClass);
            stmt.setString(5, seatNumber);
            stmt.setString(6, "Reserved");
            stmt.setString(7, "Pending");
            stmt.executeUpdate();

            System.out.println("Booking created successfully.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // 3. View Bookings
    public List<Booking> viewBookings(Connection conn) {
        List<Booking> bookings = new ArrayList<>();
        try {
            String sql = "SELECT * FROM bookings WHERE customer_id = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, getCustomerId());  // استخدام customerId هنا
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Booking booking = new Booking(                   
                    rs.getString("booking_reference"),
                    rs.getInt("customer_id"),
                    rs.getString("flight_number"),
                    rs.getString("seat_class"),
                    rs.getString("seat_number"),
                    rs.getString("status"),
                    rs.getString("payment_status")
                );
                bookings.add(booking);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return bookings;
    }

    // 4. Cancel Booking
    public void cancelBooking(Connection conn, int bookingId) {
        try {
            String sql = "UPDATE bookings SET status = 'Cancelled' WHERE booking_id = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, bookingId);
            int rowsUpdated = stmt.executeUpdate();

            if (rowsUpdated > 0) {
                System.out.println("Booking with ID " + bookingId + " has been cancelled.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Getters & Setters
    public int getCustomerId() {
        return customerId;
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    public String getCustomerAddress() {
        return customerAddress;
    }

    public void setCustomerAddress(String customerAddress) {
        this.customerAddress = customerAddress;
    }

    public String getPhoneNumber() {
        return preferences;
    }

    public void setPhoneNumber(String preferences) {
        this.preferences= preferences;
    }
}