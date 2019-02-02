package com.hazenrobotics.commoncode.movement;


import com.hazenrobotics.commoncode.models.angles.directions.RotationDirection;
import com.hazenrobotics.commoncode.models.angles.directions.SimpleDirection;
import com.hazenrobotics.commoncode.models.conditions.Condition;

/**
 * A means to control a robot's wheels and movement
 * @see EncoderWheels
 */
@SuppressWarnings("unused")
public interface Wheels {
    /**
     * Moves directly forward until a given condition is true without any turning
     * @param condition Will move until this condition is true
     * @param direction Determines if the wheels will move forward or backwards
     */
	void move(Condition condition, SimpleDirection direction);

    /**
     * Turns the robot in a given direction until a condition is true
     * @param condition Will turn until this condition is true
     * @param direction Determines what direction the turn will be towards
     */
	void turn(Condition condition, RotationDirection direction);
}
