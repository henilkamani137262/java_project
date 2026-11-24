import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.File;
import java.io.FileOutputStream;
import java.sql.*;

public class ResultManagementFrame extends JFrame {

    private JTable table;
    private DefaultTableModel model;
    private JTextField searchField;
    private String studentName = "Student";
    private int userId = 0;
    private String role = "ADMIN";

    // Keep the no-argument constructor for older code.
    public ResultManagementFrame() {
        this("Admin", 0, "ADMIN");
    }

    // Use this constructor to preserve the current login session.
    public ResultManagementFrame(String studentName, int userId, String role) {
        this.studentName = studentName;
        this.userId = userId;
        this.role = (role == null ? "STUDENT" : role.trim().toUpperCase());

        if (!"ADMIN".equalsIgnoreCase(this.role)) {
            JOptionPane.showMessageDialog(null, "Access denied. Admin only.");
            dispose();
            return;
        }

        setTitle("Result Management");
        setSize(1200, 680);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        createUI();
        loadResults();
    }

    // =====================================================
    // CREATE USER INTERFACE
    // =====================================================

    private void createUI() {

        JPanel mainPanel =
                new JPanel(new BorderLayout(10, 10));

        mainPanel.setBorder(
                BorderFactory.createEmptyBorder(
                        15, 15, 15, 15
                )
        );

        // =================================================
        // TITLE
        // =================================================

        JLabel titleLabel =
                new JLabel(
                        "RESULT MANAGEMENT",
                        SwingConstants.CENTER
                );

        titleLabel.setFont(
                new Font(
                        "Arial",
                        Font.BOLD,
                        28
                )
        );

        titleLabel.setForeground(
                new Color(105, 65, 150)
        );

        mainPanel.add(
                titleLabel,
                BorderLayout.NORTH
        );

        // =================================================
        // SEARCH PANEL
        // =================================================

        JPanel searchPanel =
                new JPanel(
                        new FlowLayout(
                                FlowLayout.LEFT,
                                10,
                                10
                        )
                );

        JLabel searchLabel =
                new JLabel("Search Student:");

        searchLabel.setFont(
                new Font(
                        "Arial",
                        Font.BOLD,
                        14
                )
        );

        searchField =
                new JTextField(20);

        JButton searchButton =
                new JButton("Search");

        JButton refreshButton =
                new JButton("Refresh");

        searchPanel.add(searchLabel);
        searchPanel.add(searchField);
        searchPanel.add(searchButton);
        searchPanel.add(refreshButton);

        mainPanel.add(
                searchPanel,
                BorderLayout.PAGE_START
        );

        // =================================================
        // RESULT TABLE
        // =================================================

        String[] columns = {

                "Result ID",
                "Student",
                "College",
                "Category",
                "Total",
                "Correct",
                "Wrong",
                "Unanswered",
                "Score %",
                "Date"
        };

        model =
                new DefaultTableModel(
                        columns,
                        0
                ) {

                    @Override
                    public boolean isCellEditable(
                            int row,
                            int column) {

                        return false;
                    }
                };

        table =
                new JTable(model);

        table.setRowHeight(28);

        table.setFont(
                new Font(
                        "Arial",
                        Font.PLAIN,
                        13
                )
        );

        table.getTableHeader()
                .setFont(
                        new Font(
                                "Arial",
                                Font.BOLD,
                                13
                        )
                );

        table.setSelectionMode(
                ListSelectionModel.SINGLE_SELECTION
        );

        JScrollPane scrollPane =
                new JScrollPane(table);

        mainPanel.add(
                scrollPane,
                BorderLayout.CENTER
        );

        // =================================================
        // BUTTON PANEL
        // =================================================

        JPanel buttonPanel =
                new JPanel(
                        new FlowLayout(
                                FlowLayout.CENTER,
                                10,
                                10
                        )
                );

        JButton fullResultButton =
                new JButton("Full Result");

        JButton pdfButton =
                new JButton("Generate PDF");

        JButton deleteButton =
                new JButton("Delete Result");

        JButton mainMenuButton =
                new JButton("Main Menu");

        buttonPanel.add(
                fullResultButton
        );

        buttonPanel.add(
                pdfButton
        );

        buttonPanel.add(
                deleteButton
        );

        buttonPanel.add(
                mainMenuButton
        );

        mainPanel.add(
                buttonPanel,
                BorderLayout.SOUTH
        );

        setContentPane(mainPanel);

        // =================================================
        // BUTTON EVENTS
        // =================================================

        searchButton.addActionListener(
                e -> searchResults()
        );

        refreshButton.addActionListener(e -> {

            searchField.setText("");

            loadResults();
        });

        searchField.addActionListener(
                e -> searchResults()
        );

        fullResultButton.addActionListener(
                e -> showFullResult()
        );

        pdfButton.addActionListener(
                e -> generateSelectedPDF()
        );

        deleteButton.addActionListener(
                e -> deleteSelectedResult()
        );

        mainMenuButton.addActionListener(e -> {
            dispose();
            new MainFrame(studentName, userId, role).setVisible(true);
        });
    }

