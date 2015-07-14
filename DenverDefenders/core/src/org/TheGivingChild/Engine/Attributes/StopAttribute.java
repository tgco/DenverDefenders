package org.TheGivingChild.Engine.Attributes;

import org.TheGivingChild.Engine.XML.Level;

import com.badlogic.gdx.utils.ObjectMap;

// Stops the objects motion, used as a triggered attribute
public class StopAttribute extends Attribute {

	public StopAttribute(ObjectMap<String, String> args) {
		super(args);
	}

	@Override
	public void update(Level level) {
		myObject.setVelocity(0, 0);
		super.update(level); // throw condition if there is one
	}

}
