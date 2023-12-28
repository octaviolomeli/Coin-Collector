package com.octaviolomeli.coinCollector.core;

/*
    Point Class to easily represent a point on the world grid. Pretty straightforward.
 */
public class Point {
    private int x;
    private int y;
    private static final int HASHCODE = 31;

    /**
     * @param x x-coordinate in the grid
     * @param y y-coordinate in the grid
     */
    public Point(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public Point copy() {
        return new Point(x, y);
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof Point p) {
            return p.x == this.x && p.y == this.y;
        }
        return false;
    }

    @Override
    public String toString() {
        return "(" + x + ", " + y + ")";
    }

    /**
     * Create a new point by summing the x and y values.
     * @param p Point to sum together
     * @return New Point containing the sum of their coordinates
     */
    public Point add(Point p) {
        return new Point(x + p.x(), y + p.y());
    }

    public int x() {
        return x;
    }

    public int y() {
        return y;
    }

    /* Increment/decrement X and Y */

    public void incrementX() {
        x += 1;
    }

    public void incrementY() {
        y += 1;
    }

    public void decrementX() {
        x -= 1;
    }

    public void decrementY() {
        y -= 1;
    }

    /* Check if two line segments intersect
       @source: GeeksForGeeks
       https://www.geeksforgeeks.org/check-if-two-given-line-segments-intersect/
     */
    public static boolean doIntersect(Point p1, Point q1, Point p2, Point q2) {
        int o1 = orientation(p1, q1, p2);
        int o2 = orientation(p1, q1, q2);
        int o3 = orientation(p2, q2, p1);
        int o4 = orientation(p2, q2, q1);

        if (o1 != o2 && o3 != o4) {
            return true;
        }

        if (o1 == 0 && onSegment(p1, p2, q1)) {
            return true;
        }

        if (o2 == 0 && onSegment(p1, q2, q1)) {
            return true;
        }

        if (o3 == 0 && onSegment(p2, p1, q2)) {
            return true;
        }

        return o4 == 0 && onSegment(p2, q1, q2);
    }

    /*
       @source: GeeksForGeeks
       https://www.geeksforgeeks.org/check-if-two-given-line-segments-intersect/
    */
    private static boolean onSegment(Point p, Point q, Point r) {
        return q.x <= Math.max(p.x, r.x) && q.x >= Math.min(p.x, r.x)
                && q.y <= Math.max(p.y, r.y) && q.y >= Math.min(p.y, r.y);
    }

    /*
        @source: GeeksForGeeks
        https://www.geeksforgeeks.org/check-if-two-given-line-segments-intersect/
    */
    private static int orientation(Point p, Point q, Point r) {
        int val = (q.y - p.y) * (r.x - q.x) - (q.x - p.x) * (r.y - q.y);

        if (val == 0) {
            return 0;
        }

        return (val > 0) ? 1 : 2;
    }

    // Calculate the distance between this point and another
    public double distance(Point o) {
        double deltaX = this.x - o.x;
        double deltaY = this.y - o.y;
        return Math.sqrt((deltaX * deltaX) + (deltaY * deltaY));
    }

    @Override
    public int hashCode() {
        return HASHCODE * x +  y;
    }
}
