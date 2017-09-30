package com.artifexiumgames.apirateslife.item;

/**
 * Created by Adam on 5/29/2016.
 */
public class CannonType {

    public enum CannonWeightType {TWELVE_POUNDER, EIGHTEEN_POUNDER, TWENTYFOUR_PONUDER, THIRTYTWO_POUNDER}
    public enum CannonRangeType{SHORT_CANNON, LONG_CANNON}

    private CannonWeightType cannonWeightType;
    private CannonRangeType cannonRangeType;
    private float range;
    private float damage;

    public CannonType(CannonWeightType cannonWeightType, CannonRangeType cannonRangeType){
        this.cannonWeightType = cannonWeightType;
        this.cannonRangeType = cannonRangeType;
        this.range = 0;
        this.damage = 0;

        adjustWeightType(cannonWeightType);
        adjustRangeType(cannonRangeType);
    }
    private void adjustWeightType(CannonWeightType cannonWeightType){
        switch (cannonWeightType){
            case TWELVE_POUNDER:
                range += 150;
                damage += 10;
                break;
            case EIGHTEEN_POUNDER:
                range += 175;
                damage += 15;
                break;

            case TWENTYFOUR_PONUDER:
                range += 200;
                damage += 20;
                break;

            case THIRTYTWO_POUNDER:
                range += 225;
                damage += 25;
                break;
        }
    }
    private void adjustRangeType(CannonRangeType cannonRangeType){
        switch(cannonRangeType){
            case SHORT_CANNON:
                range += range*.25;
                damage += damage*.10;
                break;
            case LONG_CANNON:
                range += range*.50;
                damage -= damage*.20;
        }
    }

    public void setCannonWeightType(CannonWeightType cannonWeightType){
        adjustWeightType(cannonWeightType);
    }

    public void setCannonRangeType(CannonRangeType cannonRangeType){
        adjustRangeType(cannonRangeType);
    }

    public CannonRangeType getCannonRangeType(){return cannonRangeType;}
    public CannonWeightType getCannonWeightType(){return cannonWeightType;}
    public float getRange(){return range;}
    public float getDamage(){return damage;}

}
