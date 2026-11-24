import javax.swing.*;
import java.awt.*;
import java.sql.*;

public class MainFrame extends JFrame {

    private String studentName;
    private int userId;
    private String role = "STUDENT";

    // COLORS

    private final Color backgroundColor =
            new Color(248, 240, 255);

    private final Color purpleColor =
            new Color(186, 155, 220);

    private final Color darkPurple =
            new Color(110, 75, 145);

    private final Color textColor =
            new Color(70, 50, 80);

    // =====================================================
    // CONSTRUCTORS
    // =====================================================

    public MainFrame() {

        this.studentName = "Student";
        this.userId = 0;
        this.role = "STUDENT";

        createMainFrame();
    }

    public MainFrame(String studentName) {

        this.studentName = studentName;
        this.userId = findUserId(studentName);
        this.role = findUserRole(studentName);

        createMainFrame();
    }

    public MainFrame(
            String studentName,
            int userId
    ) {

        this(studentName, userId, findUserRole(studentName));
    }

    public MainFrame(String studentName, int userId, String role) {
        this.studentName = studentName;
        this.userId = userId;
        this.role = (role == null ? "STUDENT" : role.trim().toUpperCase());
        createMainFrame();
    }

    private static String findUserRole(String username) {
        if (username == null || username.trim().isEmpty()) return "STUDENT";
        String sql = "SELECT ROLE FROM USERS WHERE USERNAME = ?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, username.trim());
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    String found = rs.getString("ROLE");
                    return found == null ? "STUDENT" : found.trim().toUpperCase();
                }
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Could not read account role: " + ex.getMessage());
        }
        return "STUDENT";
    }

    // =====================================================
    // CREATE MAIN FRAME
    // =====================================================

    private void createMainFrame() {

        setTitle(
                "Quiz Application - Main Menu"
        );

        setSize(1100, 780);

        setLocationRelativeTo(null);

        setDefaultCloseOperation(
                JFrame.EXIT_ON_CLOSE
        );

        JPanel mainPanel =
                new JPanel(
                        new BorderLayout(
                                20,
                                20
                        )
                );

        mainPanel.setBackground(
                backgroundColor
        );

        mainPanel.setBorder(
                BorderFactory.createEmptyBorder(
                        30,
                        40,
                        30,
                        40
                )
        );

        // =================================================
        // TITLE
        // =================================================

        JLabel titleLabel =
                new JLabel(
                        "QUIZ APPLICATION",
                        SwingConstants.CENTER
                );

        titleLabel.setFont(
                new Font(
                        "Arial",
                        Font.BOLD,
                        36
                )
        );

        titleLabel.setForeground(
                darkPurple
        );

        mainPanel.add(
                titleLabel,
                BorderLayout.NORTH
        );

        // =================================================
        // STUDENT NAME
        // =================================================

        JPanel topPanel =
                new JPanel(
                        new BorderLayout()
                );

        topPanel.setBackground(
                backgroundColor
        );

        JLabel studentLabel =
                new JLabel(
                        "User: " + studentName + "   |   Role: " + role,
                        SwingConstants.CENTER
                );

        studentLabel.setFont(
                new Font(
                        "Arial",
                        Font.BOLD,
                        20
                )
        );

        studentLabel.setForeground(
                darkPurple
        );

        topPanel.add(
                studentLabel,
                BorderLayout.CENTER
        );

        mainPanel.add(
                topPanel,
                BorderLayout.CENTER
        );

        // =================================================
        // BUTTON PANEL
        // =================================================

        JPanel centerPanel =
                new JPanel(
                        new GridLayout(
                                0,
                                2,
                                25,
                                25
                        )
                );

        centerPanel.setBackground(
                backgroundColor
        );

        JButton categoryButton =
                new JButton(
                        "Category Management"
                );

        JButton questionButton =
                new JButton(
                        "Question Management"
                );

        JButton quizButton =
                new JButton(
                        "Start Quiz"
                );

        JButton resultButton =
                new JButton(
                        "Result Management"
                );

        JButton reportButton =
                new JButton(
                        "Reports"
                );

        JButton exitButton =
                new JButton(
                        "Exit"
                );

        styleButton(categoryButton);
        styleButton(questionButton);
        styleButton(quizButton);
        styleButton(resultButton);
        styleButton(reportButton);
        styleButton(exitButton);

        boolean isAdmin = "ADMIN".equalsIgnoreCase(role);
        if (isAdmin) {
            centerPanel.add(categoryButton);
            centerPanel.add(questionButton);
        }
        centerPanel.add(quizButton);
        if (isAdmin) {
            centerPanel.add(resultButton);
        }
        // Every logged-in user can open reports; ReportFrame restricts student data.
        centerPanel.add(reportButton);
        centerPanel.add(exitButton);

        // Need a separate wrapper because student label
        // and button panel both use center area.

        JPanel contentPanel =
                new JPanel(
                        new BorderLayout(
                                15,
                                15
                        )
                );

        contentPanel.setBackground(
                backgroundColor
        );

        contentPanel.add(
                topPanel,
                BorderLayout.NORTH
        );

        contentPanel.add(
                centerPanel,
                BorderLayout.CENTER
        );

        mainPanel.add(
                contentPanel,
                BorderLayout.CENTER
        );

        // =================================================
        // FOOTER
        // =================================================

        JLabel footerLabel =
                new JLabel(
                        "BCA SEM-3 Quiz Application",
                        SwingConstants.CENTER
                );

        footerLabel.setFont(
                new Font(
                        "Arial",
                        Font.PLAIN,
                        14
                )
        );

        footerLabel.setForeground(
                textColor
        );

        mainPanel.add(
                footerLabel,
                BorderLayout.SOUTH
        );

        add(mainPanel);

        // =================================================
        // CATEGORY
        // =================================================

        categoryButton.addActionListener(e -> {
            if (!"ADMIN".equalsIgnoreCase(role)) {
                JOptionPane.showMessageDialog(this, "Access denied. Admin only.");
                return;
            }
            new CategoryFrame(studentName, userId, role).setVisible(true);

            dispose();
        });

        // =================================================
        // QUESTION
        // =================================================

        questionButton.addActionListener(e -> {
            if (!"ADMIN".equalsIgnoreCase(role)) {
                JOptionPane.showMessageDialog(this, "Access denied. Admin only.");
                return;
            }
            new QuestionFrame(studentName, userId, role).setVisible(true);

            dispose();
        });

        // =================================================
        // START QUIZ
        // =================================================

        quizButton.addActionListener(e -> {

            if (userId <= 0) {

                JOptionPane.showMessageDialog(
                        this,
                        "Student information not found.\n"
                                + "Please login again.",
                        "Login Required",
                        JOptionPane.WARNING_MESSAGE
                );

                dispose();

                new LoginFrame();

                return;
            }

            QuizFrame frame =
                    new QuizFrame(
                            studentName,
                            userId,
                            role
                    );

            frame.setVisible(true);

            dispose();
        });

        // =================================================
        // RESULT MANAGEMENT
        // =================================================

        resultButton.addActionListener(e -> {
            if (!"ADMIN".equalsIgnoreCase(role)) {
                JOptionPane.showMessageDialog(this, "Access denied. Admin only.");
                return;
            }
            ResultManagementFrame frame =
                    new ResultManagementFrame(studentName, userId, role);

            frame.setVisible(true);

            dispose();
        });

        // =================================================
        // REPORTS
        // =================================================

        reportButton.addActionListener(e -> {
            if (userId <= 0) {

                JOptionPane.showMessageDialog(
                        this,
                        "Please login again.",
                        "Login Required",
                        JOptionPane.WARNING_MESSAGE
                );

                return;
            }

            ReportFrame frame =
                    new ReportFrame(
                            studentName,
                            userId,
                            role
                    );

            frame.setVisible(true);

            dispose();
        });

        // =================================================
        // EXIT
        // =================================================

        exitButton.addActionListener(e -> {

            int choice =
                    JOptionPane.showConfirmDialog(
                            this,
                            "Are you sure you want to exit?",
                            "Exit Confirmation",
                            JOptionPane.YES_NO_OPTION
                    );

            if (
                    choice ==
                            JOptionPane.YES_OPTION
            ) {

                System.exit(0);
            }
        });
    }

    // =====================================================
    // FIND USER ID
    // =====================================================

    private int findUserId(
            String studentName
    ) {

        if (
                studentName == null
                        || studentName.trim().isEmpty()
        ) {

            return 0;
        }

        String sql =
                "SELECT USER_ID " +
                "FROM USERS " +
                "WHERE USERNAME = ?";

        try (
                Connection con =
                        DBConnection.getConnection();

                PreparedStatement ps =
                        con.prepareStatement(sql)
        ) {

            ps.setString(
                    1,
                    studentName.trim()
            );

            try (
                    ResultSet rs =
                            ps.executeQuery()
            ) {

                if (rs.next()) {

                    return rs.getInt(
                            "USER_ID"
                    );
                }
            }

        } catch (SQLException ex) {

            JOptionPane.showMessageDialog(
                    this,
                    "Error finding User ID:\n"
                            + ex.getMessage(),
                    "Database Error",
                    JOptionPane.ERROR_MESSAGE
            );
        }

        return 0;
    }

    // =====================================================
    // BUTTON STYLE
    // =====================================================

    private void styleButton(
            JButton button
    ) {

        button.setBackground(
                purpleColor
        );

        button.setForeground(
                Color.WHITE
        );

        button.setFont(
                new Font(
                        "Arial",
                        Font.BOLD,
                        16
                )
        );

        button.setFocusPainted(false);

        button.setBorder(
                BorderFactory.createEmptyBorder(
                        15,
                        20,
                        15,
                        20
                )
        );

        button.setCursor(
                new Cursor(
                        Cursor.HAND_CURSOR
                )
        );
    }

    // =====================================================
    // MAIN
    // =====================================================

    public static void main(
            String[] args
    ) {

        SwingUtilities.invokeLater(
                () -> new LoginFrame()
        );
    }
}