package com.hazenrobotics.commoncode.movement;

import android.support.annotation.NonNull;

import com.hazenrobotics.commoncode.interfaces.OpModeInterface;
import com.hazenrobotics.commoncode.models.angles.directions.RotationDirection;
import com.hazenrobotics.commoncode.models.angles.directions.SideDirection;
import com.hazenrobotics.commoncode.models.angles.directions.SimpleDirection;
import com.hazenrobotics.commoncode.models.conditions.Condition;
import com.qualcomm.robotcore.BuildConfig;
import com.qualcomm.robotcore.hardware.DcMotor;

public class TwoWheels implements Wheels {

    protected OpModeInterface opModeInterface;

    protected DcMotor left;
    protected DcMotor right;

    protected static final float MOVE_SPEED = 0.7f;
    protected static final float CURVE_SPEED = 0.5f;
    protected static final float TURN_SPEED = 0.3f;
    protected static final Coefficients ZEROED_COEFFICIENTS = new Coefficients(0f, 0f);


    /**
     * Initializes the class to use the two wheels with the given configuration names
     * @param opModeInterface An interface from which the wheel motors can be accessed
     * @param leftName Name of the wheel in the robot configuration
     * @param rightName See above
     */
    public TwoWheels(OpModeInterface opModeInterface, String leftName, String rightName) {
        this.opModeInterface = opModeInterface;

        left = opModeInterface.getMotor(leftName);
        right = opModeInterface.getMotor(rightName);
        left.setDirection(DcMotor.Direction.REVERSE);
        right.setDirection(DcMotor.Direction.FORWARD);
    }

    /**
     * Initializes the class to use the two wheels with default configuration names
     * @param opModeInterface An interface from which the wheel motors can be accessed
     */
    public TwoWheels(OpModeInterface opModeInterface) {
        this(opModeInterface, "left", "right");
    }

    @Override
    public void move(Condition condition, SimpleDirection direction) {
        move(condition, direction, MOVE_SPEED);
    }

    /**
     * Moves directly forward until a given condition is true at a given angle and speed
     * @param condition Will move until this condition is true
     * @param direction Determines if the wheels will move forward or backwards
     * @param speed The percent of maximum speed which wheels will move at, must be between -1f and 1f
     */
    public void move(Condition condition, SimpleDirection direction, float speed) {
        runByCoefficients(condition, calculateMove(direction), speed);
    }

    /**
     * Calculates the coefficient values for the wheels needed to move in the given direction
     * @param direction The direction to calculate coefficients for
     * @return The coefficients calculated to move towards the given angle
     */
    public Coefficients calculateMove(SimpleDirection direction) {
        Coefficients coefficients = new Coefficients();
        //Moves forwards or backwards based on direction
        coefficients.left = direction.equals(SimpleDirection.FORWARDS) ? 1f : -1f;
        coefficients.right = direction.equals(SimpleDirection.FORWARDS) ? 1f : -1f;
        return coefficients;
    }

    /**
     * Moves in either an arch or turn like motion depending on the factor given
     * @param condition Will curve until this condition is true
     * @param movementDirection Determines if the wheels will move forward or backwards on the curve
     * @param curvingDirection Determines if the wheels will curve to the right or the left
     * @param straightFactor the factor by which it will curve, must be between -1f and 1f. 1f is regular speed,
     *               0f is selected wheel side not moving, -1f is it spinning in opposite direction, turing bot
     */
    public void curve(Condition condition, SimpleDirection movementDirection, SideDirection curvingDirection, float straightFactor) {
        curve(condition, movementDirection, curvingDirection, straightFactor, CURVE_SPEED);
    }

    /**
     * Moves in either an arch or turn like motion depending on the factor given
     * @param condition Will curve until this condition is true
     * @param movementDirection Determines if the wheels will move forward or backwards on the curve
     * @param curvingDirection Determines if the wheels will curve to the right or the left
     * @param straightFactor the factor by which it will curve, must be between -1f and 1f. 1f is regular speed,
     *               0f is selected wheel side not moving, -1f is it spinning in opposite direction, turing bot
     * @param speed Between 0.0f and 1.0f, this determines the percentage of max curving speed the robot will move
     */
    public void curve(Condition condition, SimpleDirection movementDirection, SideDirection curvingDirection, float straightFactor, float speed) {
        runByCoefficients(condition, calculateCurve(movementDirection, curvingDirection, straightFactor), speed);
    }


