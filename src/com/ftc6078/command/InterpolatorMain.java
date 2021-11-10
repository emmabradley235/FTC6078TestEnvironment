package com.ftc6078.command;

import com.ftc6078.utility.ElapsedTime;
import com.ftc6078.utility.Math.interpolators.CircularLinearInterpolator;
import com.ftc6078.utility.Wrappers_General.TimestampedValue;


public class InterpolatorMain {

    public static final int PRINT_COUNT = 800;
    public static final int SIMULATED_CLOCK_SPEED = 10; //  in msec
    static int printIndex = 0;

    public static final TimestampedValue START_POINT = new TimestampedValue( 800, 800 );
    public static final TimestampedValue TARGET_POINT = new TimestampedValue( 1_600, 4_000 );

    public static void main(String[] args) {

        double circleRadius = 300;
        double startingSlope = 1.614626548;
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
