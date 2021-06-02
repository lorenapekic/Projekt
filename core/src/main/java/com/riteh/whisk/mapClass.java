package com.riteh.whisk;

import com.badlogic.gdx.maps.Map;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.utils.Array;

import java.util.concurrent.ThreadLocalRandom;

public class mapClass extends MapLayoutCreator {
    String name; //filepath to map
    String type;
    String entrance;
    String exit;
    int exits; //number of exits
    int spawns; //number of spawn tiles
    TiledMap currentLevel;
    OrthogonalTiledMapRenderer renderer;
    TiledMapTileLayer mapLayer;
    TiledMapTileLayer northExit;
    TiledMapTileLayer westExit;
    TiledMapTileLayer southExit;
    TiledMapTileLayer eastExit;
    boolean isBlocked;
    boolean isExit;
    Array<Potion> potions; //array that holds potions for the current level

    public mapClass(String name, String type, String entrance) {
        this.name = name;
        this.type = type;
        this.entrance = entrance;
        ThreadLocalRandom random = ThreadLocalRandom.current();

        this.currentLevel = new TmxMapLoader().load(this.name);
        this.mapLayer = (TiledMapTileLayer) currentLevel.getLayers().get(0);
        this.northExit = (TiledMapTileLayer) currentLevel.getLayers().get(1);
        this.westExit = (TiledMapTileLayer) currentLevel.getLayers().get(2);
        this.southExit = (TiledMapTileLayer) currentLevel.getLayers().get(3);
        this.eastExit = (TiledMapTileLayer) currentLevel.getLayers().get(4);
        this.renderer = new OrthogonalTiledMapRenderer(currentLevel, 2f);

        this.potions = new Array<Potion>();
        spawnPotions();
    }

    //not sure if we even need this
    /*public void changeMap(String mapName) {
        Map newMap = new TmxMapLoader().load(mapName);
        this.renderer.getMap().dispose();
        this.renderer.setMap((TiledMap) newMap);
        this.mapLayer = (TiledMapTileLayer) newMap.getLayers().get(0);
    }*/

    //creates an array of potions for the current map
    public void spawnPotions() {
        int currentPotion = 0;
        for (MapObject object : this.currentLevel.getLayers().get("objects").getObjects()) {
            if (object instanceof RectangleMapObject) {
                RectangleMapObject rect = ((RectangleMapObject) object);
                if (object.getProperties().containsKey("spawn")) {
                    Potion new_potion = new Potion("Small health potion", "Restores 50 health", 50, "Cat/potion.jpg");
                    new_potion.setHpRestore(50);
                    potions.add(new_potion);
                    potions.get(currentPotion).itemRectangle.x = rect.getRectangle().x*2;
                    potions.get(currentPotion).itemRectangle.y = rect.getRectangle().y*2;
                    currentPotion++;
                }
            }
        }
    }

    //get entrance coordinates
    public float[] calculateEntranceCoordinates() {
        float[] coordinates = new float[2];

        for (MapObject object : this.currentLevel.getLayers().get("objects").getObjects()) {
            if (object instanceof RectangleMapObject) {
                RectangleMapObject rect = ((RectangleMapObject) object);
                if (object.getProperties().containsKey(this.entrance)) {
                    coordinates[0] = rect.getRectangle().x*2;
                    coordinates[1] = rect.getRectangle().y*2;
                }
            }
        }
        return coordinates;
    }
}
