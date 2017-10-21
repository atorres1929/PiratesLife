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

package info.datahelix.apirateslife.event;

import info.datahelix.apirateslife.entity.Ships.NPC_Ship;
import info.datahelix.apirateslife.entity.Player;
import info.datahelix.apirateslife.entity.Ships.Ship;
import info.datahelix.apirateslife.utils.Utils;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.Array;

/**
 * Created 5/19/2016
 * @author Adam Torres
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
            npc_ship.drawAILine(renderer);
        }
        if (Utils.DEBUG_LINES){
            for (NPC_Ship npc_ship: npc_ships){
                npc_ship.drawDebugLines(renderer);
            }
            playerShip.drawDebugLines(renderer);
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

    public void disposeNPC_Textures(){
        for (NPC_Ship npc_ship: npc_ships)
            npc_ship.disposeTextures();
    }

    public Ship getPlayerShip(){return playerShip;}
    public Array<NPC_Ship> getNPCShips(){return npc_ships;}

}
