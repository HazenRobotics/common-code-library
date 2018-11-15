package com.hazenrobotics.commoncode.sensors;

import com.hazenrobotics.commoncode.models.colors.NamedColor;

public interface ColorSensor<T extends NamedColor> {
    /**
     * Determines the color the sensor is currently seeing
     * @return Returns a named color from a list of colors
     */
    T getColor();
}
