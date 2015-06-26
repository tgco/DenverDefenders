package org.TheGivingChild.Engine.Attributes;

import org.TheGivingChild.Engine.XML.GameObject;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.Array;

public class BounceOffTopAndBottomAttribute extends Attribute {
	public void update(GameObject myObject,Array<GameObject> allObjects){
		if(myObject.getY() <= 0){//bottom
			float[] temp = myObject.getVelocity();
			temp[1] = Math.abs(temp[1]);
			myObject.setVelocity(temp);
		}if(myObject.getY() + myObject.getTextureHeight() >= Gdx.graphics.getHeight()){//top
			float[] temp = myObject.getVelocity();
			temp[1] = -Math.abs(temp[1]);
			myObject.setVelocity(temp);
		}
	}
	public Array<String> getVariableNames(){
		return new Array<String>();
	}
	public void setup(GameObject myObject){}//doesnt need to setup anything		
	public Array<String> getValues(GameObject myObject){
		return myObject.getAttributeData().get(this);
	}
	
}
