package com.ftc6078.utility.Math.interpolators;


import com.ftc6078.utility.Math.AdvMath;
import com.ftc6078.utility.Math.AngleMath;
import com.ftc6078.utility.Math.Circle;
import com.ftc6078.utility.Wrappers_General.Point2d;
import com.ftc6078.utility.Wrappers_General.Range2d;
import com.ftc6078.utility.Wrappers_General.TimestampedValue;

import java.util.ArrayList;


public class SplineInterpolator {
    ArrayList<Circle> circleComponents;
    ArrayList<Range2d> circleActiveRanges;
    ArrayList<Boolean> tanIsAboveCenterFlags;
    ArrayList<LinearInterpolator> linearComponents;
    ArrayList<Range2d> linearActiveRanges;


    public SplineInterpolator(ArrayList<TimestampedValue> timestampedSetpoints, double circleRadius, double startingSlope){ // constructor, creates lines to interpolate from using points given
        LazySplineInterpolator lazyInterp = new LazySplineInterpolator(timestampedSetpoints, circleRadius, startingSlope);
        PredictiveSplineInterpolator predictiveInterp = new PredictiveSplineInterpolator(timestampedSetpoints, circleRadius, startingSlope);
        ArrayList<CircularLinearInterpolator> lazySubInterps = lazyInterp.getInterpolators();
        ArrayList<CircularLinearInterpolator> predictiveSubInterps = predictiveInterp.getInterpolators();

        // setup circles first
        circleComponents.add( lazySubInterps.get(0).getCircularComponent() ); // get the first circle of the first section
        tanIsAboveCenterFlags.add( lazySubInterps.get(0).tangentIsAboveCenter );
        int circleCount = lazySubInterps.size();
        for(int i = 1; i < lazySubInterps.size(); i++){ // get the center points (and the subsequent circles) that are in the middle of the two given circles
            Circle lazyCircleComponent = lazySubInterps.get(i).getCircularComponent();
            Circle predictiveCircleComponent = predictiveSubInterps.get(circleCount - i).getCircularComponent();
            Point2d sharedPoint = lazySubInterps.get(i).getLinearComponent().endPoint;

            Point2d lazyCircleVectorToShared = lazyCircleComponent.getCenterPoint().minus(sharedPoint);
            Point2d predictiveCircleVectorToShared = new Point2d(predictiveInterp.invertValueToNormal(new TimestampedValue( predictiveCircleComponent.getCenterPoint().minus(sharedPoint) )));

            Point2d vectorToNewCenter = lazyCircleVectorToShared.plus( predictiveCircleVectorToShared ); // add the vectors together to get the vector (aka x,y version of an angle) to the new center

            boolean tangentIsAboveCenter = lazySubInterps.get(i).tangentIsAboveCenter;
            Circle newAverageCircle;
            if( vectorToNewCenter.x == 0 ){ // if there is no x change (vector points straight up), just add the circle radius up or down accordingly
                Point2d centerPointOffset = new Point2d(0, circleRadius);
                if( tangentIsAboveCenter )
                    centerPointOffset.y *= -1;
                newAverageCircle = new Circle( circleRadius, sharedPoint.plus(centerPointOffset) );
            }
            else{
                double vectorSlopeToNewCenter = Math.tan( AngleMath.getVectorAngle(vectorToNewCenter) );

                newAverageCircle = new Circle( circleRadius, sharedPoint, vectorSlopeToNewCenter, tangentIsAboveCenter );
            }

            circleComponents.add( newAverageCircle );
            tanIsAboveCenterFlags.add( tangentIsAboveCenter );
            // TODO: find active ranges

        }
        CircularLinearInterpolator endPredictiveInterp = predictiveSubInterps.get(0);
        Point2d endPredictiveInterpCirclePoint = new Point2d( predictiveInterp.invertValueToNormal( new TimestampedValue(endPredictiveInterp.getCircularComponent().getCenterPoint()) ) );
        circleComponents.add( new Circle( endPredictiveInterp.getCircularComponent().getRadius(), endPredictiveInterpCirclePoint ) );
        tanIsAboveCenterFlags.add( predictiveSubInterps.get(0).tangentIsAboveCenter );
    }
    public SplineInterpolator(ArrayList<TimestampedValue> timestampedSetpoints, double circleRadius){
        this(timestampedSetpoints, circleRadius, 0);
    }


    public double interpolate(double timestep){
        for(int i = 0; i < circleComponents.size(); i++){
            if( circleActiveRanges.get(i).isNumWithinRange( timestep ) ){
                return circleComponents.get(i).getY( timestep, tanIsAboveCenterFlags.get(i) );
            }
        }

        for(int i = 0; i < linearComponents.size(); i++){
            if( linearActiveRanges.get(i).isNumWithinRange( timestep ) ){
                return linearComponents.get(i).interpolate( timestep );
            }
        }

        return linearComponents.get(0).interpolate(timestep);
    }

    public double getSlopeAt(double timestep){ // TODO: make it average the center not the whole line
        for(int i = 0; i < circleComponents.size(); i++){
            if( circleActiveRanges.get(i).isNumWithinRange( timestep ) ){
                return circleComponents.get(i).getSlopeAt( timestep, tanIsAboveCenterFlags.get(i) );
            }
        }

        for(int i = 0; i < linearComponents.size(); i++){
            if( linearActiveRanges.get(i).isNumWithinRange( timestep ) ){
                return linearComponents.get(i).getSlope();
            }
        }

        return linearComponents.get(0).interpolate(timestep);
    }

    public double getSlopeOfSlopeAt(double timestep){ // TODO: make it average the center not the whole line
        for(int i = 0; i < circleComponents.size(); i++){
            if( circleActiveRanges.get(i).isNumWithinRange( timestep ) ){
                return circleComponents.get(i).getSlopeOfSlopeAt( timestep, tanIsAboveCenterFlags.get(i) );
            }
        }

        return 0;
    }


    public ArrayList<Circle> getCircleComponents(){ return circleComponents; }
    public ArrayList<LinearInterpolator> getLinearComponents(){ return linearComponents; }
}
