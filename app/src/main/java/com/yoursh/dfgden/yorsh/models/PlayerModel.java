package com.yoursh.dfgden.yorsh.models;

import java.io.Serializable;

/**
 * Created by dfgden on 8/1/16.
 */
public class PlayerModel implements Serializable {

    private String name;
    private String iconContentName;
    private String iconContentTurnName;
    private int points;
    private int allPoints;
    private String secretKey;

    public PlayerModel(String name, String iconContentName, String iconContentTurnName, int points, int allPoints, String secretKey) {
        this.name = name;
        this.iconContentName = iconContentName;
        this.iconContentTurnName = iconContentTurnName;
        this.points = points;
        this.allPoints = allPoints;
        this.secretKey = secretKey;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIconContentName() {
        return iconContentName;
    }

    public void setIconContentName(String iconContentName) {
        this.iconContentName = iconContentName;
    }

    public String getIconContentTurnName() {
        return iconContentTurnName;
    }

    public void setIconContentTurnName(String iconContentTurnName) {
        this.iconContentTurnName = iconContentTurnName;
    }

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    public int getAllPoints() {
        return allPoints;
    }

    public void setAllPoints(int allPoints) {
        this.allPoints = allPoints;
    }

    public String getSecretKey() {
        return secretKey;
    }

    public void setSecretKey(String secretKey) {
        this.secretKey = secretKey;
    }
}
