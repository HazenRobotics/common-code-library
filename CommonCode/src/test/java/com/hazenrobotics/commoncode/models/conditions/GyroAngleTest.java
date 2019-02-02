package com.hazenrobotics.commoncode.models.conditions;

import com.hazenrobotics.commoncode.models.angles.Angle;
import com.hazenrobotics.commoncode.models.angles.AngleUnit;
import com.hazenrobotics.commoncode.models.angles.directions.RotationDirection;
import com.hazenrobotics.commoncode.sensors.GyroSensor;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class GyroAngleTest {

    private Angle currentAngle;

    private GyroAngle gyroAngle1;
    private GyroAngle gyroAngle2;

    @Before
    public void setUp() {
        GyroSensor sensor = new GyroSensor() {
            @Override
            public Angle getIntegratedZ() {
                return currentAngle;
            }

            @Override
            public Angle getHeading() {
                return currentAngle.normalized();
            }

            @Override
            public void calibrate() {
                resetHeading();
            }

            @Override
            public void resetHeading() {
                currentAngle = new Angle(0, AngleUnit.DEGREES);
            }

            @Override
            public boolean isCalibrating() {
                return false;
            }
        };
        sensor.calibrate();

        gyroAngle1 = new GyroAngle(new Angle(90, AngleUnit.DEGREES), sensor, RotationDirection.CLOCKWISE);
        gyroAngle2 = new GyroAngle(new Angle(90, AngleUnit.DEGREES), sensor, RotationDirection.COUNTER_CLOCKWISE);
    }

    @Test
    public void getTargetAngle() {
        //The direction was set to clockwise, so the angle should be negative
        assertEquals(new Angle(-90, AngleUnit.DEGREES), gyroAngle1.getTargetAngle());

        //The direction was set to counter-clockwise, so the angle should be positive
        assertEquals(new Angle(90, AngleUnit.DEGREES), gyroAngle2.getTargetAngle());
    }

    @Test
    public void getTargetAngleValue() {
        //The direction was set to clockwise, so the angle should be negative
        assertEquals(-90, gyroAngle1.getTargetAngleValue(AngleUnit.DEGREES), 0.05f);

        //The direction was set to counter-clockwise, so the angle should be positive
        assertEquals(90, gyroAngle2.getTargetAngleValue(AngleUnit.DEGREES), 0.05f);
    }

    @Test
    public void getTargetHeading() {
        //The direction was set to clockwise, so the angle should be negative,
        //meaning that the heading will wrap around to 270 (360 - 90)
        assertEquals(new Angle(270, AngleUnit.DEGREES), gyroAngle1.getTargetHeading());

        //The direction was set to counter-clockwise, so the angle should be positive and the heading the same
        assertEquals(new Angle(90, AngleUnit.DEGREES), gyroAngle2.getTargetHeading());
    }

    @Test
    public void getTargetHeadingValue() {
        //The direction was set to clockwise, so the angle should be negative,
        //meaning that the heading will wrap around to 270 (360 - 90)
        assertEquals(270, gyroAngle1.getTargetHeadingValue(AngleUnit.DEGREES), 0.05f);

        //The direction was set to counter-clockwise, so the angle should be positive and the heading the same
        assertEquals((90), gyroAngle2.getTargetHeadingValue(AngleUnit.DEGREES), 0.05f);
    }

    @Test
    public void getAngleRemaining() {
        currentAngle = new Angle(45, AngleUnit.DEGREES);
        assertEquals(new Angle(-135, AngleUnit.DEGREES), gyroAngle1.getAngleRemaining());
        assertEquals(new Angle(45, AngleUnit.DEGREES), gyroAngle2.getAngleRemaining());
    }

    @Test
    public void getAngleRemainingValue() {
        currentAngle = new Angle(45, AngleUnit.DEGREES);
        assertEquals(-135, gyroAngle1.getAngleRemainingValue(AngleUnit.DEGREES), 0.05f);
        assertEquals(45, gyroAngle2.getAngleRemainingValue(AngleUnit.DEGREES), 0.05f);
    }

    @Test
    public void condition() {
        currentAngle = new Angle(90, AngleUnit.DEGREES);
        assertFalse(gyroAngle1.condition());
        assertTrue(gyroAngle2.condition());

        currentAngle.setValue(-45, AngleUnit.DEGREES);
        assertFalse(gyroAngle1.condition());
        assertFalse(gyroAngle2.condition());

        currentAngle.setValue(-90, AngleUnit.DEGREES);
        assertTrue(gyroAngle1.condition());
        assertFalse(gyroAngle2.condition());
    }
}