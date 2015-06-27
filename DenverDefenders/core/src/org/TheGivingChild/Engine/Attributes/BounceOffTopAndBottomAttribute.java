package org.TheGivingChild.Engine.Attributes;

import org.TheGivingChild.Engine.XML.Level;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.ObjectMap;

public class BounceOffTopAndBottomAttribute extends Attribute {
	
	public BounceOffTopAndBottomAttribute(ObjectMap<String, String> args) {
		super(args);
	}
	
	public void update(Level level){
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
}

