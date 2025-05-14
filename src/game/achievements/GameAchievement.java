package game.achievements;

/**
 * Represents a game achievement with a name, description, and progress.
 * The achievement has tiers based on the progress, which can be updated
 * and retrieved. The class implements the Achievement interface.
 */
public class GameAchievement implements Achievement {

    private final String name;
    private final String description;
    private double progress;

    /**
     * Constructs an Achievement with the specified name and description.
     * The initial progress is set to 0.
     *
     * @param name the unique name of the achievement. Must not be null or empty.
     * @param description the description of the achievement. Must not be null or empty.
     * @throws IllegalArgumentException if name or description is null or empty.
     */
    public GameAchievement(String name, String description) {
        this.name = name;
        this.description = description;
        this.progress = 0.0;
    }

    /**
     * Returns the unique name of the achievement.
     *
     * @return the name of the achievement.
     */
    @Override
    public String getName() {
        return this.name;
    }

    /**
     * Returns the description of the achievement.
     *
     * @return the description of the achievement.
     */
    @Override
    public String getDescription() {
        return this.description;
    }

    /**
     * Returns the current progress of the achievement.
     * The progress is a value between 0.0 (0%) and 1.0 (100%).
     *
     * @return the current progress as a double.
     */
    @Override
    public double getProgress() {
        return this.progress;
    }

    /**
     * Sets the progress of the achievement to the specified value.
     *
     * @param newProgress the updated progress.
     * @throws IllegalArgumentException if the newProgress is not between 0.0 and 1.0.
     */
    @Override
    public void setProgress(double newProgress) {
        this.progress = newProgress;
    }

    /**
     * Returns the current tier of the achievement based on the progress.
     * The possible tiers are:
     * - "Novice" if progress < 0.5
     * - "Expert" if 0.5 <= progress < 0.999
     * - "Master" if progress >= 0.999
     *
     * @return the current tier of the achievement.
     */
    @Override
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
