package com.ftc6078.utility.Control_Theory.feedforward_profiles._one_dimensional;


import com.ftc6078.utility.Math.interpolators.LazySplineInterpolator;
import com.ftc6078.utility.Wrappers_General.TimestampedValue;

import java.util.ArrayList;


public class LazySplineProfile extends FeedforwardProfile {
    LazySplineInterpolator lazySplineInterp; // the thing that interpolates between the setpoints
    double K1, K2, K3; // the multiplier for the setpoint (ex. number you multiply setpoint velocity by to create the motor power required to reach that setpoint)


    public LazySplineProfile(ArrayList<TimestampedValue> timestampedSetpoints, double circleRadius, double primaryK, double secondaryK, double tertiaryK, ProfileEndBehavior endBehavior){
        super( timestampedSetpoints.get( timestampedSetpoints.size() - 1 ).timestamp, endBehavior ); // call the FeedforwardProfile constructor to setup duration and end behavior variables (using the last timestamp in the setpoints as the duration)
        this.lazySplineInterp = new LazySplineInterpolator( timestampedSetpoints, circleRadius ); // setup the multislope linear interpolator

        this.K1 = primaryK;
        this.K2 = secondaryK;
        this.K3 = tertiaryK;
    }
    public LazySplineProfile(ArrayList<TimestampedValue> timestampedSetpoints, double circleRadius, double primaryK, double secondaryK, double tertairyK){
        this(timestampedSetpoints, circleRadius,primaryK, secondaryK, tertairyK, ProfileEndBehavior.MAINTAIN); // have maintain be the default end behavior
    }
    public LazySplineProfile(ArrayList<TimestampedValue> timestampedSetpoints, double circleRadius, ProfileEndBehavior endBehavior){
        this(timestampedSetpoints, circleRadius,1.0, 1.0, 1.0, endBehavior);  // if no value passed, use a gain of 1 to not modify values
    }
    public LazySplineProfile(ArrayList<TimestampedValue> timestampedSetpoints, double circleRadius){
        this(timestampedSetpoints, circleRadius, 1.0, 1.0, 1.0, ProfileEndBehavior.MAINTAIN);  // if no value passed, use a gain of 1 to not modify values
    }


    @Override
    public double getPrimaryProfileTarget(double timestep){ // think position
        return K1 * lazySplineInterp.interpolate( timestep ); // then use that to figure out what our current point should be between the two points
    }

    @Override
    public double getSecondaryProfileTarget(double timestep){ // think velocity
        return K2 * lazySplineInterp.getSubinterpolatorForTimestep( timestep ).getSlopeAt(timestep);
    }

    @Override
    public double getTertiaryProfileTarget(double timestep){ // think velocity
        return K3 * lazySplineInterp.getSubinterpolatorForTimestep( timestep ).getSlopeOfSlopeAt( timestep );
    }
}
