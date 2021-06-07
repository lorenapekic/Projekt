package com.riteh.whisk;

public class Player extends Creature {
    public int potionCount = 0;
    public int goldCount = 0;

    public Player(/*float x, float y,*/ int size, int healthPoints, int potionCount, int goldCount) {
        super(/*x, y,*/size, healthPoints);
        this.potionCount = potionCount;
        this.goldCount = goldCount;
    }


    //Player gets a potion
    public void addPotion(){
        potionCount++;
        updatePotion();
    }

    //Player gets a coin
    public void addGold(){
        goldCount++;
        updateGold();
    }

    //the Player heals, uses the potion
    public void removePotion(){
        if(potionCount != 0){
            potionCount--;
        }
        updatePotion();
    }

    public int updatePotion() {
        return potionCount;
    }
    public int updateGold() {
        return goldCount;
    }
}
