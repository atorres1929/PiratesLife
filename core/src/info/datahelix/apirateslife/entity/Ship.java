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

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

import info.datahelix.apirateslife.effect.CannonSmoke;
import info.datahelix.apirateslife.effect.FadeInFadeOutEffectArea;
import info.datahelix.apirateslife.effect.FadeOutEffect;
import info.datahelix.apirateslife.item.CannonType;
import info.datahelix.apirateslife.screens.BattleWorld;
import info.datahelix.apirateslife.utils.Line;
import info.datahelix.apirateslife.utils.Utils;

/**
 * Created 5/24/2016
 * @author Adam Torres
 */
public class Ship implements Entity, Cloneable{

    public enum Direction{LEFT, RIGHT, STRAIGHT}
    public enum SailState{FULL, HALF, NONE}
    public enum ShipType{GUNBOAT, BRIGANTINE}
    public final ShipType SHIP_TYPE;

    protected final float DEGREES_360 = 360;
    protected final float DEGREES_270 = 270;
    protected final float DEGREES_180 = 180;
    protected final float DEGREES_90 = 90;
    protected final float DEGREES_0 = 0;
    protected final int HOW_FAR_FROM_SHIP = 100;
    protected final Sprite image;
    protected final Sprite smoke;
    protected final float imageWidth;
    protected final float imageHeight;

    protected Array<CannonShot> cannonShots;
    protected Array<Ship> targets;
    protected CannonType cannonType;
    protected CannonShot.CannonShotType loadedShot;
    protected final int MAX_CANNONS_LEFT, MAX_CANNONS_RIGHT, MAX_CANNONS_FRONT, MAX_CANNONS_BACK;
    protected final int MAX_CANNONS;
    protected final int MAX_CREW;
    protected final int MAX_HULL;
    protected final int MAX_SAILS;
    protected final float MAX_SPEED;
    protected SailState sailState;
    protected Direction dir;
    protected int cannonsLeft, cannonsRight, cannonsForward, cannonsBack;
    protected long reloadTimerForward, reloadTimerBack, reloadTimerLeft, reloadTimerRight;
    protected int reloadTime;
    protected Line leftAimingLine, rightAimingLine, forwardAimingLine, backwardAimingLine;
    protected float cannonRange;
    protected String name;
    protected int crew;
    protected float hull;
    protected float sails;
    protected float speed;
    protected float x, y;
    protected float rotation;
    protected boolean dead;

    private FadeInFadeOutEffectArea sinkingSmoke;
    private FadeOutEffect sinkingFadingAway;
    private Array<CannonSmoke> cannonSmokes;
    private BitmapFont healthDisplay;
    private int numSinkingClouds;
    private final float SINKING_CLOUD_DELAY = 1;
    private final int SINKING_CLOUD_REPEAT = 3;//Must be odd number to end on Fade out
    private final float SINKING_SHIP_TIME_TO_FADE = 3;
    private final float SINKING_SHIP_ANIMATION_DELAY = 5;
    private final float SMOKE_CLOUD_TIME_TO_FADE = .5f;
    private final float SMOKE_CLOUD_ANIMATION_DELAY = .5f;

