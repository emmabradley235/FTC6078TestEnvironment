package com.ftc6078.command;

import com.ftc6078.utility.Control_Theory.FeedforwardController;
import com.ftc6078.utility.Control_Theory.feedforward_profiles._one_dimensional.FeedforwardProfile;
import com.ftc6078.utility.Control_Theory.feedforward_profiles._one_dimensional.FeedforwardProfile.ProfileEndBehavior;
import com.ftc6078.utility.Control_Theory.feedforward_profiles._one_dimensional.LinearProfile;
import com.ftc6078.utility.ElapsedTime;
import com.ftc6078.utility.Math.CircularLinearInterpolator;
import com.ftc6078.utility.Wrappers_General.Point2d;
import com.ftc6078.utility.Wrappers_General.TimestampedValue;


public class InterpolatorMain {

    public static final int PRINT_COUNT = 800;
    public static final int SIMULATED_CLOCK_SPEED = 10; //  in msec
    static int printIndex = 0;

    public static final TimestampedValue START_POINT = new TimestampedValue( 0, -1500 );
    public static final TimestampedValue TARGET_POINT = new TimestampedValue( 8_000, -5_000 );

    public static void main(String[] args) {

        double circleRadius = 3_000;
        double startingSlope = 1;
        CircularLinearInterpolator interp = new CircularLinearInterpolator( START_POINT, TARGET_POINT, circleRadius, startingSlope );

        ElapsedTime clockSpeedTimer = new ElapsedTime();
        ElapsedTime interpolationTime = new ElapsedTime();


	    while( opmodeIsActive() ){ // the main loop
	        if( clockSpeedTimer.milliseconds() >= SIMULATED_CLOCK_SPEED){
	            double timestep = interpolationTime.milliseconds();
	            System.out.println("" + timestep
                        + ", " + interp.interpolate( timestep )
                        + ", " + interp.getSlopeAt( timestep )
                        + ", " + interp.getSlopeOfSlopeAt( timestep )
                );

                clockSpeedTimer.reset();
                printIndex++;
            }
        }
    }

    private static boolean opmodeIsActive(){
        return printIndex < PRINT_COUNT;
    }
}
