package org.TheGivingChild.Engine.Attributes;

import org.TheGivingChild.Engine.XML.GameObject;
import org.TheGivingChild.Engine.XML.Level;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.ObjectMap;

// Destroys if trigger is thrown and a touch exists on the object this attribute belongs to
public class TapDestroyAttribute extends Attribute {

	public TapDestroyAttribute(ObjectMap<String, String> args) {
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
			myObject.destroy();
		} else return;
	}

}
