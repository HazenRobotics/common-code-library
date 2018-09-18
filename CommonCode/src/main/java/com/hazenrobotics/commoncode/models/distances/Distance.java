package com.hazenrobotics.commoncode.models.distances;

import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;

public class Distance {
    protected final float EQUIVALENCE_ERROR_RANGE = 0.0001f;

    protected float value;
    protected DistanceUnit unit;

    public Distance(Distance other) {
        set(other);
    }

    public Distance(float value, DistanceUnit unit) {
        set(value, unit);
    }

    public float getValue(DistanceUnit returnUnit) {
        return (float) returnUnit.fromUnit(unit, value);
    }

    public float getMeters() {
        return getValue(DistanceUnit.METER);
    }

    public float getInches() {
        return getValue(DistanceUnit.INCH);
    }

    public float getCm() {
        return getValue(DistanceUnit.CM);
    }

    public float getMm() {
        return getValue(DistanceUnit.MM);
    }

    public void setValue(float newValue, DistanceUnit inputUnit) {
        this.value = (float) unit.fromUnit(inputUnit, newValue);
    }

    public void setValue(Distance other) {
        this.value = other.getValue(unit);
    }

    public DistanceUnit getUnit() {
        return unit;
    }

    public Distance changeUnit(DistanceUnit newUnit) {
        this.value = this.getValue(newUnit);
        this.unit = newUnit;
        return this;
    }

    public Distance changeUnit(Distance other) {
        return changeUnit(other.unit);
    }

    public Distance asUnit(DistanceUnit newUnit) {
        return new Distance(this.getValue(newUnit), newUnit);
    }

    public Distance asUnit(Distance other) {
        return asUnit(other.unit);
    }

    public Distance set(float newValue, DistanceUnit newUnit) {
        this.value = newValue;
        this.unit = newUnit;
        return this;
    }

    public Distance set(Distance other) {
        return set(other.value, other.unit);
    }

    public Distance added(Distance other) {
        return new Distance(this.value + other.getValue(this.unit), this.unit);
    }

    public Distance add(Distance other) {
        this.value += other.getValue(this.unit);
        return this;
    }

    public Distance subtracted(Distance other) {
        return new Distance(this.value - other.getValue(this.unit), this.unit);
    }

    public Distance subtract(Distance other) {
        this.value -= other.getValue(unit);
        return this;
    }

    public Distance negated() {
        return new Distance(-value, unit);
    }

    public Distance negate() {
        value = -value;
        return this;
    }

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

    public boolean notEquals(Object other) {
        return !this.equals(other);
    }

    public boolean isGreater(Distance other) {
        return this.value > other.getValue(this.unit);
    }

    public boolean isLess(Distance other) {
        return this.value > other.getValue(this.unit);
    }

    public boolean isGreaterOrEquals(Distance other) {
        return this.equals(other) || this.isGreater(other);
    }

    public boolean isLessOrEquals(Distance other) {
        return this.equals(other) || this.isLess(other);
    }
}
