package org.TheGivingChild.Engine.Attributes;

import org.TheGivingChild.Engine.XML.GameObject;
import org.TheGivingChild.Engine.XML.Level;

import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ObjectMap;

// Throws condition and destroys object (which should be a condition utility) if
// all flag attributes on the object have been set
public class FlagCheckAttribute extends Attribute {
	private Array<FlagAttribute> check;

	public FlagCheckAttribute(ObjectMap<String, String> args) {
		super(args);
		check = new Array<FlagAttribute>();
	}
	
	@Override
	public void update(Level level) {
		for (int i = 0; i < check.size; i++) {
			if (!check.get(i).isSet()) return;
		}
		// All have been set if reach here
		super.update(level);
		myObject.destroy();
	}
	
	// Get references to flag attribs
	@Override
	public void setup(GameObject myObject) {
		super.setup(myObject);
		check.clear();
		for (int i = 0; i < myObject.getTriggeredAttributes().size; i++) {
			if (myObject.getTriggeredAttributes().get(i).getClass() == FlagAttribute.class)
				check.add((FlagAttribute) myObject.getTriggeredAttributes().get(i));
		}
	}

}
