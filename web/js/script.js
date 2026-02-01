// script.js - Enhanced Hospital Management System

// DOM Elements
const preloader = document.querySelector('.preloader');
const header = document.querySelector('.header');
const hamburger = document.querySelector('.hamburger');
const navMenu = document.querySelector('.nav-menu');
const navLinks = document.querySelectorAll('.nav-link');
const backToTop = document.querySelector('.back-to-top');
const tabButtons = document.querySelectorAll('.tab-btn');
const tabContents = document.querySelectorAll('.tab-content');

// Preloader
window.addEventListener('load', () => {
    setTimeout(() => {
        preloader.classList.add('loaded');
        setTimeout(() => {
            preloader.style.display = 'none';
        }, 500);
    }, 1000);
});

// Header Scroll Effect
window.addEventListener('scroll', () => {
    if (window.scrollY > 100) {
        header.classList.add('scrolled');
        backToTop.classList.add('visible');
    } else {
        header.classList.remove('scrolled');
        backToTop.classList.remove('visible');
    }
    
    // Active nav link based on scroll position
    const sections = document.querySelectorAll('section');
    const scrollPos = window.scrollY + 100;
    
    sections.forEach(section => {
        const sectionTop = section.offsetTop;
        const sectionHeight = section.clientHeight;
        const sectionId = section.getAttribute('id');
        
        if (scrollPos >= sectionTop && scrollPos < sectionTop + sectionHeight) {
            navLinks.forEach(link => {
                link.classList.remove('active');
                if (link.getAttribute('href') === `#${sectionId}`) {
                    link.classList.add('active');
                }
            });
        }
    });
});

// Mobile Navigation
hamburger.addEventListener('click', () => {
    hamburger.classList.toggle('active');
    navMenu.classList.toggle('active');
    document.body.classList.toggle('no-scroll');
});

// Close mobile menu when clicking outside
document.addEventListener('click', (e) => {
    if (!hamburger.contains(e.target) && !navMenu.contains(e.target)) {
        hamburger.classList.remove('active');
        navMenu.classList.remove('active');
        document.body.classList.remove('no-scroll');
    }
});

// Smooth scrolling for anchor links
document.querySelectorAll('a[href^="#"]').forEach(anchor => {
    anchor.addEventListener('click', function (e) {
        e.preventDefault();
        
        const targetId = this.getAttribute('href');
        if (targetId === '#') return;
        
        const targetElement = document.querySelector(targetId);
        if (targetElement) {
            // Close mobile menu if open
            hamburger.classList.remove('active');
            navMenu.classList.remove('active');
            document.body.classList.remove('no-scroll');
            
            // Scroll to target
            window.scrollTo({
                top: targetElement.offsetTop - 80,
                behavior: 'smooth'
            });
        }
    });
});

// Back to Top Button
backToTop.addEventListener('click', () => {
    window.scrollTo({
        top: 0,
        behavior: 'smooth'
    });
});

// Tab Switching
tabButtons.forEach(button => {
    button.addEventListener('click', () => {
        // Remove active class from all buttons and contents
        tabButtons.forEach(btn => btn.classList.remove('active'));
        tabContents.forEach(content => content.classList.remove('active'));
        
        // Add active class to clicked button
        button.classList.add('active');
        
        // Show corresponding content
        const tabId = button.getAttribute('data-tab');
        const tabContent = document.getElementById(tabId);
        if (tabContent) {
            tabContent.classList.add('active');
        }
    });
});

// Form Validation for Login
const loginForm = document.getElementById('loginForm');
if (loginForm) {
    loginForm.addEventListener('submit', function(e) {
        e.preventDefault();
        
        const username = document.getElementById('username').value.trim();
        const password = document.getElementById('password').value.trim();
        const role = document.getElementById('role').value;
        
        // Basic validation
        if (!username || !password || !role) {
            showNotification('Please fill in all fields', 'error');
            return;
        }
        
        // For demo purposes - redirect based on role
        showNotification('Logging in...', 'success');
        
        setTimeout(() => {
            switch(role) {
                case 'Admin':
                    window.location.href = 'dashboard.html';
                    break;
                case 'Doctor':
                    window.location.href = 'doctor-dashboard.html';
                    break;
                case 'Patient':
                    window.location.href = 'patient-dashboard.html';
                    break;
                default:
                    window.location.href = 'dashboard.html';
            }
        }, 1500);
    });
}

