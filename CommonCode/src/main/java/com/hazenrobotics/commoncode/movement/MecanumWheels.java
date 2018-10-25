package com.hazenrobotics.commoncode.movement;

import com.hazenrobotics.commoncode.interfaces.OpModeInterface;
import com.hazenrobotics.commoncode.models.angles.Angle;
import com.hazenrobotics.commoncode.models.angles.directions.RotationDirection;
import com.hazenrobotics.commoncode.models.angles.directions.SimpleDirection;
import com.hazenrobotics.commoncode.models.conditions.Condition;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

/**
 * Controller for Mecanum Wheels, which allow a bot to move in all directions and strafe
 * @see MecanumEncoderWheels
 */
@SuppressWarnings("unused,WeakerAccess")
public class MecanumWheels implements Wheels {
    protected OpModeInterface opModeInterface;

    protected DcMotor leftFront;
    protected DcMotor rightFront;
    protected DcMotor leftBack;
    protected DcMotor rightBack;

    protected SpeedSettings speeds;

    protected static final Coefficients ZEROED_COEFFICIENTS = new Coefficients(0f, 0f, 0f, 0f);
    protected static final SpeedSettings DEFAULT_SPEEDS = new SpeedSettings(0.5f, 0.5f, 0.3f);

    /**
     * Initializes the class to use the four wheels with the given configuration of names and speed
     * settings.
     * @param opModeInterface An interface from which the wheel motors can be accessed
     * @param wheelConfig Specifies the settings for the wheels such as the name and direction
     * @param speeds The speed settings to use for the different movement types
     */
    public MecanumWheels(OpModeInterface opModeInterface, WheelConfiguration wheelConfig, SpeedSettings speeds) {
        this.opModeInterface = opModeInterface;

        leftFront = this.opModeInterface.getMotor(wheelConfig.leftFrontName);
        leftBack = this.opModeInterface.getMotor(wheelConfig.leftBackName);
        rightFront = this.opModeInterface.getMotor(wheelConfig.rightFrontName);
        rightBack = this.opModeInterface.getMotor(wheelConfig.rightBackName);

        leftFront.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        leftBack.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        rightFront.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        rightBack.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        leftFront.setDirection(wheelConfig.leftFrontDirection);
        leftBack.setDirection(wheelConfig.leftBackDirection);
        rightFront.setDirection(wheelConfig.rightFrontDirection);
        rightBack.setDirection(wheelConfig.rightBackDirection);

        this.speeds = speeds;
    }

    /**
     * Initializes the class to use the four wheels with the given configuration of names, keeping
     * the default speed settings.
     * @param opModeInterface An interface from which the wheel motors can be accessed
     * @param wheelConfig Specifies the settings for the wheels such as the name and direction
     */
    public MecanumWheels(OpModeInterface opModeInterface, WheelConfiguration wheelConfig) {
        this(opModeInterface, wheelConfig, DEFAULT_SPEEDS);
    }

    @Override
    public void move(Condition condition, SimpleDirection direction) {
        move(condition, direction, speeds.move);
    }

    /**
     * Moves directly forward until a given condition is true without any turning at a given speed
     * @param condition Will move until this condition is true
     * @param direction Determines if the wheels will move forward or backwards
     * @param speed The percent of maximum speed which wheels will move at, should be between 0f and
     *              1f
     */
    public void move(Condition condition, SimpleDirection direction, float speed) {
        runByCoefficients(condition, calculateMove(direction), Math.abs(speed));
    }

    /**
     * Calculates the coefficient values for the wheels needed to move in the given direction
     * @param direction Determines if the wheels will move forward or backwards
     * @return The coefficients calculated to move in the given direction
     */
    public Coefficients calculateMove(SimpleDirection direction) {
        Coefficients coefficients = new Coefficients();
        //Moves forwards or backwards based on direction
        float coefficient = direction.equals(SimpleDirection.FORWARDS) ? 1f : -1f;
        coefficients.leftFront = coefficient;
        coefficients.leftBack = coefficient;
        coefficients.rightFront = coefficient;
        coefficients.rightBack = coefficient;
        return coefficients;
    }

    /**
     * Strafes until a given condition is true at a given angle
     * @param condition Will strafe until this condition is true
     * @param strafeAngle The angle which will be strafed towards
     */
    public void strafe(Condition condition, Angle strafeAngle) {
        strafe(condition, strafeAngle, speeds.strafe);
    }

