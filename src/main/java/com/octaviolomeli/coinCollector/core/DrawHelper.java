package com.octaviolomeli.coinCollector.core;

import com.octaviolomeli.coinCollector.tileEngine.TETile;
import com.octaviolomeli.coinCollector.tileEngine.Tileset;

public class DrawHelper {
    private final World worldObject;
    private final TETile[][] world;

    /**
     * Initializes a DrawHelper with a world to draw on.
     * @param world The World object to draw on
     */
    public DrawHelper(World world) {
        this.worldObject = world;
        this.world = world.getTiles();
    }

    /* Vertical-horizontal L path generators */

    public void drawLDownRight(Point start, Point end) {
        Point current = start.copy();

        // Check for future vertical collisions
        if (scanForVerticalCollisions(start, end)) {
            current.decrementX();
        }

        // Draw going down, starting from start
        current = drawDown(current, end);

        if (world[current.x()][current.y()] == Tileset.WALL && current.y() == end.y()) {
            world[current.x()][current.y()] = Tileset.SAND;
            current.decrementY();
        }

        // Draw going right, continuing from previous
        drawRight(current, end);
    }


    public void drawLUpRight(Point start, Point end) {
        Point current = start.copy();

        // Check for future vertical collisions
        if (scanForVerticalCollisions(start, end)) {
            current.decrementX();
        }

        // Draw going up, starting from start
        current = drawUp(current, end);

        if (world[current.x()][current.y()] == Tileset.WALL && current.y() == end.y()) {
            world[current.x()][current.y()] = Tileset.SAND;
            current.incrementY();
        }

        // Draw going right, continuing from previous
        drawRight(current, end);
    }


    public void drawLDownLeft(Point start, Point end) {
        Point current = start.copy();

        // Check for future vertical collisions
        if (scanForVerticalCollisions(start, end)) {
            current.decrementX();
        }

        // Draw going down, starting from start
        current = drawDown(current, end);

        if (world[current.x()][current.y()] == Tileset.WALL && current.y() == end.y()) {
            world[current.x()][current.y()] = Tileset.SAND;
            current.decrementY();
        }

        // Draw going left, continuing from previous
        drawLeft(current, end);
    }


    public void drawLUpLeft(Point start, Point end) {
        Point current = start.copy();

        // Check for future vertical collisions
        if (scanForVerticalCollisions(start, end)) {
            current.decrementX();
        }

        // Draw going up, starting from start
        current = drawUp(current, end);

        if (world[current.x()][current.y()] == Tileset.WALL && current.y() == end.y()) {
            world[current.x()][current.y()] = Tileset.SAND;
            current.incrementY();
        }

        // Draw going left, continuing from previous
        drawLeft(current, end);
    }

    /* Horizontal-vertical L path generators */

    public void drawLRightDown(Point start, Point end) {
        Point current = start.copy();

        // Check for future horizontal collisions
        if (scanForHorizontalCollisions(start, end)) {
            current.decrementY();
        }

        // Draw going right, starting from start
        current = drawRight(current, end);

        if (world[current.x()][current.y()] == Tileset.WALL && current.y() == end.y()) {
            world[current.x()][current.y()] = Tileset.SAND;
            current.decrementX();
        }

        // Draw going down, continuing from previous
        drawDown(current, end);
    }


    public void drawLRightUp(Point start, Point end) {
        Point current = start.copy();

        // Check for future horizontal collisions
        if (scanForHorizontalCollisions(start, end)) {
            current.decrementY();
        }

        // Draw going right, starting from start
        current = drawRight(current, end);

        if (world[current.x()][current.y()] == Tileset.WALL && current.y() == end.y()) {
            world[current.x()][current.y()] = Tileset.SAND;
            current.incrementX();
        }

        // Draw going up, continuing from previous
        drawUp(current, end);
    }


    public void drawLLeftDown(Point start, Point end) {
        Point current = start.copy();

        // Check for future horizontal collisions
        if (scanForHorizontalCollisions(start, end)) {
            current.decrementY();
        }

        // Draw going left, starting from start
        current = drawLeft(current, end);

        if (world[current.x()][current.y()] == Tileset.WALL && current.y() == end.y()) {
            world[current.x()][current.y()] = Tileset.SAND;
            current.decrementX();
        }

        // Draw going down, continuing from previous
        drawDown(current, end);
    }


    public void drawLLeftUp(Point start, Point end) {
        Point current = start.copy();

        // Check for future vertical collisions
        if (scanForHorizontalCollisions(start, end)) {
            current.decrementY();
        }

        // Draw going left, starting from start
        current = drawLeft(current, end);

        if (world[current.x()][current.y()] == Tileset.WALL && current.y() == end.y()) {
            world[current.x()][current.y()] = Tileset.SAND;
            current.incrementX();
        }

        // Draw going up, continuing from previous
        drawUp(current, end);
    }

