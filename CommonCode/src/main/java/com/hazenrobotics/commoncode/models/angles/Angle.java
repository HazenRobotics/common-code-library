package com.hazenrobotics.commoncode.models.angles;

public class Angle {
    protected final float EQUIVALENCE_ERROR_RANGE = 0.0001f;

    protected float value;
    protected AngleUnit unit;
    protected boolean isNormalized;

    public Angle(Angle other) {
        set(other);
    }

    public Angle(float value, AngleUnit unit) {
        set(value, unit);
    }

    public float getValue(AngleUnit returnUnit) {
        return returnUnit.fromUnit(unit, value);
    }

    public float getRadians() {
        return getValue(UnnormalizedAngleUnit.RADIANS);
    }

    public float getDegrees() {
        return getValue(UnnormalizedAngleUnit.RADIANS);
    }

    public void setValue(float newValue, AngleUnit inputUnit) {
        value = unit.fromUnit(inputUnit, newValue);
    }

    public void setValue(Angle other) {
        value = other.getValue(unit);
    }

    public AngleUnit getUnit() {
        return unit;
    }

    /**
     * Makes the unit of the stored angle value the new unit type and converts the angle value to
     * the new unit.
     * @param newUnit The new unit type of the angle
     * @return This angle in new units
     * @see Angle#changeUnit(Angle)
     */
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
     * @return This angle in new units
     * @see Angle#changeUnit(AngleUnit)
     */
    public Angle changeUnit(Angle other) {
        changeUnit(other.unit);
        return this;
    }

    /**
     * Makes a copy of the angle in the specified units
     * @param newUnit The units for the new copy of the angle
     * @return Copy of angle in new units
     */
    public Angle asUnit(AngleUnit newUnit) {
        return new Angle(this.getValue(newUnit), newUnit);
    }

    /**
     * Makes a copy of the angle in a new units based on the unit of the given angle
     * @param other An angle with the same unit type for the new copy of the angle
     * @return Copy of angle in new units
     */
    public Angle asUnit(Angle other) {
        return asUnit(other.getUnit());
    }

    /**
     * Sets the unit and the angle value to new values
     * @param newValue The angle to set this angle to
     * @param newUnit The angle unit for the new value and to set for this angle
     * @return This with new value and unit
     * @see Angle#set(Angle)
     */
    public Angle set(float newValue, AngleUnit newUnit) {
        this.unit = newUnit;
        isNormalized = this.unit instanceof NormalizedAngleUnit;
        this.value = this.unit.normalize(newValue);
        return this;
    }

    /**
     * Sets the unit and the angle value to be the same as the other angle
     * @param other The angle to set this angle to
     * @return This with new value and unit from other angle
     * @see Angle#set(float, AngleUnit)
     */
    public Angle set(Angle other) {
        return set(other.value, other.unit);
    }

    /**
     * Makes a new angle whose value is the sum of this and the other angle with the same units as
     * this angle.
     * @param other The angle to add with this
     * @return The angle result from the summation
     * @see Angle#add(Angle)
     */
    public Angle added(Angle other) {
        return new Angle(this.value + other.getValue(this.unit), this.unit);
    }

    /**
     * Adds another angle's value to this angle.
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
     * same units as this angle.
     * @param other The angle to subtract from this
     * @return The angle result from the difference.
     * @see Angle#subtract(Angle)
     */
    public Angle subtracted(Angle other) {
        return new Angle(this.value - other.getValue(this.unit), this.unit);
    }

    /**
     * Subtracts another angle's value from this angle.
     * @param other The angle to add to this
     * @return This angle
     * @see Angle#subtracted(Angle)
     */
    public Angle subtract(Angle other) {
        this.value -= other.getValue(this.unit);
        return this;
    }

    /**
     * Creates an angle whose value is the negative value of this angle
     * @return A new negitive version angle with an unnormalized unit type
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
        if (!isNormalized)
            changeUnit(unit.getNormalized());
        return this;
    }

    public Angle unnormalized() {
        return !isNormalized ? new Angle(this) : new Angle(this.value, this.unit.getUnnormalized());
    }

    public Angle unnormalize() {
        if (isNormalized)
            changeUnit(unit.getUnnormalized());
        return this;
    }

    public boolean equals(Angle other) {
        return Math.abs(this.value - other.getValue(this.unit)) < EQUIVALENCE_ERROR_RANGE;
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
    public boolean isEquivelent(Angle other) {
        return (isNormalized ? this : this.normalized()).equals(other.isNormalized ? other : other.normalized());
    }

    public boolean notEquals(Object other) {
        return !this.equals(other);
    }

    public boolean isGreater(Angle other) {
        return this.value > other.getValue(this.unit);
    }

    public boolean isLess(Angle other) {
        return this.value > other.getValue(this.unit);
    }

    public boolean isGreaterOrEquals(Angle other) {
        return this.equals(other) || this.isGreater(other);
    }

    public boolean isLessOrEquals(Angle other) {
        return this.equals(other) || this.isLess(other);
    }


}