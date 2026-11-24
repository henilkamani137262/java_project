import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {

   private static final String URL =
        "jdbc:oracle:thin:@localhost:1521/orcle";

private static final String USER =
        "henil";

private static final String PASSWORD =
        "h123";
    public static Connection getConnection()
            throws SQLException {

        try {

            Class.forName(
                    "oracle.jdbc.OracleDriver"
            );

        } catch (ClassNotFoundException ex) {

            throw new SQLException(
                    "Oracle JDBC Driver not found: "
                            + ex.getMessage()
            );
        }

        return DriverManager.getConnection(
                URL,
                USER,
                PASSWORD
        );
    }
}