package com.hazenrobotics.commoncode.interfaces;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DigitalChannel;
import com.qualcomm.robotcore.hardware.HardwareDevice;
import com.qualcomm.robotcore.hardware.Servo;

/**
 * An interface for different functions to gain access to hardware on the robot
 */
public interface HardwareInterface {

	/**
	 * Gets the DcMotor with the given name connected to the robot hardware
	 *
	 * @param name The name for which to look for a DcMotor
	 * @return The motor found with the given name
	 */
	DcMotor getMotor( String name );

	/**
	 * Gets the Servo with the given name connected to the robot hardware
	 *
	 * @param name The name for which to look for a Servo
	 * @return The servo found with the given name
	 */
	Servo getServo( String name );

	/**
	 * Gets the DigitalChannel with the given name connected to the robot hardware
	 *
	 * @param name The name for which to look for a DigitalChannel
	 * @return The channel found with the given name
	 */
	DigitalChannel getDigitalChannel( String name );

	/**
	 * Gets the generic HardwareDevice with the given name connected to the robot hardware
	 *
	 * @param name The name for which to look for a generic HardwareDevice
	 * @return The device found with the given name
	 */
	HardwareDevice get( String name );
}