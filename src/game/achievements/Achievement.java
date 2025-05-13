package game.achievements;

public interface Achievement {
    public String getName();
    public String getDescription();
    public double getProgress();
    public void setProgress(double newProgress);
    public String getCurrentTier();
}
