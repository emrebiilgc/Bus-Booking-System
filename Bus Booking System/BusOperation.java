import java.io.File;
/**
 * The {@code BusOperation} class is responsible for the operational management of bus voyages in the booking system.
 * It processes input commands from a file and executes them accordingly, handling bus initialization, ticket sales, refunds, cancellations, and generating reports.
 */
public class BusOperation {
    /**
     * Processes the entire list of commands related to bus operations from the provided command line arguments.
     * This method validates the command line arguments, checks file accessibility, and reads the input file to process each command.
     *
     * @param args the command line arguments, expected to include the path to the input file and the path to the output file.
     */
    public static void process(String[] args) {
        if (args.length != 2) {
            System.err.println("ERROR: This program works exactly with two command line arguments, the first one is the path to the input file whereas the second one is the path to the output file. Sample usage can be as follows: \"java BookingSystem input.txt output.txt\". Program is going to terminate!");
            System.exit(1);
        }

        File inputFile = new File(args[0]);
        if (!inputFile.exists() || !inputFile.canRead()) {
            System.err.println("ERROR: This program cannot read from \"" + args[0] + "\", either this program does not have read permission to read that file or file does not exist. Program is going to terminate!");
            System.exit(1);
        }

        File outputFile = new File(args[1]);
        if (!outputFile.exists()) {
            try {
                outputFile.createNewFile();
            } catch (Exception e) {
                System.err.println("ERROR: This program cannot write to \"" + args[1] + "\", please check the permissions to write that directory. Program is going to terminate!");
                System.exit(1);
            }
        }
        String[] data = FileInput.readFile(args[0], true, true);
        boolean lastCommandIsZReport = false;
        for (String line : data) {
            if (line.trim().isEmpty()) {
                continue;
            }

            String[] parts = line.split("\\t");

            switch (parts[0]) {
                case "INIT_VOYAGE":
                    String busType = parts[1].toLowerCase();
                    if (busType.equals("premium") || busType.equals("standard") || busType.equals("minibus")) {
                        Print.printInitVoyageCommand(parts, args);
                        int busId = Integer.parseInt(parts[2]);
                        Bus existingBus = BookingSystem.getBusById(busId);
                        if (existingBus != null) {
                            String errorMessage = "ERROR: There is already a voyage with ID of " + busId + "!";
                            FileOutput.writeToFile(args[1], errorMessage, true, true);
                        } else {
                            try {
                                InitVoyage.busPlacer(line, args);
                                Print.printInitVoyageInfo(parts, args);
                            } catch (IllegalArgumentException e) {
                                FileOutput.writeToFile(args[1], e.getMessage(), true, true);
                            }
                        }
                    } else {
                        Print.printInitVoyageCommand(parts, args);
                        String errorMessage = "ERROR: Erroneous usage of \"INIT_VOYAGE\" command!";
                        FileOutput.writeToFile(args[1], errorMessage, true, true);
                    }
                    lastCommandIsZReport = false;
                    break;
                case "Z_REPORT":

                    if (parts.length < 2) {
                        FileOutput.writeToFile(args[1], "COMMAND: Z_REPORT", true, true);
                        FileOutput.writeToFile(args[1], "Z Report:", true, true);
                        FileOutput.writeToFile(args[1], "----------------", true, true);
                        Print.printZReport(args);
                    } else {
                        FileOutput.writeToFile(args[1], "COMMAND: Z_REPORT\t" + parts[1], true, true);
                        FileOutput.writeToFile(args[1], "ERROR: Erroneous usage of \"Z_REPORT\" command!", true, true);
                    }
                    lastCommandIsZReport = true;
                    break;
                case "SELL_TICKET":
                    if (parts.length < 3) {

                        if (parts.length == 2) {
                            String sellTicketCommand2 = String.format("COMMAND: SELL_TICKET\t%s", parts[1]);
                            FileOutput.writeToFile(args[1], sellTicketCommand2, true, true);
                        }
                        if (parts.length == 1) {
                            String sellTicketCommand1 = String.format("COMMAND: SELL_TICKET");
                            FileOutput.writeToFile(args[1], sellTicketCommand1, true, true);
                        }

                        String errorMessage = "ERROR: Erroneous usage of \"SELL_TICKET\" command!";
                        FileOutput.writeToFile(args[1], errorMessage, true, true);
                    } else {
                        String sellTicketCommand = String.format("COMMAND: SELL_TICKET\t%s\t%s", parts[1], parts[2]);
                        FileOutput.writeToFile(args[1], sellTicketCommand, true, true);
                        SellTicket.seller(Integer.parseInt(parts[1]), parts[2], args);
                    }
                    lastCommandIsZReport = false;
                    break;

                case "REFUND_TICKET":
                    if (parts.length < 3) {
                        if (parts.length == 2) {
                            String sellTicketCommand2 = String.format("COMMAND: REFUND_TICKET\t%s", parts[1]);
                            FileOutput.writeToFile(args[1], sellTicketCommand2, true, true);
                        }
                        if (parts.length == 1) {
                            String sellTicketCommand1 = String.format("COMMAND: REFUND_TICKET");
                            FileOutput.writeToFile(args[1], sellTicketCommand1, true, true);
                        }
                        FileOutput.writeToFile(args[1], "ERROR: Erroneous usage of \"REFUND_TICKET\" command!", true, true);
                    } else {
                        String refundTicketCommand = String.format("COMMAND: REFUND_TICKET\t%s\t%s", parts[1], parts[2]);
                        FileOutput.writeToFile(args[1], refundTicketCommand, true, true);
                        RefundTicket.refunder(Integer.parseInt(parts[1]), parts[2], args);
                    }
                    lastCommandIsZReport = false;
                    break;
                case "CANCEL_VOYAGE":
                    if (parts.length < 3) {
                        if (parts.length == 1) {
                            FileOutput.writeToFile(args[1], "COMMAND: CANCEL_VOYAGE", true, true);
                            FileOutput.writeToFile(args[1], "ERROR: Erroneous usage of \"CANCEL_VOYAGE\" command!", true, true);

                        } else {
                            int busId = Integer.parseInt(parts[1]);
                            String cancelVoyageCommand = String.format("COMMAND: CANCEL_VOYAGE\t%s", parts[1]);
                            FileOutput.writeToFile(args[1], cancelVoyageCommand, true, true);
                            Bus bus = BookingSystem.getBusById(busId);
                            if(Integer.parseInt(parts[1]) <= 0){
                                FileOutput.writeToFile(args[1], "ERROR: " + busId + " is not a positive integer, ID of a voyage must be a positive integer!", true, true);
                            } else if (bus == null) {
                                FileOutput.writeToFile(args[1], "ERROR: There is no voyage with ID of " + busId + "!", true, true);
                            } else  {
                                CancelVoyage.printVoyageCancellation(Integer.parseInt(parts[1]), args);
                                BookingSystem.removeBus(busId);
                            }
                        }

                    } else {
                        String cancelVoyageCommand1 = String.format("COMMAND: CANCEL_VOYAGE\t%s\t%s", parts[1],parts[2]);
                        FileOutput.writeToFile(args[1], cancelVoyageCommand1, true, true);
                        FileOutput.writeToFile(args[1], "ERROR: Erroneous usage of \"CANCEL_VOYAGE\" command!", true, true);
                    }

                    lastCommandIsZReport = false;
                    break;

                case "PRINT_VOYAGE":
                    if (parts.length < 2) {
                        FileOutput.writeToFile(args[1], "COMMAND: PRINT_VOYAGE", true, true);
                        FileOutput.writeToFile(args[1], "ERROR: Erroneous usage of \"PRINT_VOYAGE\" command!", true, true);
                    } else {
                        try {
                            int voyageId = Integer.parseInt(parts[1]);
                            FileOutput.writeToFile(args[1], "COMMAND: PRINT_VOYAGE\t" + parts[1], true, true);
                            Print.printVoyage(voyageId, args);
                        } catch (NumberFormatException e) {
                            FileOutput.writeToFile(args[1], "ERROR: " + parts[1] + " is not a positive integer, ID of a voyage must be a positive integer!", true, true);
                        }
                    }
                    lastCommandIsZReport = false;
                    break;

                default:
                    if (parts.length == 1) {
                        FileOutput.writeToFile(args[1], "COMMAND: " + parts[0], true, true);
                        FileOutput.writeToFile(args[1], "ERROR: There is no command namely " + parts[0] + "!", true, true);
                    } else {
                        FileOutput.writeToFile(args[1], "COMMAND: " + parts[0] + "\t" + parts[1], true, true);
                        FileOutput.writeToFile(args[1], "ERROR: There is no command namely " + parts[0] + "!", true, true);

                    }
                    lastCommandIsZReport = false;
                    break;
            }
        }
        if (!lastCommandIsZReport) {
            FileOutput.writeToFile(args[1], "Z Report:", true, true);
            FileOutput.writeToFile(args[1], "----------------", true, true);
            Print.printZReport(args);
        }
    }
}

