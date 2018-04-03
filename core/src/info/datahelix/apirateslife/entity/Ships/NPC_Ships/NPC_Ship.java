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

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

import info.datahelix.apirateslife.entity.Ships.Ship;
import info.datahelix.apirateslife.item.CannonType;
import info.datahelix.apirateslife.utils.Utils;

/**
 * Created 5/19/2016
 *
 * @author Adam Torres
 */
public abstract class NPC_Ship extends Ship {

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

    protected Array<Ship> enemies;
    protected Ship target;
    protected float range;
    private float angleToTarget;
    private float projectedAngleToTarget;
    private AI_AGGRESSION_STATE aggro;
    private Color aggro_Color;

    public NPC_Ship(String name, Sprite sprite, float x, float y, float rotation,
                    int maxCannonsSides, int maxCannonsFront, int maxCannonsBack, CannonType cannonType,
                    int maxCrew, int maxHull, int maxSails, int maxSpeed,
                    int numSinkingClouds,
                    AI_AGGRESSION_STATE aggro) {
        super(name, sprite, x, y, rotation, maxCannonsSides, maxCannonsFront, maxCannonsBack, cannonType, maxCrew, maxHull, maxSails, maxSpeed, numSinkingClouds);
        this.aggro = aggro;
        aggro_Color = aggro.getColor();
        enemies = new Array<Ship>();
        setSpeedMax();
    }


    @Override
    public void move() {
        super.move();
        if (target == null && enemies.size > 0) {
            target = enemies.get(0);
        }
        if (enemies.size == 0) {
            aggro = AI_AGGRESSION_STATE.PASSIVE;
        }
        range = Utils.distance(this.getCenterX(), this.getCenterY(), target.getCenterX(), target.getCenterY());
        if (target == null) {
            sailState = 0;
            aggro = AI_AGGRESSION_STATE.PASSIVE;
        }
        if (hull == MAX_HULL * .10)
            aggro = AI_AGGRESSION_STATE.RUNNING;

        switch (aggro) {

            case BROADSIDE:
            case CHASE:
                attackAI();
                checkFireCannons();
                break;

            case RUNNING:
                runningAI();
                checkFireCannons();
                break;

            case PASSIVE:
                passiveAI();
                break;
        }
        aggro_Color = aggro.getColor();
    }


    /**
     * Used only to calculate the proper direction in the next move.
     * Necessary to use the super class's unmodified move method.
     */
    private void moveProjection() {
        super.move();
    }

    public void drawAILine(ShapeRenderer shapeRenderer) {
        shapeRenderer.setColor(aggro_Color);
        shapeRenderer.line(this.getCenterPosition(), target.getCenterPosition());
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

    private void calculateNextMove() {

        try {
            NPC_Ship ship = (NPC_Ship) this.clone();
            ship.moveProjection();
            Vector2 A = target.getPosition();
            Vector2 B = ship.getPosition();
            Vector2 C = ship.getPositionFrontOfShip();
            float a = Utils.distance(C, B);
            float b = Utils.distance(C, A);
            float c = Utils.distance(A, B);

            projectedAngleToTarget = (float) Math.toDegrees(Math.acos(((a * a + c * c - b * b) / (2 * a * c))));
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }

    }

    /**
     * Used in the RUNNING AI, to see which path better leads to running away effectively from the target
     * @param direction The direction to test
     * @return The angle of the between this ship and the target, based upon moving in the direction
     * provided
     */
    private float calculateNextMoveDirection(Direction direction) {
        try {
            NPC_Ship ship = (NPC_Ship) this.clone();
            ship.setDirection(direction);
            ship.moveProjection();
            Vector2 A = target.getPosition();
            Vector2 B = ship.getPosition();
            Vector2 C = ship.getPositionFrontOfShip();
            float a = Utils.distance(C, B);
            float b = Utils.distance(C, A);
            float c = Utils.distance(A, B);

            float projectedAngleToTarget = (float) Math.toDegrees(Math.acos(((a * a + c * c - b * b) / (2 * a * c))));
            return projectedAngleToTarget;
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
            return -1f;
        }
    }

    private void checkFireCannons() {
        if (Utils.lineIntersectsRectangle(leftAimingLine, target.getHitBox())) {
            fireLeftCannons();
        } else if (Utils.lineIntersectsRectangle(rightAimingLine, target.getHitBox())) {
            fireRightCannons();
        } else if (Utils.lineIntersectsRectangle(backwardAimingLine, target.getHitBox())) {
            fireBackCannons();
        } else if (Utils.lineIntersectsRectangle(forwardAimingLine, target.getHitBox())) {
            fireForwardCannons();
        }
    }

    public void setEnemies(Array<Ship> enemies) {
        for (Ship enemy : enemies) {
            if (!this.enemies.contains(enemy, false)) {
                this.enemies.add(enemy);
            }
        }
    }

    public void addEnemy(Ship ship) {
        if (!this.enemies.contains(ship, false)) {
            this.enemies.add(ship);
        }
    }

    private void broadSideAI() {
        if (angleToTarget < DEGREES_90) {
            dir = Direction.RIGHT;
        } else if (angleToTarget >= DEGREES_90) {
            dir = Direction.LEFT;
        }
    }


    private void chaseAI() {
        if (angleToTarget < 1) {
            dir = Direction.STRAIGHT;
            return;

        }

        if (projectedAngleToTarget > angleToTarget) {
            if (dir == Direction.LEFT) {
                dir = Direction.RIGHT;
            } else {
                dir = Direction.LEFT;
            }
        }
    }

    private void attackAI() {
        calculateAngleToTarget();
        calculateNextMove();
        if (range < cannonRange * 7 / 8)
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
        calculateAngleToTarget();
        if (angleToTarget > 179) {
            dir = Direction.STRAIGHT;
            return;
        }

        if (calculateNextMoveDirection(Direction.RIGHT) < calculateNextMoveDirection(Direction.LEFT))
            dir = Direction.LEFT;
        else
            dir = Direction.RIGHT;

    }

    private void passiveAI() {

        dir = Direction.STRAIGHT;
    }

}