// Form Validation for Registration
const registerForm = document.getElementById('registerForm');
if (registerForm) {
    registerForm.addEventListener('submit', function(e) {
        e.preventDefault();
        
        const email = document.getElementById('email').value.trim();
        const password = document.getElementById('password').value;
        const confirmPassword = document.getElementById('confirmPassword').value;
        const userType = document.getElementById('userType').value;
        
        // Validation
        if (!validateEmail(email)) {
            showNotification('Please enter a valid email address', 'error');
            return;
        }
        
        if (password.length < 6) {
            showNotification('Password must be at least 6 characters', 'error');
            return;
        }
        
        if (password !== confirmPassword) {
            showNotification('Passwords do not match', 'error');
            return;
        }
        
        // Simulate registration
        showNotification('Creating account...', 'success');
        
        setTimeout(() => {
            showNotification('Account created successfully! Redirecting...', 'success');
            setTimeout(() => {
                window.location.href = 'login.html';
            }, 1500);
        }, 1000);
    });
}

// Appointment Booking
const appointmentForm = document.getElementById('appointmentForm');
if (appointmentForm) {
    appointmentForm.addEventListener('submit', function(e) {
        e.preventDefault();
        
        const patientName = document.getElementById('patientName').value.trim();
        const doctor = document.getElementById('doctor').value;
        const date = document.getElementById('appointmentDate').value;
        const time = document.getElementById('appointmentTime').value;
        const symptoms = document.getElementById('symptoms').value.trim();
        
        if (!patientName || !doctor || !date || !time || !symptoms) {
            showNotification('Please fill in all fields', 'error');
            return;
        }
        
        // Simulate appointment booking
        showNotification('Booking appointment...', 'success');
        
        setTimeout(() => {
            showNotification('Appointment booked successfully!', 'success');
            appointmentForm.reset();
            
            // Show confirmation modal
            showAppointmentConfirmation({
                patientName,
                doctor,
                date,
                time,
                symptoms
            });
        }, 1500);
    });
}

// Notification System
function showNotification(message, type = 'info') {
    // Remove existing notifications
    const existingNotifications = document.querySelectorAll('.notification');
    existingNotifications.forEach(notification => {
        notification.remove();
    });
    
    // Create notification element
    const notification = document.createElement('div');
    notification.className = `notification notification-${type}`;
    notification.innerHTML = `
        <div class="notification-content">
            <i class="fas fa-${getNotificationIcon(type)}"></i>
            <span>${message}</span>
        </div>
        <button class="notification-close">
            <i class="fas fa-times"></i>
        </button>
    `;
    
    // Add to body
    document.body.appendChild(notification);
    
    // Show notification
    setTimeout(() => {
        notification.classList.add('show');
    }, 10);
    
    // Auto remove after 5 seconds
    const autoRemove = setTimeout(() => {
        notification.classList.remove('show');
        setTimeout(() => {
            notification.remove();
        }, 300);
    }, 5000);
    
    // Close button
    const closeBtn = notification.querySelector('.notification-close');
    closeBtn.addEventListener('click', () => {
        clearTimeout(autoRemove);
        notification.classList.remove('show');
        setTimeout(() => {
            notification.remove();
        }, 300);
    });
}

function getNotificationIcon(type) {
    switch(type) {
        case 'success': return 'check-circle';
        case 'error': return 'exclamation-circle';
        case 'warning': return 'exclamation-triangle';
        default: return 'info-circle';
    }
}

