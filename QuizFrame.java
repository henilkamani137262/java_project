import javax.swing.*;
import java.awt.*;
import java.sql.*;
import java.util.ArrayList;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class QuizFrame extends JFrame {

    // =====================================================
    // USER INFORMATION
    // =====================================================

    private String studentName;
    private int userId;
    private String role = "STUDENT";

    // =====================================================
    // COLORS
    // =====================================================

    private final Color backgroundColor =
            new Color(248, 240, 255);

    private final Color purpleColor =
            new Color(186, 155, 220);

    private final Color darkPurple =
            new Color(110, 75, 145);

    private final Color pinkColor =
            new Color(248, 205, 220);

    private final Color textColor =
            new Color(70, 50, 80);

    // =====================================================
    // COMPONENTS
    // =====================================================

    private JComboBox<String> categoryCombo;

    private JLabel lblQuestionNumber;

    private JTextArea txtQuestion;

    private JRadioButton optionA;
    private JRadioButton optionB;
    private JRadioButton optionC;
    private JRadioButton optionD;

    private ButtonGroup optionGroup;

    private JButton startButton;
    private JButton nextButton;
    private JButton mainMenuButton;

    // =====================================================
    // DATA
    // =====================================================

    private ArrayList<Integer> categoryIds =
            new ArrayList<>();

    private ArrayList<Question> questions =
            new ArrayList<>();

    private int currentQuestionIndex = 0;

    private int score = 0;
    private int answeredQuestions = 0;
    private boolean quizStarted = false;
    private boolean resultSaved = false;

    // =====================================================
    // DEFAULT CONSTRUCTOR
    // =====================================================

    public QuizFrame() {
        this("Student", 0, "STUDENT");
    }

    // =====================================================
    // USER CONSTRUCTOR
    // =====================================================

    public QuizFrame(String studentName, int userId) {
        this(studentName, userId, "STUDENT");
    }

    public QuizFrame(String studentName, int userId, String role) {

        this.studentName = studentName;
        this.userId = userId;
        this.role = (role == null || role.trim().isEmpty()) ? "STUDENT" : role.trim().toUpperCase();

        setTitle(
                "Quiz Application - "
                        + studentName
        );

        setSize(900, 700);

        setLocationRelativeTo(null);

        // Confirmation is handled manually so No really keeps the quiz open.
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);

        createUI();
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                returnToMainMenu();
            }
        });

        loadCategories();
    }

    // =====================================================
    // CREATE UI
    // =====================================================

    private void createUI() {

        JPanel mainPanel =
                new JPanel(
                        new BorderLayout(
                                15,
                                15
                        )
                );

        mainPanel.setBackground(
                backgroundColor
        );

        mainPanel.setBorder(
                BorderFactory.createEmptyBorder(
                        25,
                        30,
                        25,
                        30
                )
        );

        // =================================================
        // TITLE
        // =================================================

        JLabel title =
                new JLabel(
                        "QUIZ APPLICATION",
                        SwingConstants.CENTER
                );

        title.setFont(
                new Font(
                        "Arial",
                        Font.BOLD,
                        28
                )
        );

        title.setForeground(
                darkPurple
        );

        mainPanel.add(
                title,
                BorderLayout.NORTH
        );

        // =================================================
        // CENTER
        // =================================================

        JPanel centerPanel =
                new JPanel();

        centerPanel.setLayout(
                new BoxLayout(
                        centerPanel,
                        BoxLayout.Y_AXIS
                )
        );

        centerPanel.setBackground(
                backgroundColor
        );

        // =================================================
        // STUDENT
        // =================================================

        JLabel studentLabel =
                new JLabel(
                        "Student: "
                                + studentName
                );

        studentLabel.setAlignmentX(
                Component.CENTER_ALIGNMENT
        );

        studentLabel.setFont(
                new Font(
                        "Arial",
                        Font.BOLD,
                        16
                )
        );

        studentLabel.setForeground(
                darkPurple
        );

        centerPanel.add(studentLabel);

        centerPanel.add(
                Box.createVerticalStrut(10)
        );

        // =================================================
        // CATEGORY PANEL
        // =================================================

        JPanel categoryPanel =
                new JPanel(
                        new FlowLayout(
                                FlowLayout.CENTER,
                                15,
                                10
                        )
                );

        categoryPanel.setBackground(
                pinkColor
        );

        JLabel categoryLabel =
                new JLabel(
                        "Select Category:"
                );

        categoryLabel.setFont(
                new Font(
                        "Arial",
                        Font.BOLD,
                        15
                )
        );

        categoryCombo =
                new JComboBox<>();

        categoryCombo.setPreferredSize(
                new Dimension(
                        250,
                        30
                )
        );

        startButton =
                new JButton(
                        "Start Quiz"
                );

        styleButton(startButton);

        categoryPanel.add(
                categoryLabel
        );

        categoryPanel.add(
                categoryCombo
        );

        categoryPanel.add(
                startButton
        );

        centerPanel.add(
                categoryPanel
        );

        centerPanel.add(
                Box.createVerticalStrut(20)
        );

        // =================================================
        // QUESTION NUMBER
        // =================================================

        lblQuestionNumber =
                new JLabel(
                        "Question: 0 / 0"
                );

        lblQuestionNumber.setAlignmentX(
                Component.CENTER_ALIGNMENT
        );

        lblQuestionNumber.setFont(
                new Font(
                        "Arial",
                        Font.BOLD,
                        16
                )
        );

        lblQuestionNumber.setForeground(
                darkPurple
        );

        centerPanel.add(
                lblQuestionNumber
        );

        centerPanel.add(
                Box.createVerticalStrut(15)
        );

        // =================================================
        // QUESTION PANEL
        // =================================================

        JPanel questionPanel =
                new JPanel(
                        new BorderLayout()
                );

        questionPanel.setBackground(
                Color.WHITE
        );

        questionPanel.setBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(
                                purpleColor,
                                2
                        ),
                        BorderFactory.createEmptyBorder(
                                15,
                                15,
                                15,
                                15
                        )
                )
        );

        questionPanel.setMaximumSize(
                new Dimension(
                        800,
                        150
                )
        );

        txtQuestion =
                new JTextArea();

        txtQuestion.setFont(
                new Font(
                        "Arial",
                        Font.BOLD,
                        17
                )
        );

        txtQuestion.setForeground(
                textColor
        );

        txtQuestion.setLineWrap(true);

        txtQuestion.setWrapStyleWord(true);

        txtQuestion.setEditable(false);

        txtQuestion.setBackground(
                Color.WHITE
        );

        JScrollPane questionScroll =
                new JScrollPane(
                        txtQuestion
                );

        questionPanel.add(
                questionScroll,
                BorderLayout.CENTER
        );

        centerPanel.add(
                questionPanel
        );

        centerPanel.add(
                Box.createVerticalStrut(20)
        );

        // =================================================
        // OPTIONS
        // =================================================

        JPanel optionsPanel =
                new JPanel(
                        new GridLayout(
                                4,
                                1,
                                10,
                                10
                        )
                );

        optionsPanel.setBackground(
                backgroundColor
        );

        optionsPanel.setMaximumSize(
                new Dimension(
                        750,
                        220
                )
        );

        optionA =
                new JRadioButton();

        optionB =
                new JRadioButton();

        optionC =
                new JRadioButton();

        optionD =
                new JRadioButton();

        styleOption(optionA);
        styleOption(optionB);
        styleOption(optionC);
        styleOption(optionD);

        optionGroup =
                new ButtonGroup();

        optionGroup.add(optionA);
        optionGroup.add(optionB);
        optionGroup.add(optionC);
        optionGroup.add(optionD);

        optionsPanel.add(optionA);
        optionsPanel.add(optionB);
        optionsPanel.add(optionC);
        optionsPanel.add(optionD);

        centerPanel.add(optionsPanel);

        mainPanel.add(
                centerPanel,
                BorderLayout.CENTER
        );

        // =================================================
        // BUTTONS
        // =================================================

        JPanel buttonPanel =
                new JPanel(
                        new FlowLayout(
                                FlowLayout.CENTER,
                                15,
                                10
                        )
                );

        buttonPanel.setBackground(
                backgroundColor
        );

        nextButton =
                new JButton(
                        "Next Question"
                );

        mainMenuButton =
                new JButton(
                        "Main Menu"
                );

        styleButton(nextButton);
        styleButton(mainMenuButton);

        nextButton.setEnabled(false);

        buttonPanel.add(nextButton);
        buttonPanel.add(mainMenuButton);

        mainPanel.add(
                buttonPanel,
                BorderLayout.SOUTH
        );

        add(mainPanel);

        // =================================================
        // EVENTS
        // =================================================

        startButton.addActionListener(
                e -> startQuiz()
        );

        nextButton.addActionListener(
                e -> nextQuestion()
        );

        mainMenuButton.addActionListener(e -> returnToMainMenu());

        enableOptions(false);
    }

    private void returnToMainMenu() {
        if (quizStarted && !resultSaved) {
            int choice = JOptionPane.showConfirmDialog(
                    this,
                    "Are you sure you want to exit the quiz?\\n\\n"
                            + "Your progress so far will be saved as INCOMPLETE.",
                    "Exit Quiz?",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.QUESTION_MESSAGE
            );

            if (choice != JOptionPane.YES_OPTION) {
                // No: keep this frame and all current selections/progress unchanged.
                return;
            }

            // Include the currently selected answer, if there is one.
            // It has not been counted yet because Next Question was not pressed.
            String selectedAnswer = getSelectedAnswer();
            if (selectedAnswer != null
                    && currentQuestionIndex < questions.size()) {
                Question current = questions.get(currentQuestionIndex);
                if (selectedAnswer.equalsIgnoreCase(current.correctAnswer)) {
                    score++;
                }
                answeredQuestions++;
            }

            int totalQuestions = questions.size();
            int wrongAnswers = answeredQuestions - score;
            double percentage = totalQuestions == 0
                    ? 0.0
                    : ((double) score / totalQuestions) * 100.0;

            saveResult(totalQuestions, score, wrongAnswers, percentage,
                    answeredQuestions, "INCOMPLETE");
            resultSaved = true;
            quizStarted = false;

            int unansweredQuestions = totalQuestions - answeredQuestions;
            JOptionPane.showMessageDialog(
                    this,
                    "INCOMPLETE QUIZ SAVED\n\n"
                            + "Student: " + studentName + "\n"
                            + "Total Questions: " + totalQuestions + "\n"
                            + "Answered Questions: " + answeredQuestions + "\n"
                            + "Unanswered Questions: " + unansweredQuestions + "\n"
                            + "Correct Answers: " + score + "\n"
                            + "Wrong Answers: " + wrongAnswers + "\n"
                            + "Status: INCOMPLETE",
                    "Quiz Progress Saved",
                    JOptionPane.INFORMATION_MESSAGE
            );
        }

        dispose();
        MainFrame frame = new MainFrame(studentName, userId, role);
        frame.setVisible(true);
    }

    // =====================================================
    // STYLE BUTTON
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
                        14
                )
        );

        button.setFocusPainted(false);
    }

    // =====================================================
    // STYLE OPTION
    // =====================================================

    private void styleOption(
            JRadioButton option
    ) {

        option.setBackground(
                backgroundColor
        );

        option.setForeground(
                textColor
        );

        option.setFont(
                new Font(
                        "Arial",
                        Font.PLAIN,
                        15
                )
        );

        option.setFocusPainted(false);
    }

    // =====================================================
    // LOAD CATEGORIES
    // =====================================================

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

                categoryIds.add(
                        rs.getInt(
                                "CATEGORY_ID"
                        )
                );

                categoryCombo.addItem(
                        rs.getString(
                                "CATEGORY_NAME"
                        )
                );
            }

        } catch (SQLException ex) {

            showError(
                    "Error loading categories:\n"
                            + ex.getMessage()
            );
        }
    }

    // =====================================================
    // START QUIZ
    // =====================================================

    private void startQuiz() {

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

        if (userId <= 0) {

            JOptionPane.showMessageDialog(
                    this,
                    "Student User ID not found.\n"
                            + "Please login again.",
                    "Login Error",
                    JOptionPane.ERROR_MESSAGE
            );

            return;
        }

        int selectedIndex =
                categoryCombo.getSelectedIndex();

        int categoryId =
                categoryIds.get(
                        selectedIndex
                );

        loadQuestions(categoryId);

        if (questions.isEmpty()) {

            JOptionPane.showMessageDialog(
                    this,
                    "No questions available in this category."
            );

            return;
        }

        currentQuestionIndex = 0;
        score = 0;
        answeredQuestions = 0;
        resultSaved = false;
        quizStarted = true;

        categoryCombo.setEnabled(false);

        startButton.setEnabled(false);

        nextButton.setEnabled(true);

        enableOptions(true);

        showQuestion();
    }

    // =====================================================
    // LOAD QUESTIONS
    // =====================================================

    private void loadQuestions(
            int categoryId
    ) {

        questions.clear();

        String sql =
                "SELECT QUESTION_ID, " +
                "QUESTION_TEXT, " +
                "OPTION_A, " +
                "OPTION_B, " +
                "OPTION_C, " +
                "OPTION_D, " +
                "CORRECT_ANSWER " +
                "FROM QUESTIONS " +
                "WHERE CATEGORY_ID = ? " +
                "ORDER BY QUESTION_ID";

        try (
                Connection con =
                        DBConnection.getConnection();

                PreparedStatement ps =
                        con.prepareStatement(sql)
        ) {

            ps.setInt(
                    1,
                    categoryId
            );

            try (
                    ResultSet rs =
                            ps.executeQuery()
            ) {

                while (rs.next()) {

                    Question q =
                            new Question();

                    q.questionId =
                            rs.getInt(
                                    "QUESTION_ID"
                            );

                    q.questionText =
                            rs.getString(
                                    "QUESTION_TEXT"
                            );

                    q.optionA =
                            rs.getString(
                                    "OPTION_A"
                            );

                    q.optionB =
                            rs.getString(
                                    "OPTION_B"
                            );

                    q.optionC =
                            rs.getString(
                                    "OPTION_C"
                            );

                    q.optionD =
                            rs.getString(
                                    "OPTION_D"
                            );

                    q.correctAnswer =
                            rs.getString(
                                    "CORRECT_ANSWER"
                            );

                    questions.add(q);
                }
            }

        } catch (SQLException ex) {

            showError(
                    "Error loading questions:\n"
                            + ex.getMessage()
            );
        }
    }

    // =====================================================
    // SHOW QUESTION
    // =====================================================

    private void showQuestion() {

        if (
                currentQuestionIndex
                        >= questions.size()
        ) {

            showResult();

            return;
        }

        Question q =
                questions.get(
                        currentQuestionIndex
                );

        lblQuestionNumber.setText(
                "Question "
                        + (
                        currentQuestionIndex + 1
                )
                        + " of "
                        + questions.size()
        );

        txtQuestion.setText(
                q.questionText
        );

        optionA.setText(
                "A. " + q.optionA
        );

        optionB.setText(
                "B. " + q.optionB
        );

        optionC.setText(
                "C. " + q.optionC
        );

        optionD.setText(
                "D. " + q.optionD
        );

        optionGroup.clearSelection();

        if (
                currentQuestionIndex
                        == questions.size() - 1
        ) {

            nextButton.setText(
                    "Finish Quiz"
            );

        } else {

            nextButton.setText(
                    "Next Question"
            );
        }
    }

    // =====================================================
    // NEXT QUESTION
    // =====================================================

    private void nextQuestion() {

        String selectedAnswer =
                getSelectedAnswer();

        if (selectedAnswer == null) {

            JOptionPane.showMessageDialog(
                    this,
                    "Please select an answer."
            );

            return;
        }

        Question q =
                questions.get(
                        currentQuestionIndex
                );

        if (selectedAnswer.equalsIgnoreCase(q.correctAnswer)) {
            score++;
        }

        answeredQuestions++;
        currentQuestionIndex++;

        showQuestion();
    }

    // =====================================================
    // GET ANSWER
    // =====================================================

    private String getSelectedAnswer() {

        if (optionA.isSelected()) {
            return "A";
        }

        if (optionB.isSelected()) {
            return "B";
        }

        if (optionC.isSelected()) {
            return "C";
        }

        if (optionD.isSelected()) {
            return "D";
        }

        return null;
    }

    // =====================================================
    // SHOW RESULT
    // =====================================================

    private void showResult() {

        enableOptions(false);

        nextButton.setEnabled(false);

        int totalQuestions =
                questions.size();

        int wrongAnswers =
                answeredQuestions - score;

        int unansweredQuestions = totalQuestions - answeredQuestions;

        double percentage =
                totalQuestions == 0
                        ? 0
                        : (
                        (double) score
                                / totalQuestions
                ) * 100;

        // SAVE RESULT

        saveResult(
                totalQuestions,
                score,
                wrongAnswers,
                percentage,
                answeredQuestions,
                "COMPLETED"
        );
        resultSaved = true;
        quizStarted = false;

        String message =
                "QUIZ COMPLETED\n\n"
                        + "Student: "
                        + studentName
                        + "\n\n"
                        + "Total Questions: "
                        + totalQuestions
                        + "\n"
                        + "Correct Answers: "
                        + score
                        + "\n"
                        + "Wrong Answers: "
                        + wrongAnswers
                        + "\n"
                        + "Unanswered Questions: "
                        + unansweredQuestions
                        + "\n"
                        + "Score: "
                        + String.format(
                                "%.2f",
                                percentage
                        )
                        + "%";

        JOptionPane.showMessageDialog(
                this,
                message,
                "Quiz Result",
                JOptionPane.INFORMATION_MESSAGE
        );

        txtQuestion.setText(
                "Quiz Completed!"
        );

        lblQuestionNumber.setText(
                "Final Score: "
                        + score
                        + " / "
                        + totalQuestions
        );

        categoryCombo.setEnabled(true);

        startButton.setEnabled(true);

        nextButton.setText(
                "Next Question"
        );
    }

    // =====================================================
    // SAVE RESULT
    // =====================================================

    private void saveResult(
            int totalQuestions,
            int correctAnswers,
            int wrongAnswers,
            double percentage,
            int answeredQuestions,
            String status
    ) {

        if (userId <= 0) {

            JOptionPane.showMessageDialog(
                    this,
                    "User ID is missing.\n"
                            + "Result cannot be saved.",
                    "Result Error",
                    JOptionPane.ERROR_MESSAGE
            );

            return;
        }

        int selectedIndex =
                categoryCombo.getSelectedIndex();

        if (selectedIndex == -1) {
            return;
        }

        int categoryId =
                categoryIds.get(
                        selectedIndex
                );

        // =================================================
        // IMPORTANT:
        // USER_ID is now saved in RESULTS
        // =================================================

        String sql =
                "INSERT INTO RESULTS (" +
                "RESULT_ID, USER_ID, CATEGORY_ID, STUDENT_NAME, " +
                "TOTAL_QUESTIONS, ANSWERED_QUESTIONS, " +
                "CORRECT_ANSWERS, WRONG_ANSWERS, SCORE_PERCENTAGE, " +
                "STATUS, RESULT_DATE" +
                ") VALUES (" +
                "RESULT_SEQ.NEXTVAL, ?, ?, ?, ?, ?, ?, ?, ?, ?, SYSDATE" +
                ")";

        try (
                Connection con =
                        DBConnection.getConnection();

                PreparedStatement ps =
                        con.prepareStatement(sql)
        ) {

            ps.setInt(1, userId);
            ps.setInt(2, categoryId);
            ps.setString(3, studentName);
            ps.setInt(4, totalQuestions);
            ps.setInt(5, answeredQuestions);
            ps.setInt(6, correctAnswers);
            ps.setInt(7, wrongAnswers);
            ps.setDouble(8, percentage);
            ps.setString(9, status);

            ps.executeUpdate();

        } catch (SQLException ex) {

            showError(
                    "Error saving result:\n"
                            + ex.getMessage()
            );
        }
    }

    // =====================================================
    // ENABLE OPTIONS
    // =====================================================

    private void enableOptions(
            boolean enabled
    ) {

        optionA.setEnabled(enabled);
        optionB.setEnabled(enabled);
        optionC.setEnabled(enabled);
        optionD.setEnabled(enabled);
    }

    // =====================================================
    // ERROR
    // =====================================================

    private void showError(
            String message
    ) {

        JOptionPane.showMessageDialog(
                this,
                message,
                "Error",
                JOptionPane.ERROR_MESSAGE
        );
    }

    // =====================================================
    // QUESTION CLASS
    // =====================================================

    private static class Question {

        int questionId;

        String questionText;

        String optionA;

        String optionB;

        String optionC;

        String optionD;

        String correctAnswer;
    }

    // =====================================================
    // MAIN
    // =====================================================

    public static void main(
            String[] args
    ) {

        SwingUtilities.invokeLater(
                () -> {

                    QuizFrame frame =
                            new QuizFrame();

                    frame.setVisible(true);
                }
        );
    }
}