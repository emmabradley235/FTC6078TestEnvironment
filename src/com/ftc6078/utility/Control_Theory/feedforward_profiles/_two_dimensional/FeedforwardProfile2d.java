package com.ftc6078.utility.Control_Theory.feedforward_profiles._two_dimensional;

import com.ftc6078.utility.Wrappers_General.Point2d;
import com.ftc6078.utility.Control_Theory.feedforward_profiles._one_dimensional.FeedforwardProfile.ProfileEndBehavior;



public abstract class FeedforwardProfile2d {

    double profileDuration; // local variables that all subclasses will use
    ProfileEndBehavior endBehavior;



    protected FeedforwardProfile2d(double profileDuration, ProfileEndBehavior endBehavior){ // constructor (called using super() by child classes)
        this.profileDuration = profileDuration;
        this.endBehavior = endBehavior;
    }


    public final Point2d getPrimaryTarget(double timestep){ // the method called to get a profile's primary (think position) target, the method it calls (getProfileTarget) will change depending on subclass, but still always handles end behaviour before passing in
        return getPrimaryProfileTarget( handleEndBehavior(timestep) );
    }
    public final Point2d getSecondaryTarget(double timestep) { // the method called to get a profile's primary (think velocity) target
        return getSecondaryProfileTarget( handleEndBehavior(timestep) );
    }
    public final Point2d getTertiaryTarget(double timestep){ // the method called to get a profile's primary (think acceleration) target
        return getTertiaryProfileTarget( handleEndBehavior(timestep) );
    }
    protected Point2d getPrimaryProfileTarget(double timestep){ // to be overriden by subclasses
        return new Point2d();
    }
    protected Point2d getSecondaryProfileTarget(double timestep){ // to be overriden by subclasses
        return new Point2d();
    }
    protected Point2d getTertiaryProfileTarget(double timestep){ // to be overriden by subclasses
        return new Point2d();
    }


    private double handleEndBehavior(double timestep){ // manipulates the input timestep to simulate the proper type of end behavior
        if( endBehavior == ProfileEndBehavior.BACKTRACK){
            while(timestep > profileDuration * 2){ // remove from the timestep until it is within twice the profile duration, as one duration is the way there and a second duration the way back
                timestep -= profileDuration * 2;
            }

            if(timestep > profileDuration){ // if still greater than profile duration, means the time should put us on the way back, so backtrack (as less than the profile duration after correction would be the regular way, aka frowards on the profile)
                double distanceBacktracking = timestep - profileDuration; // subtract the profile duration from the timestep to see how far into the backtracking we are
                timestep = profileDuration - distanceBacktracking; // then backtrack in the path by that amount
            }
        }
        else if( endBehavior == ProfileEndBehavior.RESTART ){ // if on the restart
            while( timestep > profileDuration ){ // remove from the timestep until it is within the profile duration, will create restart like behavior
                timestep -= profileDuration;
            }
        }
        else if( timestep > profileDuration ){ // if no others, must be maintain, so check if we are beyond the end and need to use the end value
            timestep = profileDuration; // then use the end value by telling the profile it is at the end
        }

        return timestep;
    }


    public double getProfileDuration(){return profileDuration;}
    public ProfileEndBehavior getEndBehavior(){return endBehavior;}

}
