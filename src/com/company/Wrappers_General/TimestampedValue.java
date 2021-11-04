package com.company.Wrappers_General;


public class TimestampedValue { // this is just a class to hold two doubles in one, kinda like a dictionary thing, but with one unit. Good with ArrayLists
    public double timestamp;
    public double value;

    public TimestampedValue(){} // default constructor, leaves variables at 0
    public TimestampedValue(double timestamp, double value){ // specific constructor
        this.timestamp = timestamp;
        this.value = value;
    }
    public TimestampedValue(Point2d point){
        this.timestamp = point.x;
        this.value = point.y;
    }
    public TimestampedValue(Range2d range){
        this.timestamp = range.min;
        this.value = range.max;
    }


    public String toString() {
        return "(" + timestamp + ": " + value + ")";
    }
}
