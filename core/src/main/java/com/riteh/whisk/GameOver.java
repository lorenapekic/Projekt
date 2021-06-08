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

public class GameOver implements Screen {

    final WhiskeredAway game;

    OrthographicCamera camera;
    private Stage stage = new Stage(new ScreenViewport());
    private Skin skin = new Skin(Gdx.files.internal("ButtonSkin/skin.json"));
    Slider volumeSlider;

    public GameOver(final WhiskeredAway game) {
        this.game = game;

        game.setCurrentMusicNoLoop("Audio/gameover.ogg");

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
        game.font.draw(game.batch, "You died.", 350, 400);
        game.batch.end();

        Gdx.input.setInputProcessor(stage);

        TextButton back = new TextButton("Main Menu", skin);

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
                game.level = new levelClass();
                game.setScreen(new MainMenuScreen(game));
            }
        });

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
