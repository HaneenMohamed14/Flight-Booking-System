import java.sql.*;

public class Agent extends User {
    // خصائص جديدة
    private String department;
    private String commission;

    // كونستركتر جديد
    public Agent(int userId, String username, String password, String name, String email, String contactInfo, String department, String commission) {
        super(userId, username, password, name, email, contactInfo, "agent");  // تأكد من تعديل الـ super ليكون "agent"
        this.department = department;
        this.commission = commission;
    }
    public Agent(int userId, String username, String password, String name, String email, String contactInfo, String role) {
        super(userId, username, password, name, email, contactInfo, role);
    }

    // 1. إدارة الرحلات
    public void manageFlights(Connection conn) {
        try {
            // عرض كل الرحلات
            String query = "SELECT * FROM flights";
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            System.out.println("Flights:");
            while (rs.next()) {
                System.out.println("Flight ID: " + rs.getInt("flight_id") + ", " +
                                   "Destination: " + rs.getString("destination") + ", " +
                                   "Departure: " + rs.getString("departure_time") + ", " +
                                   "Seats Available: " + rs.getInt("seats_available"));
            }

            // إضافة رحلة جديدة
            String addFlightQuery = "INSERT INTO flights (destination, departure_time, seats_available) VALUES (?, ?, ?)";
            PreparedStatement addFlightStmt = conn.prepareStatement(addFlightQuery);
            addFlightStmt.setString(1, "New York");
            addFlightStmt.setString(2, "2025-06-01 10:00:00");
            addFlightStmt.setInt(3, 200);
            addFlightStmt.executeUpdate();
            System.out.println("New flight added.");

            // تعديل بيانات رحلة
            String updateFlightQuery = "UPDATE flights SET seats_available = ? WHERE flight_id = ?";
            PreparedStatement updateFlightStmt = conn.prepareStatement(updateFlightQuery);
            updateFlightStmt.setInt(1, 180); // Example: reducing available seats
            updateFlightStmt.setInt(2, 1);  // Example: update flight with ID 1
            updateFlightStmt.executeUpdate();
            System.out.println("Flight updated.");

            // حذف رحلة
            String deleteFlightQuery = "DELETE FROM flights WHERE flight_id = ?";
            PreparedStatement deleteFlightStmt = conn.prepareStatement(deleteFlightQuery);
            deleteFlightStmt.setInt(1, 3); // Example: delete flight with ID 3
            deleteFlightStmt.executeUpdate();
            System.out.println("Flight deleted.");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // 2. إنشاء حجز لعميل
    public void createBookingForCustomer(Connection conn, int customerId) {
        try {
            // عرض الرحلات المتاحة
            String query = "SELECT * FROM flights WHERE seats_available > 0";
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            System.out.println("Available Flights:");
            while (rs.next()) {
                System.out.println("Flight ID: " + rs.getInt("flight_id") + ", " +
                                   "Destination: " + rs.getString("destination") + ", " +
                                   "Departure: " + rs.getString("departure_time") + ", " +
                                   "Seats Available: " + rs.getInt("seats_available"));
            }

            // اختيار رحلة (افترضنا الاختيار من المستخدم هنا)
            int selectedFlightId = 1; // Example selected flight

            // إنشاء حجز جديد في جدول bookings
            String bookingQuery = "INSERT INTO bookings (customer_id, flight_id) VALUES (?, ?)";
            PreparedStatement bookingStmt = conn.prepareStatement(bookingQuery);
            bookingStmt.setInt(1, customerId);
            bookingStmt.setInt(2, selectedFlightId);
            bookingStmt.executeUpdate();
            System.out.println("Booking created for customer ID: " + customerId);
            // تقليل عدد المقاعد من جدول flights
            String updateSeatsQuery = "UPDATE flights SET seats_available = seats_available - 1 WHERE flight_id = ?";
            PreparedStatement updateSeatsStmt = conn.prepareStatement(updateSeatsQuery);
            updateSeatsStmt.setInt(1, selectedFlightId);
            updateSeatsStmt.executeUpdate();

            // إضافة عملية دفع في جدول payments
            String paymentQuery = "INSERT INTO payments (customer_id, amount, payment_date) VALUES (?, ?, ?)";
            PreparedStatement paymentStmt = conn.prepareStatement(paymentQuery);
            paymentStmt.setInt(1, customerId);
            paymentStmt.setDouble(2, 1000.00); // Example payment amount
            paymentStmt.setString(3, "2025-05-12");
            paymentStmt.executeUpdate();
            System.out.println("Payment added for customer ID: " + customerId);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // 3. تعديل الحجز
    public void modifyBooking(Connection conn, int bookingId) {
        try {
            // البحث عن الحجز باستخدام bookingId
            String query = "SELECT * FROM bookings WHERE booking_id = ?";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setInt(1, bookingId);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                System.out.println("Booking found: Flight ID: " + rs.getInt("flight_id"));
            } else {
                System.out.println("Booking not found.");
                return;
            }

            // تعديل بيانات الرحلة أو المقاعد (مثال: تغيير الرحلة)
            String updateBookingQuery = "UPDATE bookings SET flight_id = ? WHERE booking_id = ?";
            PreparedStatement updateBookingStmt = conn.prepareStatement(updateBookingQuery);
            updateBookingStmt.setInt(1, 2);  // Example: new flight ID
            updateBookingStmt.setInt(2, bookingId);
            updateBookingStmt.executeUpdate();
            System.out.println("Booking updated.");

            // تعديل بيانات الدفع لو لزم
            String updatePaymentQuery = "UPDATE payments SET amount = ? WHERE booking_id = ?";
            PreparedStatement updatePaymentStmt = conn.prepareStatement(updatePaymentQuery);
            updatePaymentStmt.setDouble(1, 1200.00);  // Example: new payment amount
            updatePaymentStmt.setInt(2, bookingId);
            updatePaymentStmt.executeUpdate();
            System.out.println("Payment updated.");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // 4. توليد تقارير
    public void generateReports(Connection conn) {
        try {
            // احصائيات: عدد الرحلات
            String flightQuery = "SELECT COUNT(*) FROM flights";
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(flightQuery);
            int totalFlights = rs.getInt(1);
            System.out.println("Total flights: " + totalFlights);

            // احصائيات: عدد الحجوزات
            String bookingQuery = "SELECT COUNT(*) FROM bookings";
            rs = stmt.executeQuery(bookingQuery);
            int totalBookings = rs.getInt(1);
            System.out.println("Total bookings: " + totalBookings);

            // الرحلة الأكثر حجزًا
            String mostBookedFlightQuery = "SELECT flight_id, COUNT(*) AS bookings_count FROM bookings GROUP BY flight_id ORDER BY bookings_count DESC LIMIT 1";
            rs = stmt.executeQuery(mostBookedFlightQuery);
            if (rs.next()) {
                System.out.println("Most booked flight: " + rs.getInt("flight_id") + " with " + rs.getInt("bookings_count") + " bookings.");
            }

            // إجمالي الأرباح
            String totalRevenueQuery = "SELECT SUM(amount) FROM payments";
            rs = stmt.executeQuery(totalRevenueQuery);
            double totalRevenue = rs.getDouble(1);
            System.out.println("Total revenue: " + totalRevenue);
            } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Getters & Setters للـ properties الجدد (department و commission)
    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getCommission() {
        return commission;
    }

    public void setCommission(String commission) {
        this.commission = commission;
    }
    @Override
    public void login(Connection conn) {
        try {
            String sql = "SELECT * FROM agents WHERE username = ? AND password = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, getUsername());
            stmt.setString(2, getPassword());
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                System.out.println("Agent " + getUsername() + " logged in successfully.");
            } else {
                System.out.println("Login failed for Agent " + getUsername());
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void logout() {
        System.out.println("Agent " + getUsername() + " logged out.");
    }

    @Override
    public void updateProfile(Connection conn) {
        try {
            String sql = "UPDATE agents SET name = ?, email = ?, contactInfo = ? WHERE username = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, getName());
            stmt.setString(2, getEmail());
            stmt.setString(3, getContactInfo());
            stmt.setString(4, getUsername());
            int rowsUpdated = stmt.executeUpdate();

            if (rowsUpdated > 0) {
                System.out.println("Agent " + getUsername() + "'s profile updated.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