    /**
     * Builds a ship at a given location and rotation and of a specific model.
     * @param shipType The type of ship to be drawn
     * @param x The x coordinate where the ship should be placed
     * @param y The y coordinate where the ship should be placed
     * @param r The rotation the ship should be set at when placed.
     */
    public Ship(String name, ShipType shipType, CannonType cannonType, CannonShot.CannonShotType cannonShotType, float x, float y, float r){

        switch(shipType){
            case GUNBOAT:
                MAX_CANNONS_LEFT = 1;
                MAX_CANNONS_RIGHT = 1;
                MAX_CANNONS_BACK = 0;
                MAX_CANNONS_FRONT = 0;
                MAX_CREW =10;
                MAX_HULL = 100;
                MAX_SAILS = 70;
                MAX_SPEED = 5;
                crew = MAX_CREW;
                hull = MAX_HULL;
                sails = MAX_SAILS;
                image = new Sprite(new Texture("ships/gunboat.png"));
                numSinkingClouds = 5;
                SHIP_TYPE = ShipType.GUNBOAT;
                break;
            case BRIGANTINE:
                MAX_CANNONS_LEFT = 4;
                MAX_CANNONS_RIGHT = 4;
                MAX_CANNONS_BACK = 1;
                MAX_CANNONS_FRONT = 1;
                MAX_CREW = 40;
                MAX_HULL = 300;
                MAX_SAILS = 200;
                MAX_SPEED = 2;
                crew = MAX_CREW;
                hull = MAX_HULL;
                sails = MAX_SAILS;
                image = new Sprite(new Texture("ships/brigantine.png"));
                numSinkingClouds = 30;
                SHIP_TYPE = ShipType.BRIGANTINE;
                break;
            default:
                MAX_CANNONS_LEFT = 0;
                MAX_CANNONS_RIGHT = 0;
                MAX_CANNONS_BACK = 0;
                MAX_CANNONS_FRONT = 0;
                MAX_CREW = 0;
                MAX_HULL = 0;
                MAX_SAILS = 0;
                MAX_SPEED = 0;
                image = null;
                SHIP_TYPE = null;
                break;
        }
        dead = false;
        sailState = SailState.NONE;
        MAX_CANNONS = MAX_CANNONS_BACK+MAX_CANNONS_FRONT+MAX_CANNONS_RIGHT+MAX_CANNONS_LEFT;
        cannonsLeft = MAX_CANNONS_LEFT;
        cannonsRight = MAX_CANNONS_RIGHT;
        cannonsBack = MAX_CANNONS_BACK;
        cannonsForward = MAX_CANNONS_FRONT;
        this.cannonType = cannonType;
        this.loadedShot = cannonShotType;
        dir = Direction.STRAIGHT;
        imageWidth = image.getWidth();
        imageHeight = image.getHeight();

        this.name = name;
        this.x = x;
        this.y = y;
        rotation = r;
        smoke = new Sprite(new Texture("effects/shots/smoke.png"));
        healthDisplay = new BitmapFont(Gdx.files.internal("fonts/corsiva_title_black.fnt"));
        sinkingSmoke = new FadeInFadeOutEffectArea(this, smoke, SINKING_CLOUD_DELAY, this.getSprite().getWidth(), numSinkingClouds, SINKING_CLOUD_REPEAT);
        sinkingFadingAway = new FadeOutEffect(this, SINKING_SHIP_TIME_TO_FADE, SINKING_SHIP_ANIMATION_DELAY);
        cannonSmokes = new Array<CannonSmoke>();
        image.getTexture().setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        image.setOriginCenter();
        image.setPosition(x,y);
        image.setRotation(rotation);
        cannonRange = cannonType.getRange()+cannonType.getRange()*loadedShot.getRangePercentage();
        cannonShots = new Array<CannonShot>();
        reloadTime = 0; //TODO change back to 1 e.g. 1 sec per reload
        targets = new Array<Ship>();
        rightAimingLine = new Line();
        leftAimingLine = new Line();
        forwardAimingLine = new Line();
        backwardAimingLine = new Line();
    }

    /**
     * Draws the ship at the appropriate location and appropriate rotation.
     * @param batch the batch that the ship is drawn from.
     */
    @Override
    public void draw(SpriteBatch batch){
        for (CannonShot cannonShot: cannonShots) {
            cannonShot.draw(batch);
        }


        for (CannonSmoke smoke: cannonSmokes){
            smoke.animate(batch, Utils.DELTA);
            if (smoke.done()){
                cannonSmokes.removeValue(smoke, false);
            }
        }

        healthDisplay.draw(batch, ""+rotation, x, y);

        if (hull <= 0){
            sinkingFadingAway.animate(batch, Utils.DELTA);
            sinkingSmoke.animate(batch, Utils.DELTA);

            if (sinkingSmoke.done()){
                this.dead = true;
            }
        }

        //draw ship last
        image.setPosition(x,y);
        image.setRotation(rotation);
        image.draw(batch);
    }

    public void drawDebugLines(ShapeRenderer shapeRenderer){
        shapeRenderer.setColor(Color.RED);
        Rectangle rect = getHitBox();
        shapeRenderer.rect(rect.x, rect.y, rect.width, rect.height);
        shapeRenderer.line(rightAimingLine.getPos1(), rightAimingLine.getPos2());
        shapeRenderer.line(leftAimingLine.getPos1(), leftAimingLine.getPos2());
        shapeRenderer.line(forwardAimingLine.getPos1(), forwardAimingLine.getPos2());
        shapeRenderer.line(backwardAimingLine.getPos1(), backwardAimingLine.getPos2());
    }

