package game.core;

import org.junit.Test;
import static org.junit.Assert.*;

public class ObjectWithPositionTest {

    @Test
    public void testShipToString() {
        Ship ship = new Ship(4, 4, 50);
        String expected = "Ship(4, 4)";
        assertEquals(expected, ship.toString());
    }

    @Test
    public void testBulletToString() {
        Bullet bullet = new Bullet(5, 5);
        String expected = "Bullet(5, 5)";
        assertEquals(expected, bullet.toString());
    }

    @Test
    public void testAsteroidToString() {
        Asteroid asteroid = new Asteroid(6, 6);
        String expected = "Asteroid(6, 6)";
        assertEquals(expected, asteroid.toString());
    }

    @Test
    public void testEnemyToString() {
        Enemy enemy = new Enemy(7, 7);
        String expected = "Enemy(7, 7)";
        assertEquals(expected, enemy.toString());
    }

    @Test
    public void testHealthPowerUpToString() {
        HealthPowerUp healthPowerUp = new HealthPowerUp(8, 8);
        String expected = "HealthPowerUp(8, 8)";
        assertEquals(expected, healthPowerUp.toString());

        ShieldPowerUp shieldPowerUp = new ShieldPowerUp(9, 9);
        String expected2 = "ShieldPowerUp(9, 9)";
        assertEquals(expected2, shieldPowerUp.toString());
    }

//    @Test
//    public void testShieldPowerUpToString() {
//        ShieldPowerUp shieldPowerUp = new ShieldPowerUp(9, 9);
//        String expected = "ShieldPowerUp(9, 9)";
//        assertEquals(expected, shieldPowerUp.toString());
//    }
}
