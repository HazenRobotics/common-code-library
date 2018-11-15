package com.hazenrobotics.commoncode.models.conditions;

import com.hazenrobotics.commoncode.models.colors.NamedColor;
import com.hazenrobotics.commoncode.models.colors.NamedColorList;
import com.hazenrobotics.commoncode.sensors.ColorSensor;

public class ColorMatch extends Condition {
    protected ColorSensor colorSensor;
    protected NamedColorList colorList;

    ColorMatch(ColorSensor colorSensor, NamedColorList colorList) {
        this.colorSensor = colorSensor;
        this.colorList = colorList;
    }

    ColorMatch(ColorSensor colorSensor, NamedColor... colorList) {
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
