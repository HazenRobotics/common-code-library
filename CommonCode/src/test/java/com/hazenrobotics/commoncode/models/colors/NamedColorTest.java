package com.hazenrobotics.commoncode.models.colors;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class NamedColorTest {

    private NamedColor sensor1;
    private NamedColor sensor2;
    private NamedColor simple;
    private Color black;

    @Before
    public void setUp() {
        sensor1 = SensorColor.BLACK;
        sensor2 = SensorColor.LIME;
        simple = SimpleColor.BLACK;
        black = new Color(0, 0, 0);
    }

    @Test
    public void getColor() {
        assertEquals(black, sensor1.getColor());
        assertNotEquals(black, sensor2.getColor());
    }

    @Test
    public void equals() {
        assertTrue(sensor1.equals(black));
        assertTrue(simple.equals(black));
        assertFalse(sensor1.equals(simple));
    }

    @Test
    public void getNumber() {
        assertEquals(0, SensorColor.BLACK.getNumber());
        assertNotEquals(0, SensorColor.LIME.getNumber());
    }

    @Test
    public void getByNumber() {
        assertEquals(SensorColor.BLACK, SensorColor.getByNumber(0));
        assertEquals(SensorColor.BLACK, SensorColor.getByNumber(SensorColor.BLACK.getNumber()));
        assertEquals(SensorColor.LIME, SensorColor.getByNumber(6));
        assertEquals(sensor2, SensorColor.getByNumber(6));
    }
}