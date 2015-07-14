package org.TheGivingChild.Engine.Attributes;

import org.TheGivingChild.Engine.XML.Level;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.ObjectMap;

// REFACTOR: An edge bounce attrib with side args could combine this with the other bouce off edge attrib
public class BounceOffTopAndBottomAttribute extends Attribute {
	
	public BounceOffTopAndBottomAttribute(ObjectMap<String, String> args) {
		super(args);
	}
	
	public void update(Level level){
		super.update(level);
		if(myObject.getY() <= 0){//bottom
			float[] temp = myObject.getVelocity();
			temp[1] = Math.abs(temp[1]);
			myObject.setVelocity(temp[0], temp[1]);
		}if(myObject.getY() + myObject.getTextureHeight() >= Gdx.graphics.getHeight()){//top
			float[] temp = myObject.getVelocity();
			temp[1] = -Math.abs(temp[1]);
			myObject.setVelocity(temp[0], temp[1]);
		}
	}
}

