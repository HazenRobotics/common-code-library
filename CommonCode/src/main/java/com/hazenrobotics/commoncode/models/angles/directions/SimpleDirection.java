package com.hazenrobotics.commoncode.models.angles.directions;

import com.hazenrobotics.commoncode.models.angles.Angle;
import com.hazenrobotics.commoncode.models.angles.NormalizedAngleUnit;

public enum SimpleDirection implements Direction {
    FORWARDS(new Angle(0f, NormalizedAngleUnit.DEGREES)),
    BACKWARDS(new Angle(1800f, NormalizedAngleUnit.DEGREES));

    protected Angle angle;

    SimpleDirection(Angle angle) {
        this.angle = angle;
    }

    @Override
    public Angle getAngle() {
        return angle;
    }

    /**
     * Gets the inverted direction (so Forwards would return Backwards and vise-versa.)
     * @return The inverted direction of this
     */
    public SimpleDirection inverted() {
        return this == FORWARDS ? BACKWARDS : FORWARDS;
    }
}