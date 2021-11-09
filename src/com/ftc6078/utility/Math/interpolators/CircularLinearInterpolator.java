package com.ftc6078.utility.Math.interpolators;


import com.ftc6078.utility.Math.AdvMath;
import com.ftc6078.utility.Math.Circle;
import com.ftc6078.utility.Wrappers_General.Point2d;
import com.ftc6078.utility.Wrappers_General.TimestampedValue;

public class CircularLinearInterpolator {
    Circle circularComponent;
    LinearInterpolator linearComponent;

    Point2d startPoint, endPoint;
    double circleRadius, slopeAtStart;

    Point2d tangentPoint; // the point where the circular component meets the linear component

    boolean circleCreated = true;
    boolean tangentIsAboveCenter;


    public CircularLinearInterpolator( Point2d startPoint, Point2d endPoint, double circleRadius, double slopeAtStart ){ // constructor, creates a line to interpolate from using points given
        this.startPoint = startPoint;
        this.endPoint = endPoint;
        this.circleRadius = Math.abs( circleRadius ); // absolute value that to be safe
        this.slopeAtStart = slopeAtStart;

        if( Math.abs(endPoint.x - startPoint.x) <= 2*circleRadius ) // TODO: make sure to check that a circle is possible more accurately
            circleCreated = false;
        else if( slopeAtStart == AdvMath.findLineSlope( startPoint, endPoint ) ) // else if no angular adjustment needed to be made
            circleCreated = false;

        if( circleCreated ){ // set everything up as if there is a circle
            tangentIsAboveCenter = slopeAtStart > AdvMath.findLineSlope( startPoint, endPoint ); // if the slope needs to decrease, use the top of the circle, otherwise use the bottom to increase slope
            this.circularComponent = new Circle( circleRadius, startPoint, slopeAtStart, tangentIsAboveCenter );
            // now find the tangent line point (where the linear component and circle meet)
            this.tangentPoint = circularComponent.findStartOfTangentThatIntersects( endPoint, tangentIsAboveCenter ); // and get the full point
            // then draw the linear component between that point and the target point

            this.linearComponent = new LinearInterpolator( tangentPoint, endPoint );
        }
        else { // otherwise, if no circle needs to (or can be) created, set everything up to just draw a line from start to end point
            this.circularComponent = new Circle( 0, startPoint );
            this.tangentPoint = startPoint;

            this.linearComponent = new LinearInterpolator(startPoint, endPoint);
        }
    }
    public CircularLinearInterpolator( TimestampedValue startValue, TimestampedValue endValue, double circleRadius, double slopeAtStart ){
        this( new Point2d(startValue.timestamp, startValue.value), new Point2d(endValue.timestamp, endValue.value), circleRadius, slopeAtStart );
    }


    public double interpolate( double timestep ) {
        // if no circle present, or on the linear segment
        if (!circleCreated || timestep >= tangentPoint.x){
            return linearComponent.interpolate(timestep);
        }
        // as long as the timestep is in a valid x range (within the bounds of the circle)
        else if( timestep < circularComponent.getCenterPoint().x + circularComponent.getRadius() && timestep > circularComponent.getCenterPoint().x - circularComponent.getRadius() ){
            return circularComponent.getY( timestep, tangentIsAboveCenter );
        }

        return startPoint.y; // fail case is return the start point value
    }

    public double getSlopeAt( double timestep ){
        // if no circle present, or on the linear segment
        if (!circleCreated || timestep >= tangentPoint.x){
            return linearComponent.getSlope(); // get the slope of the line
        }
        // as long as the timestep is in a valid x range (within the bounds of the circle)
        else if( timestep < circularComponent.getCenterPoint().x + circularComponent.getRadius() && timestep > circularComponent.getCenterPoint().x - circularComponent.getRadius() ){
            return circularComponent.getSlopeAt( timestep, tangentIsAboveCenter );
        }

        return 0; // fail case is return 0, safer to have no velo if don't know what it is
    }

    public double getSlopeOfSlopeAt( double timestep ){
        // if no circle present, or on the linear segment
        if (!circleCreated || timestep >= tangentPoint.x){
            return 0; // no change in acceleration
        }
        // as long as the timestep is in a valid x range (within the bounds of the circle)
        else if( timestep < circularComponent.getCenterPoint().x + circularComponent.getRadius() && timestep > circularComponent.getCenterPoint().x - circularComponent.getRadius() ){
            return circularComponent.getSlopeOfSlopeAt( timestep, tangentIsAboveCenter );
        }

        return 0; // fail case is return 0, safer to have no acceleration if don't know what it is
    }


    public Circle getCircularComponent(){ return circularComponent; }
    public LinearInterpolator getLinearComponentInterp(){ return linearComponent; }

    public String toString(){
        return "CircularLinearInterp( " + circularComponent + ", " + linearComponent + " )";
    }
}
