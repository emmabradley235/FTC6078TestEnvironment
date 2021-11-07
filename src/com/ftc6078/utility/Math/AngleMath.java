package com.ftc6078.utility.Math;





public abstract class AngleMath {
    public enum AngleUnit{
        RADIANS,
        DEGREES
    }

    public static double clipAngle(double angle){ // makes a radian input value to be between 0 and 2PI
        return clipAngle(angle, AngleUnit.RADIANS);
    }
    public static double clipAngle(double angle, AngleUnit angleUnit){ // makes a radian input value to be between 0 and 2PI
        double circleValue = 2*Math.PI;
        if( angleUnit.equals(AngleUnit.DEGREES) ) // change modes if not using radians
            circleValue = 360;

        while( angle < 0 ) // for as long as the output is less than 0, add 360 (aka  2PI radians)
            angle += circleValue;

        while( angle >= circleValue ) // and do the opposite to get it below 360
            angle -= circleValue;

        return angle;
    }
    
}
