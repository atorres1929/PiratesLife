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

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.Array;

import info.datahelix.apirateslife.entity.CollideableEntity;
import info.datahelix.apirateslife.entity.Player;
import info.datahelix.apirateslife.entity.Ships.NPC_Ships.NPC_Ship;
import info.datahelix.apirateslife.entity.Ships.Ship;
import info.datahelix.apirateslife.utils.Utils;

/**
 * Created 5/19/2016
 * @author Adam Torres
 */
public class Battle {

    private Ship playerShip;
    private SpriteBatch batch;
    Array<CollideableEntity> collideables;
    private Camera camera;

    public Battle(Player player, Array<CollideableEntity> collideables, OrthographicCamera camera, SpriteBatch batch){
        this.playerShip = player.getShip();
        this.collideables = collideables;
        this.camera = camera;
        this.batch = batch;

    }

    public void drawShips(SpriteBatch batch){
        playerShip.draw(batch);
        for (CollideableEntity entity: collideables) {
            entity.draw(batch);
        }
    }


    public void drawShapes(ShapeRenderer renderer){
        for (CollideableEntity entity: collideables) {
            if (entity instanceof NPC_Ship) {
                NPC_Ship ship = (NPC_Ship) entity;
                ship.drawAILine(renderer);
                if (Utils.DEBUG_LINES){
                    ship.drawDebugLines(renderer);
                    playerShip.drawDebugLines(renderer);
                }
            }

        }

    }

    public void update(){
        playerShip.move();
        playerShip.moveShots();
        for (CollideableEntity entity: collideables) {
            if (entity instanceof NPC_Ship){
                NPC_Ship ship = (NPC_Ship) entity;
                if (ship.isDisposable()){
                    ship.disposeTextures();
                    collideables.removeValue(ship, false);
                }
                ship.move();
                ship.moveShots();
            }

        }

        camera.position.set(playerShip.getCenterPosition(), 0);
        camera.update();
    }

    public void disposeNPC_Textures(){
        for (CollideableEntity entity: collideables)
            entity.disposeTextures();
    }

    public Ship getPlayerShip(){return playerShip;}

    public Array<CollideableEntity> getCollideables(){return collideables;
    }

}
