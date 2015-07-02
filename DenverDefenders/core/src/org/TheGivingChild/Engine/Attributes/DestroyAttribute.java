package org.TheGivingChild.Engine.Attributes;

import org.TheGivingChild.Engine.XML.Level;

import com.badlogic.gdx.utils.ObjectMap;

// Destroys the object.  Used as a triggered attribute
public class DestroyAttribute extends Attribute {

	public DestroyAttribute(ObjectMap<String, String> args) {
		super(args);
	}

	@Override
	public void update(Level level) {
		super.update(level);
		myObject.dispose();
	}

}
