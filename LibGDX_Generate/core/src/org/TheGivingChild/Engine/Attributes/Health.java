package org.TheGivingChild.Engine.Attributes;

import org.TheGivingChild.Engine.GameObject;

public class Health implements Attribute{
	public int health;
	public void update(GameObject object){
		System.out.println("CALLED");
	}
	
}
