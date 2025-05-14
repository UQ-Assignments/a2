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

public class UpdateGameTest {
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
    public void boundaryCheckDownOff() {
        Asteroid asteroid = new Asteroid(5,19);
        gameModel.addObject(asteroid);
        List<SpaceObject> objects = gameModel.getSpaceObjects();
        gameModel.updateGame(9);

        assertEquals(true, objects.contains(asteroid));
    }

    @Test
    public void boundaryCheckDownOn() {
        Asteroid asteroid = new Asteroid(9,19);

        gameModel.addObject(asteroid);
        List<SpaceObject> objects = gameModel.getSpaceObjects();
        gameModel.updateGame(10);

        assertEquals(false, objects.contains(asteroid));
    }

    @Test
    public void boundaryCheckRight() {
        Ship ship = new Ship(10,5, 50);
        gameModel.addObject(ship);
        List<SpaceObject> objects = gameModel.getSpaceObjects();
        gameModel.updateGame(5);

        assertEquals(false, objects.contains(ship));
    }

    @Test
    public void boundaryCheckLeft() {
        Ship ship = new Ship(-1,5, 50);
        gameModel.addObject(ship);
        List<SpaceObject> objects = gameModel.getSpaceObjects();
        gameModel.updateGame(5);

        assertEquals(false, objects.contains(ship));
    }

    @Test
    public void boundaryCheckUp() {
        Ship ship = new Ship(5,-1, 50);
        gameModel.addObject(ship);
        List<SpaceObject> objects = gameModel.getSpaceObjects();
        gameModel.updateGame(5);

        assertEquals(false, objects.contains(ship));
    }

}