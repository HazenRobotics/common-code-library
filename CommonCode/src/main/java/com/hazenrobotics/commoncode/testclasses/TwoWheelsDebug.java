package com.hazenrobotics.commoncode.testclasses;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

/**
 * Class to individually debug and run both motors of a 2 motors
 * robot by using the y-axis of the right and left joy sticks of Gamepad 1
 * <br></br> Motors must be called in the 'directionLocation' name scheme e.g "left"
 * All wheel directions are set to forward. If the wheels move the wrong direction, you know that
 * the direction will need to be reverse on your final configuration.
 */
public class TwoWheelsDebug extends LinearOpMode {

    private String leftName;
    private String rightName;

    public TwoWheelsDebug() {
        this("left", "right");
    }

    public TwoWheelsDebug(String left, String right) {
        leftName = left;
        rightName = right;
    }

    @Override
    public void runOpMode() {
        DcMotor left = hardwareMap.dcMotor.get(leftName);
        DcMotor right = hardwareMap.dcMotor.get(rightName);

        left.setDirection(DcMotorSimple.Direction.FORWARD);
        right.setDirection(DcMotorSimple.Direction.FORWARD);

        waitForStart();

        while (opModeIsActive()) {
            left.setPower(-gamepad1.left_stick_y);
            right.setPower(-gamepad1.right_stick_y);
            telemetry.addData("Left Power:", " " + left.getPower());
            telemetry.addData("Right Power:", " " + right.getPower());
            telemetry.update();
            idle();
        }
    }
}