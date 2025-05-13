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

public class handlePlayerInputTest {
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

}