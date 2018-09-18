package com.hazenrobotics.commoncode.movement;

import android.support.annotation.NonNull;

import com.hazenrobotics.commoncode.interfaces.OpModeInterface;
import com.hazenrobotics.commoncode.models.angles.Angle;
import com.hazenrobotics.commoncode.models.distances.Distance;
import com.hazenrobotics.commoncode.models.angles.directions.RotationDirection;
import com.hazenrobotics.commoncode.models.angles.directions.SimpleDirection;
import com.hazenrobotics.commoncode.models.conditions.Condition;

import com.qualcomm.robotcore.BuildConfig;
import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.robotcore.external.Const;
import org.firstinspires.ftc.robotcore.external.NonConst;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;

/**
 * Controller for Mecanum Wheels with encoders, which allow a bot to move in all directions and strafe,
 * and control the degree of such movements using the encoders attached to the motors, theoretically
 * allowing for measured moments. Since Mecanum Wheels often lose traction with the ground and 'slip',
 * these encoders are not always accurate for measuring distances.
 * @see MecanumWheels
 */
public class MecanumEncoderWheels extends MecanumWheels implements EncoderWheels
{
    protected static final DistanceUnit DEFAULT_DISTANCE_UNIT = DistanceUnit.INCH;
    protected static final int COUNTS_PER_REV = 1680;
    protected static final double WHEEL_DIAMETER = DEFAULT_DISTANCE_UNIT.fromInches(4.0);
    protected static final double WHEEL_SLIP_MULTIPLIER = 3.375;
    protected static final double COUNTS_PER_UNIT = (COUNTS_PER_REV / WHEEL_DIAMETER) / WHEEL_SLIP_MULTIPLIER;
    protected static final double ROBOT_RADIUS = DEFAULT_DISTANCE_UNIT.fromInches(9.0);
    protected static final double ROBOT_TURNING_CIRCUMFERENCE = Math.PI * (2 * ROBOT_RADIUS);

    /**
     * Initializes the class to use the four wheels with the given configuration names
     * @param opModeInterface An interface from which the wheel motors can be accessed
     * @param leftFrontName Name of the wheel in the robot configuration
     * @param leftBackName See above
     * @param rightFrontName See above
     * @param rightBackName See above
     */
    public MecanumEncoderWheels(OpModeInterface opModeInterface, String leftFrontName, String leftBackName, String rightFrontName, String rightBackName) {
        super(opModeInterface, leftFrontName, leftBackName, rightFrontName, rightBackName);
    }

    /**
     * Initializes the class to use the four wheels with default configuration names
     * @param opModeInterface An interface from which the wheel motors can be accessed
     */
    public MecanumEncoderWheels(OpModeInterface opModeInterface) {
        super(opModeInterface);
    }


    @Override
    public void move(Distance distance) {
        move(distance, MOVE_SPEED);
    }

    /**
     * Moves directly forward for a given distance using encoders without any turning at a given speed
     * @param distance The distance to move
     * @param speed The percent of maximum speed which wheels will move at, must be between -1f and 1f
     */
    public void move(Distance distance, float speed) {
        int baseCounts = (int) (distance.getValue(DEFAULT_DISTANCE_UNIT) * COUNTS_PER_UNIT);
        Counts counts = Counts.fromCoefficients(calculateMove(distance.getValue(DEFAULT_DISTANCE_UNIT) > 0 ? SimpleDirection.FORWARDS : SimpleDirection.BACKWARDS), baseCounts);
        runByCounts(counts, speed);
    }

    /**
     * Records how much each of the wheel counts changed during a given movement and returns it
     * @param condition Will move until this condition is true
     * @param direction Determines if the wheels will move forward or backwards
     * @return The change in each wheels count value
     */
    Counts recordMove(Condition condition, SimpleDirection direction) {
        return recordMove(condition, direction, MOVE_SPEED);
    }

    /**
     * Records how much each of the wheel counts changed during a given movement and returns it
     * @param condition Will move until this condition is true
     * @param direction Determines if the wheels will move forward or backwards
     * @param speed The speed at which the move being recorded will be taken
     * @return The change in each wheels count value
     */
    Counts recordMove(Condition condition, SimpleDirection direction, float speed) {
        Counts initial = Counts.fromPosition(this);
        move(condition, direction, speed);
        Counts finial = Counts.fromPosition(this);
        return finial.subtracted(initial);
    }

    /**
     * Moves directly forward for a given number of counts at a given angle
     * @param distance Will move forward this distance
     * @param strafeAngle The angle which will be strafed towards
     */
    public void strafe(Distance distance, Angle strafeAngle) {
        strafe(distance, strafeAngle, STRAFE_SPEED);
    }

