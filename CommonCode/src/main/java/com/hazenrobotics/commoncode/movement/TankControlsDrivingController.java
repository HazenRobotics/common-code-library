package com.hazenrobotics.commoncode.movement;

import com.qualcomm.robotcore.hardware.Gamepad;

/**
 * A Driving Controller for Two Wheels driven bots where the left stick y-axis controls the the left wheel
 * and the right stick y-axis controls the right wheel.
 * @see TwoWheels
 */
@SuppressWarnings("unused,WeakerAccess")
public class TankControlsDrivingController implements DrivingController {
    protected Gamepad controller;
    protected TwoWheels wheels;
    protected float speed;

    /**
     * Create a driving controller with the specified wheels and controller input
     * @param wheels The Two wheels which will drive
     * @param controller The input controller which will movement direction and power
     */
    public TankControlsDrivingController(TwoWheels wheels, Gamepad controller) {
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
        this.speed = maxSpeed;
    }

    @Override
    public void updateMotion() {
        TwoWheels.Coefficients wheelCoefficients = new TwoWheels.Coefficients();

        //Left and right sticks control wheel power
        //Up on the stick is negative, so the value is negated to correct for this
        wheelCoefficients.left = -controller.left_stick_y;
        wheelCoefficients.right = -controller.right_stick_y;

        wheels.setPower(wheelCoefficients, speed);
    }

    @Override
    public void stopMotion() {
        wheels.stop();
    }
}