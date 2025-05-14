package game.core;

/**
 * Represents a movable and interactive object in the space game.
 */
public abstract class ObjectWithPosition implements SpaceObject {
    /**
     * The x coordinate of the Object
     */
    protected int x;
    /**
     * The y coordinate of the Object
     */
    protected int y;

    /**
     * Creates a movable and interactive object at the given coordinates.
     *
     * @param x the given x coordinate
     * @param y the given y coordinate
     */
    public ObjectWithPosition(int x, int y) {
        this.x = x;
        this.y = y;
    }


    /**
     * Returns the x-coordinate of this object.
     *
     * @return the x position
     */
    @Override
    public int getX() {
        return x;
    }

    /**
     * Returns the y-coordinate of this object.
     *
     * @return the y position
     */
    @Override
    public int getY() {
        return y;
    }

    /**
     * Returns a string representation of this object,
     * including its class name and position.
     *
     * @return a string in the format ClassName(x, y)
     */
    @Override
    public String toString() {
        return getClass().getSimpleName() + "(" + x + ", " + y + ")";
    }
}
