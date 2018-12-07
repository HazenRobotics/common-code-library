package com.hazenrobotics.commoncode.models.conditions;

import com.hazenrobotics.commoncode.models.colors.SimpleColor;
import com.hazenrobotics.commoncode.sensors.ColorSensor;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class ColorMatchTest {

    private SimpleColor currentColor;

    private Condition colorMatch;

    @Before
    public void setUp() {
        ColorSensor<SimpleColor> sensor = new ColorSensor<SimpleColor>() {
            @Override
            public SimpleColor getColor() {
                return currentColor;
            }
        };

        colorMatch = new ColorMatch(sensor, SimpleColor.BLACK, SimpleColor.WHITE);
    }

    @Test
    public void condition() {
        currentColor = SimpleColor.BLUE;
        assertFalse(colorMatch.condition());
        currentColor = SimpleColor.BLACK;
        assertTrue(colorMatch.condition());
    }
}