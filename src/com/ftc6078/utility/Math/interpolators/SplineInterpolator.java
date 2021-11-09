package com.ftc6078.utility.Math.interpolators;


import com.ftc6078.utility.Wrappers_General.TimestampedValue;

import java.util.ArrayList;


public class SplineInterpolator {
    PredictiveSplineInterpolator predictiveInterp;
    LazySplineInterpolator lazyInterp;

    public SplineInterpolator(ArrayList<TimestampedValue> timestampedSetpoints, double circleRadius, double startingSlope){ // constructor, creates lines to interpolate from using points given
        this.predictiveInterp = new PredictiveSplineInterpolator(timestampedSetpoints, circleRadius, startingSlope);
        this.lazyInterp = new LazySplineInterpolator(timestampedSetpoints, circleRadius, startingSlope);
    }
    public SplineInterpolator(ArrayList<TimestampedValue> timestampedSetpoints, double circleRadius){
        this(timestampedSetpoints, circleRadius, 0);
    }


    public double interpolate(double timestep){ // TODO: make it average the center not the whole line
        double lazyValue = lazyInterp.interpolate( timestep ); // get the lazy and predictive interpolator values
        double predictiveValue = predictiveInterp.interpolate( timestep );

        if( timestep < lazyInterp.getSubinterpolatorForTimestep(timestep).tangentPoint.x )

        return ( lazyValue + predictiveValue ) / 2;
    }


    public LazySplineInterpolator getLazyInterpolator(){ return lazyInterp; }
    public PredictiveSplineInterpolator getPredictiveInterpolator(){ return predictiveInterp; }
}
