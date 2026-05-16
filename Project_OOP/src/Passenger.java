import java.sql.*;

public class Passenger {
    private int passengerId;
    private int bookingId;
    private String name;
    private String passportNumber;
    private String dateOfBirth;
    private String specialRequests;

    // Constructor
    public Passenger(int passengerId, int bookingId, String name, String passportNumber, String dateOfBirth, String specialRequests) {
        this.passengerId = passengerId;
        this.bookingId = bookingId;
        this.name = name;
        this.passportNumber = passportNumber;
        this.dateOfBirth = dateOfBirth;
        this.specialRequests = specialRequests;
    }

    // Getters and Setters
    public int getPassengerId() {
        return passengerId;
    }

    public void setPassengerId(int passengerId) {
        this.passengerId = passengerId;
    }

    public int getBookingId() {
        return bookingId;
    }

    public void setBookingId(int bookingId) {
        this.bookingId = bookingId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassportNumber() {
        return passportNumber;
    }

    public void setPassportNumber(String passportNumber) {
        this.passportNumber = passportNumber;
    }

    public String getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(String dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getSpecialRequests() {
        return specialRequests;
    }

    public void setSpecialRequests(String specialRequests) {
        this.specialRequests = specialRequests;
    }

    // Method to update passenger information
    public void updateInfo(String name, String passportNumber, String dateOfBirth, String specialRequests) {
        this.name = name;
        this.passportNumber = passportNumber;
        this.dateOfBirth = dateOfBirth;
        this.specialRequests = specialRequests;
    }

    // Method to get passenger details
    public String getPassengerDetails() {
        return "Passenger ID: " + passengerId + ", Name: " + name + ", Passport Number: " + passportNumber +
               ", Date of Birth: " + dateOfBirth + ", Special Requests: " + specialRequests;
    }

    // Method to save passenger info to the database
    public void savePassenger(Connection conn) throws SQLException {
        String sql = "INSERT INTO passengers (booking_id, name, passport_number, date_of_birth, special_requests) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, this.bookingId);
            stmt.setString(2, this.name);
            stmt.setString(3, this.passportNumber);
            stmt.setString(4, this.dateOfBirth);
            stmt.setString(5, this.specialRequests);
            stmt.executeUpdate();
        }
    }

    // Method to update passenger info in the database
    public void updatePassengerInfo(Connection conn) throws SQLException {
        String sql = "UPDATE passengers SET name = ?, passport_number = ?, date_of_birth = ?, special_requests = ? WHERE passenger_id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, this.name);
            stmt.setString(2, this.passportNumber);
            stmt.setString(3, this.dateOfBirth);
            stmt.setString(4, this.specialRequests);
            stmt.setInt(5, this.passengerId);
            stmt.executeUpdate();
        }
    }

    // Method to get passenger details from the database
    public static Passenger getPassengerDetailsFromDB(Connection conn, int passengerId) throws SQLException {
        String sql = "SELECT * FROM passengers WHERE passenger_id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, passengerId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                int bookingId = rs.getInt("booking_id");
                String name = rs.getString("name");
                String passportNumber = rs.getString("passport_number");
                String dateOfBirth = rs.getString("date_of_birth");
                String specialRequests = rs.getString("special_requests");
                return new Passenger(passengerId, bookingId, name, passportNumber, dateOfBirth, specialRequests);
            } else {
                return null; // No passenger found
            }
        }
    }
}