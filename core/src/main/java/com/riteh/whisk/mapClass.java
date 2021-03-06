package com.riteh.whisk;

import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.utils.Array;

import java.util.ArrayList;
import java.util.concurrent.ThreadLocalRandom;

import squidpony.squidai.DijkstraMap;

public class mapClass extends DijkstraMap {
    final WhiskeredAway game;
    String name; //filepath to map
    TiledMap currentLevel;
    OrthogonalTiledMapRenderer renderer;
    TiledMapTileLayer mapLayer;
    TiledMapTileLayer northExit;
    TiledMapTileLayer westExit;
    TiledMapTileLayer southExit;
    TiledMapTileLayer eastExit;
    boolean isBlocked;
    boolean isExit;
    boolean north, east, south, west;
    Array<Item> items;
    Array<Enemy> enemies;
    int x, y;
    int visible[];

    public mapClass(levelClass level, int x, int y, final WhiskeredAway game) {
        this.name = "Maps/level" + level.level[x][y] + ".tmx";
        this.game = game;

        if (level.level[x-1][y] != '-') this.north = true;
        else this.north = false;
        if (level.level[x][y-1] != '-') this.west = true;
        else this.west = false;
        if (level.level[x+1][y] != '-') this.south = true;
        else this.south = false;
        if (level.level[x][y+1] != '-') this.east = true;
        else this.east = false;

        this.calculateVisibleLayers();

        this.x = x;
        this.y = y;
        //this.entrance = entrance;
        this.currentLevel = new TmxMapLoader().load(this.name);
        this.mapLayer = (TiledMapTileLayer) currentLevel.getLayers().get(0);
        this.northExit = (TiledMapTileLayer) currentLevel.getLayers().get(1);
        this.westExit = (TiledMapTileLayer) currentLevel.getLayers().get(2);
        this.southExit = (TiledMapTileLayer) currentLevel.getLayers().get(3);
        this.eastExit = (TiledMapTileLayer) currentLevel.getLayers().get(4);
        this.renderer = new OrthogonalTiledMapRenderer(currentLevel, 2f);

        this.items = new Array<Item>();
        this.enemies = new Array<Enemy>();
        if (!this.name.equals("Maps/levelS.tmx")) spawnItems();
    }

    public void calculateVisibleLayers() {
        ArrayList<Integer> array = new ArrayList<Integer>();
        int cnt = 0;
        array.add(0);
        if (this.north) array.add(1);
        if (this.west) array.add(2);
        if (this.south) array.add(3);
        if (this.east) array.add(4);

        this.visible = new int[array.size()];
        for (int i = 0; i < visible.length; i++) {
            this.visible[i] = array.get(i);
        }
        return;
    }

    //creates an array of items for the current map
    public void spawnItems() {
        ThreadLocalRandom random = ThreadLocalRandom.current();
        int chance;
        int currentItem = 0;
        int currentEnemy = 0;

        if (this.name.equals("Maps/levelE.tmx")) {
            for (MapObject object : this.currentLevel.getLayers().get("objects").getObjects()) {
                if (object instanceof RectangleMapObject) {
                    RectangleMapObject rect = ((RectangleMapObject) object);
                    if (object.getProperties().containsKey("spawn")) {
                        Item new_item;
                        new_item = new Item("Portal", "Takes you to the next level", 0, "Items/portal.gif");
                        items.add(new_item);
                        items.get(currentItem).itemRectangle.x = rect.getRectangle().x*2+12;
                        items.get(currentItem).itemRectangle.y = rect.getRectangle().y*2+5;
                        currentItem++;
                    }
                }
            }
        } else {
            for (MapObject object : this.currentLevel.getLayers().get("objects").getObjects()) {
                if (object instanceof RectangleMapObject) {
                    RectangleMapObject rect = ((RectangleMapObject) object);
                    if (object.getProperties().containsKey("spawn")) {
                        Item new_item = null;
                        Enemy new_enemy;
                        chance = random.nextInt(1, 100);
                        if (chance < 70) {
                            //put enemy spawn here
                            new_enemy = new Enemy(rect.getRectangle().x*2, rect.getRectangle().y*2, 64, 50, "Birb", "Enemies/blueBird_walkLeft.gif", "Enemies/blueBird_walkRight.gif", "Enemies/blueBird_walkFront.gif", "Enemies/blueBird_walkBehind.gif",
                                    "Enemies/blueBird_attackFront.gif", "Enemies/blueBird_attackBehind.gif", "Enemies/blueBird_attackLeft.gif", "Enemies/blueBird_attackRight.gif");
                            enemies.add(new_enemy);
                            currentEnemy++;
                        } else {
                            new_item = new Item("Small health potion", "Restores 50 health", 50, "Items/catFood.gif");
                            items.add(new_item);
                            items.get(currentItem).itemRectangle.x = rect.getRectangle().x*2+12;
                            items.get(currentItem).itemRectangle.y = rect.getRectangle().y*2+3;
                            currentItem++;


                        }

                    }
                }
            }
        }
    }

    //get entrance/spawn coordinates
    public float[] calculateEntranceCoordinates(String entrance) {
        float[] coordinates = new float[2];

        if (this.name.equals("Maps/levelS.tmx") && !this.game.level.hasEntered) {
            this.game.level.hasEntered = true;
            for (MapObject object : this.currentLevel.getLayers().get("objects").getObjects()) {
                if (object instanceof RectangleMapObject) {
                    RectangleMapObject rect = ((RectangleMapObject) object);
                    if (object.getProperties().containsKey("spawn")) {
                        coordinates[0] = rect.getRectangle().x*2;
                        coordinates[1] = rect.getRectangle().y*2;
                    }
                }
            }
        } else {
            for (MapObject object : this.currentLevel.getLayers().get("objects").getObjects()) {
                if (object instanceof RectangleMapObject) {
                    RectangleMapObject rect = ((RectangleMapObject) object);
                    if (object.getProperties().containsKey(entrance)) {
                        coordinates[0] = rect.getRectangle().x * 2;
                        coordinates[1] = rect.getRectangle().y * 2;
                    }
                }
            }
        }
        return coordinates;
    }
}
