package dao;

import model.TicketEntity;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class TicketDao {

    private static final String INSERT_SQL =
            "INSERT INTO tickets (attendee_name, ticket_tier, price, payment_method) VALUES (?, ?, ?, ?)";

    private static final String SELECT_ALL_SQL =
            "SELECT ticket_id, attendee_name, ticket_tier, price, payment_method "
                    + "FROM tickets ORDER BY ticket_id";

    public void insertTicket(TicketEntity ticket) throws SQLException {
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(INSERT_SQL)) {
            ps.setString(1, ticket.attendeeName());
            ps.setString(2, ticket.ticketTier());
            ps.setDouble(3, ticket.price());
            ps.setString(4, ticket.paymentMethod());
            ps.executeUpdate();
        }
    }

    public List<TicketEntity> getAllTickets() throws SQLException {
        List<TicketEntity> tickets = new ArrayList<>();
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(SELECT_ALL_SQL);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                tickets.add(new TicketEntity(
                        rs.getInt("ticket_id"),
                        rs.getString("attendee_name"),
                        rs.getString("ticket_tier"),
                        rs.getDouble("price"),
                        rs.getString("payment_method")));
            }
        }
        return tickets;
    }
}