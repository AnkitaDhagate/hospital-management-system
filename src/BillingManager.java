// BillingManager.java
import java.sql.*;
import java.util.Scanner;

public class BillingManager {
    
    public static void generateBill() {
        Scanner scanner = new Scanner(System.in);
        Connection conn = DatabaseConnection.getConnection();
        
        try {
            System.out.println("\n=== Generate Bill ===");
            
            // Show appointments that need billing
            String sql = "SELECT a.appointment_id, p.first_name || ' ' || p.last_name as patient_name, " +
                         "d.first_name || ' ' || d.last_name as doctor_name, d.consultation_fee " +
                         "FROM appointments a " +
                         "JOIN patients p ON a.patient_id = p.patient_id " +
                         "JOIN doctors d ON a.doctor_id = d.doctor_id " +
                         "WHERE a.status = 'Completed' AND NOT EXISTS (" +
                         "SELECT 1 FROM billing b WHERE b.appointment_id = a.appointment_id)";
            
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            
            System.out.println("\nAppointments pending billing:");
            System.out.println("ID\tPatient\t\tDoctor\t\tConsultation Fee");
            System.out.println("--------------------------------------------------");
            
            boolean hasAppointments = false;
            while (rs.next()) {
                hasAppointments = true;
                System.out.printf("%d\t%-15s\t%-15s\t%.2f\n",
                    rs.getInt("appointment_id"),
                    rs.getString("patient_name"),
                    rs.getString("doctor_name"),
                    rs.getDouble("consultation_fee"));
            }
            
            rs.close();
            stmt.close();
            
            if (!hasAppointments) {
                System.out.println("No appointments pending billing.");
                return;
            }
            
            System.out.print("\nEnter Appointment ID: ");
            int appointmentId = scanner.nextInt();
            scanner.nextLine(); // consume newline
            
            // Get appointment details
            String checkSql = "SELECT a.patient_id, a.appointment_id, d.consultation_fee " +
                             "FROM appointments a " +
                             "JOIN doctors d ON a.doctor_id = d.doctor_id " +
                             "WHERE a.appointment_id = ? AND a.status = 'Completed'";
            
            PreparedStatement checkStmt = conn.prepareStatement(checkSql);
            checkStmt.setInt(1, appointmentId);
            ResultSet checkRs = checkStmt.executeQuery();
            
            if (checkRs.next()) {
                int patientId = checkRs.getInt("patient_id");
                double consultationFee = checkRs.getDouble("consultation_fee");
                
                System.out.print("Medication Charges: ");
                double medication = scanner.nextDouble();
                
                System.out.print("Test Charges: ");
                double test = scanner.nextDouble();
                
                System.out.print("Other Charges: ");
                double other = scanner.nextDouble();
                
                System.out.print("Payment Status (Pending/Paid/Partial): ");
                scanner.nextLine(); // consume newline
                String paymentStatus = scanner.nextLine();
                
                String insertSql = "INSERT INTO billing (bill_id, patient_id, appointment_id, " +
                                  "bill_date, consultation_fee, medication_charges, " +
                                  "test_charges, other_charges, payment_status) " +
                                  "VALUES (seq_bill_id.NEXTVAL, ?, ?, SYSDATE, ?, ?, ?, ?, ?)";
                
                PreparedStatement insertStmt = conn.prepareStatement(insertSql);
                insertStmt.setInt(1, patientId);
                insertStmt.setInt(2, appointmentId);
                insertStmt.setDouble(3, consultationFee);
                insertStmt.setDouble(4, medication);
                insertStmt.setDouble(5, test);
                insertStmt.setDouble(6, other);
                insertStmt.setString(7, paymentStatus);
                
                int rows = insertStmt.executeUpdate();
                if (rows > 0) {
                    System.out.println("Bill generated successfully!");
                    
                    // Show total amount
                    String totalSql = "SELECT total_amount FROM billing WHERE appointment_id = ?";
                    PreparedStatement totalStmt = conn.prepareStatement(totalSql);
                    totalStmt.setInt(1, appointmentId);
                    ResultSet totalRs = totalStmt.executeQuery();
                    
                    if (totalRs.next()) {
                        System.out.println("Total Amount: " + totalRs.getDouble("total_amount"));
                    }
                    
                    totalRs.close();
                    totalStmt.close();
                }
                
                insertStmt.close();
            } else {
                System.out.println("Appointment not found or not completed!");
            }
            
            checkRs.close();
            checkStmt.close();
            
        } catch (SQLException e) {
            System.out.println("Error generating bill: " + e.getMessage());
        }
    }
    
    public static void viewBills() {
        Connection conn = DatabaseConnection.getConnection();
        
        try {
            String sql = "SELECT b.bill_id, p.first_name || ' ' || p.last_name as patient_name, " +
                         "b.bill_date, b.consultation_fee, b.medication_charges, " +
                         "b.test_charges, b.other_charges, b.total_amount, b.payment_status " +
                         "FROM billing b " +
                         "JOIN patients p ON b.patient_id = p.patient_id " +
                         "ORDER BY b.bill_date DESC";
            
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            
            System.out.println("\n=== All Bills ===");
            System.out.println("ID\tPatient\t\tDate\t\tTotal\t\tStatus");
            System.out.println("-------------------------------------------------------");
            
            while (rs.next()) {
                System.out.printf("%d\t%-15s\t%s\t%.2f\t%s\n",
                    rs.getInt("bill_id"),
                    rs.getString("patient_name"),
                    rs.getDate("bill_date"),
                    rs.getDouble("total_amount"),
                    rs.getString("payment_status"));
            }
            
            rs.close();
            stmt.close();
        } catch (SQLException e) {
            System.out.println("Error viewing bills: " + e.getMessage());
        }
    }
    
    public static void viewRevenueReport() {
        Connection conn = DatabaseConnection.getConnection();
        
        try {
            String sql = "SELECT " +
                         "TO_CHAR(bill_date, 'YYYY-MM') as month, " +
                         "COUNT(*) as total_bills, " +
                         "SUM(total_amount) as total_revenue, " +
                         "SUM(CASE WHEN payment_status = 'Paid' THEN total_amount ELSE 0 END) as paid_amount, " +
                         "SUM(CASE WHEN payment_status = 'Pending' THEN total_amount ELSE 0 END) as pending_amount " +
                         "FROM billing " +
                         "GROUP BY TO_CHAR(bill_date, 'YYYY-MM') " +
                         "ORDER BY month DESC";
            
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            
            System.out.println("\n=== Revenue Report ===");
            System.out.println("Month\t\tBills\tTotal Revenue\tPaid\t\tPending");
            System.out.println("--------------------------------------------------------------");
            
            while (rs.next()) {
                System.out.printf("%s\t%d\t%.2f\t\t%.2f\t%.2f\n",
                    rs.getString("month"),
                    rs.getInt("total_bills"),
                    rs.getDouble("total_revenue"),
                    rs.getDouble("paid_amount"),
                    rs.getDouble("pending_amount"));
            }
            
            rs.close();
            stmt.close();
        } catch (SQLException e) {
            System.out.println("Error viewing revenue report: " + e.getMessage());
        }
    }
}