    /**
     * Moves directly forward for a given number of counts at a given angle and speed
     * @param distance Will move forward this distance
     * @param strafeAngle The angle which will be strafed towards
     * @param speed The percent of maximum speed which wheels will move at, must be between -1f and 1f
     */
    public void strafe(Distance distance, Angle strafeAngle, float speed) {
        int baseCounts = (int) (distance.getValue(DEFAULT_DISTANCE_UNIT) * COUNTS_PER_UNIT);
        Counts counts = Counts.fromCoefficients(calculateStrafe(strafeAngle), baseCounts);
        runByCounts(counts, speed);
    }

    /**
     * Records how much each of the wheel counts changed during a given movement and returns it
     * @param condition Will move until this condition is true
     * @param strafeAngle The angle which will be strafed towards
     * @param speed The percent of maximum speed which wheels will move at, must be between -1f and 1f
     * @return The change in each wheels count value
     */
    public Counts recordStrafe(Condition condition, Angle strafeAngle, float speed) {
        Counts initial = Counts.fromPosition(this);
        strafe(condition, strafeAngle, speed);
        Counts finial = Counts.fromPosition(this);
        return finial.subtracted(initial);
    }

    @Override
    public void turn(Angle angle) {
        turn(angle, TURN_SPEED);
    }

    /**
     * Turns for a given angle using encoders
     * @param angle The angle for which to turn
     * @param speed The percent of maximum speed which wheels will turn at, must be between -1f and 1f
     */
    public void turn(Angle angle, float speed) {
        double turnDistance = (angle.getDegrees() / 360f) * ROBOT_TURNING_CIRCUMFERENCE;
        int baseCounts = (int) (turnDistance * COUNTS_PER_UNIT);

        Counts counts = Counts.fromCoefficients(calculateTurn((angle.getDegrees() > 0) ? RotationDirection.CLOCKWISE : RotationDirection.COUNTER_CLOCKWISE), baseCounts);
        runByCounts(counts, speed);
    }

    /**
     * Records how much each of the wheel counts changed during a given turn and returns it
     * @param condition Will move until this condition is true
     * @param direction Determines what direction the turn will be towards
     * @param speed The percent of maximum speed which wheels will turn at, must be between -1f and 1f
     * @return The change in each wheels count value
     */
    public Counts recordTurn(Condition condition, RotationDirection direction, float speed) {
        Counts initial = Counts.fromPosition(this);
        turn(condition, direction, speed);
        Counts finial = Counts.fromPosition(this);
        return finial.subtracted(initial);
    }

    /**
     * Moves for a given number of counts on each wheel at a given speed
     * @param counts The number of counts which the wheels will move
     * @param speed The percent of maximum speed which wheels will run at, must be between -1f and 1f
     */
    void runByCounts(Counts counts, float speed) {
        leftFront.setTargetPosition(leftFront.getCurrentPosition() + counts.leftFront);
        leftBack.setTargetPosition(leftBack.getCurrentPosition() + counts.leftBack);
        rightFront.setTargetPosition(rightFront.getCurrentPosition() + counts.rightFront);
        rightBack.setTargetPosition(rightBack.getCurrentPosition() + counts.rightBack);

        leftFront.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        leftBack.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        rightFront.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        rightBack.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        setPower(counts.toCoefficient(), speed);
        while (leftFront.isBusy() && leftBack.isBusy() && rightFront.isBusy() && rightBack.isBusy()) {
            opModeInterface.idle();
        }
        stop();

        leftFront.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        leftBack.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        rightFront.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        rightBack.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
    }

    public static class Counts {
        /**
         * The number of counts for the front left wheel
         */
        public int leftFront = 0;

        /**
         * The number of counts for the back left wheel
         */
        public int leftBack = 0;

        /**
         * The number of counts for the front right wheel
         */
        public int rightFront = 0;

        /**
         * The number of counts for the back right wheel
         */
        public int rightBack = 0;

        /**
         * Default constructor dor Counts, which initializes all four wheel counts to 0
         */
        public Counts() {}

        /**
         * Creates a Counts object which holds count values for the four different wheels
         * @param leftFrontCounts The amount of counts for the front left wheel
         * @param leftBackCounts The amount of counts for the front left wheel
         * @param rightFrontCounts The amount of counts for the front left wheel
         * @param rightBackCounts The amount of counts for the front left wheel
         */
        public Counts(int leftFrontCounts, int leftBackCounts, int rightFrontCounts, int rightBackCounts) {
            this.leftFront = leftFrontCounts;
            this.leftBack = leftBackCounts;
            this.rightFront = rightFrontCounts;
            this.rightBack = rightBackCounts;
        }

        /**
         * Copies the values of the array to be the count values of the different wheels in a Counts object
         * 0th Index = Front Left
         * 1st Index = Back Left
         * 2nd Index = Front Right
         * 3rd Index = Back Right
         * @param counts The array from which to copy the different count values; Must have a size of at least 4
         */
        @NonNull
        public static Counts fromArray(int[] counts) {
            if(BuildConfig.DEBUG)
                assert counts.length >= 4 : "A wheel counts array must contain at least 4 values";
            return new Counts(counts[0], counts[1], counts[2], counts[3]);
        }

