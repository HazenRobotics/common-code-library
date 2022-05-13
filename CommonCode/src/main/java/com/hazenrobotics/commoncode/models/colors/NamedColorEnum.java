package com.hazenrobotics.commoncode.models.colors;

public interface NamedColorEnum {

	/**
	 * Gets the color of this named color
	 *
	 * @return The color object the named color represents
	 */
	Color getColor( );

	/**
	 * Compares if this named color is the same as another color
	 *
	 * @param other The color to compare to
	 * @return If the colors represent the same thing
	 */
	boolean equals( Color other );
}
