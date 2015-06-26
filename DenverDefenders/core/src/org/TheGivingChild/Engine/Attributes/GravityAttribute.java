package org.TheGivingChild.Engine.Attributes;

import org.TheGivingChild.Engine.XML.GameObject;

import com.badlogic.gdx.utils.Array;

public class GravityAttribute extends Attribute {
	@Override
	public void setup(GameObject myObject){}

	@Override
	public void update(GameObject myObject, Array<GameObject> allObjects) {
		float rate = Float.parseFloat(data.get(0));
		myObject.setVelocity(new float[] {myObject.getVelocity()[0],myObject.getVelocity()[1]-rate});
	}

	@Override
	public Array<String> getVariableNames(){
		Array<String> val = new Array<String>();
		val.add("Rate at which object falls");
		return val;
	}
	
}
