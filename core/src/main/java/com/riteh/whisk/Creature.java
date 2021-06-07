package com.riteh.whisk;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;

import squidpony.squidai.DijkstraMap;
import squidpony.squidai.graph.Algorithms;

public class Creature{
    String currentDir;
    int healthPoints;
    Rectangle creatureRectangle;

    Animation<TextureRegion> currentAnim, animLeft, animRight, animFront, animBack, animIdleLeft, animIdleRight, attackFront, attackBack, attackLeft, attackRight;


    public Creature (float x, float y, int size, int healthPoints, String animLeft, String animRight, String animFront, String animBack,
                     String animIdleLeft, String animIdleRight, String attackFront, String attackBack, String attackLeft, String attackRight) {
        this.creatureRectangle = new Rectangle();
        this.creatureRectangle.x = x;
        this.creatureRectangle.y = y;
        this.creatureRectangle.width = size;
        this.creatureRectangle.height = size;

        this.healthPoints = healthPoints;

        this.animLeft = GifDecoder.loadGIFAnimation(Animation.PlayMode.LOOP, Gdx.files.internal(animLeft).read());
        this.animRight = GifDecoder.loadGIFAnimation(Animation.PlayMode.LOOP, Gdx.files.internal(animRight).read());
        this.animBack= GifDecoder.loadGIFAnimation(Animation.PlayMode.LOOP, Gdx.files.internal(animBack).read());
        this.animFront = GifDecoder.loadGIFAnimation(Animation.PlayMode.LOOP, Gdx.files.internal(animFront).read());
        this.animIdleLeft = GifDecoder.loadGIFAnimation(Animation.PlayMode.LOOP, Gdx.files.internal(animIdleLeft).read());
        this.animIdleRight = GifDecoder.loadGIFAnimation(Animation.PlayMode.LOOP, Gdx.files.internal(animIdleRight).read());
        this.attackLeft = GifDecoder.loadGIFAnimation(Animation.PlayMode.LOOP, Gdx.files.internal(attackLeft).read());
        this.attackRight = GifDecoder.loadGIFAnimation(Animation.PlayMode.LOOP, Gdx.files.internal(attackRight).read());
        this.attackBack= GifDecoder.loadGIFAnimation(Animation.PlayMode.LOOP, Gdx.files.internal(attackBack).read());
        this.attackFront = GifDecoder.loadGIFAnimation(Animation.PlayMode.LOOP, Gdx.files.internal(attackFront).read());
    }

    public Creature (float x, float y, int size, int healthPoints, String animLeft, String animRight, String animFront, String animBack, String attackFront, String attackBack, String attackLeft, String attackRight) {
        this.creatureRectangle = new Rectangle();
        this.creatureRectangle.x = x;
        this.creatureRectangle.y = y;
        this.creatureRectangle.width = size;
        this.creatureRectangle.height = size;
        this.healthPoints = healthPoints;

        this.animLeft = GifDecoder.loadGIFAnimation(Animation.PlayMode.LOOP, Gdx.files.internal(animLeft).read());
        this.animRight = GifDecoder.loadGIFAnimation(Animation.PlayMode.LOOP, Gdx.files.internal(animRight).read());
        this.animBack= GifDecoder.loadGIFAnimation(Animation.PlayMode.LOOP, Gdx.files.internal(animBack).read());
        this.animFront = GifDecoder.loadGIFAnimation(Animation.PlayMode.LOOP, Gdx.files.internal(animFront).read());
        this.attackFront = GifDecoder.loadGIFAnimation(Animation.PlayMode.LOOP, Gdx.files.internal(attackFront).read());
        this.attackBack = GifDecoder.loadGIFAnimation(Animation.PlayMode.LOOP, Gdx.files.internal(attackBack).read());
        this.attackLeft = GifDecoder.loadGIFAnimation(Animation.PlayMode.LOOP, Gdx.files.internal(attackLeft).read());
        this.attackRight = GifDecoder.loadGIFAnimation(Animation.PlayMode.LOOP, Gdx.files.internal(attackRight).read());
    }

    public void move(String dir) {
        switch (dir) {
            case "left":
                this.creatureRectangle.x -= 32;
                break;
            case "right":
                this.creatureRectangle.x += 32;
                break;
            case "up":
                this.creatureRectangle.y += 32;
                break;
            case "down":
                this.creatureRectangle.y -= 32;
                break;
        }
    }

    public void isHit(Boolean attack) {

        if(attack == true) {
            if(this.healthPoints > 0) {
                this.healthPoints -= 10;
            }
            if(this.healthPoints <= 0) {
                //die
            }
        }
    }


    public String getHealth() {
        return String.valueOf(this.healthPoints);
    }
}