        /**
         * Makes a copy of the internal count array which holds the count values for the different wheels
         * 0th Index = Front Left
         * 1st Index = Back Left
         * 2nd Index = Front Right
         * 3rd Index = Back Right
         * @return Returns a copy of the internal counts array, which has a length of 4
         */
        public int[] toArray() {
            return new int[]{leftFront, leftBack, rightFront, rightBack};
        }

        /**
         * Returns the largest absolute counts value out of all of the wheels
         * @return The largest counts value
         */
        public int getLargestMagnitude() {
            return Math.max(Math.max(Math.abs(leftFront), Math.abs(leftBack)), Math.max(Math.abs(rightFront), Math.abs(rightBack)));
        }

        /**
         * Creates a Counts object from a Coefficient object by multiplying the coefficients by the base counts
         * @param coefficients The coefficients to use to make the counts
         * @param baseCounts The base counts to multiply the coefficients by
         * @return A new Counts object with each wheel count being its coefficient * baseCounts
         */
        @NonNull
        public static Counts fromCoefficients(@NonNull Coefficients coefficients, int baseCounts) {
            return new Counts((int) (coefficients.leftFront * baseCounts), (int) (coefficients.leftBack * baseCounts), (int) (coefficients.rightFront * baseCounts), (int) (coefficients.rightBack * baseCounts));
        }

        /**
         * Converts the counts into the wheel coefficients of proportional sizes
         * @return The converted coefficients
         */
        public Coefficients toCoefficient() {
            float magnitude = this.getLargestMagnitude();
            magnitude = magnitude == 0 ? 1 : magnitude;
            Coefficients coefficients = new Coefficients();
            coefficients.leftFront = this.leftFront / magnitude;
            coefficients.leftBack = this.leftBack / magnitude;
            coefficients.rightFront = this.rightFront / magnitude;
            coefficients.rightBack = this.rightBack / magnitude;
            return coefficients;
        }

        // Honestly I forget what I wrote this for; probably it was written as part of some brilliant part of
        // simplifying the code: But alas, the limits of a coders memory of what he wrote last night at 2am
        // Looking back at this later, it seems like it reverses the fromCoefficients() function above which
        // takes a base count values and multiples the coefficients by it (so this function can find the
        // original coefficients.
        public Coefficients toCoefficient(int baseCounts) {
            Coefficients coefficients = new Coefficients();
            float magnitude = this.getLargestMagnitude();
            if(baseCounts == 0 || magnitude == 0) {
                coefficients.leftFront = 0f;
                coefficients.leftBack = 0f;
                coefficients.rightFront = 0f;
                coefficients.rightBack = 0f;
            } else {
                magnitude = magnitude > baseCounts ? magnitude : baseCounts;
                coefficients.leftFront = this.leftFront / magnitude;
                coefficients.leftBack = this.leftBack / magnitude;
                coefficients.rightFront = this.rightFront / magnitude;
                coefficients.rightBack = this.rightBack / magnitude;
            }
            return coefficients;
        }

        /**
         * Gets the current encoder counts of the wheels
         * @param wheels The wheels to get the encoder counts
         * @return The current counts
         */
        @NonNull
        public static Counts fromPosition(@NonNull MecanumEncoderWheels wheels) {
            return new Counts(wheels.leftFront.getCurrentPosition(), wheels.leftBack.getCurrentPosition(), wheels.rightFront.getCurrentPosition(), wheels.rightBack.getCurrentPosition());
        }

        /**
         * Returns a new counts object which is the sum between this counts object and another
         * @param other The counts to add to this
         * @return The new count values
         */
        @Const
        @NonNull
        public Counts added(@NonNull Counts other) {
            return new Counts(
                    this.leftFront + other.leftFront,
                    this.leftBack + other.leftBack,
                    this.rightFront + other.rightFront,
                    this.rightBack + other.rightBack);
        }


        /**
         * Updates the count values of this to be the sum between its and another's counts values
         * @param other The counts to add to this
         */
        @NonConst
        public void add(@NonNull Counts other) {
            this.leftFront += other.leftFront;
            this.leftBack += other.leftBack;
            this.rightFront += other.rightFront;
            this.rightBack += other.rightBack;
        }

        /**
         * Returns a new counts object which is the different between this counts object and another
         * @param other The counts to subtract from this
         * @return The new count values
         */
        @Const
        @NonNull
        public Counts subtracted(@NonNull Counts other) {
            return new Counts(
                    this.leftFront - other.leftFront,
                    this.leftBack - other.leftBack,
                    this.rightFront - other.rightFront,
                    this.rightBack - other.rightBack);
        }


        /**
         * Updates the count values of this to be the difference between its and another's counts values
         * @param other The counts to subtract from this
         */
        @NonConst
        public void subtract(@NonNull Counts other) {
            this.leftFront -= other.leftFront;
            this.leftBack -= other.leftBack;
            this.rightFront -= other.rightFront;
            this.rightBack -= other.rightBack;
        }
    }
}