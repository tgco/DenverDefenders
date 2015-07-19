package org.TheGivingChild.Engine.Attributes;

import org.TheGivingChild.Engine.XML.GameObject;
import org.TheGivingChild.Engine.XML.Level;

import com.badlogic.gdx.utils.ObjectMap;

// Attributes belong to a game object and are allowed to change its properties every frame in the update call
// Author: Walter Schlosser
public abstract class Attribute {
	// List of values this attribute needs to work, retrieved from xml file
	protected ObjectMap<String, String> args;
	// Reference to the object this attribute modifies
	protected GameObject myObject;
	// The condition that this attribute runs on, if any
	protected String trigger;
	// The condition this attribute throws, if any
	protected String throwCondition;
	// If listening to generic input events, this string will be set with the name of the event and args
	protected String argString;
	
	public Attribute(ObjectMap<String, String> args) { 
		// Set args and trigger conditions
		this.args = args;
		// The following may be null if not specified (not needed)
		trigger = args.get("on");
		throwCondition = args.get("throws");
	}
	
	/**
	 * Called once within the GameObject's constructor, used to setup initial values
	 * @param	myObject	the GameObject that currently holds this attribute
	 */
	public void setup(GameObject myObject) {
		this.myObject = myObject;
	};
	/**
	 * Used to simulate the specific behavior for each game object, called each frame if continuous
	 * or called when notified of the trigger condition if triggered.
	 * 
	 * @param	myObject	the GameObject that currently holds the given attribute
	 * @param	allObjects	all of the gameObjects within the given level
	 */
	public void update(Level level) {
		// Throw condition if there is one
		if (throwCondition != null) level.throwCondition(throwCondition);
	};
	
	public String getTrigger() { return trigger; }
	
	public void setArgString(String s) { this.argString = s; }
}
