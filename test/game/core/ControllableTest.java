package game.core;

import game.exceptions.BoundaryExceededException;
import game.utility.Direction;
import org.junit.Assert;
import org.junit.Test;
import static org.junit.Assert.*;

public class ControllableTest {


    public void moveTests() {
        moveRightError();
//        moveLeftError();
//        moveUpError();
//        moveDownError();
    }

//    @Test
//    public void moveDownError() {
//        Ship ship = new Ship(5,9, 50);
//
//        try {
//            // Attempt to move the ship up out of bounds (y = 0 -> invalid move)
//            ship.move(Direction.DOWN);
//        } catch (BoundaryExceededException e) {
//            assertEquals("Cannot move down. Out of Bounds!", e.getMessage());
//        }
//    }
//
//    @Test
//    public void moveUpError() {
//        Ship ship = new Ship(5,0, 50);
//
//        try {
//            // Attempt to move the ship up out of bounds (y = 0 -> invalid move)
//            ship.move(Direction.UP);
//        } catch (BoundaryExceededException e) {
//            assertEquals("Cannot move up. Out of Bounds!", e.getMessage());
//        }
//    }
//
//    @Test
//    public void moveLeftError() {
//        Ship ship = new Ship(0,5, 50);
//
//        try {
//            // Attempt to move the ship up out of bounds (y = 0 -> invalid move)
//            ship.move(Direction.LEFT);
//        } catch (BoundaryExceededException e) {
//            assertEquals("Cannot move left. Out of Bounds!", e.getMessage());
//        }
//    }

    @Test
    public void moveRightError() {
        Ship ship = new Ship(9,5, 50);

        try {
            // Attempt to move the ship up out of bounds (y = 0 -> invalid move)
            ship.move(Direction.RIGHT);
        } catch (BoundaryExceededException e) {
            assertEquals("Cannot move right. Out of Bounds!", e.getMessage());
        }
    }







}