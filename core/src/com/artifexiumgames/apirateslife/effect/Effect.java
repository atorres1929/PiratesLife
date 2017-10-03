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

package com.artifexiumgames.apirateslife.effect;

import com.artifexiumgames.apirateslife.utils.SpriteAccessor;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Timer;

import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenManager;

/**
 * @author Adam Torres
 * Created 5/27/2016.
 */



public abstract class Effect {

    protected TweenManager tweenManager;
    protected Sprite sprite;

    /**
     * Sets up a default constructor for the Effect class to more easily mold subclasses to whatever is
     * needed. Constructs a basic effect based on a sprite using {@link TweenManager} to manage the effect.
     * The animate method must be implemented in order to give the effect an animation.
     */
    public Effect(){
        this.tweenManager = new TweenManager();
        Tween.registerAccessor(Sprite.class, new SpriteAccessor());
    }

    /**
     * Updates the TweenManager based on how much time has passed on delta
     * @param delta the time between the last frame and the current frame
     */
    public void updateTweenManager(float delta){
        tweenManager.update(delta);
    }

    /**
     * Animates the Sprite
     * @param batch the SpriteBatch the animation will be drawn to
     * @param delta the time between the last frame and the current frame
     */
    public abstract void animate(SpriteBatch batch, float delta);

    public boolean done(){return tweenManager.getRunningTweensCount() == 0;}

    /**
     * Disposes all disposable attributes of the Effect
     */
    public abstract void dispose();

}
