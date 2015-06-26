package org.TheGivingChild.Engine.Attributes;

import org.TheGivingChild.Engine.XML.AttributeEnum;
import org.TheGivingChild.Engine.XML.GameObject;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.Array;

public class MovesAttribute extends Attribute {

	public void update(GameObject myObject,Array<GameObject> allObjects){
		myObject.setPosition((myObject.getX() + Gdx.graphics.getDeltaTime()*myObject.getVelocity()[0]), (myObject.getY() + Gdx.graphics.getDeltaTime()*myObject.getVelocity()[1]));
	}
	
	public void setup(GameObject myObject){
		myObject.setVelocity(new float[] {Float.parseFloat(myObject.getAttributeData().get(this).get(0)),Float.parseFloat(myObject.getAttributeData().get(this).get(1))});
	}
	public Array<String> getValues(GameObject myObject){
		Array<String> temp = new Array<String>();
		temp.add(myObject.getAttributeData().get(this).get(0));
		temp.add(myObject.getAttributeData().get(this).get(1));
		return temp;
	}
	
	public Array<String> getVariableNames(){
		Array<String> variableNames = new Array<String>();
		variableNames.add("Initial X Velocity");
		variableNames.add("Initial Y Velocity");
		return variableNames;
	}
}
