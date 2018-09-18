package com.hazenrobotics.commoncode.movement;

import com.hazenrobotics.commoncode.models.angles.Angle;
import com.hazenrobotics.commoncode.models.distances.Distance;;

/**
 * A means to control a robot with encoder functionality's wheels and movement
 */
public interface EncoderWheels extends Wheels {
    /**
     * Moves directly forward for a given distance using encoders without any turning
     * @param distance The distance to move
     */
    void move(Distance distance);

    /**
     * Turns for a given angle using encoders
     * @param angle The angle for which to turn
     */
    void turn(Angle angle);
}
