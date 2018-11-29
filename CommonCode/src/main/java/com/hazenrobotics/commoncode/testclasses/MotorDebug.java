package com.hazenrobotics.commoncode.testclasses;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

/**
 * Can individual debug and run a motor with the y-axis of the left stick on Gamepad 1
 * <br></br> The motor is configured by default to just be named "motor", however other names can be
 *  provided thought the constructor.
 *  The motor direction by default is set to forwards, so the direction might need to be reversed on
 *  the final configuration if the motor is moving the wrong way
 */
public class MotorDebug extends LinearOpMode {

    private String motorName;

    public MotorDebug() {
        this("motor");
    }

    public MotorDebug(String name) {
        motorName = name;
    }

    @Override
    public void runOpMode() {
        DcMotor motor = hardwareMap.dcMotor.get(motorName);

        motor.setDirection(DcMotorSimple.Direction.FORWARD);

        waitForStart();

        while (opModeIsActive()) {
            motor.setPower(-gamepad1.left_stick_y);

            telemetry.addData("Power:", " " + motor.getPower());
            telemetry.update();
        }
    }
}
