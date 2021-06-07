package com.riteh.whisk;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.Map;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;

import squidpony.squidai.DijkstraMap;

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
    levelClass level;
    Player player;


    public void create() {
        font = new BitmapFont();
        batch = new SpriteBatch();
        state = gameState.RUNNING;

        setCurrentMusic("Audio/main-theme.ogg");
        selectSoundEffect = Gdx.audio.newSound((Gdx.files.internal("Audio/select_two.mp3")));

        this.setScreen(new MainMenuScreen(this));
    }

    public void setCurrentMusic(String newMusicPath) {
        if(this.currentMusic != null) {
            this.currentMusic.stop();
        }
        this.currentMusic = Gdx.audio.newMusic(Gdx.files.internal(newMusicPath));
        this.currentMusic.setLooping(true);
        this.currentMusic.setVolume(this.musicVolume);
        this.currentMusic.play();
    }

    public float getMusicVolume() {
        return this.musicVolume;
    }

    public void setMusicVolume(float volume) {
        this.musicVolume = volume;
        this.currentMusic.setVolume(this.musicVolume);
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