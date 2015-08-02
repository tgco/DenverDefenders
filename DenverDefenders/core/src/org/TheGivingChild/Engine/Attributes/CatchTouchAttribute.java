package org.TheGivingChild.Engine.Attributes;

import org.TheGivingChild.Engine.XML.GameObject;
import org.TheGivingChild.Engine.XML.Level;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.ObjectMap;

public class CatchTouchAttribute extends Attribute {

	public CatchTouchAttribute(ObjectMap<String, String> args) {
		super(args);
	}

	@Override
	public void update(Level level) {
		// Responds to generic touch, must use arg string
		if (argString != null && argString.split("_")[0].equals("touch")) {
			String[] coords = this.argString.split("_");
			float x = Float.parseFloat(coords[1]);
			float y = Float.parseFloat(coords[2]);
			// Rect bounds
			Rectangle rect = new Rectangle(myObject.getX(), myObject.getY(), myObject.getWidth(), myObject.getHeight());
			if (rect.contains(x, y)) {
				// throws condition
				super.update(level);
				// Eat up event
				argString = null;
				// Alert it was handled
				for (int i = 0; i < level.getGameObjects().size; i++) {
					GameObject o = level.getGameObjects().get(i);
					if( o.getID() == myObject.getID()) continue;
					for (Attribute att : o.getContinuousAttributes()) {
						att.setArgString(null);
					}
				}
			} else return;
		} else return;
	}

}
