package com.octaviolomeli.coinCollector.core;

import com.octaviolomeli.coinCollector.tileEngine.TETile;
import com.octaviolomeli.coinCollector.tileEngine.Tileset;

/**
 * Player class to represent the player that users play as.
 */
public class Player {
    private final World world;
    private final Point position;
    private int score;
    private final TETile playerTile = Tileset.CHARACTER;
    private TETile lastSteppedTile; // For tracking what tile to replace the user's position with when they move

    /**
     * Initialize a player with the given position in the given world
     * @param position The position of the player in the world grid
     * @param world The world that the player moves in
     */
    public Player(Point position, World world) {
        this.world = world;
        this.position = position;
        this.score = 0;
        world.getTiles()[position.x()][position.y()] = playerTile;
        lastSteppedTile = Tileset.FLOOR;
    }

    /**
     * Move the player 1 step in the given direction
     * @param direction The direction to move in
     * @param lightOn boolean if the light is on
     */
    public void move(char direction, boolean lightOn) {
        direction = Character.toLowerCase(direction);
        if (!lightOn && (lastSteppedTile != Tileset.FLOOR && lastSteppedTile != Tileset.LAMP)) {
            world.getTiles()[position.x()][position.y()] = Tileset.FLOOR;
        } else {
            world.getTiles()[position.x()][position.y()] = lastSteppedTile;
        }

        // Check if valid movement and move if so
        if (direction == 'w' && world.getTiles()[position.x()][position.y() + 1] != Tileset.WALL) {
            this.position.incrementY();
        } else if (direction == 'a' && world.getTiles()[position.x() - 1][position.y()] != Tileset.WALL) {
            this.position.decrementX();
        } else if (direction == 's' && world.getTiles()[position.x()][position.y() - 1] != Tileset.WALL) {
            this.position.decrementY();
        } else if (direction == 'd' && world.getTiles()[position.x() + 1][position.y()] != Tileset.WALL) {
            this.position.incrementX();
        }

        // Check if a coin was stepped on
        checkCoin();

        lastSteppedTile = world.getTiles()[position.x()][position.y()];
        world.getTiles()[position.x()][position.y()] = playerTile;
    }

    // Check if a coin was stepped on. If so, update the score corresponding to the value and replace the tile.
    private void checkCoin() {
        TETile currentTile = world.getTiles()[position.x()][position.y()];
        if (currentTile == Tileset.BRONZE) {
            score += 1;
            world.getTiles()[position.x()][position.y()] = Tileset.FLOOR;
        } else if (currentTile == Tileset.SILVER) {
            score += 2;
            world.getTiles()[position.x()][position.y()] = Tileset.FLOOR;
        } else if (currentTile == Tileset.GOLD) {
            score += 3;
            world.getTiles()[position.x()][position.y()] = Tileset.FLOOR;
        }
    }

    public int getScore() {
        return score;
    }

    public Point getPosition() {
        return position;
    }
}
