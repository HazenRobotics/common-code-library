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
    public void setUp() throws Exception {
        RangeSensor sensor = new RangeSensor() {
            @Override
            public Distance getRange() {
                return currentDistance;

            }
        };

        rangeDistance1 = new RangeDistance(new Distance(5,DistanceUnit.INCH) ,sensor, true);
        rangeDistance2 = new RangeDistance(new Distance(5,DistanceUnit.INCH) ,sensor, false);
    }

    @Test
    public void getTargetDistance() {
        assertEquals(new Distance(5,DistanceUnit.INCH), rangeDistance1.getTargetDistance());

    }

    @Test
    public void getTargetDistanceValue() {
        assertEquals(5.0f, rangeDistance1.getTargetDistanceValue(DistanceUnit.INCH), 0.05f);

    }

    @Test
    public void getDistanceRemaining() {
        currentDistance = new Distance(1,DistanceUnit.INCH);
        assertEquals(new Distance(4,DistanceUnit.INCH),rangeDistance1.getDistanceRemaining());
    }

    @Test
    public void getDistanceRemainingValue() {
    }

    @Test
    public void condition() {
    }
}