package com.riteh.whisk;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;

public class Creature extends Rectangle {
    String currentDir;
    int healthPoints;

    Animation<TextureRegion> currentAnim, animLeft, animRight, animFront, animBack, animIdleLeft, animIdleRight;

    public Creature (float x, float y, int size, int healthPoints) {
        this.x = x;
        this.y = y;
        this.width = size;
        this.height = size;
        this.healthPoints = healthPoints;
    }

    public void move(String dir) {
        switch (dir) {
            case "left":
                this.x -= 32;
                break;
            case "right":
                this.x += 32;
                break;
            case "up":
                this.y += 32;
                break;
            case "down":
                this.y -= 32;
                break;
        }
    }

    public void moveUp() {
        y += 32;
    }

    public void moveLeft() {
        x -= 32;
    }
}
