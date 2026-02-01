// DoctorManager.java
import java.sql.*;
import java.util.Scanner;

public class DoctorManager {
    
    public static void addDoctor() {
        Scanner scanner = new Scanner(System.in);
        Connection conn = DatabaseConnection.getConnection();
        
        try {
            System.out.println("\n=== Add New Doctor ===");
            System.out.print("First Name: ");
            String firstName = scanner.nextLine();
            
            System.out.print("Last Name: ");
            String lastName = scanner.nextLine();
            
            System.out.print("Specialization: ");
            String specialization = scanner.nextLine();
            
            System.out.print("Qualification: ");
            String qualification = scanner.nextLine();
            
            System.out.print("Experience Years: ");
            int experience = scanner.nextInt();
            scanner.nextLine(); // consume newline
            
            System.out.print("Phone: ");
            String phone = scanner.nextLine();
            
            System.out.print("Email: ");
            String email = scanner.nextLine();
            
            System.out.print("Consultation Fee: ");
            double fee = scanner.nextDouble();
            scanner.nextLine(); // consume newline
            
            System.out.print("Available Days (e.g., Mon,Wed,Fri): ");
            String days = scanner.nextLine();
            
            System.out.print("Available Time (e.g., 10:00 AM - 5:00 PM): ");
            String time = scanner.nextLine();
            
            String sql = "INSERT INTO doctors (doctor_id, first_name, last_name, specialization, " +
                         "qualification, experience_years, phone, email, consultation_fee, " +
                         "available_days, available_time) " +
                         "VALUES (seq_doctor_id.NEXTVAL, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
            
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, firstName);
            pstmt.setString(2, lastName);
            pstmt.setString(3, specialization);
            pstmt.setString(4, qualification);
            pstmt.setInt(5, experience);
            pstmt.setString(6, phone);
            pstmt.setString(7, email);
            pstmt.setDouble(8, fee);
            pstmt.setString(9, days);
            pstmt.setString(10, time);
            
            int rows = pstmt.executeUpdate();
            if (rows > 0) {
                System.out.println("Doctor added successfully!");
            }
            
            pstmt.close();
        } catch (SQLException e) {
            System.out.println("Error adding doctor: " + e.getMessage());
        }
    }
    
    public static void viewAllDoctors() {
        Connection conn = DatabaseConnection.getConnection();
        
        try {
            String sql = "SELECT * FROM doctors ORDER BY doctor_id";
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            
            System.out.println("\n=== All Doctors ===");
            System.out.println("ID\tName\t\tSpecialization\t\tFee");
            System.out.println("--------------------------------------------------------------");
            
            while (rs.next()) {
                System.out.printf("%d\t%s %s\t%-20s\t%.2f\n",
                    rs.getInt("doctor_id"),
                    rs.getString("first_name"),
                    rs.getString("last_name"),
                    rs.getString("specialization"),
                    rs.getDouble("consultation_fee"));
            }
            
            rs.close();
            stmt.close();
        } catch (SQLException e) {
            System.out.println("Error viewing doctors: " + e.getMessage());
        }
    }
    
    public static void viewDoctorSchedule() {
        Connection conn = DatabaseConnection.getConnection();
        
        try {
            String sql = "SELECT doctor_id, first_name || ' ' || last_name as name, " +
                         "specialization, available_days, available_time " +
                         "FROM doctors ORDER BY doctor_id";
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            
            System.out.println("\n=== Doctor Schedule ===");
            System.out.println("Doctor\t\tSpecialization\t\tAvailable Days\t\tTiming");
            System.out.println("--------------------------------------------------------------------------------");
            
            while (rs.next()) {
                System.out.printf("%s\t%-20s\t%-15s\t%s\n",
                    rs.getString("name"),
                    rs.getString("specialization"),
                    rs.getString("available_days"),
                    rs.getString("available_time"));
            }
            
            rs.close();
            stmt.close();
        } catch (SQLException e) {
            System.out.println("Error viewing schedule: " + e.getMessage());
        }
    }
}