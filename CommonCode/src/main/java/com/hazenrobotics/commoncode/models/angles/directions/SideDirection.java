package com.hazenrobotics.commoncode.models.angles.directions;

import com.hazenrobotics.commoncode.models.angles.Angle;
import com.hazenrobotics.commoncode.models.angles.NormalizedAngleUnit;

@SuppressWarnings("unused")
public enum SideDirection implements Direction {
    RIGHT(new Angle(90f, NormalizedAngleUnit.DEGREES)),
    LEFT(new Angle(270f, NormalizedAngleUnit.DEGREES));

    protected Angle angle;

    SideDirection(Angle angle) {
        this.angle = angle;
    }

    @Override
    public Angle getAngle() {
        return new Angle(angle);
    }

    /**
     * Gets the inverted side direction (so Right would return Left and vise-versa.)
     * @return The inverted direction of this
     */
    public SideDirection inverted() {
        return this == RIGHT ? LEFT : RIGHT;
    }
}