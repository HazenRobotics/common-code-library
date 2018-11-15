package com.hazenrobotics.commoncode.sensors;

import com.hazenrobotics.commoncode.interfaces.HardwareInterface;
import com.hazenrobotics.commoncode.models.angles.Angle;
import com.hazenrobotics.commoncode.models.angles.AngleUnit;
import com.hazenrobotics.commoncode.models.angles.UnnormalizedAngleUnit;
import com.qualcomm.hardware.modernrobotics.ModernRoboticsI2cGyro;
import com.qualcomm.robotcore.hardware.HardwareDevice;

public class InterfaceGyro implements GyroSensor {

    protected ModernRoboticsI2cGyro internalGyro;
    protected static AngleUnit INTERNAL_ANGLE_UNIT = UnnormalizedAngleUnit.DEGREES;

    InterfaceGyro(ModernRoboticsI2cGyro internalGyro) {
        this.internalGyro = internalGyro;
    }

    InterfaceGyro(HardwareInterface hardwareInterface, String gyroName) {
        HardwareDevice device = hardwareInterface.get(gyroName);
        if (device == null || !(device instanceof ModernRoboticsI2cGyro))
            throw new IllegalArgumentException("A Modern Robotics Gyro called \"" + gyroName + "\" does not exist");
        internalGyro = (ModernRoboticsI2cGyro) device;
    }

    @Override
    public Angle getIntegratedZ() {
        return new Angle(internalGyro.getIntegratedZValue(), INTERNAL_ANGLE_UNIT);
    }

    @Override
    public Angle getHeading() {
        return new Angle(internalGyro.getHeading(), INTERNAL_ANGLE_UNIT);
    }

    @Override
    public void calibrate() {
        internalGyro.calibrate();
    }

    @Override
    public void resetHeading() {
        internalGyro.resetZAxisIntegrator();
    }

    @Override
    public boolean isCalibrating() {
        return internalGyro.isCalibrating();
    }

    /**
     * Gives the internal Modern Robotics Gyro which is being used to find the heading, etc.
     * @return The internal sensor being used.
     */
    public ModernRoboticsI2cGyro getInternalGyro() {
        return internalGyro;
    }
}
