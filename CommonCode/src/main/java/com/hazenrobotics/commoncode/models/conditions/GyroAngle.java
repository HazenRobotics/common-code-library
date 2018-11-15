package com.hazenrobotics.commoncode.models.conditions;

import com.hazenrobotics.commoncode.models.angles.Angle;
import com.hazenrobotics.commoncode.models.angles.AngleUnit;
import com.hazenrobotics.commoncode.models.angles.directions.RotationDirection;
import com.hazenrobotics.commoncode.models.angles.UnnormalizedAngleUnit;
import com.hazenrobotics.commoncode.sensors.GyroSensor;
import com.hazenrobotics.commoncode.sensors.I2cGyroSensor;

/**
 * GyroTurn is a condition type which {@link #isTrue()} when the {@link #gyroSensor} associated with
 * the condition either detects is is greater than or less than a certain {@link #targetAngle}
 */
public class GyroAngle extends Condition {
    protected final Angle targetAngle;
    protected final GyroSensor gyroSensor;
    protected final RotationDirection direction;

    /**
     * Creates a GyroTurn condition that turns the specified angle in degrees in either the clockwise
     * or counter-clockwise direction
     * @param angle The angle to turn by
     * @param gyroSensor The gyroDevice sensor which will be used to check the condition
     * @param direction The direction of turning the condition is meant for
     */
    public GyroAngle(Angle angle, I2cGyroSensor gyroSensor, RotationDirection direction) {
        this(angle, gyroSensor, direction, false);
    }

    /**
     * Creates a GyroTurn condition that turns the specified angle in degrees in either the clockwise
     * or counter-clockwise direction
     * @param angle The angle to turn by or move to
     * @param gyroSensor The gyroDevice sensor which will be used to check the condition
     * @param direction The direction of turning the condition is meant for
     * @param absoluteHeading If an absolute or relative angle heading will be used
     */
    public GyroAngle(Angle angle, GyroSensor gyroSensor, RotationDirection direction, boolean absoluteHeading) {
        this.gyroSensor = gyroSensor;
        this.direction = direction;
        boolean goingClockwise = direction.equals(RotationDirection.CLOCKWISE);
        if (absoluteHeading) {
            Angle heading = gyroSensor.getHeading();
            Angle deltaAngle = (goingClockwise ?
                    (heading.isGreater(angle.normalized())) : (heading.isLess(angle.normalized()))) //If ahead of where the angle is (for whichever direction we are going)
                    ? new Angle(360f, UnnormalizedAngleUnit.DEGREES)                          //Then add 360 to do one circle around,
                    : new Angle(0f, UnnormalizedAngleUnit.DEGREES)                            //Otherwise start with no rotation
                    .subtracted(heading.subtracted(angle));                                         //and subtract the difference in angle to hit the spot behind the current position if ahead of it,
            //or move forward the difference if it is in front of the current position
            this.targetAngle = gyroSensor.getIntegratedZ().added(goingClockwise ? deltaAngle : deltaAngle.negated());
        } else {
            this.targetAngle = gyroSensor.getIntegratedZ().added(goingClockwise ? angle : angle.negated());
        }
    }

    /**
     * Returns the target angle threshold for the condition to be true
     * @return The target angle
     */
    public Angle getTargetAngle() {
        return new Angle(targetAngle); //Return as copy so that target angle cant be modified
    }

    /**
     * Returns the target angle threshold for the condition to be true
     * @param returnUnit The unit type for the angle value to be returned as
     * @return The target angle value in the specified unit type
     */
    public float getTargetAngleValue(AngleUnit returnUnit) {
        return targetAngle.getValue(returnUnit);
    }

    /**
     * Returns the target heading angle threshold for the condition to be true
     * @return The target heading
     */
    public Angle getTargetHeading() {
        return targetAngle.normalized();
    }

    /**
     * Returns the target heading angle threshold for the condition to be true
     * @param returnUnit The unit type for the angle value to be returned as
     * @return The target heading in the specified unit type
     */
    public float getTargetHeadingValue(AngleUnit returnUnit) {
        return targetAngle.normalized().getValue(returnUnit);
    }

    /**
     * Returns the angle remaining to reach the target angle
     * @return The change in angle left
     */
    public Angle getAngleRemaining() {
        return targetAngle.subtracted(gyroSensor.getIntegratedZ());
    }

    /**
     * Returns the value of angle remaining to reach the target angle
     * @param returnUnit The unit type of the angle value to be returned
     * @return The value of the change in angle left in the specified unit type
     */
    public float getAngleRemainingValue(AngleUnit returnUnit) {
        return getAngleRemaining().getValue(returnUnit);
    }

    /**
     * Checks if the {@link #targetAngle} has been reached.
     * @return If reached target angle
     */
    @Override
    protected boolean condition() {
        Angle currentAngle = gyroSensor.getIntegratedZ();
        return direction.equals(RotationDirection.CLOCKWISE)    //Check the direction
                ? currentAngle.isGreaterOrEquals(targetAngle)   //Check if we are past the target angle for whatever direction we are moving in
                : currentAngle.isLessOrEquals(targetAngle);
    }
}
