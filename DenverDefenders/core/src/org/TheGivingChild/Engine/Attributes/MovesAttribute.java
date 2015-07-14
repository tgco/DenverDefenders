package org.TheGivingChild.Engine.Attributes;

import org.TheGivingChild.Engine.XML.GameObject;
import org.TheGivingChild.Engine.XML.Level;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.ObjectMap;

// Moves the object with its velocity and acceleration values.  Supports only constant acceleration.
public class MovesAttribute extends Attribute {

	public MovesAttribute(ObjectMap<String, String> args) {
		super(args);
	}

	public void update(Level level){
		super.update(level);
		// Update velocity
		float vx = myObject.getVelocity()[0] + Gdx.graphics.getDeltaTime()*myObject.getAcceleration()[0];
		float vy = myObject.getVelocity()[1] + Gdx.graphics.getDeltaTime()*myObject.getAcceleration()[1];
		myObject.setVelocity(vx, vy);
		
		// Update position
		float x = myObject.getX() + Gdx.graphics.getDeltaTime()*vx;
		float y = myObject.getY() + Gdx.graphics.getDeltaTime()*vy;
		myObject.setPosition(x, y);
	}
	
	public void setup(GameObject myObject){
		super.setup(myObject);
		// Set the start v and a
		myObject.setVelocity(Float.parseFloat(args.get("vx")), Float.parseFloat(args.get("vy")));
		myObject.setAcceleration(Float.parseFloat(args.get("ax")), Float.parseFloat(args.get("ay")));
	}
}
