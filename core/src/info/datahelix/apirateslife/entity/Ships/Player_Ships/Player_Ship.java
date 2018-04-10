package info.datahelix.apirateslife.entity.Ships.Player_Ships;

import com.badlogic.gdx.graphics.g2d.Sprite;

import info.datahelix.apirateslife.entity.Ships.Ship;
import info.datahelix.apirateslife.item.CannonType;

/**
 * Created by Adam Torres on 4/9/2018.
 */
public abstract class Player_Ship extends Ship {

    public Player_Ship(String name, Sprite sprite, float x, float y, float rotation,
                       int maxCannonsSides, int maxCannonsFront, int maxCannonsBack, CannonType cannonType,
                       int maxCrew, int maxHull, int maxSails, int maxSpeed, int numSinkingClouds) {
        super(name, sprite, x, y, rotation,
                maxCannonsSides, maxCannonsFront, maxCannonsBack, cannonType,
                maxCrew, maxHull, maxSails, maxSpeed, numSinkingClouds);
    }


}
