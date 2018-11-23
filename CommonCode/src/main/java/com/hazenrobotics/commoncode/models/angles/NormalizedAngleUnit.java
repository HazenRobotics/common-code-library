/*
Modifications to file Copyright (c) 2018 Hazen Robotics

Modifications include change to normalization, code style, class reference names,
added interface and sdk AngleUnit"s compatibility, and added/changed java doc.

Original Licence:
Copyright (c) 2016 Robert Atkinson

All rights reserved.

Redistribution and use in source and binary forms, with or without modification,
are permitted (subject to the limitations in the disclaimer below) provided that
the following conditions are met:

Redistributions of source code must retain the above copyright notice, this list
of conditions and the following disclaimer.

Redistributions in binary form must reproduce the above copyright notice, this
list of conditions and the following disclaimer in the documentation and/or
other materials provided with the distribution.

Neither the name of Robert Atkinson nor the names of his contributors may be used to
endorse or promote products derived from this software without specific prior
written permission.

NO EXPRESS OR IMPLIED LICENSES TO ANY PARTY'S PATENT RIGHTS ARE GRANTED BY THIS
LICENSE. THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
"AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO,
THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESSFOR A PARTICULAR PURPOSE
ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE
FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR
TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF
THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
*/
package com.hazenrobotics.commoncode.models.angles;

/**
 * An {@link AngleUnit} represents angles in different units of measure and
 * provides utility methods to convert across units. {@link AngleUnit} does not
 * maintain angle information information internally, but only helps organize
 * and use angle measures that may be maintained separately across various contexts.
 * <p>
 * Angles can be distinguished along (at least) two axes:
 *  <ol>
 *      <li>the fundamental unit (radians vs degrees)</li>
 *      <li>whether the angular quantity is isNormalized or not to the range of [-180,+180) degrees</li>
 *  </ol>
 *  Normalized angles are of most utility when dealing with physical angles, as normalization
 *  removes ambiguity of representation. In particular, two angles can be compared for equality
 *  by subtracting them, normalizing, and testing whether the absolute value of the result is
 *  smaller than some tolerance threshold. This approach neatly handles all cases of cyclical
 *  wrapping without unexpected discontinuities.
 * </p>
 * <p>
 *  Unnormalized angles can be handy when the angular quantity is not a physical angle but some
 *  related quantity such as an angular <em>velocity</em> or <em>acceleration</em>, where the
 *  quantity in question lacks the 360-degree cyclical equivalence of a physical angle.
 * </p>
 * <p>
 *  {@link NormalizedAngleUnit} expresses isNormalized angles, while {@link UnnormalizedAngleUnit} expresses unnormalized ones
 * </p>
 */
@SuppressWarnings("WeakerAccess")
public enum NormalizedAngleUnit implements AngleUnit
{
    DEGREES(), RADIANS();

    protected static final double TwoPi   = 2 * Math.PI;
    public    static final float  Pif     = (float) Math.PI;

    //----------------------------------------------------------------------------------------------
    // Primitive operations
    //----------------------------------------------------------------------------------------------

    @Override
    public double fromDegrees(double degrees) {
        return normalize(this.getUnnormalized().fromDegrees(degrees));
    }

    @Override
    public float fromDegrees(float degrees) {
        return normalize(this.getUnnormalized().fromDegrees(degrees));
    }

    @Override
    public double fromRadians(double radians) {
        return normalize(this.getUnnormalized().fromRadians(radians));
    }

    @Override
    public float fromRadians(float radians) {
        return normalize(this.getUnnormalized().fromRadians(radians));
    }

    @Override
    public double fromUnit(AngleUnit them, double theirs) {
        return normalize(this.getUnnormalized().fromUnit(them, theirs));
    }

    @Override
    public float fromUnit(AngleUnit them, float theirs) {
        return normalize(this.getUnnormalized().fromUnit(them, theirs));
    }

    //----------------------------------------------------------------------------------------------
    // Derived operations
    //----------------------------------------------------------------------------------------------

    @Override
    public double toDegrees(double inOurUnits) {
        return DEGREES.fromUnit(this, inOurUnits);
    }

    @Override
    public float toDegrees(float inOurUnits) {
        return DEGREES.fromUnit(this, inOurUnits);
    }

    @Override
    public double toRadians(double inOurUnits) {
        return RADIANS.fromUnit(this, inOurUnits);
    }

    @Override
    public float toRadians(float inOurUnits) {
        return RADIANS.fromUnit(this, inOurUnits);
    }

    //----------------------------------------------------------------------------------------------
    // Normalization
    //----------------------------------------------------------------------------------------------

    @Override
    public double normalize(double mine) {
        switch (this) {
            default:
            case RADIANS:               return normalizeRadians(mine);
            case DEGREES:               return normalizeDegrees(mine);
        }
    }

    @Override
    public float normalize(float mine) {
        switch (this) {
            default:
            case RADIANS:               return normalizeRadians(mine);
            case DEGREES:               return normalizeDegrees(mine);
        }
    }

    public static double normalizeDegrees(double degrees) {
        while (degrees >= 360.0) degrees -= 360.0;
        while (degrees < 0.0) degrees += 360.0;
        return degrees;
    }

    public static float normalizeDegrees(float degrees) {
        return (float)normalizeDegrees((double)degrees);
    }

    public static double normalizeRadians(double radians) {
        while (radians >= TwoPi) radians -= TwoPi;
        while (radians < 0) radians += TwoPi;
        return radians;
    }

    public static float normalizeRadians(float radians) {
        return (float)normalizeRadians((double)radians);
    }

    @Override
    public UnnormalizedAngleUnit getUnnormalized() {
        switch (this) {
            default:
            case RADIANS:   return UnnormalizedAngleUnit.RADIANS;
            case DEGREES:   return UnnormalizedAngleUnit.DEGREES;
        }
    }

    @Override
    public NormalizedAngleUnit getNormalized() {
        return this;
    }

    public org.firstinspires.ftc.robotcore.external.navigation.AngleUnit toFtcAngleUnit() {
        switch (this) {
            default:
            case RADIANS: return org.firstinspires.ftc.robotcore.external.navigation.AngleUnit.RADIANS;
            case DEGREES: return org.firstinspires.ftc.robotcore.external.navigation.AngleUnit.DEGREES;
        }
    }

    public static NormalizedAngleUnit fromFtcAngleUnit(org.firstinspires.ftc.robotcore.external.navigation.AngleUnit ftcUnit) {
        switch (ftcUnit) {
            default:
            case RADIANS: return NormalizedAngleUnit.RADIANS;
            case DEGREES: return NormalizedAngleUnit.DEGREES;
        }
    }
}