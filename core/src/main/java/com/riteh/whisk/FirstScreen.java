package com.riteh.whisk;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;

/** First screen of the application. Displayed after the application is created. */
public class FirstScreen implements Screen {
    final WhiskeredAway game;

    OrthographicCamera camera;

    Animation<TextureRegion> animLeft;
    Animation<TextureRegion> animRight;
    Animation<TextureRegion> animFront;
    Animation<TextureRegion> animBack;
    Animation<TextureRegion> currentAnim;
    float elapsed;

    Rectangle cat;

    public FirstScreen(final WhiskeredAway game) {
        this.game = game;

        animLeft = GifDecoder.loadGIFAnimation(Animation.PlayMode.LOOP, Gdx.files.internal("Cat/Cat_walkLeft.gif").read());
        animRight = GifDecoder.loadGIFAnimation(Animation.PlayMode.LOOP, Gdx.files.internal("Cat/Cat_walkRight.gif").read());
        animFront = GifDecoder.loadGIFAnimation(Animation.PlayMode.LOOP, Gdx.files.internal("Cat/Cat_walkBehind.gif").read());
        animBack = GifDecoder.loadGIFAnimation(Animation.PlayMode.LOOP, Gdx.files.internal("Cat/Cat_walkFront.gif").read());

        currentAnim = animLeft;

        camera = new OrthographicCamera();
        camera.setToOrtho(false, 800, 480);

        cat = new Rectangle();
        cat.x = 800 / 2 - 64 / 2;
        cat.y = 20;

        cat.width = 64;
        cat.height = 64;
    }

    @Override
    public void show() {
        // Prepare your screen here.
    }

    @Override
    public void render(float delta) {
        elapsed += Gdx.graphics.getDeltaTime();
        Gdx.gl.glClearColor(0,0,1,0);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        camera.update();

        game.batch.setProjectionMatrix(camera.combined);

        game.batch.begin();
        game.batch.draw(currentAnim.getKeyFrame(elapsed), cat.x, cat.y, cat.width, cat.height);
        game.batch.end();

        if (Gdx.input.isKeyPressed(Input.Keys.A)) {
            cat.x -= 200 * Gdx.graphics.getDeltaTime();
            currentAnim = animLeft;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.D)) {
            cat.x += 200 * Gdx.graphics.getDeltaTime();
            currentAnim = animRight;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.W)) {
            cat.y += 200 * Gdx.graphics.getDeltaTime();
            currentAnim = animFront;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.S)) {
            cat.y -= 200 * Gdx.graphics.getDeltaTime();
            currentAnim = animBack;
        }

        if (cat.x < 0) cat.x = 0;
        if (cat.x > 800 - 64) cat.x = 800 - 64;
        if (cat.y < 0) cat.y = 0;
        if (cat.y > 480 - 64) cat.y = 480 - 64;

    }

    @Override
    public void resize(int width, int height) {
        // Resize your screen here. The parameters represent the new window size.
    }

    @Override
    public void pause() {
        // Invoked when your application is paused.
    }

    @Override
    public void resume() {
        // Invoked when your application is resumed after pause.
    }

    @Override
    public void hide() {
        // This method is called when another screen replaces this one.
    }

    @Override
    public void dispose() {
        // Destroy screen's assets here.
    }
}