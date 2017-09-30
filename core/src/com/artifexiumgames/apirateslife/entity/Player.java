package com.artifexiumgames.apirateslife.entity;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;

/**
 * Created by Adam on 5/26/2016.
 */
public class Player extends Person {

    private Ship ship;
    public Player(String name, Ship ship){
        super(name, 100, new Sprite(new Texture("people/player.png")));
        this.ship = ship;
    }

    public Ship getShip(){return ship;}


}
