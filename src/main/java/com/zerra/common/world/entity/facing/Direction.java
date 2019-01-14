package com.zerra.common.world.entity.facing;

public enum Direction
{
	NORTH,
	SOUTH,
	EAST,
	WEST;

    private static final int SIZE = values().length;

    public static Direction getFromIndex(int index) {
        int i = index < 0 || index > SIZE ? 0 : index;
        return values()[i];
    }
}
