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
 *     along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package info.datahelix.apirateslife.entity.Ships;

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
import info.datahelix.apirateslife.entity.CannonShot;
import info.datahelix.apirateslife.entity.CollideableEntity;
import info.datahelix.apirateslife.entity.Entity;
import info.datahelix.apirateslife.item.CannonType;
import info.datahelix.apirateslife.screens.BattleWorld;
import info.datahelix.apirateslife.utils.Line;
import info.datahelix.apirateslife.utils.Utils;

/**
 * Created 5/24/2016
 *
 * @author Adam Torres
 */
public abstract class Ship extends CollideableEntity implements Entity, Cloneable {

    public enum Direction {LEFT, RIGHT, STRAIGHT}

    protected final float DEGREES_360 = 360;
    protected final float DEGREES_270 = 270;
    protected final float DEGREES_180 = 180;
    protected final float DEGREES_90 = 90;
    protected final float DEGREES_0 = 0;
    protected final Sprite sprite;
    protected final Sprite smoke;

    protected Array<CannonShot> cannonShots;
    protected CannonType cannonType;
    protected CannonShot.CannonShotType loadedShot;
    protected final int MAX_CANNONS_SIDES, MAX_CANNONS_FRONT, MAX_CANNONS_BACK;
    protected final int MAX_CREW;
    protected final int MAX_HULL;
    protected final int MAX_SAILS;
    protected final int MAX_SPEED;
    protected int sailState;
    protected Direction dir;
    protected int cannonsLeft, cannonsRight, cannonsForward, cannonsBack;
    protected long reloadTimerForward, reloadTimerBack, reloadTimerLeft, reloadTimerRight;
    protected float reloadTime;
    protected Line leftAimingLine, rightAimingLine, forwardAimingLine, backwardAimingLine;
    protected float cannonRange;
    protected String name;
    protected int crew;
    protected float hull;
    protected float sails;
    protected float speed;
    protected boolean dead;
    protected boolean disposable;
    protected float x, y, rotation;

    private FadeInFadeOutEffectArea sinkingSmoke;
    private FadeOutEffect sinkingFadingAway;
    private Array<CannonSmoke> cannonSmokes;
    private BitmapFont healthDisplay;
    private final float SINKING_CLOUD_DELAY = 1;
    private final int SINKING_CLOUD_REPEAT = 3;//Must be odd number to end on Fade out
    private final float SINKING_SHIP_TIME_TO_FADE = 3;
    private final float SINKING_SHIP_ANIMATION_DELAY = 5;
    private final float SMOKE_CLOUD_TIME_TO_FADE = .5f;
    private final float SMOKE_CLOUD_ANIMATION_DELAY = .5f;

    private final float mass = 10.0f; //TODO change mass for all ships to be different

    public Ship(String name, Sprite sprite, float x, float y, float rotation,
                int maxCannonsSides, int maxCannonsFront, int maxCannonsBack, CannonType cannonType,
                int maxCrew, int maxHull, int maxSails, int maxSpeed,
                int numSinkingClouds) {

        this.name = name;
        this.sprite = sprite;
        this.x = x;
        this.y = y;
        this.rotation = rotation;
        cannonsLeft = cannonsRight = MAX_CANNONS_SIDES = maxCannonsSides;
        cannonsForward = MAX_CANNONS_FRONT = maxCannonsFront;
        cannonsBack = MAX_CANNONS_BACK = maxCannonsBack;
        crew = MAX_CREW = maxCrew;
        hull = MAX_HULL = maxHull;
        sails = MAX_SAILS = maxSails;
        speed = MAX_SPEED = maxSpeed;
        dead = false;
        sailState = 0;
        this.cannonType = cannonType;
        this.loadedShot = CannonShot.CannonShotType.ROUNDSHOT;
        dir = Direction.STRAIGHT;
        smoke = new Sprite(new Texture("effects/shots/smoke.png"));
        healthDisplay = new BitmapFont(Gdx.files.internal("fonts/corsiva_title_black.fnt"));
        sinkingSmoke = new FadeInFadeOutEffectArea(this, smoke, SINKING_CLOUD_DELAY, this.getSprite().getWidth(), numSinkingClouds, SINKING_CLOUD_REPEAT);
        sinkingFadingAway = new FadeOutEffect(this, SINKING_SHIP_TIME_TO_FADE, SINKING_SHIP_ANIMATION_DELAY);
        cannonSmokes = new Array<CannonSmoke>();
        this.sprite.getTexture().setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        this.sprite.setOriginCenter();
        this.sprite.setPosition(x, y);
        this.sprite.setRotation(rotation);
        cannonRange = cannonType.getRange() + cannonType.getRange() * loadedShot.getRangePercentage();
        cannonShots = new Array<CannonShot>();
        reloadTime = 0.5f;
        rightAimingLine = new Line();
        leftAimingLine = new Line();
        forwardAimingLine = new Line();
        backwardAimingLine = new Line();
    }

