package me.phoenix.netherwartmacro.utils;

import me.phoenix.netherwartmacro.Main;

public enum Locations {
    PRIVATEISLAND("Private Island"),
    CHOLLOWS("Crystal Hollows"),
    DWARVENMINES("Dwarven Mines"),
    PARK("Park"),
    END("The End"),
    DUNGEON("Dungeons"),
    NULL("None"),
    NOTNULL("Unknown");

    private final String name;

    private final Runnable warpBack;

    Locations(String name) {
        this.name = name;
        this.warpBack = null;
    }

    Locations(String name, Runnable warpBack) {
        this.name = name;
        this.warpBack = warpBack;
    }

    public String getName() {
        return this.name;
    }

    public String toString() {
        return this.name;
    }
}
