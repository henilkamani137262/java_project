import javax.swing.*;
import java.awt.*;
import java.sql.*;

public class LoginFrame extends JFrame {
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JComboBox<String> roleBox;

    public LoginFrame() {
        setTitle("Quiz Application - Login");
        setSize(520, 390);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);

        JPanel panel = new JPanel(null);
        panel.setBackground(new Color(245, 247, 250));

        JLabel title = new JLabel("QUIZ APPLICATION", SwingConstants.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 25));
        title.setBounds(70, 22, 380, 40);
        panel.add(title);

        JLabel roleLabel = new JLabel("Login Type:");
        roleLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        roleLabel.setBounds(70, 85, 120, 28);
        panel.add(roleLabel);

        roleBox = new JComboBox<>(new String[]{"STUDENT", "ADMIN"});
        roleBox.setFont(new Font("Arial", Font.PLAIN, 15));
        roleBox.setBounds(210, 85, 230, 30);
        panel.add(roleBox);

        JLabel usernameLabel = new JLabel("Username:");
        usernameLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        usernameLabel.setBounds(70, 130, 120, 28);
        panel.add(usernameLabel);

        usernameField = new JTextField();
        usernameField.setFont(new Font("Arial", Font.PLAIN, 15));
        usernameField.setBounds(210, 130, 230, 30);
        panel.add(usernameField);

        JLabel passwordLabel = new JLabel("Password:");
        passwordLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        passwordLabel.setBounds(70, 175, 120, 28);
        panel.add(passwordLabel);

        passwordField = new JPasswordField();
        passwordField.setFont(new Font("Arial", Font.PLAIN, 15));
        passwordField.setBounds(210, 175, 230, 30);
        panel.add(passwordField);

        JButton loginButton = new JButton("Login");
        loginButton.setFont(new Font("Arial", Font.BOLD, 14));
        loginButton.setBounds(95, 235, 135, 38);
        panel.add(loginButton);

        JButton registerButton = new JButton("New Student? Register");
        registerButton.setFont(new Font("Arial", Font.BOLD, 13));
        registerButton.setBounds(240, 235, 200, 38);
        panel.add(registerButton);

        JLabel note = new JLabel("New students must register before logging in.", SwingConstants.CENTER);
        note.setFont(new Font("Arial", Font.PLAIN, 12));
        note.setForeground(new Color(90, 90, 90));
        note.setBounds(50, 290, 420, 25);
        panel.add(note);

        loginButton.addActionListener(e -> loginUser());
        passwordField.addActionListener(e -> loginUser());
        registerButton.addActionListener(e -> {
            RegisterFrame registerFrame = new RegisterFrame();
            registerFrame.setVisible(true);
            dispose();
        });

        add(panel);
    }

    private void loginUser() {
        String username = usernameField.getText().trim();
        String password = new String(passwordField.getPassword());
        String selectedRole = String.valueOf(roleBox.getSelectedItem()).trim().toUpperCase();

        if (username.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter Username.");
            return;
        }
        if (password.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter Password.");
            return;
        }

        String sql = "SELECT USER_ID, PASSWORD, ROLE FROM USERS WHERE USERNAME = ?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, username);

            try (ResultSet rs = ps.executeQuery()) {
                if (!rs.next()) {
                    JOptionPane.showMessageDialog(this,
                            "Account not found. New students should click 'New Student? Register'.",
                            "Login Failed", JOptionPane.WARNING_MESSAGE);
                    return;
                }

                int userId = rs.getInt("USER_ID");
                String savedPassword = rs.getString("PASSWORD");
                String role = rs.getString("ROLE");
                if (role == null || role.trim().isEmpty()) role = "STUDENT";
                role = role.trim().toUpperCase();

                if (!password.equals(savedPassword)) {
                    JOptionPane.showMessageDialog(this, "Invalid password.",
                            "Login Failed", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                if (!selectedRole.equals(role)) {
                    JOptionPane.showMessageDialog(this,
                            "This account is not registered as " + selectedRole + ".",
                            "Role Mismatch", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                JOptionPane.showMessageDialog(this, "Login successful!");
                dispose();
                new MainFrame(username, userId, role).setVisible(true);
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Database Error:\n" + ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new LoginFrame().setVisible(true));
    }
}
