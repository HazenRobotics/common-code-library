package com.hazenrobotics.commoncode.models.angles.directions;

import com.hazenrobotics.commoncode.models.angles.Angle;

public interface Direction {
    /**
     * Gets the angle represented by a direction (Forward for example might be 0 degrees)
     * @return The angle represented this direction
     */
    Angle getAngle();
}