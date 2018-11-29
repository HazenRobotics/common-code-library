package com.hazenrobotics.commoncode.testclasses;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

/**
 * Class to individually debug and run each motor of a 4 motor
 * robot by using the y-axis of the right and left joy sticks of Gamepad 1 and Gamepad 2
 * <br></br> Motors must be called in the 'directionLocation' name scheme e.g "leftFront"
 * All wheel directions are set to forward. If the wheels move the wrong direction, you know that
 * the direction will need to be reverse on your final configuration.
 */
public class FourWheelsDebug extends LinearOpMode {

    private String leftFrontName;
    private String leftBackName;
    private String rightFrontName;
    private String rightBackName;

    public FourWheelsDebug() {
        this("leftFront", "leftBack", "rightFront", "rightBack");
    }

    public FourWheelsDebug(String leftFront, String leftBack, String rightFront, String rightBack) {
        leftFrontName = leftFront;
        leftBackName = leftBack;
        rightFrontName = rightFront;
        rightBackName = rightBack;
    }

    @Override
    public void runOpMode() {
        DcMotor leftFront = hardwareMap.dcMotor.get(leftFrontName);
        DcMotor leftBack = hardwareMap.dcMotor.get(leftBackName);
        DcMotor rightFront = hardwareMap.dcMotor.get(rightFrontName);
        DcMotor rightBack = hardwareMap.dcMotor.get(rightBackName);

        leftFront.setDirection(DcMotorSimple.Direction.FORWARD);
        leftBack.setDirection(DcMotorSimple.Direction.FORWARD);
        rightFront.setDirection(DcMotorSimple.Direction.FORWARD);
        rightBack.setDirection(DcMotorSimple.Direction.FORWARD);

        waitForStart();

        while (opModeIsActive()) {
            leftFront.setPower(-gamepad1.left_stick_y);
            leftBack.setPower(-gamepad2.left_stick_y);
            rightFront.setPower(-gamepad1.right_stick_y);
            rightBack.setPower(-gamepad2.right_stick_y);
            telemetry.addData("Left Forward Power:", " " + leftFront.getPower());
            telemetry.addData("Right Forward Power:", " " + rightFront.getPower());
            telemetry.addData("Left Backward Power:", " " + leftBack.getPower());
            telemetry.addData("Right Backward Power:", " " + rightBack.getPower());
            telemetry.update();
            idle();
        }
    }
}