package game;

import game.achievements.Achievement;
import game.achievements.AchievementManager;
import game.achievements.PlayerStatsTracker;
import game.core.SpaceObject;
import game.ui.UI;
import game.utility.Direction;
import java.util.ArrayList;
import java.util.List;

/**
 * The Controller handling the game flow and interactions.
 *
 * Holds references to the UI and the Model, so it can pass information and references back and forth as necessary.
 * Manages changes to the game, which are stored in the Model, and displayed by the UI.
 */
public class GameController {
    private long startTime;
    private final UI ui;
    private final GameModel model;
    private final AchievementManager achievementManager;

    /**
     * An internal variable indicating whether certain methods should log their actions.
     * Not all methods respect isVerbose.
     */
    private boolean isVerbose;
    private boolean isPaused = false;


    /**
     * Constructs a new GameController.
     *
     * Initializes the game controller with the given UI, GameModel, and AchievementManager.
     * Stores the UI, model, achievement manager, and the current system time as the start time.
     * Starts the UI by calling UI.start().
     *
     * @param ui the UI used to draw the game
     * @param model the model used to maintain game information
     * @param achievementManager the manager used to maintain achievement information
     * @requires ui != null, model != null, aManager != null
     *
     * @example
     *
     * UI ui = new UI();
     * GameModel model = new GameModel();
     * AchievementManager aManager = new AchievementManager();
     * GameController controller = new GameController(ui, model, aManager);
     *
     * @assumptions The UI, GameModel, and AchievementManager are correctly initialized and functional.
     */
    public GameController(UI ui, GameModel model, AchievementManager achievementManager) {
        this.ui = ui;
        this.model = model;
        this.achievementManager = achievementManager;
        this.startTime = System.currentTimeMillis();
        ui.start();
    }

    /**
     * Constructs a new GameController with the given UI and AchievementManager.
     *
     * Initializes the game controller with the given UI and a new GameModel.
     * The GameModel is initialized with a logger and a new PlayerStatsTracker.
     * The current system time is stored as the start time.
     *
     * @param ui the UI used to draw the game
     * @param achievementManager the manager used to maintain achievement information
     * @requires ui != null, achievementManager != null
     *
     * @example
     *
     * UI ui = new UI();
     * AchievementManager aManager = new AchievementManager();
     * GameController controller = new GameController(ui, aManager);
     *
     * @assumptions The UI and AchievementManager are correctly initialized and functional.
     */
    public GameController(UI ui, AchievementManager achievementManager) {
        this(ui, new GameModel(ui::log, new PlayerStatsTracker()), achievementManager);
    }

    /**
     * Starts the main game loop.
     *
     * Initializes the game loop by passing the onTick method to the UI's onStep method,
     * and the handlePlayerInput method to the UI's onKey method. This begins the continuous
     * update of the game state and handles player input.
     *
     * Sets the start time for the game using the current system time in milliseconds.
     *
     * @provided
     *
     * @example
     *
     * controller.startGame();
     *
     * @assumptions The game will start without issues if the UI and key handling are correctly set up.
     */
    public void startGame() {
        ui.onStep(this::onTick);
        this.startTime = System.currentTimeMillis();
        ui.onKey(this::handlePlayerInput);
    }

    /**
     * Uses the provided tick to call and advance the following:<br>
     * - A call to model.updateGame(tick) to advance the game by the given tick.<br>
     * - A call to model.checkCollisions() to handle game interactions.<br>
     * - A call to model.spawnObjects() to handle object creation.<br>
     * - A call to model.levelUp() to check and handle leveling.<br>
     * - A call to refreshAchievements(tick) to handle achievement updating.<br>
     * - A call to renderGame() to draw the current state of the game.<br>
     *
     * @param tick the provided tick
     * @provided
     *
     * @example
     *
     * controller.onTick(10);
     *
     * @assumptions The game model and achievements are properly updated and managed during each tick.
     */
    public void onTick(int tick) {
        model.updateGame(tick); // Update GameObjects
        model.checkCollisions(); // Check for Collisions
        model.spawnObjects(); // Handles new spawns
        model.levelUp(); // Level up when score threshold is met
        refreshAchievements(tick); // Handle achievement updating.

        renderGame(); // Update Visual

        // Check game over
        if (model.checkGameOver()) {
            pauseGame();
            showGameOverWindow();
        }
    }

