package com.riteh.whisk;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

import javax.swing.JToggleButton;

public class MainMenuScreen implements Screen {

    final WhiskeredAway game;

    OrthographicCamera camera;

    Stage stage = new Stage(new ScreenViewport());
    Skin skin = new Skin(Gdx.files.internal("ButtonSkin/skin.json"));

    public MainMenuScreen(final WhiskeredAway game) {
        this.game = game;

        camera = new OrthographicCamera();
        camera.setToOrtho(false, 800, 480);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor( 1, 1, 1, 1 );
        Gdx.gl.glClear( GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT );

        camera.update();

        game.batch.setProjectionMatrix(camera.combined);
        game.batch.begin();
        game.font.setColor(0, 0, 0, 1);
        game.font.getData().setScale(2f, 2f);
        game.font.draw(game.batch, "WHISKERED AWAY", 250, 300);
        //game.font.draw(game.batch, "Press enter to begin", 300, 200);
        game.batch.end();

        Gdx.input.setInputProcessor(stage);
        TextButton startGame = new TextButton("Start Game",skin);
        TextButton settings = new TextButton("Settings",skin);
        startGame.setPosition(200, 200);
        settings.setPosition(200,150);
        startGame.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                game.setScreen(new FirstScreen(game));
            }
        });
        settings.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                //add options screen
                //game.setScreen((new FirstScreen(game)));
            }
        });
        //settings.addListener();
        stage.addActor(startGame);
        stage.addActor(settings);
        stage.draw();

    }

    @Override
    public void show() {
        // TODO Auto-generated method stub

    }

    @Override
    public void resize(int width, int height) {
        camera.viewportWidth = width;
        camera.viewportHeight = height;
        camera.update();
    }

    @Override
    public void pause() {
        // TODO Auto-generated method stub

    }

    @Override
    public void resume() {
        // TODO Auto-generated method stub

    }

    @Override
    public void hide() {
        // TODO Auto-generated method stub

    }

    @Override
    public void dispose() {
        // TODO Auto-generated method stub
        stage.dispose();
        skin.dispose();
    }

}
