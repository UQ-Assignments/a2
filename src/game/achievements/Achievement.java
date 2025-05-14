package game.achievements;

/**
 * The Achievement interface represents a game achievement that tracks progress and assigns tiers based on that progress.
 */
public interface Achievement {

    /**
     * Returns the unique name of the achievement.
     *
     * @return the name of the achievement.
     */
    public String getName();

    /**
     * Returns a description of the achievement, explaining its purpose or what it tracks.
     *
     * @return a description of the achievement.
     */
    public String getDescription();

    /**
     * Returns the current progress of the achievement as a value between 0.0 (0%) and 1.0 (100%).
     *
     * @return the current progress of the achievement as a double.
     * @ensure 0.0 <= getProgress() <= 1.0
     */
    public double getProgress();

    /**
     * Sets the progress to the specified value.
     *
     * @param newProgress the updated progress value, which must be between 0.0 and 1.0, inclusive.
     * @require 0.0 <= newProgress <= 1.0
     * @ensure getProgress() == newProgress, getProgress() <= 1.0, getProgress() >= 0.0
     */
    public void setProgress(double newProgress);

    /**
     * Returns the current tier of the achievement based on the progress.
     * The tiers are:
     * - "Novice" if progress is less than 0.5,
     * - "Expert" if progress is between 0.5 and 0.999,
     * - "Master" if progress is greater than or equal to 0.999.
     *
     * @return a string representing the current tier ("Novice", "Expert", or "Master").
     */
    public String getCurrentTier();
}
