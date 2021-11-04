package com.company;


import com.company.Wrappers_General.Point2d;
import com.company.feedforward_profiles._two_dimensional.FeedforwardProfile2d;
import com.company.feedforward_profiles._two_dimensional.PairProfile2d;

public class FeedforwardController2d {

    ElapsedTime profileTime;
    FeedforwardProfile2d profile2d;

    double pauseOffset = 0; // an offset value that stores the time paused, used to act like the timer (which is reset when unpaused) is outputing time starting from the time the profile was paused
    boolean paused = false; // a flag that tracks of the profile is paused

    public FeedforwardController2d(){
        profile2d = new PairProfile2d();

        profileTime = new ElapsedTime();
    }
    public FeedforwardController2d(FeedforwardProfile2d profile2d){
        this.profile2d = profile2d;

        profileTime = new ElapsedTime();
    }


    public void resetAndStartProfile(){ // starts the profile and resets any pause offset
        profileTime.reset();
        pauseOffset = 0;
    }

    public void pauseProfile(){ // pauses profile at the current time
        if(!paused) {
            pauseOffset += profileTime.milliseconds();
            paused = true;
        }
    }
    public void unpauseProfile(){ // gets the train rolling again
        if(paused){
            profileTime.reset();
            paused = false;
        }
    }
    public boolean isProfilePaused(){ return paused; }


    public Point2d getPrimaryOutput(){ // think position
        if( paused ) // if paused get the target of the profile at the paused time timestep
            return profile2d.getPrimaryTarget( pauseOffset );
        else    // if not paused, return the normal output based on the timestep (plus any required pause offset)
            return profile2d.getPrimaryTarget( pauseOffset + profileTime.milliseconds() );
    }
    public Point2d getSecondaryOutput(){ // think velocity
        if( paused ) // if paused get the target of the profile at the paused time timestep
            return profile2d.getSecondaryTarget( pauseOffset );
        else    // if not paused, return the normal output based on the timestep (plus any required pause offset)
            return profile2d.getSecondaryTarget( pauseOffset + profileTime.milliseconds() );
    }
    public Point2d getTertiaryOutput(){ // think accel
        if( paused ) // if paused get the target of the profile at the paused time timestep
            return profile2d.getTertiaryTarget( pauseOffset );
        else    // if not paused, return the normal output based on the timestep (plus any required pause offset)
            return profile2d.getTertiaryTarget( pauseOffset + profileTime.milliseconds() );
    }

    // getter methods
    public boolean isForwardComplete(){return profileTime.milliseconds() > profile2d.getProfileDuration();}
    public double getProfileTime(){return profileTime.milliseconds();}
    public FeedforwardProfile2d getProfile2d(){return profile2d;}

    public boolean setProfile(FeedforwardProfile2d newProfile){
        if(newProfile != null){
            this.profile2d = newProfile; // set the profile
            resetAndStartProfile(); // reset the profile to ensure

            return true;
        }
        else
            return false;
    }
}
