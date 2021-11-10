package com.ftc6078.utility.Math.interpolators;


import com.ftc6078.utility.Wrappers_General.Point2d;
import com.ftc6078.utility.Wrappers_General.TimestampedValue;

import java.util.ArrayList;


public class PredictiveSplineInterpolator {
    ArrayList<TimestampedValue> timestampedSetpoints;
    ArrayList<CircularLinearInterpolator> interpolators;
    double duration = 0;


    public PredictiveSplineInterpolator(ArrayList<TimestampedValue> timestampedSetpoints, double circleRadius, double startingSlope){ // constructor, creates lines to interpolate from using points given
        this.timestampedSetpoints = new ArrayList<>(); // setup the array lists
        this.duration = timestampedSetpoints.get(timestampedSetpoints.size()-1).timestamp;
        for(TimestampedValue value : timestampedSetpoints) // put in the list but reversed
            this.timestampedSetpoints.add( invertPointsTimestep(value) );

        this.interpolators = new ArrayList<>();

        double lastSlope = startingSlope;
        int interpCount = this.timestampedSetpoints.size() - 1;
        for(int i = 0; i < interpCount; i++){
            interpolators.add( new CircularLinearInterpolator( this.timestampedSetpoints.get(i), this.timestampedSetpoints.get(i+1), circleRadius, lastSlope ) ); // create a linear interpolator between each point
            lastSlope = interpolators.get(i).getLinearComponent().getSlope();

            System.out.println(interpolators.get(i));
        }
    }
    public PredictiveSplineInterpolator(ArrayList<TimestampedValue> timestampedSetpoints, double circleRadius){
        this(timestampedSetpoints, circleRadius, 0);
    }


    public double interpolate(double timestep){
        timestep = invertTimestep(timestep); // invert the timestep to make the output line up with the real timestep
        int interpIndex = 0; // the index of the corresponding line slope and offset for this timestep
        while( interpIndex < interpolators.size() && timestampedSetpoints.get(interpIndex + 1).timestamp < timestep){ // while the first timestamp of the current line is ahead of the target timestep (and still within range of the list)
            interpIndex++; // move forward in the list
        } // improper indexing

        return interpolators.get( interpIndex ).interpolate(timestep);
    }



    public CircularLinearInterpolator getSubinterpolatorForTimestep(double timestep){
        timestep = invertTimestep(timestep);
        int lineIndex = 0; // the index of the corresponding line slope and offset for this timestep
        while( lineIndex < interpolators.size() && timestampedSetpoints.get(lineIndex + 1).timestamp < timestep){ // while the first timestamp of the current line is ahead of the target timestep (and still within range of the list)
            lineIndex++; // move forward in the list
        }
        
        return interpolators.get( lineIndex );
    }
    public CircularLinearInterpolator getInterpolator(int slopeIndex){
        return interpolators.get(slopeIndex);
    }

    private TimestampedValue invertPointsTimestep(TimestampedValue pointToInvert){
        return new TimestampedValue( invertTimestep(pointToInvert.timestamp), pointToInvert.value );
    }

    private double invertTimestep(double normalTimestamp){
        return this.duration - normalTimestamp;
    }
    public TimestampedValue invertValueToNormal(TimestampedValue point){
        return new TimestampedValue( invertTimestep(point.timestamp), point.value);
    }

    public ArrayList<CircularLinearInterpolator> getInterpolators(){ return interpolators; }
}
