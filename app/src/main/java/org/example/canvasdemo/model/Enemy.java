package org.example.canvasdemo.model;

/**
 * Created by viliu on 3/30/2017.
 */

public class Enemy {
    int x;
    int y;
    int speed;
    EnemyType type;

    public Enemy(int x, int y, int speed, EnemyType type){
        this.x = x;
        this.y = y;
        this.speed = speed;
        this.type = type;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getSpeed() {
        return speed;
    }

    public EnemyType getType() {
        return type;
    }

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }
}

