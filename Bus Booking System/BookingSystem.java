import java.util.ArrayList;
import java.util.List;

/**
 * The {@code BookingSystem} class serves as the entry point and main controller for the bus reservation system.
 * It manages a collection of {@code Bus} objects and supports various operations such as adding or removing buses,
 * and retrieving buses by their IDs.
 */

public class BookingSystem {
    /**
     * The main method that serves as the entry point of the application.
     * It processes command-line arguments to execute various operations within the bus booking system.
     *
     * @param args the command line arguments, where args[0] is the input file path and args[1] is the output file path.
     */
    public static void main(String[] args) {
        BusOperation.process(args);
        RemoveLine.removeLastLine(args);
        FileOutput.writeToFile(args[1], "----------------", true, false);
    }
    private static List<Bus> allBuses = new ArrayList<>();

    public static void addBus(Bus bus) {
        allBuses.add(bus);
    }

    public static List<Bus> getAllBuses() {
        return allBuses;
    }

    public static Bus getBusById(int id) {
        for (Bus bus : allBuses) {
            if (bus.getId() == id) {
                return bus;
            }
        }
        return null;
    }

    public static void removeBus(int id) {
        allBuses.removeIf(bus -> bus.getId() == id);
    }


}
