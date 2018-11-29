package com.hazenrobotics.commoncode.testclasses;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.Servo;

/**
 * Can individually run and set the position of a servo by the movement of the y-axis of the left
 * stick on Gamepad 1
 * <bt> </bt> The servo is configured by default to just be named "servo", however other names can
 * be provided thought the constructor.
 */
public class ServoDebug extends LinearOpMode {

    private String servoName;

    public ServoDebug() {
        this("servo");
    }

    public ServoDebug(String name) {
        servoName = name;
    }

    @Override
    public void runOpMode() {
        Servo servo = hardwareMap.servo.get(servoName);

        waitForStart();

        while (opModeIsActive()) {
            servo.setPosition(Math.abs(gamepad1.left_stick_y));

            telemetry.addData("Position:", " " + servo.getPosition());
            telemetry.update();
        }
    }
}
