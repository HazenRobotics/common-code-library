package com.hazenrobotics.commoncode.movement;

/**
 * An interface for any driving method of a robot.
 */
public interface DrivingController {

    /**
     * Updates the associated wheels or other mechanism for the driving method to move based on
     * a controller or other input methods.
     */
    void updateMotion();

    /**
     * Halts the wheels or other mechanism to stop all movement.
     */
    void stopMotion();
}
