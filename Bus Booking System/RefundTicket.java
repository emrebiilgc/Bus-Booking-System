import java.util.Locale;
/**
 * The {@code RefundTicket} class is responsible for processing ticket refunds within the booking system.
 * It handles the intricacies of determining which tickets can be refunded based on the bus type and seat details,
 * and also manages the financial implications of these refunds.
 */
public class RefundTicket {
    /**
     * Checks if all specified seats are eligible for refund.
     * This method validates each seat number, ensuring it exists and has not been refunded already.
     *
     * @param bus The bus from which the seats are being refunded.
     * @param seatsToRefund An array of seat numbers to check for refund eligibility.
     * @param args Command-line arguments for output.
     * @return true if all seats can be refunded, false otherwise.
     */
    private static boolean allSeatsRefundable(Bus bus, String[] seatsToRefund, String[] args) {
        for (String seatStr : seatsToRefund) {
            int seatNumber;
            try {
                seatNumber = Integer.parseInt(seatStr);
            } catch (NumberFormatException e) {
                FileOutput.writeToFile(args[1], "ERROR: " + seatStr + " is not a valid number, seat number must be a positive integer!", true, true);
                return false;
            }

            if (seatNumber <= 0) {
                FileOutput.writeToFile(args[1], "ERROR: " + seatNumber + " is not a positive integer, seat number must be a positive integer!", true, true);
                return false;
            }

            int index = seatNumber - 1;
            if (index >= bus.getSeats().length ) {
                FileOutput.writeToFile(args[1], "ERROR: There is no such a seat!", true, true);
                return false;
            }
            if (index >= bus.getSeats().length || !bus.getSeats()[index]) {
                FileOutput.writeToFile(args[1], "ERROR: One or more seats are already empty!", true, true);
                return false;
            }
        }
        return true;
    }
    /**
     * Processes refunds for one or more tickets on a specified bus. It checks if the seats are refundable,
     * calculates the refund amounts, updates the seat status, and adjusts the revenue accordingly.
     *
     * @param busId The ID of the bus from which tickets are to be refunded.
     * @param seatNumbers A concatenated string of seat numbers to be refunded, separated by underscores (e.g., "1_2_3").
     * @param args Command-line arguments used for specifying output paths for logging.
     */
    public static void refunder(int busId, String seatNumbers, String[] args) {
        Bus bus = BookingSystem.getBusById(busId);
        if (bus == null) {
            FileOutput.writeToFile(args[1], "ERROR: There is no voyage with ID of " + busId +"!", true, true);
            return;
        }
        String[] seatsToRefund = seatNumbers.split("_");
        if (!allSeatsRefundable(bus, seatsToRefund, args)) {
            return;
        }
        StringBuilder refundedSeats = new StringBuilder();
        double totalRefundAmount = 0.0;
        boolean first = true;

        for (String seat : seatsToRefund) {
            int seatNumber = 0;
            try {
                seatNumber = Integer.parseInt(seat);
                if (bus instanceof Minibus) {
                    FileOutput.writeToFile(args[1], "ERROR: Minibus tickets are not refundable!", true, true);
                    break;
                }

                bus.refundSeat(seatNumber);  // This will throw an exception if the seat cannot be refunded


                double seatPrice = bus.getPrice();
                double refundAmount = seatPrice;

                if (bus instanceof PremiumBus) {
                    if ((seatNumber - 1) % 3 == 0) {
                        seatPrice = (((PremiumBus) bus).getPremiumFee() + 100) * bus.getPrice() / 100;
                    }
                    refundAmount = seatPrice - (seatPrice * ((PremiumBus) bus).getRefundCut() / 100);
                } else if (bus instanceof StandardBus) {
                    double refundCut = ((StandardBus) bus).getRefundCut();
                    refundAmount = seatPrice - (seatPrice * refundCut / 100);
                }

                bus.addRevenue(-refundAmount);
                totalRefundAmount += refundAmount;


                if (first) {
                    refundedSeats.append(seatNumber);
                    first = false;
                } else {
                    refundedSeats.append("-").append(seatNumber);
                }
            } catch (NumberFormatException e) {
                FileOutput.writeToFile(args[1], "Error: Invalid seat number format - " + seat, true, true);
                return;

            } catch (Exception e) {
                FileOutput.writeToFile(args[1], e.getMessage() , true, true);
                return;

            }
        }

        if (refundedSeats.length() > 0) {
            String refundedSeatSummary = String.format(Locale.US, "Seat %s of the Voyage %d from %s to %s was successfully refunded for %.2f TL.",
                    refundedSeats, bus.getId(), bus.getFrom(), bus.getTo(), totalRefundAmount);
            FileOutput.writeToFile(args[1], refundedSeatSummary, true, true);
        }
    }

}
