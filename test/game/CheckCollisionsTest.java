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

public class CheckCollisionsTest {
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
    public void collisionPrintingAsteroid() {
        lastLog = "";
        gameModel.setVerbose(true);

        Asteroid asteroid = new Asteroid(5,10);

        gameModel.addObject(asteroid);
        gameModel.checkCollisions();

        assertEquals(90, gameModel.getShip().getHealth());

        String expected = "Hit by " + asteroid.render() + "! Health reduced by " + 10 + ".";
        assertEquals(expected, lastLog);

    }

    @Test
    public void collisionPrintingEnemy() {
        lastLog = "";
        gameModel.setVerbose(true);
        Enemy enemy = new Enemy(5,10);

        gameModel.addObject(enemy);
        gameModel.checkCollisions();

        assertEquals(80, gameModel.getShip().getHealth());
        String expected = "Hit by " + enemy.render() + "! Health reduced by " + 20 + ".";
        assertEquals(expected, lastLog);
    }

    @Test
    public void collisionPrintingHeal() {
        lastLog = "";
        gameModel.setVerbose(true);
        HealthPowerUp healthPowerUp = new HealthPowerUp(5,10);

        gameModel.addObject(healthPowerUp);
        gameModel.checkCollisions();

        String expected = "PowerUp collected: " + healthPowerUp.render();
        assertEquals(expected, lastLog);
    }

    @Test
    public void collisionPrintingShield() {
        lastLog = "";
        gameModel.setVerbose(true);
        ShieldPowerUp shieldPowerUp = new ShieldPowerUp(5,10);

        gameModel.addObject(shieldPowerUp);
        gameModel.checkCollisions();

        String expected = "PowerUp collected: " + shieldPowerUp.render();
        assertEquals(expected, lastLog);
    }

    @Test
    public void collisionNoPrint() {
        lastLog = "";
        gameModel.setVerbose(false);
        assertEquals("", lastLog);
    }

    @Test
    public void collisionBulletEnemy() {
        lastLog = "";
        Bullet bullet = new Bullet(5,5);
        Enemy enemy = new Enemy(5,5);

        gameModel.addObject(bullet);
        gameModel.addObject(enemy);

        gameModel.checkCollisions();
        assertEquals("", lastLog);
    }

    @Test
    public void collisionBulletAsteroid() {
        lastLog = "";
        Bullet bullet = new Bullet(5,5);
        Asteroid asteroid = new Asteroid(5,5);

        gameModel.addObject(bullet);
        gameModel.addObject(asteroid);

        gameModel.checkCollisions();
        assertEquals("", lastLog);

        List<SpaceObject> objects = gameModel.getSpaceObjects();
        assertEquals(false, objects.contains(bullet));
        assertEquals(true, objects.contains(asteroid));
    }
}