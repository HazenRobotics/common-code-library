package com.hazenrobotics.commoncode.models.distances;

import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;

@SuppressWarnings("unused,WeakerAccess")
public class Distance {
    protected final float EQUIVALENCE_ERROR_RANGE = 0.0001f;

    protected float value;
    protected DistanceUnit unit;

    /**
     * Creates a new distance that is a copy of the given distance, using the same units and value
     * @param other The other distance from which to create a new distance from
     */
    public Distance(Distance other) {
        setDistance(other);
    }

    /**
     * Creates a new distance of the given value (in the given units)
     * @param value The value of the distance
     * @param unit The units for the specified value
     */
    public Distance(float value, DistanceUnit unit) {
        setDistance(value, unit);
    }

    /**
     * Sets the unit and the distance value to new values
     * @param newValue The distance to set this distance to
     * @param newUnit The distance unit for the new value and to set for this distance
     * @return This with new value and unit
     * @see Distance#setDistance(Distance)
     */
    public Distance setDistance(float newValue, DistanceUnit newUnit) {
        this.value = newValue;
        this.unit = newUnit;
        return this;
    }

    /**
     * Sets the unit and the distance value to be the same as the other distance
     * @param other The distance to set this distance to
     * @return This with new value and unit from other distance
     * @see Distance#setDistance(float, DistanceUnit)
     */
    public Distance setDistance(Distance other) {
        return setDistance(other.value, other.unit);
    }

    /**
     * Gets the value of the distance converted to the desired unit
     * @param returnUnit The unit to return the value of the distance in
     * @return The value of the distance
     */
    public float getValue(DistanceUnit returnUnit) {
        return (float) returnUnit.fromUnit(unit, value);
    }

    /**
     * Gets the value of the distance in meters
     * @return The value of the distance in meters
     */
    public float getMeters() {
        return getValue(DistanceUnit.METER);
    }

    /**
     * Gets the value of the distance in inches
     * @return The value of the distance in inches
     */
    public float getInches() {
        return getValue(DistanceUnit.INCH);
    }

    /**
     * Gets the value of the distance in centimeters
     * @return The value of the distance in centimeters
     */
    public float getCm() {
        return getValue(DistanceUnit.CM);
    }

    /**
     * Gets the value of the distance in millimeters
     * @return The value of the distance in millimeters
     */
    public float getMm() {
        return getValue(DistanceUnit.MM);
    }

    /**
     * Changes the value of the distance to the new given value in the given units (without changing
     * the internal units of this distance)
     * @param newValue The new value for the distance
     * @param inputUnit The units of the new distance value
     */
    public void setValue(float newValue, DistanceUnit inputUnit) {
        this.value = (float) unit.fromUnit(inputUnit, newValue);
    }

    /**
     * Changes the value of the distance to be the same as the given distance (without changing the
     * internal units of this distance)
     * @param other The distance from which to copy the value from
     */
    public void setValue(Distance other) {
        this.value = other.getValue(unit);
    }

    /**
     * Gets the unit under which the distance is stored as
     * @return The distance unit used for the distance
     */
    @Deprecated
    public DistanceUnit getUnit() {
        return unit;
    }

    /**
     * Makes the unit of the stored distance value the new unit type and converts the distance value
     * to the new unit
     * @param newUnit The new unit type of the distance
     * @return This distance in the new units
     * @see Distance#changeUnit(Distance)
     */
    @Deprecated
    public Distance changeUnit(DistanceUnit newUnit) {
        this.value = this.getValue(newUnit);
        this.unit = newUnit;
        return this;
    }

    /**
     * Makes the unit of the stored distance value the same unit as the given distance and converts
     * the distance value to the new unit
     * @param other The distance from which the unit type will be copied
     * @return This distance in the new units
     * @see Distance#changeUnit(DistanceUnit)
     */
    @Deprecated
    public Distance changeUnit(Distance other) {
        return changeUnit(other.unit);
    }

    /**
     * Makes a copy of the distance in the specified units
     * @param newUnit The units for the new copy of the distance
     * @return Copy of distance in the new units
     */
    @Deprecated
    public Distance asUnit(DistanceUnit newUnit) {
        return new Distance(this.getValue(newUnit), newUnit);
    }

    /**
     * Makes a copy of the distance in a new units based on the unit of the given distance
     * @param other An distance with the same unit type for the new copy of the distance
     * @return Copy of distance in the new units
     */
    @Deprecated
    public Distance asUnit(Distance other) {
        return asUnit(other.unit);
    }

    /**
     * Makes a new distance whose value is the sum of this and the other distance with the same units as
     * this distance
     * @param other The distance to add with this
     * @return The distance result from the summation
     * @see Distance#add(Distance)
     */
    public Distance added(Distance other) {
        return new Distance(this.value + other.getValue(this.unit), this.unit);
    }

    /**
     * Adds another distance's value to this distance
     * @param other The distance to add to this
     * @return This distance
     * @see Distance#added(Distance)
     */
    public Distance add(Distance other) {
        this.value += other.getValue(this.unit);
        return this;
    }

