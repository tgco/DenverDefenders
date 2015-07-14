package org.TheGivingChild.Engine.Attributes;

import org.TheGivingChild.Engine.XML.Level;

import com.badlogic.gdx.utils.ObjectMap;

// Set the lowest y value this object can have.  Motion stops when this value is reached
public class FloorAttribute extends Attribute {
	private float floorY;

	public FloorAttribute(ObjectMap<String, String> args) {
		super(args);
		this.floorY = Float.parseFloat(args.get("y"));
	}

	@Override
	public void update(Level level) {
		if (myObject.getY() <= floorY) {
			super.update(level);
			// stop and place just above to avoid repeated collision
			myObject.setVelocity(0, 0);
			myObject.setPosition(myObject.getX(), floorY + 1);
		}
	}

}
