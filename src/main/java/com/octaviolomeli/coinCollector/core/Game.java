package com.octaviolomeli.coinCollector.core;

import com.octaviolomeli.coinCollector.tileEngine.TETile;
import com.octaviolomeli.coinCollector.tileEngine.Tileset;


public class Game {
    private final Room spawnRoom;
    private final Player p;
    private final Enemy e;
    private final World world;
    private Room enemySpawnRoom;
    private boolean displayChasePath;
    private int highestPossibleScore;
    private boolean lightOn;
    private final long seed;

    /**
     * Initialize a Game object with corresponding dimensions and seed.
     * @param seed Seed for the random Object
     * @param width Width of the world
     * @param height Height of the world
     */
    public Game(long seed, int width, int height) {
        this.seed = seed;
        world = new World(seed, width, height);
        displayChasePath = false;
        lightOn = false;
        world.setWorld(false);
        // Randomly select an existing room in the world as the player spawn room
        spawnRoom = world.getRooms().get(world.getR().nextInt(world.getRooms().size()));
        p = new Player(spawnRoom.getCenterPoint(), world);

        // Select a spawn room for enemy
        for (Room r : world.getRooms()) {
            if (r != spawnRoom) {
                enemySpawnRoom = r;
            }
        }
        e = new Enemy(enemySpawnRoom.getCenterPoint(), world);

        e.computeChasePath(p, displayChasePath);
        placeCoins();
        placeLamps();
    }

    public TETile[][] getTiles() {
        return world.getTiles();
    }

    public Player getPlayer() {
        return p;
    }

    public Enemy getEnemy() {
        return e;
    }

    /**
     * Places a coin of random value in all rooms except spawn rooms.
     */
    private void placeCoins() {
        TETile[] coins = {Tileset.BRONZE, Tileset.SILVER, Tileset.GOLD};
        highestPossibleScore = 0;
        TETile coin;

        for (Room r : world.getRooms()) {
            if (r != spawnRoom && r != enemySpawnRoom) {
                Point center = r.getCenterPoint();
                coin = coins[world.getR().nextInt(3)];
                highestPossibleScore += Integer.parseInt(Character.toString(coin.character()));
                world.getTiles()[center.x()][center.y()] = coin;
            }
        }
    }

    /**
     * Places a lamp in all rooms except the spawn rooms.
     */
    private void placeLamps() {
        for (Room r : world.getRooms()) {
            if (r != spawnRoom && r != enemySpawnRoom) {
                Point center = r.getCenterPoint();
                world.getTiles()[center.x()][center.y() + 1] = Tileset.LAMP;
            }
        }
    }

    /**
     * Turns on the lights in each room by displaying a blue gradient on floors.
     */
    public void turnOnLights() {
        lightOn = true;
        for (Room r : world.getRooms()) {
            if (r != spawnRoom && r != enemySpawnRoom) {
                Point l = r.getCenterPoint().copy(); // l = lamp
                l.incrementY();
                for (int i = r.minX(); i < r.maxX(); i++) {
                    for (int j = r.minY(); j < r.maxY(); j++) {
                        boolean isFloor = world.getTiles()[i][j] == Tileset.FLOOR;
                        // core ring
                        boolean yAxis1 = j == l.y() + 1 && l.x() - 1 <= i && i <= l.x() + 1;
                        boolean yAxisNeg1 = j == l.y() - 1 && l.x() - 1 <= i && i <= l.x() + 1;
                        boolean xAxis1 = i == l.x() + 1 && l.y() - 1 <= j && j <= l.y() + 1;
                        boolean xAxisNeg1 = i == l.x() - 1 && l.y() - 1 <= j && j <= l.y() + 1;
                        // inner ring
                        boolean yAxis2 = j == l.y() + 2 && l.x() - 2 <= i && i <= l.x() + 2;
                        boolean yAxisNeg2 = j == l.y() - 2 && l.x() - 2 <= i && i <= l.x() + 2;
                        boolean xAxis2 = i == l.x() + 2 && l.y() - 2 <= j && j <= l.y() + 2;
                        boolean xAxisNeg2 = i == l.x() - 2 && l.y() - 2 <= j && j <= l.y() + 2;
                        if (isFloor && (yAxis1 || yAxisNeg1 || xAxis1 || xAxisNeg1)) {
                            world.getTiles()[i][j] = Tileset.CORE_LIGHT;
                        } else if (isFloor && (yAxis2 || yAxisNeg2 || xAxis2 || xAxisNeg2)) {
                            world.getTiles()[i][j] = Tileset.INNER_LIGHT;
                        } else if (isFloor) {
                            world.getTiles()[i][j] = Tileset.OUTER_LIGHT;
                        }
                    }
                }
            }
        }
    }

    /**
     * Turns off the light in each room
     */
    public void turnOffLights() {
        for (Room r : world.getRooms()) {
            for (int i = r.minX(); i < r.maxX(); i++) {
                for (int j = r.minY(); j < r.maxY(); j++) {
                    if (world.getTiles()[i][j] == Tileset.CORE_LIGHT || world.getTiles()[i][j] == Tileset.INNER_LIGHT
                            || world.getTiles()[i][j] == Tileset.OUTER_LIGHT) {
                        world.getTiles()[i][j] = Tileset.FLOOR;
                    }
                }
            }
        }
        lightOn = false;
    }

    public boolean isLightOn() {
        return lightOn;
    }
    public boolean displayChasePathEnabled() {
        return displayChasePath;
    }

    public void toggleChasePath() {
        displayChasePath = !displayChasePath;
    }

    public boolean collectedAllCoins() {
        return highestPossibleScore == p.getScore();
    }
    public long getSeed() {
        return seed;
    }
}
