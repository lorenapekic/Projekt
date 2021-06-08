package com.riteh.whisk;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Slider;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

public class OptionScreen implements Screen {

    final WhiskeredAway game;

    OrthographicCamera camera;
    private Stage stage = new Stage(new ScreenViewport());
    private Skin skin = new Skin(Gdx.files.internal("ButtonSkin/skin.json"));
    Slider volumeSlider;

    public OptionScreen(final WhiskeredAway game) {
        this.game = game;

        camera = new OrthographicCamera();
        camera.setToOrtho(false, 800, 480);
        volumeSlider = new Slider(0.0f, 1.0f, 0.1f,false, skin);
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0,0,0,1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);

        camera.update();

        game.batch.setProjectionMatrix(camera.combined);
        game.batch.begin();
        game.font.setColor(1,1,1,1);
        game.font.getData().setScale(2f,2f);
        game.font.draw(game.batch, "Settings", 350, 400);
        game.batch.end();

        Gdx.input.setInputProcessor(stage);
        
        Label volumeLabel = new Label("Music volume", skin);
        volumeLabel.setFontScale(1.8f);

        volumeSlider.setValue(game.getMusicVolume());
        volumeSlider.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                    float newVolume = volumeSlider.getValue();
                    game.setMusicVolume(newVolume);
            }
        });

        Label controlsLabel = new Label("Controls", skin,  "white");
        controlsLabel.setFontScale(1.8f);
        Label upLabel = new Label("Up: ", skin,  "white");
        upLabel.setFontScale(1.8f);
        Label upControlLabel = new Label("W", skin,  "white");
        upControlLabel.setFontScale(1.8f);
        Label downLabel = new Label("Down: ", skin,  "white");
        downLabel.setFontScale(1.8f);
        Label downControlLabel = new Label("S", skin,  "white");
        downControlLabel.setFontScale(1.8f);
        Label leftLabel = new Label("Left: ", skin,  "white");
        leftLabel.setFontScale(1.8f);
        Label leftControlLabel = new Label("A", skin,  "white");
        leftControlLabel.setFontScale(1.8f);
        Label rightLabel = new Label("Right: ", skin,  "white");
        rightLabel.setFontScale(1.8f);
        Label rightControlLabel = new Label("D", skin,  "white");
        rightControlLabel.setFontScale(1.8f);
        Label pauseLabel = new Label("Pause: ", skin,  "white");
        pauseLabel.setFontScale(1.8f);
        Label pauseControlLabel = new Label("P", skin, "white");
        pauseControlLabel.setFontScale(1.8f);
        Label attackLabel = new Label("Attack: ", skin,  "white");
        attackLabel.setFontScale(1.8f);
        Label attackControlLabel = new Label("Space", skin, "white");
        attackControlLabel.setFontScale(1.8f);

        Table settingsTable = new Table();
        settingsTable.setFillParent(true);
        settingsTable.center();
        settingsTable.add(controlsLabel).colspan(2);
        settingsTable.row();
        settingsTable.add(upLabel).padRight(30);
        settingsTable.add(upControlLabel);
        settingsTable.row();
        settingsTable.add(downLabel).padRight(30);
        settingsTable.add(downControlLabel);
        settingsTable.row();
        settingsTable.add(leftLabel).padRight(30);
        settingsTable.add(leftControlLabel);
        settingsTable.row();
        settingsTable.add(rightLabel).padRight(30);
        settingsTable.add(rightControlLabel);
        settingsTable.row();
        settingsTable.add(pauseLabel).padRight(30);
        settingsTable.add(pauseControlLabel);
        settingsTable.row();
        settingsTable.add(attackLabel).padRight(30);
        settingsTable.add(attackControlLabel);
        settingsTable.row();
        settingsTable.add(volumeLabel).padRight(30);
        settingsTable.add(volumeSlider).width(200);

        TextButton back = new TextButton("Back", skin);

        back.setHeight(40);
        back.setWidth(100);
        back.getLabel().setFontScaleX(1.2f);
        back.getLabel().setFontScaleY(1.2f);

        back.setPosition(camera.viewportWidth/2 - back.getWidth()/2, 70);
        back.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                game.selectSoundEffect.play(0.6f);
                stage.dispose();
                game.setScreen(new MainMenuScreen(game));
            }
        });

        stage.addActor(settingsTable);
        stage.addActor(back);
        stage.act();
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        camera.viewportWidth = width;
        camera.viewportHeight = height;
        stage.clear();
        stage.getViewport().update(width, height, true);
        camera.update();
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
        stage.dispose();
        skin.dispose();
    }
}
