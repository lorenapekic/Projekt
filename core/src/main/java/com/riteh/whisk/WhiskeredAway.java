package com.riteh.whisk;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

//for keeping track of game state
enum gameState {
    PAUSED,
    RUNNING
}

/** {@link com.badlogic.gdx.ApplicationListener} implementation shared by all platforms. */
public class WhiskeredAway extends Game {
    private float musicVolume = 0.5f;
    gameState state;
    SpriteBatch batch;
    BitmapFont font;
    Music currentMusic;
    Sound selectSoundEffect;

    public void create() {
        font = new BitmapFont();
        batch = new SpriteBatch();
        state = gameState.RUNNING;
        currentMusic = Gdx.audio.newMusic(Gdx.files.internal("Audio/main-theme.mp3"));
        currentMusic.setLooping(true);
        selectSoundEffect = Gdx.audio.newSound((Gdx.files.internal("Audio/select_two.mp3")));
        this.setScreen(new MainMenuScreen(this));

    }

    public float getMusicVolume() {
        return this.musicVolume;
    }

    public void setMusicVolume(float volume) {
        this.musicVolume = volume;
        currentMusic.setVolume(this.musicVolume);
    }

    public void render() {
        super.render();
    }

    public void dispose() {
        font.dispose();
        batch.dispose();
        currentMusic.dispose();
        selectSoundEffect.dispose();
    }
}