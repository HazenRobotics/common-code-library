package com.hazenrobotics.commoncode.models.angles.directions;

import com.hazenrobotics.commoncode.models.angles.Angle;
import com.hazenrobotics.commoncode.models.angles.NormalizedAngleUnit;

public enum SideDirection implements Direction {
    RIGHT(new Angle(90f, NormalizedAngleUnit.DEGREES)),
    LEFT(new Angle(270f, NormalizedAngleUnit.DEGREES));

    protected Angle angle;
    SideDirection(Angle angle) {
        this.angle = angle;
    }

    @Override
    public Angle getAngle() {
        return angle;
    }
}