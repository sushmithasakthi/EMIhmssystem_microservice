Hospital Management System (HMS) Microservice:
A Spring Boot microservice-based Hospital Management System that provides REST APIs for managing patients, doctors, departments, and appointments.

 Features:
- **Patient Management**: Create, read, update, and delete patient records
- **Doctor Management**: Manage doctor profiles with specialization and department assignment
- **Department Management**: Organize hospital departments
- **Appointment Management**: Schedule and manage patient appointments with doctors
- **Invoice & Billing**: Comprehensive invoice management with line items, taxes, discounts, and payment tracking
- **EMI Calculation**: Calculate Equated Monthly Installments for medical bill payment plans
- **EMI Plan Management**: Create and manage EMI plans for patients
- **Payment Tracking**: Track individual EMI payments with principal and interest breakdown
- **Microservice Architecture**: Fully configured microservice with health checks, monitoring, and CORS support
- **Validation**: Input validation using Jakarta Bean Validation
- **Exception Handling**: Global exception handling with proper error responses
- **Database**: PostgreSQL database with JPA/Hibernate

## Technology Stack

- **Java 21**
- **Spring Boot 4.0.1**
- **Spring Data JPA**
- **PostgreSQL**
- **Jakarta Validation**
- **Maven**

## Prerequisites

- Java 21
- Maven 3.6+
- PostgreSQL 12+

## Setup Instructions

1. **Database Setup**
   ```sql
   CREATE DATABASE hms_db;
   ```

2. **Update Database Configuration**
   Edit `src/main/resources/application.properties` and update:
   - Database URL
   - Username
   - Password

3. **Build the Project**
   ```bash
   mvn clean install
   ```

4. **Run the Application**
   ```bash
   mvn spring-boot:run
   ```

The application will start on `http://localhost:8080`

**Health Check**: Access health endpoint at `http://localhost:8081/actuator/health`

**Note**: All API endpoints are available at `http://localhost:8080/api/*`

## API Endpoints

### Patient Endpoints

- `POST /api/patients` - Create a new patient
- `GET /api/patients` - Get all patients
- `GET /api/patients/{id}` - Get patient by ID
- `PUT /api/patients/{id}` - Update patient
- `DELETE /api/patients/{id}` - Delete patient

### Doctor Endpoints

- `POST /api/doctors` - Create a new doctor
- `GET /api/doctors` - Get all doctors
- `GET /api/doctors/{id}` - Get doctor by ID
- `GET /api/doctors/department/{departmentId}` - Get doctors by department
- `GET /api/doctors/specialization/{specialization}` - Get doctors by specialization
- `PUT /api/doctors/{id}` - Update doctor
- `DELETE /api/doctors/{id}` - Delete doctor

### Department Endpoints

- `POST /api/departments` - Create a new department
- `GET /api/departments` - Get all departments
- `GET /api/departments/{id}` - Get department by ID
- `PUT /api/departments/{id}` - Update department
- `DELETE /api/departments/{id}` - Delete department

### Appointment Endpoints

- `POST /api/appointments` - Create a new appointment
- `GET /api/appointments` - Get all appointments
- `GET /api/appointments/{id}` - Get appointment by ID
- `GET /api/appointments/patient/{patientId}` - Get appointments by patient
- `GET /api/appointments/doctor/{doctorId}` - Get appointments by doctor
- `PUT /api/appointments/{id}` - Update appointment
- `PATCH /api/appointments/{id}/status` - Update appointment status
- `DELETE /api/appointments/{id}` - Delete appointment

### EMI Calculation Endpoints

- `POST /api/emi/calculate` - Calculate EMI for given principal, interest rate, and tenure

### EMI Plan Endpoints

- `POST /api/emi/plans` - Create a new EMI plan for a patient
- `GET /api/emi/plans` - Get all EMI plans
- `GET /api/emi/plans/{id}` - Get EMI plan by ID
- `GET /api/emi/plans/patient/{patientId}` - Get EMI plans by patient
- `GET /api/emi/plans/status/{status}` - Get EMI plans by status (ACTIVE, COMPLETED, CANCELLED, OVERDUE)
- `PATCH /api/emi/plans/{id}/status` - Update EMI plan status
- `DELETE /api/emi/plans/{id}` - Delete EMI plan

### EMI Payment Endpoints

- `POST /api/emi/payments` - Record a payment for an EMI plan
- `GET /api/emi/payments` - Get all payments
- `GET /api/emi/payments/{id}` - Get payment by ID
- `GET /api/emi/payments/plan/{emiPlanId}` - Get payments by EMI plan
- `GET /api/emi/payments/status/{status}` - Get payments by status (PENDING, COMPLETED, FAILED, REFUNDED)
- `PATCH /api/emi/payments/{id}/status` - Update payment status
- `DELETE /api/emi/payments/{id}` - Delete payment

### Invoice Endpoints

