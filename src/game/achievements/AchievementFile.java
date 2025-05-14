package game.achievements;

import java.util.List;

import java.util.List;

/**
 * The AchievementFile interface provides methods to manage the file storage for achievements.
 * It allows saving and reading achievements to and from a specified file location.
 */
public interface AchievementFile {

    /**
     * The default file location used if no custom file location is provided.
     */
    static final String DEFAULT_FILE_LOCATION = "achievements.log";

    /**
     * Sets the file location to save achievements data.
     *
     * @param fileLocation the new file location where achievements data should be saved.
     */
    public void setFileLocation(String fileLocation);

    /**
     * Gets the current file location where achievements data is being saved.
     *
     * @return the file location currently being used for saving achievements.
     */
    public String getFileLocation();

    /**
     * Saves the given data to the file, appending a new-line character after the data.
     *
     * @param data the data to be saved to the file.
     */
    public void save(String data);

    /**
     * Loads and returns all previously saved data as a list of strings.
     *
     * @return a list containing all previously saved data entries.
     */
    public List<String> read();
}

