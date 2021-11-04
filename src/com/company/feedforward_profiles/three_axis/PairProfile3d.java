package com.company.feedforward_profiles.three_axis;

import com.company.Wrappers_General.Point2d;
import com.company.Wrappers_General.Pose2d;
import com.company.feedforward_profiles._one_dimensional.FeedforwardProfile;
import com.company.feedforward_profiles._one_dimensional.FeedforwardProfile.ProfileEndBehavior;
import com.company.feedforward_profiles._one_dimensional.SetpointProfile;
import com.company.feedforward_profiles._two_dimensional.FeedforwardProfile2d;
import com.company.feedforward_profiles._two_dimensional.PairProfile2d;


public class PairProfile3d extends FeedforwardProfile3d {
    FeedforwardProfile2d xyProfile;
    FeedforwardProfile headingProfile;


    public PairProfile3d(FeedforwardProfile2d xyProfile, FeedforwardProfile headingProfile, double overallProfileDuration, ProfileEndBehavior overallProfileEndBehavior){
        super( overallProfileDuration, overallProfileEndBehavior ); // set the pair profile (overall profile) duration and end behaviors

        this.xyProfile = xyProfile;
        this.headingProfile = headingProfile;
    }
    public PairProfile3d(FeedforwardProfile2d xyProfile, FeedforwardProfile headingProfile){
        super( Math.max(xyProfile.getProfileDuration(), headingProfile.getProfileDuration()), ProfileEndBehavior.MAINTAIN ); // set the profile duration to the longer of the two profile durations by default, and the end behavior to maintain if not specified

        this.xyProfile = xyProfile;
        this.headingProfile = headingProfile;
    }
    public PairProfile3d(){ // create 2 setpoint profiles as the default
        super( 1000, ProfileEndBehavior.MAINTAIN );

        this.xyProfile = new PairProfile2d();
        this.headingProfile = new SetpointProfile( 0 );
    }

    @Override
    public Pose2d getPrimaryProfileTarget(double timestep){
        Point2d point = xyProfile.getPrimaryTarget(timestep);
        return new Pose2d( point.getX(), point.getY(), headingProfile.getPrimaryTarget(timestep) );
    }
    @Override
    public Pose2d getSecondaryProfileTarget(double timestep){
        Point2d point = xyProfile.getSecondaryTarget(timestep);
        return new Pose2d( point.getX(), point.getY(), headingProfile.getSecondaryTarget(timestep) );
    }
    @Override
    public Pose2d getTertiaryProfileTarget(double timestep){
        Point2d point = xyProfile.getTertiaryTarget(timestep);
        return new Pose2d( point.getX(), point.getY(), headingProfile.getTertiaryTarget(timestep) );
    }

}
