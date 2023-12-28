package com.octaviolomeli.coinCollector.core;

import org.junit.Test;
import org.junit.Assert;
import com.octaviolomeli.coinCollector.tileEngine.Tileset;

public class TestPlayer {

    @Test
    public void testMove() {
        // Use System.out.println(TETile.toString(world.getTiles())); to easily visualize the tiles
        World world = new World(0, 12, 9);
        initWorld(world);
        world.makeRoom(new Point(3, 3), 5, 5);
        Player player = new Player(new Point(4, 4), world);

        // Valid movement
        player.move('w', false);
        // Check new tile is now player
        Assert.assertEquals("self", world.getTiles()[4][5].description());
        // Check previous tile is now a floor
        Assert.assertEquals("floor", world.getTiles()[4][4].description());
        // Check player position changed
        Assert.assertEquals(new Point(4, 5), player.getPosition());

        // Invalid movement
        player.move('a', false);
        // Check new tile is still wall
        Assert.assertEquals("wall", world.getTiles()[3][5].description());
        // Check current tile is still player
        Assert.assertEquals("self", world.getTiles()[4][5].description());
        // Check player position is unchanged
        Assert.assertEquals(new Point(4, 5), player.getPosition());

        // Check if score updates when moving into coin
        world.getTiles()[5][5] = Tileset.GOLD;
        Assert.assertEquals(0, player.getScore());
        player.move('d', false);
        // Check score is properly updated
        Assert.assertEquals(3, player.getScore());
        // Check the tile is replaced
        Assert.assertEquals("self", world.getTiles()[5][5].description());
        Assert.assertEquals("floor", world.getTiles()[4][5].description());
    }

    public void initWorld(World world) {
        for (int i = 0; i < world.getTiles().length; i++) {
            for (int j = 0; j < world.getTiles()[0].length; j++) {
                world.getTiles()[i][j] = Tileset.FLOOR;
            }
        }
    }
}
