package org.TheGivingChild.Engine.Attributes;

import org.TheGivingChild.Engine.XML.GameObject;
import org.TheGivingChild.Engine.XML.Level;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.ObjectMap;

public class MovesAttribute extends Attribute {

	public MovesAttribute(ObjectMap<String, String> args) {
		super(args);
	}

	public void update(Level level){
		myObject.setPosition((myObject.getX() + Gdx.graphics.getDeltaTime()*myObject.getVelocity()[0]), (myObject.getY() + Gdx.graphics.getDeltaTime()*myObject.getVelocity()[1]));
	}
	
	public void setup(GameObject myObject){
		super.setup(myObject);
		myObject.setVelocity(new float[] {Float.parseFloat(args.get("vx")),Float.parseFloat(args.get("vy"))});
	}
}
