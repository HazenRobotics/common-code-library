package com.hazenrobotics.commoncode.input;

/**
 * A kind of button that is activated until pressed again, rather than being active only for the duration of the press.
 * 
 * @see Button#toToggle()
 */
public abstract class Toggle extends Button {
	protected boolean isEnabled = false;
	
	@Override
	public void onPress() {
		this.isEnabled = !this.isEnabled;
		if(this.isEnabled) onActivate();
		else onDeactivate();
	}
	
	@Override
	public final void onRelease() { }
	
	/** Called when the toggle is activated. */
	public abstract void onActivate();
	/** Called when the toggle is deactivated. */
	public abstract void onDeactivate();
}