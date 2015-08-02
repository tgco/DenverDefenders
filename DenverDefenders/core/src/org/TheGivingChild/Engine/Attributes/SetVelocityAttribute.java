package org.TheGivingChild.Engine.Attributes;

import org.TheGivingChild.Engine.XML.Level;

import com.badlogic.gdx.utils.ObjectMap;

public class SetVelocityAttribute extends Attribute {

	public SetVelocityAttribute(ObjectMap<String, String> args) {
		super(args);
	}
	
	@Override
	public void update(Level level) {
		super.update(level);
		float vx = Float.parseFloat(args.get("vx", String.valueOf(myObject.getVelocity()[0])));
		float vy = Float.parseFloat(args.get("vy", String.valueOf(myObject.getVelocity()[1])));
		myObject.setVelocity(vx, vy);
	}

}
