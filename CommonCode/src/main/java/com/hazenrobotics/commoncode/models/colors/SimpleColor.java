package com.hazenrobotics.commoncode.models.colors;

public enum SimpleColor implements NamedColorEnum {
	BLACK( new Color( 0, 0, 0 ) ),
	WHITE( new Color( 255, 255, 255 ) ),
	RED( new Color( 255, 0, 0 ) ),
	GREEN( new Color( 0, 255, 0 ) ),
	BLUE( new Color( 0, 0, 255 ) );

	Color color;

	SimpleColor( Color color ) {
		this.color = color;
	}

	public Color getColor( ) {
		return color;
	}

	public boolean equals( Color other ) {
		return other.equals( this );
	}
}
