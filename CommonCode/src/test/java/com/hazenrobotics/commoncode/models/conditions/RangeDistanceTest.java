package com.hazenrobotics.commoncode.models.conditions;

import com.hazenrobotics.commoncode.models.distances.Distance;
import com.hazenrobotics.commoncode.sensors.RangeSensor;

import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class RangeDistanceTest {

    private RangeDistance rangeDistance1;
    private RangeDistance rangeDistance2;
    private Distance currentDistance;

    @Before
    public void setUp() {
        RangeSensor sensor = new RangeSensor() {
            @Override
            public Distance getRange() {
                return currentDistance;

            }
        };

        rangeDistance1 = new RangeDistance(new Distance(5, DistanceUnit.INCH), sensor, true);
        rangeDistance2 = new RangeDistance(new Distance(5, DistanceUnit.INCH), sensor, false);
    }

    @Test
    public void getTargetDistance() {
        assertEquals(new Distance(5, DistanceUnit.INCH), rangeDistance1.getTargetDistance());

    }

    @Test
    public void getTargetDistanceValue() {
        assertEquals(5.0f, rangeDistance1.getTargetDistanceValue(DistanceUnit.INCH), 0.05f);
    }

    @Test
    public void getDistanceRemaining() {
        currentDistance = new Distance(1, DistanceUnit.INCH);
        assertEquals(new Distance(4, DistanceUnit.INCH), rangeDistance1.getDistanceRemaining());
    }

    @Test
    public void getDistanceRemainingValue() {
        currentDistance = new Distance(1, DistanceUnit.INCH);
        assertEquals(4, rangeDistance1.getDistanceRemainingValue(DistanceUnit.INCH), 0.05f);
    }

    @Test
    public void condition() {
        currentDistance = new Distance(5.5f, DistanceUnit.INCH);
        //1 should be true because we told it to check for having a greater distance and (5.5 > 5)
        assertTrue(rangeDistance1.condition());
        //2 should be false for the reverser reason (we told it to move not move greater than)
        assertFalse(rangeDistance2.condition());


        currentDistance.setValue(4.5f, DistanceUnit.INCH);
        //1 should be false because we told it to check for having a greater distance and !(4.5 < 5)
        assertFalse(rangeDistance1.condition());
        //2 should be true for the reverser reason (we told it to move not move greater than)
        assertTrue(rangeDistance2.condition());
    }
}