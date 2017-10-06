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

package info.datahelix.apirateslife.utils;

import com.badlogic.gdx.math.Vector2;


public class Line {

    public float x1, y1, x2, y2;

    public Line(){
        this.x1 = 0f;
        this.y1 = 0f;
        this.x2 = 0f;
        this.y2 = 0f;
    }

    public Line(float x1, float y1, float x2, float y2){
        this.x1 = x1;
        this.y1 = y1;
        this.x2 = x2;
        this.y2 = y2;
    }

    public Line(Vector2 pos1, float x2, float y2){
        this.x1 = pos1.x;
        this.y1 = pos1.y;
        this.x2 = x2;
        this.y2 = y2;
    }

    public void set(float x1, float y1, float x2, float y2){
        this.x1 = x1;
        this.y1 = y1;
        this.x2 = x2;
        this.y2 = y2;
    }

    public void set(Vector2 pos1, float x2, float y2){
        this.x1 = pos1.x;
        this.y1 = pos1.y;
        this.x2 = x2;
        this.y2 = y2;
    }

    public Vector2 getPos1(){
        return new Vector2(x1, y1);
    }

    public Vector2 getPos2(){
        return new Vector2(x2, y2);
    }
}
