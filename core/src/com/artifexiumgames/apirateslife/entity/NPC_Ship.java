package com.artifexiumgames.apirateslife.entity;

import com.artifexiumgames.apirateslife.item.CannonType;
import com.artifexiumgames.apirateslife.utils.Utils;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

/**
 * Created by Adam on 5/19/2016.
 */
public class NPC_Ship extends Ship {

    public enum AI_AGGRESSION_STATE {
        PASSIVE, CHASE, BROADSIDE, RUNNING;

        private final Color BROADSIDE_COLOR = Color.RED;
        private final Color CHASE_COLOR = Color.YELLOW;
        private final Color RUNNING_COLOR = Color.GREEN;
        private final Color PASSIVE_COLOR = Color.WHITE;

        public Color getColor() {
            switch (this) {
                case PASSIVE:
                    return PASSIVE_COLOR;
                case CHASE:
                    return CHASE_COLOR;
                case BROADSIDE:
                    return BROADSIDE_COLOR;
                case RUNNING:
                    return RUNNING_COLOR;
                default:
                    return null;
            }
        }
    }

    protected Ship target;
    protected float range;
    private float angleToTarget;
    private AI_AGGRESSION_STATE aggro;
    private Color aggro_Color;

    public NPC_Ship(String name, ShipType shipType, CannonType cannonType, CannonShot.CannonShotType cannonShotType, float x, float y, float rotation, Ship target, AI_AGGRESSION_STATE aggro) {
        super(name, shipType, cannonType, cannonShotType, x, y, rotation);
        this.target = target;
        this.aggro = aggro;
        aggro_Color = aggro.getColor();
        range = Utils.distance(this.x, this.y, target.x, target.y);
    }

    public Color getAggro_Color() {
        return aggro_Color;
    }

    @Override
    public void move() {
        super.move();
        range = Utils.distance(this.x, this.y, target.x, target.y);

        if (target == null) {
            this.sailState = SailState.NONE;
            this.aggro = AI_AGGRESSION_STATE.PASSIVE;
        }
        if (hull == MAX_HULL * .10)
            aggro = AI_AGGRESSION_STATE.RUNNING;

        switch (aggro) {

            case BROADSIDE:
            case CHASE:
                attackAI();
                break;

            case RUNNING:
                runningAI();
                break;

            case PASSIVE:
                passiveAI();
                break;
        }


    }

    public void drawAILine(ShapeRenderer shapeRenderer) {
        shapeRenderer.line(this.getCenterPosition(), target.getCenterPosition());
        if (debug == true) {
            shapeRenderer.setColor(Color.RED);
            Rectangle rect = getHitBox();
            shapeRenderer.rect(rect.x, rect.y, rect.width, rect.height);
            shapeRenderer.line(rightAimingLine.getPos1(), rightAimingLine.getPos2());
            shapeRenderer.line(leftAimingLine.getPos1(), leftAimingLine.getPos2());
            shapeRenderer.line(forwardAimingLine.getPos1(), forwardAimingLine.getPos2());
            shapeRenderer.line(backwardAimingLine.getPos1(), backwardAimingLine.getPos2());
        }
    }

    private void calculateAngleToTarget() {
        Vector2 A = target.getPosition();
        Vector2 B = this.getPosition();
        Vector2 C = this.getPositionFrontOfShip();
        float a = Utils.distance(C, B);
        float b = Utils.distance(C, A);
        float c = Utils.distance(A, B);

        angleToTarget = (float) Math.toDegrees(Math.acos(((a * a + c * c - b * b) / (2 * a * c))));
    }

    private void broadSideAI() {
        aggro_Color = aggro.getColor();
        if (angleToTarget < DEGREES_90) {
            dir = Direction.RIGHT;
        } else if (angleToTarget >= DEGREES_90) {
            dir = Direction.LEFT;
        }
        if (angleToTarget > DEGREES_90 - 5 || angleToTarget < DEGREES_90 + 5) {
            if (Utils.lineIntersectsRectangle(leftAimingLine, target.getHitBox())){
                fireLeftCannons();
            }
            else if (Utils.lineIntersectsRectangle(rightAimingLine, target.getHitBox())){
                fireRightCannons();
            }
            else if (Utils.lineIntersectsRectangle(forwardAimingLine, target.getHitBox())){
                fireForwardCannons();
            }
            else if (Utils.lineIntersectsRectangle(backwardAimingLine, target.getHitBox())){
                fireBackCannons();
            }
        }

    }

    private void chaseAI() {
        aggro_Color = aggro.getColor();
        if (angleToTarget < 10)
            dir = Direction.RIGHT;
        else
            dir = Direction.LEFT;
    }

    private void attackAI() {
        calculateAngleToTarget();

        if (range < cannonRange*7/8)
            aggro = AI_AGGRESSION_STATE.BROADSIDE;
        else if (range >= cannonRange)
            aggro = AI_AGGRESSION_STATE.CHASE;

        switch (aggro) {

            case BROADSIDE:
                broadSideAI();
                break;
            case CHASE:
                chaseAI();
                break;
        }
    }

    private void runningAI() {
        aggro_Color = aggro.getColor();
        if (angleToTarget < DEGREES_180-10)
            dir = Direction.RIGHT;
        else
            dir = Direction.LEFT;
    }

    private void passiveAI() {
        aggro_Color = aggro.getColor();

        dir = Direction.STRAIGHT;
    }

}
