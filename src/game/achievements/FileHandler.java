package game.achievements;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * The FileHandler class is an implementation of the AchievementFile interface. It handles file operations
 * for saving and reading achievement data. The class allows setting a custom file location, saving data
 * to that file, and reading data from the file. By default, the file is saved to "achievements.log".
 */
public class FileHandler implements AchievementFile {

    public static final String DEFAULT_FILE_LOCATION = "achievements.log";
    private String fileLocation;

    /**
     * Constructs a FileHandler that uses the default file location ("achievements.log") for saving data.
     */
    public FileHandler() {
        this.fileLocation = DEFAULT_FILE_LOCATION;
    }

    /**
     * Sets the file location to which data will be saved.
     *
     * @param fileLocation the new file location
     */
    public void setFileLocation(String fileLocation) {
        this.fileLocation = fileLocation;
    }

    /**
     * Gets the current file location where data is being saved.
     *
     * @return the current file location
     */
    public String getFileLocation() {
        return this.fileLocation;
    }

    /**
     * Saves the given data to the current file location, appending the data with a new line.
     *
     * @param data the data to be saved
     */
    public void save(String data) {
        // Open the file in append mode using a FileWriter
        try (FileWriter writer = new FileWriter(this.fileLocation, true)) {
            // Write the data followed by a new line separator to the file
            writer.write(data + System.lineSeparator());
        } catch (IOException e) {
            System.out.println("Error");
        }
    }

    /**
     * Loads and returns all previously saved data from the current file location as a list of strings.
     *
     * @return a list of saved data entries
     */
    public List<String> read() {
        // Create a list to store the lines read from the file
        List<String> lines = new ArrayList<>();
        // Try reading from the file using BufferedReader
        try (BufferedReader reader = new BufferedReader(new FileReader(this.fileLocation))) {
            String line;
            // Read each line of the file until end of file is reached
            while ((line = reader.readLine()) != null) {
                lines.add(line);
            }
        } catch (IOException e) {
            System.out.println("Error");
        }
        // Return the list of lines read from the file
        return lines;
    }
}