    // =====================================================
    // LOAD ALL RESULTS
    // =====================================================

    private void loadResults() {

        model.setRowCount(0);

        String sql =
                "SELECT " +
                "r.RESULT_ID, " +
                "u.USERNAME, " +
                "u.COLLEGE_NAME, " +
                "c.CATEGORY_NAME, " +
                "r.TOTAL_QUESTIONS, " +
                "r.CORRECT_ANSWERS, " +
                "r.WRONG_ANSWERS, " +
                "GREATEST(NVL(r.TOTAL_QUESTIONS,0) - NVL(r.CORRECT_ANSWERS,0) - NVL(r.WRONG_ANSWERS,0), 0) AS UNANSWERED_QUESTIONS, " +
                "r.SCORE_PERCENTAGE, " +
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

                addRow(rs);
            }

        } catch (SQLException ex) {

            showError(
                    "Error loading results:\n\n"
                            + ex.getMessage()
            );
        }
    }

    // =====================================================
    // ADD ROW TO TABLE
    // =====================================================

    private void addRow(
            ResultSet rs
    ) throws SQLException {

        Object[] row = {

                rs.getInt("RESULT_ID"),

                rs.getString("USERNAME"),

                rs.getString("COLLEGE_NAME"),

                rs.getString("CATEGORY_NAME"),

                rs.getInt("TOTAL_QUESTIONS"),

                rs.getInt("CORRECT_ANSWERS"),

                rs.getInt("WRONG_ANSWERS"),

                rs.getInt("UNANSWERED_QUESTIONS"),

                rs.getDouble("SCORE_PERCENTAGE"),

                rs.getTimestamp("RESULT_DATE")
        };

        model.addRow(row);
    }

    // =====================================================
    // SEARCH RESULTS
    // =====================================================

    private void searchResults() {

        String search =
                searchField.getText().trim();

        if (search.isEmpty()) {

            loadResults();

            return;
        }

        model.setRowCount(0);

        String sql =
                "SELECT " +
                "r.RESULT_ID, " +
                "u.USERNAME, " +
                "u.COLLEGE_NAME, " +
                "c.CATEGORY_NAME, " +
                "r.TOTAL_QUESTIONS, " +
                "r.CORRECT_ANSWERS, " +
                "r.WRONG_ANSWERS, " +
                "GREATEST(NVL(r.TOTAL_QUESTIONS,0) - NVL(r.CORRECT_ANSWERS,0) - NVL(r.WRONG_ANSWERS,0), 0) AS UNANSWERED_QUESTIONS, " +
                "r.SCORE_PERCENTAGE, " +
                "r.RESULT_DATE " +

                "FROM RESULTS r " +

                "LEFT JOIN USERS u " +
                "ON r.USER_ID = u.USER_ID " +

                "LEFT JOIN CATEGORIES c " +
                "ON r.CATEGORY_ID = c.CATEGORY_ID " +

                "WHERE LOWER(u.USERNAME) LIKE LOWER(?) " +

                "ORDER BY r.RESULT_DATE DESC";

        try (
                Connection con =
                        DBConnection.getConnection();

                PreparedStatement ps =
                        con.prepareStatement(sql)
        ) {

            ps.setString(
                    1,
                    "%" + search + "%"
            );

            try (
                    ResultSet rs =
                            ps.executeQuery()
            ) {

                while (rs.next()) {

                    addRow(rs);
                }
            }

        } catch (SQLException ex) {

            showError(
                    "Search Error:\n\n"
                            + ex.getMessage()
            );
        }
    }

    // =====================================================
    // GET SELECTED RESULT
    // =====================================================

    private ResultData getSelectedResult() {

        int row =
                table.getSelectedRow();

        if (row == -1) {

            JOptionPane.showMessageDialog(
                    this,
                    "Please select a result first.",
                    "Select Result",
                    JOptionPane.WARNING_MESSAGE
            );

            return null;
        }

        ResultData data =
                new ResultData();

        data.resultId =
                Integer.parseInt(
                        table.getValueAt(
                                row,
                                0
                        ).toString()
                );

        data.student =
                getValue(row, 1);

        data.college =
                getValue(row, 2);

        data.category =
                getValue(row, 3);

        data.total =
                Integer.parseInt(
                        table.getValueAt(
                                row,
                                4
                        ).toString()
                );

        data.correct =
                Integer.parseInt(
                        table.getValueAt(
                                row,
                                5
                        ).toString()
                );

        data.wrong =
                Integer.parseInt(
                        table.getValueAt(
                                row,
                                6
                        ).toString()
                );

        data.score =
                Double.parseDouble(
                        table.getValueAt(
                                row,
                                8
                        ).toString()
                );

        data.unanswered = Integer.parseInt(table.getValueAt(row, 7).toString());

        data.date =
                getValue(row, 9);

        return data;
    }

    // =====================================================
    // GET TABLE VALUE
    // =====================================================

    private String getValue(
            int row,
            int column
    ) {

        Object value =
                table.getValueAt(
                        row,
                        column
                );

        if (value == null) {
            return "";
        }

        return value.toString();
    }

    // =====================================================
    // SHOW FULL RESULT
    // =====================================================

    private void showFullResult() {

        ResultData r =
                getSelectedResult();

        if (r == null) {
            return;
        }

        String resultText =

                "========================================\n"
                + "              QUIZ RESULT\n"
                + "========================================\n\n"

                + "Result ID       : "
                + r.resultId + "\n"

                + "Student Name    : "
                + r.student + "\n"

                + "College Name    : "
                + r.college + "\n"

                + "Category        : "
                + r.category + "\n\n"

                + "Total Questions : "
                + r.total + "\n"

                + "Correct Answers : "
                + r.correct + "\n"

                + "Wrong Answers   : "
                + r.wrong + "\n"

                + "Unanswered Questions : "
                + r.unanswered + "\n"

                + "Score           : "
                + r.score + "%\n"

                + "Result Date     : "
                + r.date + "\n\n"

                + "========================================";

        JTextArea area =
                new JTextArea(
                        resultText
                );

        area.setFont(
                new Font(
                        "Monospaced",
                        Font.PLAIN,
                        14
                )
        );

        area.setEditable(false);

        JOptionPane.showMessageDialog(
                this,
                area,
                "Full Result",
                JOptionPane.INFORMATION_MESSAGE
        );
    }

    // =====================================================
    // GENERATE PDF
    // =====================================================

    private void generateSelectedPDF() {

        ResultData r =
                getSelectedResult();

        if (r == null) {
            return;
        }

        try {

            File pdf =
                    createPDF(r);

            JOptionPane.showMessageDialog(
                    this,
                    "Result PDF generated successfully!\n\n"
                            + "Location:\n"
                            + pdf.getAbsolutePath(),
                    "PDF Generated",
                    JOptionPane.INFORMATION_MESSAGE
            );

        } catch (Exception ex) {

            showError(
                    "PDF generation failed:\n\n"
                            + ex.getMessage()
            );
        }
    }

    // =====================================================
    // CREATE PDF
    // =====================================================

    private File createPDF(
            ResultData r
    ) throws Exception {

        File downloads =
                new File(
                        System.getProperty(
                                "user.home"
                        )
                                + File.separator
                                + "Downloads"
                );

        if (!downloads.exists()) {

            downloads.mkdirs();
        }

        File file =
                new File(
                        downloads,
                        "Quiz_Result_"
                                + r.resultId
                                + ".pdf"
                );

        String text =

                "QUIZ APPLICATION\n\n"

                + "========================================\n"

                + "                 QUIZ RESULT\n"

                + "========================================\n\n"

                + "Result ID       : "
                + r.resultId + "\n"

                + "Student Name    : "
                + r.student + "\n"

                + "College Name    : "
                + r.college + "\n"

                + "Category        : "
                + r.category + "\n"

                + "----------------------------------------\n"

                + "Total Questions : "
                + r.total + "\n"

                + "Correct Answers : "
                + r.correct + "\n"

                + "Wrong Answers   : "
                + r.wrong + "\n"

                + "Unanswered Questions : "
                + r.unanswered + "\n"

                + "Score           : "
                + r.score + "%\n"

                + "Result Date     : "
                + r.date + "\n"

                + "========================================\n\n"

                + "Thank you for participating in the Quiz.\n";

        createSimplePDF(
                file,
                text
        );

        return file;
    }

    // =====================================================
    // SIMPLE PDF CREATOR
    // =====================================================

    private void createSimplePDF(
            File file,
            String text
    ) throws Exception {

        String[] lines =
                text.split("\n");

        StringBuilder stream =
                new StringBuilder();

        stream.append("BT\n");

        stream.append(
                "/F1 12 Tf\n"
        );

        stream.append(
                "50 780 Td\n"
        );

        for (String line : lines) {

           String safeLine =
        line.replace(
                "\\",
                "\\\\"
        )
        .replace(
                "(",
                "\\("
        )
        .replace(
                ")",
                "\\)"
        );

        stream.append("(")
        .append(safeLine)
        .append(") Tj\n");
        
            stream.append(
                    "0 -18 Td\n"
            );
        }

        stream.append("ET\n");

        byte[] streamBytes =
                stream.toString()
                        .getBytes(
                                "ISO-8859-1"
                        );

        StringBuilder pdf =
                new StringBuilder();

        pdf.append(
                "%PDF-1.4\n"
        );

        int[] offsets =
                new int[6];

        // Object 1
        offsets[1] =
                pdf.length();

        pdf.append(
                "1 0 obj\n"
                + "<< /Type /Catalog /Pages 2 0 R >>\n"
                + "endobj\n"
        );

        // Object 2
        offsets[2] =
                pdf.length();

        pdf.append(
                "2 0 obj\n"
                + "<< /Type /Pages "
                + "/Kids [3 0 R] "
                + "/Count 1 >>\n"
                + "endobj\n"
        );

        // Object 3
        offsets[3] =
                pdf.length();

        pdf.append(
                "3 0 obj\n"
                + "<< /Type /Page "
                + "/Parent 2 0 R "
                + "/MediaBox [0 0 595 842] "
                + "/Resources << "
                + "/Font << /F1 4 0 R >> "
                + ">> "
                + "/Contents 5 0 R >>\n"
                + "endobj\n"
        );

        // Object 4
        offsets[4] =
                pdf.length();

        pdf.append(
                "4 0 obj\n"
                + "<< /Type /Font "
                + "/Subtype /Type1 "
                + "/BaseFont /Helvetica >>\n"
                + "endobj\n"
        );

        // Object 5
        offsets[5] =
                pdf.length();

        pdf.append(
                "5 0 obj\n"
                + "<< /Length "
                + streamBytes.length
                + " >>\n"
                + "stream\n"
        );

        pdf.append(stream);

        pdf.append(
                "endstream\n"
                + "endobj\n"
        );

        int xrefPosition =
                pdf.length();

        pdf.append(
                "xref\n"
        );

        pdf.append(
                "0 6\n"
        );

        pdf.append(
                "0000000000 65535 f \n"
        );

        for (int i = 1; i <= 5; i++) {

            pdf.append(
                    String.format(
                            "%010d 00000 n \n",
                            offsets[i]
                    )
            );
        }

        pdf.append(
                "trailer\n"
        );

        pdf.append(
                "<< /Size 6 "
                + "/Root 1 0 R >>\n"
        );

        pdf.append(
                "startxref\n"
        );

        pdf.append(
                xrefPosition
        );

        pdf.append(
                "\n%%EOF"
        );

        try (
                FileOutputStream fos =
                        new FileOutputStream(
                                file
                        )
        ) {

            fos.write(
                    pdf.toString()
                            .getBytes(
                                    "ISO-8859-1"
                            )
            );
        }
    }

    // =====================================================
    // DELETE RESULT
    // =====================================================

    private void deleteSelectedResult() {

        ResultData r =
                getSelectedResult();

        if (r == null) {
            return;
        }

        int confirm =
                JOptionPane.showConfirmDialog(
                        this,

                        "Are you sure you want to delete this result?\n\n"
                                + "Result ID: "
                                + r.resultId
                                + "\n"
                                + "Student: "
                                + r.student,

                        "Confirm Delete",

                        JOptionPane.YES_NO_OPTION,

                        JOptionPane.WARNING_MESSAGE
                );

        if (
                confirm
                        != JOptionPane.YES_OPTION
        ) {
            return;
        }

        String sql =
                "DELETE FROM RESULTS "
                        + "WHERE RESULT_ID = ?";

        try (
                Connection con =
                        DBConnection.getConnection();

                PreparedStatement ps =
                        con.prepareStatement(sql)
        ) {

            ps.setInt(
                    1,
                    r.resultId
            );

            int deleted =
                    ps.executeUpdate();

            if (deleted > 0) {

                JOptionPane.showMessageDialog(
                        this,
                        "Result deleted successfully.",
                        "Delete Result",
                        JOptionPane.INFORMATION_MESSAGE
                );

                loadResults();
            }

        } catch (SQLException ex) {

            showError(
                    "Delete Error:\n\n"
                            + ex.getMessage()
            );
        }
    }

    // =====================================================
    // ERROR MESSAGE
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
    // RESULT DATA CLASS
    // =====================================================

    private static class ResultData {

        int resultId;

        String student;
        String college;
        String category;

        int total;
        int correct;
        int wrong;
        int unanswered;

        double score;

        String date;
    }

    // =====================================================
    // MAIN
    // =====================================================

    public static void main(
            String[] args
    ) {

        SwingUtilities.invokeLater(() -> {

            new ResultManagementFrame()
                    .setVisible(true);
        });
    }
}