package game;

import game.achievements.AchievementManager;
import game.achievements.FileHandler;
import game.ui.gui.GUI;
import org.junit.Before;
import org.junit.Test;


import static org.junit.Assert.assertEquals;

public class GameModelTest {
    GameController gameController;
    GameModel gameModel;
    GUI gui;

    @Before
    public void initialize() {
        gui = new GUI();
        FileHandler fileHandler = new FileHandler();
        AchievementManager achievementManager = new AchievementManager(fileHandler);
        gameController = new GameController(gui, achievementManager);
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
        gameController.handlePlayerInput("P");

    }
}