package com.hazenrobotics.commoncode.testclasses;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

/**
 * Class to individually debug and run each motor of a 4 motor
 * robot by using the y-axi of the right and left joy sticks of  Gamepad 1 and Gamepad 2
 * <br></br> Motors must be called in the 'directionLocation' name scheme e.g "leftFront"
 * All wheel directions are set to forward. If the wheels move the wrong direction, you know that
 * the direction will need to be reverse on your final configuration.
 */
public class FourWheelsDebug extends LinearOpMode {
    @Override
    public void runOpMode() {
        DcMotor leftF = hardwareMap.dcMotor.get("leftFront");
        DcMotor leftB = hardwareMap.dcMotor.get("leftBack");
        DcMotor rightF = hardwareMap.dcMotor.get("rightFront");
        DcMotor rightB = hardwareMap.dcMotor.get("rightBack");

        leftF.setDirection(DcMotorSimple.Direction.FORWARD);
        leftB.setDirection(DcMotorSimple.Direction.FORWARD);
        rightF.setDirection(DcMotorSimple.Direction.FORWARD);
        rightB.setDirection(DcMotorSimple.Direction.FORWARD);

        waitForStart();

        while (opModeIsActive()) {
            leftF.setPower(-gamepad1.left_stick_y);
            leftB.setPower(-gamepad2.left_stick_y);
            rightF.setPower(-gamepad1.right_stick_y);
            rightB.setPower(-gamepad2.right_stick_y);
            telemetry.addData("Left Forward Power:", " " + leftF.getPower());
            telemetry.addData("Right Forward Power:", " " + rightF.getPower());
            telemetry.addData("Left Backward Power:", " " + leftB.getPower());
            telemetry.addData("Right Backward Power:", " " + rightB.getPower());
            telemetry.update();
            idle();
        }
    }
}