package game.core;

import game.exceptions.BoundaryExceededException;
import game.utility.Direction;
import static game.GameModel.*;

/**
 * Represents a controllable object in the space game.
 */
public abstract class Controllable extends ObjectWithPosition {

    /**
     * Creates a controllable object at the given coordinates.
     *
     * @param x the given x coordinate
     * @param y the given y coordinate
     */
    public Controllable(int x, int y) {
        super(x, y);
    }

    /**
     * Moves the Controllable by one in the direction given.<br>
     * Throws BoundaryExceededException if the Controllable is attempting to move outside the game boundaries.<br>
     * A controllable is considered outside the game boundaries if they are at: <br>
     * x-coordinate &gt;= GAME_WIDTH<br>
     * x-coordinate &lt; 0<br>
     * y-coordinate &gt;= GAME_HEIGHT<br>
     * y-coordinate &lt; 0<br>
     * <p>
     * Argument given to the exception is "Cannot move {up/down/left/right}. Out of bounds!" depending on the direction.
     *
     * @param direction the given direction.
     *
     * @throws BoundaryExceededException if attempting to move outside the game boundaries.
     * @hint game dimensions are stored in the model.
     */
    public void move(Direction direction) throws BoundaryExceededException {
        if (direction == Direction.UP) {
            int newY = getY() - 1;
            if (newY < 0) {
                throw new BoundaryExceededException("Cannot move up. Out of Bounds!");
            } else {
                this.y += -1;
            }
        } else if (direction == Direction.DOWN) {
            int newY = getY() + 1;
            if (newY >= GAME_HEIGHT) {
                throw new BoundaryExceededException("Cannot move down. Out of Bounds!");
            } else {
                this.y += 1;
            }
        } else if (direction == Direction.RIGHT) {
            int newX = getX() + 1;
            if (newX >= GAME_WIDTH) {
                throw new BoundaryExceededException("Cannot move right. Out of Bounds!");
            } else {
                this.x += 1;
            }
        } else if (direction == Direction.LEFT) {
            int newX = getX() - 1;
            if (newX < 0) {
                throw new BoundaryExceededException("Cannot move left. Out of Bounds!");
            } else {
                this.x += -1;
            }
        }
    }
}
