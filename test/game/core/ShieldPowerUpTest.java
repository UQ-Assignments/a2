package game.core;

import org.junit.Test;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import static org.junit.Assert.*;

public class ShieldPowerUpTest {
    @Test
    public void testApplyEffectHealsShipAndPrintsMessage() {
        var out = new ByteArrayOutputStream();
        System.setOut(new PrintStream(out));

        Ship ship = new Ship(5, 5, 50);

        ShieldPowerUp shield = new ShieldPowerUp(10,10);

        shield.applyEffect(ship);
        assertEquals(150, ship.getScore());

        assertEquals("", out.toString());

    }
}



