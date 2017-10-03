/*
 *     Copyright (C) 2017 Adam Torres
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.artifexiumgames.apirateslife.entity;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

/**
 * Created 5/26/2016
 * @author Adam Torres
 */
public class Person implements Entity{

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
