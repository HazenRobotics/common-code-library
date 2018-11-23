package com.hazenrobotics.commoncode.models.colors;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class ColorTest {

    private Color color1;
    private Color color2;
    private Color color3;
    private NamedColor named;

    @Before
    public void setUp() {
        color1 = new Color(120, 240, 50);
        color2 = new Color(100, 235, 40);
        color3 = new Color(50, 100, 50);
        named = new NamedColor() {
            @Override
            public Color getColor() {
                return new Color(100, 235, 40);
            }

            @Override
            public boolean equals(Color other) {
                return other.equals(this);
            }
        };
    }

    @Test
    public void getRed() {
        assertEquals(120, color1.getRed());
    }

    @Test
    public void getGreen() {
        assertEquals(235, color2.getGreen());
    }

    @Test
    public void getBlue() {
        assertEquals(50, color3.getBlue());
    }

    @Test
    public void constructor() {
        IllegalArgumentException e = null;
        try {
            new Color(12, 300, 0);
        } catch (IllegalArgumentException exception) {
            e = exception;
        }
        assertTrue(e instanceof IllegalArgumentException);
        e = null;
        try {
            new Color(-100, 0, 255);
        } catch (IllegalArgumentException exception) {
            e = exception;
        }
        assertTrue(e instanceof IllegalArgumentException);
    }

    @Test
    public void equals() {
        Color color = new Color(120, 240, 50);
        assertTrue(color1.equals(color));
        assertFalse(color1.equals(color2));
        assertTrue(color1.equals((Object) color));
        assertTrue(color2.equals(named));
        assertFalse(color1.equals("Turtles"));
        assertTrue(color2.equals((Object) named));
    }

    @Test
    public void approximatelyEquals() {
        assertTrue(color1.approximatelyEquals(color2));
        assertFalse(color1.approximatelyEquals(color3));
    }

    @Test
    public void approximatelyEqualsDifference() {
        assertFalse(color1.approximatelyEquals(color2, 20));
        assertTrue(color1.approximatelyEquals(color3, 300));
    }

    @Test
    public void approximatelyEqualsNamed() {
        assertTrue(color1.approximatelyEquals(named));
        assertFalse(color3.approximatelyEquals(named));
    }

    @Test
    public void approximatelyEqualsNamedDifference() {
        assertFalse(color1.approximatelyEquals(named, 20));
        assertTrue(color3.approximatelyEquals(named, 300));
    }

    @Test
    public void difference() {
        assertEquals(35, color1.difference(color2));
        assertEquals(35, color2.difference(color1));
    }
}