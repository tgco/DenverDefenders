package org.TheGivingChild.Engine.Attributes;

import org.TheGivingChild.Engine.XML.Level;

import com.badlogic.gdx.utils.ObjectMap;

// Freezes position of object when trigger thrown
public class FreezeAttribute extends Attribute {

	public FreezeAttribute(ObjectMap<String, String> args) {
		super(args);
	}
	
	@Override
	public void update(Level level) {
		super.update(level);
		myObject.freeze();
	}

}
