package com.artifexiumgames.apirateslife.utils;

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