    /* One-directional draw helpers */

    public Point drawLeft(Point start, Point end) {
        Point current = start.copy();

        while (current.x() > end.x()) {
            world[current.x()][current.y()] = Tileset.SAND;
            current.decrementX();
        }

        return current;
    }


    public Point drawRight(Point start, Point end) {
        Point current = start.copy();

        while (current.x() < end.x()) {
            world[current.x()][current.y()] = Tileset.SAND;
            current.incrementX();
        }

        return current;
    }


    public Point drawUp(Point start, Point end) {
        Point current = start.copy();

        while (current.y() < end.y() && current.y() < worldObject.getHeight()) {
            world[current.x()][current.y()] = Tileset.SAND;
            current.incrementY();
        }

        return current;
    }


    public Point drawDown(Point start, Point end) {
        Point current = start.copy();

        while (current.y() > end.y() && current.y() >= 0) {
            world[current.x()][current.y()] = Tileset.SAND;
            current.decrementY();
        }

        return current;
    }


    public void drawVertical(Point start, Point end) {
        Point current = start.copy();

        if (start.y() < end.y()) {
            drawUp(current, end);
        } else {
            drawDown(current, end);
        }
    }


    public void drawHorizontal(Point start, Point end) {
        Point current = start.copy();

        if (start.x() < end.x()) {
            drawRight(current, end);
        } else {
            drawLeft(current, end);
        }
    }

    /* Path collision detection */

    private boolean scanForHorizontalCollisions(Point start, Point end) {
        Point current = start.copy();
        int wallsEncountered = 0;

        if (start.x() < end.x()) {
            for (int x = current.x(); x < end.x(); x++) {
                if (world[x][current.y()] == Tileset.WALL) {
                    wallsEncountered++;
                }
                if (wallsEncountered > 2) {
                    return true;
                }
            }
        } else {
            for (int x = current.x(); x > end.x(); x--) {
                if (world[x][current.y()] == Tileset.WALL) {
                    wallsEncountered++;
                }
                if (wallsEncountered > 2) {
                    return true;
                }
            }
        }
        return false;
    }


    private boolean scanForVerticalCollisions(Point start, Point end) {
        Point current = start.copy();
        int wallsEncountered = 0;

        if (start.y() < end.y()) {
            for (int y = current.y(); y < end.y(); y++) {
                if (world[current.x()][y] == Tileset.WALL) {
                    wallsEncountered++;
                }
                if (wallsEncountered > 2) {
                    return true;
                }
            }
        } else {
            for (int y = current.y(); y > end.y(); y--) {
                if (world[current.x()][y] == Tileset.WALL) {
                    wallsEncountered++;
                }
                if (wallsEncountered > 2) {
                    return true;
                }
            }
        }
        return false;
    }

    /* Hallway generation */

    public void generateHallwayWalls() {
        for (int x = 0; x < worldObject.getWidth(); x++) {
            for (int y = 0; y < worldObject.getHeight(); y++) {
                // Basic cases
                if (world[x][y] == Tileset.SAND && world[x - 1][y] == Tileset.NOTHING) {
                    world[x - 1][y] = Tileset.FLOWER;
                }
                if (world[x][y] == Tileset.SAND && world[x + 1][y] == Tileset.NOTHING) {
                    world[x + 1][y] = Tileset.FLOWER;
                }
                if (world[x][y] == Tileset.SAND && world[x][y - 1] == Tileset.NOTHING) {
                    world[x][y - 1] = Tileset.FLOWER;
                }
                if (world[x][y] == Tileset.SAND && world[x][y + 1] == Tileset.NOTHING) {
                    world[x][y + 1] = Tileset.FLOWER;
                }

                // Diagonal fill cases
                if (world[x][y] == Tileset.SAND && world[x - 1][y - 1] == Tileset.NOTHING) {
                    world[x - 1][y - 1] = Tileset.FLOWER;
                }
                if (world[x][y] == Tileset.SAND && world[x + 1][y + 1] == Tileset.NOTHING) {
                    world[x + 1][y + 1] = Tileset.FLOWER;
                }
                if (world[x][y] == Tileset.SAND && world[x - 1][y + 1] == Tileset.NOTHING) {
                    world[x - 1][y + 1] = Tileset.FLOWER;
                }
                if (world[x][y] == Tileset.SAND && world[x + 1][y - 1] == Tileset.NOTHING) {
                    world[x + 1][y - 1] = Tileset.FLOWER;
                }
            }
        }
    }

    public void fillHallwayScaffolds() {
        for (int x = 0; x < worldObject.getWidth(); x++) {
            for (int y = 0; y < worldObject.getHeight(); y++) {
                if (world[x][y] == Tileset.SAND) {
                    world[x][y] = Tileset.FLOOR;
                } else if (world[x][y] == Tileset.FLOWER) {
                    world[x][y] = Tileset.WALL;
                }
            }
        }
    }
}
