
public class App {
    public static void main(String[] args) {
        // إنشاء نسخة من نظام الحجز
        BookingSystem bookingSystem = new BookingSystem();

        // تشغيل نافذة تسجيل الدخول
        javax.swing.SwingUtilities.invokeLater(() -> new LoginWindow(bookingSystem));
    }
}