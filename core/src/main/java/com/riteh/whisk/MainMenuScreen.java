package com.riteh.whisk;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Slider;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

import java.awt.Rectangle;

public class MainMenuScreen implements Screen {

    final WhiskeredAway game;
    OrthographicCamera camera;

    private Stage stage = new Stage(new ScreenViewport());
    private Skin skin = new Skin(Gdx.files.internal("ButtonSkin/skin.json"));

    private float elapsed;
    private Rectangle cat;
    private Animation<TextureRegion> sleepingAnim;

    private Sound startSoundEffect;

    public MainMenuScreen(final WhiskeredAway game) {
        this.game = game;
        camera = new OrthographicCamera();
        camera.setToOrtho(false, 800, 480);

        cat = new Rectangle();
        cat.x = 800 / 2 - 100 / 2 + 100;
        cat.y = 20;

        cat.width = 200;
        cat.height = 200;

        sleepingAnim = GifDecoder.loadGIFAnimation(Animation.PlayMode.LOOP, Gdx.files.internal("Cat/cat_sleepMainMenuAnimation.gif").read());
        startSoundEffect = Gdx.audio.newSound(Gdx.files.internal(("Audio/select_one.mp3")));

    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor( 0, 0, 0, 1 );
        Gdx.gl.glClear( GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT );
        elapsed += Gdx.graphics.getDeltaTime();

        camera.update();

        game.batch.setProjectionMatrix(camera.combined);
        game.batch.begin();
        game.font.setColor(1, 1, 1, 1);
        game.font.getData().setScale(2f, 2f);
        game.font.draw(game.batch, "WHISKERED AWAY", 270, 330);
        game.batch.draw(sleepingAnim.getKeyFrame(elapsed), cat.x,cat.y,cat.height,cat.width);
        game.batch.end();

        Gdx.input.setInputProcessor(stage);
        TextButton startGame = new TextButton("Start Game",skin, "default");
        TextButton settings = new TextButton("Settings",skin, "default");

        startGame.setHeight(40);
        startGame.setWidth(100);
        startGame.getLabel().setFontScaleX(1.2f);
        startGame.getLabel().setFontScaleY(1.2f);

        settings.setHeight(40);
        settings.setWidth(100);
        settings.getLabel().setFontScaleX(1.2f);
        settings.getLabel().setFontScaleY(1.2f);

        startGame.setPosition(camera.viewportWidth/2 - startGame.getWidth()/2, camera.viewportHeight/2  - startGame.getHeight());
        settings.setPosition(camera.viewportWidth/2 - startGame.getWidth()/2,camera.viewportHeight/2 - 50 - settings.getHeight());

        startGame.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                game.level = new levelClass();
                startSoundEffect.play(0.1f);
                stage.dispose();
                game.setScreen(new FirstScreen(game));
            }
        });

        settings.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                game.selectSoundEffect.play(0.6f);
                game.setScreen((new OptionScreen(game)));
            }
        });

        stage.addActor(startGame);
        stage.addActor(settings);
        stage.act();
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
        stage.clear();
        stage.getViewport().update(width, height, true);
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
