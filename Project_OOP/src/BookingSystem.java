import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class BookingSystem {

    private User currentUser;
    private Connection connection;
    private List<Payment> payments;

    // Constructor to initialize database connection
    public BookingSystem() {
        try {
            this.connection = DatabaseManager.getConnection();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // تسجيل الدخول
    public User login(String username, String password) {
    String query = "SELECT * FROM users WHERE username = ? AND password = ?";
    try (PreparedStatement stmt = connection.prepareStatement(query)) {
        stmt.setString(1, username);
        stmt.setString(2, password);
        ResultSet rs = stmt.executeQuery();
        if (rs.next()) {
            int userId = rs.getInt("user_id");
            String name = rs.getString("name");
            String email = rs.getString("email");
            String contactInfo = rs.getString("contact_info");
            String role = rs.getString("role");

            switch (role.toLowerCase()) {
    case "admin":
        currentUser = new Administrator(userId, username, password, name, email, contactInfo, "admin");
        break;
    case "agent":
        currentUser = new Agent(userId, username, password, name, email, contactInfo, "agent");
        break;
    case "customer":
        currentUser = new Customer(userId, username, password, name, email, contactInfo, "customer");
        break;
}
            return currentUser;
        }
    } catch (SQLException e) {
        e.printStackTrace();
    }
    return null;
}
    public User getCurrentUser(){ return currentUser ;}

    // تسجيل خروج
    public void logout() {
        currentUser = null;
    }

    // البحث عن الرحلات
    public List<Flight> searchFlights(String origin, String destination, String date) {
        String query = "SELECT * FROM flights WHERE origin = ? AND destination = ? AND departure_time >= ?";
        List<Flight> flights = new ArrayList<>();
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, origin);
            stmt.setString(2, destination);
            stmt.setString(3, date);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                flights.add(new Flight(
                        rs.getString("flight_number"),
                        rs.getString("airline"),
                        rs.getString("origin"),
                        rs.getString("destination"),
                        rs.getString("departure_time"),
                        rs.getString("arrival_time"),
                        rs.getDouble("economy_price"),
                        rs.getDouble("business_price"),
                        rs.getDouble("first_class_price"),
                        rs.getInt("total_economy_seats"),
                        rs.getInt("total_business_seats"),
                        rs.getInt("total_first_class_seats")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return flights;
    }

    // إنشاء الحجز
    public boolean createBooking(Booking booking, List<Passenger> passengers) {
    String insertBookingQuery = "INSERT INTO bookings (booking_reference, customer_id, flight_number, seat_class, seat_number, status, payment_status) VALUES (?, ?, ?, ?, ?, ?, ?)";

    try (PreparedStatement stmt = connection.prepareStatement(insertBookingQuery, Statement.RETURN_GENERATED_KEYS)) {
        stmt.setString(1, booking.getBookingReference());
        stmt.setInt(2, booking.getCustomerId());
        stmt.setString(3, booking.getFlightNumber()); // استخدم flight_number كنص
        stmt.setString(4, booking.getSeatClass());
        stmt.setString(5, booking.getSeatNumber());
        stmt.setString(6, booking.getStatus());
        stmt.setString(7, booking.getPaymentStatus());

        int affectedRows = stmt.executeUpdate();
        if (affectedRows > 0) {
            ResultSet rs = stmt.getGeneratedKeys();
            if (rs.next()) {
                int bookingId = rs.getInt(1);
                for (Passenger passenger : passengers) {
                    addPassengerToBooking(passenger, bookingId);
                }
                return true;
            }
        }
    } catch (SQLException e) {
        e.printStackTrace();
    }
    return false;
}
    public boolean createBooking(String flightNum, int customerId, String seatClass) {
    Booking booking = new Booking();
    booking.setBookingReference("REF" + System.currentTimeMillis());
    booking.setFlightNumber(flightNum); 
    booking.setCustomerId(customerId);  // استخدام customerId الصحيح
    booking.setSeatClass(seatClass);
    booking.setSeatNumber("A1");  
    booking.setStatus("Confirmed");
    booking.setPaymentStatus("Paid");

    return createBooking(booking, new ArrayList<>());
}

    // إضافة راكب لحجز
    private void addPassengerToBooking(Passenger passenger, int bookingId) {
        String insertPassengerQuery = "INSERT INTO passengers ( name, passport_number, date_of_birth, special_requests) VALUES ( ?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(insertPassengerQuery)) {
            stmt.setString(2, passenger.getName());
            stmt.setString(3, passenger.getPassportNumber());
            stmt.setString(4, passenger.getDateOfBirth());
            stmt.setString(5, passenger.getSpecialRequests());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // تنفيذ الدفع
    public boolean processPayment(Payment payment) {
        String insertPaymentQuery = "INSERT INTO payments ( amount, method, status, transaction_date) VALUES ( ?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(insertPaymentQuery)) {
            stmt.setString(1, payment.getbookingRefrence());
            stmt.setDouble(2, payment.getAmount());
            stmt.setString(3, payment.getMethod());
            stmt.setString(4, payment.getStatus());
            stmt.setString(5, payment.getTransactionDate());
            int affectedRows = stmt.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // إنشاء تذكرة
    public void generateTicket(Booking booking) {
        System.out.println("Ticket generated for booking reference: " + booking.getBookingReference());
    }
    public List<Payment> getPayments() {
        List<Payment> paymentsList = new ArrayList<>();
        String query = "SELECT * FROM payments";  // تعديل هذا الاستعلام بناءً على هيكل الجدول الخاص بك
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {
            
            while (rs.next()) {
                int paymentId = rs.getInt("payment_id");
                String bookingRefrence=rs.getString("bookingRefrence");
                double amount = rs.getDouble("amount");
                String currency=rs.getString("currency");
                String method=rs.getString("method");
                String status = rs.getString("status");
                String transactionDate=rs.getString("transaction_date");
                Payment payment = new Payment(paymentId, bookingRefrence, amount,currency,method,status,transactionDate);
                paymentsList.add(payment);
            }
        } catch (SQLException e) {
            System.out.println("Error fetching payments: " + e.getMessage());
        }
        return paymentsList;
    }
    public boolean modifyBooking(String bookingRef, String seatType, String status , String pay_stat) {
    Booking booking = getBookingByReference(bookingRef);
    if (booking != null) {
        // تعديل بيانات الحجز
        booking.setSeatClass(seatType);
        booking.setStatus(status);
        booking.setPaymentStatus(pay_stat);
        // تحديث الحجز في قاعدة البيانات أو في الذاكرة
        updateBookingInDatabase(booking);
        return true;
    }
    return false;
}

public Booking getBookingByReference(String bookingRef) {
    String query = "SELECT * FROM bookings WHERE booking_reference = ?";

    try (Connection conn = DatabaseManager.getConnection();
         PreparedStatement pstmt = conn.prepareStatement(query)) {

        pstmt.setString(1, bookingRef);
        ResultSet rs = pstmt.executeQuery();

        if (rs.next()) {
            // استرجاع بيانات الحجز
            String seatClass = rs.getString("seat_class");
            String status = rs.getString("status");
            String flightNumber = rs.getString("flight_number");
            int customerId = rs.getInt("customer_id");
            String bookingReference = rs.getString("booking_reference");
            String seatNumber = rs.getString("seat_number");
            String paymentStatus = rs.getString("payment_status");

            // إنشاء كائن الحجز وربطه بالرحلة والعميل (حسب التصميم لديك)
            Booking booking = new Booking(
                bookingReference,
                customerId,
                flightNumber,
                seatClass,
                seatNumber,
                status,
                paymentStatus
            );
            return booking;
        }

    } catch (SQLException e) {
        e.printStackTrace();
    }

    return null;
}

private void updateBookingInDatabase(Booking booking) {
    String query = "UPDATE bookings SET seat_class = ?, status = ?, payment_status = ? WHERE booking_reference = ?";
    try (Connection conn = DatabaseManager.getConnection();
         PreparedStatement pstmt = conn.prepareStatement(query)) {
        pstmt.setString(1, booking.getSeatClass());
        pstmt.setString(2, booking.getStatus());
        pstmt.setString(3, booking.getPaymentStatus());
        pstmt.setString(4, booking.getBookingReference());
        pstmt.executeUpdate();
    } catch (SQLException e) {
        e.printStackTrace();
    }
}
public Flight getFlightByNumber(String flightNumber) {
    String query = "SELECT * FROM flights WHERE flight_number = ?";
    try (Connection conn = DatabaseManager.getConnection();
         PreparedStatement pstmt = conn.prepareStatement(query)) {

        pstmt.setString(1, flightNumber);
        ResultSet rs = pstmt.executeQuery();

        if (rs.next()) {
            return new Flight(
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
        }

    } catch (SQLException e) {
        e.printStackTrace();
    }
    return null;
}
public Customer getCustomerById(int customerId) {
    String query = "SELECT * FROM users WHERE id = ? AND role = 'Customer'";
    try (Connection conn = DatabaseManager.getConnection();
         PreparedStatement pstmt = conn.prepareStatement(query)) {

        pstmt.setInt(1, customerId);
        ResultSet rs = pstmt.executeQuery();

        if (rs.next()) {
            Customer customer = new Customer(
                rs.getInt("user_id"),
                rs.getString("username"),
                rs.getString("password"),
                rs.getString("name"),
                rs.getString("email"),
                rs.getString("contact_info"),
                rs.getString("address"),
                rs.getString("preferences"),
                rs.getInt("customer_id")
            );
            return customer;
        }

    } catch (SQLException e) {
        e.printStackTrace();
    }
    return null;
}
public List<Flight> getAllFlights() {
    List<Flight> flights = new ArrayList<>();
    String query = "SELECT * FROM flights";

    try (Statement stmt = connection.createStatement();
         ResultSet rs = stmt.executeQuery(query)) {

        while (rs.next()) {
            Flight flight = new Flight();
            flight.setFlightNumber(rs.getString("flight_number"));
            flight.setAirline(rs.getString("airline"));
            flight.setOrigin(rs.getString("origin"));
            flight.setDestination(rs.getString("destination"));
            flight.setDepartureTime(rs.getString("departure_time"));
            flight.setArrivalTime(rs.getString("arrival_time"));
            // أضف أي خصائص إضافية لو عندك

            flights.add(flight);
        }
    } catch (SQLException e) {
        e.printStackTrace();
    }

    return flights;
}
// 1. إنشاء مستخدم جديد
public boolean createUser(String username, String password, String name, String email, String userType) {
    String query = "INSERT INTO users (username, password, name, email, role) VALUES (?, ?, ?, ?, ?)";
    try (PreparedStatement stmt = connection.prepareStatement(query)) {
        stmt.setString(1, username);
        stmt.setString(2, password);
        stmt.setString(3, name);
        stmt.setString(4, email);
        stmt.setString(5, userType);
        stmt.executeUpdate();
        System.out.println("User created successfully!");
        return true;
    } catch (SQLException e) {
        e.printStackTrace();
    }
    return false;
}
//add flight
public boolean addFlight(String flightNumber, String airline, String origin, String destination,
                         String departureTime, String arrivalTime,
                         double economyPrice, double businessPrice, double firstClassPrice,
                         int totalEconomySeats, int totalBusinessSeats, int totalFirstClassSeats) {

    String query = "INSERT INTO flights (flight_number, airline, origin, destination, departure_time, arrival_time, " +
            "economy_price, business_price, first_class_price, " +
            "total_economy_seats, total_business_seats, total_first_class_seats) " +
            "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

    try (Connection conn = DatabaseManager.getConnection();
         PreparedStatement pstmt = conn.prepareStatement(query)) {

        pstmt.setString(1, flightNumber);
        pstmt.setString(2, airline);
        pstmt.setString(3, origin);
        pstmt.setString(4, destination);
        pstmt.setString(5, departureTime);
        pstmt.setString(6, arrivalTime);
        pstmt.setDouble(7, economyPrice);
        pstmt.setDouble(8, businessPrice);
        pstmt.setDouble(9, firstClassPrice);
        pstmt.setInt(10, totalEconomySeats);
        pstmt.setInt(11, totalBusinessSeats);
        pstmt.setInt(12, totalFirstClassSeats);

        int rowsInserted = pstmt.executeUpdate();
        return rowsInserted > 0;

    } catch (SQLException e) {
        e.printStackTrace();
        return false;
    }
}public List<Booking> getCustomerBookings(int customerId) {
    List<Booking> bookings = new ArrayList<>();
    String query = "SELECT * FROM bookings WHERE customer_id = ?";

    try (Connection conn = DatabaseManager.getConnection();
         PreparedStatement pstmt = conn.prepareStatement(query)) {
        pstmt.setInt(1, customerId);
        ResultSet rs = pstmt.executeQuery();

        while (rs.next()) {
            String bookingReference = rs.getString("booking_reference");
            String flightNumber = rs.getString("flight_number");
            String seatClass = rs.getString("seat_class");
            String seatNumber = rs.getString("seat_number");
            String status = rs.getString("status");
            String paymentStatus = rs.getString("payment_status");

            bookings.add(new Booking(
                bookingReference, customerId, flightNumber,
                seatClass, seatNumber, status, paymentStatus
            ));
        }

    } catch (SQLException e) {
        e.printStackTrace();
    }

    return bookings;
}
    public List<Booking> getAllBookings() {
        List<Booking> bookings = new ArrayList<>();
        String query = "SELECT * FROM bookings";

        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                String bookingReference = rs.getString("booking_reference");
                int customerId = rs.getInt("customer_id");
                String flightNumber = rs.getString("flight_number");
                String seatClass = rs.getString("seat_class");
                String seatNumber = rs.getString("seat_number");
                String status = rs.getString("status");
                String paymentStatus = rs.getString("payment_status");

                bookings.add(new Booking(
                    bookingReference, customerId, flightNumber,
                    seatClass, seatNumber, status, paymentStatus
                ));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return bookings;
    }
  
}

