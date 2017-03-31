package org.example.canvasdemo.model;

public class GoldCoin {
    private int coin_x;
    private int coin_y;
    private boolean pickedUp;
    private final int score = 10;

    public GoldCoin(int x, int y)
    {
        this.coin_x = x;
        this.coin_y = y;
    }

    public int getX() {
        return coin_x;
    }

    public void setX(int coin_x) {
        this.coin_x = coin_x;
    }

    public int getY() {
        return coin_y;
    }

    public void setY(int coin_y) {
        this.coin_y = coin_y;
    }

    public boolean isPickedUp() {
        return pickedUp;
    }

    public void setPicked_up(boolean picked_up) {
        this.pickedUp = picked_up;
    }

    public int getScore() {
        return score;
    }
}
