package org.TheGivingChild.Engine.Attributes;

import org.TheGivingChild.Engine.XML.GameObject;
import org.TheGivingChild.Engine.XML.Level;

import com.badlogic.gdx.utils.ObjectMap;

public class PlaceAtObjectAttribute extends Attribute {

	public PlaceAtObjectAttribute(ObjectMap<String, String> args) {
		super(args);
	}
	
	@Override
	public void update(Level level) {
		super.update(level);
		// Get object to move to
		GameObject moveTo = level.getObjectOfID(Integer.parseInt(args.get("id")));
		// Find center
		float centerX = moveTo.getX() + moveTo.getWidth()/2f;
		float centerY = moveTo.getY() + moveTo.getHeight()/2f;
		// place at
		myObject.setPosition(centerX - myObject.getWidth()/2f, centerY);
	}

}
