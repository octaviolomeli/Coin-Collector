package com.octaviolomeli.coinCollector.core;

// Room class to represent a room in the game world. Consists of points to outline what space on the grid it occupies
public class Room {
    private final Point sourcePoint;
    private final Point[] corners;
    private final Point centerPoint;
    private final int width;
    private final int height;

    /**
     * @param sourcePoint Where the wall begins to generate from (Bottom Left)
     * @param width Width of the room
     * @param height Height of the room
     */
    public Room(Point sourcePoint, int width, int height) {
        this.sourcePoint = sourcePoint;
        this.width = width;
        this.height = height;
        this.centerPoint = new Point(sourcePoint.x() + width / 2, sourcePoint.y() + height / 2);
        this.corners = new Point[4];
        assignCornerPoints();
    }

    // Calculate the corner points using the width and height and source point
    public void assignCornerPoints() {
        corners[0] = sourcePoint;
        corners[1] = new Point(sourcePoint.x(), sourcePoint.y() + height);
        corners[2] = new Point(sourcePoint.x() + width, sourcePoint.y());
        corners[3] = new Point(sourcePoint.x()  + width, sourcePoint.y() + height);
    }

    /**
     * Determine if a room collides with another
     * @param r2 Room
     * @return boolean if the two rooms collide
     */
    public boolean inRoom(Room r2) {
        for (Point p: r2.getCorners()) {
            if (p.x() <= this.maxX() && p.x() >= this.minX() && p.y() >= this.minY() && p.y() <= this.maxY()) {
                return true;
            }
        }

        for (int i = 0; i < 4; i++) {
            int j = (i + 1) % 4; // next corner
            if (Point.doIntersect(this.getCorners()[i], this.getCorners()[j], r2.getCorners()[i], r2.getCorners()[j])) {
                return true; // rooms are colliding
            }
        }

        return false;
    }

    public int maxX() {
        return corners[3].x();
    }

    public int minX() {
        return corners[0].x();
    }

    public int maxY() {
        return corners[3].y();
    }

    public int minY() {
        return corners[0].y();
    }

    public Point[] getCorners() {
        return corners;
    }

    public Point getCenterPoint() {
        return centerPoint;
    }
}