// Appointment Confirmation Modal
function showAppointmentConfirmation(details) {
    const modal = document.createElement('div');
    modal.className = 'modal';
    modal.innerHTML = `
        <div class="modal-content">
            <div class="modal-header">
                <h3>Appointment Confirmed!</h3>
                <button class="modal-close">
                    <i class="fas fa-times"></i>
                </button>
            </div>
            <div class="modal-body">
                <div class="confirmation-details">
                    <div class="detail-item">
                        <i class="fas fa-user"></i>
                        <div>
                            <h4>Patient Name</h4>
                            <p>${details.patientName}</p>
                        </div>
                    </div>
                    <div class="detail-item">
                        <i class="fas fa-user-md"></i>
                        <div>
                            <h4>Doctor</h4>
                            <p>${details.doctor}</p>
                        </div>
                    </div>
                    <div class="detail-item">
                        <i class="fas fa-calendar"></i>
                        <div>
                            <h4>Date & Time</h4>
                            <p>${formatDate(details.date)} at ${details.time}</p>
                        </div>
                    </div>
                    <div class="detail-item">
                        <i class="fas fa-stethoscope"></i>
                        <div>
                            <h4>Symptoms</h4>
                            <p>${details.symptoms}</p>
                        </div>
                    </div>
                </div>
                <div class="appointment-id">
                    <p><strong>Appointment ID:</strong> APP${Math.floor(Math.random() * 10000)}</p>
                </div>
            </div>
            <div class="modal-footer">
                <button class="btn btn-primary print-btn">
                    <i class="fas fa-print"></i> Print Confirmation
                </button>
                <button class="btn btn-outline close-btn">
                    <i class="fas fa-times"></i> Close
                </button>
            </div>
        </div>
    `;
    
    document.body.appendChild(modal);
    
    // Add styles for modal
    const style = document.createElement('style');
    style.textContent = `
        .modal {
            position: fixed;
            top: 0;
            left: 0;
            width: 100%;
            height: 100%;
            background: rgba(0,0,0,0.5);
            display: flex;
            align-items: center;
            justify-content: center;
            z-index: 2000;
            padding: 20px;
            animation: fadeIn 0.3s ease;
        }
        
        .modal-content {
            background: white;
            border-radius: 10px;
            max-width: 500px;
            width: 100%;
            max-height: 90vh;
            overflow-y: auto;
            animation: slideUp 0.3s ease;
        }
        
        .modal-header {
            padding: 1.5rem;
            border-bottom: 1px solid #e0e0e0;
            display: flex;
            justify-content: space-between;
            align-items: center;
        }
        
        .modal-header h3 {
            margin: 0;
            color: #27ae60;
        }
        
        .modal-close {
            background: none;
            border: none;
            font-size: 1.5rem;
            cursor: pointer;
            color: #666;
        }
        
        .modal-body {
            padding: 1.5rem;
        }
        
        .confirmation-details {
            margin-bottom: 1.5rem;
        }
        
        .detail-item {
            display: flex;
            gap: 1rem;
            margin-bottom: 1rem;
            padding: 1rem;
            background: #f8f9fa;
            border-radius: 8px;
        }
        
        .detail-item i {
            color: #3498db;
            font-size: 1.2rem;
            margin-top: 0.25rem;
        }
        
        .detail-item h4 {
            margin: 0 0 0.25rem 0;
            font-size: 0.9rem;
            color: #666;
        }
        
        .detail-item p {
            margin: 0;
            font-weight: 500;
        }
        
        .appointment-id {
            text-align: center;
            padding: 1rem;
            background: #fff3cd;
            border-radius: 8px;
            margin-bottom: 1.5rem;
        }
        
        .modal-footer {
            padding: 1.5rem;
            border-top: 1px solid #e0e0e0;
            display: flex;
            gap: 1rem;
            justify-content: flex-end;
        }
        
        @keyframes fadeIn {
            from { opacity: 0; }
            to { opacity: 1; }
        }
        
        @keyframes slideUp {
            from { transform: translateY(50px); opacity: 0; }
            to { transform: translateY(0); opacity: 1; }
        }
    `;
    document.head.appendChild(style);
    
    // Close modal
    const closeBtn = modal.querySelector('.modal-close');
    const closeBtn2 = modal.querySelector('.close-btn');
    
    function closeModal() {
        modal.style.opacity = '0';
        setTimeout(() => {
            modal.remove();
            style.remove();
        }, 300);
    }
    
    closeBtn.addEventListener('click', closeModal);
    closeBtn2.addEventListener('click', closeModal);
    
    // Print button
    const printBtn = modal.querySelector('.print-btn');
    printBtn.addEventListener('click', () => {
        window.print();
    });
    
    // Close on outside click
    modal.addEventListener('click', (e) => {
        if (e.target === modal) {
            closeModal();
        }
    });
}