    /**
     * Makes a new distance whose value is the difference between this and the other distance with
     * the same units as this distance
     * @param other The distance to subtract from this
     * @return The distance result from the difference.
     * @see Distance#subtract(Distance)
     */
    public Distance subtracted(Distance other) {
        return new Distance(this.value - other.getValue(this.unit), this.unit);
    }

    /**
     * Subtracts another distance's value from this distance
     * @param other The distance to add to this
     * @return This distance
     * @see Distance#subtracted(Distance)
     */
    public Distance subtract(Distance other) {
        this.value -= other.getValue(unit);
        return this;
    }

    /**
     * Makes a new distance (of the same units) whose value is multiplied by a given modifier
     * @param multiplier The scalar by which to multiple the value of the distance
     * @return The new scaled distance
     */
    public Distance multiplied(float multiplier) {
        return new Distance(this.value * multiplier, this.unit);
    }

    /**
     * Multiplies the value of the distance by a given modifier
     * @param multiplier The scalar by which to multiple the value of this distance
     * @return This distance with its value scaled
     */
    public Distance multiply(float multiplier) {
        this.value *= multiplier;
        return this;
    }

    /**
     * Divides this distance by another and returns the result
     * @param other The distance to divide this one by
     * @return A float equal to the value of this distance divided by another, will return 0 if the
     * other distance is 0
     */
    public float divided(Distance other) {
        float otherValue = other.getValue(this.unit);
        return otherValue != 0 ? this.value / otherValue : 0;
    }

    /**
     * Creates an distance whose value is the negative value of this distance
     * @return A new negative version of the distance
     */
    public Distance negated() {
        return new Distance(-value, unit);
    }

    /**
     * Makes the distance's value negative of its previous
     * @return This with its values negative their previous
     */
    public Distance negate() {
        value = -value;
        return this;
    }

    /**
     * Returns the the absolute value of the distance
     * @return A new positive version of the distance with the same magnitude
     */
    public Distance abs() {
        return positivized();
    }

    /**
     * Creates a new distance of positive value with the same magnitude as this
     * @return A new positive version of the distance with the same magnitude
     */
    public Distance positivized() {
        return this.isPositive() ? new Distance(this) : this.negated();
    }

    /**
     * Changes the distance to be a positive value with the same magnitude
     * @return This with a positive value
     */
    public Distance positivize() {
        return this.isPositive() ? this : this.negate();
    }

    /**
     * Gives the current sign (positive or negative) of this distance.
     * @return 1 if the the value is positive or zero, and -1 if the value is negative
     */
    public int getSign() {
        return isPositive() ? 1 : -1;
    }

    /**
     * Returns if this distance is positive (or zero.)
     * @return true if the distance is greater than or equal to zero, false if it is not
     */
    public boolean isPositive() {
        return value >= 0;
    }

    /**
     * Compares two distances to see if the are equal within a range of error
     * @param other The distance to compare with this
     * @return If the values of the distances are the same (once converted to the same
     * unit) within a {@link Distance#EQUIVALENCE_ERROR_RANGE range of error}
     */
    public boolean equals(Distance other) {
        return Math.abs(this.value - other.getValue(this.unit)) < EQUIVALENCE_ERROR_RANGE;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Distance) {
            return equals((Distance) obj);
        }
        return super.equals(obj);
    }

    /**
     * Checks if this distance is not equal to the given distance
     * @param other The distance to compare to
     * @return If !{@link Distance#equals equals(other)} for the given distance
     */
    public boolean notEquals(Object other) {
        return !this.equals(other);
    }

    /**
     * Checks if this distance is greater than the given distance's value
     * @param other The distance to compare to
     * @return If the value of this distance is greater than the other angle's value (when both
     * converted to this distance's units)
     */
    public boolean isGreater(Distance other) {
        return this.value > other.getValue(this.unit);
    }

    /**
     * Checks if this distance is less than the given distance's value
     * @param other The distance to compare to
     * @return If the value of this distance is less than the other distance's value (when both
     * converted to this distance's units)
     */
    public boolean isLess(Distance other) {
        return this.value < other.getValue(this.unit);
    }

    /**
     * Checks if this distance is greater than or equal to the given other distance
     * @param other The distance to compare to
     * @return If the value of this distance is greater than or equal to the other distance's value
     * (when both converted to this distance's units)
     */
    public boolean isGreaterOrEquals(Distance other) {
        return this.equals(other) || this.isGreater(other);
    }

    /**
     * Checks if this distance is less than or equal to the given other distance
     * @param other The distance to compare to
     * @return If the value of this distance is less than or equal to the other distance's value
     * (when both converted to this distance's units)
     */
    public boolean isLessOrEquals(Distance other) {
        return this.equals(other) || this.isLess(other);
    }

    @Override
    public String toString() {
        return value + " " + unit;
    }
}
