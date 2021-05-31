package com.riteh.whisk;

public class Weapon extends Item {
    private float attackDmg;
    private boolean owned;
    private float cooldown;

    public Weapon(String name, String description, int cost, String itemImagePath) {
        super(name, description, cost, itemImagePath);
    }

    public float getAttackDmg() {
        return attackDmg;
    }

    public void setAttackDmg(float attackDmg) {
        this.attackDmg = attackDmg;
    }

    public boolean isOwned() {
        return owned;
    }

    public void setOwned(boolean owned) {
        this.owned = owned;
    }

    public float getCooldown() {
        return cooldown;
    }

    public void setCooldown(float cooldown) {
        this.cooldown = cooldown;
    }
}
