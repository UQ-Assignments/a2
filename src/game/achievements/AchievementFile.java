package game.achievements;

import java.util.List;

public interface AchievementFile {
    static final String DEFAULT_FILE_LOCATION =
            "achievements.log";
    public void setFileLocation(String fileLocation);
    public String getFileLocation();
    public void save(String save);
    public List<String> read();
}