    /**
     * Displays a Game Over window containing the player's final statistics and achievement
     * progress.
     *
     * This window includes:
     * - Number of shots fired and shots hi
     * - Number of Enemies destroyed
     * - Survival time in second
     * - Progress for each achievement, including name, description, completion percentage
     * and current tier
     *
     * @provided
     *
     * @example
     *
     * controller.showGameOverWindow();
     *
     * @assumptions This method assumes that the achievement manager and stats tracker are available and properly initialized.
     */
    private void showGameOverWindow() {
        // Create a new window to display game over stats.
        javax.swing.JFrame gameOverFrame = new javax.swing.JFrame("Game Over - Player Stats");
        gameOverFrame.setSize(400, 300);
        gameOverFrame.setLocationRelativeTo(null); // center on screen
        gameOverFrame.setDefaultCloseOperation(javax.swing.JFrame.DISPOSE_ON_CLOSE);


        StringBuilder sb = new StringBuilder();
        sb.append("Shots Fired: ").append(getStatsTracker().getShotsFired()).append("\n");
        sb.append("Shots Hit: ").append(getStatsTracker().getShotsHit()).append("\n");
        sb.append("Enemies Destroyed: ").append(getStatsTracker().getShotsHit()).append("\n");
        sb.append("Survival Time: ").append(getStatsTracker()
                .getElapsedSeconds()).append(" seconds\n");


        List<Achievement> achievements = achievementManager.getAchievements();
        for (Achievement ach : achievements) {
            double progressPercent = ach.getProgress() * 100;
            sb.append(ach.getName())
                    .append(" - ")
                    .append(ach.getDescription())
                    .append(" (")
                    .append(String.format("%.0f%%", progressPercent))
                    .append(" complete, Tier: ")
                    .append(ach.getCurrentTier())
                    .append(")\n");
        }

        String statsText = sb.toString();

        // Create a text area to show stats.
        javax.swing.JTextArea statsArea = new javax.swing.JTextArea(statsText);
        statsArea.setEditable(false);
        statsArea.setFont(new java.awt.Font("Monospaced", java.awt.Font.PLAIN, 14));

        // Add the text area to a scroll pane (optional) and add it to the frame.
        javax.swing.JScrollPane scrollPane = new javax.swing.JScrollPane(statsArea);
        gameOverFrame.add(scrollPane);

        // Make the window visible.
        gameOverFrame.setVisible(true);
    }

    /**
     * Renders the game state, updating the UI with the current score, health, level, and time survived.
     *
     * This method updates the game's display to reflect the current state. It will show:
     * - The score of the player's ship
     * - The health of the player's ship
     * - The current level
     * - The time survived since the game started
     * Additionally, it renders all the space objects in the game, including the ship itself.
     *
     * The method interacts with the UI to update the player's statistics and displays a list of space objects.
     * This allows the player to see their current progress and any relevant visual representation of the game state.
     *
     * @provided
     * @example
     *
     * // Assuming `game` is an instance of the game class
     * game.renderGame(); // Updates the UI with the current game stats and renders space objects
     *
     */
    public void renderGame() {
        ui.setStat("Score", String.valueOf(model.getShip().getScore()));
        ui.setStat("Health", String.valueOf(model.getShip().getHealth()));
        ui.setStat("Level", String.valueOf(model.getLevel()));
        ui.setStat("Time Survived", ((System.currentTimeMillis() - startTime) / 1000) + " seconds");
        List<SpaceObject> temp = new ArrayList<>(model.getSpaceObjects());
        temp.add(model.getShip());
        ui.render(temp);
    }

