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

package info.datahelix.apirateslife.entity;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;

/**
 * Created 5/26/2016
 * @author Adam Torres
 */
public class Player extends Person {

    private info.datahelix.apirateslife.entity.Ships.Ship ship;
    public Player(String name, info.datahelix.apirateslife.entity.Ships.Ship ship){
        super(name, 100, new Sprite(new Texture("people/player.png")));
        this.ship = ship;
    }

    public info.datahelix.apirateslife.entity.Ships.Ship getShip(){return ship;}


}
