// Main.java
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.Scanner;

public class Main {
    private static Connection connection;
    private static Scanner scanner = new Scanner(System.in);
    private static SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
    private static String currentUser = null;
    private static String currentUserRole = null;

    public static void main(String[] args) {
        System.out.println("==========================================");
        System.out.println("   HOSPITAL MANAGEMENT SYSTEM");
        System.out.println("==========================================");
        
        // Connect to database
        connectToDatabase();
        
        // Login
        if (login()) {
            showMainMenu();
        }
        
        // Close resources
        closeResources();
    }
    
    private static void connectToDatabase() {
        try {
            // Load Oracle JDBC driver
            Class.forName("oracle.jdbc.driver.OracleDriver");
            
            // Database connection details
            String url = "jdbc:oracle:thin:@localhost:1521:XE";
            String username = "hospital_admin";
            String password = "hospital123";
            
            // Establish connection
            connection = DriverManager.getConnection(url, username, password);
            System.out.println("✅ Database connected successfully!\n");
            
        } catch (ClassNotFoundException e) {
            System.out.println("❌ Oracle JDBC Driver not found!");
            System.exit(1);
        } catch (SQLException e) {
            System.out.println("❌ Database connection failed: " + e.getMessage());
            System.exit(1);
        }
    }
    
    private static boolean login() {
        System.out.println("=== LOGIN ===");
        System.out.print("Username: ");
        String username = scanner.nextLine();
        
        System.out.print("Password: ");
        String password = scanner.nextLine();
        
        try {
            String sql = "SELECT role FROM users1 WHERE username = ? AND password = ?";
            PreparedStatement pstmt = connection.prepareStatement(sql);
            pstmt.setString(1, username);
            pstmt.setString(2, password);
            
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                currentUser = username;
                currentUserRole = rs.getString("role");
                System.out.println("\n✅ Login successful! Welcome " + username + " (" + currentUserRole + ")");
                return true;
            } else {
                System.out.println("❌ Invalid username or password!");
                return false;
            }
            
        } catch (SQLException e) {
            System.out.println("❌ Login error: " + e.getMessage());
            return false;
        }
    }
    
    private static void showMainMenu() {
        boolean exit = false;
        
        while (!exit) {
            System.out.println("\n=== MAIN MENU ===");
            System.out.println("1. Patient Management");
            System.out.println("2. Doctor Management");
            System.out.println("3. Appointment Management");
            System.out.println("4. Billing Management");
            System.out.println("5. Medical Records");
            System.out.println("6. Reports");
            System.out.println("7. User Management");
            System.out.println("8. View Dashboard");
            System.out.println("9. Logout");
            System.out.println("0. Exit");
            System.out.print("Enter your choice: ");
            
            int choice = getIntInput();
            
            switch (choice) {
                case 1:
                    patientManagement();
                    break;
                case 2:
                    doctorManagement();
                    break;
                case 3:
                    appointmentManagement();
                    break;
                case 4:
                    billingManagement();
                    break;
                case 5:
                    medicalRecordsManagement();
                    break;
                case 6:
                    reportsManagement();
                    break;
                case 7:
                    userManagement();
                    break;
                case 8:
                    viewDashboard();
                    break;
                case 9:
                    System.out.println("Logging out...");
                    currentUser = null;
                    currentUserRole = null;
                    if (login()) {
                        showMainMenu();
                    } else {
                        exit = true;
                    }
                    break;
                case 0:
                    exit = true;
                    break;
                default:
                    System.out.println("❌ Invalid choice! Please try again.");
            }
        }
    }
    
    private static void patientManagement() {
        boolean back = false;
        
        while (!back) {
            System.out.println("\n=== PATIENT MANAGEMENT ===");
            System.out.println("1. Register New Patient");
            System.out.println("2. View All Patients");
            System.out.println("3. Search Patient");
            System.out.println("4. Update Patient Information");
            System.out.println("5. View Patient Medical History");
            System.out.println("6. Back to Main Menu");
            System.out.print("Enter your choice: ");
            
            int choice = getIntInput();
            
            switch (choice) {
                case 1:
                    registerPatient();
                    break;
                case 2:
                    viewAllPatients();
                    break;
                case 3:
                    searchPatient();
                    break;
                case 4:
                    updatePatient();
                    break;
                case 5:
                    viewPatientHistory();
                    break;
                case 6:
                    back = true;
                    break;
                default:
                    System.out.println("❌ Invalid choice!");
            }
        }
    }
    
    private static void registerPatient() {
        try {
            System.out.println("\n=== REGISTER NEW PATIENT ===");
            
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
            
            PreparedStatement pstmt = connection.prepareStatement(sql);
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
                System.out.println("✅ Patient registered successfully!");
                
                // Get the generated patient ID
                Statement stmt = connection.createStatement();
                ResultSet rs = stmt.executeQuery("SELECT seq_patient_id.CURRVAL FROM dual");
                if (rs.next()) {
                    System.out.println("Patient ID: " + rs.getInt(1));
                }
                rs.close();
                stmt.close();
            }
            
            pstmt.close();
        } catch (SQLException e) {
            System.out.println("❌ Error registering patient: " + e.getMessage());
        }
    }
    
    private static void viewAllPatients() {
        try {
            String sql = "SELECT * FROM patients ORDER BY patient_id";
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            
            System.out.println("\n=== ALL PATIENTS ===");
            System.out.printf("%-10s %-20s %-15s %-15s %-10s %-30s\n", 
                "ID", "Name", "Phone", "Email", "Gender", "Blood Group");
            System.out.println("--------------------------------------------------------------------------------");
            
            while (rs.next()) {
                System.out.printf("%-10d %-20s %-15s %-15s %-10s %-10s\n",
                    rs.getInt("patient_id"),
                    rs.getString("first_name") + " " + rs.getString("last_name"),
                    rs.getString("phone"),
                    rs.getString("email"),
                    rs.getString("gender"),
                    rs.getString("blood_group"));
            }
            
            // Get total count
            ResultSet countRs = connection.createStatement().executeQuery("SELECT COUNT(*) FROM patients");
            if (countRs.next()) {
                System.out.println("\nTotal Patients: " + countRs.getInt(1));
            }
            countRs.close();
            
            rs.close();
            stmt.close();
        } catch (SQLException e) {
            System.out.println("❌ Error viewing patients: " + e.getMessage());
        }
    }
    
    private static void searchPatient() {
        System.out.print("\nEnter Patient ID or Name to search: ");
        String search = scanner.nextLine();
        
        try {
            String sql = "SELECT * FROM patients WHERE patient_id = ? OR LOWER(first_name) LIKE ? OR LOWER(last_name) LIKE ?";
            PreparedStatement pstmt = connection.prepareStatement(sql);
            
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
            
            System.out.println("\n=== SEARCH RESULTS ===");
            boolean found = false;
            while (rs.next()) {
                found = true;
                System.out.println("Patient ID: " + rs.getInt("patient_id"));
                System.out.println("Name: " + rs.getString("first_name") + " " + rs.getString("last_name"));
                System.out.println("DOB: " + rs.getDate("date_of_birth"));
                System.out.println("Gender: " + rs.getString("gender"));
                System.out.println("Address: " + rs.getString("address"));
                System.out.println("Phone: " + rs.getString("phone"));
                System.out.println("Email: " + rs.getString("email"));
                System.out.println("Emergency Contact: " + rs.getString("emergency_contact"));
                System.out.println("Blood Group: " + rs.getString("blood_group"));
                System.out.println("Registered: " + rs.getDate("registration_date"));
                System.out.println("----------------------------------------");
            }
            
            if (!found) {
                System.out.println("❌ No patients found.");
            }
            
            rs.close();
            pstmt.close();
        } catch (SQLException e) {
            System.out.println("❌ Error searching patient: " + e.getMessage());
        }
    }
    
    private static void updatePatient() {
        System.out.print("\nEnter Patient ID to update: ");
        int patientId = getIntInput();
        scanner.nextLine(); // consume newline
        
        try {
            // Check if patient exists
            String checkSql = "SELECT * FROM patients WHERE patient_id = ?";
            PreparedStatement checkStmt = connection.prepareStatement(checkSql);
            checkStmt.setInt(1, patientId);
            ResultSet rs = checkStmt.executeQuery();
            
            if (!rs.next()) {
                System.out.println("❌ Patient ID not found!");
                return;
            }
            
            System.out.println("\nCurrent Information:");
            System.out.println("Name: " + rs.getString("first_name") + " " + rs.getString("last_name"));
            System.out.println("Phone: " + rs.getString("phone"));
            System.out.println("Email: " + rs.getString("email"));
            System.out.println("Address: " + rs.getString("address"));
            
            System.out.println("\n=== UPDATE PATIENT ===");
            System.out.print("New Phone (press Enter to keep current): ");
            String newPhone = scanner.nextLine();
            if (newPhone.isEmpty()) newPhone = rs.getString("phone");
            
            System.out.print("New Email (press Enter to keep current): ");
            String newEmail = scanner.nextLine();
            if (newEmail.isEmpty()) newEmail = rs.getString("email");
            
            System.out.print("New Address (press Enter to keep current): ");
            String newAddress = scanner.nextLine();
            if (newAddress.isEmpty()) newAddress = rs.getString("address");
            
            System.out.print("New Emergency Contact (press Enter to keep current): ");
            String newEmergency = scanner.nextLine();
            if (newEmergency.isEmpty()) newEmergency = rs.getString("emergency_contact");
            
            String updateSql = "UPDATE patients SET phone = ?, email = ?, address = ?, emergency_contact = ? WHERE patient_id = ?";
            PreparedStatement updateStmt = connection.prepareStatement(updateSql);
            updateStmt.setString(1, newPhone);
            updateStmt.setString(2, newEmail);
            updateStmt.setString(3, newAddress);
            updateStmt.setString(4, newEmergency);
            updateStmt.setInt(5, patientId);
            
            int rows = updateStmt.executeUpdate();
            if (rows > 0) {
                System.out.println("✅ Patient information updated successfully!");
            }
            
            rs.close();
            checkStmt.close();
            updateStmt.close();
        } catch (SQLException e) {
            System.out.println("❌ Error updating patient: " + e.getMessage());
        }
    }
    
    private static void viewPatientHistory() {
        System.out.print("\nEnter Patient ID: ");
        int patientId = getIntInput();
        
        try {
            // Get patient info
            String patientSql = "SELECT * FROM patients WHERE patient_id = ?";
            PreparedStatement patientStmt = connection.prepareStatement(patientSql);
            patientStmt.setInt(1, patientId);
            ResultSet patientRs = patientStmt.executeQuery();
            
            if (!patientRs.next()) {
                System.out.println("❌ Patient ID not found!");
                return;
            }
            
            System.out.println("\n=== PATIENT MEDICAL HISTORY ===");
            System.out.println("Patient: " + patientRs.getString("first_name") + " " + patientRs.getString("last_name"));
            System.out.println("ID: " + patientRs.getInt("patient_id"));
            System.out.println("Blood Group: " + patientRs.getString("blood_group"));
            System.out.println("----------------------------------------");
            
            // Get medical records
            String recordsSql = "SELECT mr.*, d.first_name || ' ' || d.last_name as doctor_name " +
                               "FROM medical_records mr " +
                               "LEFT JOIN doctors d ON mr.doctor_id = d.doctor_id " +
                               "WHERE mr.patient_id = ? ORDER BY mr.visit_date DESC";
            PreparedStatement recordsStmt = connection.prepareStatement(recordsSql);
            recordsStmt.setInt(1, patientId);
            ResultSet recordsRs = recordsStmt.executeQuery();
            
            boolean hasRecords = false;
            while (recordsRs.next()) {
                hasRecords = true;
                System.out.println("\nVisit Date: " + recordsRs.getDate("visit_date"));
                System.out.println("Doctor: " + recordsRs.getString("doctor_name"));
                System.out.println("Diagnosis: " + recordsRs.getString("diagnosis"));
                System.out.println("Treatment: " + recordsRs.getString("treatment"));
                System.out.println("Medications: " + recordsRs.getString("medications"));
                System.out.println("Test Results: " + recordsRs.getString("test_results"));
                if (recordsRs.getDate("next_visit_date") != null) {
                    System.out.println("Next Visit: " + recordsRs.getDate("next_visit_date"));
                }
                System.out.println("----------------------------------------");
            }
            
            if (!hasRecords) {
                System.out.println("No medical records found for this patient.");
            }
            
            // Get appointments
            System.out.println("\n=== APPOINTMENT HISTORY ===");
            String apptSql = "SELECT a.*, d.first_name || ' ' || d.last_name as doctor_name " +
                            "FROM appointments a " +
                            "JOIN doctors d ON a.doctor_id = d.doctor_id " +
                            "WHERE a.patient_id = ? ORDER BY a.appointment_date DESC";
            PreparedStatement apptStmt = connection.prepareStatement(apptSql);
            apptStmt.setInt(1, patientId);
            ResultSet apptRs = apptStmt.executeQuery();
            
            boolean hasAppointments = false;
            while (apptRs.next()) {
                hasAppointments = true;
                System.out.println("\nDate: " + apptRs.getDate("appointment_date"));
                System.out.println("Time: " + apptRs.getString("appointment_time"));
                System.out.println("Doctor: " + apptRs.getString("doctor_name"));
                System.out.println("Status: " + apptRs.getString("status"));
                System.out.println("Symptoms: " + apptRs.getString("symptoms"));
                System.out.println("Diagnosis: " + apptRs.getString("diagnosis"));
                System.out.println("----------------------------------------");
            }
            
            if (!hasAppointments) {
                System.out.println("No appointments found for this patient.");
            }
            
            patientRs.close();
            recordsRs.close();
            apptRs.close();
            patientStmt.close();
            recordsStmt.close();
            apptStmt.close();
        } catch (SQLException e) {
            System.out.println("❌ Error viewing patient history: " + e.getMessage());
        }
    }
    
    private static void doctorManagement() {
        boolean back = false;
        
        while (!back) {
            System.out.println("\n=== DOCTOR MANAGEMENT ===");
            System.out.println("1. Add New Doctor");
            System.out.println("2. View All Doctors");
            System.out.println("3. Search Doctor");
            System.out.println("4. Update Doctor Information");
            System.out.println("5. View Doctor Schedule");
            System.out.println("6. Back to Main Menu");
            System.out.print("Enter your choice: ");
            
            int choice = getIntInput();
            
            switch (choice) {
                case 1:
                    addDoctor();
                    break;
                case 2:
                    viewAllDoctors();
                    break;
                case 3:
                    searchDoctor();
                    break;
                case 4:
                    updateDoctor();
                    break;
                case 5:
                    viewDoctorSchedule();
                    break;
                case 6:
                    back = true;
                    break;
                default:
                    System.out.println("❌ Invalid choice!");
            }
        }
    }
    
    private static void addDoctor() {
        try {
            System.out.println("\n=== ADD NEW DOCTOR ===");
            
            System.out.print("First Name: ");
            String firstName = scanner.nextLine();
            
            System.out.print("Last Name: ");
            String lastName = scanner.nextLine();
            
            System.out.print("Specialization: ");
            String specialization = scanner.nextLine();
            
            System.out.print("Qualification: ");
            String qualification = scanner.nextLine();
            
            System.out.print("Experience Years: ");
            int experience = getIntInput();
            scanner.nextLine(); // consume newline
            
            System.out.print("Phone: ");
            String phone = scanner.nextLine();
            
            System.out.print("Email: ");
            String email = scanner.nextLine();
            
            System.out.print("Consultation Fee: ");
            double fee = getDoubleInput();
            scanner.nextLine(); // consume newline
            
            System.out.print("Available Days (e.g., Mon,Wed,Fri): ");
            String days = scanner.nextLine();
            
            System.out.print("Available Time (e.g., 10:00 AM - 5:00 PM): ");
            String time = scanner.nextLine();
            
            String sql = "INSERT INTO doctors (doctor_id, first_name, last_name, specialization, " +
                         "qualification, experience_years, phone, email, consultation_fee, " +
                         "available_days, available_time) " +
                         "VALUES (seq_doctor_id.NEXTVAL, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
            
            PreparedStatement pstmt = connection.prepareStatement(sql);
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
                System.out.println("✅ Doctor added successfully!");
                
                // Get the generated doctor ID
                Statement stmt = connection.createStatement();
                ResultSet rs = stmt.executeQuery("SELECT seq_doctor_id.CURRVAL FROM dual");
                if (rs.next()) {
                    System.out.println("Doctor ID: " + rs.getInt(1));
                }
                rs.close();
                stmt.close();
            }
            
            pstmt.close();
        } catch (SQLException e) {
            System.out.println("❌ Error adding doctor: " + e.getMessage());
        }
    }
    
    private static void viewAllDoctors() {
        try {
            String sql = "SELECT * FROM doctors ORDER BY doctor_id";
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            
            System.out.println("\n=== ALL DOCTORS ===");
            System.out.printf("%-10s %-25s %-20s %-15s %-10s %-15s\n", 
                "ID", "Name", "Specialization", "Experience", "Fee", "Phone");
            System.out.println("----------------------------------------------------------------------------------------");
            
            while (rs.next()) {
                System.out.printf("%-10d %-25s %-20s %-15d $%-9.2f %-15s\n",
                    rs.getInt("doctor_id"),
                    "Dr. " + rs.getString("first_name") + " " + rs.getString("last_name"),
                    rs.getString("specialization"),
                    rs.getInt("experience_years"),
                    rs.getDouble("consultation_fee"),
                    rs.getString("phone"));
            }
            
            // Get total count
            ResultSet countRs = connection.createStatement().executeQuery("SELECT COUNT(*) FROM doctors");
            if (countRs.next()) {
                System.out.println("\nTotal Doctors: " + countRs.getInt(1));
            }
            countRs.close();
            
            rs.close();
            stmt.close();
        } catch (SQLException e) {
            System.out.println("❌ Error viewing doctors: " + e.getMessage());
        }
    }
    
    private static void searchDoctor() {
        System.out.print("\nEnter Doctor ID, Name, or Specialization to search: ");
        String search = scanner.nextLine();
        
        try {
            String sql = "SELECT * FROM doctors WHERE doctor_id = ? OR LOWER(first_name) LIKE ? OR " +
                        "LOWER(last_name) LIKE ? OR LOWER(specialization) LIKE ?";
            PreparedStatement pstmt = connection.prepareStatement(sql);
            
            try {
                int id = Integer.parseInt(search);
                pstmt.setInt(1, id);
                pstmt.setString(2, "%" + search.toLowerCase() + "%");
                pstmt.setString(3, "%" + search.toLowerCase() + "%");
                pstmt.setString(4, "%" + search.toLowerCase() + "%");
            } catch (NumberFormatException e) {
                pstmt.setInt(1, 0);
                pstmt.setString(2, "%" + search.toLowerCase() + "%");
                pstmt.setString(3, "%" + search.toLowerCase() + "%");
                pstmt.setString(4, "%" + search.toLowerCase() + "%");
            }
            
            ResultSet rs = pstmt.executeQuery();
            
            System.out.println("\n=== SEARCH RESULTS ===");
            boolean found = false;
            while (rs.next()) {
                found = true;
                System.out.println("Doctor ID: " + rs.getInt("doctor_id"));
                System.out.println("Name: Dr. " + rs.getString("first_name") + " " + rs.getString("last_name"));
                System.out.println("Specialization: " + rs.getString("specialization"));
                System.out.println("Qualification: " + rs.getString("qualification"));
                System.out.println("Experience: " + rs.getInt("experience_years") + " years");
                System.out.println("Phone: " + rs.getString("phone"));
                System.out.println("Email: " + rs.getString("email"));
                System.out.println("Consultation Fee: $" + rs.getDouble("consultation_fee"));
                System.out.println("Available Days: " + rs.getString("available_days"));
                System.out.println("Available Time: " + rs.getString("available_time"));
                System.out.println("----------------------------------------");
            }
            
            if (!found) {
                System.out.println("❌ No doctors found.");
            }
            
            rs.close();
            pstmt.close();
        } catch (SQLException e) {
            System.out.println("❌ Error searching doctor: " + e.getMessage());
        }
    }
    
    private static void updateDoctor() {
        System.out.print("\nEnter Doctor ID to update: ");
        int doctorId = getIntInput();
        scanner.nextLine(); // consume newline
        
        try {
            // Check if doctor exists
            String checkSql = "SELECT * FROM doctors WHERE doctor_id = ?";
            PreparedStatement checkStmt = connection.prepareStatement(checkSql);
            checkStmt.setInt(1, doctorId);
            ResultSet rs = checkStmt.executeQuery();
            
            if (!rs.next()) {
                System.out.println("❌ Doctor ID not found!");
                return;
            }
            
            System.out.println("\nCurrent Information:");
            System.out.println("Name: Dr. " + rs.getString("first_name") + " " + rs.getString("last_name"));
            System.out.println("Phone: " + rs.getString("phone"));
            System.out.println("Email: " + rs.getString("email"));
            System.out.println("Consultation Fee: $" + rs.getDouble("consultation_fee"));
            
            System.out.println("\n=== UPDATE DOCTOR ===");
            System.out.print("New Phone (press Enter to keep current): ");
            String newPhone = scanner.nextLine();
            if (newPhone.isEmpty()) newPhone = rs.getString("phone");
            
            System.out.print("New Email (press Enter to keep current): ");
            String newEmail = scanner.nextLine();
            if (newEmail.isEmpty()) newEmail = rs.getString("email");
            
            System.out.print("New Consultation Fee (press Enter to keep current): ");
            String feeInput = scanner.nextLine();
            double newFee;
            if (feeInput.isEmpty()) {
                newFee = rs.getDouble("consultation_fee");
            } else {
                newFee = Double.parseDouble(feeInput);
            }
            
            System.out.print("New Available Days (press Enter to keep current): ");
            String newDays = scanner.nextLine();
            if (newDays.isEmpty()) newDays = rs.getString("available_days");
            
            System.out.print("New Available Time (press Enter to keep current): ");
            String newTime = scanner.nextLine();
            if (newTime.isEmpty()) newTime = rs.getString("available_time");
            
            String updateSql = "UPDATE doctors SET phone = ?, email = ?, consultation_fee = ?, " +
                              "available_days = ?, available_time = ? WHERE doctor_id = ?";
            PreparedStatement updateStmt = connection.prepareStatement(updateSql);
            updateStmt.setString(1, newPhone);
            updateStmt.setString(2, newEmail);
            updateStmt.setDouble(3, newFee);
            updateStmt.setString(4, newDays);
            updateStmt.setString(5, newTime);
            updateStmt.setInt(6, doctorId);
            
            int rows = updateStmt.executeUpdate();
            if (rows > 0) {
                System.out.println("✅ Doctor information updated successfully!");
            }
            
            rs.close();
            checkStmt.close();
            updateStmt.close();
        } catch (SQLException e) {
            System.out.println("❌ Error updating doctor: " + e.getMessage());
        }
    }
    
    private static void viewDoctorSchedule() {
        try {
            String sql = "SELECT doctor_id, first_name || ' ' || last_name as name, " +
                         "specialization, available_days, available_time, phone " +
                         "FROM doctors ORDER BY doctor_id";
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            
            System.out.println("\n=== DOCTOR SCHEDULE ===");
            System.out.printf("%-10s %-25s %-20s %-20s %-20s %-15s\n", 
                "ID", "Name", "Specialization", "Available Days", "Timing", "Phone");
            System.out.println("------------------------------------------------------------------------------------------------------------");
            
            while (rs.next()) {
                System.out.printf("%-10d %-25s %-20s %-20s %-20s %-15s\n",
                    rs.getInt("doctor_id"),
                    rs.getString("name"),
                    rs.getString("specialization"),
                    rs.getString("available_days"),
                    rs.getString("available_time"),
                    rs.getString("phone"));
            }
            
            rs.close();
            stmt.close();
        } catch (SQLException e) {
            System.out.println("❌ Error viewing schedule: " + e.getMessage());
        }
    }
    
    private static void appointmentManagement() {
        boolean back = false;
        
        while (!back) {
            System.out.println("\n=== APPOINTMENT MANAGEMENT ===");
            System.out.println("1. Book Appointment");
            System.out.println("2. View All Appointments");
            System.out.println("3. View Today's Appointments");
            System.out.println("4. Update Appointment Status");
            System.out.println("5. Cancel Appointment");
            System.out.println("6. Back to Main Menu");
            System.out.print("Enter your choice: ");
            
            int choice = getIntInput();
            
            switch (choice) {
                case 1:
                    bookAppointment();
                    break;
                case 2:
                    viewAllAppointments();
                    break;
                case 3:
                    viewTodayAppointments();
                    break;
                case 4:
                    updateAppointmentStatus();
                    break;
                case 5:
                    cancelAppointment();
                    break;
                case 6:
                    back = true;
                    break;
                default:
                    System.out.println("❌ Invalid choice!");
            }
        }
    }
    
    private static void bookAppointment() {
        try {
            System.out.println("\n=== BOOK APPOINTMENT ===");
            
            // Show available patients
            System.out.println("\nAvailable Patients:");
            String patientSql = "SELECT patient_id, first_name || ' ' || last_name as name FROM patients ORDER BY patient_id";
            Statement patientStmt = connection.createStatement();
            ResultSet patientRs = patientStmt.executeQuery(patientSql);
            
            System.out.printf("%-10s %-30s\n", "ID", "Name");
            System.out.println("--------------------------------");
            while (patientRs.next()) {
                System.out.printf("%-10d %-30s\n",
                    patientRs.getInt("patient_id"),
                    patientRs.getString("name"));
            }
            patientRs.close();
            patientStmt.close();
            
            System.out.print("\nEnter Patient ID: ");
            int patientId = getIntInput();
            scanner.nextLine(); // consume newline
            
            // Show available doctors
            System.out.println("\nAvailable Doctors:");
            String doctorSql = "SELECT doctor_id, first_name || ' ' || last_name as name, " +
                              "specialization, available_days FROM doctors ORDER BY doctor_id";
            Statement doctorStmt = connection.createStatement();
            ResultSet doctorRs = doctorStmt.executeQuery(doctorSql);
            
            System.out.printf("%-10s %-30s %-20s %-20s\n", "ID", "Name", "Specialization", "Available Days");
            System.out.println("--------------------------------------------------------------------");
            while (doctorRs.next()) {
                System.out.printf("%-10d %-30s %-20s %-20s\n",
                    doctorRs.getInt("doctor_id"),
                    doctorRs.getString("name"),
                    doctorRs.getString("specialization"),
                    doctorRs.getString("available_days"));
            }
            doctorRs.close();
            doctorStmt.close();
            
            System.out.print("\nEnter Doctor ID: ");
            int doctorId = getIntInput();
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
            
            PreparedStatement pstmt = connection.prepareStatement(sql);
            pstmt.setInt(1, patientId);
            pstmt.setInt(2, doctorId);
            pstmt.setString(3, date);
            pstmt.setString(4, time);
            pstmt.setString(5, symptoms);
            
            int rows = pstmt.executeUpdate();
            if (rows > 0) {
                System.out.println("✅ Appointment booked successfully!");
                
                // Get the generated appointment ID
                Statement stmt = connection.createStatement();
                ResultSet rs = stmt.executeQuery("SELECT seq_appointment_id.CURRVAL FROM dual");
                if (rs.next()) {
                    System.out.println("Appointment ID: " + rs.getInt(1));
                }
                rs.close();
                stmt.close();
            }
            
            pstmt.close();
        } catch (SQLException e) {
            System.out.println("❌ Error booking appointment: " + e.getMessage());
        }
    }
    
    private static void viewAllAppointments() {
        try {
            String sql = "SELECT a.appointment_id, p.first_name || ' ' || p.last_name as patient_name, " +
                         "d.first_name || ' ' || d.last_name as doctor_name, a.appointment_date, " +
                         "a.appointment_time, a.status, a.symptoms " +
                         "FROM appointments a " +
                         "JOIN patients p ON a.patient_id = p.patient_id " +
                         "JOIN doctors d ON a.doctor_id = d.doctor_id " +
                         "ORDER BY a.appointment_date DESC";
            
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            
            System.out.println("\n=== ALL APPOINTMENTS ===");
            System.out.printf("%-12s %-20s %-25s %-15s %-15s %-15s %-30s\n", 
                "ID", "Patient", "Doctor", "Date", "Time", "Status", "Symptoms");
            System.out.println("--------------------------------------------------------------------------------------------------------------------");
            
            while (rs.next()) {
                System.out.printf("%-12d %-20s %-25s %-15s %-15s %-15s %-30s\n",
                    rs.getInt("appointment_id"),
                    rs.getString("patient_name"),
                    rs.getString("doctor_name"),
                    rs.getDate("appointment_date"),
                    rs.getString("appointment_time"),
                    rs.getString("status"),
                    rs.getString("symptoms"));
            }
            
            // Get total count
            ResultSet countRs = connection.createStatement().executeQuery("SELECT COUNT(*) FROM appointments");
            if (countRs.next()) {
                System.out.println("\nTotal Appointments: " + countRs.getInt(1));
            }
            countRs.close();
            
            rs.close();
            stmt.close();
        } catch (SQLException e) {
            System.out.println("❌ Error viewing appointments: " + e.getMessage());
        }
    }
    
    private static void viewTodayAppointments() {
        try {
            String sql = "SELECT a.appointment_id, p.first_name || ' ' || p.last_name as patient_name, " +
                         "d.first_name || ' ' || d.last_name as doctor_name, a.appointment_time, " +
                         "a.status, a.symptoms " +
                         "FROM appointments a " +
                         "JOIN patients p ON a.patient_id = p.patient_id " +
                         "JOIN doctors d ON a.doctor_id = d.doctor_id " +
                         "WHERE a.appointment_date = TRUNC(SYSDATE) " +
                         "ORDER BY a.appointment_time";
            
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            
            System.out.println("\n=== TODAY'S APPOINTMENTS ===");
            System.out.printf("%-12s %-20s %-25s %-15s %-15s %-30s\n", 
                "ID", "Patient", "Doctor", "Time", "Status", "Symptoms");
            System.out.println("----------------------------------------------------------------------------------------------");
            
            boolean hasAppointments = false;
            while (rs.next()) {
                hasAppointments = true;
                System.out.printf("%-12d %-20s %-25s %-15s %-15s %-30s\n",
                    rs.getInt("appointment_id"),
                    rs.getString("patient_name"),
                    rs.getString("doctor_name"),
                    rs.getString("appointment_time"),
                    rs.getString("status"),
                    rs.getString("symptoms"));
            }
            
            if (!hasAppointments) {
                System.out.println("No appointments scheduled for today.");
            }
            
            rs.close();
            stmt.close();
        } catch (SQLException e) {
            System.out.println("❌ Error viewing today's appointments: " + e.getMessage());
        }
    }
    
    private static void updateAppointmentStatus() {
        System.out.print("\nEnter Appointment ID to update: ");
        int appointmentId = getIntInput();
        scanner.nextLine(); // consume newline
        
        try {
            // Check current status
            String checkSql = "SELECT status FROM appointments WHERE appointment_id = ?";
            PreparedStatement checkStmt = connection.prepareStatement(checkSql);
            checkStmt.setInt(1, appointmentId);
            ResultSet rs = checkStmt.executeQuery();
            
            if (!rs.next()) {
                System.out.println("❌ Appointment ID not found!");
                return;
            }
            
            String currentStatus = rs.getString("status");
            System.out.println("Current Status: " + currentStatus);
            
            System.out.print("Enter new status (Scheduled/Completed/Cancelled): ");
            String newStatus = scanner.nextLine();
            
            if (newStatus.equalsIgnoreCase("Completed")) {
                System.out.print("Enter diagnosis: ");
                String diagnosis = scanner.nextLine();
                
                System.out.print("Enter prescription: ");
                String prescription = scanner.nextLine();
                
                String updateSql = "UPDATE appointments SET status = ?, diagnosis = ?, prescription = ? " +
                                 "WHERE appointment_id = ?";
                PreparedStatement updateStmt = connection.prepareStatement(updateSql);
                updateStmt.setString(1, newStatus);
                updateStmt.setString(2, diagnosis);
                updateStmt.setString(3, prescription);
                updateStmt.setInt(4, appointmentId);
                
                int rows = updateStmt.executeUpdate();
                if (rows > 0) {
                    System.out.println("✅ Appointment status updated to Completed!");
                    
                    // Add to medical records
                    addToMedicalRecords(appointmentId);
                }
                
                updateStmt.close();
            } else {
                String updateSql = "UPDATE appointments SET status = ? WHERE appointment_id = ?";
                PreparedStatement updateStmt = connection.prepareStatement(updateSql);
                updateStmt.setString(1, newStatus);
                updateStmt.setInt(2, appointmentId);
                
                int rows = updateStmt.executeUpdate();
                if (rows > 0) {
                    System.out.println("✅ Appointment status updated successfully!");
                }
                
                updateStmt.close();
            }
            
            rs.close();
            checkStmt.close();
        } catch (SQLException e) {
            System.out.println("❌ Error updating appointment: " + e.getMessage());
        }
    }
    
    private static void addToMedicalRecords(int appointmentId) {
        try {
            // Get appointment details
            String sql = "SELECT a.patient_id, a.doctor_id, a.diagnosis, a.prescription " +
                        "FROM appointments a WHERE a.appointment_id = ?";
            PreparedStatement pstmt = connection.prepareStatement(sql);
            pstmt.setInt(1, appointmentId);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                String insertSql = "INSERT INTO medical_records (record_id, patient_id, doctor_id, " +
                                  "diagnosis, treatment, medications, visit_date) " +
                                  "VALUES (seq_record_id.NEXTVAL, ?, ?, ?, ?, ?, SYSDATE)";
                PreparedStatement insertStmt = connection.prepareStatement(insertSql);
                insertStmt.setInt(1, rs.getInt("patient_id"));
                insertStmt.setInt(2, rs.getInt("doctor_id"));
                insertStmt.setString(3, rs.getString("diagnosis"));
                insertStmt.setString(4, "Consultation");
                insertStmt.setString(5, rs.getString("prescription"));
                
                int rows = insertStmt.executeUpdate();
                if (rows > 0) {
                    System.out.println("✅ Medical record added successfully!");
                }
                
                insertStmt.close();
            }
            
            rs.close();
            pstmt.close();
        } catch (SQLException e) {
            System.out.println("❌ Error adding medical record: " + e.getMessage());
        }
    }
    
    private static void cancelAppointment() {
        System.out.print("\nEnter Appointment ID to cancel: ");
        int appointmentId = getIntInput();
        
        try {
            String sql = "UPDATE appointments SET status = 'Cancelled' WHERE appointment_id = ?";
            PreparedStatement pstmt = connection.prepareStatement(sql);
            pstmt.setInt(1, appointmentId);
            
            int rows = pstmt.executeUpdate();
            if (rows > 0) {
                System.out.println("✅ Appointment cancelled successfully!");
            } else {
                System.out.println("❌ Appointment ID not found!");
            }
            
            pstmt.close();
        } catch (SQLException e) {
            System.out.println("❌ Error cancelling appointment: " + e.getMessage());
        }
    }
    
    private static void billingManagement() {
        boolean back = false;
        
        while (!back) {
            System.out.println("\n=== BILLING MANAGEMENT ===");
            System.out.println("1. Generate Bill");
            System.out.println("2. View All Bills");
            System.out.println("3. Update Payment Status");
            System.out.println("4. View Pending Bills");
            System.out.println("5. Back to Main Menu");
            System.out.print("Enter your choice: ");
            
            int choice = getIntInput();
            
            switch (choice) {
                case 1:
                    generateBill();
                    break;
                case 2:
                    viewAllBills();
                    break;
                case 3:
                    updatePaymentStatus();
                    break;
                case 4:
                    viewPendingBills();
                    break;
                case 5:
                    back = true;
                    break;
                default:
                    System.out.println("❌ Invalid choice!");
            }
        }
    }
    
    private static void generateBill() {
        try {
            System.out.println("\n=== GENERATE BILL ===");
            
            // Show appointments that need billing
            String sql = "SELECT a.appointment_id, p.first_name || ' ' || p.last_name as patient_name, " +
                         "d.first_name || ' ' || d.last_name as doctor_name, d.consultation_fee " +
                         "FROM appointments a " +
                         "JOIN patients p ON a.patient_id = p.patient_id " +
                         "JOIN doctors d ON a.doctor_id = d.doctor_id " +
                         "WHERE a.status = 'Completed' AND NOT EXISTS (" +
                         "SELECT 1 FROM billing b WHERE b.appointment_id = a.appointment_id)";
            
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            
            System.out.println("\nAppointments pending billing:");
            System.out.printf("%-12s %-20s %-25s %-15s\n", "ID", "Patient", "Doctor", "Consultation Fee");
            System.out.println("--------------------------------------------------------------------");
            
            boolean hasAppointments = false;
            while (rs.next()) {
                hasAppointments = true;
                System.out.printf("%-12d %-20s %-25s $%-14.2f\n",
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
            int appointmentId = getIntInput();
            scanner.nextLine(); // consume newline
            
            // Get appointment details
            String checkSql = "SELECT a.patient_id, a.appointment_id, d.consultation_fee " +
                             "FROM appointments a " +
                             "JOIN doctors d ON a.doctor_id = d.doctor_id " +
                             "WHERE a.appointment_id = ? AND a.status = 'Completed'";
            
            PreparedStatement checkStmt = connection.prepareStatement(checkSql);
            checkStmt.setInt(1, appointmentId);
            ResultSet checkRs = checkStmt.executeQuery();
            
            if (checkRs.next()) {
                int patientId = checkRs.getInt("patient_id");
                double consultationFee = checkRs.getDouble("consultation_fee");
                
                System.out.print("Medication Charges: $");
                double medication = getDoubleInput();
                
                System.out.print("Test Charges: $");
                double test = getDoubleInput();
                
                System.out.print("Other Charges: $");
                double other = getDoubleInput();
                scanner.nextLine(); // consume newline
                
                System.out.print("Payment Status (Pending/Paid/Partial): ");
                String paymentStatus = scanner.nextLine();
                
                String insertSql = "INSERT INTO billing (bill_id, patient_id, appointment_id, " +
                                  "bill_date, consultation_fee, medication_charges, " +
                                  "test_charges, other_charges, payment_status) " +
                                  "VALUES (seq_bill_id.NEXTVAL, ?, ?, SYSDATE, ?, ?, ?, ?, ?)";
                
                PreparedStatement insertStmt = connection.prepareStatement(insertSql);
                insertStmt.setInt(1, patientId);
                insertStmt.setInt(2, appointmentId);
                insertStmt.setDouble(3, consultationFee);
                insertStmt.setDouble(4, medication);
                insertStmt.setDouble(5, test);
                insertStmt.setDouble(6, other);
                insertStmt.setString(7, paymentStatus);
                
                int rows = insertStmt.executeUpdate();
                if (rows > 0) {
                    System.out.println("✅ Bill generated successfully!");
                    
                    // Show total amount
                    String totalSql = "SELECT total_amount FROM billing WHERE appointment_id = ?";
                    PreparedStatement totalStmt = connection.prepareStatement(totalSql);
                    totalStmt.setInt(1, appointmentId);
                    ResultSet totalRs = totalStmt.executeQuery();
                    
                    if (totalRs.next()) {
                        System.out.println("Total Amount: $" + totalRs.getDouble("total_amount"));
                    }
                    
                    totalRs.close();
                    totalStmt.close();
                }
                
                insertStmt.close();
            } else {
                System.out.println("❌ Appointment not found or not completed!");
            }
            
            checkRs.close();
            checkStmt.close();
            
        } catch (SQLException e) {
            System.out.println("❌ Error generating bill: " + e.getMessage());
        }
    }
    
    private static void viewAllBills() {
        try {
            String sql = "SELECT b.bill_id, p.first_name || ' ' || p.last_name as patient_name, " +
                         "b.bill_date, b.consultation_fee, b.medication_charges, " +
                         "b.test_charges, b.other_charges, b.total_amount, b.payment_status " +
                         "FROM billing b " +
                         "JOIN patients p ON b.patient_id = p.patient_id " +
                         "ORDER BY b.bill_date DESC";
            
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            
            System.out.println("\n=== ALL BILLS ===");
            System.out.printf("%-10s %-20s %-15s %-10s %-10s %-10s %-10s %-12s %-15s\n", 
                "ID", "Patient", "Date", "Consult", "Med", "Test", "Other", "Total", "Status");
            System.out.println("--------------------------------------------------------------------------------------------------------");
            
            double grandTotal = 0;
            while (rs.next()) {
                System.out.printf("%-10d %-20s %-15s $%-9.2f $%-9.2f $%-9.2f $%-9.2f $%-11.2f %-15s\n",
                    rs.getInt("bill_id"),
                    rs.getString("patient_name"),
                    rs.getDate("bill_date"),
                    rs.getDouble("consultation_fee"),
                    rs.getDouble("medication_charges"),
                    rs.getDouble("test_charges"),
                    rs.getDouble("other_charges"),
                    rs.getDouble("total_amount"),
                    rs.getString("payment_status"));
                
                grandTotal += rs.getDouble("total_amount");
            }
            
            System.out.printf("\nGrand Total: $%.2f\n", grandTotal);
            
            rs.close();
            stmt.close();
        } catch (SQLException e) {
            System.out.println("❌ Error viewing bills: " + e.getMessage());
        }
    }
    
    private static void updatePaymentStatus() {
        System.out.print("\nEnter Bill ID to update: ");
        int billId = getIntInput();
        scanner.nextLine(); // consume newline
        
        try {
            // Check current status
            String checkSql = "SELECT payment_status, total_amount FROM billing WHERE bill_id = ?";
            PreparedStatement checkStmt = connection.prepareStatement(checkSql);
            checkStmt.setInt(1, billId);
            ResultSet rs = checkStmt.executeQuery();
            
            if (!rs.next()) {
                System.out.println("❌ Bill ID not found!");
                return;
            }
            
            System.out.println("Current Status: " + rs.getString("payment_status"));
            System.out.println("Total Amount: $" + rs.getDouble("total_amount"));
            
            System.out.print("Enter new payment status (Pending/Paid/Partial): ");
            String newStatus = scanner.nextLine();
            
            String updateSql = "UPDATE billing SET payment_status = ? WHERE bill_id = ?";
            PreparedStatement updateStmt = connection.prepareStatement(updateSql);
            updateStmt.setString(1, newStatus);
            updateStmt.setInt(2, billId);
            
            int rows = updateStmt.executeUpdate();
            if (rows > 0) {
                System.out.println("✅ Payment status updated successfully!");
            }
            
            rs.close();
            checkStmt.close();
            updateStmt.close();
        } catch (SQLException e) {
            System.out.println("❌ Error updating payment status: " + e.getMessage());
        }
    }
    
    private static void viewPendingBills() {
        try {
            String sql = "SELECT b.bill_id, p.first_name || ' ' || p.last_name as patient_name, " +
                         "b.bill_date, b.total_amount, b.payment_status " +
                         "FROM billing b " +
                         "JOIN patients p ON b.patient_id = p.patient_id " +
                         "WHERE b.payment_status IN ('Pending', 'Partial') " +
                         "ORDER BY b.bill_date";
            
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            
            System.out.println("\n=== PENDING BILLS ===");
            System.out.printf("%-10s %-20s %-15s %-15s %-15s\n", 
                "ID", "Patient", "Date", "Amount", "Status");
            System.out.println("------------------------------------------------------------");
            
            double totalPending = 0;
            boolean hasPending = false;
            while (rs.next()) {
                hasPending = true;
                System.out.printf("%-10d %-20s %-15s $%-14.2f %-15s\n",
                    rs.getInt("bill_id"),
                    rs.getString("patient_name"),
                    rs.getDate("bill_date"),
                    rs.getDouble("total_amount"),
                    rs.getString("payment_status"));
                
                totalPending += rs.getDouble("total_amount");
            }
            
            if (!hasPending) {
                System.out.println("No pending bills.");
            } else {
                System.out.printf("\nTotal Pending Amount: $%.2f\n", totalPending);
            }
            
            rs.close();
            stmt.close();
        } catch (SQLException e) {
            System.out.println("❌ Error viewing pending bills: " + e.getMessage());
        }
    }
    
    private static void medicalRecordsManagement() {
        boolean back = false;
        
        while (!back) {
            System.out.println("\n=== MEDICAL RECORDS ===");
            System.out.println("1. Add Medical Record");
            System.out.println("2. View Patient Medical Records");
            System.out.println("3. Update Medical Record");
            System.out.println("4. Back to Main Menu");
            System.out.print("Enter your choice: ");
            
            int choice = getIntInput();
            
            switch (choice) {
                case 1:
                    addMedicalRecord();
                    break;
                case 2:
                    viewPatientMedicalRecords();
                    break;
                case 3:
                    updateMedicalRecord();
                    break;
                case 4:
                    back = true;
                    break;
                default:
                    System.out.println("❌ Invalid choice!");
            }
        }
    }
    
    private static void addMedicalRecord() {
        try {
            System.out.println("\n=== ADD MEDICAL RECORD ===");
            
            System.out.print("Patient ID: ");
            int patientId = getIntInput();
            scanner.nextLine(); // consume newline
            
            System.out.print("Doctor ID: ");
            int doctorId = getIntInput();
            scanner.nextLine(); // consume newline
            
            System.out.print("Diagnosis: ");
            String diagnosis = scanner.nextLine();
            
            System.out.print("Treatment: ");
            String treatment = scanner.nextLine();
            
            System.out.print("Medications: ");
            String medications = scanner.nextLine();
            
            System.out.print("Test Results: ");
            String testResults = scanner.nextLine();
            
            System.out.print("Next Visit Date (YYYY-MM-DD, or press Enter for none): ");
            String nextVisit = scanner.nextLine();
            
            String sql = "INSERT INTO medical_records (record_id, patient_id, doctor_id, " +
                         "diagnosis, treatment, medications, test_results, next_visit_date) " +
                         "VALUES (seq_record_id.NEXTVAL, ?, ?, ?, ?, ?, ?, " +
                         (nextVisit.isEmpty() ? "NULL" : "TO_DATE(?, 'YYYY-MM-DD')") + ")";
            
            PreparedStatement pstmt;
            if (nextVisit.isEmpty()) {
                pstmt = connection.prepareStatement(sql);
                pstmt.setInt(1, patientId);
                pstmt.setInt(2, doctorId);
                pstmt.setString(3, diagnosis);
                pstmt.setString(4, treatment);
                pstmt.setString(5, medications);
                pstmt.setString(6, testResults);
            } else {
                pstmt = connection.prepareStatement(sql.replace("NULL", "TO_DATE(?, 'YYYY-MM-DD')"));
                pstmt.setInt(1, patientId);
                pstmt.setInt(2, doctorId);
                pstmt.setString(3, diagnosis);
                pstmt.setString(4, treatment);
                pstmt.setString(5, medications);
                pstmt.setString(6, testResults);
                pstmt.setString(7, nextVisit);
            }
            
            int rows = pstmt.executeUpdate();
            if (rows > 0) {
                System.out.println("✅ Medical record added successfully!");
            }
            
            pstmt.close();
        } catch (SQLException e) {
            System.out.println("❌ Error adding medical record: " + e.getMessage());
        }
    }
    
    private static void viewPatientMedicalRecords() {
        System.out.print("\nEnter Patient ID: ");
        int patientId = getIntInput();
        
        try {
            String sql = "SELECT mr.*, d.first_name || ' ' || d.last_name as doctor_name " +
                        "FROM medical_records mr " +
                        "LEFT JOIN doctors d ON mr.doctor_id = d.doctor_id " +
                        "WHERE mr.patient_id = ? ORDER BY mr.visit_date DESC";
            
            PreparedStatement pstmt = connection.prepareStatement(sql);
            pstmt.setInt(1, patientId);
            ResultSet rs = pstmt.executeQuery();
            
            System.out.println("\n=== MEDICAL RECORDS ===");
            boolean hasRecords = false;
            while (rs.next()) {
                hasRecords = true;
                System.out.println("\nRecord ID: " + rs.getInt("record_id"));
                System.out.println("Visit Date: " + rs.getDate("visit_date"));
                System.out.println("Doctor: " + rs.getString("doctor_name"));
                System.out.println("Diagnosis: " + rs.getString("diagnosis"));
                System.out.println("Treatment: " + rs.getString("treatment"));
                System.out.println("Medications: " + rs.getString("medications"));
                System.out.println("Test Results: " + rs.getString("test_results"));
                if (rs.getDate("next_visit_date") != null) {
                    System.out.println("Next Visit: " + rs.getDate("next_visit_date"));
                }
                System.out.println("----------------------------------------");
            }
            
            if (!hasRecords) {
                System.out.println("No medical records found for this patient.");
            }
            
            rs.close();
            pstmt.close();
        } catch (SQLException e) {
            System.out.println("❌ Error viewing medical records: " + e.getMessage());
        }
    }
    
    private static void updateMedicalRecord() {
        System.out.print("\nEnter Medical Record ID to update: ");
        int recordId = getIntInput();
        scanner.nextLine(); // consume newline
        
        try {
            // Check if record exists
            String checkSql = "SELECT * FROM medical_records WHERE record_id = ?";
            PreparedStatement checkStmt = connection.prepareStatement(checkSql);
            checkStmt.setInt(1, recordId);
            ResultSet rs = checkStmt.executeQuery();
            
            if (!rs.next()) {
                System.out.println("❌ Record ID not found!");
                return;
            }
            
            System.out.println("\nCurrent Information:");
            System.out.println("Diagnosis: " + rs.getString("diagnosis"));
            System.out.println("Treatment: " + rs.getString("treatment"));
            System.out.println("Medications: " + rs.getString("medications"));
            
            System.out.println("\n=== UPDATE MEDICAL RECORD ===");
            System.out.print("New Diagnosis (press Enter to keep current): ");
            String newDiagnosis = scanner.nextLine();
            if (newDiagnosis.isEmpty()) newDiagnosis = rs.getString("diagnosis");
            
            System.out.print("New Treatment (press Enter to keep current): ");
            String newTreatment = scanner.nextLine();
            if (newTreatment.isEmpty()) newTreatment = rs.getString("treatment");
            
            System.out.print("New Medications (press Enter to keep current): ");
            String newMedications = scanner.nextLine();
            if (newMedications.isEmpty()) newMedications = rs.getString("medications");
            
            System.out.print("New Test Results (press Enter to keep current): ");
            String newTestResults = scanner.nextLine();
            if (newTestResults.isEmpty()) newTestResults = rs.getString("test_results");
            
            String updateSql = "UPDATE medical_records SET diagnosis = ?, treatment = ?, " +
                              "medications = ?, test_results = ? WHERE record_id = ?";
            PreparedStatement updateStmt = connection.prepareStatement(updateSql);
            updateStmt.setString(1, newDiagnosis);
            updateStmt.setString(2, newTreatment);
            updateStmt.setString(3, newMedications);
            updateStmt.setString(4, newTestResults);
            updateStmt.setInt(5, recordId);
            
            int rows = updateStmt.executeUpdate();
            if (rows > 0) {
                System.out.println("✅ Medical record updated successfully!");
            }
            
            rs.close();
            checkStmt.close();
            updateStmt.close();
        } catch (SQLException e) {
            System.out.println("❌ Error updating medical record: " + e.getMessage());
        }
    }
    
    private static void reportsManagement() {
        boolean back = false;
        
        while (!back) {
            System.out.println("\n=== REPORTS ===");
            System.out.println("1. Revenue Report");
            System.out.println("2. Patient Statistics");
            System.out.println("3. Doctor Performance");
            System.out.println("4. Appointment Statistics");
            System.out.println("5. Back to Main Menu");
            System.out.print("Enter your choice: ");
            
            int choice = getIntInput();
            
            switch (choice) {
                case 1:
                    revenueReport();
                    break;
                case 2:
                    patientStatistics();
                    break;
                case 3:
                    doctorPerformance();
                    break;
                case 4:
                    appointmentStatistics();
                    break;
                case 5:
                    back = true;
                    break;
                default:
                    System.out.println("❌ Invalid choice!");
            }
        }
    }
    
    private static void revenueReport() {
        try {
            System.out.println("\n=== REVENUE REPORT ===");
            
            // Monthly revenue
            String sql = "SELECT " +
                         "TO_CHAR(bill_date, 'YYYY-MM') as month, " +
                         "COUNT(*) as total_bills, " +
                         "SUM(total_amount) as total_revenue, " +
                         "SUM(CASE WHEN payment_status = 'Paid' THEN total_amount ELSE 0 END) as paid_amount, " +
                         "SUM(CASE WHEN payment_status = 'Pending' THEN total_amount ELSE 0 END) as pending_amount " +
                         "FROM billing " +
                         "GROUP BY TO_CHAR(bill_date, 'YYYY-MM') " +
                         "ORDER BY month DESC";
            
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            
            System.out.printf("\n%-10s %-10s %-15s %-15s %-15s\n", 
                "Month", "Bills", "Total Revenue", "Paid", "Pending");
            System.out.println("--------------------------------------------------------------");
            
            double grandTotal = 0;
            double grandPaid = 0;
            double grandPending = 0;
            
            while (rs.next()) {
                System.out.printf("%-10s %-10d $%-14.2f $%-14.2f $%-14.2f\n",
                    rs.getString("month"),
                    rs.getInt("total_bills"),
                    rs.getDouble("total_revenue"),
                    rs.getDouble("paid_amount"),
                    rs.getDouble("pending_amount"));
                
                grandTotal += rs.getDouble("total_revenue");
                grandPaid += rs.getDouble("paid_amount");
                grandPending += rs.getDouble("pending_amount");
            }
            
            System.out.println("--------------------------------------------------------------");
            System.out.printf("%-10s %-10s $%-14.2f $%-14.2f $%-14.2f\n",
                "TOTAL", "",
                grandTotal,
                grandPaid,
                grandPending);
            
            rs.close();
            stmt.close();
        } catch (SQLException e) {
            System.out.println("❌ Error generating revenue report: " + e.getMessage());
        }
    }
    
    private static void patientStatistics() {
        try {
            System.out.println("\n=== PATIENT STATISTICS ===");
            
            // Total patients
            String totalSql = "SELECT COUNT(*) as total FROM patients";
            ResultSet totalRs = connection.createStatement().executeQuery(totalSql);
            totalRs.next();
            int totalPatients = totalRs.getInt("total");
            
            // New patients this month
            String newSql = "SELECT COUNT(*) as new_patients FROM patients " +
                           "WHERE EXTRACT(MONTH FROM registration_date) = EXTRACT(MONTH FROM SYSDATE) " +
                           "AND EXTRACT(YEAR FROM registration_date) = EXTRACT(YEAR FROM SYSDATE)";
            ResultSet newRs = connection.createStatement().executeQuery(newSql);
            newRs.next();
            int newPatients = newRs.getInt("new_patients");
            
            // Patients by gender
            String genderSql = "SELECT gender, COUNT(*) as count FROM patients GROUP BY gender";
            ResultSet genderRs = connection.createStatement().executeQuery(genderSql);
            
            // Patients by blood group
            String bloodSql = "SELECT blood_group, COUNT(*) as count FROM patients WHERE blood_group IS NOT NULL GROUP BY blood_group ORDER BY count DESC";
            ResultSet bloodRs = connection.createStatement().executeQuery(bloodSql);
            
            System.out.println("\nTotal Patients: " + totalPatients);
            System.out.println("New Patients This Month: " + newPatients);
            
            System.out.println("\nPatients by Gender:");
            System.out.printf("%-10s %-10s %-10s\n", "Gender", "Count", "Percentage");
            System.out.println("----------------------------");
            while (genderRs.next()) {
                double percentage = (genderRs.getDouble("count") / totalPatients) * 100;
                System.out.printf("%-10s %-10d %-9.1f%%\n",
                    genderRs.getString("gender"),
                    genderRs.getInt("count"),
                    percentage);
            }
            
            System.out.println("\nPatients by Blood Group:");
            System.out.printf("%-10s %-10s\n", "Blood Group", "Count");
            System.out.println("--------------------");
            while (bloodRs.next()) {
                System.out.printf("%-10s %-10d\n",
                    bloodRs.getString("blood_group"),
                    bloodRs.getInt("count"));
            }
            
            totalRs.close();
            newRs.close();
            genderRs.close();
            bloodRs.close();
        } catch (SQLException e) {
            System.out.println("❌ Error generating patient statistics: " + e.getMessage());
        }
    }
    
    private static void doctorPerformance() {
        try {
            System.out.println("\n=== DOCTOR PERFORMANCE ===");
            
            String sql = "SELECT " +
                         "d.doctor_id, " +
                         "d.first_name || ' ' || d.last_name as doctor_name, " +
                         "d.specialization, " +
                         "COUNT(a.appointment_id) as total_appointments, " +
                         "SUM(CASE WHEN a.status = 'Completed' THEN 1 ELSE 0 END) as completed_appointments, " +
                         "ROUND(AVG(d.consultation_fee), 2) as avg_fee " +
                         "FROM doctors d " +
                         "LEFT JOIN appointments a ON d.doctor_id = a.doctor_id " +
                         "GROUP BY d.doctor_id, d.first_name, d.last_name, d.specialization " +
                         "ORDER BY total_appointments DESC";
            
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            
            System.out.printf("\n%-10s %-25s %-20s %-15s %-20s %-10s\n", 
                "ID", "Doctor", "Specialization", "Total", "Completed", "Avg Fee");
            System.out.println("----------------------------------------------------------------------------------------");
            
            while (rs.next()) {
                int total = rs.getInt("total_appointments");
                int completed = rs.getInt("completed_appointments");
                double completionRate = total > 0 ? (completed * 100.0 / total) : 0;
                
                System.out.printf("%-10d %-25s %-20s %-15d %-20d $%-9.2f\n",
                    rs.getInt("doctor_id"),
                    rs.getString("doctor_name"),
                    rs.getString("specialization"),
                    total,
                    completed,
                    rs.getDouble("avg_fee"));
            }
            
            rs.close();
            stmt.close();
        } catch (SQLException e) {
            System.out.println("❌ Error generating doctor performance report: " + e.getMessage());
        }
    }
    
    private static void appointmentStatistics() {
        try {
            System.out.println("\n=== APPOINTMENT STATISTICS ===");
            
            // Today's appointments
            String todaySql = "SELECT COUNT(*) as today_appointments FROM appointments " +
                             "WHERE appointment_date = TRUNC(SYSDATE)";
            ResultSet todayRs = connection.createStatement().executeQuery(todaySql);
            todayRs.next();
            int todayAppointments = todayRs.getInt("today_appointments");
            
            // Weekly appointments
            String weekSql = "SELECT COUNT(*) as week_appointments FROM appointments " +
                            "WHERE appointment_date >= TRUNC(SYSDATE) - 7";
            ResultSet weekRs = connection.createStatement().executeQuery(weekSql);
            weekRs.next();
            int weekAppointments = weekRs.getInt("week_appointments");
            
            // Monthly appointments
            String monthSql = "SELECT COUNT(*) as month_appointments FROM appointments " +
                             "WHERE EXTRACT(MONTH FROM appointment_date) = EXTRACT(MONTH FROM SYSDATE) " +
                             "AND EXTRACT(YEAR FROM appointment_date) = EXTRACT(YEAR FROM SYSDATE)";
            ResultSet monthRs = connection.createStatement().executeQuery(monthSql);
            monthRs.next();
            int monthAppointments = monthRs.getInt("month_appointments");
            
            // Appointments by status
            String statusSql = "SELECT status, COUNT(*) as count FROM appointments GROUP BY status";
            ResultSet statusRs = connection.createStatement().executeQuery(statusSql);
            
            System.out.println("\nToday's Appointments: " + todayAppointments);
            System.out.println("This Week's Appointments: " + weekAppointments);
            System.out.println("This Month's Appointments: " + monthAppointments);
            
            System.out.println("\nAppointments by Status:");
            System.out.printf("%-15s %-10s\n", "Status", "Count");
            System.out.println("---------------------");
            while (statusRs.next()) {
                System.out.printf("%-15s %-10d\n",
                    statusRs.getString("status"),
                    statusRs.getInt("count"));
            }
            
            todayRs.close();
            weekRs.close();
            monthRs.close();
            statusRs.close();
        } catch (SQLException e) {
            System.out.println("❌ Error generating appointment statistics: " + e.getMessage());
        }
    }
    
    private static void userManagement() {
        if (!"Admin".equals(currentUserRole)) {
            System.out.println("❌ Access denied! Admin privileges required.");
            return;
        }
        
        boolean back = false;
        
        while (!back) {
            System.out.println("\n=== USER MANAGEMENT ===");
            System.out.println("1. Create New User");
            System.out.println("2. View All Users");
            System.out.println("3. Reset Password");
            System.out.println("4. Delete User");
            System.out.println("5. Back to Main Menu");
            System.out.print("Enter your choice: ");
            
            int choice = getIntInput();
            
            switch (choice) {
                case 1:
                    createUser();
                    break;
                case 2:
                    viewAllUsers();
                    break;
                case 3:
                    resetPassword();
                    break;
                case 4:
                    deleteUser();
                    break;
                case 5:
                    back = true;
                    break;
                default:
                    System.out.println("❌ Invalid choice!");
            }
        }
    }
    
    private static void createUser() {
        try {
            System.out.println("\n=== CREATE NEW USER ===");
            
            System.out.print("Username: ");
            String username = scanner.nextLine();
            
            System.out.print("Password: ");
            String password = scanner.nextLine();
            
            System.out.print("Role (Admin/Doctor/Receptionist): ");
            String role = scanner.nextLine();
            
            System.out.print("Associated ID (enter 0 if none): ");
            int associatedId = getIntInput();
            scanner.nextLine(); // consume newline
            
            String sql = "INSERT INTO users1 (user_id, username, password, role, associated_id, created_date) " +
                         "VALUES (seq_user_id.NEXTVAL, ?, ?, ?, ?, SYSDATE)";
            
            PreparedStatement pstmt = connection.prepareStatement(sql);
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
                System.out.println("✅ User created successfully!");
            }
            
            pstmt.close();
        } catch (SQLException e) {
            System.out.println("❌ Error creating user: " + e.getMessage());
        }
    }
    
    private static void viewAllUsers() {
        try {
            String sql = "SELECT * FROM users1 ORDER BY user_id";
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            
            System.out.println("\n=== ALL USERS ===");
            System.out.printf("%-10s %-20s %-20s %-20s %-15s\n", 
                "ID", "Username", "Role", "Associated ID", "Created Date");
            System.out.println("-----------------------------------------------------------------------------");
            
            while (rs.next()) {
                System.out.printf("%-10d %-20s %-20s %-20s %-15s\n",
                    rs.getInt("user_id"),
                    rs.getString("username"),
                    rs.getString("role"),
                    rs.getInt("associated_id") == 0 ? "None" : rs.getInt("associated_id"),
                    rs.getDate("created_date"));
            }
            
            rs.close();
            stmt.close();
        } catch (SQLException e) {
            System.out.println("❌ Error viewing users: " + e.getMessage());
        }
    }
    
    private static void resetPassword() {
        System.out.print("\nEnter Username to reset password: ");
        String username = scanner.nextLine();
        
        System.out.print("Enter new password: ");
        String newPassword = scanner.nextLine();
        
        try {
            String sql = "UPDATE users1 SET password = ? WHERE username = ?";
            PreparedStatement pstmt = connection.prepareStatement(sql);
            pstmt.setString(1, newPassword);
            pstmt.setString(2, username);
            
            int rows = pstmt.executeUpdate();
            if (rows > 0) {
                System.out.println("✅ Password reset successfully!");
            } else {
                System.out.println("❌ Username not found!");
            }
            
            pstmt.close();
        } catch (SQLException e) {
            System.out.println("❌ Error resetting password: " + e.getMessage());
        }
    }
    
    private static void deleteUser() {
        System.out.print("\nEnter Username to delete: ");
        String username = scanner.nextLine();
        
        // Prevent deleting current user
        if (username.equals(currentUser)) {
            System.out.println("❌ Cannot delete current logged-in user!");
            return;
        }
        
        System.out.print("Are you sure you want to delete user '" + username + "'? (yes/no): ");
        String confirm = scanner.nextLine();
        
        if (!confirm.equalsIgnoreCase("yes")) {
            System.out.println("Deletion cancelled.");
            return;
        }
        
        try {
            String sql = "DELETE FROM users1 WHERE username = ?";
            PreparedStatement pstmt = connection.prepareStatement(sql);
            pstmt.setString(1, username);
            
            int rows = pstmt.executeUpdate();
            if (rows > 0) {
                System.out.println("✅ User deleted successfully!");
            } else {
                System.out.println("❌ Username not found!");
            }
            
            pstmt.close();
        } catch (SQLException e) {
            System.out.println("❌ Error deleting user: " + e.getMessage());
        }
    }
    
    private static void viewDashboard() {
        try {
            System.out.println("\n=== HOSPITAL DASHBOARD ===");
            
            // Get counts
            String patientSql = "SELECT COUNT(*) as count FROM patients";
            String doctorSql = "SELECT COUNT(*) as count FROM doctors";
            String appointmentSql = "SELECT COUNT(*) as count FROM appointments WHERE appointment_date = TRUNC(SYSDATE)";
            String billingSql = "SELECT SUM(total_amount) as total FROM billing WHERE bill_date = TRUNC(SYSDATE)";
            
            ResultSet patientRs = connection.createStatement().executeQuery(patientSql);
            ResultSet doctorRs = connection.createStatement().executeQuery(doctorSql);
            ResultSet appointmentRs = connection.createStatement().executeQuery(appointmentSql);
            ResultSet billingRs = connection.createStatement().executeQuery(billingSql);
            
            patientRs.next();
            doctorRs.next();
            appointmentRs.next();
            billingRs.next();
            
            int patientCount = patientRs.getInt("count");
            int doctorCount = doctorRs.getInt("count");
            int appointmentCount = appointmentRs.getInt("count");
            double dailyRevenue = billingRs.getDouble("total");
            
            System.out.println("\n📊 Current Statistics:");
            System.out.println("─────────────────────────────");
            System.out.println("👥 Total Patients: " + patientCount);
            System.out.println("👨‍⚕️  Total Doctors: " + doctorCount);
            System.out.println("📅 Today's Appointments: " + appointmentCount);
            System.out.println("💰 Today's Revenue: $" + (dailyRevenue > 0 ? String.format("%.2f", dailyRevenue) : "0.00"));
            
            // Recent activities
            System.out.println("\n🔄 Recent Activities:");
            System.out.println("─────────────────────────────");
            
            // Recent appointments
            String recentAppointments = "SELECT * FROM (SELECT a.appointment_id, p.first_name || ' ' || p.last_name as patient, " +
                                       "a.status, a.appointment_date FROM appointments a " +
                                       "JOIN patients p ON a.patient_id = p.patient_id " +
                                       "ORDER BY a.appointment_date DESC) WHERE ROWNUM <= 5";
            ResultSet recentRs = connection.createStatement().executeQuery(recentAppointments);
            
            while (recentRs.next()) {
                System.out.println("Appointment #" + recentRs.getInt("appointment_id") + " - " + recentRs.getString("patient") + " (" + recentRs.getString("status") + ")");
            }
            
            // Upcoming appointments
            System.out.println("\n📅 Upcoming Appointments (Next 3 days):");
            System.out.println("─────────────────────────────");
            
            String upcomingSql = "SELECT a.appointment_id, p.first_name || ' ' || p.last_name as patient, " + "a.appointment_date, a.appointment_time " + "FROM appointments a " + "JOIN patients p ON a.patient_id = p.patient_id " + "WHERE a.appointment_date BETWEEN TRUNC(SYSDATE) AND TRUNC(SYSDATE) + 3 " + "AND a.status = 'Scheduled' " + "ORDER BY a.appointment_date, a.appointment_time";
            ResultSet upcomingRs = connection.createStatement().executeQuery(upcomingSql);
            
            boolean hasUpcoming = false;
            while (upcomingRs.next()) {
                hasUpcoming = true;
                System.out.println(upcomingRs.getDate("appointment_date") + " " +  upcomingRs.getString("appointment_time") + " - " + upcomingRs.getString("patient"));
            }
            
            if (!hasUpcoming) {
                System.out.println("No upcoming appointments in next 3 days.");
            }
            
            // Pending bills
            System.out.println("\n💸 Pending Bills:");
            System.out.println("─────────────────────────────");
            
            String pendingSql = "SELECT COUNT(*) as count, SUM(total_amount) as total " + "FROM billing WHERE payment_status IN ('Pending', 'Partial')";
            ResultSet pendingRs = connection.createStatement().executeQuery(pendingSql);
            pendingRs.next();
            
            System.out.println("Pending Bills: " + pendingRs.getInt("count"));
            System.out.println("Total Pending Amount: $" + (pendingRs.getDouble("total") > 0 ?  String.format("%.2f", pendingRs.getDouble("total")) : "0.00"));
            
            patientRs.close();
            doctorRs.close();
            appointmentRs.close();
            billingRs.close();
            recentRs.close();
            upcomingRs.close();
            pendingRs.close();
            
        } catch (SQLException e) {
            System.out.println("❌ Error loading dashboard: " + e.getMessage());
        }
    }
    
    private static int getIntInput() {
        while (true) {
            try {
                return scanner.nextInt();
            } catch (Exception e) {
                System.out.print("❌ Invalid input. Please enter a number: ");
                scanner.nextLine(); // Clear invalid input
            }
        }
    }
    
    private static double getDoubleInput() {
        while (true) {
            try {
                return scanner.nextDouble();
            } catch (Exception e) {
                System.out.print("❌ Invalid input. Please enter a number: ");
                scanner.nextLine(); // Clear invalid input
            }
        }
    }
    
    private static void closeResources() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
                System.out.println("\nDatabase connection closed.");
            }
            if (scanner != null) {
                scanner.close();
            }
        } catch (SQLException e) {
            System.out.println("Error closing resources: " + e.getMessage());
        }
    }
}