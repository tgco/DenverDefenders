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
		
		// Update position after scaling velocity.  The set velocity is at pixels/sec for a 1024x600 screen
		float x = myObject.getX() + Gdx.graphics.getDeltaTime() * (vx * Gdx.graphics.getWidth()/1024f);
		float y = myObject.getY() + Gdx.graphics.getDeltaTime() * (vy * Gdx.graphics.getHeight()/600f);
		myObject.setPosition(x, y);
	}
	
	public void setup(GameObject myObject){
		super.setup(myObject);
		
		float vx = Float.parseFloat(args.get("vx", "0"));
		float vy = Float.parseFloat(args.get("vy", "0"));
		float ax = Float.parseFloat(args.get("ax", "0"));
		float ay = Float.parseFloat(args.get("ay", "0"));
		// Set the start v and a
		myObject.setVelocity(vx, vy);
		myObject.setAcceleration(ax,ay);
	}
}
