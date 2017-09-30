package com.artifexiumgames.apirateslife.entity;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

public class Person implements Entity{

    /**
     * Created by Adam on 5/26/2016.
     */
    public enum Direction{UP, DOWN, LEFT, RIGHT}

    private String name;
    private int health;
    private Direction dir;
    private final int MAX_HEALTH;
    private Sprite image;
    private float x,y;

    public Person(String name, int health, Sprite image) {
        this.name = name;
        this.MAX_HEALTH = health;
        this.health = MAX_HEALTH;
        this.image = image;
    }

    @Override
    public void draw(SpriteBatch batch) {

    }

    @Override
    public void move(){
        switch (dir){
            case UP:
                y += 1;
                break;
            case DOWN:
                y -= 1;
                break;
            case LEFT:
                x -= 1;
                break;
            case RIGHT:
                x += 1;
                break;
        }

    }

    public void setPosition(float x, float y){
        this.x = x;
        this.y = y;
    }

    public void setDirection(Direction dir){this.dir = dir;}

    @Override
    public Sprite getSprite() {
        return null;
    }

    @Override
    public Vector2 getPosition() {
        return null;
    }

    @Override
    public Vector2 getCenterPosition() {
        return null;
    }

    @Override
    public float getX() {
        return 0;
    }

    @Override
    public float getY() {
        return 0;
    }

    @Override
    public float getCenterX() {
        return 0;
    }

    @Override
    public float getCenterY() {
        return 0;
    }

    @Override
    public void disposeTextures() {
        image.getTexture().dispose();
    }

}
