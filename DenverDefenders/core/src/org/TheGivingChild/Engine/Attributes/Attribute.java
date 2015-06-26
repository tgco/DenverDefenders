package org.TheGivingChild.Engine.Attributes;

import org.TheGivingChild.Engine.XML.GameObject;

import com.badlogic.gdx.utils.Array;

// Attributes belong to a game object and are allowed to change its properties every frame in the update call
public abstract class Attribute {
	// List of values this attribute needs to operate
	protected Array<String> data;
	
	/**
	 * Called once within the GameObject's constructor, used to setup initial values
	 * @param	myObject	the GameObject that currently holds this attribute
	 */
	public abstract void setup(GameObject myObject);
	/**
	 * Used to simulate the specific behavior for each game object, called each frame
	 * @param	myObject	the GameObject that currently holds the given attribute
	 * @param	allObjects	all of the gameObjects within the given level
	 */
	public abstract void update(GameObject myObject,Array<GameObject> allObjects);
	/**
	 * Used by the Editor to tell the user what values they need to input
	 * @return	A libGDX Array object containing descriptions of the values to setup
	 */
	public abstract Array<String> getVariableNames();
	
	public void setAttributeData(Array<String> data) {
		this.data = data;
	}
	
	/**
	 * Used by XML_Writer, returns the values associated with the given GameObject's attribute
	 * @param	myObject	the GameObject that currently holds this attribute
	 * @return	A libGDX Array object containing the values to write
	 */
	public Array<String> getAttributeData() { return data; }
}
