package com.hazenrobotics.commoncode.models.angles;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class AngleTest {

    private Angle angle1;
    private Angle angle2;
    private Angle angle3;

    @Before
    public void setUp() {
        angle1 = new Angle(45, AngleUnit.DEGREES);
        angle2 = new Angle((float) Math.PI, AngleUnit.RADIANS);
        angle3 = new Angle(-120, AngleUnit.DEGREES);
    }

    @Test
    public void getValue() {
        float value1 = angle1.getValue(AngleUnit.DEGREES);
        assertEquals(45, value1, 0.01f);
        float value2 = angle2.getValue(AngleUnit.DEGREES);
        assertEquals(180, value2, 0.01f);
    }

    @Test
    public void setAngle() {
        angle1.setAngle(120, AngleUnit.DEGREES);
        assertEquals(120, angle1.getValue(AngleUnit.DEGREES), 0.01f);
    }

    @Test
    public void setAngleOther() {
        angle1.setAngle(angle2);
        assertEquals(180, angle1.getValue(AngleUnit.DEGREES), 0.01f);
    }

    @Test
    public void getRadians() {
        assertEquals(Math.PI / 4, angle1.getRadians(), 0.01f);
    }

    @Test
    public void getDegrees() {
        assertEquals(180, angle2.getDegrees(), 0.01f);
    }

    @Test
    public void constructors() {
        Angle copy = new Angle(angle1);
        assertEquals(45, angle1.getDegrees(), 0.01f);
        assertEquals(45, copy.getDegrees(), 0.01f);
    }

    @Test
    public void setValue() {
        Angle angle = new Angle(140, NormalizedAngleUnit.DEGREES);
        angle.setValue(-60, AngleUnit.DEGREES);
        assertEquals(300, angle.getValue(AngleUnit.DEGREES), 0.01f);
    }

    @Test
    public void setValueOther() {
        Angle angle = new Angle(140, NormalizedAngleUnit.DEGREES);
        angle.setValue(new Angle(-60, AngleUnit.DEGREES));
        assertEquals(300, angle.getValue(AngleUnit.DEGREES), 0.01f);
    }

    @Test
    public void added() {
        Angle result = angle1.added(angle2);
        assertEquals(45 + 180, result.getDegrees(), 0.01f);
    }

    @Test
    public void add() {
        angle1.add(angle2);
        assertEquals(45 + 180, angle1.getDegrees(), 0.01f);
    }

    @Test
    public void subtracted() {
        Angle result = angle1.subtracted(angle2);
        assertEquals(45 - 180, result.getDegrees(), 0.01f);
    }

    @Test
    public void subtract() {
        angle1.subtract(angle2);
        assertEquals(45 - 180, angle1.getDegrees(), 0.01f);
    }

    @Test
    public void multiplied() {
        Angle result = angle1.multiplied(3);
        assertEquals(45 * 3, result.getDegrees(), 0.01f);
    }

    @Test
    public void multiply() {
        angle1.multiply(3);
        assertEquals(45 * 3, angle1.getDegrees(), 0.01f);
    }

    @Test
    public void divided() {
        float result = angle2.divided(angle1);
        assertEquals(180f / 45f, result, 0.01f);
    }

    @Test
    public void negated() {
        Angle result = angle1.negated();
        assertEquals(-45, result.getDegrees(), 0.01f);
    }

    @Test
    public void negate() {
        angle1.negate();
        assertEquals(-45, angle1.getDegrees(), 0.01f);
    }

    @Test
    public void abs() {
        Angle result = angle3.abs();
        assertEquals(120, result.getDegrees(), 0.01f);
    }

    @Test
    public void positivized() {
        Angle result = angle3.positivized();
        assertEquals(120, result.getDegrees(), 0.01f);
    }

    @Test
    public void positivize() {
        angle3.positivize();
        assertEquals(120, angle3.getDegrees(), 0.01f);
    }

    @Test
    public void getSign() {
        assertEquals(-1, angle3.getSign());
        assertEquals(1, angle1.getSign());
    }

    @Test
    public void isPositive() {
        assertTrue(angle1.isPositive());
        assertFalse(angle3.isPositive());
    }

    @Test
    public void isNormalized() {
        Angle normalAngle = new Angle(400, NormalizedAngleUnit.DEGREES);
        assertTrue(normalAngle.isNormalized());
        assertFalse(angle1.isNormalized());
    }

    @Test
    public void normalized() {
        Angle result = angle1.normalized();
        assertTrue(result.isNormalized);
    }

    @Test
    public void normalize() {
        angle1.normalize();
        assertTrue(angle1.isNormalized());
    }

    @Test
    public void unnormalized() {
        Angle normalAngle = new Angle(400, NormalizedAngleUnit.DEGREES);
        Angle result = normalAngle.unnormalized();
        assertFalse(result.isNormalized());
    }

    @Test
    public void unnormalize() {
        Angle result = new Angle(400, NormalizedAngleUnit.DEGREES);
        result.unnormalize();
        assertFalse(result.isNormalized());
    }

    @Test
    public void equals() {
        Angle other = new Angle(45, UnnormalizedAngleUnit.DEGREES);
        assertTrue(angle1.equals(other));
        assertFalse(angle2.equals(other));
    }

    @Test
    public void equalsDefault() {
        Angle other = new Angle(45, UnnormalizedAngleUnit.DEGREES);
        assertTrue(angle1.equals((Object) other));
        assertFalse(angle1.equals("Turtles"));
    }

    @Test
    public void isEquivalent() {
        Angle other = new Angle(405, AngleUnit.DEGREES);
        assertTrue(angle1.isEquivalent(other));
        assertFalse(angle1.isEquivalent(angle2));
    }

    @Test
    public void notEquals() {
        assertTrue(angle1.notEquals("other"));
        assertFalse(angle1.notEquals(angle1));
    }

    @Test
    public void isGreater() {
        assertTrue(angle1.isGreater(angle3));
        assertFalse(angle1.isGreater(angle2));
    }

    @Test
    public void isLess() {
        assertFalse(angle1.isLess(angle3));
        assertTrue(angle1.isLess(angle2));
    }

    @Test
    public void isGreaterOrEquals() {
        Angle other = new Angle((float) Math.PI / 4, AngleUnit.RADIANS);
        assertTrue(angle1.isGreaterOrEquals(other));
        assertTrue(angle1.isGreaterOrEquals(angle3));
        assertFalse(angle1.isGreaterOrEquals(angle2));
    }

    @Test
    public void isLessOrEquals() {
        Angle other = new Angle((float) Math.PI / 4, AngleUnit.RADIANS);
        assertTrue(angle1.isGreaterOrEquals(other));
        assertFalse(angle1.isLessOrEquals(angle3));
        assertTrue(angle1.isLessOrEquals(angle2));
    }

    @Test
    public void toStringTest() {
        assertEquals("45.0 " + AngleUnit.DEGREES.toString(), angle1.toString());
    }
}