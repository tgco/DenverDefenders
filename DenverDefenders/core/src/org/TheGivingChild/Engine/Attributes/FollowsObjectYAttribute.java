package org.TheGivingChild.Engine.Attributes;

import org.TheGivingChild.Engine.XML.GameObject;
import org.TheGivingChild.Engine.XML.Level;

import com.badlogic.gdx.utils.ObjectMap;

// Auto sets velocity to follow the y value of the desired object id
public class FollowsObjectYAttribute extends Attribute {
	private int idToFollow;
	private float followSpeed;
	
	public FollowsObjectYAttribute(ObjectMap<String, String> args) {
		super(args);
		idToFollow = Integer.parseInt(args.get("id"));
		followSpeed = Float.parseFloat(args.get("v"));
	}

	@Override
	public void update(Level level) {
		super.update(level);
		//find the object we want to follow
		GameObject follow = level.getObjectOfID(idToFollow); // REFACTOR: O(n) lookup could be reduced to constant time if a reference to the ob is cached

		//object found, now decide which way to go
		if(follow.getY()+(follow.getTextureHeight()/2) > myObject.getY() + (myObject.getTextureHeight()/2))
			myObject.setVelocity(myObject.getVelocity()[0], followSpeed);
		else
			myObject.setVelocity(myObject.getVelocity()[0], -followSpeed);

	}
}
