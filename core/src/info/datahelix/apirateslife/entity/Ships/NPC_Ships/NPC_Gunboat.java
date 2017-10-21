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

package info.datahelix.apirateslife.entity.Ships.NPC_Ships;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;

import info.datahelix.apirateslife.entity.Ships.NPC_Ships.NPC_Ship;
import info.datahelix.apirateslife.entity.Ships.Ship;
import info.datahelix.apirateslife.item.CannonType;

/**
 * Created by Adam Torres on 10/19/2017.
 */

public class NPC_Gunboat extends NPC_Ship {

    public NPC_Gunboat(String name, float x, float y, float rotation, Ship target, AI_AGGRESSION_STATE aggro) {
        super(name, new Sprite(new Texture("ships/gunboat.png")),
                x, y, rotation,
                1, 0, 0,
                new CannonType(CannonType.CannonWeightType.TWELVE_POUNDER, CannonType.CannonRangeType.SHORT_CANNON),
                10, 100, 70, 5,
                10,
                target, aggro);
    }
}
