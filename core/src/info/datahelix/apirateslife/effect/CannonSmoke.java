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

package info.datahelix.apirateslife.effect;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

/**
 * Created by Adam on 10/18/2017.
 */

public class CannonSmoke extends FadeOutEffect {

    private final float speed = 5f;
    private float x, y, rotation, imageRotation;

    public CannonSmoke(Sprite s, float timeToFade, float delayToFade, float x, float y, float rotation) {
        super(s, timeToFade, delayToFade, x, y);
        this.rotation = rotation;
        this.x = x;
        this.y = y;
    }

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
    }

    @Override
    public void animate(SpriteBatch batch, float delta){
        sprite.setPosition(x,y);
        sprite.setRotation(imageRotation);
        super.animate(batch, delta);
    }
}

