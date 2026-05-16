import java.sql.*;

public class Booking {

    
    private String bookingReference;
    private int customerId;
    private String flightNum;
    private String seatClass;
    private String seatNumber;
    private String status;
    private String paymentStatus;
    public Booking(){}
    public Booking( String bookingReference, int customerId, String flightnum, String seatClass, 
                   String seatNumber, String status, String paymentStatus) {
        
        this.bookingReference = bookingReference;
        this.customerId = customerId;
        this.flightNum = flightnum;
        this.seatClass = seatClass;
        this.seatNumber = seatNumber;
        this.status = status;
        this.paymentStatus = paymentStatus;
    }

    // إضافة مسافر إلى الحجز
    public void addPassenger(Connection conn, int bookingId, String name, String passportNumber, String dateOfBirth, String specialRequests) throws SQLException {
        String query = "INSERT INTO passengers (booking_id, name, passport_number, date_of_birth, special_requests) " +
                       "VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, bookingId);
            stmt.setString(2, name);
            stmt.setString(3, passportNumber);
            stmt.setString(4, dateOfBirth);
            stmt.setString(5, specialRequests);
            stmt.executeUpdate();
        }
    }

    // تعديل بيانات المسافر
    public void modifyPassengerDetails(Connection conn, int passengerId, String name, String passportNumber, String dateOfBirth, String specialRequests) throws SQLException {
        String query = "UPDATE passengers SET name = ?, passport_number = ?, date_of_birth = ?, special_requests = ? WHERE passenger_id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, name);
            stmt.setString(2, passportNumber);
            stmt.setString(3, dateOfBirth);
            stmt.setString(4, specialRequests);
            stmt.setInt(5, passengerId);
            stmt.executeUpdate();
        }
    }

    // حذف مسافر من الحجز
    public void removePassenger(Connection conn, int passengerId) throws SQLException {
        String query = "DELETE FROM passengers WHERE passenger_id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, passengerId);
            stmt.executeUpdate();
        }
    }

    // استرجاع تفاصيل مسافر
    public void getPassengerDetails(Connection conn, int passengerId) throws SQLException {
        String query = "SELECT * FROM passengers WHERE passenger_id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, passengerId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                System.out.println("Passenger ID: " + rs.getInt("passenger_id"));
                System.out.println("Name: " + rs.getString("name"));
                System.out.println("Passport Number: " + rs.getString("passport_number"));
                System.out.println("Date of Birth: " + rs.getString("date_of_birth"));
                System.out.println("Special Requests: " + rs.getString("special_requests"));
            } else {
                System.out.println("No passenger found with ID: " + passengerId);
            }
        }
    }

    // Getters & Setters
    

    public String getBookingReference() {
        return bookingReference;
    }

    public void setBookingReference(String bookingReference) {
        this.bookingReference = bookingReference;
    }

    public int getCustomerId() {
        return customerId;
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    public String getFlightNumber() {
        return flightNum;
    }

    public void setFlightNumber(String flightnm) {
        this.flightNum = flightnm;
    }

    public String getSeatClass() {
        return seatClass;
    }

    public void setSeatClass(String seatClass) {
        this.seatClass = seatClass;
    }
    public String getSeatNumber() {
        return seatNumber;
    }

    public void setSeatNumber(String seatNumber) {
        this.seatNumber = seatNumber;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getPaymentStatus() {
        return paymentStatus;
    }

    public void setPaymentStatus(String paymentStatus) {
        this.paymentStatus = paymentStatus;
    }
}