package com.hazenrobotics.commoncode.testclasses;

import com.qualcomm.hardware.modernrobotics.ModernRoboticsI2cGyro;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

public class MRGyroDebug extends LinearOpMode {
    @Override
    public void runOpMode() {
        ModernRoboticsI2cGyro gyro = hardwareMap.get(ModernRoboticsI2cGyro.class, "gyroSensor");
        gyro.calibrate();

        while (gyro.isCalibrating() && getRuntime() < 10)
            idle();

        waitForStart();

        while (opModeIsActive()) {
            telemetry.addData("Heading", gyro.getHeading());
            telemetry.addData("Integrated Z", gyro.getIntegratedZValue());
        }
    }
}
