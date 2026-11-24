import javax.swing.*;
import java.awt.*;
import java.sql.*;

public class RegisterFrame extends JFrame {
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JPasswordField confirmPasswordField;
    private JTextField collegeField;
    private JTextField emailField;

    public RegisterFrame() {
        setTitle("Quiz Application - Student Registration");
        setSize(540, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);

        JPanel panel = new JPanel(null);
        panel.setBackground(new Color(245, 247, 250));

        JLabel title = new JLabel("STUDENT REGISTRATION", SwingConstants.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 23));
        title.setBounds(60, 20, 420, 35);
        panel.add(title);

        addLabel(panel, "Username:", 65, 80);
        usernameField = addTextField(panel, 220, 80);

        addLabel(panel, "Password:", 65, 125);
        passwordField = new JPasswordField();
        passwordField.setBounds(220, 125, 240, 30);
        panel.add(passwordField);

        addLabel(panel, "Confirm Password:", 65, 170);
        confirmPasswordField = new JPasswordField();
        confirmPasswordField.setBounds(220, 170, 240, 30);
        panel.add(confirmPasswordField);

        addLabel(panel, "College Name:", 65, 215);
        collegeField = addTextField(panel, 220, 215);

        addLabel(panel, "Email (optional):", 65, 260);
        emailField = addTextField(panel, 220, 260);

        JButton registerButton = new JButton("Create Student Account");
        registerButton.setFont(new Font("Arial", Font.BOLD, 14));
        registerButton.setBounds(145, 320, 250, 38);
        panel.add(registerButton);

        JButton backButton = new JButton("Back to Login");
        backButton.setFont(new Font("Arial", Font.PLAIN, 13));
        backButton.setBounds(190, 375, 160, 32);
        panel.add(backButton);

        registerButton.addActionListener(e -> registerStudent());
        backButton.addActionListener(e -> {
            new LoginFrame().setVisible(true);
            dispose();
        });

        add(panel);
    }

    private void addLabel(JPanel panel, String text, int x, int y) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Arial", Font.PLAIN, 15));
        label.setBounds(x, y, 150, 28);
        panel.add(label);
    }

    private JTextField addTextField(JPanel panel, int x, int y) {
        JTextField field = new JTextField();
        field.setFont(new Font("Arial", Font.PLAIN, 14));
        field.setBounds(x, y, 240, 30);
        panel.add(field);
        return field;
    }

    private void registerStudent() {
        String username = usernameField.getText().trim();
        String password = new String(passwordField.getPassword());
        String confirmPassword = new String(confirmPasswordField.getPassword());
        String college = collegeField.getText().trim();
        String email = emailField.getText().trim();

        if (username.isEmpty() || password.isEmpty() || college.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Username, Password and College Name are required.");
            return;
        }
        if (password.length() < 4) {
            JOptionPane.showMessageDialog(this, "Password must be at least 4 characters.");
            return;
        }
        if (!password.equals(confirmPassword)) {
            JOptionPane.showMessageDialog(this, "Password and Confirm Password do not match.");
            return;
        }

        String checkSql = "SELECT USER_ID FROM USERS WHERE UPPER(USERNAME) = UPPER(?)";
        String insertSql = "INSERT INTO USERS " +
                "(USER_ID, USERNAME, PASSWORD, COLLEGE_NAME, EMAIL, ROLE) " +
                "VALUES (USER_SEQ.NEXTVAL, ?, ?, ?, ?, 'STUDENT')";

        try (Connection con = DBConnection.getConnection()) {
            try (PreparedStatement check = con.prepareStatement(checkSql)) {
                check.setString(1, username);
                try (ResultSet rs = check.executeQuery()) {
                    if (rs.next()) {
                        JOptionPane.showMessageDialog(this,
                                "This username already exists. Please choose another username.",
                                "Username Exists", JOptionPane.WARNING_MESSAGE);
                        return;
                    }
                }
            }

            try (PreparedStatement insert = con.prepareStatement(insertSql)) {
                insert.setString(1, username);
                insert.setString(2, password);
                insert.setString(3, college);
                if (email.isEmpty()) insert.setNull(4, Types.VARCHAR);
                else insert.setString(4, email);
                insert.executeUpdate();
            }

            JOptionPane.showMessageDialog(this,
                    "Registration successful! You can now log in with your username and password.",
                    "Registration Complete", JOptionPane.INFORMATION_MESSAGE);
            new LoginFrame().setVisible(true);
            dispose();

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this,
                    "Registration failed:\n" + ex.getMessage() +
                    "\n\nCheck that USERS has EMAIL and ROLE columns and USER_SEQ exists.",
                    "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
