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

package com.artifexiumgames.apirateslife.utils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.viewport.Viewport;

/**
 * Created 5/19/2016
 * @author Adam Torres
 */
public class Utils {

    public static boolean DEBUG_LINES = false;
    public static boolean DISABLE_SPLASH = true;

    public static BitmapFont corsivaBlack = new BitmapFont(Gdx.files.internal("fonts/corsiva_black.fnt"));
    public static BitmapFont corsivaWhite = new BitmapFont(Gdx.files.internal("fonts/corsiva_white.fnt"));
    public static BitmapFont corsivaTitleWhite = new BitmapFont(Gdx.files.internal("fonts/corsiva_title_white.fnt"));
    public static BitmapFont corsivaTitleBlack = new BitmapFont(Gdx.files.internal("fonts/corsiva_title_black.fnt"));
    public static float DELTA = 0;
    public static int DEFAULT_BUTTON_PAD = 20;

    /**
     * Taken from Rectangle2D in the java.awt.geom package
     * Used in the {@code lineIntersectsRectangle} method and the {@code outcode} method.
     * For more information visit https://docs.oracle.com/javase/7/docs/api/java/awt/geom/Rectangle2D.html
     */
    private static final int OUT_LEFT = 1;
    /**
     * Taken from Rectangle2D in the java.awt.geom package
     * Used in the {@code lineIntersectsRectangle} method and the {@code outcode} method.
     * For more information visit https://docs.oracle.com/javase/7/docs/api/java/awt/geom/Rectangle2D.html
     */
    private static final int OUT_TOP = 2;
    /**
     * Taken from Rectangle2D in the java.awt.geom package
     * Used in the {@code lineIntersectsRectangle} method and the {@code outcode} method.
     * For more information visit https://docs.oracle.com/javase/7/docs/api/java/awt/geom/Rectangle2D.html
     */
    private static final int OUT_RIGHT = 4;
    /**
     * Taken from Rectangle2D in the java.awt.geom package
     * Used in the {@code lineIntersectsRectangle} method and the {@code outcode} method.
     * For more information visit https://docs.oracle.com/javase/7/docs/api/java/awt/geom/Rectangle2D.html
     */
    private static final int OUT_BOTTOM = 8;








    //MATH

    /**
     * Rotate a point p around pivot point (cx,cy)
     * @param cx x of the pivot point
     * @param cy y of the pivot point
     * @param angle the angle which the point must be rotated according to
     * @param p the point to be rotated
     * @return a rotated point around (cx, cy) given point p
     */
    public static Vector2 rotatePoint(float cx, float cy, float angle, Vector2 p){
        //Get the sin and cosine of the angle
        float s = (float) Math.sin(angle);
        float c = (float) Math.cos(angle);
        //translate point back to origin
        p.x -= cx;
        p.y -= cy;
        //rotate point
        float xnew = p.x * c + p.y * s;
        float ynew = p.x * s - p.y * c;
        //translate point back
        p.x = xnew + cx;
        p.y = ynew + cy;
        return p;
    }

    public static float distance(float x1, float y1, float x2, float y2){
        return (float) Math.sqrt((x1-x2)*(x1-x2) + (y1-y2)*(y1-y2));
    }

    public static double distance(double x1, double y1, double x2, double y2){
        return Math.sqrt((x1-x2)*(x1-x2) + (y1-y2)*(y1-y2));
    }

    public static float distance(Vector2 vector1, Vector2 vector2){
        return (float) Math.sqrt((vector1.x-vector2.x)*(vector1.x-vector2.x) + (vector1.y-vector2.y)*(vector1.y-vector2.y));
    }

    public static float angleBetweenVectors(Vector2 vector1, Vector2 vector2){
        return (float) Math.atan2(vector2.x - vector1.x, vector2.y - vector1.y)* (float) (180/Math.PI);
    }

    public static boolean lineIntersectsRectangle(Line line, Rectangle rect){

        double x1 = line.x1;
        double x2 = line.x2;
        double y1 = line.y1;
        double y2 = line.y2;

        int out1, out2;
        if ((out2 = outcode(x2, y2, rect)) == 0) {
            return true;
        }
        while ((out1 = outcode(x1, y1, rect)) != 0) {
            if ((out1 & out2) != 0) {
                return false;
            }
            if ((out1 & (OUT_LEFT | OUT_RIGHT)) != 0) {
                double x = rect.getX();
                if ((out1 & OUT_RIGHT) != 0) {
                    x += rect.getWidth();
                }
                y1 = y1 + (x - x1) * (y2 - y1) / (x2 - x1);
                x1 = x;
            } else {
                double y = rect.getY();
                if ((out1 & OUT_BOTTOM) != 0) {
                    y += rect.getHeight();
                }
                x1 = x1 + (y - y1) * (x2 - x1) / (y2 - y1);
                y1 = y;
            }
        }
        return true;

    }






    //UTILITIES

    public static void refreshVieport(Viewport viewport){
        viewport.update(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
    }





    //PRIVATE METHODS

    /**
     * Taken from Rectangle2D in the java.awt.geom package
     * Used in the {@code lineIntersectsRectangle} method.
     * For more information visit https://docs.oracle.com/javase/7/docs/api/java/awt/geom/Rectangle2D.html
     */
    private static int outcode(double x, double y, Rectangle rect) {
            /*
             * Note on casts to double below.  If the arithmetic of
             * x+w or y+h is done in float, then some bits may be
             * lost if the binary exponents of x/y and w/h are not
             * similar.  By converting to double before the addition
             * we force the addition to be carried out in double to
             * avoid rounding error in the comparison.
             *
             * See bug 4320890 for problems that this inaccuracy causes.
             */
        int out = 0;
        if (rect.width <= 0) {
            out |= OUT_LEFT | OUT_RIGHT;
        } else if (x < rect.x) {
            out |= OUT_LEFT;
        } else if (x > rect.x + (double) rect.width) {
            out |= OUT_RIGHT;
        }
        if (rect.height <= 0) {
            out |= OUT_TOP | OUT_BOTTOM;
        } else if (y < rect.y) {
            out |= OUT_TOP;
        } else if (y > rect.y + (double) rect.height) {
            out |= OUT_BOTTOM;
        }
        return out;
    }
}
