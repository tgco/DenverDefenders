package org.TheGivingChild.Engine.Attributes;

import org.TheGivingChild.Engine.XML.Level;

import com.badlogic.gdx.utils.ObjectMap;

public class GravityAttribute extends Attribute {

	public GravityAttribute(ObjectMap<String, String> args) {
		super(args);
	}

	@Override
	public void update(Level level) {
		float rate = Float.parseFloat(args.get("ay"));
		myObject.setVelocity(new float[] {myObject.getVelocity()[0],myObject.getVelocity()[1]-rate});
	}
}
