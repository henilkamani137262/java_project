import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class ReportFrame extends JFrame {

    // =====================================================
    // VARIABLES
    // =====================================================

    private String studentName;
    private int userId;
    private String role = "STUDENT";

    private JTable myHistoryTable;
    private JTable allHistoryTable;

    private DefaultTableModel myModel;
    private DefaultTableModel allModel;

    // =====================================================
    // CONSTRUCTOR
    // =====================================================

    public ReportFrame(String studentName, int userId) {
        this(studentName, userId, "STUDENT");
    }

    public ReportFrame(String studentName, int userId, String role) {

        this.studentName = studentName;
        this.userId = userId;
        this.role = (role == null || role.trim().isEmpty()) ? "STUDENT" : role.trim().toUpperCase();

        setTitle("Quiz Reports & History");

        setSize(1150, 650);

        setLocationRelativeTo(null);

        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            @Override public void windowClosing(WindowEvent e) { returnToMainMenu(); }
        });

        // =================================================
        // MAIN PANEL
        // =================================================

        JPanel mainPanel = new JPanel();

        mainPanel.setLayout(new BorderLayout(10, 10));

        mainPanel.setBorder(
                BorderFactory.createEmptyBorder(
                        15,
                        15,
                        15,
                        15
                )
        );

        // =================================================
        // TOP PANEL
        // =================================================

        JPanel topPanel = new JPanel();

        topPanel.setLayout(new BorderLayout());

        // Title

        JLabel titleLabel = new JLabel(
                "Quiz Reports & History",
                SwingConstants.CENTER
        );

        titleLabel.setFont(
                new Font(
                        "Arial",
                        Font.BOLD,
                        26
                )
        );

        // Student name

        JLabel studentLabel = new JLabel(
                "Logged-in Student: " + studentName
        );

        studentLabel.setFont(
                new Font(
                        "Arial",
                        Font.BOLD,
                        15
                )
        );

        topPanel.add(
                titleLabel,
                BorderLayout.CENTER
        );

        topPanel.add(
                studentLabel,
                BorderLayout.SOUTH
        );

        mainPanel.add(
                topPanel,
                BorderLayout.NORTH
        );

        // =================================================
        // MY HISTORY TABLE
        // =================================================

        String[] myColumns = {

                "Result ID",
                "Category",
                "Total",
                "Answered",
                "Unanswered",
                "Correct",
                "Wrong",
                "Score %",
                "Status",
                "Date"
        };

        myModel = new DefaultTableModel(
                myColumns,
                0
        ) {

            @Override
            public boolean isCellEditable(
                    int row,
                    int column
            ) {
                return false;
            }
        };

        myHistoryTable = new JTable(
                myModel
        );

        myHistoryTable.setFont(
                new Font(
                        "Arial",
                        Font.PLAIN,
                        14
                )
        );

        myHistoryTable.setRowHeight(28);

        myHistoryTable.getTableHeader()
                .setFont(
                        new Font(
                                "Arial",
                                Font.BOLD,
                                14
                        )
                );

        JScrollPane myScrollPane =
                new JScrollPane(
                        myHistoryTable
                );

        // =================================================
        // ALL HISTORY TABLE
        // =================================================

        String[] allColumns = {

                "Result ID",
                "Student",
                "College",
                "Category",
                "Total",
                "Answered",
                "Unanswered",
                "Correct",
                "Wrong",
                "Score %",
                "Status",
                "Date"
        };

        allModel = new DefaultTableModel(
                allColumns,
                0
        ) {

            @Override
            public boolean isCellEditable(
                    int row,
                    int column
            ) {
                return false;
            }
        };

        allHistoryTable = new JTable(
                allModel
        );

        allHistoryTable.setFont(
                new Font(
                        "Arial",
                        Font.PLAIN,
                        14
                )
        );

        allHistoryTable.setRowHeight(28);

        allHistoryTable.getTableHeader()
                .setFont(
                        new Font(
                                "Arial",
                                Font.BOLD,
                                14
                        )
                );

        JScrollPane allScrollPane =
                new JScrollPane(
                        allHistoryTable
                );

        // =================================================
        // TABBED PANE
        // =================================================

        JTabbedPane tabbedPane =
                new JTabbedPane();

        // Students can see only their own history.
        // Only ADMIN accounts can see all students' history.
        tabbedPane.addTab("My Quiz History", myScrollPane);

        if ("ADMIN".equalsIgnoreCase(role)) {
            tabbedPane.addTab("All Quiz History", allScrollPane);
        }

        mainPanel.add(
                tabbedPane,
                BorderLayout.CENTER
        );

        // =================================================
        // BUTTON PANEL
        // =================================================

        JPanel buttonPanel =
                new JPanel();

        buttonPanel.setLayout(
                new FlowLayout(
                        FlowLayout.CENTER,
                        15,
                        10
                )
        );

        // Refresh button

        JButton refreshButton =
                new JButton(
                        "Refresh"
                );

        refreshButton.setFont(
                new Font(
                        "Arial",
                        Font.BOLD,
                        14
                )
        );

        // Back button

        JButton backButton =
                new JButton(
                        "Back to Main Menu"
                );

        backButton.setFont(
                new Font(
                        "Arial",
                        Font.BOLD,
                        14
                )
        );

        buttonPanel.add(
                refreshButton
        );

        buttonPanel.add(
                backButton
        );

        mainPanel.add(
                buttonPanel,
                BorderLayout.SOUTH
        );

        // =================================================
        // REFRESH BUTTON ACTION
        // =================================================

        refreshButton.addActionListener(e -> {

            loadMyHistory();

            if ("ADMIN".equalsIgnoreCase(role)) {
                loadAllHistory();
            }

            JOptionPane.showMessageDialog(
                    this,
                    "History refreshed successfully!"
            );
        });

        // =================================================
        // BACK BUTTON ACTION
        // =================================================

        backButton.addActionListener(e -> returnToMainMenu());

        // =================================================
        // ADD MAIN PANEL
        // =================================================

        add(mainPanel);

        // =================================================
        // LOAD DATA
        // =================================================

        loadMyHistory();

        if ("ADMIN".equalsIgnoreCase(role)) {
            loadAllHistory();
        }
    }

    // =====================================================
    // MY HISTORY
    // =====================================================

    private void loadMyHistory() {

        // Clear old data

        myModel.setRowCount(0);

        if (userId <= 0) {
            JOptionPane.showMessageDialog(this,
                    "Logged-in USER_ID is missing. Please log in again.",
                    "Login Required", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String sql =
                "SELECT " +
                "r.RESULT_ID, " +
                "c.CATEGORY_NAME, " +
                "r.TOTAL_QUESTIONS, " +
                "NVL(r.ANSWERED_QUESTIONS, NVL(r.CORRECT_ANSWERS,0) + NVL(r.WRONG_ANSWERS,0)) AS ANSWERED_QUESTIONS, " +
                "(NVL(r.TOTAL_QUESTIONS,0) - NVL(r.ANSWERED_QUESTIONS, NVL(r.CORRECT_ANSWERS,0) + NVL(r.WRONG_ANSWERS,0))) AS UNANSWERED_QUESTIONS, " +
                "r.CORRECT_ANSWERS, " +
                "r.WRONG_ANSWERS, " +
                "r.SCORE_PERCENTAGE, " +
                "NVL(r.STATUS, 'COMPLETED') AS STATUS, " +
                "r.RESULT_DATE " +

                "FROM RESULTS r " +

                "LEFT JOIN CATEGORIES c " +
                "ON r.CATEGORY_ID = c.CATEGORY_ID " +

                "WHERE r.USER_ID = ? " +

                "ORDER BY r.RESULT_DATE DESC";

        try (
                Connection con =
                        DBConnection.getConnection();

                PreparedStatement ps =
                        con.prepareStatement(sql)
        ) {

            // Logged-in user ID

            ps.setInt(
                    1,
                    userId
            );

            try (
                    ResultSet rs =
                            ps.executeQuery()
            ) {

                while (rs.next()) {

                    int resultId =
                            rs.getInt(
                                    "RESULT_ID"
                            );

                    String category =
                            rs.getString(
                                    "CATEGORY_NAME"
                            );

                    int total =
                            rs.getInt(
                                    "TOTAL_QUESTIONS"
                            );

                    int answered = rs.getInt("ANSWERED_QUESTIONS");
                    int unanswered = rs.getInt("UNANSWERED_QUESTIONS");

                    int correct =
                            rs.getInt(
                                    "CORRECT_ANSWERS"
                            );

                    int wrong =
                            rs.getInt(
                                    "WRONG_ANSWERS"
                            );

                    double score =
                            rs.getDouble(
                                    "SCORE_PERCENTAGE"
                            );

                    String status = rs.getString("STATUS");
                    Timestamp date =
                            rs.getTimestamp(
                                    "RESULT_DATE"
                            );

                    Object[] row = {
                            resultId, category, total, answered, unanswered,
                            correct, wrong, score, status, date
                    };

                    myModel.addRow(row);
                }
            }

        } catch (SQLException ex) {

            JOptionPane.showMessageDialog(
                    this,
                    "Error loading your quiz history:\n"
                            + ex.getMessage(),
                    "Database Error",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    // =====================================================
    // ALL STUDENTS HISTORY
    // =====================================================

    private void loadAllHistory() {

        allModel.setRowCount(0);

        // Defence in depth: a student must never load all students' records.
        if (!"ADMIN".equalsIgnoreCase(role)) {
            return;
        }

        String sql =
                "SELECT " +

                "r.RESULT_ID, " +

                "u.USERNAME, " +

                "u.COLLEGE_NAME, " +

                "c.CATEGORY_NAME, " +

                "r.TOTAL_QUESTIONS, " +

                "NVL(r.ANSWERED_QUESTIONS, NVL(r.CORRECT_ANSWERS,0) + NVL(r.WRONG_ANSWERS,0)) AS ANSWERED_QUESTIONS, " +

                "(NVL(r.TOTAL_QUESTIONS,0) - NVL(r.ANSWERED_QUESTIONS, NVL(r.CORRECT_ANSWERS,0) + NVL(r.WRONG_ANSWERS,0))) AS UNANSWERED_QUESTIONS, " +

                "r.CORRECT_ANSWERS, " +

                "r.WRONG_ANSWERS, " +

                "r.SCORE_PERCENTAGE, " +

                "NVL(r.STATUS, 'COMPLETED') AS STATUS, " +

                "r.RESULT_DATE " +

                "FROM RESULTS r " +

                "LEFT JOIN USERS u " +

                "ON r.USER_ID = u.USER_ID " +

                "LEFT JOIN CATEGORIES c " +

                "ON r.CATEGORY_ID = c.CATEGORY_ID " +

                "ORDER BY r.RESULT_DATE DESC";

        try (
                Connection con =
                        DBConnection.getConnection();

                PreparedStatement ps =
                        con.prepareStatement(sql);

                ResultSet rs =
                        ps.executeQuery()
        ) {

            while (rs.next()) {

                int resultId =
                        rs.getInt(
                                "RESULT_ID"
                        );

                String student =
                        rs.getString(
                                "USERNAME"
                        );

                String college =
                        rs.getString(
                                "COLLEGE_NAME"
                        );

                String category =
                        rs.getString(
                                "CATEGORY_NAME"
                        );

                int total =
                        rs.getInt(
                                "TOTAL_QUESTIONS"
                        );

                int answered = rs.getInt("ANSWERED_QUESTIONS");
                int unanswered = rs.getInt("UNANSWERED_QUESTIONS");

                int correct =
                        rs.getInt(
                                "CORRECT_ANSWERS"
                        );

                int wrong =
                        rs.getInt(
                                "WRONG_ANSWERS"
                        );

                double score =
                        rs.getDouble(
                                "SCORE_PERCENTAGE"
                        );

                String status = rs.getString("STATUS");
                Timestamp date =
                        rs.getTimestamp(
                                "RESULT_DATE"
                        );

                Object[] row = {
                        resultId, student, college, category, total,
                        answered, unanswered, correct, wrong, score, status, date
                };

                allModel.addRow(row);
            }

        } catch (SQLException ex) {

            JOptionPane.showMessageDialog(
                    this,
                    "Error loading all quiz history:\n"
                            + ex.getMessage(),
                    "Database Error",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    private void returnToMainMenu() {
        dispose();
        MainFrame frame = new MainFrame(studentName, userId, role);
        frame.setVisible(true);
    }

    // =====================================================
    // MAIN METHOD
    // =====================================================

    public static void main(String[] args) {

        SwingUtilities.invokeLater(() -> {

            ReportFrame frame =
                    new ReportFrame(
                            "Henil",
                            1
                    );

            frame.setVisible(true);
        });
    }
}