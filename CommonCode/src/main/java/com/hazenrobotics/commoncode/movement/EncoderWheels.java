package com.hazenrobotics.commoncode.movement;

import com.hazenrobotics.commoncode.models.angles.Angle;
import com.hazenrobotics.commoncode.models.angles.directions.RotationDirection;
import com.hazenrobotics.commoncode.models.angles.directions.SimpleDirection;
import com.hazenrobotics.commoncode.models.distances.Distance;

/**
 * A means to control a robot with encoder functionality's wheels and movement
 */
@SuppressWarnings("unused")
public interface EncoderWheels extends Wheels {

    /**
     * Moves directly forward for a given distance using encoders without any turning.
     * @param distance The distance to move; a negative distance will invert the given movement
     *                 direction
     * @param direction The direction in which to move towards
     */
    void move(Distance distance, SimpleDirection direction);

    /**
     * Turns for a give angle using encoders.
     * @param angle The angle for which to turn; a negative angle inverts the given rotation
     *              direction
     * @param direction The rotation direction in which to turn towards
     */
    void turn(Angle angle, RotationDirection direction);
}
