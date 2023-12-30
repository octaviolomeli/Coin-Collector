package com.octaviolomeli.coinCollector.core;

import com.octaviolomeli.coinCollector.tileEngine.TERenderer;
import com.octaviolomeli.coinCollector.tileEngine.TETile;
import edu.princeton.cs.algs4.StdDraw;

import java.awt.*;
import java.sql.*;
import java.util.Random;

public class Engine {
    TERenderer ter = new TERenderer();
    private static String keyStringPresses;
    public static Long seed;
    public static final int WIDTH = 90;
    public static final int HEIGHT = 47;
    public static final int FRAMERATE = 30;
    public static final int[] INT_MAGIC_NUMBERS = new int[]{1000, 16, 40, 20, 30, 500, 2000, 23};
    public static final double[] DOUBLE_MAGIC_NUMBERS = new double[]{1.5, 1.7, 2.5, 3.7};

    /**
     * Initializes an Engine object with seed and keyPresses.
     */
    public Engine(String seedArg, String keyPresses) {
        if (seedArg.equals("random")) {
            seed = new Random().nextLong();
        } else {
            seed = Long.parseLong(seedArg);
        }
        keyStringPresses = keyPresses;
    }

    /**
     * Handles user interaction with keyboard. Starts the game.
     */
    public void run() {
        Game game = gameSetUp();
        Point lastMousePosition = new Point((int) StdDraw.mouseX(), (int) StdDraw.mouseY());
        Point currMousePosition;

        ter.initialize(WIDTH, HEIGHT, 0, -1);

        char lastPressedKey = 'b';

        while (true) {
            currMousePosition = new Point((int) StdDraw.mouseX(), (int) StdDraw.mouseY());

            if (StdDraw.hasNextKeyTyped()) {
                char key = StdDraw.nextKeyTyped();
                keyStringPresses += key;
                lastPressedKey = keyChecks(key, lastPressedKey, game, true);
                renderGame(game.getTiles(), game.getPlayer().getScore(), currMousePosition, game, game.getSeed());
            }

            if (currMousePosition.equals(lastMousePosition)) {
                renderGame(game.getTiles(), game.getPlayer().getScore(), currMousePosition, game, game.getSeed());
                StdDraw.pause(INT_MAGIC_NUMBERS[0] / FRAMERATE);
            }

            lastMousePosition = new Point((int) StdDraw.mouseX(), (int) StdDraw.mouseY());
        }
    }

    /**
     * Show the End Screen after losing or winning
     * @param headTitle The main title
     * @param subTitle The subtitle
     * @param score The score of the player
     */
    private void showEndScreen(String headTitle, String subTitle, int score) {
        StdDraw.setPenColor(Color.white);
        StdDraw.clear(Color.black);
        StdDraw.setFont(new Font("Arial", Font.BOLD, INT_MAGIC_NUMBERS[2]));
        StdDraw.text(WIDTH / 2.0, HEIGHT / DOUBLE_MAGIC_NUMBERS[0], headTitle);
        StdDraw.setFont(new Font("Arial", Font.BOLD, INT_MAGIC_NUMBERS[4]));
        StdDraw.text(WIDTH / 2.0, HEIGHT / DOUBLE_MAGIC_NUMBERS[2], subTitle);
        StdDraw.setFont(new Font("Arial", Font.BOLD, INT_MAGIC_NUMBERS[7]));
        StdDraw.text(WIDTH / 2.0, HEIGHT / 3.0, "Your final score was: " + score);
        StdDraw.show();
    }