    public void moveShots(){
        for (CannonShot cannonShot: cannonShots){
            if (cannonShot.getRange() >= cannonRange) {
                cannonShot.disposeTextures();
                cannonShots.removeValue(cannonShot, false);
            }
            else{
                for (Ship target: targets){
                    if (target.getHitBox().contains(cannonShot.getHitBox())) {
                        target.hit(cannonShot, cannonType, target);
                        cannonShot.disposeTextures();
                        cannonShots.removeValue(cannonShot, false);
                    }
                }
            }
            cannonShot.move();
        }
        for (CannonSmoke smoke: cannonSmokes){
            smoke.move();
        }
    }

    protected void hit(CannonShot cannonShot, CannonType cannonType, Ship target){
        float hullDamage;
        float sailDamage;
        hullDamage = cannonType.getDamage()*cannonShot.getHullDamage();
        sailDamage = cannonType.getDamage()*cannonShot.getSailDamage();

        target.hull -= hullDamage;
        target.sails -= sailDamage;
    }

    @Override
    public void move(){
        if (hull > 0) {
            if (rotation % DEGREES_360 == 0)
                rotation = 0;
            switch (dir) {
                case STRAIGHT:
                    break;
                case LEFT:
                    moveLeft();
                    break;
                case RIGHT:
                    moveRight();
                    break;
            }
            switch (sailState) {
                case FULL:
                    if (speed < MAX_SPEED){
                        speed+=0.01f;
                    }
                    break;
                case HALF:
                    if (speed > MAX_SPEED / 2) {
                        speed-=0.01f;
                    }
                    else if (speed < MAX_SPEED / 2){
                        speed+=0.01f;
                    }
                    break;
                case NONE:
                    if (speed > 0){
                        speed-=0.01f;
                    }
                    break;
            }
            ensureWithinBorders();

            double scale_X = Math.sin(Math.abs(rotation * Math.PI / DEGREES_180));
            double scale_Y = Math.cos(Math.abs(rotation * Math.PI / DEGREES_180));

            double distance_X = (speed * scale_X);
            double distance_Y = (speed * scale_Y);

            if (rotation > DEGREES_0)
                x -= distance_X;
            else
                x += distance_X;
            if (rotation < DEGREES_90 && rotation > DEGREES_270)
                y -= distance_Y;
            else
                y += distance_Y;

            calculateAimingLines();
        }
    }

    /**
     * Ensures the ship is still in the borders of the map, and if not, places is back in the borders with it's rotation reversed.
     */
    private void ensureWithinBorders(){
        if (x > BattleWorld.WORLD_SIZE-imageWidth) {
            rotation += DEGREES_180;
            x -= 10;
        }
        else if  (x < 0+imageWidth) {
            rotation += DEGREES_180;
            x += 10;
        }
        else if (y > BattleWorld.WORLD_SIZE-imageHeight){
            rotation += DEGREES_180;
            y -= 10;
        }
        else if (y < 0+imageHeight){
            rotation += DEGREES_180;
            y += 10;
        }
    }

    /**
     * Increases the rotation of the ship by 1 degree. Based on a circle of 360 degrees where 0 degrees is North of the ship.
     */
    private void moveLeft(){rotation++;}

    /**
     * Reduces the rotation of the ship by 1 degree. Based on a circle of 360 degrees where 0 degrees is North of the ship.
     */
    private void moveRight(){rotation--;}

