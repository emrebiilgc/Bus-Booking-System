import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
/**
 * The {@code RemoveLine} class provides utility methods to manipulate text files,
 * specifically focusing on removing the last line from a specified file. This can be
 * useful for undoing additions to log files or correcting file outputs.
 */
public class RemoveLine {
    /**
     * Removes the last line from the file specified by the path in the command-line arguments.
     * It reads the current contents of the file, removes the last line, and then rewrites the file.
     *
     * @param args Command-line arguments where args[1] should contain the path of the file to be modified.
     */
    public static void removeLastLine(String[] args)  {

        String[] data = FileInput.readFile(args[1], true, true);

        List<String> lines = new ArrayList<>(Arrays.asList(data));

        if (!lines.isEmpty()) {
            lines.remove(lines.size() - 1);
        }
        clearFile(args);
        for (String line : lines) {
            FileOutput.writeToFile(args[1], line, true, true);
        }
    }
    /**
     * Clears the content of the file specified by the path.
     * This method is used internally to reset the file content before rewriting it.
     *
     * @param args Command-line arguments where args[1] should contain the path of the file to be modified.
     */
    private static void clearFile(String[] args) {
        FileOutput.writeToFile(args[1], "", false, false);
    }
}
