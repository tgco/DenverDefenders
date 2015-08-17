package org.TheGivingChild.Engine.Attributes;

import org.TheGivingChild.Engine.AudioManager;
import org.TheGivingChild.Engine.XML.Level;

import com.badlogic.gdx.utils.ObjectMap;

// Destroys the object. Typically used as a triggered attribute since object cannot be recovered without resetting the level
public class DestroyAttribute extends Attribute {

	public DestroyAttribute(ObjectMap<String, String> args) {
		super(args);
	}

	@Override
	public void update(Level level) {
		super.update(level);
		myObject.destroy();
		// play sounds
		AudioManager.getInstance().play("sounds/pop.mp3");
	}

}
