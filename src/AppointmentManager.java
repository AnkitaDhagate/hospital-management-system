// AppointmentManager.java
import java.sql.*;
import java.util.Scanner;

public class AppointmentManager {
    
    public static void bookAppointment() {
        Scanner scanner = new Scanner(System.in);
        Connection conn = DatabaseConnection.getConnection();
        
        try {
            System.out.println("\n=== Book Appointment ===");
            
            // Show available patients
            System.out.println("\nAvailable Patients:");
            String patientSql = "SELECT patient_id, first_name || ' ' || last_name as name FROM patients";
            Statement patientStmt = conn.createStatement();
            ResultSet patientRs = patientStmt.executeQuery(patientSql);
            
            while (patientRs.next()) {
                System.out.println(patientRs.getInt("patient_id") + ": " + patientRs.getString("name"));
            }
            patientRs.close();
            patientStmt.close();
            
            System.out.print("\nEnter Patient ID: ");
            int patientId = scanner.nextInt();
            scanner.nextLine(); // consume newline
            
            // Show available doctors
            System.out.println("\nAvailable Doctors:");
            String doctorSql = "SELECT doctor_id, first_name || ' ' || last_name as name, " +
                              "specialization, available_days FROM doctors";
            Statement doctorStmt = conn.createStatement();
            ResultSet doctorRs = doctorStmt.executeQuery(doctorSql);
            
            while (doctorRs.next()) {
                System.out.println(doctorRs.getInt("doctor_id") + ": " + 
                                 doctorRs.getString("name") + " (" + 
                                 doctorRs.getString("specialization") + ") - " +
                                 doctorRs.getString("available_days"));
            }
            doctorRs.close();
            doctorStmt.close();
            
            System.out.print("\nEnter Doctor ID: ");
            int doctorId = scanner.nextInt();
            scanner.nextLine(); // consume newline
            
            System.out.print("Appointment Date (YYYY-MM-DD): ");
            String date = scanner.nextLine();
            
            System.out.print("Appointment Time (e.g., 10:30 AM): ");
            String time = scanner.nextLine();
            
            System.out.print("Symptoms: ");
            String symptoms = scanner.nextLine();
            
            String sql = "INSERT INTO appointments (appointment_id, patient_id, doctor_id, " +
                         "appointment_date, appointment_time, status, symptoms) " +
                         "VALUES (seq_appointment_id.NEXTVAL, ?, ?, TO_DATE(?, 'YYYY-MM-DD'), ?, 'Scheduled', ?)";
            
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, patientId);
            pstmt.setInt(2, doctorId);
            pstmt.setString(3, date);
            pstmt.setString(4, time);
            pstmt.setString(5, symptoms);
            
            int rows = pstmt.executeUpdate();
            if (rows > 0) {
                System.out.println("Appointment booked successfully!");
            }
            
            pstmt.close();
        } catch (SQLException e) {
            System.out.println("Error booking appointment: " + e.getMessage());
        }
    }
    
    public static void viewAppointments() {
        Connection conn = DatabaseConnection.getConnection();
        
        try {
            String sql = "SELECT a.appointment_id, p.first_name || ' ' || p.last_name as patient_name, " +
                         "d.first_name || ' ' || d.last_name as doctor_name, a.appointment_date, " +
                         "a.appointment_time, a.status " +
                         "FROM appointments a " +
                         "JOIN patients p ON a.patient_id = p.patient_id " +
                         "JOIN doctors d ON a.doctor_id = d.doctor_id " +
                         "ORDER BY a.appointment_date DESC";
            
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            
            System.out.println("\n=== All Appointments ===");
            System.out.println("ID\tPatient\t\tDoctor\t\tDate\t\tTime\tStatus");
            System.out.println("---------------------------------------------------------------------------");
            
            while (rs.next()) {
                System.out.printf("%d\t%-15s\t%-15s\t%s\t%s\t%s\n",
                    rs.getInt("appointment_id"),
                    rs.getString("patient_name"),
                    rs.getString("doctor_name"),
                    rs.getDate("appointment_date"),
                    rs.getString("appointment_time"),
                    rs.getString("status"));
            }
            
            rs.close();
            stmt.close();
        } catch (SQLException e) {
            System.out.println("Error viewing appointments: " + e.getMessage());
        }
    }
    
    public static void updateAppointmentStatus() {
        Scanner scanner = new Scanner(System.in);
        Connection conn = DatabaseConnection.getConnection();
        
        try {
            System.out.println("\n=== Update Appointment Status ===");
            viewAppointments();
            
            System.out.print("\nEnter Appointment ID to update: ");
            int appointmentId = scanner.nextInt();
            scanner.nextLine(); // consume newline
            
            System.out.print("Enter new status (Scheduled/Completed/Cancelled): ");
            String status = scanner.nextLine();
            
            System.out.print("Enter diagnosis (if completed): ");
            String diagnosis = scanner.nextLine();
            
            System.out.print("Enter prescription (if completed): ");
            String prescription = scanner.nextLine();
            
            String sql = "UPDATE appointments SET status = ?, diagnosis = ?, prescription = ? " +
                         "WHERE appointment_id = ?";
            
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, status);
            pstmt.setString(2, diagnosis);
            pstmt.setString(3, prescription);
            pstmt.setInt(4, appointmentId);
            
            int rows = pstmt.executeUpdate();
            if (rows > 0) {
                System.out.println("Appointment status updated successfully!");
            } else {
                System.out.println("Appointment ID not found!");
            }
            
            pstmt.close();
        } catch (SQLException e) {
            System.out.println("Error updating appointment: " + e.getMessage());
        }
    }
}