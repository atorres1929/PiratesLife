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
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import aurelienribon.tweenengine.Tween;

/**
 * This class is used to fade out a single entity. The effect is done on the entity itself,
 * and uses the sprite the entity is based.
 * Created 11/2/2016
 * @author Adam Torres
 */
public class FadeOutEffect extends Effect {

    protected Sprite sprite;

    /**
     * Fades an entity out, likely after its death. Ensure that implementing classes properly dispose
     * of themselves, as this class does not handle texture disposal (doing so many dispose of the
     * entity's texture before it is due to be disposed of).
     * @param entity The entity that will fade out
     * @param timeToFade The time it will take for the entity to fade out
     * @param delayToFade The time it will take for the animation to actually start
     */
    public FadeOutEffect(Entity entity, float timeToFade, float delayToFade){
        this.sprite = entity.getSprite();
        Tween.set(sprite, SpriteAccessor.ALPHA).target(1).start(tweenManager);
        Tween.to(sprite, SpriteAccessor.ALPHA, timeToFade).target(0).delay(delayToFade).start(tweenManager);
    }


    public FadeOutEffect(Sprite s, float timeToFade, float delayToFade, float x, float y){
        sprite = new Sprite(s.getTexture());
        sprite.setPosition(x, y);
        Tween.set(sprite, SpriteAccessor.ALPHA).target(1).start(tweenManager);
        Tween.to(sprite, SpriteAccessor.ALPHA, timeToFade).target(0).delay(delayToFade).start(tweenManager);
    }

    /**
     * This animates the effect by simply drawing the sprite to the batch as the alpha f
     * @param batch the SpriteBatch the animation will be drawn to
     * @param delta the time between the last frame and the current frame
     */
    @Override
    public void animate(SpriteBatch batch, float delta) {
        super.updateTweenManager(delta);
        sprite.draw(batch);

    }

    @Override
    public boolean done() {
        return super.done();
    }

    public Sprite getSprite(){return sprite;}

}
