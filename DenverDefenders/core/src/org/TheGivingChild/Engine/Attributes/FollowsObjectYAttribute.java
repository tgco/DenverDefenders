package org.TheGivingChild.Engine.Attributes;

import org.TheGivingChild.Engine.XML.GameObject;
import org.TheGivingChild.Engine.XML.Level;

import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ObjectMap;

public class FollowsObjectYAttribute extends Attribute {
	public FollowsObjectYAttribute(ObjectMap<String, String> args) {
		super(args);
	}

	@Override
	public void update(Level level) {
		int id = Integer.parseInt(args.get("id"));
		float velocity = Float.parseFloat(args.get("v"));
		//find the object we want to follow
		Array<GameObject> allObjects = level.getGameObjects();
		for(int i = 0; i< allObjects.size;i++){
			if(allObjects.get(i).getID() == id){
				//object found, now decide which way to go
				if(allObjects.get(i).getY()+(allObjects.get(i).getTextureHeight()/2) > myObject.getY() + (myObject.getTextureHeight()/2))
					myObject.setVelocity(myObject.getVelocity()[0], velocity);
				else
					myObject.setVelocity(myObject.getVelocity()[0], -velocity);
			}
		}
		
	}
}
