package com.octaviolomeli.coinCollector.core;

import org.junit.Test;
import org.junit.Assert;

public class TestRoom {

    @Test
    public void testAssignCorners() {
        Point sourcePoint = new Point(0, 0); // Will be the bottom left corner
        Room room1 = new Room(sourcePoint, 2, 4);
        Point[] expectedPoints = new Point[]{new Point(0, 0), new Point(0, 4), new Point(2, 0), new Point(2, 4)};
        Assert.assertArrayEquals("TestAssignCorners with order matter", expectedPoints, room1.getCorners());
    }

    @Test
    public void testInRoom() {
        // Contained within another room
        Point sourcePoint1 = new Point(0, 0);
        Room room1 = new Room(sourcePoint1, 2, 2);

        Point sourcePoint2 = new Point(-1, -1);
        Room room2 = new Room(sourcePoint2, 4, 4);

        Assert.assertTrue(room1.inRoom(room2));

        // Partial overlap
        Point sourcePoint3 = new Point(-2, 0);
        Room room3 = new Room(sourcePoint3, 4, 4);

        Point sourcePoint4 = new Point(0, 0);
        Room room4 = new Room(sourcePoint4, 4, 4);

        Assert.assertTrue(room3.inRoom(room4));

        // No overlap
        Point sourcePoint5 = new Point(-100, -100);
        Room room5 = new Room(sourcePoint5, 5, 5);

        Point sourcePoint6 = new Point(0, 3);
        Room room6 = new Room(sourcePoint6, 23, 21);
    }
}
