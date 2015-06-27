package org.TheGivingChild.Engine.Attributes;

import org.TheGivingChild.Engine.XML.Level;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.ObjectMap;

// Bounces the object off the edge of the screen
public class BounceOffEdgeOfScreenAttribute extends Attribute {
	
	public BounceOffEdgeOfScreenAttribute(ObjectMap<String, String> args) {
		super(args);
	}

	public void update(Level level){
		if(myObject.getX() <= 0){//left
			float[] temp = myObject.getVelocity();
			temp[0] = Math.abs(temp[0]);
			myObject.setVelocity(temp);
		}if(myObject.getX() + myObject.getTextureWidth() >= Gdx.graphics.getWidth()) {//right
			float[] temp = myObject.getVelocity();
			temp[0] = -Math.abs(temp[0]);
			myObject.setVelocity(temp);
		}if(myObject.getY() <= 0){//bottom
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
