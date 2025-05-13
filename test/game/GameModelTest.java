package game;

import game.achievements.Achievement;
import game.achievements.AchievementManager;
import game.achievements.FileHandler;
import game.core.SpaceObject;
import game.ui.KeyHandler;
import game.ui.Tickable;
import game.ui.UI;
import org.junit.Before;
import org.junit.Test;


import java.util.List;

import static org.junit.Assert.assertEquals;

public class GameModelTest {
    GameController gameController;
    GameModel gameModel;
    public String lastLog;

    @Before
    public void initialize() {
        FileHandler fileHandler = new FileHandler();
        AchievementManager achievementManager = new AchievementManager(fileHandler);
        gameController = new GameController(new UI() {
            @Override
            public void start() {

            }

            @Override
            public void pause() {

            }

            @Override
            public void stop() {

            }

            @Override
            public void onStep(Tickable tickable) {

            }

            @Override
            public void onKey(KeyHandler key) {

            }

            @Override
            public void render(List<SpaceObject> objects) {

            }

            @Override
            public void log(String message) {
                lastLog = message;

            }

            @Override
            public void setStat(String label, String value) {

            }

            @Override
            public void logAchievementMastered(String message) {

            }

            @Override
            public void logAchievements(List<Achievement> achievements) {

            }

            @Override
            public void setAchievementProgressStat(String achievementName, double progressPercentage) {

            }
        }, achievementManager);
        gameModel = gameController.getModel();
    }

    @Test
    public void checkGameOverFalse() {
        boolean expectedTruth = false;
        assertEquals(expectedTruth, gameModel.checkGameOver());
    }

    @Test
    public void checkGameOverTrue() {
        boolean expectedTruth = true;
        gameModel.getShip().takeDamage(100);

        assertEquals(expectedTruth, gameModel.checkGameOver());
    }

    @Test
    public void testPause() {
        lastLog = "";
        gameController.handlePlayerInput("P");
        String expected = "Game paused." + System.lineSeparator();
        assertEquals(expected, lastLog);
    }

    @Test
    public void verboseLevelUp() {
        lastLog = "";
        gameModel.setVerbose(true);
        int levelUp = gameModel.getLevel();

        gameModel.getShip().addScore(50);
        assertEquals(levelUp, gameModel.getLevel());
        assertEquals("", lastLog);

        lastLog = "";
        gameModel.getShip().addScore(50);
        gameModel.levelUp();
        assertEquals(levelUp + 1, gameModel.getLevel());
        String message1 = "Level Up! Welcome to Level " + levelUp + 1 + ". Spawn rate increased to " + 7 + "%." + System.lineSeparator();
        assertEquals(message1, lastLog);

        lastLog = "";
        gameModel.getShip().addScore(200);
        gameModel.levelUp();
        assertEquals(levelUp + 2, gameModel.getLevel());
        String message2 = "Level Up! Welcome to Level " + levelUp + 2 + ". Spawn rate increased to " + 12 + "%." + System.lineSeparator();
        assertEquals(message2, lastLog);
    }

    @Test
    public void noVerboseLevelUp() {
        lastLog = "";
        gameModel.setVerbose(false);
        int levelUp = gameModel.getLevel();

        gameModel.getShip().addScore(50);
        assertEquals(levelUp, gameModel.getLevel());
        assertEquals("", lastLog);

        lastLog = "";
        gameModel.getShip().addScore(50);
        gameModel.levelUp();
        assertEquals(levelUp + 1, gameModel.getLevel());
        assertEquals("", lastLog);

        lastLog = "";
        gameModel.getShip().addScore(200);
        gameModel.levelUp();
        assertEquals(levelUp + 2, gameModel.getLevel());
        assertEquals("", lastLog);
    }
}