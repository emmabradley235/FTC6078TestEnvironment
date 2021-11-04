package com.company.Math;


import com.company.Wrappers_General.TimestampedValue;

import java.util.ArrayList;


public class MultislopeLinearInterpolator {
    ArrayList<TimestampedValue> timestampedSetpoints;
    ArrayList<LinearInterpolator> interpolators;



    public MultislopeLinearInterpolator( ArrayList<TimestampedValue> timestampedSetpoints){ // constructor, creates lines to interpolate from using points given
        this.timestampedSetpoints = timestampedSetpoints; // setup the array lists
        this.interpolators = new ArrayList<>();

        int interpCount = timestampedSetpoints.size() - 1;
        for(int i = 0; i < interpCount; i++){
            interpolators.add( new LinearInterpolator( timestampedSetpoints.get(i), timestampedSetpoints.get(i+1) ) ); // create a linear interpolator between each point
        }
    }


    public double interpolate(double timestep){
        int lineIndex = 0; // the index of the corresponding line slope and offset for this timestep
        while( lineIndex < interpolators.size() && timestampedSetpoints.get(lineIndex + 1).timestamp < timestep){ // while the first timestamp of the current line is ahead of the target timestep (and still within range of the list)
            lineIndex++; // move forward in the list
        } // improper indexing
        //System.out.print("" + lineIndex + ": ");

        return interpolators.get( lineIndex ).interpolate(timestep);
    }


    public LinearInterpolator getSubinterpolatorForTimestep(double timestep){
        int lineIndex = 0; // the index of the corresponding line slope and offset for this timestep
        while( lineIndex < interpolators.size() && timestampedSetpoints.get(lineIndex + 1).timestamp < timestep){ // while the first timestamp of the current line is ahead of the target timestep (and still within range of the list)
            lineIndex++; // move forward in the list
        }
        
        return interpolators.get( lineIndex );
    }
    public LinearInterpolator getInterpolator(int slopeIndex){
        return interpolators.get(slopeIndex);
    }
}
