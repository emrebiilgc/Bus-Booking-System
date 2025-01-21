import java.util.Comparator;
import java.util.List;
import java.util.Locale;
/**
 * The {@code Print} class provides utilities for formatting and printing information related to bus operations in the booking system.
 * It handles the output of detailed information about voyages, tickets, and financial reports to ensure clear communication and record-keeping.
 */
public class Print {
    /**
     * Prints detailed information about the initialization of a voyage command, including bus type and voyage details.
     * This method formats the output based on the type of bus and the specifics of the voyage command.
     *
     * @param parts Array of strings containing the details of the voyage initialization command.
     * @param args  Command-line arguments specifying paths for output files.
     */

    public static void printInitVoyageCommand(String[] parts,String[] args) {
        String commandInfo = "";;
        if (parts.length > 8) {
            // Premium voyage
            commandInfo = String.format(Locale.US, "COMMAND: INIT_VOYAGE\t%s\t%s\t%s\t%s\t%s\t%s\t%s\t%s",
                    parts[1], parts[2], parts[3], parts[4], parts[5], parts[6], parts[7], parts[8]);
        } else if (parts.length > 7) {
            // Standard voyage
            commandInfo = String.format(Locale.US, "COMMAND: INIT_VOYAGE\t%s\t%s\t%s\t%s\t%s\t%s\t%s",
                    parts[1], parts[2], parts[3], parts[4], parts[5], parts[6], parts[7]);
        } else {
            // Minibus voyage
            commandInfo = String.format(Locale.US, "COMMAND: INIT_VOYAGE\t%s\t%s\t%s\t%s\t%s\t%s",
                    parts[1], parts[2], parts[3], parts[4], parts[5], parts[6]);
        }
        FileOutput.writeToFile(args[1], commandInfo, true, true);


    }
    /**
     * Prints detailed information about a voyage after it has been initialized.
     * This method formats and prints specifics such as voyage ID, route details, and pricing.
     *
     * @param parts Array of strings containing the voyage details.
     * @param args  Command-line arguments for specifying output file paths.
     */
    public static void printInitVoyageInfo(String[] parts,String[] args) {
        double price = Double.parseDouble(parts[6]);
        int seats = Integer.parseInt(parts[5]);
        String voyageInfo;

        if (parts.length > 8) {
            double premiumPrice = price * (100 + Double.parseDouble(parts[8])) / 100;
            voyageInfo = String.format(Locale.US, "Voyage %s was initialized as a premium (1+2) voyage from %s to %s with %.2f TL priced %d regular seats and %.2f TL priced %d premium seats. Note that refunds will be %s%% less than the paid amount.",
                    parts[2], parts[3], parts[4], price, seats * 2, premiumPrice, seats, parts[7]);
        } else if (parts.length > 7) {
            voyageInfo = String.format(Locale.US, "Voyage %s was initialized as a standard (2+2) voyage from %s to %s with %.2f TL priced %d regular seats. Note that refunds will be %s%% less than the paid amount.",
                    parts[2], parts[3], parts[4], price, seats * 4, parts[7]);
        } else {
            voyageInfo = String.format(Locale.US, "Voyage %s was initialized as a minibus (2) voyage from %s to %s with %.2f TL priced %d regular seats. Note that minibus tickets are not refundable.",
                    parts[2], parts[3], parts[4], price, seats * 2);
        }
        String busType = parts[1].toLowerCase();
        if (busType.equals("premium") || busType.equals("standard") || busType.equals("minibus")) {
            FileOutput.writeToFile(args[1], voyageInfo, true, true);
        }

    }
    /**
     * Generates and prints a financial Z report for the booking system.
     * This method collates financial data from all voyages and formats it into a comprehensive Z report.
     *
     * @param args Command-line arguments that may specify output details or file paths.
     */
    public static void printZReport(String[] args) {
        List<Bus> buses = BookingSystem.getAllBuses();
        buses.sort(Comparator.comparingInt(Bus::getId));
        String revenue = "";
        if (buses.isEmpty()){
            FileOutput.writeToFile(args[1], "No Voyages Available!", true, true);
            FileOutput.writeToFile(args[1], "----------------", true, true);
            return;
        }
        for (Bus bus : buses) {
            FileOutput.writeToFile(args[1], "Voyage " + bus.getId(), true, true);
            FileOutput.writeToFile(args[1], bus.getFrom() + "-" + bus.getTo(), true, true);
            FileOutput.writeToFile(args[1], bus.visualizeSeats(), true, false);
            revenue = "Revenue: " + String.format(Locale.US, "%.2f", bus.getRevenue());
            FileOutput.writeToFile(args[1], revenue, true, true);
            FileOutput.writeToFile(args[1], "----------------", true, true);
        }
    }
    /**
     * Handles printing of voyage details based on a specific bus ID.
     * This method fetches the relevant voyage details from the system and formats them for output.
     *
     * @param busId The ID of the bus whose voyage details need to be printed.
     * @param args  Command-line arguments for specifying output file paths.
     */
    public static void printVoyage(int busId,String[] args) {
        String revenue = "";
        Bus bus = BookingSystem.getBusById(busId);
        if (busId <= 0){
            FileOutput.writeToFile(args[1], "ERROR: " + busId + " is not a positive integer, ID of a voyage must be a positive integer!", true, true);
            return;
        } else if (bus == null) {
            FileOutput.writeToFile(args[1], "ERROR: There is no voyage with ID of " + busId + "!", true, true);
            return;
        }

        FileOutput.writeToFile(args[1], "Voyage " + bus.getId(), true, true);
        FileOutput.writeToFile(args[1], bus.getFrom() + "-" + bus.getTo(), true, true);
        FileOutput.writeToFile(args[1], bus.visualizeSeats(), true, false);
        revenue= "Revenue: " + String.format(Locale.US, "%.2f", bus.getRevenue());
        FileOutput.writeToFile(args[1], revenue, true, true);
    }

}