    public void fireForwardCannons(){
        if ((System.currentTimeMillis() - reloadTimerForward)/1000 >= reloadTime * cannonsForward) {

            CannonShot cannonShot;
            CannonSmoke cannonSmoke;
            Vector2 cannonAndSmokePos = new Vector2();

            if (cannonsForward == 1){
                cannonAndSmokePos.x = getCenterX();
                cannonAndSmokePos.y = getCenterY();
                cannonAndSmokePos = Utils.rotatePoint(getCenterX(), getCenterY(), DEGREES_180 - rotation, cannonAndSmokePos);
                cannonShot = new CannonShot(cannonAndSmokePos.x, cannonAndSmokePos.y, rotation, loadedShot);
                cannonSmoke = new CannonSmoke(smoke, SMOKE_CLOUD_TIME_TO_FADE, SMOKE_CLOUD_ANIMATION_DELAY, cannonAndSmokePos.x, cannonAndSmokePos.y, rotation);
                cannonSmokes.add(cannonSmoke);
                cannonShots.add(cannonShot);
            }
            else {
                //Used to flip between placing a shot on the left or right of the ship
                boolean flip = true;
                int cannonSeparationLeft = 16;
                int cannonSeparationRight = 0;
                for (int i = 0; i < cannonsForward; i++) {

                    if (flip) {
                        flip = false;
                        cannonAndSmokePos.x = getCenterX() - cannonSeparationLeft;
                        cannonAndSmokePos.y = getCenterY();
                        cannonSeparationLeft += 16;
                    }
                    else {
                        flip = true;
                        cannonAndSmokePos.x = getCenterX() + cannonSeparationRight;
                        cannonAndSmokePos.y = getCenterY();
                        cannonSeparationRight += 16;
                    }
                    cannonAndSmokePos = Utils.rotatePoint(getCenterX(), getCenterY(), DEGREES_180 - rotation, cannonAndSmokePos);
                    cannonShot = new CannonShot(cannonAndSmokePos.x, cannonAndSmokePos.y, rotation, loadedShot);
                    cannonSmoke = new CannonSmoke(smoke, SMOKE_CLOUD_TIME_TO_FADE, SMOKE_CLOUD_ANIMATION_DELAY, cannonAndSmokePos.x, cannonAndSmokePos.y, rotation);
                    cannonSmokes.add(cannonSmoke);
                    cannonShots.add(cannonShot);
                }
            }
            reloadTimerForward = System.currentTimeMillis();
        }
    }

    public void fireBackCannons(){
        if ((System.currentTimeMillis() - reloadTimerForward)/1000 >= reloadTime * cannonsForward) {

            CannonShot cannonShot;
            CannonSmoke cannonSmoke;
            Vector2 cannonAndSmokePos = new Vector2();

            if (cannonsForward == 1){
                cannonAndSmokePos.x = getCenterX();
                cannonAndSmokePos.y = getCenterY();
                cannonAndSmokePos = Utils.rotatePoint(getCenterX(), getCenterY(), DEGREES_180 - rotation, cannonAndSmokePos);
                cannonShot = new CannonShot(cannonAndSmokePos.x, cannonAndSmokePos.y, rotation + DEGREES_180, loadedShot);
                cannonSmoke = new CannonSmoke(smoke, SMOKE_CLOUD_TIME_TO_FADE, SMOKE_CLOUD_ANIMATION_DELAY, cannonAndSmokePos.x, cannonAndSmokePos.y, rotation + DEGREES_180);
                cannonSmokes.add(cannonSmoke);
                cannonShots.add(cannonShot);
                reloadTimerForward = System.currentTimeMillis();
            }
            else {
                //Used to flip between placing a shot on the left or right of the ship
                boolean flip = true;
                int cannonSeparationLeft = 16;
                int cannonSeparationRight = 0;
                for (int i = 0; i < cannonsForward; i++) {

                    if (flip) {
                        flip = false;
                        cannonAndSmokePos.x = getCenterX() - cannonSeparationLeft;
                        cannonAndSmokePos.y = getCenterY();
                        cannonSeparationLeft += 16;
                    }
                    else {
                        flip = true;
                        cannonAndSmokePos.x = getCenterX() + cannonSeparationRight;
                        cannonAndSmokePos.y = getCenterY();
                        cannonSeparationRight += 16;
                    }
                    cannonAndSmokePos = Utils.rotatePoint(getCenterX(), getCenterY(), DEGREES_180 - rotation, cannonAndSmokePos);
                    cannonShot = new CannonShot(cannonAndSmokePos.x, cannonAndSmokePos.y, rotation + DEGREES_180, loadedShot);
                    cannonSmoke = new CannonSmoke(smoke, SMOKE_CLOUD_TIME_TO_FADE, SMOKE_CLOUD_ANIMATION_DELAY, cannonAndSmokePos.x, cannonAndSmokePos.y, rotation + DEGREES_90);
                    cannonSmokes.add(cannonSmoke);
                    cannonShots.add(cannonShot);
                }
            }
        }
    }


