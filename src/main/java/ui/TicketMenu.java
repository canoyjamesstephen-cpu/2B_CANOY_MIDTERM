package ui;

import dao.TicketDao;
import model.TicketEntity;

import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Scanner;

public class TicketMenu {

    private static final String DOUBLE_LINE = "=".repeat(60);
    private static final String SINGLE_LINE = "-".repeat(60);

    private static final String ROW_FORMAT = "| %-4s | %-24s | %-12s | %12s | %-10s |%n";
    private static final String BORDER = "+" + "-".repeat(6) + "+" + "-".repeat(26) + "+"
            + "-".repeat(14) + "+" + "-".repeat(14) + "+" + "-".repeat(12) + "+";

    private final Scanner scanner = new Scanner(System.in);
    private final TicketDao dao = new TicketDao();

    public void run() {
        boolean running = true;
        while (running) {
            printMenu();
            if (!scanner.hasNextLine()) {
                break;
            }
            String choice = scanner.nextLine().trim();
            switch (choice) {
                case "1" -> addTicket();
                case "2" -> viewTickets();
                case "3" -> {
                    System.out.println();
                    System.out.println("Exiting system... Thank you!");
                    running = false;
                }
                default -> System.out.println("\nInvalid choice. Please enter 1, 2, or 3.");
            }
        }
        scanner.close();
    }

    private void printMenu() {
        System.out.println();
        System.out.println(SINGLE_LINE);
        System.out.println("                EVENT TICKET SALES SYSTEM");
        System.out.println(DOUBLE_LINE);
        System.out.println("[1] Add New Ticket");
        System.out.println("[2] View Sold Tickets");
        System.out.println("[3] Exit System");
        System.out.println(SINGLE_LINE);
        System.out.print("Enter choice: ");
    }

    private void addTicket() {
        System.out.println();
        System.out.println("--- ISSUE EVENT TICKET ---");
        String name = promptRequired("Enter Attendee Name: ");
        String tier = promptChoice("Enter Ticket Tier (REGULAR / VIP / VVIP): ",
                "REGULAR", "VIP", "VVIP");
        double price = promptPrice("Enter Price: ");
        String payment = promptChoice("Enter Payment Method (CASH / GCASH / CARD): ",
                "CASH", "GCASH", "CARD");

        try {
            dao.insertTicket(new TicketEntity(0, name, tier, price, payment));
            System.out.println();
            System.out.println("SUCCESS: Ticket issued to attendee!");
        } catch (SQLException e) {
            System.out.println();
            System.out.println("ERROR: Could not save ticket. " + e.getMessage());
        }
    }

    private void viewTickets() {
        try {
            List<TicketEntity> tickets = dao.getAllTickets();
            System.out.println();
            System.out.println("=".repeat(BORDER.length()));
            System.out.println("                         EVENT TICKET PURCHASES");
            System.out.println("=".repeat(BORDER.length()));

            if (tickets.isEmpty()) {
                System.out.println("No tickets sold yet.");
                return;
            }

            System.out.println(BORDER);
            System.out.printf(ROW_FORMAT, "ID", "ATTENDEE NAME", "TICKET TIER", "PRICE", "PAYMENT");
            System.out.println(BORDER);
            for (TicketEntity t : tickets) {
                System.out.printf(Locale.US, ROW_FORMAT,
                        t.ticketId(),
                        cut(t.attendeeName(), 24),
                        t.ticketTier(),
                        String.format(Locale.US, "%.2f", t.price()),
                        t.paymentMethod());
            }
            System.out.println(BORDER);
            System.out.println("Total Records Displayed: " + tickets.size());
        } catch (SQLException e) {
            System.out.println("ERROR: Could not read tickets. " + e.getMessage());
        }
    }

    private String promptRequired(String label) {
        while (true) {
            System.out.print(label);
            String value = scanner.nextLine().trim();
            if (!value.isEmpty()) {
                return value;
            }
            System.out.println("This field cannot be empty.");
        }
    }

    private String promptChoice(String label, String... options) {
        while (true) {
            System.out.print(label);
            String value = scanner.nextLine().trim().toUpperCase();
            if (Arrays.asList(options).contains(value)) {
                return value;
            }
            System.out.println("Invalid input. Choose one of: " + String.join(", ", options));
        }
    }

    private double promptPrice(String label) {
        while (true) {
            System.out.print(label);
            String value = scanner.nextLine().trim();
            try {
                double price = Double.parseDouble(value);
                if (price > 0) {
                    return price;
                }
                System.out.println("Price must be greater than 0.");
            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid number (e.g., 1500.00).");
            }
        }
    }

    private String cut(String s, int max) {
        if (s == null) {
            return "";
        }
        return s.length() <= max ? s : s.substring(0, max - 3) + "...";
    }
}