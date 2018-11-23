package com.hazenrobotics.commoncode.models.angles;

@SuppressWarnings("unused,WeakerAccess")
public class Angle {
    public static final float EQUIVALENCE_ERROR_RANGE = 0.0001f;

    protected float value;
    protected AngleUnit unit;
    protected boolean isNormalized;

    /**
     * Creates a new angle that is a copy of the given angle, using the same units and value
     * @param other The other angle from which to create a new angle from
     */
    public Angle(Angle other) {
        setAngle(other);
    }

    /**
     * Creates a new angle of the given value (in the given units)
     * @param value The value of the angle
     * @param unit The units for the specified value
     */
    public Angle(float value, AngleUnit unit) {
        setAngle(value, unit);
    }

    /**
     * Sets the unit and the angle value to new values (which may change if the angle is normalized
     * or not)
     * @param newValue The angle to set this angle to
     * @param newUnit The angle unit for the new value and to set for this angle
     * @return This with new value and unit
     * @see Angle#setAngle(Angle)
     */
    public Angle setAngle(float newValue, AngleUnit newUnit) {
        this.unit = newUnit;
        isNormalized = this.unit instanceof NormalizedAngleUnit;
        this.value = isNormalized ? this.unit.normalize(newValue) : newValue;
        return this;
    }

    /**
     * Sets the unit and the angle value to be the same as the other angle (which may change if the
     * angle is normalized or not)
     * @param other The angle to set this angle to
     * @return This with new value and unit from other angle
     * @see Angle#setAngle(float, AngleUnit)
     */
    public Angle setAngle(Angle other) {
        return setAngle(other.value, other.unit);
    }

    /**
     * Gets the value of the angle converted to the desired unit
     * @param returnUnit The unit to return the value of the angle in
     * @return The value of the angle
     */
    public float getValue(AngleUnit returnUnit) {
        return returnUnit.fromUnit(unit, value);
    }

    /**
     * Gets the value of the angle in radians
     * @return The value of the angle in radians
     */
    public float getRadians() {
        return getValue(UnnormalizedAngleUnit.RADIANS);
    }

    /**
     * Gets the value of the angle converted to degrees
     * @return The value of the angle in degrees
     */
    public float getDegrees() {
        return getValue(UnnormalizedAngleUnit.DEGREES);
    }

    /**
     * Changes the value of the angle to the new given value in the given units (WITHOUT changing
     * the internal units or the normalization status of this angle)
     * @param newValue The new value for the angle
     * @param inputUnit The units of the new angle value
     */
    public void setValue(float newValue, AngleUnit inputUnit) {
        value = unit.fromUnit(inputUnit, newValue);
    }

    /**
     * Changes the value of the angle to be the same as the given angle (WITHOUT changing the
     * internal units or the normalization status of this angle)
     * @param other The angle from which to copy the value from
     */
    public void setValue(Angle other) {
        value = other.getValue(unit);
    }

    /**
     * Gets the unit under which the angle is stored as
     * @return The angle unit used for the angle
     */
    @Deprecated
    public AngleUnit getUnit() {
        return unit;
    }

    /**
     * Makes the unit of the stored angle value the new unit type and converts the angle value to
     * the new unit.
     * @param newUnit The new unit type of the angle
     * @return This angle in the new units
     * @see Angle#changeUnit(Angle)
     */
    @Deprecated
    public Angle changeUnit(AngleUnit newUnit) {
        this.value = this.getValue(newUnit);
        this.unit = newUnit;
        this.isNormalized = this.unit instanceof NormalizedAngleUnit;
        return this;
    }

    /**
     * Makes the unit of the stored angle value the same unit as the given angle and converts the
     * angle value to the new unit.
     * @param other The angle from which the unit type will be copied
     * @return This angle in the new units
     * @see Angle#changeUnit(AngleUnit)
     */
    @Deprecated
    public Angle changeUnit(Angle other) {
        return changeUnit(other.unit);
    }

    /**
     * Makes a copy of the angle in the specified units
     * @param newUnit The units for the new copy of the angle
     * @return Copy of angle in the new units
     */
    @Deprecated
    public Angle asUnit(AngleUnit newUnit) {
        return new Angle(this.getValue(newUnit), newUnit);
    }

