package org.TheGivingChild.Engine.Attributes;

import org.TheGivingChild.Engine.XML.GameObject;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.Array;

public class MovesAttribute extends Attribute {

	public void update(GameObject myObject,Array<GameObject> allObjects){
		myObject.setPosition((myObject.getX() + Gdx.graphics.getDeltaTime()*myObject.getVelocity()[0]), (myObject.getY() + Gdx.graphics.getDeltaTime()*myObject.getVelocity()[1]));
	}
	
	public void setup(GameObject myObject){
		myObject.setVelocity(new float[] {Float.parseFloat(data.get(0)),Float.parseFloat(data.get(1))});
	}
	
	public Array<String> getVariableNames(){
		Array<String> variableNames = new Array<String>();
		variableNames.add("Initial X Velocity");
		variableNames.add("Initial Y Velocity");
		return variableNames;
	}
}
