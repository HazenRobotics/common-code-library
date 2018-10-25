package com.hazenrobotics.commoncode.sensors;

import com.hazenrobotics.commoncode.models.angles.Angle;
import com.hazenrobotics.commoncode.models.angles.AngleUnit;
import com.hazenrobotics.commoncode.models.angles.UnnormalizedAngleUnit;
import com.qualcomm.robotcore.hardware.I2cAddr;
import com.qualcomm.robotcore.hardware.I2cDevice;
import com.qualcomm.robotcore.util.TypeConversion;

import java.nio.ByteOrder;

/**
 * An I2c gyro sensor object which can determine its normalized and summative heading compared to
 * when it was last {@link #calibrate() calibrated}
 */
public class I2cGyroSensor extends I2cSensor {
    protected static final int DEFAULT_ADDRESS = 0x20;
    protected static final int HEADING_REG_START = 0x0;
    protected static final int INTEGRATED_Z_REG_START = 0x06;
    protected static final int GYRO_READ_LENGTH = 2;
    protected static final int CALIBRATE_REG_START = 0x4E;
    protected static final int RESET_HEADING_REG_START = 0x52;
    protected static final AngleUnit SENSOR_ANGLE_UNIT = UnnormalizedAngleUnit.DEGREES;
    protected static final AngleUnit DEFAULT_ANGLE_RETURN_UNIT = UnnormalizedAngleUnit.DEGREES;

    /**
     * Creates a sensor with an address of {@link #DEFAULT_ADDRESS}
     * @param sensorDevice The sensor object on the hardware map to be used
     */
    public I2cGyroSensor(I2cDevice sensorDevice) {
        super(sensorDevice, I2cAddr.create8bit((DEFAULT_ADDRESS)));
    }

    /**
     * Creates a sensor with the given address
     * @param address The address which references the given sensor
     * @param sensorDevice The sensor object on the hardware map to be used
     */
    public I2cGyroSensor(I2cDevice sensorDevice, I2cAddr address) {
        super(sensorDevice, address);
    }

    /**
     * Gets the current integrated Z value held by the sensor, which counts up or down as you
     * rotate.
     * @return The Integrated Z value in {@link I2cGyroSensor#SENSOR_ANGLE_UNIT Degrees} (the
     * unit the sensor reads in) with the same range as a short.
     * @see I2cGyroSensor#getIntegratedZ(AngleUnit)
     * @see I2cGyroSensor#getIntegratedZ()
     * @see I2cGyroSensor#getIntegratedZValue(AngleUnit)
     */
    public int getIntegratedZValue() {
        cache = sensorReader.read(INTEGRATED_Z_REG_START, GYRO_READ_LENGTH);
        return TypeConversion.byteArrayToInt(cache, ByteOrder.LITTLE_ENDIAN);
    }

    /**
     * Gets the current integrated Z value held by the sensor, which counts up or down as you rotate
     * @param returnUnit The unit type in which the angle value will be returned
     * @return The Integrated Z value in the specified units
     * @see I2cGyroSensor#getIntegratedZ(AngleUnit)
     * @see I2cGyroSensor#getIntegratedZ()
     * @see I2cGyroSensor#getIntegratedZValue()
     */
    public float getIntegratedZValue(AngleUnit returnUnit) {
        return returnUnit.fromUnit(SENSOR_ANGLE_UNIT, getIntegratedZValue());
    }

    /**
     * Gets the current integrated Z value held by the sensor, which counts up or down as you rotate,
     * as an Angle
     * @return The Integrated Z value as an Angle in the
     * {@link I2cGyroSensor#DEFAULT_ANGLE_RETURN_UNIT Default Return Unit}.
     * @see I2cGyroSensor#getIntegratedZ(AngleUnit)
     */
    public Angle getIntegratedZ() {
        return getIntegratedZ(DEFAULT_ANGLE_RETURN_UNIT);
    }

    /**
     * Gets the current integrated Z value held by the sensor, which counts up or down as you rotate,
     * as an Angle
     * @param returnUnit The unit type in which the angle will be returned
     * @return The Integrated Z value as an Angle in the specified unit
     * @see I2cGyroSensor#getIntegratedZ()
     * @see I2cGyroSensor#getIntegratedZValue(AngleUnit)
     */
    public Angle getIntegratedZ(AngleUnit returnUnit) {
        return new Angle(getIntegratedZValue(returnUnit), returnUnit);
    }

    /**
     * Gets the current heading value held by the sensor which loops back to 0 after you pass 360
     * degrees
     * @return The heading value of the gyroDevice in degrees, between 0 and 359
     */
    public int getHeadingValue() {
        cache = sensorReader.read(HEADING_REG_START, GYRO_READ_LENGTH);
        return TypeConversion.byteArrayToInt(cache, ByteOrder.LITTLE_ENDIAN);
    }

    /**
     * Gets the current heading value held by the sensor which loops back to 0 after you make one
     * full rotation
     * @param returnUnit The unit type in which the angle value will be returned
     * @return The heading value of the gyroDevice in the specified angle unit
     * @see I2cGyroSensor#getHeading(AngleUnit)
     * @see I2cGyroSensor#getHeading()
     */
    public float getHeadingValue(AngleUnit returnUnit) {
        return returnUnit.fromUnit(SENSOR_ANGLE_UNIT.getNormalized(), getHeadingValue());
    }

    /**
     * Gets the current heading value held by the sensor, which loops back to 0 after you make one
     * full rotation, as an Angle
     * @return The Heading value as an Angle in the
     * {@link I2cGyroSensor#DEFAULT_ANGLE_RETURN_UNIT Default Return Unit}.
     * @see I2cGyroSensor#getHeading(AngleUnit)
     * @see I2cGyroSensor#getHeadingValue(AngleUnit)
     */
    public Angle getHeading() {
        return getHeading(DEFAULT_ANGLE_RETURN_UNIT);
    }

    /**
     * Gets the current heading value held by the sensor, which loops back to 0 after you make one
     * full rotation, as an Angle
     * @param returnUnit The unit type in which the angle will be returned
     * @return The Heading value as an Angle in the
     * {@link I2cGyroSensor#DEFAULT_ANGLE_RETURN_UNIT Default Return Unit}.
     * @see I2cGyroSensor#getHeading()
     * @see I2cGyroSensor#getHeadingValue(AngleUnit)
     */
    public Angle getHeading(AngleUnit returnUnit) {
        return new Angle(getHeadingValue(returnUnit), returnUnit);
    }

    /**
     * Calibrates the Gyro, which may take some time to fully complete; consider checking {@link #isCalibrating()}
     * after.
     */
    public void calibrate() {
        sensorReader.write8(COMMAND_REG_START, CALIBRATE_REG_START);
    }

    /**
     * Resets the Gyro's Z heading to zero
     */
    public void resetHeading() {
        sensorReader.write8(COMMAND_REG_START, RESET_HEADING_REG_START);
    }

    /**
     * Checks if the gyroDevice is still calibrating, either by a full calibration or heading reset
     * @return If the Gyro is currently calibrating
     */
    public boolean isCalibrating() {
        return sensorReader.read(COMMAND_REG_START, 1)[0] != 0x00;
    }
}
