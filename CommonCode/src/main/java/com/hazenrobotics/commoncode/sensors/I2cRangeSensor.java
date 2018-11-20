package com.hazenrobotics.commoncode.sensors;

import com.hazenrobotics.commoncode.models.distances.Distance;
import com.qualcomm.robotcore.hardware.I2cAddr;
import com.qualcomm.robotcore.hardware.I2cDevice;

import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;

/**
 * An I2c range sensor object which can determine the distance to an object in front of it in various
 * units using either ultrasonic or optical sensing.
 */
@SuppressWarnings("unused,WeakerAccess")
public class I2cRangeSensor extends I2cSensor implements RangeSensor {
    protected static final DistanceMode DEFAULT_DISTANCE_MODE = DistanceMode.ULTRASONIC;
    protected DistanceMode mode;
    protected static final int DEFAULT_ADDRESS = 0x28;
    protected static final int RANGE_REG_START = 0x04; //Register to start reading
    protected static final int RANGE_READ_LENGTH = 2; //Number of byte to read
    protected static final DistanceUnit SENSOR_DISTANCE_UNIT = DistanceUnit.CM;
    protected static final DistanceUnit DEFAULT_DISTANCE_RETURN_UNIT = DistanceUnit.INCH;

    @Override
    public Distance getRange() {
        return (mode == DistanceMode.ULTRASONIC ? getUltrasonic() : getOptical());
    }

    /**
     * Creates a sensor with an address of {@link #DEFAULT_ADDRESS}
     * @param sensorDevice The sensor object on the hardware map to be used
     */
    public I2cRangeSensor(I2cDevice sensorDevice) {
        this(sensorDevice, I2cAddr.create8bit(DEFAULT_ADDRESS));
    }

    /**
     * Creates a sensor with the given address
     * @param address The address which references the given sensor
     * @param sensorDevice The sensor object on the hardware map to be used
     */
    public I2cRangeSensor(I2cDevice sensorDevice, I2cAddr address) {
        super(sensorDevice, address);
        mode = DEFAULT_DISTANCE_MODE;
    }

    /**
     * Creates a sensor with the given address
     * @param address The address which references the given sensor
     * @param sensorDevice The sensor object on the hardware map to be used
     */
    public I2cRangeSensor(I2cDevice sensorDevice, int address) {
        this(sensorDevice, I2cAddr.create8bit(address));
    }

    /**
     * Determines the distance in front of the sensor to the nearest object using ultrasonic sensing
     * @return The distance value (in cm) to the object
     */
    public int getUltrasonicValue() {
        return sensorReader.read(RANGE_REG_START, RANGE_READ_LENGTH)[0] & 0xFF;
    }

    /**
     * Determines the distance in front of the sensor to the nearest object using ultrasonic sensing
     * @param returnUnit The unit to return the distance value in
     * @return The distance value (in the specified unit) to the object
     */
    public float getUltrasonicValue(DistanceUnit returnUnit) {
        return (float) returnUnit.fromUnit(SENSOR_DISTANCE_UNIT, getUltrasonicValue());
    }

    /**
     * Determines the distance in front of the sensor to the nearest object using ultrasonic sensing
     * @return The distance to the object
     */
    public Distance getUltrasonic() {
        return getUltrasonic(DEFAULT_DISTANCE_RETURN_UNIT);
    }

    /**
     * Determines the distance in front of the sensor to the nearest object using ultrasonic sensing
     * @param returnUnit The unit to return the distance as
     * @return The distance to the object in the specified units
     */
    public Distance getUltrasonic(DistanceUnit returnUnit) {
        return new Distance(getUltrasonicValue(returnUnit), returnUnit);
    }

    /**
     * Determines the distance in front of the sensor to the nearest object using optical sensing
     * @return The distance value (in cm) to the object
     */
    public double getOpticalValue() {
        return sensorReader.read(RANGE_REG_START, RANGE_READ_LENGTH)[1] & 0xFF;
    }

    /**
     * Determines the distance in front of the sensor to the nearest object using optical sensing
     * @param returnUnit The unit to return the distance value in
     * @return The distance value (in the specified unit) to the object
     */
    public float getOpticalValue(DistanceUnit returnUnit) {
        return (float) returnUnit.fromUnit(SENSOR_DISTANCE_UNIT, getOpticalValue());
    }

    /**
     * Determines the distance in front of the sensor to the nearest object using optical sensing
     * @return The distance to the object
     */
    public Distance getOptical() {
        return getOptical(DEFAULT_DISTANCE_RETURN_UNIT);
    }

    /**
     * Determines the distance in front of the sensor to the nearest object using optical sensing
     * @param returnUnit The unit to return the distance as
     * @return The distance to the object in the specified units
     */
    public Distance getOptical(DistanceUnit returnUnit) {
        return new Distance(getOpticalValue(returnUnit), returnUnit);
    }

    public void setDistanceMode(DistanceMode mode) {
        this.mode = mode;
    }

    public enum DistanceMode {
        ULTRASONIC,
        OPTICAL
    }
}