    /**
     *  Draw the HUD of the game
     * @param world The grid representation of the world
     * @param score The current score achieved
     * @param p The Player object
     * @param g The Game object
     * @param seed The seed of the Random object
     */
    private void renderGame(TETile[][] world, int score, Point p, Game g, long seed) {
        final int scoreXStart = 3;
        final int tileXStart = 15;
        final int seedXStart = INT_MAGIC_NUMBERS[4];
        final int pathXStart = 45;
        final int lightXStart = 60;

        ter.renderFrame(world);

        StdDraw.setPenColor(Color.white);
        StdDraw.line(0, HEIGHT - 2, WIDTH, HEIGHT - 2);

        StdDraw.text(scoreXStart, HEIGHT - 1, "Score: " + score);
        StdDraw.text(seedXStart, HEIGHT - 1, "Seed: " + seed);
        StdDraw.text(lightXStart, HEIGHT - 1, "Lights On: " + g.isLightOn() + " [F]");
        StdDraw.text(pathXStart, HEIGHT - 1, "Path On: " + g.displayChasePathEnabled() + " [P]");

        if (p.y() >= HEIGHT - 2) {
            StdDraw.text(tileXStart, HEIGHT - 1, "Current Tile: HUD");
        } else {
            StdDraw.text(tileXStart, HEIGHT - 1, "Current Tile: " + world[p.x()][p.y() + 1].description());
        }

        StdDraw.show();

    }

    /**
     * Saves the progress of the Game to the database into the proper row
     */
    private void saveToDatabase(int slot) {
        // Save if the slot # is not 0
        if (slot != 0) {
            String url = "jdbc:postgresql://localhost:5432/CoinCollector";
            String username = "postgres";
            String password = "*********";
            try {
                Connection con = DriverManager.getConnection(url, username, password);
                Statement st = con.createStatement();
                String inputString = keyStringPresses.substring(0, keyStringPresses.length() - 2);
                String sql = "UPDATE saved_worlds SET seed = '" + seed + "', keypresses = '" + inputString + "' WHERE id = " + slot;
                st.executeUpdate(sql);
                con.close();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }

    /**
     * Sets up the Game object
     * @return Game object
     */
    private Game gameSetUp() {
        Game game = new Game(seed, WIDTH, HEIGHT);
        char lastPressedKey = 'b';
        for (char key : keyStringPresses.toCharArray()) {
            lastPressedKey = keyChecks(key, lastPressedKey, game, false);
        }
        return game;
    }

    /**
     * Performs an action based on the key press
     * @param key The latest key press
     * @param lastPressedKey The previous key press
     * @param game The Game object
     * @param includePauses Whether pauses are included
     * @return Returns the key pressed
     */
    private char keyChecks(char key, char lastPressedKey, Game game, boolean includePauses) {
        if (Character.isDigit(lastPressedKey) && (key == 'q' || key == 'Q')) {
            saveToDatabase(Integer.parseInt(Character.toString(lastPressedKey)));
            if (includePauses) {
                System.exit(0);
            }
            return 'K';

        } else if (key == 'p' || key == 'P') {
            game.toggleChasePath();
            game.getEnemy().computeChasePath(game.getPlayer(), game.displayChasePathEnabled());
            if (game.isLightOn()) {
                game.turnOnLights();
            }
        } else if (key == 'f' || key == 'F') {
            if (game.isLightOn()) {
                game.turnOffLights();
            } else {
                game.turnOnLights();
            }
        } else if (Character.toLowerCase(key) == 'w' || Character.toLowerCase(key) == 'a'
                || Character.toLowerCase(key) == 's' || Character.toLowerCase(key) == 'd') {
            game.getPlayer().move(key, game.isLightOn());
            game.getEnemy().computeChasePath(game.getPlayer(), game.displayChasePathEnabled());

            if (!game.getEnemy().move()) {
                showEndScreen("You Died!", "You were eaten by the monster.", game.getPlayer().getScore());
                if (includePauses) {
                    StdDraw.pause(INT_MAGIC_NUMBERS[6]);
                    System.exit(0);
                }
                return 'K';
            } else if (game.collectedAllCoins()) {
                showEndScreen("You Won!", "You escaped with all the coins!", game.getPlayer().getScore());
                if (includePauses) {
                    StdDraw.pause(INT_MAGIC_NUMBERS[6]);
                    System.exit(0);
                }
                return 'K';
            }

            if (game.isLightOn()) {
                game.turnOnLights();
            }
        }
        return key;
    }
}
