package com.hazenrobotics.commoncode.interfaces;

import com.qualcomm.robotcore.hardware.Gamepad;

/**
 * An interface for different functions to gain access to an OpModes gamepad controllers
 */
public interface GamepadInterface {

    /**
     * Accesses gamepad1 (of an OpMode)
     * @return The first gamepad object
     */
    Gamepad getGamepad1();

    /**
     * Accesses gamepad2 (of an OpMode)
     * @return The second gamepad object
     */
    Gamepad getGamepad2();
}