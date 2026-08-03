# MediFlow 🏥

MediFlow is a backend clinic management system built using **Spring Boot**. The project aims to automate common clinic workflows such as patient registration, appointment management, and notifications.

A major goal of MediFlow is to demonstrate **asynchronous processing using RabbitMQ**, allowing time-consuming tasks such as sending notifications to run in the background without blocking the main request.

## Problem

In a traditional synchronous system, when an appointment is booked the application may need to:

```text
Book Appointment
      ↓
Save Appointment
      ↓
Send Email
      ↓
Send Notification
      ↓
Return Response
```

If the notification service is slow, the user has to wait for the entire process.

MediFlow will use RabbitMQ to process these tasks asynchronously:

```text
Book Appointment
      ↓
Save to PostgreSQL
      ↓
Publish Event → RabbitMQ
      ↓
Return Response Immediately

Meanwhile:

RabbitMQ → Consumer → Send Notification
```

This keeps the main API responsive while background tasks are processed independently.

## Tech Stack

* Java
* Spring Boot
* Spring Data JPA
* PostgreSQL
* RabbitMQ *(planned)*
* Docker *(planned)*
* JWT Authentication *(planned)*
* Maven
* Git & GitHub

## Architecture

MediFlow follows a layered backend architecture:

```text
Client
  ↓
Controller
  ↓
Service
  ↓
Repository
  ↓
PostgreSQL
```

The application uses DTOs to separate the external API contract from database entities.

## Current Features

### Patient Management

Currently implemented:

* Register a patient
* Get all patients
* Get patient by ID
* Patient request/response DTOs
* Input validation
* Duplicate email detection
* Duplicate phone number detection
* Custom exception handling
* Global validation error handling
* Proper HTTP status codes

## Patient APIs

### Create Patient

```http
POST /api/patients
```

Example request:

```json
{
  "firstName": "Rahul",
  "lastName": "Sharma",
  "age": 25,
  "gender": "Male",
  "phone": "9876543210",
  "email": "rahul@example.com",
  "address": "Pune"
}
```

### Get All Patients

```http
GET /api/patients
```

### Get Patient By ID

```http
GET /api/patients/{id}
```

Example:

```http
GET /api/patients/1
```

## Error Handling

MediFlow uses centralized exception handling using Spring Boot's `@RestControllerAdvice`.

For example, invalid input can return:

```json
{
  "status": 400,
  "message": "Validation Failed",
  "errors": {
    "phone": "Phone number must be 10 digits"
  }
}
```

Trying to register an existing email can return:

```json
{
  "status": 409,
  "message": "Email already exists."
}
```

Requesting a patient that does not exist can return:

```json
{
  "status": 404,
  "message": "Patient not found with id: 100"
}
```

## Planned Features

* Update patient
* Delete patient
* Doctor management
* Appointment scheduling
* RabbitMQ asynchronous event processing
* Email/notification service
* Docker containerization
* Authentication and authorization using JWT
* API documentation
* Automated testing

## Planned RabbitMQ Flow

One of the main goals of MediFlow is to demonstrate event-driven asynchronous processing.

```text
Appointment Created
        ↓
Appointment Service
        ↓
Publish AppointmentBooked Event
        ↓
RabbitMQ Exchange
        ↓
Notification Queue
        ↓
Notification Consumer
        ↓
Send Confirmation
```

The appointment API will not need to wait for the notification to be sent before returning a successful response.

## Project Status

🚧 **Under active development**

The Patient module is currently being developed. RabbitMQ, appointment management, Docker support, and authentication will be added in upcoming stages.
