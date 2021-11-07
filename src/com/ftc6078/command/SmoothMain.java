package com.ftc6078.command;

import com.ftc6078.utility.Control_Theory.FeedforwardController;
import com.ftc6078.utility.Control_Theory.feedforward_profiles._one_dimensional.FeedforwardProfile;
import com.ftc6078.utility.Control_Theory.feedforward_profiles._one_dimensional.FeedforwardProfile.ProfileEndBehavior;
import com.ftc6078.utility.Control_Theory.feedforward_profiles._one_dimensional.SmoothProfile;
import com.ftc6078.utility.ElapsedTime;


public class SmoothMain {

    public static final int PRINT_COUNT = 1000;
    public static final int SIMULATED_CLOCK_SPEED = 10; //  in msec
    static int printIndex = -1;


    public static void main(String[] args) {
        FeedforwardProfile profile = new SmoothProfile(1, 0, 0.5, 0.001, 1, ProfileEndBehavior.BACKTRACK);
        FeedforwardController controller = new FeedforwardController(profile);

        ElapsedTime timer = new ElapsedTime();


	    controller.resetAndStartProfile();


        while( opmodeIsActive() ){ // the main loop
	        if( timer.milliseconds() >= SIMULATED_CLOCK_SPEED){

                System.out.print("" + controller.getProfileTime());
                System.out.print(", " + controller.getPrimaryOutput());
                System.out.print(", " + controller.getSecondaryOutput());
                System.out.println(", " + controller.getTertiaryOutput());


                timer.reset();
                printIndex++;
            }
        }
    }

    private static boolean opmodeIsActive(){
        return printIndex < PRINT_COUNT;
    }
}
