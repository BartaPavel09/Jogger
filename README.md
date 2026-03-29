# Jogger

Jogger is a comprehensive full-stack application designed to help users track their running activities, monitor their fitness progress through interactive charts, and earn badges for their achievements.

## Architecture

The project is divided into two main components:
* **Backend (`jogger_backend`)**: A RESTful API built with Java Spring Boot, secured via JWT authentication, utilizing MySQL for data persistence.
* **Frontend (`jogger_frontend`)**: A cross-platform application built with Flutter, providing a responsive user interface for runner logging, data visualization, and profile management.

## Key Features

* **User Authentication**: Secure registration and login system utilizing JSON Web Tokens (JWT).
* **Activity Tracking**: Log running activities, including distance, duration, and pace.
* **Dashboard & Analytics**: Monitor fitness progress through detailed weekly charts and calorie burn visualizations.
* **Gamification System**: Automatically award badges to users based on their running milestones and completed activities.
* **Role-Based Access**: Includes dedicated admin screen.

## Tech Stack

**Backend:**
* Java
* Spring Boot
* Spring Security (JWT)
* Spring Data JPA
* MySQL
* Maven

**Frontend:**
* Flutter
* Dart
* REST API Integration

## Prerequisites

Ensure you have the following installed on your local machine before starting:
* Git
* Java Development Kit (JDK) 17
* Flutter SDK
* MySQL Server

## Installation and Setup

### 1. Clone the Repository

```bash
git clone https://github.com/BartaPavel09/Jogger.git
cd Jogger
```

### 2. Database Configuration

The project includes a `db.sql` file in the root directory to quickly recreate the database.

Log into your local MySQL server:
```bash
mysql -u root -p
```

Create the database and import the schema:
```sql
CREATE DATABASE jogger_db;
USE jogger_db;
source db.sql;
```

### 3. Backend Setup

Navigate to the backend directory:
```bash
cd jogger_backend
```

Configure the database credentials. Open `src/main/resources/application.yml` (or `application.properties`) and update the database connection settings to match your local MySQL setup:
```properties
spring.datasource.url=jdbc:mysql://localhost:3306/jogger_db
spring.datasource.username=root
spring.datasource.password=your_password
```

Run the Spring Boot application using the Maven wrapper

### 4. Frontend Setup

Open a new terminal window and navigate to the frontend directory:
```bash
cd jogger_frontend
```

Install the required Flutter dependencies:
```bash
flutter pub get
```

Run the application on your preferred connected device or emulator:
```bash
flutter run
```

## Project Structure

* `/jogger_backend`: Contains the backend logic, including REST Controllers, Services, Repositories, Entity Mappers, and JWT Security configurations.
* `/jogger_frontend`: Contains the frontend application with the UI Screens (Home, Login, Register, Profile, Admin), custom Widgets (Charts), Models, and API connection services.
* `db.sql`: The primary database architecture dump file.

## License

This project is maintained by Pavel Barta.
