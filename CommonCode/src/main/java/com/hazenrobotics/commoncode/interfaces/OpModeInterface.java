package com.hazenrobotics.commoncode.interfaces;

/**
 * An interface for accessing the hardware, input, idling, and status of an OpMode.
 */
public interface OpModeInterface extends HardwareInterface, GamepadInterface, IdleInterface {

	/**
	 * Checks if the OpMode this interface is accessing should still be active. If the OpMode is no
	 * longer active, any loops currently calling this function should break and stop.
	 *
	 * @return True if the OpMode is currently active, False if the OpMode needs to or is stopped.
	 */
	boolean opModeIsActive( );
}