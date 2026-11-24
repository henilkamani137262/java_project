import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;
import java.util.ArrayList;

public class QuestionFrame extends JFrame {

    private String studentName;
    private int userId;
    private String role;

    Color backgroundColor = new Color(248, 240, 250);
    Color primaryColor = new Color(190, 160, 210);
    Color darkColor = new Color(110, 75, 130);
    Color textColor = new Color(65, 45, 75);

    JTable questionTable;
    DefaultTableModel tableModel;

    JComboBox<String> categoryCombo;

    JTextArea txtQuestion;

    JTextField txtOptionA;
    JTextField txtOptionB;
    JTextField txtOptionC;
    JTextField txtOptionD;

    JComboBox<String> correctAnswerCombo;

    JTextField txtSearch;

    ArrayList<Integer> categoryIds =
            new ArrayList<>();

    // Default constructor kept for compatibility with older code.
    public QuestionFrame() {
        this("Student", 0, "STUDENT");
    }

    // Compatibility constructor for code that only passes a username.
    public QuestionFrame(String studentName) {
        this(studentName, findUserId(studentName), findUserRole(studentName));
    }

    // Preferred constructor: keeps the current user's session and role.
    public QuestionFrame(String studentName, int userId, String role) {

        this.studentName = studentName;
        this.userId = userId;
        this.role = (role == null || role.trim().isEmpty())
                ? "STUDENT" : role.trim().toUpperCase();

        setTitle("Question Management");

        setSize(1100, 700);

        setLocationRelativeTo(null);

        setDefaultCloseOperation(
                JFrame.DISPOSE_ON_CLOSE
        );

        JPanel mainPanel =
                new JPanel(
                        new BorderLayout(15, 15)
                );

        mainPanel.setBackground(
                backgroundColor
        );

        mainPanel.setBorder(
                BorderFactory.createEmptyBorder(
                        20, 20, 20, 20
                )
        );

        JLabel title =
                new JLabel(
                        "QUESTION MANAGEMENT",
                        SwingConstants.CENTER
                );

        title.setFont(
                new Font(
                        "Arial",
                        Font.BOLD,
                        26
                )
        );

        title.setForeground(
                darkColor
        );

        mainPanel.add(
                title,
                BorderLayout.NORTH
        );

        // FORM PANEL

        JPanel formPanel =
                new JPanel(
                        new GridBagLayout()
                );

        formPanel.setBackground(
                backgroundColor
        );

        GridBagConstraints gbc =
                new GridBagConstraints();

        gbc.insets =
                new Insets(
                        6, 8, 6, 8
                );

        gbc.fill =
                GridBagConstraints.HORIZONTAL;

        // CATEGORY

        categoryCombo =
                new JComboBox<>();

        addRow(
                formPanel,
                gbc,
                createLabel("Category:"),
                categoryCombo,
                0
        );

        // QUESTION

        txtQuestion =
                new JTextArea(3, 20);

        txtQuestion.setLineWrap(true);

        txtQuestion.setWrapStyleWord(true);

        JScrollPane questionScroll =
                new JScrollPane(
                        txtQuestion
                );

        addRow(
                formPanel,
                gbc,
                createLabel("Question:"),
                questionScroll,
                1
        );

        // OPTION A

        txtOptionA =
                new JTextField();

        addRow(
                formPanel,
                gbc,
                createLabel("Option A:"),
                txtOptionA,
                2
        );

        // OPTION B

        txtOptionB =
                new JTextField();

        addRow(
                formPanel,
                gbc,
                createLabel("Option B:"),
                txtOptionB,
                3
        );

        // OPTION C

        txtOptionC =
                new JTextField();

        addRow(
                formPanel,
                gbc,
                createLabel("Option C:"),
                txtOptionC,
                4
        );

        // OPTION D

        txtOptionD =
                new JTextField();

        addRow(
                formPanel,
                gbc,
                createLabel("Option D:"),
                txtOptionD,
                5
        );

        // CORRECT ANSWER

        correctAnswerCombo =
                new JComboBox<>(
                        new String[]{
                                "A",
                                "B",
                                "C",
                                "D"
                        }
                );

        addRow(
                formPanel,
                gbc,
                createLabel("Correct Answer:"),
                correctAnswerCombo,
                6
        );

        mainPanel.add(
                formPanel,
                BorderLayout.WEST
        );

        // TABLE

        tableModel =
                new DefaultTableModel(
                        new Object[]{
                                "Question ID",
                                "Category",
                                "Question",
                                "A",
                                "B",
                                "C",
                                "D",
                                "Correct"
                        },
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

        questionTable =
                new JTable(tableModel);

        questionTable.setRowHeight(28);

        questionTable
                .getTableHeader()
                .setBackground(
                        primaryColor
                );

        questionTable
                .getTableHeader()
                .setForeground(
                        Color.WHITE
                );

        questionTable
                .getTableHeader()
                .setFont(
                        new Font(
                                "Arial",
                                Font.BOLD,
                                12
                        )
                );

        JScrollPane tableScroll =
                new JScrollPane(
                        questionTable
                );

        // RIGHT PANEL

        JPanel rightPanel =
                new JPanel(
                        new BorderLayout(
                                10, 10
                        )
                );

        rightPanel.setBackground(
                backgroundColor
        );

        // SEARCH

        JPanel searchPanel =
                new JPanel(
                        new FlowLayout(
                                FlowLayout.LEFT
                        )
                );

        searchPanel.setBackground(
                backgroundColor
        );

        txtSearch =
                new JTextField(
                        25
                );

        JButton searchButton =
                new JButton(
                        "Search"
                );

        JButton refreshButton =
                new JButton(
                        "Refresh"
                );

        styleButton(searchButton);
        styleButton(refreshButton);

        searchPanel.add(
                createLabel(
                        "Search:"
                )
        );

        searchPanel.add(
                txtSearch
        );

        searchPanel.add(
                searchButton
        );

        searchPanel.add(
                refreshButton
        );

        rightPanel.add(
                searchPanel,
                BorderLayout.NORTH
        );

        rightPanel.add(
                tableScroll,
                BorderLayout.CENTER
        );

        mainPanel.add(
                rightPanel,
                BorderLayout.CENTER
        );

        // BUTTON PANEL

        JPanel buttonPanel =
                new JPanel(
                        new FlowLayout(
                                FlowLayout.CENTER,
                                10,
                                10
                        )
                );

        buttonPanel.setBackground(
                backgroundColor
        );

        JButton addButton =
                new JButton(
                        "Add Question"
                );

        JButton updateButton =
                new JButton(
                        "Update Question"
                );

        JButton deleteButton =
                new JButton(
                        "Delete Question"
                );

        JButton clearButton =
                new JButton(
                        "Clear"
                );

        JButton mainMenuButton =
                new JButton(
                        "Main Menu"
                );

        JButton[] buttons = {
                addButton,
                updateButton,
                deleteButton,
                clearButton,
                mainMenuButton
        };

        for (JButton button : buttons) {

            styleButton(button);

            buttonPanel.add(button);
        }

        mainPanel.add(
                buttonPanel,
                BorderLayout.SOUTH
        );

        add(mainPanel);

        // LOAD DATA

        loadCategories();

        loadQuestions();

        // ACTIONS

        addButton.addActionListener(
                e -> addQuestion()
        );

        updateButton.addActionListener(
                e -> updateQuestion()
        );

        deleteButton.addActionListener(
                e -> deleteQuestion()
        );

        clearButton.addActionListener(
                e -> clearFields()
        );

        searchButton.addActionListener(
                e -> searchQuestions()
        );

        refreshButton.addActionListener(
                e -> {
                    txtSearch.setText("");
                    loadQuestions();
                }
        );

        questionTable.getSelectionModel()
                .addListSelectionListener(
                        e -> loadSelectedQuestion()
                );

        mainMenuButton.addActionListener(
                e -> {

                    dispose();

                    MainFrame frame =
                            new MainFrame(studentName, userId, role);

                    frame.setVisible(true);
                }
        );
    }

    private static int findUserId(String username) {
        if (username == null || username.trim().isEmpty()) return 0;
        String sql = "SELECT USER_ID FROM USERS WHERE USERNAME = ?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, username.trim());
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return rs.getInt("USER_ID");
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null,
                    "Could not read user ID: " + ex.getMessage());
        }
        return 0;
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
            JOptionPane.showMessageDialog(null,
                    "Could not read account role: " + ex.getMessage());
        }
        return "STUDENT";
    }

    private JLabel createLabel(
            String text
    ) {

        JLabel label =
                new JLabel(text);

        label.setFont(
                new Font(
                        "Arial",
                        Font.BOLD,
                        13
                )
        );

        label.setForeground(
                textColor
        );

        return label;
    }

    private void addRow(
            JPanel panel,
            GridBagConstraints gbc,
            Component label,
            Component field,
            int row
    ) {

        gbc.gridx = 0;

        gbc.gridy = row;

        gbc.weightx = 0.3;

        panel.add(
                label,
                gbc
        );

        gbc.gridx = 1;

        gbc.weightx = 0.7;

        panel.add(
                field,
                gbc
        );
    }

    private void styleButton(
            JButton button
    ) {

        button.setBackground(
                primaryColor
        );

        button.setForeground(
                Color.WHITE
        );

        button.setFont(
                new Font(
                        "Arial",
                        Font.BOLD,
                        12
                )
        );

        button.setFocusPainted(false);
    }

    // LOAD CATEGORIES

    private void loadCategories() {

        categoryCombo.removeAllItems();

        categoryIds.clear();

        String sql =
                "SELECT CATEGORY_ID, CATEGORY_NAME " +
                "FROM CATEGORIES " +
                "ORDER BY CATEGORY_NAME";

        try (
                Connection con =
                        DBConnection.getConnection();

                Statement st =
                        con.createStatement();

                ResultSet rs =
                        st.executeQuery(sql)
        ) {

            while (rs.next()) {

                int id =
                        rs.getInt(
                                "CATEGORY_ID"
                        );

                String name =
                        rs.getString(
                                "CATEGORY_NAME"
                        );

                categoryIds.add(id);

                categoryCombo.addItem(name);
            }

        } catch (SQLException ex) {

            JOptionPane.showMessageDialog(
                    this,
                    "Category loading error:\n"
                            + ex.getMessage()
            );
        }
    }

    // LOAD QUESTIONS

    private void loadQuestions() {

        tableModel.setRowCount(0);

        String sql =
                "SELECT q.QUESTION_ID, " +
                "c.CATEGORY_NAME, " +
                "q.QUESTION_TEXT, " +
                "q.OPTION_A, " +
                "q.OPTION_B, " +
                "q.OPTION_C, " +
                "q.OPTION_D, " +
                "q.CORRECT_ANSWER " +
                "FROM QUESTIONS q " +
                "LEFT JOIN CATEGORIES c " +
                "ON q.CATEGORY_ID = c.CATEGORY_ID " +
                "ORDER BY q.QUESTION_ID";

        try (
                Connection con =
                        DBConnection.getConnection();

                Statement st =
                        con.createStatement();

                ResultSet rs =
                        st.executeQuery(sql)
        ) {

            while (rs.next()) {

                tableModel.addRow(
                        new Object[]{
                                rs.getInt(
                                        "QUESTION_ID"
                                ),

                                rs.getString(
                                        "CATEGORY_NAME"
                                ),

                                rs.getString(
                                        "QUESTION_TEXT"
                                ),

                                rs.getString(
                                        "OPTION_A"
                                ),

                                rs.getString(
                                        "OPTION_B"
                                ),

                                rs.getString(
                                        "OPTION_C"
                                ),

                                rs.getString(
                                        "OPTION_D"
                                ),

                                rs.getString(
                                        "CORRECT_ANSWER"
                                )
                        }
                );
            }

        } catch (SQLException ex) {

            showError(ex);
        }
    }

    // ADD QUESTION

    private void addQuestion() {

        if (
                categoryCombo.getSelectedIndex()
                        == -1
        ) {

            JOptionPane.showMessageDialog(
                    this,
                    "Please select a category."
            );

            return;
        }

        String question =
                txtQuestion.getText().trim();

        String optionA =
                txtOptionA.getText().trim();

        String optionB =
                txtOptionB.getText().trim();

        String optionC =
                txtOptionC.getText().trim();

        String optionD =
                txtOptionD.getText().trim();

        String correct =
                correctAnswerCombo
                        .getSelectedItem()
                        .toString();

        if (
                question.isEmpty()
                        ||
                optionA.isEmpty()
                        ||
                optionB.isEmpty()
                        ||
                optionC.isEmpty()
                        ||
                optionD.isEmpty()
        ) {

            JOptionPane.showMessageDialog(
                    this,
                    "Please fill all fields."
            );

            return;
        }

        int categoryId =
                categoryIds.get(
                        categoryCombo
                                .getSelectedIndex()
                );

        String sql =
                "INSERT INTO QUESTIONS " +
                "(QUESTION_ID, CATEGORY_ID, " +
                "QUESTION_TEXT, OPTION_A, " +
                "OPTION_B, OPTION_C, OPTION_D, " +
                "CORRECT_ANSWER) " +
                "VALUES " +
                "(QUESTION_SEQ.NEXTVAL, ?, ?, ?, ?, ?, ?, ?)";

        try (
                Connection con =
                        DBConnection.getConnection();

                PreparedStatement pst =
                        con.prepareStatement(sql)
        ) {

            pst.setInt(
                    1,
                    categoryId
            );

            pst.setString(
                    2,
                    question
            );

            pst.setString(
                    3,
                    optionA
            );

            pst.setString(
                    4,
                    optionB
            );

            pst.setString(
                    5,
                    optionC
            );

            pst.setString(
                    6,
                    optionD
            );

            pst.setString(
                    7,
                    correct
            );

            pst.executeUpdate();

            JOptionPane.showMessageDialog(
                    this,
                    "Question added successfully!"
            );

            clearFields();

            loadQuestions();

        } catch (SQLException ex) {

            showError(ex);
        }
    }

    // LOAD SELECTED QUESTION

    private void loadSelectedQuestion() {

        int row =
                questionTable.getSelectedRow();

        if (row == -1) {

            return;
        }

        String category =
                questionTable
                        .getValueAt(
                                row,
                                1
                        )
                        .toString();

        categoryCombo.setSelectedItem(
                category
        );

        txtQuestion.setText(
                questionTable
                        .getValueAt(
                                row,
                                2
                        )
                        .toString()
        );

        txtOptionA.setText(
                questionTable
                        .getValueAt(
                                row,
                                3
                        )
                        .toString()
        );

        txtOptionB.setText(
                questionTable
                        .getValueAt(
                                row,
                                4
                        )
                        .toString()
        );

        txtOptionC.setText(
                questionTable
                        .getValueAt(
                                row,
                                5
                        )
                        .toString()
        );

        txtOptionD.setText(
                questionTable
                        .getValueAt(
                                row,
                                6
                        )
                        .toString()
        );

        correctAnswerCombo.setSelectedItem(
                questionTable
                        .getValueAt(
                                row,
                                7
                        )
                        .toString()
        );
    }

    // UPDATE QUESTION

    private void updateQuestion() {

        int row =
                questionTable.getSelectedRow();

        if (row == -1) {

            JOptionPane.showMessageDialog(
                    this,
                    "Please select a question."
            );

            return;
        }

        int questionId =
                Integer.parseInt(
                        questionTable
                                .getValueAt(
                                        row,
                                        0
                                )
                                .toString()
                );

        int categoryId =
                categoryIds.get(
                        categoryCombo
                                .getSelectedIndex()
                );

        String sql =
                "UPDATE QUESTIONS SET " +
                "CATEGORY_ID = ?, " +
                "QUESTION_TEXT = ?, " +
                "OPTION_A = ?, " +
                "OPTION_B = ?, " +
                "OPTION_C = ?, " +
                "OPTION_D = ?, " +
                "CORRECT_ANSWER = ? " +
                "WHERE QUESTION_ID = ?";

        try (
                Connection con =
                        DBConnection.getConnection();

                PreparedStatement pst =
                        con.prepareStatement(sql)
        ) {

            pst.setInt(
                    1,
                    categoryId
            );

            pst.setString(
                    2,
                    txtQuestion
                            .getText()
                            .trim()
            );

            pst.setString(
                    3,
                    txtOptionA
                            .getText()
                            .trim()
            );

            pst.setString(
                    4,
                    txtOptionB
                            .getText()
                            .trim()
            );

            pst.setString(
                    5,
                    txtOptionC
                            .getText()
                            .trim()
            );

            pst.setString(
                    6,
                    txtOptionD
                            .getText()
                            .trim()
            );

            pst.setString(
                    7,
                    correctAnswerCombo
                            .getSelectedItem()
                            .toString()
            );

            pst.setInt(
                    8,
                    questionId
            );

            pst.executeUpdate();

            JOptionPane.showMessageDialog(
                    this,
                    "Question updated successfully!"
            );

            loadQuestions();

            clearFields();

        } catch (SQLException ex) {

            showError(ex);
        }
    }

    // DELETE QUESTION

    private void deleteQuestion() {

        int row =
                questionTable.getSelectedRow();

        if (row == -1) {

            JOptionPane.showMessageDialog(
                    this,
                    "Please select a question."
            );

            return;
        }

        int questionId =
                Integer.parseInt(
                        questionTable
                                .getValueAt(
                                        row,
                                        0
                                )
                                .toString()
                );

        int confirm =
                JOptionPane.showConfirmDialog(
                        this,
                        "Delete this question?",
                        "Confirm Delete",
                        JOptionPane.YES_NO_OPTION
                );

        if (
                confirm !=
                JOptionPane.YES_OPTION
        ) {

            return;
        }

        String sql =
                "DELETE FROM QUESTIONS " +
                "WHERE QUESTION_ID = ?";

        try (
                Connection con =
                        DBConnection.getConnection();

                PreparedStatement pst =
                        con.prepareStatement(sql)
        ) {

            pst.setInt(
                    1,
                    questionId
            );

            pst.executeUpdate();

            JOptionPane.showMessageDialog(
                    this,
                    "Question deleted successfully!"
            );

            loadQuestions();

            clearFields();

        } catch (SQLException ex) {

            showError(ex);
        }
    }

    // SEARCH QUESTIONS

    private void searchQuestions() {

        String search =
                txtSearch
                        .getText()
                        .trim();

        if (search.isEmpty()) {

            loadQuestions();

            return;
        }

        tableModel.setRowCount(0);

        String sql =
                "SELECT q.QUESTION_ID, " +
                "c.CATEGORY_NAME, " +
                "q.QUESTION_TEXT, " +
                "q.OPTION_A, q.OPTION_B, " +
                "q.OPTION_C, q.OPTION_D, " +
                "q.CORRECT_ANSWER " +
                "FROM QUESTIONS q " +
                "LEFT JOIN CATEGORIES c " +
                "ON q.CATEGORY_ID = c.CATEGORY_ID " +
                "WHERE LOWER(q.QUESTION_TEXT) " +
                "LIKE LOWER(?) " +
                "OR LOWER(c.CATEGORY_NAME) " +
                "LIKE LOWER(?) " +
                "ORDER BY q.QUESTION_ID";

        try (
                Connection con =
                        DBConnection.getConnection();

                PreparedStatement pst =
                        con.prepareStatement(sql)
        ) {

            String value =
                    "%" + search + "%";

            pst.setString(
                    1,
                    value
            );

            pst.setString(
                    2,
                    value
            );

            ResultSet rs =
                    pst.executeQuery();

            while (rs.next()) {

                tableModel.addRow(
                        new Object[]{
                                rs.getInt(
                                        "QUESTION_ID"
                                ),

                                rs.getString(
                                        "CATEGORY_NAME"
                                ),

                                rs.getString(
                                        "QUESTION_TEXT"
                                ),

                                rs.getString(
                                        "OPTION_A"
                                ),

                                rs.getString(
                                        "OPTION_B"
                                ),

                                rs.getString(
                                        "OPTION_C"
                                ),

                                rs.getString(
                                        "OPTION_D"
                                ),

                                rs.getString(
                                        "CORRECT_ANSWER"
                                )
                        }
                );
            }

        } catch (SQLException ex) {

            showError(ex);
        }
    }

    // CLEAR

    private void clearFields() {

        txtQuestion.setText("");

        txtOptionA.setText("");

        txtOptionB.setText("");

        txtOptionC.setText("");

        txtOptionD.setText("");

        correctAnswerCombo.setSelectedIndex(0);

        questionTable.clearSelection();
    }

    private void showError(
            Exception ex
    ) {

        JOptionPane.showMessageDialog(
                this,
                "Error:\n"
                        + ex.getMessage()
        );
    }

    public static void main(
            String[] args
    ) {

        SwingUtilities.invokeLater(() -> {

            QuestionFrame frame =
     new QuestionFrame();

            frame.setVisible(true);
        });
    }
}