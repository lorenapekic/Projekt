package com.riteh.whisk;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

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
    Rectangle exit;

    Stage stage = new Stage(new ScreenViewport());
    Skin skin = new Skin(Gdx.files.internal("ButtonSkin/skin.json"));
    mapClass map;
    TiledMapTileLayer.Cell cell;

    public FirstScreen(final WhiskeredAway game) {
        this.game = game;

        cat = new Rectangle();
        exit = new Rectangle();

        //create current level and spawn potions, set cat spawn coordinates and exit coordinates
        map = new mapClass("Maps/test2.tmx", "MapTypePlaceholder", "north"/*, "south"*/);

        //set entrance and exit coordinates
        float[] entranceCoordinates = new float[2];
        entranceCoordinates = map.calculateEntranceCoordinates();
        cat.x = entranceCoordinates[0];
        cat.y = entranceCoordinates[1];

        /*float[] exitCoordinates = new float[2];
        exitCoordinates = map.calculateExitCoordinates();
        exit.x = exitCoordinates[0];
        exit.y = exitCoordinates[1];*/

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

        exit.width = 16;
        exit.height = 16;

        cat.width = 64;
        cat.height = 64;
    }

    @Override
    public void show() {
        camera = new OrthographicCamera();
        camera.setToOrtho(false, 800, 480);
    }

    @Override
    public void render(float delta) {
       switch (game.state) {
           case PAUSED:
               game.currentMusic.pause();
               Gdx.gl.glClearColor( 0, 0, 0, 1 );
               Gdx.gl.glClear( GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT );

               camera.update();

               game.batch.setProjectionMatrix(camera.combined);
               game.batch.begin();
               game.font.setColor(1, 1, 1, 1);
               game.font.getData().setScale(2f, 2f);
               game.font.draw(game.batch, "WHISKERED AWAY", 270, 400);
               game.font.draw(game.batch, "The game is paused", 270, 300);
               game.batch.end();

               Gdx.input.setInputProcessor(stage);
               TextButton resume = new TextButton("Resume", skin);
               TextButton quit = new TextButton("Quit", skin);

               resume.setHeight(40);
               resume.setWidth(100);
               resume.getLabel().setFontScaleX(1.2f);
               resume.getLabel().setFontScaleY(1.2f);

               quit.setHeight(40);
               quit.setWidth(100);
               quit.getLabel().setFontScaleX(1.2f);
               quit.getLabel().setFontScaleY(1.2f);

               resume.setPosition(camera.viewportWidth/2 - resume.getWidth()/2, camera.viewportHeight/2 - resume.getHeight());
               quit.setPosition(camera.viewportWidth/2 - resume.getWidth()/2, camera.viewportHeight/2 - 50 - quit.getHeight());

               resume.addListener(new ChangeListener() {
                    @Override
                    public void changed(ChangeEvent event, Actor actor) {
                        game.selectSoundEffect.play(0.6f);
                        game.state = gameState.RUNNING;
                        stage.clear();
                    }
               });

               quit.addListener(new ChangeListener() {
                    @Override
                    public void changed(ChangeEvent event, Actor actor) {
                        game.selectSoundEffect.play(0.6f);
                        game.state = gameState.RUNNING;
                        map = new mapClass("Maps/level0.tmx", "MapTypePlaceholder", "north"/*, "east"*/);
                        game.setScreen(new MainMenuScreen(game));
                    }
               });

               stage.addActor(resume);
               stage.addActor(quit);
               stage.act();
               stage.draw();
               break;

           case RUNNING:
               game.currentMusic.play();
               elapsed += Gdx.graphics.getDeltaTime();
               Gdx.gl.glClearColor(0,0,0,1);
               Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

               map.renderer.setView(camera);
               int[] type = new int[]{0,1,2,3,4};
               map.renderer.render(type);
               camera.update();

               game.batch.setProjectionMatrix(camera.combined);


               game.batch.begin();
               //draw potions
               for(Potion currentPotion : map.potions) {
                   if(!currentPotion.isPickedUp()) {
                       game.batch.draw(currentPotion.itemImage, currentPotion.itemRectangle.x, currentPotion.itemRectangle.y, currentPotion.itemRectangle.width, currentPotion.itemRectangle.height);
                   }
               }
               game.batch.draw(currentAnim.getKeyFrame(elapsed), cat.x, cat.y, cat.width, cat.height);
               game.batch.end();

               //check if potions are picked up
               for(Potion currentPotion : map.potions) {
                   if(cat.overlaps(currentPotion.itemRectangle)) {
                       currentPotion.setPickedUp(true);
                       //add potion to player's inventory
                   }
               }


               if (Gdx.input.isKeyPressed(Input.Keys.A)) {
                   keyPressedTime += Gdx.graphics.getDeltaTime();
                   map.isBlocked = map.mapLayer.getCell((int) (cat.x / 32), (int) ((cat.y + 16) / 32)).getTile().getProperties().containsKey("blocked");
                   cell = map.westExit.getCell((int) (cat.x / 32), (int) ((cat.y + 16) / 32));
                   if (cell != null) map.isExit = map.westExit.getCell((int) (cat.x / 32), (int) ((cat.y + 16) / 32)).getTile().getProperties().containsKey("west");
                   if (MathUtils.isZero(keyPressedTime % keyDelta, 0.025f) && map.isExit) {
                       map = new mapClass("Maps/level6.tmx", "MapTypePlaceholder", "east"/*, "east"*/);
                       //set new exit and entrance coordinates
                       float[] entranceCoordinates = new float[2];
                       entranceCoordinates = map.calculateEntranceCoordinates();
                       cat.x = entranceCoordinates[0];
                       cat.y = entranceCoordinates[1];
                   }
                   if (MathUtils.isZero(keyPressedTime % keyDelta, 0.025f) && !map.isBlocked) cat.x -= 32;
                       currentAnim = animLeft;
                       faceDir = 1;
               }
               if (Gdx.input.isKeyPressed(Input.Keys.D)) {
                   keyPressedTime += Gdx.graphics.getDeltaTime();
                   map.isBlocked = map.mapLayer.getCell((int) ((cat.x + 32) / 32 + 1), (int) ((cat.y + 16) / 32)).getTile().getProperties().containsKey("blocked");
                   cell = map.eastExit.getCell((int) ((cat.x + 32) / 32 + 1), (int) ((cat.y + 16) / 32));
                   if (cell != null) map.isExit = map.eastExit.getCell((int) ((cat.x + 32) / 32 + 1), (int) ((cat.y + 16) / 32)).getTile().getProperties().containsKey("east");
                   if (MathUtils.isZero(keyPressedTime % keyDelta, 0.025f) && map.isExit) {
                       map = new mapClass("Maps/level6.tmx", "MapTypePlaceholder", "west"/*, "east"*/);
                       //set new exit and entrance coordinates
                       float[] entranceCoordinates = new float[2];
                       entranceCoordinates = map.calculateEntranceCoordinates();
                       cat.x = entranceCoordinates[0];
                       cat.y = entranceCoordinates[1];
                   }
                   if (MathUtils.isZero(keyPressedTime % keyDelta, 0.025f) && !map.isBlocked) cat.x += 32;
                       currentAnim = animRight;
                       faceDir = 2;
               }
               if (Gdx.input.isKeyPressed(Input.Keys.W)) {
                   keyPressedTime += Gdx.graphics.getDeltaTime();
                   map.isBlocked = map.mapLayer.getCell((int) ((cat.x + 16) / 32), (int) ((cat.y + 16) / 32 + 1)).getTile().getProperties().containsKey("blocked");
                   cell = map.northExit.getCell((int) ((cat.x + 16) / 32), (int) ((cat.y + 16) / 32 + 1));
                   if (cell != null) map.isExit = map.northExit.getCell((int) ((cat.x + 16) / 32), (int) ((cat.y + 16) / 32 + 1)).getTile().getProperties().containsKey("north");
                   if (MathUtils.isZero(keyPressedTime % keyDelta, 0.025f) && map.isExit) {
                       map = new mapClass("Maps/level6.tmx", "MapTypePlaceholder", "south"/*, "east"*/);
                       //set new exit and entrance coordinates
                       float[] entranceCoordinates = new float[2];
                       entranceCoordinates = map.calculateEntranceCoordinates();
                       cat.x = entranceCoordinates[0];
                       cat.y = entranceCoordinates[1];
                   }
                   if (MathUtils.isZero(keyPressedTime % keyDelta, 0.025f) && !map.isBlocked) cat.y += 32;
                   currentAnim = animFront;
               }
               if (Gdx.input.isKeyPressed(Input.Keys.S)) {
                   keyPressedTime += Gdx.graphics.getDeltaTime();
                   map.isBlocked = map.mapLayer.getCell((int) ((cat.x + 16) / 32), (int) (cat.y / 32) - 1).getTile().getProperties().containsKey("blocked");
                   cell = map.southExit.getCell((int) ((cat.x + 16) / 32), (int) (cat.y / 32) - 1);
                   if (cell != null) map.isExit = map.southExit.getCell((int) ((cat.x + 16) / 32), (int) (cat.y / 32) - 1).getTile().getProperties().containsKey("south");
                   if (MathUtils.isZero(keyPressedTime % keyDelta, 0.025f) && map.isExit) {
                       map = new mapClass("Maps/level6.tmx", "MapTypePlaceholder", "north"/*, "east"*/);
                       //set new exit and entrance coordinates
                       float[] entranceCoordinates = new float[2];
                       entranceCoordinates = map.calculateEntranceCoordinates();
                       cat.x = entranceCoordinates[0];
                       cat.y = entranceCoordinates[1];
                   }
                   if (MathUtils.isZero(keyPressedTime % keyDelta, 0.025f) && !map.isBlocked) cat.y -= 32;
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
        stage.clear();
        stage.getViewport().update(width, height, true);
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
        stage.dispose();
        skin.dispose();
    }
}