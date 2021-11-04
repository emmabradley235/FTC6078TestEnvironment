package com.company.feedforward_profiles._two_dimensional;

import com.company.Wrappers_General.Point2d;
import com.company.feedforward_profiles._one_dimensional.FeedforwardProfile;
import com.company.feedforward_profiles._one_dimensional.FeedforwardProfile.ProfileEndBehavior;
import com.company.feedforward_profiles._one_dimensional.SetpointProfile;


public class PairProfile2d extends FeedforwardProfile2d {
    FeedforwardProfile xProfile, yProfile;


    public PairProfile2d(FeedforwardProfile xProfile, FeedforwardProfile yProfile, double overallProfileDuration, ProfileEndBehavior overallProfileEndBehavior){
        super( overallProfileDuration, overallProfileEndBehavior ); // set the pair profile (overall profile) duration and end behaviors

        this.xProfile = xProfile;
        this.yProfile = yProfile;
    }
    public PairProfile2d(FeedforwardProfile xProfile, FeedforwardProfile yProfile){
        super( Math.max(xProfile.getProfileDuration(), yProfile.getProfileDuration()), ProfileEndBehavior.MAINTAIN ); // set the profile duration to the longer of the two profile durations by default, and the end behavior to maintain if not specified

        this.xProfile = xProfile;
        this.yProfile = yProfile;
    }
    public PairProfile2d(){ // create 2 setpoint profiles as the default
        super( 1000, ProfileEndBehavior.MAINTAIN );

        this.xProfile = new SetpointProfile( 0 );
        this.yProfile = new SetpointProfile( 0 );
    }

    @Override
    public Point2d getPrimaryProfileTarget(double timestep){
        return new Point2d( xProfile.getPrimaryTarget(timestep), yProfile.getPrimaryTarget(timestep) );
    }
    @Override
    public Point2d getSecondaryProfileTarget(double timestep){
        return new Point2d( xProfile.getSecondaryTarget(timestep), yProfile.getSecondaryTarget(timestep) );
    }
    @Override
    public Point2d getTertiaryProfileTarget(double timestep){
        return new Point2d( xProfile.getTertiaryTarget(timestep), yProfile.getTertiaryTarget(timestep) );
    }

}