    /**
     * Retrieves the player statistics tracker associated with the game model.
     *
     * This method returns the `PlayerStatsTracker` which is responsible for tracking and managing the player's statistics,
     * such as shots fired, shots hit, enemies destroyed, and other relevant data during gameplay.
     * The stats tracker is typically used for achievement tracking and providing feedback to the player.
     *
     * @return the `PlayerStatsTracker` instance associated with the game model
     * @provided
     * @example
     *
     * // Assuming `game` is an instance of the game class
     * PlayerStatsTracker statsTracker = game.getStatsTracker();
     * System.out.println("Shots Fired: " + statsTracker.getShotsFired());
     *
     */
    public PlayerStatsTracker getStatsTracker() {
        return model.getStatsTracker();
    }

    /**
     * Sets the verbosity level for game logging and updates the game model's verbosity setting.
     *
     * This method enables or disables verbose logging, which can be useful for debugging or providing detailed feedback
     * during gameplay. When verbosity is enabled, the game will log additional information to the UI, such as movement
     * details, state changes, and achievement progress.
     *
     * The method also propagates the verbosity setting to the game model, which might use it for internal logging or tracking purposes.
     *
     * @param verbose if true, enables verbose logging; if false, disables verbose logging
     * @provided
     * @example
     *
     * // Assuming `game` is an instance of the game class
     * game.setVerbose(true); // Enable verbose logging
     * game.setVerbose(false); // Disable verbose logging
     *
     */
    public void setVerbose(boolean verbose) {
        isVerbose = verbose;
        getModel().setVerbose(verbose);
    }

    /**
     * Handles player input and performs actions such as moving the ship or firing bullets.
     * <p>
     * This method processes the player's input and takes appropriate actions based on the command provided. The following
     * actions are supported:
     *
     *     "W": Move the ship up
     *     "A": Move the ship left
     *     "S": Move the ship down
     *     <"D": Move the ship right
     *     <"F": Fire a bullet
     *     "P": Pause or unpause the game
     *
     * When the game is paused, only un-pausing is allowed. Invalid inputs will be logged.
     *
     * If verbosity is enabled, the method logs the ship's movement (e.g., "Ship moved to (x, y)") whenever the ship is moved.
     *
     * @param input the player's input command, which is a single character (W, A, S, D, F, or P).
     * @provided
     * @example
     *
     * // Assuming `game` is an instance of the game class
     * game.handlePlayerInput("W"); // Move the ship up
     * game.handlePlayerInput("F"); // Fire a bullet
     * game.handlePlayerInput("P"); // Pause or unpause the game
     * game.handlePlayerInput("X"); // Invalid input
     *
     */
    public void handlePlayerInput(String input) {
        input = input.toUpperCase();

        if (isPaused) {
            if (input.equals("P")) {
                pauseGame(); // Only allowed action while paused
            }
            return; // Ignore everything else while paused
        }

        boolean moved = false;

        switch (input) {
            case "W" -> {
                model.getShip().move(Direction.UP);
                moved = true;
            }
            case "A" -> {
                model.getShip().move(Direction.LEFT);
                moved = true;
            }
            case "S" -> {
                model.getShip().move(Direction.DOWN);
                moved = true;
            }
            case "D" -> {
                model.getShip().move(Direction.RIGHT);
                moved = true;
            }
            case "F" -> {
                model.fireBullet();
                model.getStatsTracker().recordShotFired();
            }
            case "P" -> pauseGame();
            default -> ui.log("Invalid input. Use W, A, S, D, F, or P.");
        }

        if (isVerbose && moved) {
            ui.log("Ship moved to (" + model.getShip().getX() + ", "
                    + model.getShip().getY() + ")");
        }
    }