- `POST /api/invoices` - Create a new invoice
- `GET /api/invoices` - Get all invoices
- `GET /api/invoices/{id}` - Get invoice by ID
- `GET /api/invoices/number/{invoiceNumber}` - Get invoice by invoice number
- `GET /api/invoices/patient/{patientId}` - Get invoices by patient
- `GET /api/invoices/patient/{patientId}/unpaid` - Get unpaid invoices by patient
- `GET /api/invoices/status/{status}` - Get invoices by status (DRAFT, ISSUED, SENT, PAID, PARTIALLY_PAID, OVERDUE, CANCELLED, REFUNDED)
- `PUT /api/invoices/{id}` - Update invoice
- `POST /api/invoices/{id}/payment` - Record payment for an invoice
- `PATCH /api/invoices/{id}/status` - Update invoice status
- `DELETE /api/invoices/{id}` - Delete invoice

## Example API Requests

### Create Patient
```json
POST /api/patients
{
  "name": "John Doe",
  "email": "john.doe@example.com",
  "phone": "1234567890",
  "address": "123 Main St",
  "gender": "Male",
  "dob": "1990-01-01",
  "bloodGroup": "O+"
}
```

### Create Department
```json
POST /api/departments
{
  "name": "Cardiology",
  "description": "Heart and cardiovascular care",
  "location": "Building A, Floor 2"
}
```

### Create Doctor
```json
POST /api/doctors
{
  "name": "Dr. Jane Smith",
  "email": "jane.smith@hospital.com",
  "phone": "9876543210",
  "specialization": "Cardiologist",
  "departmentId": "<department-uuid>",
  "qualification": "MD, Cardiology",
  "experience": 10
}
```

### Create Appointment
```json
POST /api/appointments
{
  "patientId": "<patient-uuid>",
  "doctorId": "<doctor-uuid>",
  "appointmentDate": "2024-12-20T10:00:00",
  "reason": "Regular checkup",
  "notes": "Follow-up appointment"
}
```

### Calculate EMI
```json
POST /api/emi/calculate
{
  "principalAmount": 100000.00,
  "interestRate": 12.0,
  "tenureMonths": 12
}
```

Response includes:
- EMI amount
- Total amount payable
- Total interest
- Monthly payment schedule with principal and interest breakdown

### Create EMI Plan
```json
POST /api/emi/plans
{
  "patientId": "<patient-uuid>",
  "principalAmount": 50000.00,
  "interestRate": 10.5,
  "tenureMonths": 6,
  "startDate": "2024-01-01",
  "description": "Medical treatment payment plan"
}
```

### Record EMI Payment
```json
POST /api/emi/payments
{
  "emiPlanId": "<emi-plan-uuid>",
  "paymentAmount": 8500.00,
  "paymentDate": "2024-01-01",
  "paymentMethod": "Credit Card",
  "transactionId": "TXN123456"
}
```

### Create Invoice
```json
POST /api/invoices
{
  "patientId": "<patient-uuid>",
  "appointmentId": "<appointment-uuid>",
  "invoiceDate": "2024-12-20",
  "dueDate": "2025-01-20",
  "taxRate": 18.0,
  "discountAmount": 500.00,
  "billingAddress": "123 Main St, City",
  "billingContact": "patient@example.com",
  "notes": "Medical consultation and tests",
  "items": [
    {
      "description": "Doctor Consultation",
      "quantity": 1,
      "unitPrice": 2000.00,
      "itemType": "CONSULTATION",
      "notes": "General checkup"
    },
    {
      "description": "Blood Test",
      "quantity": 1,
      "unitPrice": 1500.00,
      "itemType": "TEST",
      "notes": "Complete blood count"
    },
    {
      "description": "Medication",
      "quantity": 2,
      "unitPrice": 500.00,
      "itemType": "MEDICATION",
      "notes": "Prescribed medicines"
    }
  ]
}
```

### Record Invoice Payment
```
POST /api/invoices/{invoiceId}/payment?paymentAmount=5000.00
```

## Project Structure

```
hms_service/
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com/emi_hms_microservice/hms_service/
│   │   │       ├── config/         # Configuration classes
│   │   │       ├── controller/     # REST controllers
│   │   │       ├── dto/            # Data Transfer Objects
│   │   │       ├── exception/      # Exception handlers
│   │   │       ├── model/          # Entity models
│   │   │       ├── repository/     # JPA repositories
│   │   │       ├── service/        # Business logic
│   │   │       └── HmsServiceApplication.java
│   │   └── resources/
│   │       └── application.properties
│   └── test/
└── pom.xml
```

## Microservice Features

### Health Monitoring
- **Actuator Endpoints**: Available at `http://localhost:8081/actuator`
  - `/health` - Service health status
  - `/info` - Service information
  - `/metrics` - Application metrics
  - `/prometheus` - Prometheus metrics endpoint

### CORS Configuration
- CORS is enabled for all endpoints to support frontend integration
- Configurable in `MicroserviceConfig.java`

### Database Connection Pooling
- HikariCP connection pool configured
- Maximum pool size: 10
- Minimum idle connections: 5
- Connection timeout: 20 seconds

## Database Schema

The application uses JPA/Hibernate with `ddl-auto=update`, which automatically creates/updates tables based on entity definitions.

## Error Handling

The application includes global exception handling that returns structured error responses:

```json
{
  "timestamp": "2024-12-19T10:00:00",
  "status": 400,
  "message": "Error message"
}
```

## License

This project is part of the EMI HMS Microservice system.

