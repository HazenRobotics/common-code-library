package com.hazenrobotics.commoncode.models.angles.directions;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class DirectionTest {

    private Direction side;
    private Direction simple;
    private RotationDirection rotation;

    @Before
    public void setUp() {
        side = SideDirection.LEFT;
        simple = SimpleDirection.BACKWARDS;
        rotation = RotationDirection.CLOCKWISE;
    }

    @Test
    public void getAngle() {
        assertEquals(270f, side.getAngle().getDegrees(), 0.01f);
        assertEquals(180f, simple.getAngle().getDegrees(), 0.01f);
    }

    @Test
    public void inverted() {
        assertEquals(SideDirection.RIGHT, side.inverted());
        assertEquals(SimpleDirection.FORWARDS, simple.inverted());
        assertEquals(RotationDirection.COUNTER_CLOCKWISE, rotation.inverted());
    }
}