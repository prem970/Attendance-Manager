import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.sql.*;
import java.time.LocalDate;
import java.util.*;
abstract class Person {
    protected String id;
    protected String name;
    protected String role;
    public Person(String id, String name, String role) {
        this.id = id;
        this.name = name;
        this.role = role;
    }
    public abstract void displayInfo();
}
class Student extends Person {
    public Student(String id, String name) {
        super(id, name, "Student");
    }
    @Override
    public void displayInfo() {
        System.out.println("Student ID: " + id + ", Name: " + name);
    }
}
class Teacher extends Person {
    public Teacher(String id, String name) {
        super(id, name, "Teacher");
    }
    @Override
    public void displayInfo() {
        System.out.println("Teacher ID: " + id + ", Name: " + name);
    }
}
public class AttendanceApp extends JFrame {
    private AttendanceRecord record;
    private JTextField userIdField, nameField;
    private JTextArea displayArea;
    private JComboBox<String> roleBox;
    private static final String DB_URL = "jdbc:mysql://localhost:3306/testdb";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "";
    public AttendanceApp() {
        record = new AttendanceRecord();
        setTitle("Attendance Management System");
        setSize(700, 550);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());
        getContentPane().setBackground(Color.decode("#F0F4F8"));
        JPanel inputPanel = new JPanel(new GridLayout(4, 2, 12, 12));
        inputPanel.setBackground(Color.WHITE);
        inputPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.decode("#4A90E2")), "User Info", 0, 0,
                new Font("Segoe UI", Font.BOLD, 16), Color.decode("#4A90E2")));
        JLabel lbl1 = new JLabel("User ID:");
        JLabel lbl2 = new JLabel("Name:");
        JLabel lbl3 = new JLabel("Role:");
        lbl1.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        lbl2.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        lbl3.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        userIdField = new JTextField();
        userIdField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        nameField = new JTextField();
        nameField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        roleBox = new JComboBox<>(new String[]{"Teacher", "Student"});
        roleBox.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        JButton registerBtn = new JButton("Register");
        JButton markBtn = new JButton("Mark Attendance");
        stylizeButton(registerBtn);
        stylizeButton(markBtn);
        inputPanel.add(lbl1); inputPanel.add(userIdField);
        inputPanel.add(lbl2); inputPanel.add(nameField);
        inputPanel.add(lbl3); inputPanel.add(roleBox);
        inputPanel.add(registerBtn); inputPanel.add(markBtn);
        add(inputPanel, BorderLayout.NORTH);
        displayArea = new JTextArea();
        displayArea.setEditable(false);
        displayArea.setBackground(Color.decode("#FAFAFA"));
        displayArea.setForeground(Color.decode("#333333"));
        displayArea.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        displayArea.setMargin(new Insets(10, 10, 10, 10));
        JScrollPane scroll = new JScrollPane(displayArea);
        scroll.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.decode("#4A90E2")),
                BorderFactory.createEmptyBorder(5, 5, 5, 5)));
        add(scroll, BorderLayout.CENTER);
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        bottomPanel.setBackground(Color.WHITE);
        JButton saveBtn = new JButton("Save to File");
        JButton loadBtn = new JButton("Load from File");
        JButton dbSaveBtn = new JButton("Save to Database");
        JButton viewBtn = new JButton("View Attendance");
        stylizeButton(saveBtn);
        stylizeButton(loadBtn);
        stylizeButton(dbSaveBtn);
        stylizeButton(viewBtn);
        bottomPanel.add(saveBtn);
        bottomPanel.add(loadBtn);
        bottomPanel.add(dbSaveBtn);
        bottomPanel.add(viewBtn);
        add(bottomPanel, BorderLayout.SOUTH);
        registerBtn.addActionListener(e -> registerUser());
        markBtn.addActionListener(e -> markAttendance());
        saveBtn.addActionListener(e -> saveToFile());
        loadBtn.addActionListener(e -> loadFromFile());
        dbSaveBtn.addActionListener(e -> saveToDatabase());
        viewBtn.addActionListener(e -> viewAttendance());
        setVisible(true);
    }
    private void stylizeButton(JButton b) {
        b.setBackground(Color.decode("#4A90E2")); // Modern blue
        b.setForeground(Color.WHITE);
        b.setFocusPainted(false);
        b.setFont(new Font("Segoe UI", Font.BOLD, 14));
        b.setBorder(BorderFactory.createEmptyBorder(8, 15, 8, 15));
        b.setCursor(new Cursor(Cursor.HAND_CURSOR));
        b.setOpaque(true);
        b.setContentAreaFilled(true);
        b.setBorderPainted(false);
        b.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                b.setBackground(Color.decode("#357ABD")); // Darker blue on hover
            }
            @Override
            public void mouseExited(MouseEvent e) {
                b.setBackground(Color.decode("#4A90E2")); // Original blue
            }
        });
    }
    private void registerUser() {
        String id = userIdField.getText().trim();
        String name = nameField.getText().trim();
        String role = (String) roleBox.getSelectedItem();
        if (id.isEmpty() || name.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill in all fields.");
            return;
        }
        try {
            Person p = role.equals("Teacher") ? new Teacher(id, name) : new Student(id, name);
            record.registerUser(p);
            displayArea.append("Registered: " + p.name + " (" + p.role + ")\n");
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
        }
    }
    private void markAttendance() {
        String id = userIdField.getText().trim();
        if (id.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Enter User ID to mark attendance.");
            return;
        }
        try {
            record.markAttendance(id);
            displayArea.append("Attendance marked for ID: " + id + " on " + LocalDate.now() + "\n");
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
        }
    }
    private void viewAttendance() {
        displayArea.append("\n--- Attendance Records ---\n");
        for (var entry : record.getAllRecords().entrySet()) {
            displayArea.append(entry.getKey() + " → " + entry.getValue() + "\n");
        }
    }
    private void saveToFile() {
        try {
            record.saveToFile("attendance.csv");
            JOptionPane.showMessageDialog(this, "Records saved successfully!");
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "File Error: " + e.getMessage());
        }
    }
    private void loadFromFile() {
        try {
            record.loadFromFile("attendance.csv");
            JOptionPane.showMessageDialog(this, "Records loaded successfully!");
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "File Error: " + e.getMessage());
        }
    }
    private void saveToDatabase() {
        try {
            Connection con = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
            Statement st = con.createStatement();

            // Create users table
            st.execute("CREATE TABLE IF NOT EXISTS users (User_ID VARCHAR(50) PRIMARY KEY, Name VARCHAR(100), Role VARCHAR(20))");

            // Create attendance table
            st.execute("CREATE TABLE IF NOT EXISTS attendance (id INT AUTO_INCREMENT PRIMARY KEY, User_ID VARCHAR(50), attendance_date DATE, FOREIGN KEY(User_ID) REFERENCES users(User_ID))");

            // Insert users
            for (var userEntry : record.users.entrySet()) {
                Person p = userEntry.getValue();
                PreparedStatement ps = con.prepareStatement("INSERT IGNORE INTO users VALUES (?, ?, ?)");
                ps.setString(1, p.id);
                ps.setString(2, p.name);
                ps.setString(3, p.role);
                ps.executeUpdate();
                ps.close();
            }

            // Insert attendance records
            for (var entry : record.getAllRecords().entrySet()) {
                for (LocalDate d : entry.getValue()) {
                    PreparedStatement ps = con.prepareStatement("INSERT INTO attendance (User_ID, attendance_date) VALUES (?, ?)");
                    ps.setString(1, entry.getKey());
                    ps.setDate(2, java.sql.Date.valueOf(d));
                    ps.executeUpdate();
                    ps.close();
                }
            }

            st.close();
            con.close();
            JOptionPane.showMessageDialog(this, "Saved to MySQL database successfully!");
            displayArea.append("Data saved to MySQL database.\n");
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Database Error: " + e.getMessage());
            displayArea.append("Database Error: " + e.getMessage() + "\n");
        }
    }
    public static void main(String[] args) {
        SwingUtilities.invokeLater(AttendanceApp::new);
    }
}
class AttendanceRecord {
    private Map<String, Set<LocalDate>> attendanceMap = new HashMap<>();
    public Map<String, Person> users = new HashMap<>();
    public void registerUser(Person p) throws Exception {
        if (users.containsKey(p.id)) throw new Exception("User already exists.");
        users.put(p.id, p);
        attendanceMap.putIfAbsent(p.id, new HashSet<>());
    }
    public void markAttendance(String id) throws Exception {
        if (!users.containsKey(id)) throw new Exception("User not found.");
        attendanceMap.computeIfAbsent(id, k -> new HashSet<>()).add(LocalDate.now());
    }
    public Map<String, Set<LocalDate>> getAllRecords() { return attendanceMap; }
    public void saveToFile(String filename) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename))) {
            for (var entry : attendanceMap.entrySet()) {
                writer.write(entry.getKey() + "," + entry.getValue());
                writer.newLine();
            }
        }
    }
    public void loadFromFile(String filename) throws IOException {
        attendanceMap.clear();
        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",", 2);
                if (parts.length == 2) {
                    String id = parts[0];
                    String[] dates = parts[1].replace("[", "").replace("]", "").split(", ");
                    Set<LocalDate> dateSet = new HashSet<>();
                    for (String d : dates) {
                        if (!d.isBlank()) dateSet.add(LocalDate.parse(d));
                    }
                    attendanceMap.put(id, dateSet);
                }
            }
        }
    }
}