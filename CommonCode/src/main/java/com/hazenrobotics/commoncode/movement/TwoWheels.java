package com.hazenrobotics.commoncode.movement;

import com.hazenrobotics.commoncode.interfaces.OpModeInterface;
import com.hazenrobotics.commoncode.models.angles.directions.RotationDirection;
import com.hazenrobotics.commoncode.models.angles.directions.SideDirection;
import com.hazenrobotics.commoncode.models.angles.directions.SimpleDirection;
import com.hazenrobotics.commoncode.models.conditions.Condition;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

/**
 * Controller for a basic Two-Wheels configuration (one on each side of the bot.) This allow a bot
 * to move forward, turn, and curve.
 * @see TwoEncoderWheels
 */
@SuppressWarnings("unused,WeakerAccess")
public class TwoWheels implements Wheels {
    protected OpModeInterface opModeInterface;

    protected DcMotor left;
    protected DcMotor right;

    protected SpeedSettings speeds;

    protected static final Coefficients ZEROED_COEFFICIENTS = new Coefficients(0f, 0f);
    public static final SpeedSettings DEFAULT_SPEEDS = new SpeedSettings(0.7f, 0.5f, 0.3f);

    /**
     * Initializes the class to use the two wheels with the given configuration of names and speed
     * settings.
     * @param opModeInterface An interface from which the wheel motors can be accessed
     * @param wheelConfig Specifies the settings for the wheels such as the name and direction
     * @param speeds The speed settings to use for the different movement types
     */
    public TwoWheels(OpModeInterface opModeInterface, WheelConfiguration wheelConfig, SpeedSettings speeds) {
        this.opModeInterface = opModeInterface;

        left = opModeInterface.getMotor(wheelConfig.leftName);
        right = opModeInterface.getMotor(wheelConfig.rightName);
        left.setDirection(wheelConfig.leftDirection);
        right.setDirection(wheelConfig.rightDirection);

        this.speeds = speeds;
    }

    /**
     * Initializes the class to use the two wheels with the given configuration of names, keeping the
     * default speed settings.
     * @param opModeInterface An interface from which the wheel motors can be accessed
     * @param wheelConfig Specifies the settings for the wheels such as the name and direction
     */
    public TwoWheels(OpModeInterface opModeInterface, WheelConfiguration wheelConfig) {
        this(opModeInterface, wheelConfig, DEFAULT_SPEEDS);
    }

    @Override
    public void move(Condition condition, SimpleDirection direction) {
        move(condition, direction, speeds.move);
    }

    /**
     * Moves directly forward until a given condition is true at a given angle and speed.
     * @param condition Will move until this condition is true
     * @param direction Determines if the wheels will move forward or backwards
     * @param speed The percent of maximum speed which wheels will move at, should be between 0f and
     *              1f
     */
    public void move(Condition condition, SimpleDirection direction, float speed) {
        runByCoefficients(condition, calculateMove(direction), Math.abs(speed));
    }

    /**
     * Calculates the coefficient values for the wheels needed to move in the given direction.
     * @param direction The direction to calculate coefficients for
     * @return The coefficients calculated to move towards the given angle
     */
    public Coefficients calculateMove(SimpleDirection direction) {
        Coefficients coefficients = new Coefficients();
        //Moves forwards or backwards based on direction
        coefficients.left = direction == SimpleDirection.FORWARDS ? 1f : -1f;
        coefficients.right = direction == SimpleDirection.FORWARDS ? 1f : -1f;
        return coefficients;
    }

    /**
     * Moves in either an arch or turn like motion depending on the factor given.
     * @param condition Will curve until this condition is true
     * @param movementDirection Determines if the wheels will move forward or backwards on the curve
     * @param curvingDirection Determines if the wheels will curve to the right or the left
     * @param straightFactor The factor by which it will curve, should be between -1f and 1f. 1f is
     *                       regular speed, 0f is selected wheel side not moving, -1f is it spinning
     *                       in opposite direction, turing bot
     */
    public void curve(Condition condition, SimpleDirection movementDirection, SideDirection curvingDirection, float straightFactor) {
        curve(condition, movementDirection, curvingDirection, straightFactor, speeds.curve);
    }