    public void fireLeftCannons(){
        if ((System.currentTimeMillis() - reloadTimerLeft)/1000 >= reloadTime * cannonsLeft) {

            CannonShot cannonShot;
            CannonSmoke cannonSmoke;
            Vector2 cannonAndSmokePos = new Vector2();
            if (cannonsLeft == 1){
                cannonAndSmokePos.x = getCenterX();
                cannonAndSmokePos.y = getCenterY();
                cannonAndSmokePos = Utils.rotatePoint(getCenterX(), getCenterY(), DEGREES_180 - rotation, cannonAndSmokePos);
                cannonShot = new CannonShot(cannonAndSmokePos.x, cannonAndSmokePos.y, rotation + DEGREES_90, loadedShot);
                cannonSmoke = new CannonSmoke(smoke, SMOKE_CLOUD_TIME_TO_FADE, SMOKE_CLOUD_ANIMATION_DELAY, cannonAndSmokePos.x, cannonAndSmokePos.y, rotation + DEGREES_90);
                cannonSmokes.add(cannonSmoke);
                cannonShots.add(cannonShot);
            }
            else {
                //Used to flip between placing a shot on the left or right of the ship
                boolean flip = true;
                int cannonSeparationBackward = 16;
                int cannonSeparationForward = 0;
                for (int i = 0; i < cannonsLeft; i++) {

                    if (flip) {
                        flip = false;
                        cannonAndSmokePos.x = getCenterX();
                        cannonAndSmokePos.y = getCenterY() - cannonSeparationBackward;
                        cannonSeparationBackward += 16;
                    } else {
                        flip = true;
                        cannonAndSmokePos.x = getCenterX();
                        cannonAndSmokePos.y = getCenterY() + cannonSeparationForward;
                        cannonSeparationForward += 16;
                    }
                    cannonAndSmokePos = Utils.rotatePoint(getCenterX(), getCenterY(), DEGREES_180 - rotation, cannonAndSmokePos);
                    cannonShot = new CannonShot(cannonAndSmokePos.x, cannonAndSmokePos.y, rotation + DEGREES_90, loadedShot);
                    cannonSmoke = new CannonSmoke(smoke, SMOKE_CLOUD_TIME_TO_FADE, SMOKE_CLOUD_ANIMATION_DELAY, cannonAndSmokePos.x, cannonAndSmokePos.y, rotation + DEGREES_90);
                    cannonSmokes.add(cannonSmoke);
                    cannonShots.add(cannonShot);
                }
            }
            reloadTimerLeft = System.currentTimeMillis();
        }
    }


    public void fireRightCannons(){
        if ((System.currentTimeMillis() - reloadTimerRight)/1000 >= reloadTime * cannonsRight) {

            CannonShot cannonShot;
            CannonSmoke cannonSmoke;
            Vector2 cannonAndSmokePos = new Vector2();
            if (cannonsRight == 1){
                cannonAndSmokePos.x = getCenterX();
                cannonAndSmokePos.y = getCenterY();
                cannonAndSmokePos = Utils.rotatePoint(getCenterX(), getCenterY(), DEGREES_180 - rotation, cannonAndSmokePos);
                cannonShot = new CannonShot(cannonAndSmokePos.x, cannonAndSmokePos.y, rotation - DEGREES_90, loadedShot);
                cannonSmoke = new CannonSmoke(smoke, SMOKE_CLOUD_TIME_TO_FADE, SMOKE_CLOUD_ANIMATION_DELAY, cannonAndSmokePos.x, cannonAndSmokePos.y, rotation - DEGREES_90);
                cannonSmokes.add(cannonSmoke);
                cannonShots.add(cannonShot);
            }
            else {
                //Used to flip between placing a shot on the left or right of the ship
                boolean flip = true;
                int cannonSeparationBackward = 16;
                int cannonSeparationForward = 0;
                for (int i = 0; i < cannonsRight; i++) {

                    if (flip) {
                        flip = false;
                        cannonAndSmokePos.x = getCenterX();
                        cannonAndSmokePos.y = getCenterY() - cannonSeparationBackward;
                        cannonSeparationBackward += 16;
                    } else {
                        flip = true;
                        cannonAndSmokePos.x = getCenterX();
                        cannonAndSmokePos.y = getCenterY() + cannonSeparationForward;
                        cannonSeparationForward += 16;
                    }
                    cannonAndSmokePos = Utils.rotatePoint(getCenterX(), getCenterY(), DEGREES_180 - rotation, cannonAndSmokePos);
                    cannonShot = new CannonShot(cannonAndSmokePos.x, cannonAndSmokePos.y, rotation - DEGREES_90, loadedShot);
                    cannonSmoke = new CannonSmoke(smoke, SMOKE_CLOUD_TIME_TO_FADE, SMOKE_CLOUD_ANIMATION_DELAY, cannonAndSmokePos.x, cannonAndSmokePos.y, rotation - DEGREES_90);
                    cannonSmokes.add(cannonSmoke);
                    cannonShots.add(cannonShot);
                }
            }

            reloadTimerRight = System.currentTimeMillis();
        }
    }

