package com.company;

import java.time.Instant;
import java.time.LocalTime;

public class ElapsedTime {
    long startMsec = 0;

    ElapsedTime(){
        reset();
    }

    public void reset(){
        startMsec = Instant.now().toEpochMilli();
    }

    public long milliseconds() {
        return Instant.now().toEpochMilli() - startMsec; // convert nanosecs to msecs
    }


}
