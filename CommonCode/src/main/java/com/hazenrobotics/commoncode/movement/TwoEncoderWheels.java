package com.hazenrobotics.commoncode.movement;

import com.hazenrobotics.commoncode.interfaces.OpModeInterface;
import com.hazenrobotics.commoncode.models.angles.Angle;
import com.hazenrobotics.commoncode.models.angles.UnnormalizedAngleUnit;
import com.hazenrobotics.commoncode.models.angles.directions.RotationDirection;
import com.hazenrobotics.commoncode.models.angles.directions.SimpleDirection;
import com.hazenrobotics.commoncode.models.conditions.Condition;
import com.hazenrobotics.commoncode.models.distances.Distance;
import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;

/**
 * Controller for a basic Two-Wheels configuration (one on each side of the bot) with encoders.
 * This allow a bot to move forward, turn, and curve and control the degree of such movements using the
 * encoders attached to the motors, allowing for measured movements.
 * Using this two wheel motor configuration is preferred when using encoders over
 * {@link MecanumEncoderWheels Mecanum Encoder Wheels} because the wheels tend to not slip, making
 * the encoders more reliable.
 * @see TwoWheels
 * @see MecanumEncoderWheels
 */
@SuppressWarnings("unused,WeakerAccess")
public class TwoEncoderWheels extends TwoWheels implements EncoderWheels {
    protected EncoderConfiguration encoderConfig;


    /**
     * Initializes the class to use the two wheels with the given configuration of names, speed, and
     * encoder settings.
     * @param opModeInterface An interface from which the wheel motors can be accessed
     * @param leftName Name of the left wheel in the robot configuration
     * @param rightName Name of the right wheel in the robot configuration
     * @param encoderConfig A configuration of the robots wheels and dimensions in order to properly
     *                      use the encoders to calculate measured moves
     * @param speeds The speed settings to use for the different movement types
     */
    public TwoEncoderWheels(OpModeInterface opModeInterface, String leftName, String rightName, EncoderConfiguration encoderConfig, SpeedSettings speeds) {
        super(opModeInterface, leftName, rightName, speeds);
        this.encoderConfig = encoderConfig;
    }

    /**
     * Initializes the class to use the two wheels with the given configuration of names and
     * encoder settings.
     * @param opModeInterface An interface from which the wheel motors can be accessed
     * @param leftName Name of the left wheel in the robot configuration
     * @param rightName Name of the right wheel in the robot configuration
     */
    public TwoEncoderWheels(OpModeInterface opModeInterface, String leftName, String rightName, EncoderConfiguration encoderConfig) {
        this(opModeInterface, leftName, rightName, encoderConfig, DEFAULT_SPEEDS);
    }

    @Override
    public void move(Distance distance, SimpleDirection direction) {
        move(distance, SimpleDirection.FORWARDS, speeds.move);
    }

    /**
     * Moves directly forward for a given distance using encoders without any turning at a given
     * speed.
     *@param distance The distance to move; a negative distance will invert the given movement
     *                 direction
     * @param direction The direction in which to move towards
     * @param speed The percent of maximum speed which wheels will turn at, should be between 0f and
     *              1f
     */
    public void move(Distance distance, SimpleDirection direction, float speed) {
        /*
        If the distance is positive, leave the direction unchanged, otherwise: invert it so that
        movement power can be calculated just from direction not the sign of the direction.
        */
        direction = distance.isPositive() ? direction : direction.inverted();
        distance = distance.positivized();

        int baseCounts = encoderConfig.getMoveCounts(distance);
        Counts counts = Counts.fromCoefficients(calculateMove(direction), baseCounts);
        runByCounts(counts, Math.abs(speed));
    }

    /**
     * Records how much each of the wheel counts changed during a given movement and returns it
     * @param condition Will move until this condition is true
     * @param direction Determines if the wheels will move forward or backwards
     * @return The change in each wheels count value
     */
    public Counts recordMove(Condition condition, SimpleDirection direction) {
        return recordMove(condition, direction, speeds.move);
    }

    /**
     * Records how much each of the wheel counts changed during a given movement and returns it
     * @param condition Will move until this condition is true
     * @param direction Determines if the wheels will move forward or backwards
     * @param speed The speed at which the move being recorded will be taken
     * @return The change in each wheels count value
     */
    public Counts recordMove(Condition condition, SimpleDirection direction, float speed) {
        Counts initial = Counts.fromPosition(this);
        move(condition, direction, speed);
        Counts finial = Counts.fromPosition(this);
        return finial.subtracted(initial);
    }

    @Override
    public void turn(Angle angle, RotationDirection direction) {
        turn(angle, direction, speeds.turn);
    }

