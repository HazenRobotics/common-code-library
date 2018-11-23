package com.hazenrobotics.commoncode.models.distances;

import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class DistanceTest {

    private Distance distance1;
    private Distance distance2;
    private Distance distance3;

    @Before
    public void setUp() {
        distance1 = new Distance(12, DistanceUnit.INCH);
        distance2 = new Distance(1.5f, DistanceUnit.METER);
        distance3 = new Distance(-10, DistanceUnit.CM);
    }

    @Test
    public void getValue() {
        float value1 = distance1.getValue(DistanceUnit.INCH);
        assertEquals(12, value1, 0.01f);
        float value2 = distance2.getValue(DistanceUnit.INCH);
        assertEquals(1.5f * 39.3701f, value2, 0.01f);
    }

    @Test
    public void setDistance() {
        distance1.setDistance(120, DistanceUnit.MM);
        assertEquals(120, distance1.getValue(DistanceUnit.MM), 0.01f);
    }

    @Test
    public void setDistanceOther() {
        distance1.setDistance(distance2);
        assertEquals(150, distance1.getValue(DistanceUnit.CM), 0.01f);
    }

    @Test
    public void getMeters() {
        assertEquals(-0.1, distance3.getMeters(), 0.01f);
    }

    @Test
    public void getInches() {
        assertEquals(-10f * (39.3701f / 100f), distance3.getInches(), 0.1f);
    }

    @Test
    public void getCm() {
        assertEquals(150, distance2.getCm(), 0.1f);
    }

    @Test
    public void getMm() {
        assertEquals(-100, distance3.getMm(), 0.1f);
    }

    @Test
    public void constructors() {
        Distance copy = new Distance(distance1);
        assertEquals(12, distance1.getInches(), 0.01f);
        assertEquals(12, copy.getInches(), 0.01f);
    }

    @Test
    public void setValue() {
        Distance distance = new Distance(140, DistanceUnit.CM);
        distance.setValue(-12, DistanceUnit.MM);
        assertEquals(-1.2f, distance.getCm(), 0.01f);
    }

    @Test
    public void setValueOther() {
        Distance distance = new Distance(140, DistanceUnit.CM);
        distance.setValue(new Distance(-12, DistanceUnit.MM));
        assertEquals(-1.2f, distance.getCm(), 0.01f);
    }

    @Test
    public void added() {
        Distance result = distance2.added(distance3);
        assertEquals(1.5f * 100 + (-10), result.getCm(), 0.01f);
    }

    @Test
    public void add() {
        distance2.add(distance3);
        assertEquals(1.5f * 100 + (-10), distance2.getCm(), 0.01f);
    }

    @Test
    public void subtracted() {
        Distance result = distance2.subtracted(distance3);
        assertEquals(1.5f * 100 - (-10), result.getCm(), 0.01f);
    }

    @Test
    public void subtract() {
        distance2.subtract(distance3);
        assertEquals(1.5f * 100 - (-10), distance2.getCm(), 0.01f);
    }

    @Test
    public void multiplied() {
        Distance result = distance1.multiplied(3);
        assertEquals(12 * 3, result.getInches(), 0.01f);
    }

    @Test
    public void multiply() {
        distance1.multiply(3);
        assertEquals(12 * 3, distance1.getInches(), 0.01f);
    }

    @Test
    public void divided() {
        float result = distance2.divided(distance3);
        assertEquals(150f / -10f, result, 0.01f);
    }

    @Test
    public void negated() {
        Distance result = distance1.negated();
        assertEquals(-12, result.getInches(), 0.01f);
    }

    @Test
    public void negate() {
        distance1.negate();
        assertEquals(-12, distance1.getInches(), 0.01f);
    }

    @Test
    public void abs() {
        Distance result = distance3.abs();
        assertEquals(10, result.getCm(), 0.01f);
    }

    @Test
    public void positivized() {
        Distance result = distance3.positivized();
        assertEquals(10, result.getCm(), 0.01f);
    }

    @Test
    public void positivize() {
        distance3.positivize();
        assertEquals(10, distance3.getCm(), 0.01f);
    }

    @Test
    public void getSign() {
        assertEquals(-1, distance3.getSign());
        assertEquals(1, distance1.getSign());
    }

    @Test
    public void isPositive() {
        assertTrue(distance1.isPositive());
        assertFalse(distance3.isPositive());
    }

    @Test
    public void equals() {
        Distance other = new Distance(-100, DistanceUnit.MM);
        assertTrue(distance3.equals(other));
        assertFalse(distance2.equals(other));
    }

    @Test
    public void equalsDefault() {
        Distance other = new Distance(-100, DistanceUnit.MM);
        assertTrue(distance3.equals((Object) other));
        assertFalse(distance3.equals("Turtles"));
    }

    @Test
    public void notEquals() {
        assertTrue(distance1.notEquals("other"));
        assertFalse(distance1.notEquals(distance1));
    }

    @Test
    public void isGreater() {
        assertTrue(distance1.isGreater(distance3));
        assertFalse(distance1.isGreater(distance2));
    }

    @Test
    public void isLess() {
        assertFalse(distance1.isLess(distance3));
        assertTrue(distance1.isLess(distance2));
    }

    @Test
    public void isGreaterOrEquals() {
        Distance other = new Distance(-100, DistanceUnit.MM);
        assertTrue(distance3.isGreaterOrEquals(other));
        assertTrue(distance1.isGreaterOrEquals(distance3));
        assertFalse(distance1.isGreaterOrEquals(distance2));
    }

    @Test
    public void isLessOrEquals() {
        Distance other = new Distance(-100, DistanceUnit.MM);
        assertTrue(distance3.isGreaterOrEquals(other));
        assertFalse(distance1.isLessOrEquals(distance3));
        assertTrue(distance1.isLessOrEquals(distance2));
    }

    @Test
    public void toStringTest() {
        assertEquals("12.0 " + DistanceUnit.INCH.toString(), distance1.toString());
    }
}