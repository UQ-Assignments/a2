package game.achievements;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class FileHandler extends Object implements AchievementFile {

    public static final String DEFAULT_FILE_LOCATION = "achievements.log";
    private String fileLocation;


    public FileHandler() {
        this.fileLocation = DEFAULT_FILE_LOCATION;
    }

    public void setFileLocation(String fileLocation) {
        this.fileLocation = fileLocation;
    }

    public String getFileLocation() {
        return this.fileLocation;
    }

    public void save(String data) {
        try (FileWriter writer = new FileWriter(this.fileLocation, true)) {
            writer.write(data + System.lineSeparator());
        } catch (IOException e) {

        }
    }

    public List<String> read() {
        List<String> lines = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(this.fileLocation))) {
            String line;
            while ((line = reader.readLine()) != null) {
                lines.add(line);
            }

        } catch (IOException e) {

        }

        return lines;
    }

}
