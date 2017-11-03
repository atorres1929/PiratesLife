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

package info.datahelix.apirateslife.entity;

import com.badlogic.gdx.utils.Array;

/**
 * Created by Adam Torres on 10/24/2017.
 */

public abstract class CollideableEntity implements Entity{

    protected Array<CollideableEntity> collideables;

    /**
     * Checks if this entity's sprite is colliding with some other rectangle (a sprite usually) and
     * performs an action based on the entity's purpose.
     * @param entity the entity to check collision against
     */
    public abstract boolean checkCollision(Entity entity);

    public abstract void hit(CollideableEntity entity, float... damage);

    /**
     * Copies all values in the list to this objects collideables list.
     * If this object exists within the list, it is removed.
     * Necessary for checking of collisions.
     * @param collideables the list of CollideableEntites to be copied
     */
    public void setCollideables(Array<CollideableEntity> collideables) {
        this.collideables = new Array<CollideableEntity>();
        for (int i = 0; i < collideables.size; i++){
            this.collideables.add(collideables.get(i));
        }
        this.collideables.removeValue(this, false);
    }
}
