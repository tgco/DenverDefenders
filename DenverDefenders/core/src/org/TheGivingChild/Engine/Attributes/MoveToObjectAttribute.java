package org.TheGivingChild.Engine.Attributes;

import org.TheGivingChild.Engine.AudioManager;
import org.TheGivingChild.Engine.XML.GameObject;
import org.TheGivingChild.Engine.XML.Level;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.ObjectMap;

// Sets velocity on trigger to go to the object id passed
public class MoveToObjectAttribute extends Attribute {

	public MoveToObjectAttribute(ObjectMap<String, String> args) {
		super(args);
	}
	
	@Override
	public void update(Level level) {
		// throws condition
		super.update(level);
		// set velocity
		GameObject follow = level.getObjectOfID(Integer.parseInt(args.get("id")));
		Vector2 vel = new Vector2(follow.getX() - myObject.getX(), follow.getY() - myObject.getY()).nor();
		vel.scl(Float.parseFloat(args.get("speed")));
		myObject.setVelocity(vel.x, vel.y);
		
		AudioManager.getInstance().play("sounds/pop.mp3");
	}

}
