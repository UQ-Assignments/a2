package game.achievements;

public class GameAchievement implements Achievement {

    private final String name;
    private final String description;
    private double progress;

    public GameAchievement(String name, String description) {
        this.name = name;
        this.description = description;
        this.progress = 0.0;
    }

    public String getName() {
        return this.name;
    }

    public String getDescription() {
        return this.description;
    }

    public double getProgress() {
        return this.progress;
    }

    public void setProgress(double newProgress) {
        this.progress = newProgress;
    }

    public String getCurrentTier() {
        if (getProgress() < 0.5) {
            return "Novice";
        } else if (0.5 <= getProgress() && getProgress() < 0.999) {
            return "Expert";
        } else {
            return "Master";
        }

    }
}
