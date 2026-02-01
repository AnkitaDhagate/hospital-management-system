// PatientManager.java
import java.sql.*;
import java.util.Scanner;

public class PatientManager {
    
    public static void addPatient() {
        Scanner scanner = new Scanner(System.in);
        Connection conn = DatabaseConnection.getConnection();
        
        try {
            System.out.println("\n=== Add New Patient ===");
            System.out.print("First Name: ");
            String firstName = scanner.nextLine();
            
            System.out.print("Last Name: ");
            String lastName = scanner.nextLine();
            
            System.out.print("Date of Birth (YYYY-MM-DD): ");
            String dob = scanner.nextLine();
            
            System.out.print("Gender (Male/Female/Other): ");
            String gender = scanner.nextLine();
            
            System.out.print("Address: ");
            String address = scanner.nextLine();
            
            System.out.print("Phone: ");
            String phone = scanner.nextLine();
            
            System.out.print("Email: ");
            String email = scanner.nextLine();
            
            System.out.print("Emergency Contact: ");
            String emergencyContact = scanner.nextLine();
            
            System.out.print("Blood Group: ");
            String bloodGroup = scanner.nextLine();
            
            String sql = "INSERT INTO patients (patient_id, first_name, last_name, date_of_birth, " +
                         "gender, address, phone, email, emergency_contact, blood_group, registration_date) " +
                         "VALUES (seq_patient_id.NEXTVAL, ?, ?, TO_DATE(?, 'YYYY-MM-DD'), ?, ?, ?, ?, ?, ?, SYSDATE)";
            
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, firstName);
            pstmt.setString(2, lastName);
            pstmt.setString(3, dob);
            pstmt.setString(4, gender);
            pstmt.setString(5, address);
            pstmt.setString(6, phone);
            pstmt.setString(7, email);
            pstmt.setString(8, emergencyContact);
            pstmt.setString(9, bloodGroup);
            
            int rows = pstmt.executeUpdate();
            if (rows > 0) {
                System.out.println("Patient added successfully!");
            }
            
            pstmt.close();
        } catch (SQLException e) {
            System.out.println("Error adding patient: " + e.getMessage());
        }
    }
    
    public static void viewAllPatients() {
        Connection conn = DatabaseConnection.getConnection();
        
        try {
            String sql = "SELECT * FROM patients ORDER BY patient_id";
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            
            System.out.println("\n=== All Patients ===");
            System.out.println("ID\tName\t\tPhone\t\tEmail");
            System.out.println("--------------------------------------------------");
            
            while (rs.next()) {
                System.out.println(rs.getInt("patient_id") + "\t" +
                                 rs.getString("first_name") + " " + rs.getString("last_name") + "\t" +
                                 rs.getString("phone") + "\t" + rs.getString("email"));
            }
            
            rs.close();
            stmt.close();
        } catch (SQLException e) {
            System.out.println("Error viewing patients: " + e.getMessage());
        }
    }
    
    public static void searchPatient() {
        Scanner scanner = new Scanner(System.in);
        Connection conn = DatabaseConnection.getConnection();
        
        try {
            System.out.print("\nEnter Patient ID or Name to search: ");
            String search = scanner.nextLine();
            
            String sql = "SELECT * FROM patients WHERE patient_id = ? OR LOWER(first_name) LIKE ? OR LOWER(last_name) LIKE ?";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            
            try {
                int id = Integer.parseInt(search);
                pstmt.setInt(1, id);
                pstmt.setString(2, "%" + search.toLowerCase() + "%");
                pstmt.setString(3, "%" + search.toLowerCase() + "%");
            } catch (NumberFormatException e) {
                pstmt.setInt(1, 0);
                pstmt.setString(2, "%" + search.toLowerCase() + "%");
                pstmt.setString(3, "%" + search.toLowerCase() + "%");
            }
            
            ResultSet rs = pstmt.executeQuery();
            
            System.out.println("\n=== Search Results ===");
            boolean found = false;
            while (rs.next()) {
                found = true;
                System.out.println("Patient ID: " + rs.getInt("patient_id"));
                System.out.println("Name: " + rs.getString("first_name") + " " + rs.getString("last_name"));
                System.out.println("DOB: " + rs.getDate("date_of_birth"));
                System.out.println("Gender: " + rs.getString("gender"));
                System.out.println("Phone: " + rs.getString("phone"));
                System.out.println("Email: " + rs.getString("email"));
                System.out.println("Blood Group: " + rs.getString("blood_group"));
                System.out.println("Registered: " + rs.getDate("registration_date"));
                System.out.println("----------------------------------------");
            }
            
            if (!found) {
                System.out.println("No patients found.");
            }
            
            rs.close();
            pstmt.close();
        } catch (SQLException e) {
            System.out.println("Error searching patient: " + e.getMessage());
        }
    }
}