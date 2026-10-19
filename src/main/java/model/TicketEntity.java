package model;

/**
 * One issued event ticket (5 attributes).
 * ticketId is 0 for a new ticket; the database assigns the real ID.
 */
public record TicketEntity(
        int ticketId,
        String attendeeName,
        String ticketTier,
        double price,
        String paymentMethod) {
}