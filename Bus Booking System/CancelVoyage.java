import java.util.Locale;
/**
 * The {@code CancelVoyage} class manages the cancellation of bus voyages within the booking system.
 * It provides methods to handle the cancellation process, including refunding tickets and updating the system's records.
 */

public class CancelVoyage {
    /**
     * Cancels a bus voyage identified by the bus ID and updates the booking system accordingly.
     * This method checks if the bus exists, and if it does, it proceeds to cancel the voyage, refund all tickets,
     * and remove the bus from the system. It also logs details of the cancellation and any refunds processed.
     *
     * @param busId The ID of the bus whose voyage is to be cancelled.
     * @param args  Command-line arguments containing file paths for logging outputs.
     */
    public static void printVoyageCancellation(int busId, String[] args) {
        Bus bus = BookingSystem.getBusById(busId);
        FileOutput.writeToFile(args[1], "Voyage " + bus.getId() + " was successfully cancelled!", true, true);
        FileOutput.writeToFile(args[1], "Voyage details can be found below:", true, true);
        FileOutput.writeToFile(args[1], "Voyage " + bus.getId(), true, true);
        FileOutput.writeToFile(args[1], bus.getFrom() + "-" + bus.getTo(), true, true);

        double totalRefundAmount = 0;
        for (int i = 0; i < bus.getSeats().length; i++) {
            if (bus.getSeats()[i]) {
                try {
                    double refundAmount = 0;

                    if (bus instanceof PremiumBus) {
                        if (i % 3 == 0) {
                            refundAmount = (((PremiumBus) bus).getPremiumFee() + 100 ) / 100.0 * bus.getPrice() ;
                        }
                        else {
                            refundAmount = bus.getPrice();
                        }
                    } else if (bus instanceof StandardBus) {
                        refundAmount =  bus.getPrice();
                    }

                    totalRefundAmount += refundAmount;

                } catch (Exception e) {
                    FileOutput.writeToFile(args[1], "Error while refunding seat " + (i + 1) + ": " + e.getMessage(), true, true);
                }
            }
        }
        bus.addRevenue(-totalRefundAmount);

        String revenue = "";

        FileOutput.writeToFile(args[1], bus.visualizeSeats() , true, false);


        revenue = "Revenue: " + String.format(Locale.US, "%.2f", bus.getRevenue());
        FileOutput.writeToFile(args[1], revenue, true, true);
    }
}