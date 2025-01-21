/**
 * The abstract class {@code Bus} serves as a blueprint for various types of buses in a booking system.
 * It contains common properties and methods that all bus types inherit, such as bus ID, route information, and seating.
 * Specific types of buses will extend this class to implement specific features and behaviors.
 */
public abstract class Bus {
    private int id;
    private String from;
    private String to;
    private int rows;
    private double price;
    protected boolean[] seats;
    private double revenue = 0;

    public double getRevenue() {
        return revenue;
    }

    protected void addRevenue(double amount) {
        this.revenue += amount;
    }

    public String getFrom() {
        return from;
    }

    public String getTo() {
        return to;
    }

    public int getRows() {
        return rows;
    }

    public double getPrice() {
        return price;
    }

    public int getId() {
        return id;
    }

    /**
     * Constructor for creating a new Bus object with specified attributes.
     *
     * @param id    the unique identifier for the bus
     * @param from  the starting point of the bus route
     * @param to    the destination point of the bus route
     * @param rows  the number of rows of seats in the bus
     * @param price the base price of a ticket
     */

    public Bus(int id, String from, String to, int rows, double price) {
        this.id = id;
        this.from = from;
        this.to = to;
        this.rows = rows;
        this.price = price;
    }
    public abstract String visualizeSeats();

    public void sellSeat(int seatNumber) throws Exception {
    }
    public void refundSeat(int seatNumber) {
        int index = seatNumber - 1;
        seats[index] = false;
    }
    public boolean[] getSeats() {
        return seats;
    }

}