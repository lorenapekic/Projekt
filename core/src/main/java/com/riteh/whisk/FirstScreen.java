package com.riteh.whisk;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.MathUtils;

/** First screen of the application. Displayed after the application is created. */
public class FirstScreen implements Screen {

    final WhiskeredAway game;

    OrthographicCamera camera;

    Animation<TextureRegion> animLeft;
    Animation<TextureRegion> animRight;
    Animation<TextureRegion> animFront;
    Animation<TextureRegion> animBack;
    Animation<TextureRegion> animIdleLeft;
    Animation<TextureRegion> animIdleRight;
    Animation<TextureRegion> currentAnim;

    int faceDir;

    float elapsed;

    float keyPressedTime;
    float keyDelta;

    Rectangle cat;

    TiledMap map;
    OrthogonalTiledMapRenderer renderer;

    public FirstScreen(final WhiskeredAway game) {
        this.game = game;

        animLeft = GifDecoder.loadGIFAnimation(Animation.PlayMode.LOOP, Gdx.files.internal("Cat/Cat_walkLeft.gif").read());
        animRight = GifDecoder.loadGIFAnimation(Animation.PlayMode.LOOP, Gdx.files.internal("Cat/Cat_walkRight.gif").read());
        animFront = GifDecoder.loadGIFAnimation(Animation.PlayMode.LOOP, Gdx.files.internal("Cat/Cat_walkBehind.gif").read());
        animBack = GifDecoder.loadGIFAnimation(Animation.PlayMode.LOOP, Gdx.files.internal("Cat/Cat_walkFront.gif").read());
        animIdleLeft = GifDecoder.loadGIFAnimation(Animation.PlayMode.LOOP, Gdx.files.internal("Cat/Cat_idleTailLeft.gif").read());
        animIdleRight = GifDecoder.loadGIFAnimation(Animation.PlayMode.LOOP, Gdx.files.internal("Cat/Cat_idleTailRight.gif").read());

        currentAnim = animIdleLeft;
        faceDir = 1;
        keyPressedTime = 0f;
        keyDelta = 0.25f;

        cat = new Rectangle();
        cat.x = 800 / 2 - 64 / 2;
        cat.y = 20;

        cat.width = 64;
        cat.height = 64;
    }

    @Override
    public void show() {
        map = new TmxMapLoader().load("Maps/test.tmx");
        renderer = new OrthogonalTiledMapRenderer(map);
        camera = new OrthographicCamera();
        camera.setToOrtho(false, 800, 480);
    }

    @Override
    public void render(float delta) {
       switch (game.state) {
           case PAUSED:
               Gdx.gl.glClearColor( 1, 1, 1, 1 );
               Gdx.gl.glClear( GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT );

               camera.update();

               game.batch.setProjectionMatrix(camera.combined);
               game.batch.begin();
               game.font.setColor(0, 0, 0, 1);
               game.font.getData().setScale(2f, 2f);
               game.font.draw(game.batch, "WHISKERED AWAY", 250, 300);
               game.font.draw(game.batch, "The game is paused", 300, 200);
               game.batch.end();

               if(Gdx.input.isKeyJustPressed(Input.Keys.P)) {
                   game.state = gameState.RUNNING;
               }

               if(Gdx.input.isKeyJustPressed((Input.Keys.Q))) {
                   game.state = gameState.RUNNING;
                   game.setScreen(new MainMenuScreen(game));
               }
               break;
           case RUNNING:
               elapsed += Gdx.graphics.getDeltaTime();
               Gdx.gl.glClearColor(0,0,0,1);
               Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

               renderer.setView(camera);
               renderer.render();
               camera.update();

               game.batch.setProjectionMatrix(camera.combined);

               game.batch.begin();
               game.batch.draw(currentAnim.getKeyFrame(elapsed), cat.x, cat.y, cat.width, cat.height);
               game.batch.end();

               if (Gdx.input.isKeyPressed(Input.Keys.A)) {
                   keyPressedTime += Gdx.graphics.getDeltaTime();
                   if (MathUtils.isZero(keyPressedTime % keyDelta, 0.025f)) cat.x -= 32;
                   currentAnim = animLeft;
                   faceDir = 1;
               }
               if (Gdx.input.isKeyPressed(Input.Keys.D)) {
                   keyPressedTime += Gdx.graphics.getDeltaTime();
                   if (MathUtils.isZero(keyPressedTime % keyDelta, 0.025f)) cat.x += 32;
                   currentAnim = animRight;
                   faceDir = 2;
               }
               if (Gdx.input.isKeyPressed(Input.Keys.W)) {
                   keyPressedTime += Gdx.graphics.getDeltaTime();
                   if (MathUtils.isZero(keyPressedTime % keyDelta, 0.025f)) cat.y += 32;
                   currentAnim = animFront;
               }
               if (Gdx.input.isKeyPressed(Input.Keys.S)) {
                   keyPressedTime += Gdx.graphics.getDeltaTime();
                   if (MathUtils.isZero(keyPressedTime % keyDelta, 0.025f)) cat.y -= 32;
                   currentAnim = animBack;
               }
               if (!(Gdx.input.isKeyPressed(Input.Keys.W)) && !(Gdx.input.isKeyPressed(Input.Keys.A)) && !(Gdx.input.isKeyPressed(Input.Keys.S)) && !(Gdx.input.isKeyPressed(Input.Keys.D))) {
                   keyPressedTime = 0f;
                   if (faceDir == 1) currentAnim = animIdleLeft;
                   else currentAnim = animIdleRight;
               }
               //Pause game
               if(Gdx.input.isKeyJustPressed(Input.Keys.P)) {
                    game.state = gameState.PAUSED;
               }

                /*
                if (cat.x < 0 + 80) cat.x = 0 + 80;
                if (cat.x > 800 - 144) cat.x = 800 - 144;
                if (cat.y < 0 + 32) cat.y = 0 + 32;
                if (cat.y > 480 - 64) cat.y = 480 - 64;
                */

               if (cat.x < 0 + 16) cat.x = 0 + 16;
               if (cat.x > 800 - 48) cat.x = 800 - 48;
               if (cat.y < 0 + 32) cat.y = 0 + 32;
               if (cat.y > 480 - 64) cat.y = 480 - 64;
               break;
       }

    }

    @Override
    public void resize(int width, int height) {
        camera.viewportWidth = width;
        camera.viewportHeight = height;
        camera.update();
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