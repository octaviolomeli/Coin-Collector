package com.octaviolomeli.coinCollector.core;

import com.octaviolomeli.coinCollector.tileEngine.TETile;
import com.octaviolomeli.coinCollector.tileEngine.Tileset;
import java.util.*;

public class Enemy {
    private final World world;
    private Point position;
    private List<Point> path; // Path to the player
    private final TETile enemyTile = Tileset.ENEMY;

    /**
     * Initialize an enemy with the given position in the given world
     * @param position The starting position of the enemy in the world grid
     * @param world The world in which the enemy moves in
     */
    public Enemy(Point position, World world) {
        this.world = world;
        this.position = position;
        this.path = new ArrayList<>();
        world.getTiles()[position.x()][position.y()] = enemyTile;
    }


    /**
     * Move the enemy 1 step closer to the player.
     * @return boolean representing if the enemy moved. Used to determine if the player died.
     */
    public boolean move() {
        if (path.size() <= 1) {
            return false;
        }

        // Move to the next point in path
        world.getTiles()[position.x()][position.y()] = Tileset.FLOOR;
        position = path.get(0);
        world.getTiles()[position.x()][position.y()] = enemyTile;
        path.remove(0);
        return true;
    }


    /**
     * Generate the path for the enemy to follow
     * @param p The player
     * @param displayPath boolean if user turned on display Path feature
     */
    public void computeChasePath(Player p, boolean displayPath) {
        path.remove(p.getPosition());

        clearPreviousChasePath(p);

        getPath(getPosition(), p.getPosition());

        path.remove(getPosition());

        if (displayPath) {
            for (Point point : path) {
                if (!point.equals(p.getPosition()) && !point.equals(getPosition())) {
                    world.getTiles()[point.x()][point.y()] = Tileset.ENEMY_PATH;
                }
            }
        }
    }

    /**
     * Find the shortest path from start to end points. Uses BFS
     * @param start Starting point of the path
     * @param end End point of the path
     */
    public void getPath(Point start, Point end) {
        Queue<Point> q = new ArrayDeque<>();
        boolean[][] visitedGrid = new boolean[world.getWidth()][world.getHeight()];
        Map<Point, Point> pathTracing = new HashMap<>();

        q.add(start);
        visitedGrid[start.x()][start.y()] = true;

        while (!q.isEmpty()) {
            Point current = q.remove();

            if (current.equals(end)) {
                buildPath(current, pathTracing);
                break;
            }

            visitedGrid[current.x()][current.y()] = true;

            Point[] cardinalDirections = {
                new Point(0, 1),
                new Point(0, -1),
                new Point(1, 0),
                new Point(-1, 0)
            };

            for (Point p : cardinalDirections) {
                Point adjacentPoint = current.add(p);
                if (world.getTiles()[adjacentPoint.x()][adjacentPoint.y()] != Tileset.WALL
                        && world.getTiles()[adjacentPoint.x()][adjacentPoint.y()] != Tileset.BRONZE
                        && world.getTiles()[adjacentPoint.x()][adjacentPoint.y()] != Tileset.SILVER
                        && world.getTiles()[adjacentPoint.x()][adjacentPoint.y()] != Tileset.GOLD
                        && world.getTiles()[adjacentPoint.x()][adjacentPoint.y()] != Tileset.LAMP
                        && !visitedGrid[adjacentPoint.x()][adjacentPoint.y()]
                        && !q.contains(adjacentPoint)) {
                    q.add(adjacentPoint);
                    pathTracing.put(adjacentPoint, current);
                }
            }
        }
    }


    /**
     * @param end Where the path ends at
     * @param pathTracing Mapping a point to the next point in the path
     */
    private void buildPath(Point end, Map<Point, Point> pathTracing) {
        Point current = end.copy();
        path = new ArrayList<>();
        path.add(end);
        while (pathTracing.containsKey(current)) {
            current = pathTracing.get(current);
            path.add(current);
        }

        Collections.reverse(path);
    }


    /**
     * Replace the enemy path with floor tiles. Used for turning off the display path.
     * @param p The player, needed for their position
     */
    public void clearPreviousChasePath(Player p) {
        for (Point point : path) {
            if (!point.equals(p.getPosition()) && !point.equals(getPosition())) {
                if (world.getTiles()[point.x()][point.y()] == Tileset.ENEMY_PATH) {
                    world.getTiles()[point.x()][point.y()] = Tileset.FLOOR;
                }
            }
        }

        path = new ArrayList<>();
    }

    public Point getPosition() {
        return position;
    }
}
