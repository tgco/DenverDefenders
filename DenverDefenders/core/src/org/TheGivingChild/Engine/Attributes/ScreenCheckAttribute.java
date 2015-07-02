package org.TheGivingChild.Engine.Attributes;

import org.TheGivingChild.Engine.XML.Level;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.ObjectMap;

// Checks if object is off of the selected sides of the screen and throws the selected condition if true
public class ScreenCheckAttribute extends Attribute {
	private String side;
	
	public ScreenCheckAttribute(ObjectMap<String, String> args) {
		super(args);
		this.side = args.get("side");
	}

	@Override
	public void update(Level level) {
		boolean offScreen = false;
		if (side.equals("top")) {
			if (myObject.getY() > Gdx.graphics.getHeight()) offScreen = true;
		}
		else if (side.equals("bottom")) {
			if (myObject.getY() + myObject.getTextureHeight() < 0) offScreen = true;
		}
		else if (side.equals("left")) {
			if (myObject.getX() - myObject.getTextureWidth() < 0) offScreen = true;
		}
		else if (side.equals("right")) {
			if (myObject.getX() > Gdx.graphics.getWidth()) offScreen = true;
		}
		
		// Throw condition
		if (offScreen) {
			super.update(level);
		}
	}

}