// Utility Functions
function validateEmail(email) {
    const re = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
    return re.test(email);
}

function formatDate(dateString) {
    const date = new Date(dateString);
    return date.toLocaleDateString('en-US', {
        weekday: 'long',
        year: 'numeric',
        month: 'long',
        day: 'numeric'
    });
}

// Doctor Search
const doctorSearch = document.getElementById('doctorSearch');
if (doctorSearch) {
    doctorSearch.addEventListener('input', function() {
        const searchTerm = this.value.toLowerCase();
        const doctorCards = document.querySelectorAll('.doctor-card');
        
        doctorCards.forEach(card => {
            const doctorName = card.querySelector('h3').textContent.toLowerCase();
            const specialization = card.querySelector('.specialization').textContent.toLowerCase();
            
            if (doctorName.includes(searchTerm) || specialization.includes(searchTerm)) {
                card.style.display = 'block';
            } else {
                card.style.display = 'none';
            }
        });
    });
}

// Patient Registration Form Wizard
const registrationWizard = document.querySelector('.registration-wizard');
if (registrationWizard) {
    const steps = registrationWizard.querySelectorAll('.wizard-step');
    const nextBtns = registrationWizard.querySelectorAll('.next-step');
    const prevBtns = registrationWizard.querySelectorAll('.prev-step');
    const progress = registrationWizard.querySelector('.wizard-progress');
    
    let currentStep = 0;
    
    function updateWizard() {
        // Hide all steps
        steps.forEach(step => step.classList.remove('active'));
        
        // Show current step
        steps[currentStep].classList.add('active');
        
        // Update progress
        const progressPercent = ((currentStep + 1) / steps.length) * 100;
        progress.style.width = `${progressPercent}%`;
        
        // Update buttons
        const prevBtn = registrationWizard.querySelector('.prev-step');
        const nextBtn = registrationWizard.querySelector('.next-step');
        
        if (currentStep === 0) {
            prevBtn.style.visibility = 'hidden';
        } else {
            prevBtn.style.visibility = 'visible';
        }
        
        if (currentStep === steps.length - 1) {
            nextBtn.innerHTML = '<i class="fas fa-check"></i> Submit';
        } else {
            nextBtn.innerHTML = 'Next <i class="fas fa-arrow-right"></i>';
        }
    }
    
    nextBtns.forEach(btn => {
        btn.addEventListener('click', () => {
            if (currentStep < steps.length - 1) {
                currentStep++;
                updateWizard();
            } else {
                // Submit form
                registrationWizard.closest('form').submit();
            }
        });
    });
    
    prevBtns.forEach(btn => {
        btn.addEventListener('click', () => {
            if (currentStep > 0) {
                currentStep--;
                updateWizard();
            }
        });
    });
    
    // Initialize wizard
    updateWizard();
}

// Dashboard Statistics Animation
const statNumbers = document.querySelectorAll('.stat-number');
if (statNumbers.length > 0) {
    statNumbers.forEach(stat => {
        const target = parseInt(stat.textContent);
        const increment = target / 100;
        let current = 0;
        
        const timer = setInterval(() => {
            current += increment;
            if (current >= target) {
                current = target;
                clearInterval(timer);
            }
            stat.textContent = Math.floor(current);
        }, 20);
    });
}

