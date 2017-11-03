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

package info.datahelix.apirateslife.entity;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

import info.datahelix.apirateslife.entity.Ships.Ship;
import info.datahelix.apirateslife.item.CannonType;
import info.datahelix.apirateslife.utils.Utils;

/**
 * Created 5/29/2016
 * @author Adam Torres
 */
public class CannonShot extends CollideableEntity{

    public enum CannonShotType{

        ROUNDSHOT, CHAINSHOT, GRAPESHOT, BOMBSHOT;


        /**
         * Returns the percentage multiplier that the {@link CannonType} will be modifed against.
         * <p>e.g. If CHAINSHOT then the CannonType's range will be at 100% of maximum</p>
         * @return  A float that represents the percentage the range should be modified.
         *          <p>If ROUNDSHOT = 1f</p>
         *          <p>If CHAINSHOT = 0.75f</p>
         *          <p>If BOMBSHOT = 0.50f</p>
         *          <p>If GRAPESHOT = 0.25f</p>
         * @see CannonShot
         * @see CannonType
         */
        public float getRangePercentage(){
            switch(this){
                case ROUNDSHOT:
                    return 1;
                case CHAINSHOT:
                    return .75f;
                case BOMBSHOT:
                    return .50f;
                case GRAPESHOT:
                    return .25f;
                default:
                    return 0f;
            }
        }
    }

    private static final float roundShotHullDamage = 1f;
    private static final float roundShotSailDamage = .50f;
    private static final float chainShotHullDamage = .50f;
    private static final float chainShotSailDamage = 1f;
    private static final float grapeShotHullDamage = .25f;
    private static final float grapeShotSailDamage = .10f;
    private static final float bombShotHullDamage = 2f;
    private static final float bombShotSailDamage = .75f;
    protected final int speed = 15;
    protected float x,y,rotation;
    protected float range;
    protected float imageRotation;
    protected CannonShotType cannonShotType;
    protected Sprite image;

    /**
     * Constructs a CannonShot based on a {@link CannonShotType} at specified coordinates
     * @param x
     * @param y
     * @param rotation
     * @param type
     */
    public CannonShot(float x, float y, float rotation, CannonShotType type){
        switch (type){
            case ROUNDSHOT:
                image = new Sprite(new Texture("effects/shots/round_shot.png"));
                break;
            case CHAINSHOT:
                image = new Sprite(new Texture("effects/shots/chain_shot.png"));
                break;
            case GRAPESHOT:
                image = new Sprite(new Texture("effects/shots/grape_shot.png"));
                break;
            case BOMBSHOT:
                image = new Sprite(new Texture("effects/shots/bomb_shot.png"));
                break;
        }
        this.x = x;
        this.y = y;
        this.rotation = rotation;
        this.range = 0;
        this.imageRotation = 0;
        this.cannonShotType = type;
        this.image.setOriginCenter();
        image.setPosition(x,y);
        image.setRotation(rotation);
    }
    @Override
    public void draw(SpriteBatch batch) {
        image.setPosition(x,y);
        image.setRotation(imageRotation);
        image.draw(batch);
    }

    @Override
    public void move() {
        double scale_X = Math.sin(Math.abs(Math.toRadians(rotation)));
        double scale_Y = Math.cos(Math.abs(Math.toRadians(rotation)));

        double distance_X = (speed*scale_X);
        double distance_Y = (speed*scale_Y);
        if (rotation > 0)
            x -= distance_X;
        else
            x += distance_X;
        if (rotation < 90 && rotation > 270)
            y -= distance_Y;
        else
            y += distance_Y;

        imageRotation+=5;
        range += Utils.distance(0, 0, distance_X, distance_Y);
    }

    @Override
    public boolean checkCollision(Entity entity) {
        if (this.getHitBox().overlaps(entity.getHitBox())){
            return true;
        }
        return false;
    }

    @Override
    public void hit(CollideableEntity target, float... damages) {
        if (target instanceof Ship){
            Ship ship = (Ship) target;
            ship.damageHull(damages[0] + damages[1]);
            ship.damageSail(damages[0] + damages[2]);
        }
    }

    public Rectangle getHitBox(){return image.getBoundingRectangle();}

    public CannonShotType getCannonShotType(){return cannonShotType;}

    public float getHullDamage(){
        switch (cannonShotType){
            case ROUNDSHOT:
                return roundShotHullDamage;
            case CHAINSHOT:
                return chainShotHullDamage;
            case GRAPESHOT:
                return grapeShotHullDamage;
            case BOMBSHOT:
                return bombShotHullDamage;
            default:
                return -1;
        }
    }

    public float getSailDamage(){
        switch (cannonShotType){
            case ROUNDSHOT:
                return roundShotSailDamage;
            case CHAINSHOT:
                return chainShotSailDamage;
            case GRAPESHOT:
                return grapeShotSailDamage;
            case BOMBSHOT:
                return bombShotSailDamage;
            default:
                return -1;
        }
    }
    public float getRange(){return range;}

    @Override
    public Sprite getSprite() {
        return image;
    }

    @Override
    public Vector2 getPosition() {
        return new Vector2(x,y);
    }

    @Override
    public Vector2 getCenterPosition() {
        return new Vector2(x+image.getWidth()/2,y+image.getHeight()/2);
    }

    @Override
    public float getX() {
        return x;
    }

    @Override
    public float getY() {
        return y;
    }

    @Override
    public float getCenterX() {
        return x+image.getWidth()/2;
    }

    @Override
    public float getCenterY() {
        return y+image.getHeight()/2;
    }

    @Override
    public void disposeTextures() {
        image.getTexture().dispose();
    }
}
