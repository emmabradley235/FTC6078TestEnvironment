package com.ftc6078.utility.Control_Theory.feedforward_profiles._one_dimensional;


import com.ftc6078.utility.Math.interpolators.PredictiveSplineInterpolator;
import com.ftc6078.utility.Math.interpolators.SplineInterpolator;
import com.ftc6078.utility.Wrappers_General.TimestampedValue;

import java.util.ArrayList;


public class SplineProfile extends FeedforwardProfile {
    SplineInterpolator splineInterp; // the thing that interpolates between the setpoints
    double K1, K2, K3; // the multiplier for the setpoint (ex. number you multiply setpoint velocity by to create the motor power required to reach that setpoint)


    public SplineProfile(ArrayList<TimestampedValue> timestampedSetpoints, double circleRadius, double primaryK, double secondaryK, double tertiaryK, ProfileEndBehavior endBehavior){
        super( timestampedSetpoints.get( timestampedSetpoints.size() - 1 ).timestamp, endBehavior ); // call the FeedforwardProfile constructor to setup duration and end behavior variables (using the last timestamp in the setpoints as the duration)
        this.splineInterp = new SplineInterpolator( timestampedSetpoints, circleRadius ); // setup the multislope linear interpolator

        this.K1 = primaryK;
        this.K2 = secondaryK;
        this.K3 = tertiaryK;
    }
    public SplineProfile(ArrayList<TimestampedValue> timestampedSetpoints, double circleRadius, double primaryK, double secondaryK, double tertairyK){
        this(timestampedSetpoints, circleRadius,primaryK, secondaryK, tertairyK, ProfileEndBehavior.MAINTAIN); // have maintain be the default end behavior
    }
    public SplineProfile(ArrayList<TimestampedValue> timestampedSetpoints, double circleRadius, ProfileEndBehavior endBehavior){
        this(timestampedSetpoints, circleRadius,1.0, 1.0, 1.0, endBehavior);  // if no value passed, use a gain of 1 to not modify values
    }
    public SplineProfile(ArrayList<TimestampedValue> timestampedSetpoints, double circleRadius){
        this(timestampedSetpoints, circleRadius, 1.0, 1.0, 1.0, ProfileEndBehavior.MAINTAIN);  // if no value passed, use a gain of 1 to not modify values
    }


    @Override
    public double getPrimaryProfileTarget(double timestep){ // think position
        return K1 * splineInterp.interpolate( timestep ); // then use that to figure out what our current point should be between the two points
    }

    @Override
    public double getSecondaryProfileTarget(double timestep){ // think velocity
        double lazyValue = splineInterp.getLazyInterpolator().getSubinterpolatorForTimestep( timestep ).getSlopeAt(timestep); // get the lazy and predictive interpolator values
        double predictiveValue = splineInterp.getPredictiveInterpolator().getSubinterpolatorForTimestep( timestep ).getSlopeAt(timestep);

        return K2 * ( lazyValue + predictiveValue ) / 2;
    }

    @Override
    public double getTertiaryProfileTarget(double timestep){ // think velocity
        double lazyValue = splineInterp.getLazyInterpolator().getSubinterpolatorForTimestep( timestep ).getSlopeOfSlopeAt(timestep); // get the lazy and predictive interpolator values
        double predictiveValue = splineInterp.getPredictiveInterpolator().getSubinterpolatorForTimestep( timestep ).getSlopeOfSlopeAt(timestep);

        return K3 * ( lazyValue + predictiveValue ) / 2;
    }

    public SplineInterpolator getSplineInterpolator(){ return this.splineInterp; }
}
