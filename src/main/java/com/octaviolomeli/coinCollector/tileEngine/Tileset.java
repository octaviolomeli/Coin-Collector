package com.octaviolomeli.coinCollector.tileEngine;

import java.awt.Color;

/**
 * Contains constant tile objects, to avoid having to remake the same tiles in different parts of
 * the code.
 *
 * Ex:
 *      world[x][y] = Tileset.FLOOR;
 */

public class Tileset {
    public static final TETile WALL = new TETile('#', new Color(216, 128, 128), Color.darkGray, "wall");
    public static final TETile FLOOR = new TETile('·', new Color(128, 192, 128), Color.black, "floor");
    public static final TETile NOTHING = new TETile(' ', Color.black, Color.black, "nothing");
    public static final TETile FLOWER = new TETile('❀', Color.magenta, Color.pink, "flower");
    public static final TETile SAND = new TETile('▒', Color.yellow, Color.black, "sand");
    public static final TETile CHARACTER = new TETile('Φ', Color.white, Color.black, "self");
    public static final TETile BRONZE = new TETile('1', Color.white, new Color(150, 67, 3), "bronze coin");
    public static final TETile SILVER = new TETile('2', Color.black, Color.gray, "silver coin");
    public static final TETile GOLD = new TETile('3', Color.black, Color.yellow, "gold coin");
    public static final TETile LAMP = new TETile('⬤', Color.white, new Color(89, 111, 212), "lamp");
    public static final TETile CORE_LIGHT = new TETile('·', Color.white, new Color(89, 111, 212), "core light ring");
    public static final TETile INNER_LIGHT = new TETile('·', Color.white, new Color(47, 77, 214), "inner light ring");
    public static final TETile OUTER_LIGHT = new TETile('·', Color.white, new Color(12, 47, 204), "outer light ring");
    public static final TETile ENEMY = new TETile('●', Color.red, Color.black, "enemy");
    public static final TETile ENEMY_PATH = new TETile('█', Color.orange, Color.black,"enemy path");
}


