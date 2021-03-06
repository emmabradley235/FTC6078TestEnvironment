package com.ftc6078.utility.Math.interpolators;


import com.ftc6078.utility.Wrappers_General.TimestampedValue;

import java.util.ArrayList;


public class LazySplineInterpolator {
    ArrayList<TimestampedValue> timestampedSetpoints;
    ArrayList<CircularLinearInterpolator> interpolators;



    public LazySplineInterpolator(ArrayList<TimestampedValue> timestampedSetpoints, double circleRadius, double startingSlope){ // constructor, creates lines to interpolate from using points given
        this.timestampedSetpoints = timestampedSetpoints; // setup the array lists
        this.interpolators = new ArrayList<>();

        double lastSlope = startingSlope;
        int interpCount = timestampedSetpoints.size() - 1;
        for(int i = 0; i < interpCount; i++){
            interpolators.add( new CircularLinearInterpolator( timestampedSetpoints.get(i), timestampedSetpoints.get(i+1), circleRadius, lastSlope ) ); // create a linear interpolator between each point
            lastSlope = interpolators.get(i).getLinearComponent().getSlope();

            //System.out.println(interpolators.get(i));
        }
    }
    public LazySplineInterpolator(ArrayList<TimestampedValue> timestampedSetpoints, double circleRadius){
        this(timestampedSetpoints, circleRadius, 0);
    }


    public double interpolate(double timestep){
        int lineIndex = 0; // the index of the corresponding line slope and offset for this timestep
        while( lineIndex < interpolators.size() && timestampedSetpoints.get(lineIndex + 1).timestamp < timestep){ // while the first timestamp of the current line is ahead of the target timestep (and still within range of the list)
            lineIndex++; // move forward in the list
        } // improper indexing
        //System.out.print("" + lineIndex + ": ");

        return interpolators.get( lineIndex ).interpolate(timestep);
    }


    public CircularLinearInterpolator getSubinterpolatorForTimestep(double timestep){
        int lineIndex = 0; // the index of the corresponding line slope and offset for this timestep
        while( lineIndex < interpolators.size() && timestampedSetpoints.get(lineIndex + 1).timestamp < timestep){ // while the first timestamp of the current line is ahead of the target timestep (and still within range of the list)
            lineIndex++; // move forward in the list
        }
        
        return interpolators.get( lineIndex );
    }
    public CircularLinearInterpolator getInterpolator(int slopeIndex){
        return interpolators.get(slopeIndex);
    }
    public ArrayList<CircularLinearInterpolator> getInterpolators(){ return interpolators; }
}
