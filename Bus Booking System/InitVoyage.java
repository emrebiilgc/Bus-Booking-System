/**
 * The {@code InitVoyage} class is responsible for initializing voyages in the booking system.
 * It reads and interprets commands to set up new bus voyages, ensuring the correct type of bus is created based on the provided specifications.
 */
public class InitVoyage {
    /**
     * Processes a line of data to determine the type of bus to create and initializes the voyage.
     * This method interprets the bus type from the command line input and creates the appropriate bus object.
     *
     * @param line The line of data containing voyage initialization parameters.
     * @param args Command-line arguments where args[1] may be used to determine output paths for logs or errors.
     */
    public static void busPlacer(String line,String[] args) {
        String[] parts = line.split("\\t");
        Bus newBus = null;
        switch (parts[1]) {
            case "Standard":

                newBus = StandardBus.createStandartbus(line);

                break;
            case "Minibus":

                newBus = Minibus.createMinibus(line);

                break;
            case "Premium":
                newBus = PremiumBus.createPremiumbus(line);

                break;
        }
        if (newBus != null) {
            BookingSystem.addBus(newBus);
        }
    }
}
