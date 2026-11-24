import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class CategoryFrame extends JFrame {

    private String studentName = "Student";
    private int userId = 0;
    private String role = "STUDENT";

    // =========================
    // PASTEL PURPLE THEME
    // =========================

    Color backgroundColor =
            new Color(245, 240, 255);

    Color cardColor =
            new Color(252, 250, 255);

    Color primaryColor =
            new Color(142, 118, 181);

    Color darkPurple =
            new Color(92, 72, 130);

    Color textColor =
            new Color(55, 45, 70);

    Color borderColor =
            new Color(215, 205, 230);

    // =========================
    // COMPONENTS
    // =========================

    JTextField txtCategoryId;
    JTextField txtCategoryName;
    JTextField txtSearch;

    JTable categoryTable;
    DefaultTableModel tableModel;

    // =========================
    // CONSTRUCTOR
    // =========================

    public CategoryFrame() {
        this("Student", 0, "STUDENT");
    }

    public CategoryFrame(String studentName, int userId, String role) {
        this.studentName = studentName;
        this.userId = userId;
        this.role = (role == null || role.trim().isEmpty()) ? "STUDENT" : role.trim().toUpperCase();

        setTitle(
                "Category Management"
        );

        setSize(
                950,
                650
        );

        setLocationRelativeTo(null);

        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            @Override public void windowClosing(WindowEvent e) { returnToMainMenu(); }
        });

        // =========================
        // MAIN PANEL
        // =========================

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

        // =========================
        // TITLE
        // =========================

        JLabel title =
                new JLabel(
                        "CATEGORY MANAGEMENT",
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
                darkPurple
        );

        mainPanel.add(
                title,
                BorderLayout.NORTH
        );

        // =========================
        // CENTER PANEL
        // =========================

        JPanel centerPanel =
                new JPanel(
                        new BorderLayout(
                                15,
                                15
                        )
                );

        centerPanel.setBackground(
                backgroundColor
        );

        // =========================
        // FORM PANEL
        // =========================

        JPanel formPanel =
                new JPanel(
                        new GridBagLayout()
                );

        formPanel.setBackground(
                cardColor
        );

        formPanel.setBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(
                                borderColor
                        ),
                        BorderFactory.createEmptyBorder(
                                15,
                                20,
                                15,
                                20
                        )
                )
        );

        GridBagConstraints gbc =
                new GridBagConstraints();

        gbc.insets =
                new Insets(
                        8,
                        10,
                        8,
                        10
                );

        gbc.fill =
                GridBagConstraints.HORIZONTAL;

        // =========================
        // CATEGORY ID
        // =========================

        JLabel idLabel =
                createLabel(
                        "Category ID:"
                );

        txtCategoryId =
                new JTextField();

        txtCategoryId.setEditable(
                false
        );

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 0.2;

        formPanel.add(
                idLabel,
                gbc
        );

        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.weightx = 0.8;

        formPanel.add(
                txtCategoryId,
                gbc
        );

        // =========================
        // CATEGORY NAME
        // =========================

        JLabel nameLabel =
                createLabel(
                        "Category Name:"
                );

        txtCategoryName =
                new JTextField();

        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.weightx = 0.2;

        formPanel.add(
                nameLabel,
                gbc
        );

        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.weightx = 0.8;

        formPanel.add(
                txtCategoryName,
                gbc
        );

        // =========================
        // BUTTONS
        // =========================

        JPanel formButtonPanel =
                new JPanel(
                        new FlowLayout(
                                FlowLayout.CENTER,
                                10,
                                5
                        )
                );

        formButtonPanel.setBackground(
                cardColor
        );

        JButton addButton =
                new JButton(
                        "Add Category"
                );

        JButton updateButton =
                new JButton(
                        "Update Category"
                );

        JButton deleteButton =
                new JButton(
                        "Delete Category"
                );

        JButton clearButton =
                new JButton(
                        "Clear"
                );

        styleButton(
                addButton
        );

        styleButton(
                updateButton
        );

        styleButton(
                deleteButton
        );

        styleButton(
                clearButton
        );

        formButtonPanel.add(
                addButton
        );

        formButtonPanel.add(
                updateButton
        );

        formButtonPanel.add(
                deleteButton
        );

        formButtonPanel.add(
                clearButton
        );

        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 2;

        formPanel.add(
                formButtonPanel,
                gbc
        );

        centerPanel.add(
                formPanel,
                BorderLayout.NORTH
        );

        // =========================
        // TABLE
        // =========================

        tableModel =
                new DefaultTableModel(
                        new Object[]{
                                "Category ID",
                                "Category Name"
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

        categoryTable =
                new JTable(
                        tableModel
                );

        categoryTable.setRowHeight(
                25
        );

        categoryTable
                .getTableHeader()
                .setBackground(
                        primaryColor
                );

        categoryTable
                .getTableHeader()
                .setForeground(
                        Color.WHITE
                );

        categoryTable
                .getTableHeader()
                .setFont(
                        new Font(
                                "Arial",
                                Font.BOLD,
                                13
                        )
                );

        JScrollPane scrollPane =
                new JScrollPane(
                        categoryTable
                );

        centerPanel.add(
                scrollPane,
                BorderLayout.CENTER
        );

        mainPanel.add(
                centerPanel,
                BorderLayout.CENTER
        );

        // =========================
        // BOTTOM PANEL
        // =========================

        JPanel bottomPanel =
                new JPanel(
                        new BorderLayout(
                                10,
                                10
                        )
                );

        bottomPanel.setBackground(
                backgroundColor
        );

        // =========================
        // SEARCH PANEL
        // =========================

        JPanel searchPanel =
                new JPanel(
                        new FlowLayout(
                                FlowLayout.LEFT
                        )
                );

        searchPanel.setBackground(
                backgroundColor
        );

        JLabel searchLabel =
                createLabel(
                        "Search Category:"
                );

        txtSearch =
                new JTextField(
                        20
                );

        JButton searchButton =
                new JButton(
                        "Search"
                );

        JButton refreshButton =
                new JButton(
                        "Refresh"
                );

        styleButton(
                searchButton
        );

        styleButton(
                refreshButton
        );

        searchPanel.add(
                searchLabel
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

        bottomPanel.add(
                searchPanel,
                BorderLayout.NORTH
        );

        // =========================
        // MAIN MENU
        // =========================

        JPanel menuPanel =
                new JPanel(
                        new FlowLayout(
                                FlowLayout.RIGHT
                        )
                );

        menuPanel.setBackground(
                backgroundColor
        );

        JButton mainMenuButton =
                new JButton(
                        "Main Menu"
                );

        styleButton(
                mainMenuButton
        );

        menuPanel.add(
                mainMenuButton
        );

        bottomPanel.add(
                menuPanel,
                BorderLayout.SOUTH
        );

        mainPanel.add(
                bottomPanel,
                BorderLayout.SOUTH
        );

        add(
                mainPanel
        );

        // =========================
        // LOAD DATA
        // =========================

        loadCategories();

        // =========================
        // BUTTON ACTIONS
        // =========================

        addButton.addActionListener(
                e -> addCategory()
        );

        updateButton.addActionListener(
                e -> updateCategory()
        );

        deleteButton.addActionListener(
                e -> deleteCategory()
        );

        clearButton.addActionListener(
                e -> clearFields()
        );

        searchButton.addActionListener(
                e -> searchCategory()
        );

        refreshButton.addActionListener(
                e -> {

                    txtSearch.setText("");

                    loadCategories();
                }
        );

        // =========================
        // TABLE SELECTION
        // =========================

        categoryTable
                .getSelectionModel()
                .addListSelectionListener(
                        e -> {

                            if (
                                    !e.getValueIsAdjusting()
                            ) {

                                loadSelectedCategory();
                            }
                        }
                );

        // =========================
        // MAIN MENU
        // =========================

        mainMenuButton.addActionListener(e -> returnToMainMenu());
    }

    private void returnToMainMenu() {
        dispose();
        MainFrame frame = new MainFrame(studentName, userId, role);
        frame.setVisible(true);
    }

    // =========================
    // CREATE LABEL
    // =========================

    private JLabel createLabel(
            String text
    ) {

        JLabel label =
                new JLabel(
                        text
                );

        label.setFont(
                new Font(
                        "Arial",
                        Font.BOLD,
                        14
                )
        );

        label.setForeground(
                textColor
        );

        return label;
    }

    // =========================
    // STYLE BUTTON
    // =========================

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
                        13
                )
        );

        button.setFocusPainted(
                false
        );

        button.setCursor(
                new Cursor(
                        Cursor.HAND_CURSOR
                )
        );
    }

    // =========================
    // LOAD CATEGORIES
    // =========================

    private void loadCategories() {

        tableModel.setRowCount(
                0
        );

        String sql =
                "SELECT CATEGORY_ID, CATEGORY_NAME " +
                "FROM CATEGORIES " +
                "ORDER BY CATEGORY_ID";

        try (
                Connection con =
                        DBConnection.getConnection();

                Statement st =
                        con.createStatement();

                ResultSet rs =
                        st.executeQuery(
                                sql
                        )
        ) {

            while (
                    rs.next()
            ) {

                tableModel.addRow(
                        new Object[]{

                                rs.getInt(
                                        "CATEGORY_ID"
                                ),

                                rs.getString(
                                        "CATEGORY_NAME"
                                )
                        }
                );
            }

        } catch (
                SQLException ex
        ) {

            showError(
                    "Error loading categories:\n"
                            + ex.getMessage()
            );
        }
    }

    // =========================
    // ADD CATEGORY
    // =========================

    private void addCategory() {

        String categoryName =
                txtCategoryName
                        .getText()
                        .trim();

        if (
                categoryName.isEmpty()
        ) {

            JOptionPane.showMessageDialog(
                    this,
                    "Please enter category name."
            );

            return;
        }

        // Check duplicate category

        String checkSql =
                "SELECT COUNT(*) " +
                "FROM CATEGORIES " +
                "WHERE LOWER(CATEGORY_NAME) = LOWER(?)";

        try (
                Connection con =
                        DBConnection.getConnection();

                PreparedStatement checkPs =
                        con.prepareStatement(
                                checkSql
                        )
        ) {

            checkPs.setString(
                    1,
                    categoryName
            );

            try (
                    ResultSet rs =
                            checkPs.executeQuery()
            ) {

                if (
                        rs.next()
                        &&
                        rs.getInt(1) > 0
                ) {

                    JOptionPane.showMessageDialog(
                            this,
                            "Category already exists.",
                            "Duplicate Category",
                            JOptionPane.WARNING_MESSAGE
                    );

                    return;
                }
            }

            String sql =
                    "INSERT INTO CATEGORIES " +
                    "(CATEGORY_ID, CATEGORY_NAME) " +
                    "VALUES " +
                    "(CATEGORY_SEQ.NEXTVAL, ?)";

            try (
                    PreparedStatement pst =
                            con.prepareStatement(
                                    sql
                            )
            ) {

                pst.setString(
                        1,
                        categoryName
                );

                pst.executeUpdate();

                JOptionPane.showMessageDialog(
                        this,
                        "Category added successfully!"
                );

                clearFields();

                loadCategories();
            }

        } catch (
                SQLException ex
        ) {

            showError(
                    "Error adding category:\n"
                            + ex.getMessage()
            );
        }
    }

    // =========================
    // UPDATE CATEGORY
    // =========================

    private void updateCategory() {

        if (
                txtCategoryId
                        .getText()
                        .trim()
                        .isEmpty()
        ) {

            JOptionPane.showMessageDialog(
                    this,
                    "Please select a category first."
            );

            return;
        }

        String categoryName =
                txtCategoryName
                        .getText()
                        .trim();

        if (
                categoryName.isEmpty()
        ) {

            JOptionPane.showMessageDialog(
                    this,
                    "Please enter category name."
            );

            return;
        }

        int categoryId;

        try {

            categoryId =
                    Integer.parseInt(
                            txtCategoryId
                                    .getText()
                    );

        } catch (
                NumberFormatException ex
        ) {

            JOptionPane.showMessageDialog(
                    this,
                    "Invalid Category ID.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );

            return;
        }

        // Check duplicate name for another category

        String checkSql =
                "SELECT COUNT(*) " +
                "FROM CATEGORIES " +
                "WHERE LOWER(CATEGORY_NAME) = LOWER(?) " +
                "AND CATEGORY_ID <> ?";

        try (
                Connection con =
                        DBConnection.getConnection();

                PreparedStatement checkPs =
                        con.prepareStatement(
                                checkSql
                        )
        ) {

            checkPs.setString(
                    1,
                    categoryName
            );

            checkPs.setInt(
                    2,
                    categoryId
            );

            try (
                    ResultSet rs =
                            checkPs.executeQuery()
            ) {

                if (
                        rs.next()
                        &&
                        rs.getInt(1) > 0
                ) {

                    JOptionPane.showMessageDialog(
                            this,
                            "Another category with this name already exists.",
                            "Duplicate Category",
                            JOptionPane.WARNING_MESSAGE
                    );

                    return;
                }
            }

            String sql =
                    "UPDATE CATEGORIES " +
                    "SET CATEGORY_NAME = ? " +
                    "WHERE CATEGORY_ID = ?";

            try (
                    PreparedStatement pst =
                            con.prepareStatement(
                                    sql
                            )
            ) {

                pst.setString(
                        1,
                        categoryName
                );

                pst.setInt(
                        2,
                        categoryId
                );

                int result =
                        pst.executeUpdate();

                if (
                        result > 0
                ) {

                    JOptionPane.showMessageDialog(
                            this,
                            "Category updated successfully!"
                    );

                    clearFields();

                    loadCategories();

                } else {

                    JOptionPane.showMessageDialog(
                            this,
                            "Category not found."
                    );
                }
            }

        } catch (
                SQLException ex
        ) {

            showError(
                    "Error updating category:\n"
                            + ex.getMessage()
            );
        }
    }

    // =========================
    // DELETE CATEGORY
    // =========================
    // IMPORTANT:
    // Category cannot be deleted if
    // questions are using it.
    // =========================

    private void deleteCategory() {

        if (
                txtCategoryId
                        .getText()
                        .trim()
                        .isEmpty()
        ) {

            JOptionPane.showMessageDialog(
                    this,
                    "Please select a category first."
            );

            return;
        }

        int categoryId;

        try {

            categoryId =
                    Integer.parseInt(
                            txtCategoryId
                                    .getText()
                    );

        } catch (
                NumberFormatException ex
        ) {

            JOptionPane.showMessageDialog(
                    this,
                    "Invalid Category ID.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );

            return;
        }

        String categoryName =
                txtCategoryName
                        .getText()
                        .trim();

        // =================================================
        // CHECK QUESTIONS
        // =================================================

        String checkSql =
                "SELECT COUNT(*) " +
                "FROM QUESTIONS " +
                "WHERE CATEGORY_ID = ?";

        try (
                Connection con =
                        DBConnection.getConnection();

                PreparedStatement checkPs =
                        con.prepareStatement(
                                checkSql
                        )
        ) {

            checkPs.setInt(
                    1,
                    categoryId
            );

            int questionCount = 0;

            try (
                    ResultSet rs =
                            checkPs.executeQuery()
            ) {

                if (
                        rs.next()
                ) {

                    questionCount =
                            rs.getInt(1);
                }
            }

            // =================================================
            // QUESTIONS FOUND
            // =================================================

            if (
                    questionCount > 0
            ) {

                JOptionPane.showMessageDialog(
                        this,
                        "Cannot delete this category.\n\n"
                        + "Category: "
                        + categoryName
                        + "\n"
                        + "Questions using this category: "
                        + questionCount
                        + "\n\n"
                        + "Please delete or move these questions first.",
                        "Category In Use",
                        JOptionPane.WARNING_MESSAGE
                );

                return;
            }

            // =================================================
            // CONFIRM DELETE
            // =================================================

            int choice =
                    JOptionPane.showConfirmDialog(
                            this,
                            "Are you sure you want to delete\n"
                            + "category: "
                            + categoryName
                            + "?",
                            "Confirm Delete",
                            JOptionPane.YES_NO_OPTION,
                            JOptionPane.WARNING_MESSAGE
                    );

            if (
                    choice !=
                            JOptionPane.YES_OPTION
            ) {

                return;
            }

            // =================================================
            // DELETE
            // =================================================

            String deleteSql =
                    "DELETE FROM CATEGORIES " +
                    "WHERE CATEGORY_ID = ?";

            try (
                    PreparedStatement deletePs =
                            con.prepareStatement(
                                    deleteSql
                            )
            ) {

                deletePs.setInt(
                        1,
                        categoryId
                );

                int result =
                        deletePs.executeUpdate();

                if (
                        result > 0
                ) {

                    JOptionPane.showMessageDialog(
                            this,
                            "Category deleted successfully!",
                            "Success",
                            JOptionPane.INFORMATION_MESSAGE
                    );

                    clearFields();

                    loadCategories();

                } else {

                    JOptionPane.showMessageDialog(
                            this,
                            "Category not found."
                    );
                }
            }

        } catch (
                SQLException ex
        ) {

            // Extra protection in case
            // Oracle FK constraint is reached.

            if (
                    ex.getErrorCode() == 2292
            ) {

                JOptionPane.showMessageDialog(
                        this,
                        "This category cannot be deleted because "
                        + "questions are still using it.\n\n"
                        + "Please delete or move the questions first.",
                        "Category In Use",
                        JOptionPane.WARNING_MESSAGE
                );

            } else {

                showError(
                        "Error deleting category:\n"
                                + ex.getMessage()
                );
            }
        }
    }

    // =========================
    // SEARCH CATEGORY
    // =========================

    private void searchCategory() {

        String search =
                txtSearch
                        .getText()
                        .trim();

        if (
                search.isEmpty()
        ) {

            loadCategories();

            return;
        }

        tableModel.setRowCount(
                0
        );

        String sql =
                "SELECT CATEGORY_ID, CATEGORY_NAME " +
                "FROM CATEGORIES " +
                "WHERE LOWER(CATEGORY_NAME) " +
                "LIKE LOWER(?) " +
                "ORDER BY CATEGORY_ID";

        try (
                Connection con =
                        DBConnection.getConnection();

                PreparedStatement pst =
                        con.prepareStatement(
                                sql
                        )
        ) {

            pst.setString(
                    1,
                    "%" + search + "%"
            );

            try (
                    ResultSet rs =
                            pst.executeQuery()
            ) {

                while (
                        rs.next()
                ) {

                    tableModel.addRow(
                            new Object[]{

                                    rs.getInt(
                                            "CATEGORY_ID"
                                    ),

                                    rs.getString(
                                            "CATEGORY_NAME"
                                    )
                            }
                    );
                }
            }

        } catch (
                SQLException ex
        ) {

            showError(
                    "Search error:\n"
                            + ex.getMessage()
            );
        }
    }

    // =========================
    // LOAD SELECTED CATEGORY
    // =========================

    private void loadSelectedCategory() {

        int row =
                categoryTable
                        .getSelectedRow();

        if (
                row == -1
        ) {

            return;
        }

        txtCategoryId.setText(
                categoryTable
                        .getValueAt(
                                row,
                                0
                        )
                        .toString()
        );

        txtCategoryName.setText(
                categoryTable
                        .getValueAt(
                                row,
                                1
                        )
                        .toString()
        );
    }

    // =========================
    // CLEAR FIELDS
    // =========================

    private void clearFields() {

        txtCategoryId.setText(
                ""
        );

        txtCategoryName.setText(
                ""
        );

        categoryTable.clearSelection();

        txtCategoryName.requestFocus();
    }

    // =========================
    // SHOW ERROR
    // =========================

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

    // =========================
    // MAIN METHOD
    // =========================

    public static void main(
            String[] args
    ) {

        SwingUtilities.invokeLater(
                () -> {

                    CategoryFrame frame =
                            new CategoryFrame();

                    frame.setVisible(
                            true
                    );
                }
        );
    }
}