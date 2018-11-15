package com.hazenrobotics.commoncode.sensors;

import com.hazenrobotics.commoncode.models.distances.Distance;

public interface RangeSensor {

    /**
     * Determines the distance in front of the sensor to the nearest object
     * @return The distance to the object
     */
    Distance getRange();
}
