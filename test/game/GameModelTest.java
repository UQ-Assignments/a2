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
        testGamePause();
        testNoMovementWhenPaused();
        testGameUnpause();
        testMovementAfterUnpause();
    }

    private void testGamePause() {
        lastLog = "";
        gameController.handlePlayerInput("P"); // Pause the game
        assertEquals("Game paused.", lastLog);
    }

    private void testNoMovementWhenPaused() {
        int originalY = gameModel.getShip().getY();
        int originalX = gameModel.getShip().getX();

        gameController.handlePlayerInput("W");
        gameController.handlePlayerInput("A");
        gameController.handlePlayerInput("S");
        gameController.handlePlayerInput("D");

        assertEquals(originalY, gameModel.getShip().getY());
        assertEquals(originalX, gameModel.getShip().getX());
    }

    private void testGameUnpause() {
        gameController.handlePlayerInput("P");
        assertEquals("Game unpaused.", lastLog);
    }

    private void testMovementAfterUnpause() {
        int originalY = gameModel.getShip().getY();
        int originalX = gameModel.getShip().getX();

        gameController.handlePlayerInput("W");
        assertEquals(originalY - 1, gameModel.getShip().getY());
        assertEquals(originalX, gameModel.getShip().getX());
    }

    @Test
    public void invalidInput() {
        testInvalidInputKey();
        testInvalidInputMessage();
    }

    private void testInvalidInputKey() {
        lastLog = "";
        int originalY = gameModel.getShip().getY();
        int originalX = gameModel.getShip().getX();

        gameController.handlePlayerInput("g");

        assertEquals(originalY, gameModel.getShip().getY());
        assertEquals(originalX, gameModel.getShip().getX());
    }

    private void testInvalidInputMessage() {
        assertEquals("Invalid input. Use W, A, S, D, F, or P.", lastLog);
    }


    @Test
    public void inputsWithVerbose() {
        testMoveWithVerboseEnabled();
        testMoveWithVerboseDisabled();
    }

    private void testMoveWithVerboseEnabled() {
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

    private void testMoveWithVerboseDisabled() {
        gameController.setVerbose(false);
        lastLog = "";
        gameController.handlePlayerInput("W");
        assertEquals("", lastLog);
    }


    @Test
    public void verboseLevelUp() {
        testNoLevelUpMessageWhenScoreIsLow();
        testLevelUpMessageWhenScoreIncreases();
        testLevelUpMessageWhenScoreIncreasesAgain();
    }

    private void testNoLevelUpMessageWhenScoreIsLow() {
        lastLog = "";
        gameController.setVerbose(true);
        int levelUp = gameModel.getLevel();

        gameModel.getShip().addScore(50);
        assertEquals(levelUp, gameModel.getLevel());
        assertEquals("", lastLog);
    }

    private void testLevelUpMessageWhenScoreIncreases() {
        lastLog = "";
        gameModel.getShip().addScore(50);
        gameModel.levelUp();
        int levelUp = gameModel.getLevel();
        String message1 = "Level Up! Welcome to Level " + (levelUp) + ". Spawn rate increased to " + 7 + "%.";
        assertEquals(message1, lastLog);
    }

    private void testLevelUpMessageWhenScoreIncreasesAgain() {
        lastLog = "";
        gameModel.getShip().addScore(200);
        gameModel.levelUp();
        int levelUp = gameModel.getLevel();
        String message2 = "Level Up! Welcome to Level " + (levelUp) + ". Spawn rate increased to " + 12 + "%.";
        assertEquals(message2, lastLog);
    }

    @Test
    public void noVerboseLevelUp() {
        testNoLevelUpMessageWhenScoreIsLow();
        testNoLevelUpMessageWhenScoreIncreases();
        testNoLevelUpMessageWhenScoreIncreasesAgain();
    }

    private void NoLevelUpMessageWhenScoreIsLowTest() {
        lastLog = "";
        gameController.setVerbose(false);
        int levelUp = gameModel.getLevel();

        gameModel.getShip().addScore(50);
        assertEquals(levelUp, gameModel.getLevel());
        assertEquals("", lastLog);
    }

    private void testNoLevelUpMessageWhenScoreIncreases() {
        lastLog = "";
        gameModel.getShip().addScore(50);
        gameModel.levelUp();
        int levelUp = gameModel.getLevel();
        assertEquals(levelUp + 1, gameModel.getLevel());
        assertEquals("", lastLog);
    }

    private void testNoLevelUpMessageWhenScoreIncreasesAgain() {
        lastLog = "";
        gameModel.getShip().addScore(200);
        gameModel.levelUp();
        int levelUp = gameModel.getLevel();
        assertEquals(levelUp + 2, gameModel.getLevel());
        assertEquals("", lastLog);
    }





}