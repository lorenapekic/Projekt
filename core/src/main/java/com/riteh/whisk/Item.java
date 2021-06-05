package com.riteh.whisk;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;

public class Item {
    private String name;
    private String description;
    private int cost;
    protected Texture itemImage;
    protected Animation<TextureRegion> itemAnim;
    protected Rectangle itemRectangle;
    private boolean pickedUp;

    public Item(String name, String description, int cost, String itemAnimPath) {
        this.name = name;
        this.description = description;
        this.cost = cost;
        //this.itemImage = new Texture(Gdx.files.internal(itemImagePath));
        this.itemAnim = GifDecoder.loadGIFAnimation(Animation.PlayMode.LOOP, Gdx.files.internal(itemAnimPath).read());

        this.itemRectangle = new Rectangle();
        this.itemRectangle.width = 28;
        this.itemRectangle.height = 28;

        this.pickedUp = false;
    }

    public boolean isPickedUp() {
        return pickedUp;
    }

    public void setPickedUp(boolean pickedUp) {
        this.pickedUp = pickedUp;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getCost() {
        return cost;
    }

    public void setCost(int cost) {
        this.cost = cost;
    }
}
