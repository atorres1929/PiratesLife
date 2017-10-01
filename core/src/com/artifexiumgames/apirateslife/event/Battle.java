package com.artifexiumgames.apirateslife.event;

import com.artifexiumgames.apirateslife.entity.CannonShot;
import com.artifexiumgames.apirateslife.entity.NPC_Ship;
import com.artifexiumgames.apirateslife.entity.Player;
import com.artifexiumgames.apirateslife.entity.Ship;
import com.artifexiumgames.apirateslife.utils.Utils;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;

/**
 * Created by Adam on 5/19/2016.
 */
public class Battle {

    private Ship playerShip;
    private Array<NPC_Ship> npc_ships;
    private SpriteBatch batch;
    private Camera camera;

    public Battle(Player player, Array<Ship> npc_ships, OrthographicCamera camera, SpriteBatch batch){
        this.playerShip = player.getShip();
        this.npc_ships = new Array<NPC_Ship>();
        for (Ship ship: npc_ships){
            this.npc_ships.add((NPC_Ship) ship);
        }
        this.camera = camera;
        this.batch = batch;

    }

    public void drawShips(SpriteBatch batch){
        playerShip.draw(batch);
        for (NPC_Ship npc_ship: npc_ships) {
            npc_ship.draw(batch);
        }
    }


    public void drawShapes(ShapeRenderer renderer){
        for (NPC_Ship npc_ship: npc_ships) {
            renderer.setColor(npc_ship.getAggro_Color());
            npc_ship.drawAILine(renderer);
        }
    }

    public void update(){
        playerShip.move();
        playerShip.moveShots();
        for (NPC_Ship enemy: npc_ships) {
            if (enemy.isDead()){
                enemy.disposeTextures();
                npc_ships.removeValue(enemy, false);
            }
            enemy.move();
            enemy.moveShots();
        }

        camera.position.set(playerShip.getCenterPosition(), 0);
        camera.update();
    }

    public void disposeTextures(){
        for (NPC_Ship npc_ship: npc_ships)
            npc_ship.disposeTextures();
    }

    public Ship getPlayerShip(){return playerShip;}
    public Array<NPC_Ship> getNPCShips(){return npc_ships;}

}