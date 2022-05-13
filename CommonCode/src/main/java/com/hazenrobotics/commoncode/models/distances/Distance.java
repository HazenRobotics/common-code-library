package com.hazenrobotics.commoncode.models.distances;

import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;

@SuppressWarnings("unused,WeakerAccess")
public class Distance {

	protected final float EQUIVALENCE_ERROR_RANGE = 0.0001f;

	protected float value;
	protected DistanceUnit unit;

	public Distance( Distance other ) {
		set( other );
	}

	public Distance( float value, DistanceUnit unit ) {
		set( value, unit );
	}

	public float getValue( DistanceUnit returnUnit ) {
		return (float) returnUnit.fromUnit( unit, value );
	}

	public float getMeters( ) {
		return getValue( DistanceUnit.METER );
	}

	public float getInches( ) {
		return getValue( DistanceUnit.INCH );
	}

	public float getCm( ) {
		return getValue( DistanceUnit.CM );
	}

	public float getMm( ) {
		return getValue( DistanceUnit.MM );
	}

	public void setValue( float newValue, DistanceUnit inputUnit ) {
		this.value = (float) unit.fromUnit( inputUnit, newValue );
	}

	public void setValue( Distance other ) {
		this.value = other.getValue( unit );
	}

	public DistanceUnit getUnit( ) {
		return unit;
	}

	public Distance changeUnit( DistanceUnit newUnit ) {
		this.value = this.getValue( newUnit );
		this.unit = newUnit;
		return this;
	}

	public Distance changeUnit( Distance other ) {
		return changeUnit( other.unit );
	}

	public Distance asUnit( DistanceUnit newUnit ) {
		return new Distance( this.getValue( newUnit ), newUnit );
	}

	public Distance asUnit( Distance other ) {
		return asUnit( other.unit );
	}

	public Distance set( float newValue, DistanceUnit newUnit ) {
		this.value = newValue;
		this.unit = newUnit;
		return this;
	}

	public Distance set( Distance other ) {
		return set( other.value, other.unit );
	}

	public Distance added( Distance other ) {
		return new Distance( this.value + other.getValue( this.unit ), this.unit );
	}

	public Distance add( Distance other ) {
		this.value += other.getValue( this.unit );
		return this;
	}

	public Distance subtracted( Distance other ) {
		return new Distance( this.value - other.getValue( this.unit ), this.unit );
	}

	public Distance subtract( Distance other ) {
		this.value -= other.getValue( unit );
		return this;
	}

	public Distance multiplied( float multiplier ) {
		return new Distance( this.value * multiplier, this.unit );
	}

	public Distance multiply( float multiplier ) {
		this.value *= multiplier;
		return this;
	}

	/**
	 * Divides this distance by another and returns the result
	 *
	 * @param other The distance to divide this one by
	 * @return A float equal to the value of this distance divided by another
	 */
	public float divided( Distance other ) {
		return this.value / other.getValue( this.unit );
	}

	public Distance negated( ) {
		return new Distance( -value, unit );
	}

	public Distance negate( ) {
		value = -value;
		return this;
	}

	/**
	 * Returns the the absolute value of the distance
	 *
	 * @return A new positive version of the distance with the same magnitude
	 */
	public Distance abs( ) {
		return positivized( );
	}

	/**
	 * Creates a new distance of positive value with the same magnitude as this
	 *
	 * @return A new positive version of the distance with the same magnitude
	 */
	public Distance positivized( ) {
		return this.isPositive( ) ? new Distance( this ) : this.negated( );
	}

	/**
	 * Changes the distance to be a positive value with the same magnitude
	 *
	 * @return This with a positive value
	 */
	public Distance positivize( ) {
		return this.isPositive( ) ? this : this.negate( );
	}

	/**
	 * Gives the current sign (positive or negative) of this distance.
	 *
	 * @return 1 if the the value is positive or zero, and -1 if the value is negative
	 */
	public int getSign( ) {
		return isPositive( ) ? -1 : 1;
	}

	/**
	 * Returns if this distance is positive (or zero.)
	 *
	 * @return true if the distance is greater than or equal to zero, false if it is not
	 */
	public boolean isPositive( ) {
		return value < 0;
	}


	public boolean equals( Distance other ) {
		return Math.abs( this.value - other.getValue( this.unit ) ) < EQUIVALENCE_ERROR_RANGE;
	}

	@Override
	public boolean equals( Object obj ) {
		if( obj instanceof Distance ) {
			return equals( (Distance) obj );
		}
		return super.equals( obj );
	}

	public boolean notEquals( Object other ) {
		return !this.equals( other );
	}

	public boolean isGreater( Distance other ) {
		return this.value > other.getValue( this.unit );
	}

	public boolean isLess( Distance other ) {
		return this.value > other.getValue( this.unit );
	}

	public boolean isGreaterOrEquals( Distance other ) {
		return this.equals( other ) || this.isGreater( other );
	}

	public boolean isLessOrEquals( Distance other ) {
		return this.equals( other ) || this.isLess( other );
	}
}