    /**
     * Pauses or unpauses the game.
     *
     * This method toggles the paused state of the game. When the game is paused, no actions can be performed, except for
     * unpausing the game. The UI will reflect the change in state by pausing or unpausing the game and logging the
     * appropriate message.
     *
     * @provided
     * @example
     *
     * // Assuming `game` is an instance of the game class
     * game.pauseGame(); // Pauses or unpauses the game based on its current state
     *
     */
    public void pauseGame() {
        ui.pause();
        isPaused = !isPaused;

        if (isPaused) {
            ui.log("Game paused.");
        } else {
            ui.log("Game unpaused.");
        }
    }

    /**
     * Refreshes the achievement progress based on the current game stats.
     *
     * This method updates the progress for various achievements based on the player's performance:
     *
     *     "Survivor" - Progress is based on the time survived (max 120 seconds).
     *     "Enemy Exterminator" - Progress is based on the number of shots hit (max 20 hits).
     *     "Sharp Shooter" - Progress is based on shot accuracy (only if more than 10 shots fired).
     *
     * The achievement progress is logged at regular intervals when verbosity is enabled.
     *
     * @param tick The current game tick, used to control the frequency of logging achievement progress.
     * @provided
     * @example
     *
     * // Assuming `game` is an instance of the game class
     * game.refreshAchievements(100); // Refresh achievement progress every 100 ticks
     *
     */
    public void refreshAchievements(int tick) {
        int shotsFired = model.getStatsTracker().getShotsFired();
        int shotsHit = model.getStatsTracker().getShotsHit();
        long elapsedMillis = System.currentTimeMillis() - startTime;

        // Survivor: mastered at 120 seconds
        double survivalTimeProgress = Math.min(elapsedMillis / 1000.0 / 120.0, 1.0);

        // Enemy Exterminator: mastered at 20 hits
        double shotHitProgress = Math.min(shotsHit / 20.0, 1.0);

        // Sharp Shooter: mastered at 99% accuracy, only if more than 10 shots fired
        float sharpShooterProgress;
        if (shotsFired > 10) {
            double accuracy = model.getStatsTracker().getAccuracy();  // accuracy in 0.0–1.0
            sharpShooterProgress = (float) Math.min(accuracy / 0.99, 1.0);
        } else {
            sharpShooterProgress = 0.0f;
        }

        // Update all achievement progress
        achievementManager.updateAchievement("Survivor", survivalTimeProgress);
        achievementManager.updateAchievement("Enemy Exterminator", shotHitProgress);
        achievementManager.updateAchievement("Sharp Shooter", sharpShooterProgress);

        // Check for newly mastered achievements
        achievementManager.logAchievementMastered();

        // Verbose logging to UI every 100 ticks
        if (isVerbose && tick % 100 == 0) {
            ui.logAchievements(achievementManager.getAchievements());
        }

        updateAchievements(survivalTimeProgress, shotHitProgress, sharpShooterProgress, tick);
    }

    private void updateAchievements(double survivalTimeProgress, double shotHitProgress,
                                   float sharpShooterProgress, int tick) {
        // Update all achievement progress
        achievementManager.updateAchievement("Survivor", survivalTimeProgress);
        achievementManager.updateAchievement("Enemy Exterminator", shotHitProgress);
        achievementManager.updateAchievement("Sharp Shooter", sharpShooterProgress);

        // Check for newly mastered achievements
        achievementManager.logAchievementMastered();

        // Verbose logging to UI every 100 ticks
        if (isVerbose && tick % 100 == 0) {
            ui.logAchievements(achievementManager.getAchievements());
        }
    }



    /**
     * Retrieves the current game model.
     *
     * This method provides access to the underlying game model, which contains the game state, such as the ship's
     * position, the level, and other relevant data.
     *
     * @return The current game model.
     * @provided
     * @example
     *
     * // Assuming `game` is an instance of the game class
     * GameModel model = game.getModel(); // Get the current game model
     *
     */
    public GameModel getModel() {
        return this.model;
    }
}
