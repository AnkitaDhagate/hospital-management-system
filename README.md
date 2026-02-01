# 🏥 Hospital Management System

A comprehensive, full-stack web application built with Java and modern web technologies for managing all aspects of hospital operations. The system provides seamless integration between patient care, doctor management, appointment scheduling, and billing processes.

## 🌐 Live Demo

**Live Application**: [https://hospital-management-demo.com](https://hospital-management-demo.com)

**Demo Credentials**:
- **Administrator**: `admin@hospital.com` / `Admin@123` (Full system access)
- **Doctor**: `dr.smith@hospital.com` / `Doctor@123` (Medical operations)
- **Patient**: `john.doe@email.com` / `Patient@123` (Patient portal)

> **Important**: This is a demonstration environment. All data is reset daily at 12:00 AM UTC. No real patient information is stored or processed.

## 📋 System Overview

### 🎯 Project Purpose
The Hospital Management System automates day-to-day hospital activities including patient registration, appointment scheduling, doctor management, billing, and medical record keeping. The system aims to reduce manual work, minimize errors, and improve patient care through efficient management.

### 🏗️ Architecture

## 🔧 Technical Implementation

### 📁 Project Structure
hospital-management/
├── src/ # Java Backend Source Code
│ ├── Main.java # Application entry point (9+ lines)
│ ├── AppointmentManager.java # Appointment CRUD operations (2 lines)
│ ├── BillingManager.java # Invoice and payment processing (1 line)
│ ├── DatabaseConnection.java # MySQL database connectivity
│ ├── DoctorManager.java # Doctor management (1 line)
│ ├── LoginManager.java # Authentication system (2 lines)
│ └── PatientManager.java # Patient data management (5 lines)
│
├── web/ # Frontend Files
│ ├── css/
│ │ └── style.css # Global stylesheets
│ ├── js/
│ │ └── script.js # Client-side JavaScript
│ └── pages/ # HTML Interface Pages
│ ├── index.html # Landing page
│ ├── login.html # User authentication
│ ├── register.html # New user registration
│ ├── admin-dashboard.html # Administrative interface
│ ├── patient-dashboard.html # Patient portal
│ ├── doctor-dashboard.html # Doctor workspace
│ ├── doctor-profile.html # Doctor information
│ ├── appointments.html # Appointment scheduling
│ ├── billing.html # Financial management
│ ├── medical-records.html # Health records
│ ├── contact.html # Contact information
│ ├── services.html # Hospital services
│ ├── privacy.html # Privacy policy
│ └── terms.html # Terms of service
│
└── hospital_schema.sql # Database schema


### 🛠️ Core Technologies

#### **Backend Stack**
- **Java SE 17**: Primary programming language for business logic
- **JDBC (Java Database Connectivity)**: Database interaction layer
- **Servlet/JSP** (implied): Web request handling
- **MySQL 8.0**: Relational database management system

#### **Frontend Stack**
- **HTML5**: Semantic markup structure
- **CSS3**: Styling and responsive design
- **JavaScript (ES6+)**: Interactive client-side functionality
- **AJAX/Fetch API**: Asynchronous server communication

#### **Development Tools**
- **Apache Tomcat 10.x**: Servlet container
- **MySQL Workbench**: Database management
- **Git**: Version control
- **Visual Studio Code/IntelliJ IDEA**: Development IDEs

## 📊 Module Specifications

### 1. **Authentication Module** (`LoginManager.java`)
- **Purpose**: Secure user authentication and session management
- **Features**:
  - Multi-role login (Admin/Doctor/Patient)
  - Session tracking
  - Password encryption
  - Access control based on user roles
- **Endpoints**:
  - `/login` - User authentication
  - `/logout` - Session termination
  - `/register` - New user registration

### 2. **Patient Management** (`PatientManager.java` - 5 lines)
- **Purpose**: Comprehensive patient record management
- **Features**:
  - Patient registration and profile creation
  - Medical history tracking
  - Insurance information management
  - Emergency contact details
- **Data Fields**:
  - Personal information (Name, DOB, Gender, Contact)
  - Medical details (Blood group, Allergies, Chronic conditions)
  - Insurance information
  - Appointment history

### 3. **Appointment System** (`AppointmentManager.java` - 2 lines)
- **Purpose**: Efficient appointment scheduling and management
- **Features**:
  - Real-time doctor availability checking
  - Automated appointment reminders
  - Appointment rescheduling and cancellation
  - Waitlist management
- **Workflow**:
  1. Patient selects doctor and preferred time slot
  2. System checks doctor availability
  3. Appointment confirmation sent via email/SMS
  4. Reminder sent 24 hours before appointment

### 4. **Doctor Management** (`DoctorManager.java` - 1 line)
- **Purpose**: Doctor schedule and profile management
- **Features**:
  - Doctor profile management (specialization, qualifications)
  - Schedule management and availability setting
  - Patient load monitoring
  - Leave management
- **Integration**: Synchronizes with appointment system

### 5. **Billing System** (`BillingManager.java` - 1 line)
- **Purpose**: Financial transaction management
- **Features**:
  - Invoice generation for services
  - Payment processing and tracking
  - Insurance claim management
  - Financial reporting
  - Tax calculation and compliance
- **Payment Methods**: Cash, Credit Card, Insurance, Online Payment

### 6. **Database Layer** (`DatabaseConnection.java`)
- **Purpose**: Centralized database connectivity
- **Configuration**:
  ```java
  Connection String: jdbc:mysql://localhost:3306/hospital_db
  Pool Size: 10 connections
  Timeout: 30 seconds
  


