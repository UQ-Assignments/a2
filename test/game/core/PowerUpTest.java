package game.core;

import org.junit.Test;

import static org.junit.Assert.*;

public class PowerUpTest {

    @Test
    public void ShieldMovingDownTrue() {
        ShieldPowerUp shieldPowerUp = new ShieldPowerUp(5,5);
        shieldPowerUp.tick(9);
        int x = 5;
        int y = 5;
        assertEquals(y, shieldPowerUp.getY());
        assertEquals(x, shieldPowerUp.getX());
    }

    @Test
    public void ShieldMovingDownFalse() {
        ShieldPowerUp shieldPowerUp = new ShieldPowerUp(5,5);
        shieldPowerUp.tick(10);
        int x = 5;
        int y = 6;
        assertEquals(y, shieldPowerUp.getY());
        assertEquals(x, shieldPowerUp.getX());
    }
}