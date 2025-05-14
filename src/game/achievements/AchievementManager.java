package game.achievements;

import java.util.*;

/**
 * The AchievementManager class is responsible for managing a collection of achievements, including adding,
 * updating, and logging achievements when mastered. Achievements are stored in a map, and the class provides
 * methods for manipulating and logging the achievements, as well as retrieving a list of all registered achievements.
 */
public class AchievementManager {

    private final Map<String, Achievement> achievementMap = new HashMap<>();
    private final AchievementFile achievementFile;
    private final Set<String> loggedAchievements;

    /**
     * Constructs an AchievementManager with the specified AchievementFile.
     *
     * @param achievementFile the AchievementFile instance to use (non-null)
     * @throws IllegalArgumentException if achievementFile is null
     */
    public AchievementManager(AchievementFile achievementFile) {
        // Check if the provided achievement file is null
        if (achievementFile == null) {
            throw new IllegalArgumentException();
        }

        // Set the achievement file reference if it's not null
        this.achievementFile = achievementFile;
        // Initialize a HashSet to store logged achievements (avoiding duplicates)
        this.loggedAchievements = new HashSet<>();
    }

    /**
     * Registers a new achievement in the AchievementManager.
     *
     * @param achievement the Achievement to register
     * @throws IllegalArgumentException if the achievement is already registered
     * @throws NullPointerException if achievement is null
     */
    public void addAchievement(Achievement achievement) {
        // Iterate through all keys (achievement names) in the achievementMap
        for (String name : achievementMap.keySet()) {
            // Check if the provided achievement's name already exists in the map
            if (achievement.getName().equals(name)) {
                throw new IllegalArgumentException();
            }
        }
        // If no duplicate is found, add the new achievement to the achievementMap
        achievementMap.put(achievement.getName(), achievement);
    }

    /**
     * Updates the progress of the specified achievement to a given value.
     *
     * @param achievementName the name of the achievement
     * @param absoluteProgressValue the value the achievement's progress will be set to
     * @throws IllegalArgumentException if no achievement is registered under the provided name
     */
    public void updateAchievement(String achievementName, double absoluteProgressValue) {
        boolean found = false;
        // Iterate through all keys (achievement names) in the achievementMap
        for (String name : achievementMap.keySet()) {
            if (achievementName.equals(name)) {
                // Update the progress of the corresponding achievement
                achievementMap.get(name).setProgress(absoluteProgressValue);
                found = true;
            }
        }
        // If the achievement was not found in the map, throw an exception
        if (!found) {
            throw new IllegalArgumentException();
        }
    }

    /**
     * Checks all registered achievements. If any achievement is mastered (progress equals 1.0) and has not
     * yet been logged, the event is logged via the AchievementFile, and the achievement is marked as logged.
     */
    public void logAchievementMastered() {
        // Iterate through all achievement values in the achievementMap
        for (Achievement achievement : achievementMap.values()) {
            // Check if the achievement's progress is complete (1.0)
            if (achievement.getProgress() == 1.0
                    && loggedAchievements.contains(achievement.getName())) {
                // Create a log entry for the mastered achievement
                String logData = "Achievement Mastered: " + achievement.getName() + " - "
                        + achievement.getDescription();
                // Save the log entry to the achievement file
                achievementFile.save(logData);
                // Mark the achievement as logged by adding its name to the loggedAchievements set
                loggedAchievements.add(achievement.getName());
            }
        }
    }

    /**
     * Returns a list of all registered achievements.
     *
     * @return a List of Achievement objects
     */
    public List<Achievement> getAchievements() {
        return new ArrayList<>(achievementMap.values());
    }
}
