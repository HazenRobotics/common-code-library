package com.hazenrobotics.commoncode.models.angles.directions;

public enum RotationDirection {
	CLOCKWISE,
	COUNTER_CLOCKWISE;

	/**
	 * Gets the inverted rotation direction (so Clockwise would return Counter-clockwise and
	 * vise-versa.)
	 *
	 * @return The inverted direction of this
	 */
	public RotationDirection inverted( ) {
		return this == CLOCKWISE ? COUNTER_CLOCKWISE : CLOCKWISE;
	}
}