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

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;

import info.datahelix.apirateslife.entity.CannonShot;
import info.datahelix.apirateslife.entity.Ship;
import info.datahelix.apirateslife.item.CannonType;

/**
 * Created by Adam Torres on 10/19/2017.
 */

public class Gunboat extends Ship {


    public Gunboat(String name, ShipType shipType, CannonType cannonType, CannonShot.CannonShotType cannonShotType, float x, float y, float r) {
        super(name, shipType, cannonType, cannonShotType, x, y, r);
    }
}
