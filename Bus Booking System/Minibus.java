/**
 * The {@code Minibus} class extends the {@code Bus} abstract class and represents a specific type of bus with a distinct seating arrangement and characteristics.
 * This class is tailored for shorter or less frequent routes, typically having a smaller seating capacity.
 */
public class Minibus extends Bus {
    /**
     * Constructs a new PremiumBus with specified attributes and pricing details.
     *
     * @param id         the unique identifier for the premium bus
     * @param from       the starting point of the premium bus route
     * @param to         the destination point of the premium bus route
     * @param rows       the number of rows of seats in the premium bus, with three seats per row
     * @param price      the base price of a regular seat ticket
     */
    public Minibus(int id, String from, String to, int rows, double price){
        super(id, from, to, rows, price);
        this.seats = new boolean[rows * 2];
    }
    /**
     * Factory method to create a new Minibus instance from a given line of data.
     * This method parses the input line to extract minibus properties and creates a new Minibus object.
     *
     * @param line a string containing the minibus data in tab-separated format
     * @return a new Minibus object
     * @throws IllegalArgumentException if any of the parameters extracted from the line are invalid
     */
    public static Minibus createMinibus(String line){
        String[] parts = line.split("\\t");
        int id = 0, rows = 0, price = 0;

        try {
            id = Integer.parseInt(parts[2]);
            if (id <= 0) {
                throw new IllegalArgumentException("ERROR: " + id + " is not a positive integer, ID of a voyage must be a positive integer!");
            }
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("ERROR: " + id + " is not a positive integer, ID of a voyage must be a positive integer!");
        }

        try {
            rows = Integer.parseInt(parts[5]);
            if (rows <= 0) {
                throw new IllegalArgumentException("ERROR: " + rows + " is not a positive integer, number of seat rows must be a positive integer!");
            }
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("ERROR: " + rows + " is not a positive integer, number of seat rows must be a positive integer!");
        }

        try {
            price = (int) Double.parseDouble(parts[6]);
            if (price <= 0) {
                throw new IllegalArgumentException("ERROR: " + price + " is not a positive number, price must be a positive number!");
            }
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("ERROR: " + price + " is not a positive number, price must be a positive number!");
        }

        return new Minibus(Integer.parseInt(parts[2]),parts[3],parts[4],
                Integer.parseInt(parts[5]),Double.parseDouble(parts[6]));
    }
    /**
     * Attempts to sell a seat in the minibus.
     * This method overrides the abstract {@code sellSeat} method in the {@code Bus} class to update revenue and seat status specifically for minibuses.
     *
     * @param seatNumber the seat number to be sold
     * @throws Exception if the seat number is invalid or the seat is already sold
     */
    @Override
    public void sellSeat(int seatNumber) throws Exception {

        if (seatNumber <= 0) {
            throw new IllegalArgumentException("ERROR: " + seatNumber + " is not a positive integer, seat number must be a positive integer!");
        }

        if (seatNumber < 1 || seatNumber > seats.length) {
            throw new Exception("ERROR: There is no such a seat!");
        } else {
            if (seats[seatNumber - 1]) {

                throw new Exception("ERROR: This seat is already sold.");
            }
            seats[seatNumber - 1] = true;
            addRevenue(this.getPrice());
        }
    }
    /**
     * Implements the abstract {@code visualizeSeats} method from the {@code Bus} class to provide a visualization of the seat arrangement in the minibus.
     * Each seat is represented as "X" for taken or "*" for available, arranged in rows with spaces for readability.
     *
     * @return a string representing the current seating arrangement
     */
    @Override
    public String visualizeSeats() {
        StringBuilder visualization = new StringBuilder();
        for (int i = 0; i < seats.length; i++) {
            visualization.append(seats[i] ? "X" : "*");

            if ((i + 1) % 2 != 0 && i != seats.length - 1) {
                visualization.append(" ");
            }
            if ((i + 1) % 2 == 0) {
                visualization.append("\n");
            }
        }
        if (visualization.length() > 0 && visualization.charAt(visualization.length() - 1) == '\n') {
            visualization.deleteCharAt(visualization.length() - 1);
        }
        visualization.append("\n");
        return visualization.toString();
    }


}
