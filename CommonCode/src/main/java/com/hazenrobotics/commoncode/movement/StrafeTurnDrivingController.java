package com.hazenrobotics.commoncode.movement;

import com.qualcomm.robotcore.hardware.Gamepad;

/**
 * A Driving Controller for Mecanum Wheels where the left stick controls the the forward-back and
 * left-right strafing movement, and the x-axis on the right stick controls turning.
 * @see MecanumWheels
 */
public class StrafeTurnDrivingController implements DrivingController {

    protected Gamepad controller;
    protected MecanumWheels wheels;
    protected float speed;

    /**
     * Create a driving controller with the specified wheels and controller input
     * @param wheels The Mecanum wheels which will drive
     * @param controller The input controller which will movement direction and power
     */
    public StrafeTurnDrivingController(MecanumWheels wheels, Gamepad controller) {
        this.controller = controller;
        this.wheels = wheels;
        speed = 1f;
    }

    /**
     * Changes the input controller
     * @param controller The new controller to be used
     * @return False if the controller did not change, otherwise true
     */
    public boolean setController(Gamepad controller) {
        if(this.controller == controller)
            return false;
        this.controller = controller;
        return true;
    }

    /**
     * Changes the max movement speed
     * @param maxSpeed The new max movement speed
     */
    public void setSpeed(float maxSpeed) {
        this.speed = speed;
    }

    @Override
    public void updateMotion() {
        float turn_x = controller.right_stick_x; //Stick that determines how far robot is turning
        float x = controller.left_stick_x;
        float y = -controller.left_stick_y; //Up on the stick is negative, so the value is negated to correct for this

        //Used to determine the greatest possible value of y +/- x to scale them
        float magnitude = Math.abs(y) + Math.abs(x) + Math.abs(turn_x);
        //Used to prevent setting motor to power over 1
        float scale = Math.max(1, magnitude);

        MecanumWheels.Coefficients wheelCoefficients = new MecanumWheels.Coefficients();

        //Algorithm for calculating the power that is set to the wheels
        wheelCoefficients.leftFront = (y + x + turn_x) / scale;
        wheelCoefficients.rightFront = (y - x - turn_x) / scale;
        wheelCoefficients.leftBack = (y - x + turn_x) / scale;
        wheelCoefficients.rightBack = (y + x - turn_x) / scale;

        wheels.setPower(wheelCoefficients, speed);
    }

    @Override
    public void stopMotion() {
        wheels.stop();
    }
}
