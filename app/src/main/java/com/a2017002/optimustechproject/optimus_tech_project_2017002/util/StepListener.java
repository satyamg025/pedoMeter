package com.a2017002.optimustechproject.optimus_tech_project_2017002.util;

/**
 * Created by satyam on 26/6/17.
 */

public interface StepListener {

    /**
     * Called when a step has been detected.  Given the time in nanoseconds at
     * which the step was detected.
     */
    public void step(long timeNs);

}