    /**
     * Strafes until a given condition is true at a given angle and speed
     * @param condition Will strafe until this condition is true
     * @param strafeAngle The angle which will be strafed towards given in the specified unit
     * @param speed The percent of maximum speed which wheels will move at, should be between 0f and
     *              1f
     */
    public void strafe(Condition condition, Angle strafeAngle, float speed) {
        runByCoefficients(condition, calculateStrafe(strafeAngle), Math.abs(speed));
    }

    /**
     * Calculates the coefficient values for the wheels needed to strafe towards the given angle
     * @param strafeAngle The angle which will be strafed towards
     * @return The coefficients calculated to strafe towards the given angle
     */
    public Coefficients calculateStrafe(Angle strafeAngle) {
        float strafeAngleRadians = strafeAngle.getRadians();
        //Relative x and y movement from angle of movement
        float x = (float) Math.sin(strafeAngleRadians);
        float y = (float) Math.cos(strafeAngleRadians);

        //Used to determine the greatest possible value of y +/- x to scale them
        float magnitude = Math.abs(y) + Math.abs(x);
        //Used to prevent setting motor to power over 1
        float scale = Math.max(1, magnitude);

        Coefficients coefficients = new Coefficients();
        coefficients.leftFront = (y - x) / scale;
        coefficients.leftBack = (y + x) / scale;
        coefficients.rightFront = (y - x) / scale;
        coefficients.rightBack = (y - x) / scale;
        return coefficients;
    }

    @Override
    public void turn(Condition condition, RotationDirection direction) {
        turn(condition, direction, speeds.turn);
    }

    /**
     * Turns the robot in a given direction until a condition is true at a given speed
     * @param condition Will turn until this condition is true
     * @param direction Determines what direction the turn will be towards
     * @param speed The percent of maximum speed which wheels will turn at, must be between -1f and 1f
     */
    public void turn(Condition condition, RotationDirection direction, float speed) {
        runByCoefficients(condition, calculateTurn(direction), Math.abs(speed));
    }


    /**
     * Calculates the coefficient values for the wheels need to turn in the given direction
     * @param direction Determines what direction the turn will be towards
     * @return The coefficients calculated to turn in the given direction
     */
    public Coefficients calculateTurn(RotationDirection direction) {
        //Makes one side or the other move backwards to turn left or right based on the angle
        int leftCoefficient = direction == RotationDirection.CLOCKWISE ? 1 : -1;
        int rightCoefficient = direction == RotationDirection.CLOCKWISE ? -1 : 1;

        Coefficients coefficients = new Coefficients();
        coefficients.leftFront = leftCoefficient;
        coefficients.leftBack = leftCoefficient;
        coefficients.rightFront = rightCoefficient;
        coefficients.rightBack = rightCoefficient;
        return coefficients;
    }

    /**
     * Runs the wheels based on the relative values of the wheel coefficients at the given speed until a condition is true
     * @param condition Will run wheels until this condition is true
     * @param coefficients The coefficients which the wheels will proportionally move by
     * @param speed The speed at which the wheels will move
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
     * Sets the power of the wheels based on the relative value of the wheel coefficients at a given speed until another power is set
     * @param coefficients The coefficients which the wheels will proportionally move by
     * @param speed The speed at which the wheels will move
     */
    public void setPower(Coefficients coefficients, float speed) {
        //If the magnitude of speed is greater than 1, it reduces its magnitude to 1 so that the motor power is never set to over 1
        speed = boundRange(speed);
        double magnitude = coefficients.getLargestMagnitude();
        magnitude = magnitude < 1 ? 1 : magnitude;
        leftFront.setPower(coefficients.leftFront * speed / magnitude);
        leftBack.setPower(coefficients.leftBack * speed / magnitude);
        rightFront.setPower(coefficients.rightFront * speed / magnitude);
        rightBack.setPower(coefficients.rightBack * speed / magnitude);
    }

    /**
     * Stops the movement of the wheels
     */
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

    /**
     * Stores relative power values of different wheels for movement
     */
    public static class Coefficients {
        /**
         * The coefficient for the front left wheel
         */
        public float leftFront = 0f;

        /**
         * The coefficient for the back left wheel
         */
        public float leftBack = 0f;

