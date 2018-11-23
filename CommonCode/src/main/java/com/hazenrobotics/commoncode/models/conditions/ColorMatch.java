package com.hazenrobotics.commoncode.models.conditions;

import com.hazenrobotics.commoncode.models.colors.NamedColor;
import com.hazenrobotics.commoncode.models.colors.NamedColorList;
import com.hazenrobotics.commoncode.sensors.ColorSensor;

@SuppressWarnings("unused")
public class ColorMatch extends Condition {
    protected ColorSensor colorSensor;
    protected NamedColorList colorList;

    /**
     * Creates a condition which will check a given sensor to see if it matches one of the provided
     * colors in the list
     * @param colorSensor The color sensor which will provide the color to be checked
     * @param colorList The list of named colors which will be compared against
     */
    public ColorMatch(ColorSensor colorSensor, NamedColorList colorList) {
        this.colorSensor = colorSensor;
        this.colorList = colorList;
    }

    /**
     * Creates a condition which will check a given sensor to see if it matches one of the provided
     * colors in the list
     * @param colorSensor The color sensor which will provide the color to be checked
     * @param colorList The list of named colors which will be compared against
     */
    public ColorMatch(ColorSensor colorSensor, NamedColor... colorList) {
        this(colorSensor, new NamedColorList());
        for(NamedColor color : colorList) {
            this.colorList.addColor(color);
        }
    }

    @Override
    protected boolean condition() {
        return colorList.contains(colorSensor.getColor());
    }
}
