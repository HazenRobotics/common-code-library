package com.hazenrobotics.commoncode.interfaces;

/**
 * An interface for functions to idle the robot logic
 */
public interface IdleInterface {
    /**
     * Idles the robot momentarily, allowing other threads and the hardware to update it state.
     */
    void idle();

    /**
     * Waits for a given amounts of time, idling while waiting
     * @param milliseconds The amount of time in milliseconds to wait
     */
    void sleep(long milliseconds);
}