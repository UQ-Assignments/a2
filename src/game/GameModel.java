package game;


import game.achievements.PlayerStatsTracker;
import game.core.*;
import game.utility.Logger;
import game.core.SpaceObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Represents the game information and state. Stores and manipulates the game state.
 */
public class GameModel {
    public static final int GAME_HEIGHT = 20;
    public static final int GAME_WIDTH = 10;
    public static final int START_SPAWN_RATE = 2; // spawn rate (percentage chance per tick)
    public static final int SPAWN_RATE_INCREASE = 5; // Increase spawn rate by 5% per level
    public static final int START_LEVEL = 1; // Starting level value
    public static final int SCORE_THRESHOLD = 100; // Score threshold for leveling
    public static final int ASTEROID_DAMAGE = 10; // The amount of damage an asteroid deals
    public static final int ENEMY_DAMAGE = 20; // The amount of damage an enemy deals
    public static final double ENEMY_SPAWN_RATE = 0.5; // Percentage of asteroid spawn chance
    public static final double POWER_UP_SPAWN_RATE = 0.25; // Percentage of asteroid spawn chance

    private final Random random = new Random(); // ONLY USED IN this.spawnObjects()
    private final List<SpaceObject> spaceObjects; // List of all objects
    private final Ship ship; // Core.Ship starts at (5, 10) with 100 health
    private int lvl; // The current game level
    private int spawnRate; // The current game spawn rate
    private final Logger logger; // The Logger reference used for logging.
    private final PlayerStatsTracker statTracker;
    private boolean isVerbose;

    /**
     * Constructs a new GameModel instance.
     * <p>
     * Models a game by storing and modifying game data.
     * Logger should be a method reference like UI.log.
     * Example: Model gameModel = new GameModel(ui::log)
     * <p>
     * Initializes:
     * - A list for tracking SpaceObjects
     * - The starting level and spawn rate
     * - A Ship instance
     * - Logger and PlayerStatsTracker references
     *
     * @param logger function for logging messages
     * @param statsTracker object for tracking player stats
     */

    public GameModel(Logger logger, PlayerStatsTracker statsTracker) {
        spaceObjects = new ArrayList<>();
        lvl = START_LEVEL;
        spawnRate = START_SPAWN_RATE;
        ship = new Ship();
        this.logger = logger;
        statTracker = statsTracker;
    }

    /**
     * Returns the ship instance in the game.
     *
     * @return the current ship instance.
     */
    public Ship getShip() {
        return ship;
    }

    /**
     * Returns a list of all SpaceObjects in the game.
     *
     * @return a list of all spaceObjects.
     */
    public List<SpaceObject> getSpaceObjects() {
        return spaceObjects;
    }

    /**
     * Returns the current level.
     *
     * @return the current level.
     */
    public int getLevel() {
        return lvl;
    }

    private void verboseLog(String message) {
        if (isVerbose) {
            logger.log(message);
        }
    }

    /**
     * Returns the player statistics tracker associated with the game.
     *
     * @return the current PlayerStatsTracker instance.
     */
    public PlayerStatsTracker getStatsTracker() {
        return statTracker;
    }

    /**
     * Adds a SpaceObject to the game.
     * <p>
     * Objects are considered part of the game only when they are tracked by the model.
     *
     * @param object the SpaceObject to be added to the game.
     * @requires object != null.
     */
    public void addObject(SpaceObject object) {
        this.spaceObjects.add(object);
    }

    /**
     * Updates the game state by moving all space objects and removing any that are out of bounds.
     * <p>
     * Each object's position is updated by calling its tick(tick) method.
     * Any object that is no longer within the game area is removed.
     *
     * @param tick the time step value used to update each object's position.
     */
    public void updateGame(int tick) {
        List<SpaceObject> toRemove = new ArrayList<>();
        for (SpaceObject obj : spaceObjects) {
            obj.tick(tick); // Move objects downward
            if (!isInBounds(obj)) { // Remove objects that move off-screen
                toRemove.add(obj);
            }
        }
        spaceObjects.removeAll(toRemove);
    }

