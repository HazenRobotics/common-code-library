package com.hazenrobotics.commoncode.sensors;

import com.hazenrobotics.commoncode.models.angles.Angle;

public interface GyroSensor {

    /**
     * Gets the current integrated Z value held by the sensor, which counts up or down as you rotate,
     * as an Angle
     * @return The Integrated Z value as an Angle in the
     */
    Angle getIntegratedZ();

    /**
     * Gets the current heading value held by the sensor, which loops back to 0 after you make one
     * full rotation, as an Angle
     * @return The Heading value as an Angle in the
     */
    Angle getHeading();

    /**
     * Calibrates the Gyro, which may take some time to fully complete; consider checking {@link #isCalibrating()}
     * after.
     */
    void calibrate();

    /**
     * Resets the Gyro's Z heading to zero
     */
    void resetHeading();

    /**
     * Checks if the gyroDevice is still calibrating, either by a full calibration or heading reset
     * @return If the Gyro is currently calibrating
     */
    boolean isCalibrating();
}