    /**
     * Makes a copy of the angle in a new units based on the unit of the given angle
     * @param other An angle with the same unit type for the new copy of the angle
     * @return Copy of angle in the new units
     */
    @Deprecated
    public Angle asUnit(Angle other) {
        return asUnit(other.unit);
    }

    /**
     * Makes a new angle whose value is the sum of this and the other angle with the same units as
     * this angle
     * @param other The angle to add with this
     * @return The angle result from the summation
     * @see Angle#add(Angle)
     */
    public Angle added(Angle other) {
        return new Angle(this.value + other.getValue(this.unit), this.unit);
    }

    /**
     * Adds another angle's value to this angle
     * @param other The angle to add to this
     * @return This angle
     * @see Angle#added(Angle)
     */
    public Angle add(Angle other) {
        this.value += other.getValue(this.unit);
        return this;
    }

    /**
     * Makes a new angle whose value is the difference between this and the other angle with the
     * same units as this angle
     * @param other The angle to subtract from this
     * @return The angle result from the difference
     * @see Angle#subtract(Angle)
     */
    public Angle subtracted(Angle other) {
        return new Angle(this.value - other.getValue(this.unit), this.unit);
    }

    /**
     * Subtracts another angle's value from this angle
     * @param other The angle to add to this
     * @return This angle
     * @see Angle#subtracted(Angle)
     */
    public Angle subtract(Angle other) {
        this.value -= other.getValue(this.unit);
        return this;
    }

    /**
     * Makes a new angle (of the same units) whose value is multiplied by a given modifier (If this
     * angle is normalized, normalization will be applied to the new angle)
     * @param multiplier The scalar by which to multiple the value of the angle
     * @return The new scaled angle
     */
    public Angle multiplied(float multiplier) {
        return new Angle(this.value * multiplier, this.unit);
    }

    /**
     * Multiplies the value of the angle by a given modifier (If this angle is normalized,
     * normalization will be applied to the new value)
     * @param multiplier The scalar by which to multiple the value of this angle
     * @return This angle with its value scaled
     */
    public Angle multiply(float multiplier) {
        this.value *= multiplier;
        return this;
    }

    /**
     * Divides this angle by another and returns the result
     * @param other The angle to divide this one by
     * @return A float equal to the value of this angle divided by another, will return 0 if the
     * other angle is 0
     */
    public float divided(Angle other) {
        float otherValue = other.getValue(this.unit);
        return otherValue != 0 ? this.value / otherValue : 0;
    }

    /**
     * Creates an angle whose value is the negative value of this angle
     * @return A new negative version of the angle with an unnormalized unit type
     */
    public Angle negated() {
        return new Angle(-this.value, this.unit.getUnnormalized());
    }

    /**
     * Makes the angle's value negative of its previous
     * @return This with an unnormalized unit type
     */
    public Angle negate() {
        this.unnormalize();
        this.value = -this.value;
        return this;
    }

    /**
     * Returns the the absolute value of the angle
     * @return A new positive version of the angle with the same magnitude
     */
    public Angle abs() {
        return positivized();
    }

    /**
     * Creates a new angle of positive value with the same magnitude as this
     * @return A new positive version of the angle with the same magnitude
     */
    public Angle positivized() {
        return this.isPositive() ? new Angle(this) : this.negated();
    }

    /**
     * Changes the angle to be a positive value with the same magnitude
     * @return This with a positive value
     */
    public Angle positivize() {
        return this.isPositive() ? this : this.negate();
    }

    /**
     * Gives the current sign (positive or negative) of this angle.
     * @return 1 if the the value is positive or zero, and -1 if the value is negative
     */
    public int getSign() {
        return isPositive() ? 1 : -1;
    }

    /**
     * Returns if this angle is positive (or zero.)
     * @return true if the angle is greater than or equal to zero, false if it is not
     */
    public boolean isPositive() {
        return value >= 0;
    }

    /**
     * Gets the normalization status of the is angle, which is if the value is limited to the range
     * of one rotation: 0 to 360 degrees or 0 to 2π radians
     * @return If the unit of this angle is a {@link NormalizedAngleUnit}
     */
    public boolean isNormalized() {
        return isNormalized;
    }

