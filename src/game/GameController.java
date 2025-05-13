package game;

import game.achievements.Achievement;
import game.achievements.AchievementManager;
import game.achievements.GameAchievement;
import game.achievements.PlayerStatsTracker;
import game.core.SpaceObject;
import game.ui.UI;
import game.utility.Direction;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * The Controller handling the game flow and interactions.
 * <p>
 * Holds references to the UI and the Model, so it can pass information and references back and forth as necessary.<br>
 * Manages changes to the game, which are stored in the Model, and displayed by the UI.<br>
 */
public class GameController {
    private long startTime;
    private final UI ui;
    private final GameModel model;
    private final AchievementManager aManager;

    /**
     * An internal variable indicating whether certain methods should log their actions.
     * Not all methods respect isVerbose.
     */
    private boolean isVerbose = false;
    private boolean isPaused = false;


    /**
     * Initializes the game controller with the given UI, GameModel and AchievementManager.<br>
     * Stores the UI, GameModel, AchievementManager and start time.<br>
     * The start time System.currentTimeMillis() should be stored as a long.<br>
     * Starts the UI using UI.start().<br>
     *
     * @param ui the UI used to draw the Game
     * @param model the model used to maintain game information
     * @param aManager the manager used to maintain achievement information
     *
     * @requires ui is not null
     * @requires model is not null
     * @requires achievementManager is not null
     * @provided
     */
    public GameController(UI ui, GameModel model, AchievementManager aManager) {
        this.ui = ui;
        this.model = model;
        this.aManager = aManager;
        this.startTime = System.currentTimeMillis();
        ui.start();
    }

    /**
     * Initializes the game controller with the given UI and GameModel.<br>
     * Stores the ui, model and start time.<br>
     * The start time System.currentTimeMillis() should be stored as a long.<br>
     *
     * @param ui    the UI used to draw the Game
     * @param aManager the manager used to maintain achievement information
     *
     * @requires ui is not null
     * @requires achievementManager is not null
     * @provided
     */
    public GameController(UI ui, AchievementManager aManager) {
        this(ui, new GameModel(ui::log, new PlayerStatsTracker()), aManager);
    }

    /**
     * Starts the main game loop.<br>
     * <p>
     * Passes onTick and handlePlayerInput to ui.onStep and ui.onKey respectively.
     * @provided
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
     * @param tick the provided tick
     * @provided
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
     * progress.<br>
     * <p>
     * This window includes:<br>
     * - Number of shots fired and shots hit<br>
     * - Number of Enemies destroyed<br>
     * - Survival time in seconds<br>
     * - Progress for each achievement, including name, description, completion percentage
     * and current tier<br>
     * @provided
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
        sb.append("Survival Time: ").append(getStatsTracker().getElapsedSeconds()).append(" seconds\n");


        List<Achievement> achievements= aManager.getAchievements();
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
     * Also, renders all space objects, including the ship.
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

    public PlayerStatsTracker getStatsTracker() {
        return model.getStatsTracker();
    }

    public void setVerbose(boolean verbose) {
        isVerbose = verbose;
        getModel().setVerbose(verbose);
    }

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
            ui.log("Ship moved to (" + model.getShip().getX() + ", " + model.getShip().getY() + ")");
        }
    }


    public void pauseGame() {
        ui.pause();
        isPaused = !isPaused;

        if (isPaused) {
            ui.log("Game paused.");
        } else {
            ui.log("Game unpaused.");
        }
    }

    public void refreshAchievements(int tick) {
        float result;
        int shotsFired = model.getStatsTracker().getShotsFired();
        int shotsHit = model.getStatsTracker().getShotsHit();
        long elapsedMillis = System.currentTimeMillis() - startTime;

        double survivalTimeProgress = Math.min(elapsedMillis / 1000.0 / 120.0, 1.0);
        double shotHitProgress = Math.min(shotsHit / 20.0, 1.0);

        if (shotsFired > 10) {
            float accuracy = (float) shotsHit / shotsFired;
            result = Math.min(accuracy / 0.99f, 1.0f);
        } else {
            result = 0.0f;
        }

        aManager.updateAchievement("Survivor", survivalTimeProgress);
        aManager.updateAchievement("Enemy Exterminator", shotHitProgress);
        aManager.updateAchievement("Sharp Shooter", result);

        if (isVerbose && tick % 100 == 0) {
            ui.log("Achievement Progress:");
            ui.log(String.format("  Survivor: %.2f", survivalTimeProgress));
            ui.log(String.format("  Enemy Exterminator: %.2f", shotHitProgress));
            ui.log(String.format("  Sharp Shooter: %.2f", result));
        }
        aManager.logAchievementMastered(); //Do i need this
    }

    public GameModel getModel() {
        return this.model;
    }
}
