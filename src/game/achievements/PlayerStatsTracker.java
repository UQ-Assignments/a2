package game.achievements;

/**
 * Tracks the statistics of a player's shots, including the number of shots fired,
 * the number of successful hits, and calculates shooting accuracy.
 * It also tracks the elapsed time since the tracker was initialized.
 */
public class PlayerStatsTracker {
    private final long startingTime;
    private int shotFired = 0;
    private int shotHit = 0;

    /**
     * Constructs a PlayerStatsTracker with a custom start time.
     *
     * @param startTime the time when the tracking began, in milliseconds.
     */
    public PlayerStatsTracker(long startTime) {
        this.startingTime = startTime;
    }

    /**
     * Constructs a PlayerStatsTracker with the current system time (in milliseconds) as the start time.
     */
    public PlayerStatsTracker() {
        this.startingTime = System.currentTimeMillis();
    }

    /**
     * Records the player firing one shot, by incrementing the shots fired by 1.
     */
    public void recordShotFired() {
        this.shotFired++;
    }

    /**
     * Records the player hitting one target, by incrementing the shots hit by 1.
     */
    public void recordShotHit() {
        this.shotHit++;
    }

    /**
     * Returns the total number of shots that the player has fired.
     *
     * @return the number of shots fired.
     */
    public int getShotsFired() {
        return this.shotFired;
    }

    /**
     * Returns the total number of shots the player has successfully hit.
     *
     * @return the number of shots hit.
     */
    public int getShotsHit() {
        return this.shotHit;
    }

    /**
     * Returns the number of seconds elapsed since the tracker started.
     *
     * @return the elapsed time in seconds.
     */
    public long getElapsedSeconds() {
        return (System.currentTimeMillis() - startingTime) / 1000;
    }

    /**
     * Returns the player's shooting accuracy percentage.
     * Accuracy is calculated as shots hit divided by shots fired.
     * Unless no shots are fired, in which case accuracy is 0.0.
     *
     * @return the shooting average percentage as a decimal.
     */
    public double getAccuracy() {
        if (this.shotFired == 0) {
            return 0.0;
        }
        return (double) this.shotHit / this.shotFired;
    }
}
