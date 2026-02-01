// LoginManager.java
import java.sql.*;
import java.util.Scanner;

public class LoginManager {
    
    public static boolean authenticateUser() {
        Scanner scanner = new Scanner(System.in);
        Connection conn = DatabaseConnection.getConnection();
        
        try {
            System.out.println("\n=== Hospital Management System Login ===");
            System.out.print("Username: ");
            String username = scanner.nextLine();
            
            System.out.print("Password: ");
            String password = scanner.nextLine();
            
            String sql = "SELECT role FROM users1 WHERE username = ? AND password = ?";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, username);
            pstmt.setString(2, password);
            
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                String role = rs.getString("role");
                System.out.println("\nLogin successful! Welcome " + username + " (" + role + ")");
                rs.close();
                pstmt.close();
                return true;
            } else {
                System.out.println("Invalid username or password!");
                rs.close();
                pstmt.close();
                return false;
            }
            
        } catch (SQLException e) {
            System.out.println("Login error: " + e.getMessage());
            return false;
        }
    }
    
    public static void createUser() {
        Scanner scanner = new Scanner(System.in);
        Connection conn = DatabaseConnection.getConnection();
        
        try {
            System.out.println("\n=== Create New User ===");
            System.out.print("Username: ");
            String username = scanner.nextLine();
            
            System.out.print("Password: ");
            String password = scanner.nextLine();
            
            System.out.print("Role (Admin/Doctor/Receptionist): ");
            String role = scanner.nextLine();
            
            System.out.print("Associated ID (enter 0 if none): ");
            int associatedId = scanner.nextInt();
            scanner.nextLine(); // consume newline
            
            String sql = "INSERT INTO users1 (user_id, username, password, role, associated_id, created_date) " +
                         "VALUES (seq_user_id.NEXTVAL, ?, ?, ?, ?, SYSDATE)";
            
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, username);
            pstmt.setString(2, password);
            pstmt.setString(3, role);
            if (associatedId == 0) {
                pstmt.setNull(4, java.sql.Types.INTEGER);
            } else {
                pstmt.setInt(4, associatedId);
            }
            
            int rows = pstmt.executeUpdate();
            if (rows > 0) {
                System.out.println("User created successfully!");
            }
            
            pstmt.close();
        } catch (SQLException e) {
            System.out.println("Error creating user: " + e.getMessage());
        }
    }
}