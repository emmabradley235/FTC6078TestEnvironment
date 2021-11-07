package com.ftc6078.utility;

import java.time.Instant;


public class ElapsedTime {
    long startMsec = 0;

    public ElapsedTime(){
        reset();
    }

    public void reset(){
        startMsec = Instant.now().toEpochMilli();
    }

    public long milliseconds() {
        return Instant.now().toEpochMilli() - startMsec; // convert nanosecs to msecs
    }
}
