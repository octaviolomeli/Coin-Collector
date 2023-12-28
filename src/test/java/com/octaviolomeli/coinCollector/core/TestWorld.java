package com.octaviolomeli.coinCollector.core;

import org.junit.Test;
import org.junit.Assert;

public class TestWorld {

    @Test
    public void testMakeRoom() {
        World world1 = new World(0, 100, 100);
        Assert.assertEquals(0, world1.getRooms().size());

        // Should succeed in making a room
        Point sourcePoint1 = new Point(20, 20);
        world1.makeRoom(sourcePoint1, 10, 10);
        Assert.assertEquals(1, world1.getRooms().size());

        // Should fail to make a room
        Point sourcePoint2 = new Point(15, 15);
        world1.makeRoom(sourcePoint2, 20, 20);
        Assert.assertEquals(1, world1.getRooms().size());

        // Should succeed in making a room
        Point sourcePoint3 = new Point(50, 50);
        world1.makeRoom(sourcePoint3, 5, 5);
        Assert.assertEquals(2, world1.getRooms().size());
    }

    @Test
    public void testNearestRoom() {
        World world1 = new World(0, 100, 100);

        Point sourcePoint1 = new Point(0, 0);
        Room room1 = new Room(sourcePoint1, 5, 5);
        world1.getRooms().add(room1);

        Point sourcePoint2 = new Point(50, 50);
        Room room2 = new Room(sourcePoint2, 5, 5);
        world1.getRooms().add(room2);

        // Should be each other's nearest room
        Assert.assertEquals(room2, world1.nearestRoom(room1));
        Assert.assertEquals(room1, world1.nearestRoom(room2));

        // Introduce a room in between them
        Point sourcePoint3 = new Point(25, 25);
        Room room3 = new Room(sourcePoint3, 5, 5);
        world1.getRooms().add(room3);

        // New room should be the nearest for both
        Assert.assertEquals(room3, world1.nearestRoom(room1));
        Assert.assertEquals(room3, world1.nearestRoom(room2));
        Assert.assertNotEquals(room1, world1.nearestRoom(room2));
        Assert.assertEquals(room1, world1.nearestRoom(room3));
    }

}