    /**
     * Increments the sail state.
     */
    public void incrementSailState(){
        if (SailState.NONE == sailState)
            sailState = SailState.HALF;
        else if (SailState.HALF == sailState)
            sailState = SailState.FULL;
    }

    /**
     * Decrements the sail state.
     */
    public void decrementSailState(){
        if (SailState.FULL == sailState)
            sailState = SailState.HALF;
        else if (SailState.HALF == sailState)
            sailState = SailState.NONE;
    }

    protected void calculateAimingLines(){

        float lengthX_Horizontal = (float) (cannonRange*Math.cos(Math.toRadians(rotation)));
        float lengthY_Horizontal = (float) (cannonRange*Math.sin(Math.toRadians(rotation)));
        float lengthX_Vertical = (float) (cannonRange*Math.cos(Math.toRadians(rotation+DEGREES_90)));
        float lengthY_Vertical = (float) (cannonRange*Math.sin(Math.toRadians(rotation+DEGREES_90)));

        rightAimingLine.set(
                getCenterPosition(),
                getCenterX()+lengthX_Horizontal,
                getCenterY()+lengthY_Horizontal
        );
        leftAimingLine.set(
                getCenterPosition(),
                getCenterX()-lengthX_Horizontal,
                getCenterY()-lengthY_Horizontal
        );
        forwardAimingLine.set(
                getCenterPosition(),
                getCenterX()+lengthX_Vertical,
                getCenterY()+lengthY_Vertical
        );
        backwardAimingLine.set(
                getCenterPosition(),
                getCenterX()-lengthX_Vertical,
                getCenterY()-lengthY_Vertical
        );

    }
    /**
     * Sets the direction of the ship.
     * @param dir Parameter must be Direction enum of either LEFT, RIGHT or CENTER.
     */
    public void setDirection(Direction dir){
        this.dir = dir;
    }

    public void setTargets(Array<Ship> targets){this.targets = targets;}

    public void setTarget(Ship target){this.targets.add(target);}

    public void setHull(float hull){this.hull = hull;}

    public void setSail(float sail){this.sails = sail;}

    public Array<CannonShot> getCannonShots(){return cannonShots;}

    @Override
    public Rectangle getHitBox(){return image.getBoundingRectangle();}

    public float getRotation(){return rotation; }

    @Override
    public Sprite getSprite(){return image;}
    /**
     * Gets the position of the ship based on the image of the ship (the lower left corner)
     * @return The position of the ship based on the image of the ship.
     */
    @Override
    public Vector2 getPosition(){return new Vector2(x, y);}

    /**
     * Gives the ship's center position. Calculated based on the ship's image width and height.
     * @return Ship's center
     */
    @Override
    public Vector2 getCenterPosition(){return new Vector2(x+imageWidth/2,y+imageHeight/2);}
    /**
     * Gives a point in front of the ship from a predetermined distance {@code HOW_FAR_FROM_SHIP}
     * 90 Degrees is added to the original computation to make 0 degrees North of the Ship
     * @return The position in front of the ship
     */
    public Vector2 getPositionFrontOfShip(){return new Vector2( (float) (x+(HOW_FAR_FROM_SHIP*Math.cos((Math.toRadians(rotation+90))))),
                                                                (float) (y+(HOW_FAR_FROM_SHIP*Math.sin((Math.toRadians(rotation+90))))));}
    @Override
    public float getX(){return x;}
    @Override
    public float getY(){return y;}

    /**
     * Gives the ship's x based on where the center of the ship's image is located.
     * @return the Ship's x that is in the center of the ship.
     */
    @Override
    public float getCenterX(){return x+imageWidth/2;}

    /**
     * Gives the ship's y based on where the center of the ship's image is located.
     * @return the Ship's y that is in the center of the ship.
     */
    @Override
    public float getCenterY(){return y+imageHeight/2;}

    public float getCannonRange(){return cannonRange;}

    public float getHull(){return hull;}

    public float getSails(){return sails;}

    public boolean isDead(){return dead;}

    /**
     * Disposes all textures that the given ship uses. Should never be called on player directly, as textures should remain between battles.
     */
    @Override
    public void disposeTextures(){
        image.getTexture().dispose();
        smoke.getTexture().dispose();
    }

}