    /**
     * Spawns new game objects (Asteroids, Enemies, and PowerUps) at random positions along the top of the screen.
     * <p>
     * This method makes exactly 6 calls to random.nextInt() and 1 call to random.nextBoolean() in the following order:
     * 1. Check if an Asteroid should spawn using random.nextInt(100) < spawnRate
     * 2. If so, get Asteroid x-coordinate using random.nextInt(GAME_WIDTH)
     * 3. Check if an Enemy should spawn using random.nextInt(100) < spawnRate * ENEMY_SPAWN_RATE
     * 4. If so, get Enemy x-coordinate using random.nextInt(GAME_WIDTH)
     * 5. Check if a PowerUp should spawn using random.nextInt(100) < spawnRate * POWER_UP_SPAWN_RATE
     * 6. If so, get PowerUp x-coordinate using random.nextInt(GAME_WIDTH)
     * 7. Use random.nextBoolean() to decide whether to spawn a ShieldPowerUp (true) or a HealthPowerUp (false)
     * <p>
     * All objects spawn at y = 0 (top of the screen).
     * Objects are not added if they would overlap with the ship or an existing space object.
     * However, random calls are still made regardless of whether the object is actually spawned.
     */

    public void spawnObjects() {
        // Spawn asteroids with a chance determined by spawnRate
        if (random.nextInt(100) < spawnRate) {
            int x = random.nextInt(GAME_WIDTH); // Random x-coordinate
            int y = 0; // Spawn at the top of the screen
            if (!isCollidingWithShip(x, y) && isOccupying(x, y)) {
                addObject(new Asteroid(x, y));
            }
        }

        // Spawn enemies with a lower chance
        // Half the rate of asteroids
        if (random.nextInt(100) < spawnRate * ENEMY_SPAWN_RATE) {
            int x = random.nextInt(GAME_WIDTH);
            int y = 0;
            if (!isCollidingWithShip(x, y) && isOccupying(x, y)) {
                addObject(new Enemy(x, y));
            }
        }

        // Spawn power-ups with an even lower chance
        // One-fourth the spawn rate of asteroids
        if (random.nextInt(100) < spawnRate * POWER_UP_SPAWN_RATE) {
            int x = random.nextInt(GAME_WIDTH);
            int y = 0;
            PowerUp powerUp = random.nextBoolean() ? new ShieldPowerUp(x, y) :
                    new HealthPowerUp(x, y);
            if (!isCollidingWithShip(x, y) && isOccupying(x, y)) {
                addObject(powerUp);
            }
        }
    }

    /**
     * Checks if a given position would collide with the ship.
     *
     * @param x the x-coordinate to check.
     * @param y the y-coordinate to check.
     * @return true if the position collides with the ship, false otherwise.
     */
    private boolean isCollidingWithShip(int x, int y) {
        return (ship.getX() == x) && (ship.getY() == y);
    }


    /**
     * Checks if any space object currently occupies the specified (x, y) position.
     * <p>
     * Iterates through all active space objects in the game and returns true
     * if any object is located at the given coordinates.
     *
     * @param x the x-coordinate to check.
     * @param y the y-coordinate to check.
     * @return true if the position is occupied by any space object; false otherwise.
     */
    private boolean isOccupying(int x, int y) {
        for (SpaceObject spaceObject : getSpaceObjects()) {
            if (spaceObject.getX() == x && spaceObject.getY() == y) {
                return false;
            }
        }
        return true;
    }

    /**
     * Increases the game level and spawn rate if the player's score meets the threshold.
     * <p>
     * The level goes up when the ship's score is at least the current level
     * multiplied by the score threshold. The spawn rate also increases.
     * If verbose mode is enabled, a message is logged to indicate the level up.
     */
    public void levelUp() {
        if (ship.getScore() >= getLevel() * SCORE_THRESHOLD) {
            lvl++;
            spawnRate += SPAWN_RATE_INCREASE;
            verboseLog("Level Up! Welcome to Level " + lvl + ". Spawn rate increased to " + spawnRate + "%.");
        }
    }

    /**
     * Fires a bullet from the ship's current position.
     * <p>
     * A new bullet is created at the ship's current coordinates and added
     * to the list of space objects in the game.
     */
    public void fireBullet() {
        int bulletX = ship.getX();
        int bulletY = ship.getY(); // Core.Bullet starts just above the ship
        spaceObjects.add(new Bullet(bulletX, bulletY));
    }

