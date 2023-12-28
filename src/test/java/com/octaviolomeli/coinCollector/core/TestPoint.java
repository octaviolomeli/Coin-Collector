package com.octaviolomeli.coinCollector.core;

import org.junit.Test;
import org.junit.Assert;

public class TestPoint {

    @Test
    public void testEquals() {
        Point point1 = new Point(1, -3);
        Point point2 = new Point(5, -3);
        Point point3 = new Point(1, -3);
        Assert.assertEquals(point1, point3);
        Assert.assertEquals(point1, point1);
        Assert.assertNotEquals(point1, point2);
        Assert.assertNotEquals(null, point1);
    }

    @Test
    public void testSum() {
        Point point1 = new Point(5, 3);
        Point point2 = new Point(-5, 6);
        Point point3 = new Point(0, 9);
        Assert.assertEquals(point1.add(point2), point3);

        // Check that the used points are unchanged
        Point point4 = new Point(5, 3);
        Assert.assertEquals(point1, point4);
    }

    @Test
    public void testCopy() {
        Point point1 = new Point(4, 5);
        Point point2 = point1.copy();
        Assert.assertEquals(point1, point2);

        point1.incrementX();
        Assert.assertNotEquals(point1, point2);
        Assert.assertEquals(4 , point2.x());
    }

    @Test
    public void testDistance() {
        Point point1 = new Point(0, 0);
        Point point2 = new Point(8, 15);
        Assert.assertEquals(17, point1.distance(point2), .005);

        Point point3 = new Point(8, 12);
        Point point4 = new Point(2, 4);
        Assert.assertEquals(10, point3.distance(point4), .005);
    }

    @Test
    public void testIntersection() {
        Point f1 = new Point(5, 5);
        Point f2 = new Point(0, -5);
        Point g1 = new Point(1, 5);
        Point g2 = new Point(2, 10);

        Assert.assertTrue(Point.doIntersect(f1, g1, f2, g2));

        Point h1 = new Point(2, -6);
        Point h2 = new Point(-2, 10);
        Point k1 = new Point(1, 2);
        Point k2 = new Point(0, 6);

        Assert.assertFalse(Point.doIntersect(h1, k1, h2, k2));
    }
}
