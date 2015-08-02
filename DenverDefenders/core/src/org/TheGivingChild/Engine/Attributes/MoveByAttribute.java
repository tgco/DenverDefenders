package org.TheGivingChild.Engine.Attributes;

import org.TheGivingChild.Engine.XML.Level;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.ObjectMap;

public class MoveByAttribute extends Attribute {

	public MoveByAttribute(ObjectMap<String, String> args) {
		super(args);
	}
	
	@Override
	public void update(Level level) {
		super.update(level);
		// Move by the desired amount
		float dx = Float.parseFloat(args.get("dx", "0"));
		float dy = Float.parseFloat(args.get("dy", "0"));
		float x = myObject.getX();
		float y = myObject.getY();
		
		// scale
		dx = dx * Gdx.graphics.getWidth()/1024f;
		dy = dy * Gdx.graphics.getHeight()/600f;
		myObject.setPosition(x + dx, y+ dy);
	}

}
