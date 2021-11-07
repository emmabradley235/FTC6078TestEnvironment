package com.ftc6078.command;

import com.ftc6078.utility.Control_Theory.FeedforwardController;
import com.ftc6078.utility.ElapsedTime;
import com.ftc6078.utility.Wrappers_General.TimestampedValue;
import com.ftc6078.utility.Control_Theory.feedforward_profiles._one_dimensional.FeedforwardProfile;
import com.ftc6078.utility.Control_Theory.feedforward_profiles._one_dimensional.FeedforwardProfile.ProfileEndBehavior;
import com.ftc6078.utility.Control_Theory.feedforward_profiles._one_dimensional.MultislopeLinearProfile;

import java.util.ArrayList;
import java.util.Arrays;


public class MultislopeLinearMain {

    public static final int PRINT_COUNT = 1000;
    public static final int SIMULATED_CLOCK_SPEED = 10; //  in msec
    static int printIndex = -1;

    private static double timeMult = 800;
    private static TimestampedValue[] timestampedValues = { // the graph of power over time
            new TimestampedValue(0, 0),
            new TimestampedValue(1*timeMult, 0.8),
            new TimestampedValue(2*timeMult, 0),
            new TimestampedValue(3*timeMult, 0),
            new TimestampedValue(4*timeMult, -0.8),
            new TimestampedValue(5*timeMult, 0.0),
            new TimestampedValue(5.5*timeMult, 0.0),
    };


    public static void main(String[] args) {
        FeedforwardProfile profile = new MultislopeLinearProfile(new ArrayList(Arrays.asList(timestampedValues)), 1, 1, ProfileEndBehavior.RESTART);
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