    /**
     * Draws the ship at the appropriate location and appropriate rotation.
     *
     * @param batch the batch that the ship is drawn from.
     */
    @Override
    public void draw(SpriteBatch batch) {
        for (CannonShot cannonShot : cannonShots) {
            cannonShot.draw(batch);
        }


        for (CannonSmoke smoke : cannonSmokes) {
            smoke.animate(batch, Utils.DELTA);
            if (smoke.done()) {
                cannonSmokes.removeValue(smoke, false);
            }
        }

        healthDisplay.draw(batch, "" + hull, x, y);

        //draw ship last
        sprite.setPosition(x, y);
        sprite.setRotation(rotation);
        sprite.draw(batch);

        if (hull <= 0) {
            this.dead = true;
            sinkingFadingAway.animate(batch, Utils.DELTA);
            sinkingSmoke.animate(batch, Utils.DELTA);
            if (sinkingSmoke.done()) {
                disposable = true;
            }
        }
    }

    public void drawDebugLines(ShapeRenderer shapeRenderer) {
        shapeRenderer.setColor(Color.RED);
        Rectangle rect = getHitBox();
        shapeRenderer.rect(rect.x, rect.y, rect.width, rect.height);
        shapeRenderer.line(rightAimingLine.getPos1(), rightAimingLine.getPos2());
        shapeRenderer.line(leftAimingLine.getPos1(), leftAimingLine.getPos2());
        shapeRenderer.line(forwardAimingLine.getPos1(), forwardAimingLine.getPos2());
        shapeRenderer.line(backwardAimingLine.getPos1(), backwardAimingLine.getPos2());
    }

    public void moveShots() {
        for (CannonShot cannonShot : cannonShots) {
            if (cannonShot.getRange() > cannonRange) {
                cannonShot.disposeTextures();
                cannonShots.removeValue(cannonShot, false);
            } else {
                for (CollideableEntity entity : collideables) {
                    if (entity.checkCollision(cannonShot)) {
                        if (entity instanceof Ship) {
                            cannonShot.hit(entity, cannonType.getDamage(), cannonShot.getHullDamage(), cannonShot.getSailDamage());
                        }
                        cannonShot.disposeTextures();
                        cannonShots.removeValue(cannonShot, false);
                    }
                }
            }
            cannonShot.move();
        }
        for (CannonSmoke smoke : cannonSmokes) {
            smoke.move();
        }
    }

    //Should be used for when this entity hits another, e.g. Ship hits another Ship, rocks, etc.
    @Override
    public void hit(CollideableEntity entity, float... damage) {
        //TODO attach this to collision code

        //Get the mtd
        Vector2 delta = this.getCenterPosition().sub(entity.getCenterPosition());
        float d = delta.len();
        //minimum translation distance to push the ships apart after intersecting
        Vector2 minimumTranslationDistance = delta.scl(((this.getSprite().getHeight() / 2 + entity.getSprite().getHeight() / 2) - d) / d);

        //resolve intersection
        //inverse mass quantities
    }

    @Override
    public boolean checkCollision(Entity entity) {
        return this.getHitBox().overlaps(entity.getHitBox());
    }


