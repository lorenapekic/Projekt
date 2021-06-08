package com.riteh.whisk;

import com.badlogic.gdx.utils.Array;

import squidpony.squidai.DijkstraMap;

public class Player extends Creature {
    public int potionCount = 0;
    public int goldCount = 0;


    public Player(float x, float y, int size, int healthPoints, int potionCount, int goldCount, String animLeft, String animRight, String animFront, String animBack, String animIdleLeft, String animIdleRight, String attackFront, String attackBack, String attackLeft, String attackRight) {
        super(x, y, size, healthPoints, animLeft, animRight, animFront, animBack, animIdleLeft, animIdleRight, attackFront, attackBack, attackLeft, attackRight);

        this.potionCount = potionCount;
        this.goldCount = goldCount;

        this.currentAnim = this.animLeft;
        this.currentDir = "left";
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
            if(this.healthPoints > 50) {
                this.healthPoints = 100;
            } else {
                this.healthPoints += 50;
            }
        }
        updatePotion();
    }

    public int updatePotion() {
        return potionCount;
    }
    public int updateGold() {
        return goldCount;
    }

    public void attack(Array<Enemy> enemies) {
        switch (this.currentDir) {
            case "left":
                this.currentAnim = this.attackLeft;
                for(Enemy currentEnemy : enemies) {
                    if(this.creatureRectangle.overlaps(currentEnemy.creatureRectangle)) {
                        currentEnemy.isHit(true);
                    }
                }
                break;
            case "right":
                this.currentAnim = this.attackRight;
                for(Enemy currentEnemy : enemies) {
                    if(this.creatureRectangle.overlaps(currentEnemy.creatureRectangle)) {
                        currentEnemy.isHit(true);
                    }
                }
                break;
        }
    }

}
