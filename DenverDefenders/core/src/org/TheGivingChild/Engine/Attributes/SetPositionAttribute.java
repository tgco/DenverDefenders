package org.TheGivingChild.Engine.Attributes;

import org.TheGivingChild.Engine.XML.Level;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.ObjectMap;

// Sets position on trigger
public class SetPositionAttribute extends Attribute {

	public SetPositionAttribute(ObjectMap<String, String> args) {
		super(args);
	}
	
	@Override
	public void update(Level level) {
		float x = Float.parseFloat(args.get("x", String.valueOf(myObject.getX())));
		float y = Float.parseFloat(args.get("y", String.valueOf(myObject.getY())));
		// Scale
		x = Gdx.graphics.getWidth()/1024f;
		y = Gdx.graphics.getHeight()/600f;
		myObject.setPosition(x, y);
	}
}