    /**
     * Turns for a given angle using encoders.
     * @param angle The angle for which to turn; a negative angle inverts the given rotation
     *              direction
     * @param direction The rotation direction in which to turn towards
     * @param speed The percent of maximum speed which wheels will turn at, should be between 0f and
     *              1f
     */
    public void turn(Angle angle, RotationDirection direction, float speed) {
        /*
        If the distance is positive, leave the direction unchanged, otherwise: invert it so that
        movement power can be calculated just from direction not the sign of the direction.
        */
        direction = angle.isPositive() ? direction : direction.inverted();
        angle = angle.positivized();

        int baseCounts = encoderConfig.getTurnCounts(angle);
        Counts counts = Counts.fromCoefficients(calculateTurn(direction), baseCounts);
        runByCounts(counts, Math.abs(speed));
    }

    /**
     * Records how much each of the wheel counts changed during a given turn and returns it
     * @param condition Will move until this condition is true
     * @param direction Determines what direction the turn will be towards
     * @param speed The percent of maximum speed which wheels will turn at, should be between 0f and
     *              1f
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
     * @param speed The percent of maximum speed which wheels will turn at, should be between 0f and
     *              1f
     */
    void runByCounts(Counts counts, float speed) {
        //If a negative speed was passed here, we are moving backwards overall
        counts = speed < 0 ? counts.negated() : counts;
        speed = Math.abs(speed);

        left.setTargetPosition(left.getCurrentPosition() + counts.left);
        right.setTargetPosition(right.getCurrentPosition() + counts.right);

        left.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        right.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        setPower(counts.toCoefficient(), speed);
        while (left.isBusy() && right.isBusy() && opModeInterface.opModeIsActive()) {
            opModeIsActive.getTelemetry().addData("Left Target", left.getTargetPosition());
            opModeIsActive.getTelemetry().addData("Right Target", right.getTargetPosition());
            opModeIsActive.getTelemetry().addData("Left Position", left.getCurrentPosition());
            opModeIsActive.getTelemetry().addData("Right Position", right.getCurrentPosition());
            opModeIsActive.getTelemetry().addData("Left Busy", left.isBusy());
            opModeIsActive.getTelemetry().addData("Right Busy", right.isBusy());
            opModeIsActive.getTelemetry().update();
            opModeInterface.idle();
        }
        stop();

        left.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        right.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
    }

    public static class Counts {
        /** The number of counts for the left wheel */
        public int left = 0;

        /** The number of counts for the right wheel */
        public int right = 0;

        /**
         * Default constructor for Counts, which initializes both wheel counts to 0
         */
        public Counts() {}

        /**
         * Creates a Counts object which holds count values for the two wheels
         * @param leftCounts The amount of counts for the left wheel
         * @param rightCounts The amount of counts for the right wheel
         */
        public Counts(int leftCounts, int rightCounts) {
            this.left = leftCounts;
            this.right = rightCounts;
        }

        /**
         * Returns the largest absolute counts value out of both of the wheels
         * @return The largest counts value
         */
        public int getLargestMagnitude() {
            return Math.max(Math.abs(left), Math.abs(right));
        }

        /**
         * Creates a Counts object from a Coefficient object by multiplying the coefficients by the
         * base counts
         * @param coefficients The coefficients to use to make the counts
         * @param baseCounts The base counts to multiply the coefficients by
         * @return A new Counts object with each wheel count being its coefficient * baseCounts
         */
        public static Counts fromCoefficients(Coefficients coefficients, int baseCounts) {
            return new Counts((int) (coefficients.left * baseCounts), (int) (coefficients.right * baseCounts));
        }

        /**
         * Converts the counts into the wheel coefficients of proportional sizes
         * @return The converted coefficients
         */
        public Coefficients toCoefficient() {
            float magnitude = this.getLargestMagnitude();
            magnitude = magnitude == 0 ? 1 : magnitude;
            Coefficients coefficients = new Coefficients();
            coefficients.left = this.left / magnitude;
            coefficients.right = this.right / magnitude;
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
                coefficients.left = 0f;
                coefficients.right = 0f;
            } else {
                magnitude = magnitude > baseCounts ? magnitude : baseCounts;
                coefficients.left = this.left / magnitude;
                coefficients.right = this.right / magnitude;
            }
            return coefficients;
        }

        /**
         * Gets the current encoder counts of the wheels
         * @param wheels The wheels to get the encoder counts
         * @return The current counts
         */
        public static Counts fromPosition(TwoEncoderWheels wheels) {
            return new Counts(wheels.left.getCurrentPosition(), wheels.right.getCurrentPosition());
        }

        /**
         * Returns a new counts object which is the sum between this counts object and another
         * @param other The counts to add to this
         * @return The new count values
         */
        public Counts added(Counts other) {
            return new Counts(
                    this.left + other.left,
                    this.right + other.right);
        }


        /**
         * Updates the count values of this to be the sum between its and another's counts values
         * @param other The counts to add to this
         * @return These counts
         */
        public Counts add(Counts other) {
            this.left += other.left;
            this.right += other.right;
            return this;
        }

