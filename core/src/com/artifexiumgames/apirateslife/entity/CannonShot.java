package com.artifexiumgames.apirateslife.entity;

import com.artifexiumgames.apirateslife.utils.Utils;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.artifexiumgames.apirateslife.item.CannonType;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.BoundingBox;

/**
 * @author Adam Torres
 * Created 5/29/2016.
 */
public class CannonShot implements Entity{

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
    private final int speed = 30;
    private float x,y,rotation;
    private Rectangle hitBox;
    private float range;
    private float imageRotation;
    private CannonShotType cannonShotType;
    private Sprite image;


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
        this.hitBox = image.getBoundingRectangle();
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
        Rectangle rect = image.getBoundingRectangle();
        hitBox.set(rect.x, rect.y, rect.width, rect.height);
    }

    public Rectangle getHitBox(){return hitBox;}

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
