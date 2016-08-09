package com.yoursh.dfgden.yorsh.models;

/**
 * Created by dfgden on 8/1/16.
 */
public class Player {

    private String name;
    private int iconId;
    private int points;


    public Player(String name, int iconId, int points) {
        this.name = name;
        this.iconId = iconId;
        this.points = points;
    }

    public String getName() {
        return name;
    }

    public int getIconId() {
        return iconId;
    }

    public int getPoints() {
        return points;
    }
}
