import java.sql.*;

public class Administrator extends User {
    private int admin_id;
    private String security_level;

    // Constructor
    public Administrator(int userId, String username, String password, String name, String email, String contactInfo, int admin_id, String security_level) {
        super(userId, username, password, name, email, contactInfo, "administrator");  // تغيير النوع لـ "administrator"
        this.admin_id = admin_id;
        this.security_level = security_level;
    }

    // 1. إنشاء مستخدم جديد
    public void createUser(Connection conn, String username, String password, String name, String email, String userType) {
        try {
            String query = "INSERT INTO users (username, password, name, email, user_type) VALUES (?, ?, ?, ?, ?)";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setString(1, username);
            stmt.setString(2, password);  // تأكد من تشفير كلمة المرور
            stmt.setString(3, name);
            stmt.setString(4, email);
            stmt.setString(5, userType);
            stmt.executeUpdate();
            System.out.println("User created successfully!");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // 2. تعديل إعدادات النظام
    public void modifySystemSettings(Connection conn, String settingName, String settingValue) {
        try {
            String query = "UPDATE system_settings SET setting_value = ? WHERE setting_name = ?";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setString(1, settingValue);
            stmt.setString(2, settingName);
            stmt.executeUpdate();
            System.out.println("System setting updated successfully!");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // 3. عرض سجلات النظام
    public void viewSystemLogs(Connection conn) {
        try {
            String query = "SELECT * FROM system_logs";
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            while (rs.next()) {
                System.out.println("Log ID: " + rs.getInt("log_id") + ", " +
                                   "Activity: " + rs.getString("activity") + ", " +
                                   "Date: " + rs.getString("log_date"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // 4. إدارة صلاحيات المستخدمين
    public void manageUserAccess(Connection conn, int userId, String newUserType) {
        try {
            String query = "UPDATE users SET user_type = ? WHERE user_id = ?";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setString(1, newUserType);
            stmt.setInt(2, userId);
            stmt.executeUpdate();
            System.out.println("User access updated successfully!");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Getters & setters for admin_id and security_level
    public int getAdmin_id() {
        return admin_id;
    }

    public void setAdmin_id(int admin_id) {
        this.admin_id = admin_id;
    }

    public String getSecurity_level() {
        return security_level;
    }

    public void setSecurity_level(String security_level) {
        this.security_level = security_level;
    }
    //ccc
    public Administrator(int userId, String username, String password, String name, String email, String contactInfo, String role) {
        super(userId, username, password, name, email, contactInfo, role);
    }
    @Override
    public void login(Connection conn) {
        try {
            String sql = "SELECT * FROM administrators WHERE username = ? AND password = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, getUsername());
            stmt.setString(2, getPassword());
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                System.out.println("Administrator " + getUsername() + " logged in successfully.");
            } else {
                System.out.println("Login failed for Administrator " + getUsername());
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void logout() {
        System.out.println("Administrator " + getUsername() + " logged out.");
    }

    @Override
    public void updateProfile(Connection conn) {
        try {
            String sql = "UPDATE administrators SET name = ?, email = ?, contactInfo = ? WHERE username = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, getName());
            stmt.setString(2, getEmail());
            stmt.setString(3, getContactInfo());
            stmt.setString(4, getUsername());
            int rowsUpdated = stmt.executeUpdate();

            if (rowsUpdated > 0) {
                System.out.println("Administrator " + getUsername() + "'s profile updated.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
