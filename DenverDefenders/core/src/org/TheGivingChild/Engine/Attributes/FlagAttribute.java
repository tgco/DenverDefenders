package org.TheGivingChild.Engine.Attributes;

import org.TheGivingChild.Engine.XML.GameObject;
import org.TheGivingChild.Engine.XML.Level;

import com.badlogic.gdx.utils.ObjectMap;

// Bool flag wrapped in an object
public class FlagAttribute extends Attribute {
	private boolean flag;
	
	public FlagAttribute(ObjectMap<String, String> args) {
		super(args);
	}
	
	@Override
	public void setup(GameObject myObject) {
		super.setup(myObject);
		flag = false;
	}
	
	@Override
	public void update(Level level) {
		super.update(level);
		flag = true;
	}
	
	public boolean isSet() {
		return flag;
	}
}