    @Override
    public void move() {

        calculateAimingLines();
        ensureWithinBorders();

        for (CollideableEntity entity : collideables) {
            if (checkCollision(entity)) {
                //TODO do stuff related to collision
//                hit(entity, );
            }
        }

        if (rotation % DEGREES_360 == 0) {
            rotation = 0;
        }
        if (!dead) {
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
            if (speed < sailState) {
                speed += 0.01f;
            } else {
                speed -= 0.01f;
            }
        } else {
            if (speed > 0) {
                speed -= 0.5f;
            }
        }

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

    }

    /**
     * Ensures the ship is still in the borders of the map, and if not, places is back in the borders with it's rotation reversed.
     */
    private void ensureWithinBorders() {
        if (x > BattleWorld.WORLD_SIZE - sprite.getWidth()) {
            rotation += DEGREES_180;
            x -= 10;
        } else if (x < 0 + sprite.getWidth()) {
            rotation += DEGREES_180;
            x += 10;
        } else if (y > BattleWorld.WORLD_SIZE - sprite.getHeight()) {
            rotation += DEGREES_180;
            y -= 10;
        } else if (y < 0 + sprite.getHeight()) {
            rotation += DEGREES_180;
            y += 10;
        }
    }

    /**
     * Increases the rotation of the ship by 1 degree. Based on a circle of 360 degrees where 0 degrees is North of the ship.
     */
    private void moveLeft() {
        rotation++;
    }

    /**
     * Reduces the rotation of the ship by 1 degree. Based on a circle of 360 degrees where 0 degrees is North of the ship.
     */
    private void moveRight() {
        rotation--;
    }