        /**
         * Returns a new counts object which is the different between this counts object and another
         * @param other The counts to subtract from this
         * @return The new count values
         */
        public Counts subtracted(Counts other) {
            return new Counts(
                    this.left - other.left,
                    this.right - other.right);
        }


        /**
         * Updates the count values of this to be the difference between its and another's counts values
         * @param other The counts to subtract from this
         * @return These counts
         */
        public Counts subtract(Counts other) {
            this.left -= other.left;
            this.right -= other.right;
            return this;
        }

        /**
         * Updates the count values of this with each of its count values the opposite sign of their
         * previous value.
         * @return The new count values
         */
        public Counts negated() {
            return new Counts(
                    -this.left,
                    -this.right);
        }

        /**
         * Returns a new counts object with each of its count values the opposite sign of these
         * counts.
         * @return These counts
         */
        public Counts negate() {
            left = -left;
            right = -right;
            return this;
        }
    }

    /**
     * Stores details related to the encoders in order to generate counts from movement distances
     * and angles.
     */
    public static class EncoderConfiguration {

        protected static final DistanceUnit DISTANCE_UNIT = DistanceUnit.INCH;
        protected final float countsPerUnit;
        /*
         * Can also be interpreted as the robots turning circumference. Its just the distance the
         * wheels need to move in one direction to make one full rotation (or spin in this cause in
         * order to have more differentiable naming compared to counts per revolution for motors)
         */
        protected final Distance distancePerSpin;

        /**
         * A multiple by which the counts must be multiplied to accurate travel the distance
         * requested using getCounts(). This is because the wheels might slip while travel, so this
         * multiplier accounts for that.
         */
        protected float calibrationMultiplier;

        /**
         * Creates an new encoder configuration for a two wheel movement type.
         * @param countsPerRev The number of counts the encoders for the motor in use have per
         *                     revolution of the output shaft of the motor
         * @param wheelDiameter The diameter of the wheel which is being spun by the motor
         * @param robotDiameter The diameter of the robot, specifically being the distance between
         *                      the two wheels
         * @param calibrationMultiplier A multiplier of what percentage of the correct distance the
         *                              wheels go on a calibration test, should be greater than 0 -
         *                              Defaults to 1f when omitted
         */
        public EncoderConfiguration(int countsPerRev, Distance wheelDiameter, Distance robotDiameter, float calibrationMultiplier) {
            setCalibrationMultiplier(calibrationMultiplier);
            Distance wheelCircumference = wheelDiameter.multiplied((float) Math.PI);
            distancePerSpin = robotDiameter.multiplied((float) Math.PI);

            countsPerUnit = (countsPerRev / wheelCircumference.getValue(DISTANCE_UNIT));
        }

        /**
         * Creates an new encoder configuration for a two wheel movement type. Wheels calibration
         * defaults to 1.0f (which will assume perfect accuracy.)
         * @param countsPerRev The number of counts the encoders for the motor in use have per
         *                     revolution of the output shaft of the motor
         * @param wheelDiameter The diameter of the wheel which is being spun by the motor
         * @param robotDiameter The diameter of the robot, specifically being the distance between
         *                      the two wheels
         */
        public EncoderConfiguration(int countsPerRev, Distance wheelDiameter, Distance robotDiameter) {
            this(countsPerRev, wheelDiameter, robotDiameter, 1.0f);
        }

        /**
         * Gets the number of counts required to go the specified distance.
         * @param moveDistance The distance that needs to be moved
         * @return The counts required to go the distance, including wheel calibration adjustment
         */
        public int getMoveCounts(Distance moveDistance) {
            return (int) (moveDistance.getValue(DISTANCE_UNIT) * countsPerUnit * calibrationMultiplier);
        }

        /**
         * Gets the number of counts to turn the specified angle.
         * @param turnAngle The angle that needs to be turned
         * @return The counts required to go the turn angle, including wheel calibration adjustment
         */
        public int getTurnCounts(Angle turnAngle) {
            float spins = turnAngle.divided(new Angle(360, UnnormalizedAngleUnit.DEGREES));
            Distance distance = distancePerSpin.multiplied(spins);

            return getMoveCounts(distance);
        }

        /**
         * Changes the calibration multiplier, which is used to adjust the amount of counts
         * which will be moved to account for predictable error.
         * @param calibrationMultiplier Updates the calibration multiplier to a new number, which
         *                              should be greater than 0
         */
        public void setCalibrationMultiplier(float calibrationMultiplier) {
            this.calibrationMultiplier = calibrationMultiplier > 0 ? calibrationMultiplier : 1f;
        }

        /**
         * Gets the current calibration multiplier, which is used to adjust the amount of counts
         * which will be moved to account for predictable error.
         * @return The current calibration multiplier, which should be greater than zero
         */
        public float getCalibrationMultiplier() {
            return calibrationMultiplier;
        }
    }
}
