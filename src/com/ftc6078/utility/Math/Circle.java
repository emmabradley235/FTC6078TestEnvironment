package com.ftc6078.utility.Math;

import com.ftc6078.utility.Wrappers_General.Point2d;


public class Circle {
    Point2d centerPoint;
    double radius;

    public Circle(){
        this.centerPoint = new Point2d();
        this.radius = 1;
    }
    public Circle( double radius ){
        this.centerPoint = new Point2d();
        this.radius = radius;
    }
    public Circle( double radius, Point2d centerPoint ){
        this.centerPoint = centerPoint;
        this.radius = radius;
    }
    public Circle( double radius, Point2d tangentPoint, double slopeAtTangentPoint, boolean tangentIsAboveCenter ){
        this.radius = radius;

        double centerX, centerY;

        if( slopeAtTangentPoint == 0 ){ // if the slope at the tangent point is flat, then easy, just look at if it should be positioned above or below and add/sub the radius to tan point y
            centerX = tangentPoint.x;

            if( tangentIsAboveCenter ) // if the tangent is above the center, need to subtract the radius from y
                centerY = tangentPoint.y - radius;
            else  // else the tangent is below the center, meaning center is above it, so add y
                centerY = tangentPoint.y + radius;
        }
        else { // if having to do it the hard way
            // do trig to find the center point
            // img of math worked out can be found at:  https://bit.ly/2ZSD6Dd

            double tangentLineAngle = Math.atan( slopeAtTangentPoint );
            double radialLineAngle = tangentLineAngle - Math.PI/2;
            double xChange = radius * Math.cos( radialLineAngle );
            double yChange = radius * Math.sin( radialLineAngle );

            if( tangentIsAboveCenter ) { // if the tangent is above of the center, the center is below and right of the tangent, so subtract change in y
                centerX = tangentPoint.x + xChange;
                centerY = tangentPoint.y + yChange;
            }
            else { // else the tangent is below the center, meaning center is above and left of it, so add change in y
                centerY = tangentPoint.y - yChange;
                centerX = tangentPoint.x - xChange;
            }
        }

        this.centerPoint = new Point2d( centerX, centerY );
    }


    public double getY( double x, boolean yIsAboveCenter ){
        // gets the y coordinate of the circle at the given x value, either + or - depending

        // yChange = sqrt( R^2 - (x - centerX)^2 ), vai solving the circle equation for y
        double yChange = Math.sqrt( Math.pow(radius, 2) - Math.pow(x - centerPoint.x, 2) );

        if( yIsAboveCenter ){
            return centerPoint.y + yChange;
        }
        else {
            return centerPoint.y - yChange;
        }
    }

    public double getSlopeAt( double x, boolean yIsAboveCenter ){
        return getSlopeAt( x, getY( x, yIsAboveCenter ) );
    }
    public double getSlopeAt( double x, double y ){
        if( y - centerPoint.y == 0 ) // if is even with the center point, then infinite slope, and div 0 error, so prevent those thigns please
            return 0;

        return (centerPoint.x - x) / (y - centerPoint.y); // return the derivative of the circle
    }

    public double getSlopeOfSlopeAt( double x, boolean yIsAboveCenter ){
        return getSlopeOfSlopeAt( x, getY( x, yIsAboveCenter ) );
    }
    public double getSlopeOfSlopeAt( double x, double y ){
        if( y - centerPoint.y == 0 ) // if is even with the center point, then infinite slope, and div 0 error, so prevent those thigns please
            return 0;

        // return second derivative of a circle, aka:
        // ( f'(x, y)*(xCenter - x) + y - yCenter) / (y - yCenter)^2
        return ( getSlopeAt( x, y ) * (centerPoint.x - x) + y - centerPoint.y ) / Math.pow(y - centerPoint.y, 2); // return the derivative of the circle
    }


    public Point2d findStartOfTangentThatIntersects( Point2d tangentIntersectPoint, boolean tangentIsAboveCenter ){
        double Xc = centerPoint.x; // setup variables for easy and more readable math that is to come
        double Yc = centerPoint.y;
        double Xt = tangentIntersectPoint.x;
        double Yt = tangentIntersectPoint.y;

        double h = Math.sqrt(  Math.pow(Xc - Xt, 2) + Math.pow(Yc - Yt, 2)  );
        double s = Math.sqrt(  Math.pow(h, 2) - Math.pow(radius, 2)  );
        double radiusToTangentLocalAngle = Math.atan( s / radius );


        if( !tangentIsAboveCenter )
            radiusToTangentLocalAngle = -radiusToTangentLocalAngle; // if tangent is below the center point, the angle should be below the center point also
        double centerToTargetAngle = Math.atan( (Yt - Yc)/(Xt - Xc) );

        double centerToTangentAngle = centerToTargetAngle + radiusToTangentLocalAngle;
        double tangentX = centerPoint.x + (radius * Math.cos( centerToTangentAngle ));

        double tangentY = getY( tangentX, tangentIsAboveCenter );

        return new Point2d( tangentX, tangentY );
    }


    public Point2d getCenterPoint(){ return centerPoint; }
    public double getRadius(){ return radius; }

    public String toString(){
        return "Circle( Radius = " + this.radius + ", Center = " + this.centerPoint + " )";
    }
}
