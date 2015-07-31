package org.TheGivingChild.Engine.Attributes;

import org.TheGivingChild.Engine.XML.GameObject;
import org.TheGivingChild.Engine.XML.Level;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.ObjectMap;

// Sets velocity on trigger to go to the object id passed
public class MoveToObjectAttribute extends Attribute {

	public MoveToObjectAttribute(ObjectMap<String, String> args) {
		super(args);
	}
	
	@Override
	public void update(Level level) {
		// Responds to generic touch, must use arg string
		String[] coords = this.argString.split("_");
		float x = Float.parseFloat(coords[1]);
		float y = Float.parseFloat(coords[2]);
		// Rect bounds
		Rectangle rect = new Rectangle(myObject.getX(), myObject.getY(), myObject.getWidth(), myObject.getHeight());
		if (rect.contains(x, y)) {
			// throws condition
			super.update(level);
			// set velocity
			GameObject follow = level.getObjectOfID(Integer.parseInt(args.get("id")));
			Vector2 vel = new Vector2(follow.getX() - myObject.getX(), follow.getY() - myObject.getY()).nor();
			vel.scl(Float.parseFloat(args.get("speed")));
			myObject.setVelocity(vel.x, vel.y);
		} else return;
	}

}
