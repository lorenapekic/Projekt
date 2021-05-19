package com.riteh.whisk;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

//for keeping track of game state
enum gameState {
    PAUSED,
    RUNNING
}

/** {@link com.badlogic.gdx.ApplicationListener} implementation shared by all platforms. */
public class WhiskeredAway extends Game {
    gameState state;
    SpriteBatch batch;
    BitmapFont font;

    public void create() {
        font = new BitmapFont();
        batch = new SpriteBatch();
        state = gameState.RUNNING;
        this.setScreen(new MainMenuScreen(this));
    }

    public void render() {
        super.render();
    }

    public void dispose() {
        font.dispose();
        batch.dispose();
    }
}