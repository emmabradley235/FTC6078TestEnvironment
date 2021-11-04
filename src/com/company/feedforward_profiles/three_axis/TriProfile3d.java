package com.company.feedforward_profiles.three_axis;

import com.company.Wrappers_General.Point2d;
import com.company.Wrappers_General.Pose2d;
import com.company.feedforward_profiles._one_dimensional.FeedforwardProfile;
import com.company.feedforward_profiles._one_dimensional.FeedforwardProfile.ProfileEndBehavior;
import com.company.feedforward_profiles._one_dimensional.SetpointProfile;
import com.company.feedforward_profiles._two_dimensional.FeedforwardProfile2d;
import com.company.feedforward_profiles._two_dimensional.PairProfile2d;


public class TriProfile3d extends FeedforwardProfile3d {
    FeedforwardProfile xProfile, yProfile, headingProfile;


    public TriProfile3d(FeedforwardProfile xProfile, FeedforwardProfile yProfile, FeedforwardProfile headingProfile, double overallProfileDuration, ProfileEndBehavior overallProfileEndBehavior){
        super( overallProfileDuration, overallProfileEndBehavior ); // set the pair profile (overall profile) duration and end behaviors

        this.xProfile = xProfile;
        this.yProfile = yProfile;
        this.headingProfile = headingProfile;
    }
    public TriProfile3d(FeedforwardProfile xProfile, FeedforwardProfile yProfile,  FeedforwardProfile headingProfile){
        super( Math.max( Math.max( xProfile.getProfileDuration(), yProfile.getProfileDuration() ), headingProfile.getProfileDuration() ), ProfileEndBehavior.MAINTAIN ); // set the profile duration to the longer of the two profile durations by default, and the end behavior to maintain if not specified

        this.xProfile = xProfile;
        this.yProfile = yProfile;
        this.headingProfile = headingProfile;
    }
    public TriProfile3d(){ // create 2 setpoint profiles as the default
        super( 1000, ProfileEndBehavior.MAINTAIN );

        this.xProfile = new SetpointProfile( 0 );
        this.yProfile = new SetpointProfile( 0 );
        this.headingProfile = new SetpointProfile( 0 );
    }

    @Override
    public Pose2d getPrimaryProfileTarget(double timestep){
        return new Pose2d( xProfile.getPrimaryTarget(timestep), yProfile.getPrimaryTarget(timestep), headingProfile.getPrimaryTarget(timestep) );
    }
    @Override
    public Pose2d getSecondaryProfileTarget(double timestep){
        return new Pose2d( xProfile.getSecondaryTarget(timestep), yProfile.getSecondaryTarget(timestep), headingProfile.getSecondaryTarget(timestep) );
    }
    @Override
    public Pose2d getTertiaryProfileTarget(double timestep){
        return new Pose2d( xProfile.getTertiaryTarget(timestep), yProfile.getTertiaryTarget(timestep), headingProfile.getTertiaryTarget(timestep) );
    }

}
