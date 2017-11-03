package utils;

import com.badlogic.gdx.math.Vector2;

import org.junit.Assert;
import org.junit.Test;

import info.datahelix.apirateslife.utils.Utils;

/**
 * Created 10/6/2017
 *
 * @author Adam Torres
 */

public class TestUtils {

    @Test
    public void testPointRotation1(){
        Assert.assertEquals(new Vector2(8, 4), Utils.rotatePoint(4, 4, 90, new Vector2(4,8)));
    }

    @Test
    public void testPointRotation2(){
        Assert.assertEquals(new Vector2(0, 4), Utils.rotatePoint(4, 4, -90, new Vector2(4,8)));
    }

    @Test
    public void testPointRotation3(){
        Assert.assertEquals(new Vector2(4, 0), Utils.rotatePoint(4, 4, 180, new Vector2(4,8)));
    }

    @Test
    public void testPointRotation4(){
        Assert.assertEquals(new Vector2(4, 0), Utils.rotatePoint(4, 4, -180, new Vector2(4,8)));
    }

    @Test
    public void testPointRotation5(){
        Assert.assertEquals(new Vector2(4, 8), Utils.rotatePoint(4, 8, 360, new Vector2(4, 8)));
    }

    @Test
    public void testPointRotation6(){
        Assert.assertEquals(new Vector2(4, 8), Utils.rotatePoint(4, 8, -360, new Vector2(4, 8)));
    }

    @Test
    public void testPointRotation7(){
        Assert.assertEquals(new Vector2(4, 8), Utils.rotatePoint(4, 8, -720, new Vector2(4, 8)));
    }

    @Test
    public void testPointRotation8(){
        Assert.assertEquals(new Vector2((float) Math.sqrt(2), 0), Utils.rotatePoint(0, 0, 45, new Vector2(1, 1)));
    }

}

