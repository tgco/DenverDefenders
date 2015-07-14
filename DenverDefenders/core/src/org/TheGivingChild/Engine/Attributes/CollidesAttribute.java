package org.TheGivingChild.Engine.Attributes;

import org.TheGivingChild.Engine.XML.GameObject;
import org.TheGivingChild.Engine.XML.Level;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ObjectMap;

// Continuous attribute that checks collisions with the specified other objects, and throws a corresponding condition
public class CollidesAttribute extends Attribute {
	// Object ids that this object should check for
	Array<Integer> collideIDs;

	public CollidesAttribute(ObjectMap<String, String> args) {
		super(args);
		collideIDs = new Array<Integer>();
		// create array of objects to check collisions against
		String[] split = args.get("with").split(",");
		for (String s : split) {
			collideIDs.add(Integer.parseInt(s));
		}
	}

	@Override
	public void update(Level level) {
		super.update(level);
		// Check collisions using bounding rectangle
		Rectangle myRect = new Rectangle(myObject.getX(), myObject.getY(), myObject.getWidth(), myObject.getHeight());
		for (int i = 0; i < collideIDs.size; i++) {
			GameObject other = level.getObjectOfID(collideIDs.get(i));
			if (other != null) { // object may have been removed
				Rectangle otherRect = new Rectangle(other.getX(), other.getY(), other.getWidth(), other.getHeight());
				if (myRect.overlaps(otherRect)) {
					// construct condition and throw a notification
					String condition = "collide_" + myObject.getID() + "_" + other.getID();
					level.throwCondition(condition);
				}
			}
		}
	}
}
