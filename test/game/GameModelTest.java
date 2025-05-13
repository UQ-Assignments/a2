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
    public  void testGamePause() {
        lastLog = "";
        gameController.handlePlayerInput("P"); // Pause the game
        assertEquals("Game paused.", lastLog);
    }

    @Test
    public void testGameUnpause() {
        lastLog = "";
        gameController.handlePlayerInput("P");
        gameController.handlePlayerInput("P");
        assertEquals("Game unpaused.", lastLog);
    }

    @Test
    public void testNoMovementWhenPaused() {
        gameController.handlePlayerInput("P");
        int originalY = gameModel.getShip().getY();
        int originalX = gameModel.getShip().getX();

        gameController.handlePlayerInput("W");
        gameController.handlePlayerInput("A");

        assertEquals(originalY, gameModel.getShip().getY());
        assertEquals(originalX, gameModel.getShip().getX());
    }

    @Test
    public void testInvalidInputKey() {
        lastLog = "";
        int originalY = gameModel.getShip().getY();
        int originalX = gameModel.getShip().getX();

        gameController.handlePlayerInput("g");

        assertEquals(originalY, gameModel.getShip().getY());
        assertEquals(originalX, gameModel.getShip().getX());

        assertEquals("Invalid input. Use W, A, S, D, F, or P.", lastLog);
    }




    @Test
    public void testMoveWithVerboseEnabled() {
        gameController.setVerbose(true);
        lastLog = "";
        int originalY = gameModel.getShip().getY();
        int originalX = gameModel.getShip().getX();
        gameController.handlePlayerInput("W");
        int newY = originalY - 1;

        assertEquals(newY, gameModel.getShip().getY());
        assertEquals(originalX, gameModel.getShip().getX());

        String expected = "Ship moved to (" + originalX + ", " + newY + ")";
        assertEquals(expected, lastLog);
    }

    @Test
    public void testMoveWithVerboseDisabled() {
        gameController.setVerbose(false);
        lastLog = "";
        gameController.handlePlayerInput("W");
        assertEquals("", lastLog);
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
    public void testLevelUpMessageWhenScoreIncreasesAgain() {
        gameModel.setVerbose(true);
        lastLog = "";
        gameModel.getShip().addScore(250);
        gameModel.levelUp();
        int levelUp = gameModel.getLevel();
        String message2 = "Level Up! Welcome to Level " + (levelUp) + ". Spawn rate increased to " + 12 + "%.";
        assertEquals(message2, lastLog);
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

    @Test
    public void testLevelUpHigher() {
        lastLog = "";
        int expectedLevel = gameModel.getLevel() + 2;
        gameModel.getShip().addScore(200);
        gameModel.levelUp();
        assertEquals(expectedLevel, gameModel.getLevel());
        assertEquals("", lastLog);
    }






    @Test
    public void collisionPrinting() {


    }




}