package com.ftc6078.utility.Wrappers_General;



public class Range2d { // a class that neatly holds a minimum and maximum for a 2d range of values
    public double min;
    public double max;


    public Range2d(){
        this.min = 0;
        this.max = 0;
    }
    public Range2d(double min, double max){
        this.min = min;
        this.max = max;
    }
    public Range2d(Point2d point){
        this.min = point.x;
        this.max = point.y;
    }


    public void setMin(double min){this.min = min;}
    public void setMax(double max){this.max = max;}

    public double getMin() {
        return min;
    }
    public double getMax() {
        return max;
    }


    public String toString(){
        return "range(" + min + ", " + max + ")";
    }
}