    /**
     * Calculates the coefficient values for the wheels need to curve to a certain factor in the given direction
     * @param movementDirection Determines if the wheels will move forward or backwards on the curve
     * @param curvingDirection Determines if the wheels will curve to the right or the left
     * @param straightFactor the factor by which it will curve, must be between -1f and 1f. 1f is regular speed,
     *               0f is selected wheel side not moving, -1f is it spinning in opposite direction, turing bot
     * @return
     */
    public Coefficients calculateCurve(SimpleDirection movementDirection, SideDirection curvingDirection, float straightFactor) {
        Coefficients coefficients = new Coefficients();

        //Makes one side or the other move backwards to turn left or right based on the direction
        coefficients.left = (movementDirection.equals(SimpleDirection.FORWARDS) ? 1 : -1) * (curvingDirection.equals(SideDirection.RIGHT) ? 1 : straightFactor);
        coefficients.right = (movementDirection.equals(SimpleDirection.FORWARDS) ? 1 : -1) * (curvingDirection.equals(SideDirection.RIGHT) ? straightFactor : 1);
        return coefficients;
    }

    @Override
    public void turn(Condition condition, RotationDirection direction) {
        turn(condition, direction, TURN_SPEED);
    }

    /**
     * Turns the robot in a given direction until a condition is true at a given speed
     * @param condition Will turn until this condition is true
     * @param direction Determines if the wheels will turn counter-clockwise or clockwise
     * @param speed The percent of maximum speed which wheels will move at, must be between -1f and 1f
     */
    public void turn(Condition condition, RotationDirection direction, float speed) {
        runByCoefficients(condition, calculateTurn(direction), speed);
    }

    /**
     * Calculates the coefficient values for the wheels need to turn in the given direction
     * @param direction Determines if the wheels will turn counter-clockwise or clockwise
     * @return The coefficients calculated to turn in the given direction
     */
    public Coefficients calculateTurn(RotationDirection direction) {
        Coefficients coefficients = new Coefficients();
        //Makes one side or the other move backwards to turn left or right based on the direction
        coefficients.left = direction.equals(RotationDirection.CLOCKWISE) ? 1 : -1;
        coefficients.right = direction.equals(RotationDirection.CLOCKWISE) ? -1 : 1;
        return coefficients;
    }

    /**
     * Runs the wheels based on the relative values of the wheel coefficients at the given speed until a condition is true
     * @param condition Will run wheels until this condition is true
     * @param coefficients The coefficients which the wheels will proportionally move by
     * @param speed The speed at which the wheels will move
     */
    void runByCoefficients(Condition condition, Coefficients coefficients, float speed) {
        setPower(coefficients, speed);
        while (!condition.isTrue()) {
            opModeInterface.idle();
        }
        setPower(ZEROED_COEFFICIENTS, speed);
    }

    /**
     * Sets the power of the wheels based on the relative value of the wheel coefficients at a given speed until another power is set
     * @param coefficients The coefficients which the wheels will proportionally move by
     * @param speed The speed at which the wheels will move
     */
    public void setPower(Coefficients coefficients, float speed) {
        if(BuildConfig.DEBUG)
            assert Math.abs(speed) <= 1 : "Movement Speed must be between positive and negative 1";
        double magnitude = coefficients.getLargestMagnitude();
        magnitude = magnitude < 1 ? 1 : magnitude;
        left.setPower(coefficients.left * speed / magnitude);
        right.setPower(coefficients.right * speed / magnitude);
    }

    /**
     * Stops the movement of the wheels
     */
    public void stop() {
        setPower(ZEROED_COEFFICIENTS, 1f);
    }

    /**
     * Stores relative power values of different wheels for movement
     */
    public static class Coefficients {
        /**
         * The coefficient for the left wheel
         */
        public float left = 0f;

        /**
         * The coefficient for the right wheel
         */
        public float right = 0f;

        /**
         * Default constructor for Coefficients, which initializes all the wheel coefficients to 0f
         */
        public Coefficients() {}

        /**
         * Creates a Coefficients object which holds coefficient values for the two different wheels
         * @param leftCoefficient The coefficient for the left wheel
         * @param rightCoefficient The coefficient for the right wheel
         */
        public Coefficients(float leftCoefficient, float rightCoefficient) {
            this.left = leftCoefficient;
            this.right = rightCoefficient;
        }

        /**
         * Copies the values of the array to be the coefficient values of the different wheels in a Coefficients object
         * 0th Index = Left
         * 1st Index = Right
         * @param coefficients The array from which to copy the different coefficient values; Must have a size of at least 2
         */
        @NonNull
        public static Coefficients fromArray(float[] coefficients) {
            if(BuildConfig.DEBUG)
                assert coefficients.length >= 2 : "A wheel coefficients array must contain at least 2 values";
            return new Coefficients(coefficients[0], coefficients[1]);
        }

        /**
         * Makes a copy of the internal coefficient array which holds the coefficient values for the different wheels
         * 0th Index = Left
         * 1st Index = Right
         * @return Returns a copy of the internal coefficients array, which has a length of 2
         */
        public float[] toArray() {
            return new float[]{left, right};
        }

        /**
         * Returns the largest absolute coefficient value out of the two wheels
         * @return The largest coefficient value
         */
        public double getLargestMagnitude() {
            return Math.max(Math.abs(left), Math.abs(right));
        }
    }
}
