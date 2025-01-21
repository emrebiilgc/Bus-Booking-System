import java.util.Locale;
/**
 * The {@code SellTicket} class handles the sale of tickets within the booking system.
 * It verifies seat availability, processes ticket sales, and updates financial records for each bus voyage.
 */
public class SellTicket {
    /**
     * Processes the sale of tickets for a specified bus. This method validates the seat numbers,
     * ensures they are available, and then completes the sale by updating seat status and revenue.
     *
     * @param busId The ID of the bus for which tickets are being sold.
     * @param seatNumbers A string containing the seat numbers to sell, separated by underscores (e.g., "1_2_3").
     * @param args Command-line arguments used for specifying output paths for logging.
     */
    public static void seller(int busId, String seatNumbers, String[] args) {
        Bus bus = BookingSystem.getBusById(busId);
        if (bus == null) {
            FileOutput.writeToFile(args[1], "ERROR: There is no voyage with ID of " + busId + "!", true, true);
            return;
        }
        String[] seatNumbersArray = seatNumbers.split("_");
        StringBuilder soldSeats = new StringBuilder();
        double totalCost = 0.0;
        boolean first = true;

        try {
            for (String seat : seatNumbersArray) {
                int seatNumber = Integer.parseInt(seat);
                if (seatNumber <= 0) {
                    throw new IllegalArgumentException("ERROR: " + seatNumber + " is not a positive integer, seat number must be a positive integer!");
                }
                int index = seatNumber - 1;
                if (index >= bus.seats.length || index < 0) {
                    throw new Exception("ERROR: There is no such a seat!");
                }
                if (bus.seats[index]) {
                    throw new Exception("ERROR: One or more seats already sold!");
                }
            }

            for (String seat : seatNumbersArray) {
                int seatNumber = Integer.parseInt(seat);
                bus.sellSeat(seatNumber);
                double seatPrice = bus.getPrice();

                if (bus instanceof PremiumBus && (seatNumber - 1) % 3 == 0) {
                    seatPrice = (((PremiumBus) bus).getPremiumFee() + 100) * seatPrice / 100;
                }

                totalCost += seatPrice;
                if (first) {
                    soldSeats.append(seatNumber);
                    first = false;
                } else {
                    soldSeats.append("-").append(seatNumber);
                }
            }

            if (soldSeats.length() > 0) {
                String seatVoyage = String.format(Locale.US, "Seat %s of the Voyage %d from %s to %s was successfully sold for %.2f TL.",
                        soldSeats, bus.getId(), bus.getFrom(), bus.getTo(), totalCost);
                FileOutput.writeToFile(args[1], seatVoyage, true, true);
            }

        } catch (NumberFormatException e) {
            FileOutput.writeToFile(args[1], "Error: Invalid seat number format - " , true, true);
        } catch (IllegalArgumentException e) {
            FileOutput.writeToFile(args[1], e.getMessage(), true, true);
        } catch (Exception e) {
            FileOutput.writeToFile(args[1], e.getMessage(), true, true);
        }
    }
}
