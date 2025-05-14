package game;

import game.achievements.Achievement;
import game.achievements.AchievementManager;
import game.achievements.FileHandler;
import game.core.*;
import game.ui.KeyHandler;
import game.ui.Tickable;
import game.ui.UI;
import org.junit.Before;
import org.junit.Test;


import java.util.List;

import static org.junit.Assert.assertEquals;

    public class LevelUpTest {
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
    public void testNoLevelUpVerbose() {
        gameModel.setVerbose(true);
        lastLog = "";
        int levelUp = gameModel.getLevel();

        gameModel.getShip().addScore(50);
        assertEquals(levelUp, gameModel.getLevel());
        assertEquals("", lastLog);
    }

    @Test
    public void testLevelUpVerbose() {
        gameModel.setVerbose(true);
        lastLog = "";
        gameModel.getShip().addScore(100);
        gameModel.levelUp();
        int levelUp = gameModel.getLevel();
        String message1 = "Level Up! Welcome to Level " + (levelUp) + ". Spawn rate increased to " + 7 + "%.";
        assertEquals(message1, lastLog);
    }

    @Test
    public void NoLevelUp() {
        lastLog = "";
        gameController.setVerbose(false);
        int levelUp = gameModel.getLevel();

        gameModel.getShip().addScore(50);
        assertEquals(levelUp, gameModel.getLevel());
        assertEquals("", lastLog);
    }

    @Test
    public void testLevelUp() {
        lastLog = "";
        int expectedLevel = gameModel.getLevel() + 1;
        gameModel.getShip().addScore(150);
        gameModel.levelUp();
        assertEquals(expectedLevel, gameModel.getLevel());
        assertEquals("", lastLog);
    }
}