    /**
     * Moves in either an arch or turn like motion depending on the factor given.
     * @param condition Will curve until this condition is true
     * @param movementDirection Determines if the wheels will move forward or backwards on the curve
     * @param curvingDirection Determines if the wheels will curve to the right or the left
     * @param straightFactor The factor by which it will curve, should be between -1f and 1f. 1f is
     *                       regular speed, 0f is selected wheel side not moving, -1f is it spinning
     *                       in opposite direction, turing bot
     * @param speed Between 0f and 1f, this determines the percentage of max curving speed the robot
     *              will move
     */
    public void curve(Condition condition, SimpleDirection movementDirection, SideDirection curvingDirection, float straightFactor, float speed) {
        runByCoefficients(condition, calculateCurve(movementDirection, curvingDirection, straightFactor), Math.abs(speed));
    }


    /**
     * Calculates the coefficient values for the wheels need to curve to a certain factor in the
     * given direction.
     * @param movementDirection Determines if the wheels will move forward or backwards on the curve
     * @param curvingDirection Determines if the wheels will curve to the right or the left
     * @param straightFactor The factor by which it will curve, must be between -1f and 1f. 1f is
     *                       regular speed, 0f is selected wheel side not moving, -1f is it spinning
     *                       in opposite direction, turing bot.
     * @return The coefficients calculated to strafe towards the given direction
     */
    public Coefficients calculateCurve(SimpleDirection movementDirection, SideDirection curvingDirection, float straightFactor) {
        Coefficients coefficients = new Coefficients();

        //Makes one side or the other move backwards to turn left or right based on the direction
        coefficients.left = (movementDirection == SimpleDirection.FORWARDS ? 1 : -1) * (curvingDirection == SideDirection.RIGHT ? 1 : straightFactor);
        coefficients.right = (movementDirection == SimpleDirection.FORWARDS ? 1 : -1) * (curvingDirection == SideDirection.RIGHT ? straightFactor : 1);
        return coefficients;
    }

    @Override
    public void turn(Condition condition, RotationDirection direction) {
        turn(condition, direction, speeds.turn);
    }

    /**
     * Turns the robot in a given direction until a condition is true at a given speed.
     * @param condition Will turn until this condition is true
     * @param direction Determines if the wheels will turn counter-clockwise or clockwise
     * @param speed The percent of maximum speed which wheels will move at, should be between 0f and
     *              1f
     */
    public void turn(Condition condition, RotationDirection direction, float speed) {
        runByCoefficients(condition, calculateTurn(direction), Math.abs(speed));
    }

    /**
     * Calculates the coefficient values for the wheels need to turn in the given direction.
     * @param direction Determines if the wheels will turn counter-clockwise or clockwise
     * @return The coefficients calculated to turn in the given direction
     */
    public Coefficients calculateTurn(RotationDirection direction) {
        Coefficients coefficients = new Coefficients();
        //Makes one side or the other move backwards to turn left or right based on the direction
        coefficients.left = direction == RotationDirection.CLOCKWISE ? 1 : -1;
        coefficients.right = direction == RotationDirection.CLOCKWISE ? -1 : 1;
        return coefficients;
    }

    /**
     * Runs the wheels based on the relative values of the wheel coefficients at the given speed
     * until a condition is true.
     * @param condition Will run wheels until this condition is true
     * @param coefficients The coefficients which the wheels will proportionally move by
     * @param speed The percent of maximum speed which wheels will turn at, should be between 0f and
     *              1f
     */
    public void runByCoefficients(Condition condition, Coefficients coefficients, float speed) {
        //If a negative speed was passed here, we are moving backwards overall
        coefficients = speed < 0 ? coefficients.negated() : coefficients;
        speed = Math.abs(speed);

        setPower(coefficients, speed);
        while (!condition.isTrue() && opModeInterface.opModeIsActive()) {
            opModeInterface.getTelemetry().addData("Condition", condition.isTrue());
            opModeInterface.getTelemetry().update();
            opModeInterface.idle();
        }
        stop();
    }

