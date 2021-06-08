package com.riteh.whisk;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

/** First screen of the appliplayerion. Displayed after the appliplayerion is created. */
public class FirstScreen implements Screen {

    final WhiskeredAway game;

    OrthographicCamera camera;

    float elapsed;

    float keyPressedTime;
    float keyDelta;

    //Rectangle player;
    Rectangle exit;
    Player player;

    Stage stage = new Stage(new ScreenViewport());
    Skin skin = new Skin(Gdx.files.internal("ButtonSkin/skin.json"));
    mapClass map;
    TiledMapTileLayer.Cell cell;
    int roomX, roomY;
    int[] layers;


    public FirstScreen(final WhiskeredAway game) {
        this.game = game;
        game.setCurrentMusic("Audio/dungeon1.ogg");
        //player = new Rectangle();
        exit = new Rectangle();

        //create current level and spawn potions, set player spawn coordinates and exit coordinates
        map = new mapClass(game.level, 7, 7, game);
        roomX = 7;
        roomY = 7;

        //set entrance and exit coordinates
        float[] entranceCoordinates = map.calculateEntranceCoordinates("north");

        player = new Player(entranceCoordinates[0], entranceCoordinates[1], 64, 100, 0, 0,  "Cat/Cat_walkLeft.gif", "Cat/Cat_walkRight.gif","Cat/Cat_walkFront.gif", "Cat/Cat_walkBehind.gif", "Cat/Cat_idleTailLeft.gif", "Cat/Cat_idleTailRight.gif",
                "Cat/Cat_attackFront.gif", "Cat/Cat_attackBehind.gif", "Cat/Cat_attackLeft.gif", "Cat/Cat_attackRight.gif");

        keyPressedTime = 0f;
        keyDelta = 0.25f;

        exit.width = 16;
        exit.height = 16;
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
                        map = new mapClass(game.level, 7, 7, game);
                        game.setCurrentMusic("Audio/main-theme.ogg");
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
                map.renderer.render(map.visible);
                camera.update();

                game.batch.setProjectionMatrix(camera.combined);

                game.batch.begin();
                  for(Item currentItem : map.items) {
                    if(!currentItem.isPickedUp()) {
                        game.batch.draw(currentItem.itemAnim.getKeyFrame(elapsed), currentItem.itemRectangle.x, currentItem.itemRectangle.y, currentItem.itemRectangle.width, currentItem.itemRectangle.height);
                    }
                }

                for(Enemy currentEnemy : map.enemies) {
                      game.batch.draw(currentEnemy.currentAnim.getKeyFrame(elapsed), currentEnemy.creatureRectangle.x, currentEnemy.creatureRectangle.y, currentEnemy.creatureRectangle.width, currentEnemy.creatureRectangle.height);
                }
               
                game.batch.draw(player.currentAnim.getKeyFrame(elapsed), player.creatureRectangle.x, player.creatureRectangle.y, player.creatureRectangle.width, player.creatureRectangle.height);
                game.font.setColor(1, 1, 1, 1);
                game.font.draw(game.batch, "potion: "+player.updatePotion(), 660, 450);
                game.font.draw(game.batch, "gold: " + player.updateGold(), 660, 420);
                game.font.draw(game.batch, "Health: " + player.getHealth(), 640, 380);
                game.batch.end();


                //check if potions are picked up
                for(Item currentItem : map.items) {
                    if(player.creatureRectangle.contains(currentItem.itemRectangle) && !currentItem.isPickedUp()) {
                        if (currentItem.getName().equals("Portal")) {
                            if (game.counter == 10) {
                                game.counter = 0;
                                game.setScreen(new GameCompleted(game));
                            } else {
                                game.counter++;
                                game.level = new levelClass();
                                game.setScreen(new FirstScreen(game));
                            }
                        }
                        currentItem.setPickedUp(true);
                        //add potion to player's inventory
                        if(currentItem.getName().equals( "Small health potion")){
                            player.addPotion();
                        }else if(currentItem.getName().equals("Coin")){
                            player.addGold();
                        }
                    }
                }

                if (Gdx.input.isKeyPressed(Input.Keys.A)) {
                    keyPressedTime += Gdx.graphics.getDeltaTime();
                    map.isBlocked = map.mapLayer.getCell((int) (player.creatureRectangle.x / 32), (int) ((player.creatureRectangle.y + 16) / 32)).getTile().getProperties().containsKey("blocked");
                    cell = map.westExit.getCell((int) (player.creatureRectangle.x / 32), (int) ((player.creatureRectangle.y + 16) / 32));
                    if (cell != null) map.isExit = map.westExit.getCell((int) (player.creatureRectangle.x / 32), (int) ((player.creatureRectangle.y + 16) / 32)).getTile().getProperties().containsKey("west");
                    if (MathUtils.isZero(keyPressedTime % keyDelta, 0.025f) && map.isExit && map.west) {
                        roomY--;
                        map = new mapClass(game.level, roomX, roomY, game);
                        //set new exit and entrance coordinates
                        float[] entranceCoordinates = new float[2];
                        entranceCoordinates = map.calculateEntranceCoordinates("east");
                        player.creatureRectangle.x = entranceCoordinates[0];
                        player.creatureRectangle.y = entranceCoordinates[1];
                    }
                    if (MathUtils.isZero(keyPressedTime % keyDelta, 0.025f) && !map.isBlocked) {
                        player.move("left");
                        player.currentAnim = player.animLeft;
                        player.currentDir = "left";
                        for(Enemy currentEnemy : map.enemies) {
                            currentEnemy.attack(player);
                            if (player.isDead) game.setScreen(new GameOver(game));
                        }
                    }
                    map.isExit = false;
                }
                if (Gdx.input.isKeyPressed(Input.Keys.D)) {
                    keyPressedTime += Gdx.graphics.getDeltaTime();
                    map.isBlocked = map.mapLayer.getCell((int) ((player.creatureRectangle.x + 32) / 32 + 1), (int) ((player.creatureRectangle.y + 16) / 32)).getTile().getProperties().containsKey("blocked");
                    cell = map.eastExit.getCell((int) ((player.creatureRectangle.x + 32) / 32 + 1), (int) ((player.creatureRectangle.y + 16) / 32));
                    if (cell != null) map.isExit = map.eastExit.getCell((int) ((player.creatureRectangle.x + 32) / 32 + 1), (int) ((player.creatureRectangle.y + 16) / 32)).getTile().getProperties().containsKey("east");
                    if (MathUtils.isZero(keyPressedTime % keyDelta, 0.025f) && map.isExit && map.east) {
                        roomY++;
                        map = new mapClass(game.level, roomX, roomY, game);                       //set new exit and entrance coordinates
                        float[] entranceCoordinates = new float[2];
                        entranceCoordinates = map.calculateEntranceCoordinates("west");
                        player.creatureRectangle.x = entranceCoordinates[0];
                        player.creatureRectangle.y = entranceCoordinates[1];
                    }
                    if (MathUtils.isZero(keyPressedTime % keyDelta, 0.025f) && !map.isBlocked) {
                        player.move("right");
                        player.currentAnim = player.animRight;
                        player.currentDir = "right";
                        for(Enemy currentEnemy : map.enemies) {
                            currentEnemy.attack(player);
                            if (player.isDead) game.setScreen(new GameOver(game));
                        }
                    }
                    map.isExit = false;
                }
                if (Gdx.input.isKeyPressed(Input.Keys.W)) {
                    keyPressedTime += Gdx.graphics.getDeltaTime();
                    map.isBlocked = map.mapLayer.getCell((int) ((player.creatureRectangle.x + 16) / 32), (int) ((player.creatureRectangle.y + 16) / 32 + 1)).getTile().getProperties().containsKey("blocked");
                    cell = map.northExit.getCell((int) ((player.creatureRectangle.x + 16) / 32), (int) ((player.creatureRectangle.y + 16) / 32 + 1));
                    if (cell != null) map.isExit = map.northExit.getCell((int) ((player.creatureRectangle.x + 16) / 32), (int) ((player.creatureRectangle.y + 16) / 32 + 1)).getTile().getProperties().containsKey("north");
                    if (MathUtils.isZero(keyPressedTime % keyDelta, 0.025f) && map.isExit && map.north) {
                        roomX--;
                        map = new mapClass(game.level, roomX, roomY, game);                       //set new exit and entrance coordinates
                        float[] entranceCoordinates = new float[2];
                        entranceCoordinates = map.calculateEntranceCoordinates("south");
                        player.creatureRectangle.x = entranceCoordinates[0];
                        player.creatureRectangle.y = entranceCoordinates[1];
                        for(Enemy currentEnemy : map.enemies) {
                            currentEnemy.attack(player);
                            if (player.isDead) game.setScreen(new GameOver(game));
                        }
                    }
                    if (MathUtils.isZero(keyPressedTime % keyDelta, 0.025f) && !map.isBlocked) {
                        player.move("up");
                        player.currentAnim = player.animBack;
                    }
                    map.isExit = false;
                }
                if (Gdx.input.isKeyPressed(Input.Keys.S)) {
                    keyPressedTime += Gdx.graphics.getDeltaTime();
                    map.isBlocked = map.mapLayer.getCell((int) ((player.creatureRectangle.x + 16) / 32), (int) (player.creatureRectangle.y / 32) - 1).getTile().getProperties().containsKey("blocked");
                    cell = map.southExit.getCell((int) ((player.creatureRectangle.x + 16) / 32), (int) (player.creatureRectangle.y / 32) - 1);
                    if (cell != null) map.isExit = map.southExit.getCell((int) ((player.creatureRectangle.x + 16) / 32), (int) (player.creatureRectangle.y / 32) - 1).getTile().getProperties().containsKey("south");
                    if (MathUtils.isZero(keyPressedTime % keyDelta, 0.025f) && map.isExit && map.south) {
                        roomX++;
                        map = new mapClass(game.level, roomX, roomY, game);                       //set new exit and entrance coordinates
                        float[] entranceCoordinates = new float[2];
                        entranceCoordinates = map.calculateEntranceCoordinates("north");
                        player.creatureRectangle.x = entranceCoordinates[0];
                        player.creatureRectangle.y = entranceCoordinates[1];
                    }
                    if (MathUtils.isZero(keyPressedTime % keyDelta, 0.025f) && !map.isBlocked) {
                        player.move("down");
                        player.currentAnim = player.animFront;
                        for(Enemy currentEnemy : map.enemies) {
                            currentEnemy.attack(player);
                            if (player.isDead) game.setScreen(new GameOver(game));
                        }
                    }
                    map.isExit = false;
                }
                if (!(Gdx.input.isKeyPressed(Input.Keys.W)) && !(Gdx.input.isKeyPressed(Input.Keys.A)) && !(Gdx.input.isKeyPressed(Input.Keys.S)) && !(Gdx.input.isKeyPressed(Input.Keys.D))) {
                    keyPressedTime = 0f;
                    if (player.currentDir.equals("left")) player.currentAnim = player.animIdleLeft;
                    else player.currentAnim = player.animIdleRight;
                }

                //Use potion
                if(Gdx.input.isKeyJustPressed(Input.Keys.Q)) {
                    player.removePotion();
                }

                //Pause game
                if(Gdx.input.isKeyJustPressed(Input.Keys.P)) {
                    game.state = gameState.PAUSED;
                }

                if(Gdx.input.isKeyPressed(Input.Keys.SPACE)) {
                    player.attack(map.enemies);
                    for (Enemy enemy : map.enemies) {
                        if (enemy.isDead) {
                            enemy.creatureRectangle.x = 1500;
                        }
                    }
                }

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
        // Invoked when your appliplayerion is paused.
    }

    @Override
    public void resume() {
        // Invoked when your appliplayerion is resumed after pause.

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