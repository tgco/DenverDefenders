package org.TheGivingChild.Engine.Attributes;

import org.TheGivingChild.Engine.XML.GameObject;

import com.badlogic.gdx.utils.Array;

public class FollowsObjectYAttribute extends Attribute {
	@Override
	public void update(GameObject myObject, Array<GameObject> allObjects) {
		int ID = Integer.parseInt(data.get(0));
		float velocity = Float.parseFloat(data.get(1));
		//find the object we want to follow
		for(int i = 0; i<allObjects.size;i++){
			if(allObjects.get(i).getID() == ID){
				//object found, now decide which way to go
				if(allObjects.get(i).getY()+(allObjects.get(i).getTextureHeight()/2) > myObject.getY() + (myObject.getTextureHeight()/2))
					myObject.setVelocity(new float[] {myObject.getVelocity()[0],velocity});
				else
					myObject.setVelocity(new float[] {myObject.getVelocity()[0],-velocity});
			}
		}
		
	}

	@Override
	public Array<String> getVariableNames() {
		Array<String> vals = new Array<String>();
		vals.add("ID of object to follow");
		vals.add("speed at which to follow object");
		return vals;
	}
	
	@Override
	public void setup(GameObject myObject){}
}