    /**
     * Detects and handles collisions between the ship, bullets, and other space objects.
     * <p>
     * A collision occurs when two objects share the same x and y coordinates.
     * <p>
     * Ship collision handling:
     * - If the ship collides with a PowerUp, apply its effect and log a message if verbose is true.
     * - If the ship collides with an Asteroid or Enemy, the ship takes damage and a message is logged if verbose is true.
     * - The colliding object is removed from the game after any ship collision.
     * <p>
     * Bullet collision handling:
     * - If a bullet hits an Enemy, both the bullet and the enemy are removed. Also calls recordShotHit().
     * - If a bullet hits an Asteroid, only the bullet is removed.
     */
    public void checkCollisions() {
        List<SpaceObject> toRemove = new ArrayList<>();
        for (SpaceObject obj : spaceObjects) {
            // Skip checking Ships (No ships should be in this list)
            if (obj instanceof Ship) {
                continue;
            }
            // Check Ship collision (except Bullets)
            if (isCollidingWithShip(obj.getX(), obj.getY()) && !(obj instanceof Bullet)) {
                // Handle collision effects
                switch (obj) {
                    case PowerUp powerUp -> {
                        powerUp.applyEffect(ship);
                        verboseLog("PowerUp collected: " + obj.render());
                    }
                    case Asteroid asteroid -> {
                        ship.takeDamage(ASTEROID_DAMAGE);
                        verboseLog("Hit by " + obj.render() + "! Health reduced by " + ASTEROID_DAMAGE + ".");

                    }
                    case Enemy enemy -> {
                        ship.takeDamage(ENEMY_DAMAGE);
                        verboseLog("Hit by " + obj.render() + "! Health reduced by " + ENEMY_DAMAGE + ".");
                    }
                    default -> {
                    }
                }
                toRemove.add(obj);
            }
        }

        for (SpaceObject obj : spaceObjects) {
            // Check only Bullets
            if (!(obj instanceof Bullet)) {
                continue;
            }
            // Check Bullet collision
            for (SpaceObject other : spaceObjects) {
                if (obj == other) continue;
                // Check only Enemies

                if ((obj.getX() == other.getX()) && (obj.getY() == other.getY())) {

                    if (other instanceof Enemy) {
                        getStatsTracker().recordShotHit();
                        toRemove.add(obj);  // Remove bullet
                        toRemove.add(other); // Remove enemy
                        break;

                    } else if (other instanceof Asteroid) {
                        toRemove.add(obj);
                        break;
                    }
                }
            }
        }

        spaceObjects.removeAll(toRemove); // Remove all collided objects
    }

    /**
     * Sets the seed for the Random instance used in the game.
     * <p>
     * This method is provided for testing purposes only and should never be called in production code.
     *
     * @param seed the seed value to set for the Random instance
     */
    public void setRandomSeed(int seed) {
        this.random.setSeed(seed);
    }

    /**
     * Checks whether the game is over.
     * <p>
     * The game is considered over if the Ship's health is less than or equal to 0.
     *
     * @return true if the Ship's health is less than or equal to 0, false otherwise
     */
    public boolean checkGameOver() {
        if (getShip().getHealth() <= 0) {
            return true;
        }
        return false;
    }

    /**
     * Checks whether a given SpaceObject is within the game boundaries.
     * <p>
     * A SpaceObject is considered in bounds if its x and y coordinates
     * are within the width and height of the game area.
     *
     * @param spaceObject the object to check
     * @return true if the object is within bounds, false otherwise
     */
    public static boolean isInBounds(SpaceObject spaceObject) {
        if (spaceObject.getX() >= GAME_WIDTH || spaceObject.getX() < 0) {
            return false;
        } else if (spaceObject.getY() >= GAME_HEIGHT || spaceObject.getY() < 0) {
            return false;
        }
        return true;
    }

    /**
     * Enables or disables verbose logging.
     * <p>
     * When verbose mode is on, the game will log detailed messages
     * during certain events like level-ups or collisions.
     *
     * @param verbose true to enable verbose logging, false to disable it
     */
    public void setVerbose(boolean verbose) {
        isVerbose = verbose;
    }
}
