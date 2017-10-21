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

package info.datahelix.apirateslife.effect;

import info.datahelix.apirateslife.entity.Entity;
import info.datahelix.apirateslife.utils.SpriteAccessor;
import info.datahelix.apirateslife.utils.Utils;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

import java.util.Random;

import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenManager;

/**
 * This class is for creating an area effect of many sprites fading in and fading out around an entity.
 * Created 5/27/2016
 * @author Adam Torres
 */
public class FadeInFadeOutEffectArea extends Effect {

    private final int DISTANCE_FROM_ENTITY;
    private final int NUMBER_OF_EFFECTS;
    private final float ALPHA_BEFORE_REPOSITION = 0.1f;
    private Entity focusedEntity;
    private Sprite[] spriteArray;
    private Random random;

    /**
     * Creates an effect of MANY sprites around a SINGLE entity that fade in then fade out using {@link TweenManager} and {@link Tween}. The
     * TweenManager simply modifies the alpha value of the sprite using {@link SpriteAccessor}.
     * This effect is based off an Entity and will display effects around the Entity in a circle given
     * radius {@code distanceFromEntity}. The number of effects displayed around the entity is limited
     * only by the hardware of the machine the code is running on.
     * @param entity The entity the effects will be displayed around
     * @param sprite The sprite from which the effect is drawn
     * @param fadeDelay The amount of time in seconds the fade should take place in
     * @param distanceFromEntity The maximum distance from the entity the effects can be drawn at
     * @param numberOfEffects The number of effects to be drawn around the entity
     * @see Entity
     * @see Sprite
     * @see Tween
     * @see TweenManager
     */
    public FadeInFadeOutEffectArea(Entity entity, Sprite sprite, float fadeDelay, float distanceFromEntity, int numberOfEffects, int numberOfRepeats){
        DISTANCE_FROM_ENTITY = (int) distanceFromEntity;
        NUMBER_OF_EFFECTS = numberOfEffects;
        this.focusedEntity = entity;
        this.spriteArray = new Sprite[NUMBER_OF_EFFECTS];
        this.sprite = sprite;
        this.random = new Random();
        float delay = 0;
        for (int i = 0; i < spriteArray.length; i++){
            spriteArray[i] = new Sprite(sprite.getTexture());
            spriteArray[i].setPosition(random.nextInt(DISTANCE_FROM_ENTITY)+ focusedEntity.getCenterX()-random.nextInt(DISTANCE_FROM_ENTITY),
                                        random.nextInt(DISTANCE_FROM_ENTITY)+ focusedEntity.getCenterY()-random.nextInt(DISTANCE_FROM_ENTITY));
            Tween.set(spriteArray[i], SpriteAccessor.ALPHA).target(0).start(tweenManager);
            Tween.to(spriteArray[i], SpriteAccessor.ALPHA, fadeDelay).target(1).delay(delay).repeatYoyo(numberOfRepeats, fadeDelay).start(tweenManager);
            delay += 0.1f;
        }
    }

    /**
     * Animates the effect. This is done using TweenManager. The TweenManger changes the alpha value of
     * the given sprite using {@link SpriteAccessor}. This method should be called before it's target
     * entity is drawn to avoid the animations being drawn over the entity.
     * @param batch the SpriteBatch the animation will be drawn to
     * @param delta the time between the last frame and the current frame
     */
    public void animate(SpriteBatch batch, float delta) {
        super.updateTweenManager(delta);
        for (int i = 0; i < spriteArray.length; i++) {

            Vector2 spritePosition = new Vector2(spriteArray[i].getOriginX(), spriteArray[i].getOriginY());
            float distance = Utils.distance(spritePosition, focusedEntity.getCenterPosition());

            if (distance > DISTANCE_FROM_ENTITY && spriteArray[i].getColor().a < ALPHA_BEFORE_REPOSITION) {
                float position1 = random.nextInt(DISTANCE_FROM_ENTITY)+ focusedEntity.getCenterX()-random.nextInt(DISTANCE_FROM_ENTITY);
                float position2 = random.nextInt(DISTANCE_FROM_ENTITY)+ focusedEntity.getCenterY()-random.nextInt(DISTANCE_FROM_ENTITY);
                spriteArray[i].setPosition(position1, position2);
            }

            spriteArray[i].draw(batch);
        }
    }
}
