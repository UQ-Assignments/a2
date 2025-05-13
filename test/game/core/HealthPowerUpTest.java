package game.core;

import org.junit.Test;
import static org.junit.Assert.*;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

public class HealthPowerUpTest {
    @Test
    public void testApplyEffectHealsShipAndPrintsMessage() {
        var out = new ByteArrayOutputStream();
        System.setOut(new PrintStream(out));

        Ship ship = new Ship(5, 5, 50);

        HealthPowerUp heal = new HealthPowerUp(10,10);

        heal.applyEffect(ship);
        assertEquals(70, ship.getHealth());
        assertEquals("", out.toString());
    }
}
