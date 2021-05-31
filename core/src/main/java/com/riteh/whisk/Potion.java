package com.riteh.whisk;

public class Potion extends Item {
    private float hpRestore;
    protected static int MAX_POTIONS = 3;

    public Potion(String name, String description, int cost, String itemImagePath) {
        super(name, description, cost, itemImagePath);
    }

    public float getHpRestore() {
        return hpRestore;
    }

    public void setHpRestore(float hpRestore) {
        this.hpRestore = hpRestore;
    }
}
