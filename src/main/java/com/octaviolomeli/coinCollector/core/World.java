package com.octaviolomeli.coinCollector.core;

import com.octaviolomeli.coinCollector.tileEngine.*;
import java.util.*;

public class World {
    private final TETile[][] world; // Grid representation of the world
    private final List<Room> rooms;
    private final DrawHelper dh; // For drawing on the world grid
    private final Random r; // Random Object
    private final int width;
    private final int height;

    /**
     * Initializes a world with the given width and height and uses the seed to make random decisions.
     * @param seed Seed used for the Random object
     * @param width Width of the world's grid
     * @param height Height of the world's grid
     */
    public World(long seed, int width, int height) {
        this.width = width;
        this.height = height;
        this.world = new TETile[width][height];
        this.rooms = new ArrayList<>();
        this.r = new Random(seed);
        this.dh = new DrawHelper(this);

        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                world[x][y] = Tileset.NOTHING;
            }
        }
    }

    /**
     * Randomly generate the world with rooms and hallways
     * @param debug Boolean if debug mode is on
     */
    public void setWorld(boolean debug) {
        final int minRoomsToGenerate = 15;
        final int maxRoomsToGenerate = 30;
        final int minRoomSize = 6;
        final int maxRoomSize = 12;

        int roomsToGenerate = Math.max(minRoomsToGenerate, r.nextInt() % maxRoomsToGenerate);

        // Make valid rooms
        while (roomsToGenerate != rooms.size()) {
            Point randomSourcePoint = new Point(Math.abs(r.nextInt() % width - 1), Math.abs(r.nextInt() % height - 1));
            int randomMinRoomSize = Math.max(minRoomSize, Math.abs(r.nextInt() % maxRoomSize));
            int randomMaxRoomSize = Math.max(minRoomSize, Math.abs(r.nextInt() % maxRoomSize));
            this.makeRoom(randomSourcePoint, randomMinRoomSize, randomMaxRoomSize);
        }

        // For each room, connect them to the nearest one
        for (Room room : rooms) {
            Room n = nearestRoom(room);
            connectRooms(room, n);
        }

        // Generate hallways walls
        dh.generateHallwayWalls();

        // Fill in the hallways
        if (!debug) {
            dh.fillHallwayScaffolds();
        }
    }

    /**
     * Try to create a room
     * @param source Point to start generating the room from
     * @param w The width of the room
     * @param h The height of the room
     */
    public void makeRoom(Point source, int w, int h) {
        Room room = new Room(source, w, h);

        // Check if this room collides with any other
        if (collisionChecks(room)) {
            return;
        }

        // draw room
        for (int x = source.x() - 1; x < w + source.x() + 1; x++) {
            for (int y = source.y() - 1; y < h + source.y() + 1; y++) {
                if (indexBound(x, y) && x == source.x() - 1 || x == w + source.x() || y == source.y() - 1
                        || y == h + source.y()) {
                    world[x][y] = Tileset.NOTHING;
                } else if (indexBound(x, y) && x == source.x() || x == w + source.x() - 1 || y == source.y()
                        || y == h + source.y() - 1) {
                    world[x][y] = Tileset.WALL;
                } else if (indexBound(x, y)) {
                    world[x][y] = Tileset.FLOOR;
                }
            }
        }

        // Add to the list of existing rooms
        rooms.add(room);
    }

    /**
     * Find the nearest room to the given room
     * @param room Room
     * @return Return the nearest room to the given room
     */
    public Room nearestRoom(Room room) {
        Room nearestRoom = new Room(new Point(Integer.MAX_VALUE, Integer.MAX_VALUE), 1, 1);
        // Calculate distance between rooms
        for (Room n : rooms) {
            double currentToRDistance = n.getCenterPoint().distance(room.getCenterPoint());
            double nearestToRDistance = nearestRoom.getCenterPoint().distance(room.getCenterPoint());
            if (n != room && currentToRDistance < nearestToRDistance) {
                nearestRoom = n;
            }
        }
        return nearestRoom;
    }


    /**
     * Connect two rooms via hallways
     * @param r1 Room 1
     * @param r2 Room 2
     */
    private void connectRooms(Room r1, Room r2) {
        Point startingPoint = r1.getCenterPoint();
        Point endingPoint = r2.getCenterPoint();

        // Determine which direction to search for the other room
        if (startingPoint.x() < endingPoint.x() && startingPoint.y() > endingPoint.y()) {
            dh.drawLDownRight(startingPoint, endingPoint);
        } else if (startingPoint.x() < endingPoint.x() && startingPoint.y() < endingPoint.y()) {
            dh.drawLUpRight(startingPoint, endingPoint);
        } else if (startingPoint.x() > endingPoint.x() && startingPoint.y() > endingPoint.y()) {
            dh.drawLDownLeft(startingPoint, endingPoint);
        } else if (startingPoint.x() > endingPoint.x() && startingPoint.y() < endingPoint.y()) {
            dh.drawLUpLeft(startingPoint, endingPoint);
        } else if (startingPoint.x() == endingPoint.x() && startingPoint.y() != endingPoint.y()) {
            dh.drawVertical(startingPoint, endingPoint);
        } else if (startingPoint.x() != endingPoint.x() && startingPoint.y() == endingPoint.y()) {
            dh.drawHorizontal(startingPoint, endingPoint);
        }

        tiePointToCenter(startingPoint);
    }

    /**
     * Draws hallways connecting given point to the world center
     * @param p Point to connect to the center of world via hallways
     */
    public void tiePointToCenter(Point p) {
        Point center = new Point(this.width / 2, this.height / 2);

        // While the center point is a wall, move it to a non-wall tile
        while (world[center.x()][center.y()] == Tileset.WALL) {
            center.incrementX();
        }

        world[center.x()][center.y()] = Tileset.SAND;

        if (p.x() < center.x() && p.y() > center.y()) {
            dh.drawLRightDown(p, center);
        } else if (p.x() < center.x() && p.y() < center.y()) {
            dh.drawLRightUp(p, center);
        } else if (p.x() > center.x() && p.y() > center.y()) {
            dh.drawLLeftDown(p, center);
        } else if (p.x() > center.x() && p.y() < center.y()) {
            dh.drawLLeftUp(p, center);
        } else if (p.x() == center.x() && p.y() != center.y()) {
            dh.drawVertical(p, center);
        } else if (p.x() != center.x() && p.y() == center.y()) {
            dh.drawHorizontal(p, center);
        }
    }

    /**
     * Check if given room collides with existing rooms
     * @param room room to compare against others
     * @return boolean representing if the room collides with an existing room
     */
    private boolean collisionChecks(Room room) {
        // runoff AND padding check
        for (Point p : room.getCorners()) {
            if (p.x() <= 0 || p.x() >= this.width || p.y() <= 0 || p.y() >= this.height) {
                return true;
            }
        }

        // room and room collision
        for (Room otherRoom : this.rooms) {
            if (room.inRoom(otherRoom) || otherRoom.inRoom(room)) {
                return true;
            }
        }

        return false;
    }

    // World getter methods
    public TETile[][] getTiles() {
        return world;
    }


    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    private boolean indexBound(int x, int y) {
        return x >= 0 && x < this.width && y >= 0 && y < this.height;
    }

    public List<Room> getRooms() {
        return rooms;
    }

    public Random getR() {
        return this.r;
    }
}
