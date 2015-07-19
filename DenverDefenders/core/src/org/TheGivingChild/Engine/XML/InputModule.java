package org.TheGivingChild.Engine.XML;

import com.badlogic.gdx.Gdx;

// Sends generic input events to a mini game level by polling for events
public class InputModule {
	// Checks for input and throws an appropriate condition
	public void poll(Level level) {
		if (Gdx.input.justTouched()) {
			// Get screen coordinates
			float x = Gdx.input.getX();
			float y = Gdx.graphics.getHeight() - Gdx.input.getY();
			// Create and send an event
			level.throwInputCondition("touch_" + x + "_" + y);
		}
	}
}
