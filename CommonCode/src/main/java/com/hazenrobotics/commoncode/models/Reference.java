package com.hazenrobotics.commoncode.models;

/**
 * An abstract class to be used to capture a reference to a variable through an anonymous class
 *
 * @param <E> The type of the referenced being capture
 */
public abstract class Reference<E> {

	/**
	 * Can be overridden in an anonymous class to give read access to a variable
	 *
	 * @return The value of the variable being referenced
	 */
	public abstract E get( );

	/**
	 * Can be overridden in an anonymous class to give write access to a variable
	 *
	 * @param value The value to set the variable being referenced to
	 */
	public abstract void set( E value );
}
