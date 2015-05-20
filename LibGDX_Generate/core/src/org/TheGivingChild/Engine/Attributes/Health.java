package org.TheGivingChild.Engine.Attributes;


public class Health implements Attribute{
	public int health;
	public void update(GameObject object){
		System.out.println("CALLED");
	}
	
}
