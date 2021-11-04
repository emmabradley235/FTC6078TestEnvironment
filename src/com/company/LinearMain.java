package com.company;

import com.company.feedforward_profiles._one_dimensional.FeedforwardProfile;
import com.company.feedforward_profiles._one_dimensional.LinearProfile;
import com.company.feedforward_profiles._one_dimensional.FeedforwardProfile.ProfileEndBehavior;


public class LinearMain {

    public static final int PRINT_COUNT = 1000;
    public static final int SIMULATED_CLOCK_SPEED = 10; //  in msec
    static int printIndex = -1;


    public static void main(String[] args) {
        FeedforwardProfile profile = new LinearProfile(0, 1, 2000, ProfileEndBehavior.BACKTRACK);
	    FeedforwardController controller = new FeedforwardController(profile);
	    ElapsedTime timer = new ElapsedTime();


	    controller.resetAndStartProfile();

	    while( opmodeIsActive() ){ // the main loop
	        if( timer.milliseconds() >= SIMULATED_CLOCK_SPEED){
	            System.out.println("" + controller.getProfileTime()
                        + ", " + controller.getPrimaryOutput()
                        + ", " + controller.getSecondaryOutput()
                        + ", " + controller.getTertiaryOutput()
                );

                timer.reset();
                printIndex++;
            }
        }
    }

    private static boolean opmodeIsActive(){
        return printIndex < PRINT_COUNT;
    }
}
