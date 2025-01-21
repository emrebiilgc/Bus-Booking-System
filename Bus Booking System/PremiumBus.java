/**
 * The {@code PremiumBus} class extends the {@code Bus} abstract class and represents a premium type of bus with enhanced features.
 * This bus type offers premium seats and implements a different pricing and refund strategy to accommodate a higher service level.
 */
public class PremiumBus extends Bus {
    private double refundCut;
    private double premiumFee;
    /**
     * Constructs a new PremiumBus with specified attributes and pricing details.
     *
     * @param id         the unique identifier for the premium bus
     * @param from       the starting point of the premium bus route
     * @param to         the destination point of the premium bus route
     * @param rows       the number of rows of seats in the premium bus, with three seats per row
     * @param price      the base price of a regular seat ticket
     * @param refundCut  the percentage deduction applied when refunding a ticket
     * @param premiumFee the additional fee applied to premium seats
     */
    public PremiumBus(int id, String from, String to, int rows, double price, double refundCut, double premiumFee){
        super(id, from, to, rows, price);
        this.refundCut = refundCut;
        this.premiumFee = premiumFee;
        this.seats = new boolean[rows * 3];
    }
    public double getPremiumFee() {
        return premiumFee;
    }
    public double getRefundCut() {
        return refundCut;
    }
    /**
     * Factory method to create a new PremiumBus instance from a given line of data.
     * This method parses the input line to extract premium bus properties and creates a new PremiumBus object.
     *
     * @param line a string containing the premium bus data in tab-separated format
     * @return a new PremiumBus object
     * @throws IllegalArgumentException if any of the parameters extracted from the line are invalid
     */
    public static PremiumBus createPremiumbus(String line) throws IllegalArgumentException {
        String[] parts = line.split("\\t");


        int id = 0, rows = 0, price = 0;
        double refundCut = 0, premiumFee = 0;

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
                throw new IllegalArgumentException("ERROR: " + rows + " is not a positive integer, number of seat rows of a voyage must be a positive integer!");
            }
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("ERROR: " + rows + " is not a positive integer, number of seat rows of a voyage must be a positive integer!");
        }

        try {
            price = (int) Double.parseDouble(parts[6]);
            if (price <= 0) {
                throw new IllegalArgumentException("ERROR: " + price + " is not a positive number, price must be a positive number!");
            }
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("ERROR: " + price + " is not a positive number, price must be a positive number!");
        }

        try {
            refundCut = (int)Double.parseDouble(parts[7]);
            if (refundCut < 0 || refundCut > 100) {
                throw new IllegalArgumentException("ERROR: " + (int)refundCut + " is not an integer that is in range of [0, 100], refund cut must be an integer that is in range of [0, 100]!");
            }
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("ERROR: " + (int)refundCut + " is not an integer that is in range of [0, 100], refund cut must be an integer that is in range of [0, 100]!");
        }

        try {
            premiumFee = (int)Double.parseDouble(parts[8]);
            if (premiumFee < 0) {
                throw new IllegalArgumentException("ERROR: " + (int)premiumFee + " is not a non-negative integer, premium fee must be a non-negative integer!");
            }
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("ERROR: " + (int)premiumFee + " is not a non-negative integer, premium fee must be a non-negative integer!");
        }


        return new PremiumBus(Integer.parseInt(parts[2]),parts[3],parts[4],
                Integer.parseInt(parts[5]),Double.parseDouble(parts[6]),Double.parseDouble(parts[7]),Double.parseDouble(parts[8]));
    }
    /**
     * Attempts to sell a seat in the premium bus.
     * This method overrides the abstract {@code sellSeat} method in the {@code Bus} class to apply pricing adjustments for premium seats and update the revenue accordingly.
     *
     * @param seatNumber the seat number to be sold
     * @throws Exception if the seat number is invalid or the seat is already sold
     */
    public void sellSeat(int seatNumber) throws Exception {
        int index = seatNumber - 1;
        double seatPrice = (index % 3 == 0) ? (100 + premiumFee) * getPrice() / 100 : getPrice();
        addRevenue(seatPrice);
        if (seatNumber <= 0) {
            throw new IllegalArgumentException("ERROR: " + seatNumber + " is not a positive integer, seat number must be a positive integer!");
        }
        if (index >= 0 && index < seats.length) {
            if (!seats[index]) {
                seats[index] = true;

            } else {
                throw new Exception("ERROR: This seat is already sold.");
            }
        } else {
            throw new Exception("ERROR: There is no such a seat!");
        }
    }
    /**
     * Implements the abstract {@code visualizeSeats} method from the {@code Bus} class to provide a visualization of the seat arrangement in the premium bus.
     * Seats are arranged in groups of three with specific markings for premium seats.
     *
     * @return a string representing the current seating arrangement
     */


    @Override
    public String visualizeSeats() {
        StringBuilder visualization = new StringBuilder();
        for (int i = 0; i < seats.length; i++) {

            visualization.append(seats[i] ? "X" : "*");

            if (i % 3 == 0) {
                visualization.append(" | ");
            } else if (i % 3 == 1) {
                visualization.append(" ");
            }

            if (i % 3 == 2 && i != seats.length - 1) {
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
