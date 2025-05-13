package game.achievements;

public class PlayerStatsTracker {
    private long startingTime;
    private int shotFired = 0;
    private int shotHit = 0;


    public PlayerStatsTracker(long startTime) {
        this.startingTime = startTime;
    }

    public PlayerStatsTracker() {
        this.startingTime = System.currentTimeMillis();
    }

    public void recordShotFired() {
        this.shotFired ++;
    }

    public void recordShotHit() {
        this.shotHit ++;
    }

    public int getShotsFired() {
        return this.shotFired;
    }

    public int getShotsHit() {
        return this.shotHit;
    }

    public long getElapsedSeconds() {
        return (System.currentTimeMillis() - startingTime) / 1000;
    }

    public double getAccuracy() {
        if (this.shotFired == 0) {
            return 0.0;
        }
        return (double)this.shotHit / this.shotFired;
    }
}
