package org.example.canvasdemo.model;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class DifficultyLevel {
    private ArrayList<GoldCoin> goldCoins;
    private ArrayList<Enemy> enemies;
    private boolean finalLevel = false;

    public DifficultyLevel(ArrayList<GoldCoin> coinsList, ArrayList<Enemy> enemiesList) {
        this.goldCoins = coinsList;
        this.enemies = enemiesList;
    }

    public ArrayList<GoldCoin> getGoldCoins() {
        return goldCoins;
    }

    public void setGoldCoins(ArrayList<GoldCoin> goldCoins) {
        this.goldCoins = goldCoins;
    }

    public ArrayList<Enemy> getEnemies() {
        return enemies;
    }

    public void setEnemies(ArrayList<Enemy> enemies) {
        this.enemies = enemies;
    }

    public int getRemainingCoinsAmount() {
        int remaining = 0;

        for(GoldCoin gc : goldCoins) {
            if(!gc.isPickedUp()) {
                remaining++;
            }
        }
        return remaining;
    }

    public ArrayList<GoldCoin> getRemainingGoldCoins() {
        ArrayList<GoldCoin> result = new ArrayList<GoldCoin>();

        for(GoldCoin gc: goldCoins) {
            if(!gc.isPickedUp()) {
                result.add(gc);
            }
        }

        return result;
    }

    public boolean isFinalLevel() {
        return finalLevel;
    }

    public void setFinalLevel(boolean finalLevel) {
        this.finalLevel = finalLevel;
    }
}