// Emergency Contact Quick Dial
const emergencyBtn = document.querySelector('.emergency-btn');
if (emergencyBtn) {
    emergencyBtn.addEventListener('click', () => {
        if (confirm('Call Emergency Services?')) {
            window.location.href = 'tel:+18001234567';
        }
    });
}

// Initialize tooltips
const tooltips = document.querySelectorAll('[data-tooltip]');
tooltips.forEach(tooltip => {
    tooltip.addEventListener('mouseenter', function() {
        const tooltipText = this.getAttribute('data-tooltip');
        const tooltipEl = document.createElement('div');
        tooltipEl.className = 'tooltip';
        tooltipEl.textContent = tooltipText;
        document.body.appendChild(tooltipEl);
        
        const rect = this.getBoundingClientRect();
        tooltipEl.style.left = `${rect.left + rect.width / 2}px`;
        tooltipEl.style.top = `${rect.top - tooltipEl.offsetHeight - 10}px`;
        tooltipEl.style.transform = 'translateX(-50%)';
        
        this.tooltipElement = tooltipEl;
    });
    
    tooltip.addEventListener('mouseleave', function() {
        if (this.tooltipElement) {
            this.tooltipElement.remove();
        }
    });
});

// Add CSS for notifications
const notificationStyles = document.createElement('style');
notificationStyles.textContent = `
    .notification {
        position: fixed;
        top: 20px;
        right: 20px;
        background: white;
        border-radius: 8px;
        box-shadow: 0 5px 15px rgba(0,0,0,0.1);
        padding: 1rem;
        display: flex;
        align-items: center;
        justify-content: space-between;
        min-width: 300px;
        max-width: 400px;
        transform: translateX(400px);
        transition: transform 0.3s ease;
        z-index: 10000;
        border-left: 4px solid #3498db;
    }
    
    .notification.show {
        transform: translateX(0);
    }
    
    .notification-success {
        border-left-color: #27ae60;
    }
    
    .notification-error {
        border-left-color: #e74c3c;
    }
    
    .notification-warning {
        border-left-color: #f39c12;
    }
    
    .notification-content {
        display: flex;
        align-items: center;
        gap: 1rem;
        flex: 1;
    }
    
    .notification-content i {
        font-size: 1.2rem;
    }
    
    .notification-success .notification-content i {
        color: #27ae60;
    }
    
    .notification-error .notification-content i {
        color: #e74c3c;
    }
    
    .notification-warning .notification-content i {
        color: #f39c12;
    }
    
    .notification-close {
        background: none;
        border: none;
        color: #666;
        cursor: pointer;
        padding: 0.25rem;
        font-size: 1rem;
    }
    
    .tooltip {
        position: absolute;
        background: #333;
        color: white;
        padding: 0.5rem 1rem;
        border-radius: 4px;
        font-size: 0.9rem;
        white-space: nowrap;
        z-index: 1000;
        pointer-events: none;
    }
    
    .tooltip:after {
        content: '';
        position: absolute;
        top: 100%;
        left: 50%;
        margin-left: -5px;
        border-width: 5px;
        border-style: solid;
        border-color: #333 transparent transparent transparent;
    }
    
    .no-scroll {
        overflow: hidden;
    }
`;

document.head.appendChild(notificationStyles);

// Demo data initialization for dashboard
if (window.location.pathname.includes('dashboard')) {
    setTimeout(() => {
        showNotification('Welcome to your dashboard!', 'success');
        
        // Update dashboard stats
        const appointmentsToday = document.getElementById('appointmentsToday');
        const patientsRegistered = document.getElementById('patientsRegistered');
        const revenueToday = document.getElementById('revenueToday');
        
        if (appointmentsToday) appointmentsToday.textContent = '28';
        if (patientsRegistered) patientsRegistered.textContent = '15';
        if (revenueToday) revenueToday.textContent = '$4,850';
    }, 1000);
}