    public void fireForwardCannons() {
        if ((System.currentTimeMillis() - reloadTimerForward) / 1000 >= reloadTime * cannonsForward) {

            CannonShot cannonShot;
            CannonSmoke cannonSmoke;
            Vector2 cannonAndSmokePos = new Vector2();

            if (cannonsForward == 1) {
                cannonAndSmokePos.x = getCenterX();
                cannonAndSmokePos.y = getCenterY();
                cannonAndSmokePos = Utils.rotatePoint(getCenterX(), getCenterY(), DEGREES_180 - rotation, cannonAndSmokePos);
                cannonShot = new CannonShot(cannonAndSmokePos.x, cannonAndSmokePos.y, rotation, loadedShot);
                cannonSmoke = new CannonSmoke(smoke, SMOKE_CLOUD_TIME_TO_FADE, SMOKE_CLOUD_ANIMATION_DELAY, cannonAndSmokePos.x, cannonAndSmokePos.y, rotation);
                cannonSmokes.add(cannonSmoke);
                cannonShots.add(cannonShot);
            } else {
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
                    } else {
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

    public void fireBackCannons() {
        if ((System.currentTimeMillis() - reloadTimerBack) / 1000 >= reloadTime * cannonsBack) {

            CannonShot cannonShot;
            CannonSmoke cannonSmoke;
            Vector2 cannonAndSmokePos = new Vector2();

            if (cannonsBack == 1) {
                cannonAndSmokePos.x = getCenterX();
                cannonAndSmokePos.y = getCenterY();
                cannonAndSmokePos = Utils.rotatePoint(getCenterX(), getCenterY(), DEGREES_180 - rotation, cannonAndSmokePos);
                cannonShot = new CannonShot(cannonAndSmokePos.x, cannonAndSmokePos.y, rotation + DEGREES_180, loadedShot);
                cannonSmoke = new CannonSmoke(smoke, SMOKE_CLOUD_TIME_TO_FADE, SMOKE_CLOUD_ANIMATION_DELAY, cannonAndSmokePos.x, cannonAndSmokePos.y, rotation + DEGREES_180);
                cannonSmokes.add(cannonSmoke);
                cannonShots.add(cannonShot);
            } else {
                //Used to flip between placing a shot on the left or right of the ship
                boolean flip = true;
                int cannonSeparationLeft = 16;
                int cannonSeparationRight = 0;
                for (int i = 0; i < cannonsBack; i++) {

                    if (flip) {
                        flip = false;
                        cannonAndSmokePos.x = getCenterX() - cannonSeparationLeft;
                        cannonAndSmokePos.y = getCenterY();
                        cannonSeparationLeft += 16;
                    } else {
                        flip = true;
                        cannonAndSmokePos.x = getCenterX() + cannonSeparationRight;
                        cannonAndSmokePos.y = getCenterY();
                        cannonSeparationRight += 16;
                    }
                    cannonAndSmokePos = Utils.rotatePoint(getCenterX(), getCenterY(), DEGREES_180 - rotation, cannonAndSmokePos);
                    cannonShot = new CannonShot(cannonAndSmokePos.x, cannonAndSmokePos.y, rotation + DEGREES_180, loadedShot);
                    cannonSmoke = new CannonSmoke(smoke, SMOKE_CLOUD_TIME_TO_FADE, SMOKE_CLOUD_ANIMATION_DELAY, cannonAndSmokePos.x, cannonAndSmokePos.y, rotation + DEGREES_180);
                    cannonSmokes.add(cannonSmoke);
                    cannonShots.add(cannonShot);
                }
            }
            reloadTimerBack = System.currentTimeMillis();
        }
    }


    public void fireLeftCannons() {
        if ((System.currentTimeMillis() - reloadTimerLeft) / 1000 >= reloadTime * cannonsLeft) {

            CannonShot cannonShot;
            CannonSmoke cannonSmoke;
            Vector2 cannonAndSmokePos = new Vector2();
            if (cannonsLeft == 1) {
                cannonAndSmokePos.x = getCenterX();
                cannonAndSmokePos.y = getCenterY();
                cannonAndSmokePos = Utils.rotatePoint(getCenterX(), getCenterY(), DEGREES_180 - rotation, cannonAndSmokePos);
                cannonShot = new CannonShot(cannonAndSmokePos.x, cannonAndSmokePos.y, rotation + DEGREES_90, loadedShot);
                cannonSmoke = new CannonSmoke(smoke, SMOKE_CLOUD_TIME_TO_FADE, SMOKE_CLOUD_ANIMATION_DELAY, cannonAndSmokePos.x, cannonAndSmokePos.y, rotation + DEGREES_90);
                cannonSmokes.add(cannonSmoke);
                cannonShots.add(cannonShot);
            } else {
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


    public void fireRightCannons() {
        if ((System.currentTimeMillis() - reloadTimerRight) / 1000 >= reloadTime * cannonsRight) {

            CannonShot cannonShot;
            CannonSmoke cannonSmoke;
            Vector2 cannonAndSmokePos = new Vector2();
            if (cannonsRight == 1) {
                cannonAndSmokePos.x = getCenterX();
                cannonAndSmokePos.y = getCenterY();
                cannonAndSmokePos = Utils.rotatePoint(getCenterX(), getCenterY(), DEGREES_180 - rotation, cannonAndSmokePos);
                cannonShot = new CannonShot(cannonAndSmokePos.x, cannonAndSmokePos.y, rotation - DEGREES_90, loadedShot);
                cannonSmoke = new CannonSmoke(smoke, SMOKE_CLOUD_TIME_TO_FADE, SMOKE_CLOUD_ANIMATION_DELAY, cannonAndSmokePos.x, cannonAndSmokePos.y, rotation - DEGREES_90);
                cannonSmokes.add(cannonSmoke);
                cannonShots.add(cannonShot);
            } else {
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
     * Set speed to highest possible value
     * Essentially using all sails on the ship
     */
    public void setSpeedMax() {
        sailState = MAX_SPEED;
    }

    /**
     * Set speed to lowest possible value
     * Essentially putting away all sails on the ship
     */
    public void setSpeedMin() {
        sailState = 0;
    }

    /**
     * Increments the sail state.
     */
    public void speedUp() {
        if (sailState < MAX_SPEED) {
            sailState++;
        }
    }

    /**
     * Decrements the sail state.
     */
    public void speedDown() {
        if (sailState > 0) {
            sailState--;
        }
    }

    protected void calculateAimingLines() {

        float lengthX_Horizontal = (float) (cannonRange * Math.cos(Math.toRadians(rotation)));
        float lengthY_Horizontal = (float) (cannonRange * Math.sin(Math.toRadians(rotation)));
        float lengthX_Vertical = (float) (cannonRange * Math.cos(Math.toRadians(rotation + DEGREES_90)));
        float lengthY_Vertical = (float) (cannonRange * Math.sin(Math.toRadians(rotation + DEGREES_90)));

        rightAimingLine.set(
                getCenterPosition(),
                getCenterX() + lengthX_Horizontal,
                getCenterY() + lengthY_Horizontal
        );
        leftAimingLine.set(
                getCenterPosition(),
                getCenterX() - lengthX_Horizontal,
                getCenterY() - lengthY_Horizontal
        );
        forwardAimingLine.set(
                getCenterPosition(),
                getCenterX() + lengthX_Vertical,
                getCenterY() + lengthY_Vertical
        );
        backwardAimingLine.set(
                getCenterPosition(),
                getCenterX() - lengthX_Vertical,
                getCenterY() - lengthY_Vertical
        );

    }

    /**
     * Sets the direction of the ship.
     *
     * @param dir Parameter must be Direction enum of either LEFT, RIGHT or CENTER.
     */
    public void setDirection(Direction dir) {
        this.dir = dir;
    }

    public void damageHull(float damage) {
        this.hull -= damage;
    }

    public void damageSail(float damage) {
        this.sails -= damage;
    }

    public void damageCrew(float damage) {
        this.crew -= damage;
    }

    public void setHull(float hull) {
        this.hull = hull;
    }

    public void setSail(float sail) {
        this.sails = sail;
    }

    public Array<CannonShot> getCannonShots() {
        return cannonShots;
    }

    @Override
    public Rectangle getHitBox() {
        return sprite.getBoundingRectangle();
    }

    public float getRotation() {
        return rotation;
    }

    @Override
    public Sprite getSprite() {
        return sprite;
    }

    /**
     * Gets the position of the ship based on the image of the ship (the lower left corner)
     *
     * @return The position of the ship based on the image of the ship.
     */
    @Override
    public Vector2 getPosition() {
        return new Vector2(x, y);
    }

    /**
     * Gives the ship's center position. Calculated based on the ship's image width and height.
     *
     * @return Ship's center
     */
    @Override
    public Vector2 getCenterPosition() {
        return new Vector2(x + sprite.getWidth() / 2, y + sprite.getHeight() / 2);
    }

    /**
     * Gives a point in front of the ship from a predetermined distance {@code HOW_FAR_FROM_SHIP}
     * 90 Degrees is added to the original computation to make 0 degrees North of the Ship
     *
     * @return The position in front of the ship
     */
    public Vector2 getPositionFrontOfShip() {
        final int HOW_FAR_FROM_SHIP = 100;
        return new Vector2((float) (x + (HOW_FAR_FROM_SHIP * Math.cos((Math.toRadians(rotation + 90))))),
                (float) (y + (HOW_FAR_FROM_SHIP * Math.sin((Math.toRadians(rotation + 90)))))
        );
    }

    @Override
    public float getX() {
        return x;
    }

    @Override
    public float getY() {
        return y;
    }

    /**
     * Gives the ship's x based on where the center of the ship's image is located.
     *
     * @return the Ship's x that is in the center of the ship.
     */
    @Override
    public float getCenterX() {
        return x + sprite.getWidth() / 2;
    }

    /**
     * Gives the ship's y based on where the center of the ship's image is located.
     *
     * @return the Ship's y that is in the center of the ship.
     */
    @Override
    public float getCenterY() {
        return y + sprite.getHeight() / 2;
    }

    public float getCannonRange() {
        return cannonRange;
    }

    public float getHull() {
        return hull;
    }

    public float getSails() {
        return sails;
    }

    public boolean isDead() {
        return dead;
    }

    public boolean isDisposable() {
        return disposable;
    }

    /**
     * Disposes all textures that the given ship uses. Should never be called on player directly, as textures should remain between battles.
     */
    @Override
    public void disposeTextures() {
        sprite.getTexture().dispose();
        smoke.getTexture().dispose();
    }

}
