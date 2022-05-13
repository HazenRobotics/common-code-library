package com.hazenrobotics.commoncode.models.conditions;

import com.hazenrobotics.commoncode.models.distances.Distance;
import com.hazenrobotics.commoncode.sensors.I2cRangeSensor;

import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;

/**
 * Range is a condition type which {@link #isTrue()} when the {@link #rangeSensor} associated with the condition either detects is is greater than or less than a certain {@link #distance}
 */
public class RangeDistance extends Condition {

	protected final Distance distance;
	protected final I2cRangeSensor rangeSensor;
	protected final boolean moveGreater;

	/**
	 * Creates a range condition that moves the specified distance in the specified units
	 *
	 * @param distance    The target distance for the condition to be true
	 * @param rangeSensor The range sensor which will be used to check the condition
	 * @param moveGreater If the condition will be true when the sensor reads greater than the target distance, or less than
	 */
	public RangeDistance( Distance distance, I2cRangeSensor rangeSensor, boolean moveGreater ) {
		this.distance = distance;
		this.rangeSensor = rangeSensor;
		this.moveGreater = moveGreater;
	}

	/**
	 * Returns the target distance threshold for the condition to be true
	 *
	 * @return The target distance
	 */
	public Distance getTargetDistance( ) {
		return distance;
	}

	/**
	 * Returns the target distance threshold for the condition to be true
	 *
	 * @param returnUnit The unit type for the distance to be returned in
	 * @return The target distance
	 */
	public Distance getTargetDistance( DistanceUnit returnUnit ) {
		return distance.asUnit( returnUnit );
	}

	/**
	 * Returns the target distance threshold value for the condition to be true
	 *
	 * @param returnUnit The unit type for the distance value to be returned in
	 * @return The target distance value
	 */
	public float getTargetDistanceValue( DistanceUnit returnUnit ) {
		return distance.getValue( returnUnit );
	}

	/**
	 * Returns the distance remaining to pass the target threshold
	 *
	 * @return The distance remaining
	 */
	public Distance getDistanceRemaining( ) {
		return distance.subtracted( rangeSensor.getUltrasonic( ) );
	}

	/**
	 * Returns the distance remaining to pass the target threshold
	 *
	 * @param returnUnit The unit type for the distance to be returned in
	 * @return The distance remaining
	 */
	public Distance getDistanceRemaining( DistanceUnit returnUnit ) {
		return getDistanceRemaining( ).changeUnit( returnUnit );
	}

	/**
	 * Returns the distance remaining to pass the target threshold
	 *
	 * @param returnUnit The unit type for the distance to be returned in
	 * @return The distance remaining
	 */
	public float getDistanceRemainingValue( DistanceUnit returnUnit ) {
		return getDistanceRemaining( ).getValue( returnUnit );
	}

	/**
	 * Checks if the target {@link #distance} has been reached.
	 *
	 * @return If reached target distance
	 */
	@Override
	protected boolean condition( ) {
		Distance currentDistance = rangeSensor.getUltrasonic( );
		return moveGreater ? currentDistance.isGreaterOrEquals( distance ) : currentDistance.isLessOrEquals( distance );
	}
}