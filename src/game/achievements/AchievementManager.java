package game.achievements;

import java.util.*;

public class AchievementManager extends Object{

    private final Map<String, Achievement> achievementMap = new HashMap<>();
    private final AchievementFile achievementFile;
    private final Set<String> loggedAchievements;

    public AchievementManager(AchievementFile achievementFile) {
        if (achievementFile == null) {
            throw new IllegalArgumentException();
        }
        this.achievementFile = achievementFile;
        this.loggedAchievements = new HashSet<>();
    }

    public void addAchievement(Achievement achievement) {
        for (String name: achievementMap.keySet()) {
            if (achievement.getName().equals(name)) {
                throw new IllegalArgumentException();
            }
        }
        achievementMap.put(achievement.getName(), achievement);
    }

    public void updateAchievement(String achievementName, double absoluteProgressValue) {
        boolean found = false;
        for (String name: achievementMap.keySet()) {
            if (achievementName.equals(name)) {
                achievementMap.get(name).setProgress(absoluteProgressValue);
                found = true;
            }
        }
        if (!found) {
            throw new IllegalArgumentException();
        }
    }

    public void logAchievementMastered() {
        for (Achievement achievement : achievementMap.values()) {
            if (achievement.getProgress() == 1.0 && loggedAchievements.contains(achievement.getName())) {
                String logData = "Achievement Mastered: " + achievement.getName() + " - " + achievement.getDescription();
                achievementFile.save(logData);
                loggedAchievements.add(achievement.getName());
            }
        }
    }

    public List<Achievement> getAchievements() {
        return new ArrayList<>(achievementMap.values());
    }
}