    /**
     * Sets the power of the wheels based on the relative value of the wheel coefficients at a given
     * speed until another power is set.
     * @param coefficients The coefficients which the wheels will proportionally move by
     * @param speed The speed at which the wheels will move, must be between -1 and 1
     */
    public void setPower(Coefficients coefficients, float speed) {
        //Bounds the speed to be between -1 and 1 so the motors are not set to an improper power level
        speed = boundRange(speed);
        double magnitude = coefficients.getLargestMagnitude();
        magnitude = magnitude < 1 ? 1 : magnitude;
        left.setPower(coefficients.left * speed / magnitude);
        right.setPower(coefficients.right * speed / magnitude);
    }

    /** Stops the movement of the wheels. */
    public void stop() {
        setPower(ZEROED_COEFFICIENTS, 1f);
    }

    /**
     * Ensures that the given value is between -1 and 1.
     * @param value the value to bound
     * @return a float between -1 and 1, being -1 or 1 if the value was more or less than the range,
     * and just the original value if it was not
     */
    private static float boundRange(float value) {
        //If the magnitude is greater than 1, it reduces its magnitude to 1
        return  Math.abs(value) > 1 ? (value > 0 ? 1: -1) : (value);
    }

    /** Stores relative power values of different wheels for movement. */
    public static class Coefficients {
        /** The coefficient for the left wheel. */
        public float left = 0f;

        /** The coefficient for the right whee.l */
        public float right = 0f;

        /**
         * Default constructor for Coefficients, which initializes all the wheel coefficients to 0f.
         */
        public Coefficients() {}

        /**
         * Creates a Coefficients object which holds coefficient values for the two different wheels.
         * @param leftCoefficient The coefficient for the left wheel
         * @param rightCoefficient The coefficient for the right wheel
         */
        public Coefficients(float leftCoefficient, float rightCoefficient) {
            this.left = leftCoefficient;
            this.right = rightCoefficient;
        }

        /**
         * Returns the largest absolute coefficient value out of the two wheels.
         * @return The largest coefficient value
         */
        public double getLargestMagnitude() {
            return Math.max(Math.abs(left), Math.abs(right));
        }

        /**
         * Updates the coefficient values of this with each of its coefficient values the opposite
         * sign of their previous value.
         * @return The new coefficient values
         */
        public Coefficients negated() {
            return new Coefficients(
                    -this.left,
                    -this.right);
        }

        /**
         * Returns a new coefficient object with each of its coefficient values the opposite sign of
         * these coefficients.
         * @return These coefficients
         */
        public Coefficients negate() {
            left = -left;
            right = -right;
            return this;
        }
    }

    /**
     * Stores the speed settings the wheels will use when no others are specified.
     * Speeds for moving, curving, and turning can be specified
     */
    public static class SpeedSettings {
        public float move;
        public float curve;
        public float turn;

        /**
         * Initializes the different speed values with the given settings.
         * @param moveSpeed The speed which the wheels will move at unless otherwise specified -
         *                  Should be between 0 and 1
         * @param curveSpeed The speed which the wheels will curve at unless otherwise specified -
         *                  Should be between 0 and 1
         * @param turnSpeed The speed which the wheels will turn at unless otherwise specified -
         *                  Should be between 0 and 1
         */
        public SpeedSettings(float moveSpeed, float curveSpeed, float turnSpeed) {
            this.move = moveSpeed;
            this.curve = curveSpeed;
            this.turn = turnSpeed;
        }
    }

    public static class WheelConfiguration {
        protected final String leftName;
        protected final String rightName;
        protected final DcMotorSimple.Direction leftDirection;
        protected final DcMotorSimple.Direction rightDirection;

        public WheelConfiguration(String leftName, String rightName, DcMotorSimple.Direction leftDirection, DcMotorSimple.Direction rightDirection) {
            this.leftName = leftName;
            this.rightName = rightName;
            this.leftDirection = leftDirection;
            this.rightDirection = rightDirection;
        }

        public WheelConfiguration(String leftName, String rightName) {
            this(leftName, rightName, DcMotorSimple.Direction.FORWARD, DcMotorSimple.Direction.REVERSE);
        }
    }
}