        /**
         * The coefficient for the front right wheel
         */
        public float rightFront = 0f;

        /**
         * The coefficient for the back right wheel
         */
        public float rightBack = 0f;

        /**
         * Default constructor for Coefficients, which initializes all four wheel coefficients to 0f
         */
        public Coefficients() {}

        /**
         * Creates a Coefficients object which holds coefficient values for the four different wheels
         * @param leftFrontCoefficient The coefficient for the front left wheel
         * @param leftBackCoefficient The coefficient for the back left wheel
         * @param rightFrontCoefficient The coefficient for the front right wheel
         * @param rightBackCoefficient The coefficient for the back right wheel
         */
        public Coefficients(float leftFrontCoefficient, float leftBackCoefficient, float rightFrontCoefficient, float rightBackCoefficient) {
            this.leftFront = leftFrontCoefficient;
            this.leftBack = leftBackCoefficient;
            this.rightFront = rightFrontCoefficient;
            this.rightBack = rightBackCoefficient;
        }

        /**
         * Returns the largest absolute coefficient value out of all of the wheels
         * @return The largest coefficient value
         */
        public double getLargestMagnitude() {
            return Math.max(Math.max(Math.abs(leftFront), Math.abs(leftBack)), Math.max(Math.abs(rightFront), Math.abs(rightBack)));
        }

        /**
         * Updates the coefficients values of this with each of its coefficients values the opposite
         * sign of their previous value.
         * @return The new coefficient values
         */
        public Coefficients negated() {
            return new Coefficients(
                    -this.leftFront,
                    -this.leftBack,
                    -this.rightFront,
                    -this.rightBack);
        }

        /**
         * Returns a new coefficients object with each of its coefficient values the opposite sign
         * of these coefficients.
         * @return These coefficients
         */
        public Coefficients negate() {
            leftFront = -leftFront;
            leftBack = -leftBack;
            rightFront = -rightFront;
            rightBack = -rightBack;
            return this;
        }
    }

    /**
     * Stores the speed settings the wheels will use when no others are specified.
     * Speeds for moving, strafing, and turning can be specified
     */
    public static class SpeedSettings {
        public float move;
        public float strafe;
        public float turn;

        /**
         * Initializes the different speed values with the given settings.
         * @param moveSpeed The speed which the wheels will move at unless otherwise specified -
         *                  Should be between 0 and 1
         * @param strafeSpeed The speed which the wheels will strafe at unless otherwise specified -
         *                  Should be between 0 and 1
         * @param turnSpeed The speed which the wheels will turn at unless otherwise specified -
         *                  Should be between 0 and 1
         */
        public SpeedSettings(float moveSpeed, float strafeSpeed, float turnSpeed) {
            this.move = moveSpeed;
            this.strafe = strafeSpeed;
            this.turn = turnSpeed;
        }
    }

    public static class WheelConfiguration {
        protected final String leftFrontName;
        protected final String leftBackName;
        protected final String rightFrontName;
        protected final String rightBackName;
        protected final DcMotorSimple.Direction leftFrontDirection;
        protected final DcMotorSimple.Direction leftBackDirection;
        protected final DcMotorSimple.Direction rightFrontDirection;
        protected final DcMotorSimple.Direction rightBackDirection;

        public WheelConfiguration(String leftFrontName, String leftBackName, String rightFrontName, String rightBackName,
                                  DcMotorSimple.Direction leftFrontDirection, DcMotorSimple.Direction leftBackDirection, DcMotorSimple.Direction rightFrontDirection, DcMotorSimple.Direction rightBackDirection) {
            this.leftFrontName = leftFrontName;
            this.leftBackName = leftBackName;
            this.rightFrontName = rightFrontName;
            this.rightBackName = rightBackName;
            this.leftFrontDirection = leftFrontDirection;
            this.leftBackDirection = leftBackDirection;
            this.rightFrontDirection = rightFrontDirection;
            this.rightBackDirection = rightBackDirection;
        }

        public WheelConfiguration(String leftFrontName, String leftBackName, String rightFrontName, String rightBackName) {
            this(leftFrontName, leftBackName, rightFrontName, rightBackName, DcMotorSimple.Direction.REVERSE, DcMotorSimple.Direction.REVERSE, DcMotorSimple.Direction.FORWARD, DcMotorSimple.Direction.FORWARD);
        }
    }
}