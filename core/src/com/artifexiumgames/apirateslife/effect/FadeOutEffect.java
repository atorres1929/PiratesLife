package com.artifexiumgames.apirateslife.effect;

import com.artifexiumgames.apirateslife.entity.Entity;
import com.artifexiumgames.apirateslife.utils.SpriteAccessor;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import aurelienribon.tweenengine.Tween;

/**
 * This class is used to fade out a single entity. The effect is done on the entity itself,
 * and uses the sprite the entity is based.
 * Created by Adam on 11/2/2016.
 * @author Adam
 */
public class FadeOutEffect extends Effect {

    private Sprite sprite;

    /**
     * Fades an entity out, likely after its death. Ensure that implementing classes properly dispose
     * of themselves, as this class does not handle texture disposal (doing so many dispose of the
     * entity's texture before it is due to be disposed of).
     * @param entity The entity that will fade out
     * @param timeToFade The time it will take for the entity to fade out
     * @param delayToFade The time it will take for the animation to actually start
     */
    public FadeOutEffect(Entity entity, int timeToFade, int delayToFade){
        this.sprite = entity.getSprite();
        Tween.set(sprite, SpriteAccessor.ALPHA).target(1).start(tweenManager);
        Tween.to(sprite, SpriteAccessor.ALPHA, timeToFade).target(0).delay(delayToFade).start(tweenManager);
    }


    public FadeOutEffect(Sprite sprite, int timeToFade, int delayToFade, float x, float y){
        this.sprite = sprite;
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

    /**
     * This method is left empty and unused in this child class because disposing of the entity's
     * sprite should be handled by the entity class itself.
     */
    @Override
    public void dispose() {

    }
}
