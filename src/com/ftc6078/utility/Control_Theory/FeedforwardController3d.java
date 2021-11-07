package com.ftc6078.utility.Control_Theory;


import com.ftc6078.utility.ElapsedTime;
import com.ftc6078.utility.Wrappers_General.Pose2d;
import com.ftc6078.utility.Control_Theory.feedforward_profiles.three_axis.FeedforwardProfile3d;
import com.ftc6078.utility.Control_Theory.feedforward_profiles.three_axis.TriProfile3d;

public class FeedforwardController3d {

    ElapsedTime profileTime;
    FeedforwardProfile3d profile3d;

    double pauseOffset = 0; // an offset value that stores the time paused, used to act like the timer (which is reset when unpaused) is outputing time starting from the time the profile was paused
    boolean paused = false; // a flag that tracks of the profile is paused

    public FeedforwardController3d(){
        profile3d = new TriProfile3d();

        profileTime = new ElapsedTime();
    }
    public FeedforwardController3d( FeedforwardProfile3d profile3d ){
        this.profile3d = profile3d;

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


    public Pose2d getPrimaryOutput(){ // think position
        if( paused ) // if paused get the target of the profile at the paused time timestep
            return profile3d.getPrimaryTarget( pauseOffset );
        else    // if not paused, return the normal output based on the timestep (plus any required pause offset)
            return profile3d.getPrimaryTarget( pauseOffset + profileTime.milliseconds() );
    }
    public Pose2d getSecondaryOutput(){ // think velocity
        if( paused ) // if paused get the target of the profile at the paused time timestep
            return profile3d.getSecondaryTarget( pauseOffset );
        else    // if not paused, return the normal output based on the timestep (plus any required pause offset)
            return profile3d.getSecondaryTarget( pauseOffset + profileTime.milliseconds() );
    }
    public Pose2d getTertiaryOutput(){ // think accel
        if( paused ) // if paused get the target of the profile at the paused time timestep
            return profile3d.getTertiaryTarget( pauseOffset );
        else    // if not paused, return the normal output based on the timestep (plus any required pause offset)
            return profile3d.getTertiaryTarget( pauseOffset + profileTime.milliseconds() );
    }

    // getter methods
    public boolean isForwardComplete(){return profileTime.milliseconds() > profile3d.getProfileDuration();}
    public double getProfileTime(){return profileTime.milliseconds();}
    public FeedforwardProfile3d getProfile3d(){return profile3d;}

    public boolean setProfile(FeedforwardProfile3d newProfile){
        if(newProfile != null){
            this.profile3d = newProfile; // set the profile
            resetAndStartProfile(); // reset the profile to ensure

            return true;
        }
        else
            return false;
    }
}
