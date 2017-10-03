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
 * @author Adam Torres
 */
public interface Entity {

    float x = 0;
    float y = 0;

    /**
     * Draw the Entity
     * @param batch The batch the Entity will be drawn to
     */
    void draw(SpriteBatch batch);

    /**
     * Move the entity across the screen
     */
    void move();

    /**
     * Get the sprite that the Entity is drawn from.
     * All entities should have a sprite.
     * @return sprite the Entity is drawn from
     */
    Sprite getSprite();

    /**
     * Get the position of the entity.
     * All entities should have an x and y of type float.
     * The position returned is the lower left hand corner of the entity by default
     * @return the lower left hand corner of the entity in the form of a 2D vector (0,0)
     */
    Vector2 getPosition();

    /**
     * Get the center position of the entity
     * @return the center position of the entity (width/2, height/2)
     * @see Entity#getPosition()
     */
    Vector2 getCenterPosition();

    /**
     * Get the x position of the entity.
     * By default the lower left corner of the entity is (0,0)
     * DOES NOT RETURN THE CENTER X
     * @return the x position of the entity
     * @see Entity#getCenterX()
     */
    float getX();

    /**
     * Get the y position of the entity.
     * By default the lower left corner of the entity is (0,0)
     * DOES NOT RETURN THE CENTER Y
     * @return the y position of the entity
     * @see Entity#getCenterY()
     */
    float getY();

    /**
     * Get the center x of the entity.
     * @return width/2
     * @see Entity#getX()
     */
    float getCenterX();

    /**
     * Get the center y of the entity.
     * @return height/2
     * @see Entity#getY()
     */
    float getCenterY();

    /**
     * Ensure that all resources are freed from the implementing class.
     * Also caries out animations or anything else essential to death.
     * batch can be null if no SpriteBatch is needed for animation
     */
    void disposeTextures();
}
