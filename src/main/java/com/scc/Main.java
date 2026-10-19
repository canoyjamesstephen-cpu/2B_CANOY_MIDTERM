import dao.DatabaseConnection;
import ui.TicketMenu;

import java.sql.SQLException;

public class Main {
    public static void main(String[] args) {
        try {
            DatabaseConnection.initializeDatabase();
        } catch (SQLException e) {
            System.out.println("Could not initialize database: " + e.getMessage());
            return;
        }
        new TicketMenu().run();
    }
}