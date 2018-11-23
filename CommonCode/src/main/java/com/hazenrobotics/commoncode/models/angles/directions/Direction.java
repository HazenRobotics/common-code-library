package com.hazenrobotics.commoncode.models.angles.directions;

import com.hazenrobotics.commoncode.models.angles.Angle;

public interface Direction {
    /**
     * Gets the angle represented by a direction (Forward for example might be 0 degrees)
     * @return The angle represented this direction
     */
    Angle getAngle();

    /**
     * Gets the corresponding opposite direction in a direction pair, the angle of this inverted
     * direction should be 180 degrees more or less than this direction.
     * @return The opposite direction
     */
    Direction inverted();
}