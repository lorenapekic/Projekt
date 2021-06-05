package com.riteh.whisk;

public class Player extends Creature {
    int potionCount;
    int goldCount;

    public Player(float x, float y, int size, int healthPoints, int potionCount, int goldCount) {
        super(x, y, size, healthPoints);
        this.goldCount = goldCount;
        this.potionCount = potionCount;
    }
}
