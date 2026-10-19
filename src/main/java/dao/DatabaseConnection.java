package dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public final class DatabaseConnection {

    private static final String URL = "jdbc:sqlite:tickets.db";

    private DatabaseConnection() {
    }

    /** Opens a connection with foreign key enforcement turned on. */
    public static Connection getConnection() throws SQLException {
        Connection conn = DriverManager.getConnection(URL);
        try (Statement st = conn.createStatement()) {
            st.execute("PRAGMA foreign_keys = ON;");
        }
        return conn;
    }

    /** Creates the tickets table on first run. */
    public static void initializeDatabase() throws SQLException {
        String sql = """
                CREATE TABLE IF NOT EXISTS tickets (
                    ticket_id      INTEGER PRIMARY KEY AUTOINCREMENT,
                    attendee_name  TEXT NOT NULL,
                    ticket_tier    TEXT NOT NULL CHECK (ticket_tier IN ('REGULAR', 'VIP', 'VVIP')),
                    price          REAL NOT NULL CHECK (price > 0),
                    payment_method TEXT NOT NULL CHECK (payment_method IN ('CASH', 'GCASH', 'CARD'))
                )
                """;
        try (Connection conn = getConnection();
             Statement st = conn.createStatement()) {
            st.execute(sql);
        }
    }
}