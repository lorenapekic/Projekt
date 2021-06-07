package com.riteh.whisk;

import com.badlogic.gdx.math.MathUtils;

import squidpony.squidai.DijkstraMap;

public class Enemy extends Creature {
    String name;

    public Enemy(float x, float y, int size, int healthPoints, String name, String animLeft, String animRight, String animFront, String animBack, String attackFront, String attackBack, String attackLeft, String attackRight) {
        super(x, y, size, healthPoints, animLeft, animRight, animFront, animBack, attackFront, attackBack, attackLeft, attackRight);
        this.name = name;
        this.currentDir = "down";
        this.currentAnim = this.animFront;
    }

    public void dropCoin() {
        if(this.healthPoints == 0) {

        }
    }

    public void attack(Player cat) {
        //distance between enemy and cat
        double distance = Math.sqrt(Math.pow(this.creatureRectangle.x-cat.creatureRectangle.x,2)+ Math.pow(this.creatureRectangle.y - cat.creatureRectangle.y, 2));

        if(distance <= 80 ) {
            if(cat.creatureRectangle.x > this.creatureRectangle.x && cat.creatureRectangle.y > this.creatureRectangle.y) {
                this.currentAnim = this.attackBack;
                cat.isHit(true);
            } else if(cat.creatureRectangle.x < this.creatureRectangle.x && cat.creatureRectangle.y < this.creatureRectangle.y) {
                this.currentAnim = this.attackFront;
                cat.isHit(true);
            } else if(cat.creatureRectangle.x < this.creatureRectangle.x) {
                this.currentAnim = this.attackLeft;
                cat.isHit(true);
            } else {
                this.currentAnim = this.attackRight;
                cat.isHit(true);
            }
        } else {
            this.currentAnim = this.animFront;
        }
    }

}
