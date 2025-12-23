Attendance Manager
Attendance Manager is a simple Java-based application designed to manage student attendance records efficiently. It uses MySQL as the backend database and connects via the mysql-connector-java library.

📌 Features
Add, update, and delete student records.

Mark attendance for students.

View attendance reports.

MySQL database integration for persistent storage.

Lightweight and easy to run.

🛠️ Technologies Used
Java (Core application logic)

MySQL (Database)

MySQL Connector/J (mysql-connector-java-8.0.25.jar)

📂 Project Structure
Code
Attendance-Manager/
│
├── AttendanceApp.java          # Main application file
├── mysql-connector-java-8.0.25.jar  # JDBC driver
⚙️ Setup Instructions
Clone the repository

bash
git clone https://github.com/prem970/Attendance-Manager.git
cd Attendance-Manager
Install MySQL  
Ensure MySQL is installed and running on your system.

Create Database

sql
CREATE DATABASE attendance_db;
USE attendance_db;
Configure Tables  
Example schema:

sql
CREATE TABLE students (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    roll_no VARCHAR(50) UNIQUE NOT NULL
);

CREATE TABLE attendance (
    id INT AUTO_INCREMENT PRIMARY KEY,
    student_id INT,
    date DATE,
    status ENUM('Present','Absent') NOT NULL,
    FOREIGN KEY (student_id) REFERENCES students(id)
);
Compile and Run

bash
javac -cp .:mysql-connector-java-8.0.25.jar AttendanceApp.java
java -cp .:mysql-connector-java-8.0.25.jar AttendanceApp
🚀 Usage
Run the application to interact with the attendance system.

Add students, mark attendance, and generate reports.

Modify AttendanceApp.java to customize features as needed.

🤝 Contributing
Contributions are welcome!

Fork the repository

Create a new branch (feature-xyz)

Commit changes

Submit a pull request

📜 License
This project is licensed under the MIT License. You are free to use, modify, and distribute it.