    /**
     * Makes an angle of equivalent direction to this within the range of one rotation: 0 to 360
     * degrees or 0 to 2π radians
     * @return Equivalent angle in range of 0 to 360 or 0 to 2π
     */
    public Angle normalized() {
        return isNormalized ? new Angle(this) : new Angle(this.value, this.unit.getNormalized());
    }

    /**
     * Changes this angle to one of equivalent direction within the range of one rotation: 0 to 360
     * degrees or 0 to 2π radians
     * @return This as an equivalent angle in range of 0 to 360 or 0 to 2π
     */
    public Angle normalize() {
        return !isNormalized ? changeUnit(unit.getNormalized()) : this;
    }

    /**
     * Makes an angle of equivalent value, but is not normalized to the range of one rotation: 0 to
     * 360 degrees or 0 to 2π radians
     * @return Equivalent angle that is not normalized
     */
    public Angle unnormalized() {
        return !isNormalized ? new Angle(this) : new Angle(this.value, this.unit.getUnnormalized());
    }

    /**
     * Changes this angle to have the same value, but not be normalized to the range of one
     * rotation: 0 to 360 degrees or 0 to 2π radians
     * @return This angle without normalization
     */
    public Angle unnormalize() {
        return isNormalized ? changeUnit(unit.getUnnormalized()) : this;
    }

    /**
     * Compares two angles to see if the are equal within a range of error
     * @param other The angle to compare with this
     * @return If the values of the angles are the same (once converted to the same
     * unit) within a {@link Angle#EQUIVALENCE_ERROR_RANGE range of error}
     */
    public boolean equals(Angle other) {
        return Math.abs(this.value - other.getValue(this.unit.getUnnormalized())) < EQUIVALENCE_ERROR_RANGE;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Angle) {
            return equals((Angle) obj);
        }
        return super.equals(obj);
    }

    /**
     * Compares if two angles have the same equivalent value: that they represent the same direction
     * around a point (The angles have the same value when bounded to a 0 to 360 degrees or
     * 0 to 2π radians range)
     * @param other The angle to check equivalence with this one
     * @return True if both this angle's and the other angle's {@link Angle#normalized()
     * normalized angle} are {@link Angle#equals(Angle) equal}
     */
    public boolean isEquivalent(Angle other) {
        return (isNormalized ? this : this.normalized()).equals(other.isNormalized ? other : other.normalized());
    }

    /**
     * Checks if this angle is not equal to the given angle
     * @param other The angle to compare to
     * @return If !{@link Angle#equals equals(other)} for the given angle
     */
    public boolean notEquals(Object other) {
        return !this.equals(other);
    }

    /**
     * Checks if this angle is greater than the given angle's value (The other angle's value is
     * compared after normalization if this angle is normalized)
     * @param other The angle to compare to
     * @return If the value of this angle is greater than the other angle's value (when both
     * converted to this angle's units)
     */
    public boolean isGreater(Angle other) {
        return this.value > other.getValue(this.unit);
    }

    /**
     * Checks if this angle is less than the given angle's value (The other angle's value is
     * compared after normalization if this angle is normalized)
     * @param other The angle to compare to
     * @return If the value of this angle is less than the other angle's value (when both
     * converted to this angle's units)
     */
    public boolean isLess(Angle other) {
        return this.value < other.getValue(this.unit);
    }

    /**
     * Checks if this angle is greater than or equal to the given other angle
     * @param other The angle to compare to
     * @return If the value of this angle is greater than or equal to the other angle's value (when
     * both converted to this angle's units)
     */
    public boolean isGreaterOrEquals(Angle other) {
        return this.equals(other) || this.isGreater(other);
    }

    /**
     * Checks if this angle is less than or equal to the given other angle
     * @param other The angle to compare to
     * @return If the value of this angle is less than or equal to the other angle's value (when
     * both converted to this angle's units)
     */
    public boolean isLessOrEquals(Angle other) {
        return this.equals(other) || this.isLess(other);
    }
    
    @Override
    public String toString() {
        return value + " " + unit;
    }

}
