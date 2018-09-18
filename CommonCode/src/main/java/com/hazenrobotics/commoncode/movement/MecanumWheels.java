package com.hazenrobotics.commoncode.movement;

import android.support.annotation.NonNull;

import com.hazenrobotics.commoncode.interfaces.HardwareInterface;
import com.hazenrobotics.commoncode.interfaces.OpModeInterface;
import com.hazenrobotics.commoncode.models.angles.Angle;
import com.hazenrobotics.commoncode.models.angles.directions.RotationDirection;
import com.hazenrobotics.commoncode.models.angles.directions.SimpleDirection;
import com.hazenrobotics.commoncode.models.conditions.Condition;
import com.qualcomm.robotcore.BuildConfig;
import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;

/**
 * Controller for Mecanum Wheels, which allow a bot to move in all directions and strafe
 * @see MecanumEncoderWheels
 */
public class MecanumWheels implements Wheels {
    protected OpModeInterface opModeInterface;

    protected DcMotor leftFront;
    protected DcMotor rightFront;
    protected DcMotor leftBack;
    protected DcMotor rightBack;

    protected static final float MOVE_SPEED = 0.5f;
    protected static final float STRAFE_SPEED = 0.5f;
    protected static final float TURN_SPEED = 0.3f;
    protected static final AngleUnit DEFAULT_ANGLE_UNIT = AngleUnit.DEGREES;
    protected static final Coefficients ZEROED_COEFFICIENTS = new Coefficients(0f, 0f, 0f, 0f);

    /**
     * Initializes the class to use the four wheels with the given configuration names
     * @param opModeInterface An interface from which the wheel motors can be accessed
     * @param leftFrontName Name of the wheel in the robot configuration
     * @param leftBackName See above
     * @param rightFrontName See above
     * @param rightBackName See above
     */
    public MecanumWheels(OpModeInterface opModeInterface, String leftFrontName, String leftBackName, String rightFrontName, String rightBackName) {
        this.opModeInterface = opModeInterface;
        
        leftFront = this.opModeInterface.getMotor(leftFrontName);
        leftBack = this.opModeInterface.getMotor(leftBackName);
        rightFront = this.opModeInterface.getMotor(rightFrontName);
        rightBack = this.opModeInterface.getMotor(rightBackName);

        leftFront.setDirection(DcMotor.Direction.REVERSE);
        leftBack.setDirection(DcMotor.Direction.REVERSE);
        rightFront.setDirection(DcMotor.Direction.FORWARD);
        rightBack.setDirection(DcMotor.Direction.FORWARD);
    }

    /**
     * Initializes the class to use the four wheels with default configuration names
     * @param opModeInterface An interface from which the wheel motors can be accessed
     */
    public MecanumWheels(OpModeInterface opModeInterface) {
        this(opModeInterface, "leftFront", "leftBack", "rightFront", "rightBack");
    }

    @Override
    public void move(Condition condition, SimpleDirection direction) {
        move(condition, direction, MOVE_SPEED);
    }

    /**
     * Moves directly forward until a given condition is true without any turning at a given speed
     * @param condition Will move until this condition is true
     * @param direction Determines if the wheels will move forward or backwards
     * @param speed The percent of maximum speed which wheels will move at, must be between -1f and 1f
     */
    public void move(Condition condition, SimpleDirection direction, float speed) {
        runByCoefficients(condition, calculateMove(direction), speed);
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
        strafe(condition, strafeAngle, STRAFE_SPEED);
    }

    /**
     * Strafes until a given condition is true at a given angle and speed
     * @param condition Will strafe until this condition is true
     * @param strafeAngle The angle which will be strafed towards given in the specified unit
     * @param speed The percent of maximum speed which wheels will move at, must be between -1f and 1f
     */
    public void strafe(Condition condition, Angle strafeAngle, float speed) {
        runByCoefficients(condition, calculateStrafe(strafeAngle), speed);
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
        turn(condition, direction, TURN_SPEED);
    }

    /**
     * Turns the robot in a given direction until a condition is true at a given speed
     * @param condition Will turn until this condition is true
     * @param direction Determines what direction the turn will be towards
     * @param speed The percent of maximum speed which wheels will turn at, must be between -1f and 1f
     */
    public void turn(Condition condition, RotationDirection direction, float speed) {
        runByCoefficients(condition, calculateTurn(direction), speed);
    }


    /**
     * Calculates the coefficient values for the wheels need to turn in the given direction
     * @param direction Determines what direction the turn will be towards
     * @return The coefficients calculated to turn in the given direction
     */
    public Coefficients calculateTurn(RotationDirection direction) {
        //Makes one side or the other move backwards to turn left or right based on the angle
        int leftCoefficient = direction.equals(RotationDirection.CLOCKWISE) ? 1 : -1;
        int rightCoefficient = direction.equals(RotationDirection.CLOCKWISE) ? -1 : 1;

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
         * @param leftBackCoefficient The coefficient for the front left wheel
         * @param rightFrontCoefficient The coefficient for the front left wheel
         * @param rightBackCoefficient The coefficient for the front left wheel
         */
        public Coefficients(float leftFrontCoefficient, float leftBackCoefficient, float rightFrontCoefficient, float rightBackCoefficient) {
            this.leftFront = leftFrontCoefficient;
            this.leftBack = leftBackCoefficient;
            this.rightFront = rightFrontCoefficient;
            this.rightBack = rightBackCoefficient;
        }

        /**
         * Copies the values of the array to be the coefficient values of the different wheels in a Coefficients object
         * 0th Index = Front Left
         * 1st Index = Back Left
         * 2nd Index = Front Right
         * 3rd Index = Back Right
         * @param coefficients The array from which to copy the different coefficient values; Must have a size of at least 4
         */
        @NonNull
        public static Coefficients fromArray(float[] coefficients) {
            if(BuildConfig.DEBUG)
                assert coefficients.length >= 4 : "A wheel coefficients array must contain at least 4 values";
            return new Coefficients(coefficients[0], coefficients[1], coefficients[2], coefficients[3]);
        }

        /**
         * Makes a copy of the internal coefficient array which holds the coefficient values for the different wheels
         * 0th Index = Front Left
         * 1st Index = Back Left
         * 2nd Index = Front Right
         * 3rd Index = Back Right
         * @return Returns a copy of the internal coefficients array, which has a length of 4
         */
        public float[] toArray() {
            return new float[]{leftFront, leftBack, rightFront, rightBack};
        }

        /**
         * Returns the largest absolute coefficient value out of all of the wheels
         * @return The largest coefficient value
         */
        public double getLargestMagnitude() {
            return Math.max(Math.max(Math.abs(leftFront), Math.abs(leftBack)), Math.max(Math.abs(rightFront), Math.abs(rightBack)));
        }
    }
}