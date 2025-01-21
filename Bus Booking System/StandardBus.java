/**
 * The {@code StandardBus} class extends the {@code Bus} abstract class and represents a standard type of bus with regular features.
 * This bus type offers a 2+2 seating configuration and implements a standard refund policy.
 */
public class StandardBus extends Bus{
    private double refundCut;
    /**
     * Constructs a new StandardBus with specified attributes including a refund policy.
     *
     * @param id         the unique identifier for the standard bus
     * @param from       the starting point of the standard bus route
     * @param to         the destination point of the standard bus route
     * @param rows       the number of rows of seats in the standard bus, with four seats per row
     * @param price      the base price of a ticket for the standard bus
     * @param refundCut  the percentage deduction applied when refunding a ticket
     */
    public StandardBus(int id, String from, String to, int rows, double price, double refundCut){
        super(id, from, to, rows, price);
        this.refundCut = refundCut;
        this.seats = new boolean[rows * 4];
    }
    public static StandardBus createStandartbus(String line){
        String[] parts = line.split("\\t");
        int id = 0, rows = 0, price = 0;
        double refundCut = 0;

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

        try {
            refundCut = (int)Double.parseDouble(parts[7]);
            if (refundCut < 0 || refundCut > 100) {
                throw new IllegalArgumentException("ERROR: " + (int)refundCut + " is not an integer that is in range of [0, 100], refund cut must be an integer that is in range of [0, 100]!");
            }
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("ERROR: " + (int)refundCut + " is not an integer that is in range of [0, 100], refund cut must be an integer that is in range of [0, 100]!");
        }

        return new StandardBus(Integer.parseInt(parts[2]),parts[3],parts[4],
                Integer.parseInt(parts[5]),Double.parseDouble(parts[6]),Double.parseDouble(parts[7]));
    }
    public double getRefundCut() {
        return refundCut;
    }
    /**
     * Attempts to sell a seat in the standard bus.
     * This method overrides the abstract {@code sellSeat} method in the {@code Bus} class to update revenue and seat status specifically for standard buses.
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
     * Implements the abstract {@code visualizeSeats} method from the {@code Bus} class to provide a visualization of the seat arrangement in the standard bus.
     * Seats are arranged in groups of four with clear separation between each pair of seats for clarity.
     *
     * @return a string representing the current seating arrangement
     */
    @Override
    public String visualizeSeats() {
        StringBuilder visualization = new StringBuilder();
        for (int i = 0; i < seats.length; i++) {
            visualization.append(seats[i] ? "X " : "* ");

            if ((i + 1) % 2 == 0 && (i + 1) % 4 != 0) {
                visualization.append("| ");
            }
            if ((i + 1) % 4 == 0) {
                visualization.deleteCharAt(visualization.length() - 1);
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
