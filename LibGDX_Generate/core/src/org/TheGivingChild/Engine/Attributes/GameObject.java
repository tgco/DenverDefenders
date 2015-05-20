package org.TheGivingChild.Engine.Attributes;

import java.lang.reflect.Method;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ObjectMap;
import org.TheGivingChild.Engine.Attributes.*;
import com.badlogic.gdx.utils.reflect.*;
import com.badlogic.gdx.utils.reflect.Method.*;
import com.badlogic.gdx.math.GridPoint2;

public class GameObject extends Actor{//libGDX actors have all the listeners we will need
	private ObjectMap<String,Integer> validAttributes;
	private ObjectMap<String,Array<String>> attributeValues;//attribute_health mapped to whatever it needs to use, it's own storage
	private int ID;
	private String imageFilename;
	private GridPoint2 location;
	//private double rotation;//RADIANS OR DEGREES?
	
	/*	1: All game objects must have 4 attributes, an int ID, a string which lists their attributes(delimited by ','), an image filename, and an initial location(also delimited by a comma)
	 * 	2: Each object's attributes are then elements within the object
	 * 	3: The values(can be zero or any positive amount) must be labelled as value1, value2, value3, etc.
	 */
	
	public GameObject(int newID, String img,GridPoint2 initLoc){
		ID = newID;
		imageFilename = img;
		location = initLoc;
		//rotation = 0;
		validAttributes = new ObjectMap<String,Integer>();//map from function name to int representing if it's allowed to be used
		attributeValues = new ObjectMap<String,Array<String>>();//map from function name to the variables it has stored and can use, pseudo OO because java hates reflection and fun and children
	}
	
	public String toString(){
		return "ID: " + ID + ", Image filename: " + imageFilename + " X: " + location.x + " Y: " + location.y;
	}
	
	public void update(){//working
		for(Method currentMethod:this.getClass().getMethods()){
			if(validAttributes.containsKey(currentMethod.getName().replaceAll("attribute_",""))){
				try{
					System.out.println("current method: " + currentMethod.getName());
					currentMethod.invoke(this);
				}catch(Exception e){
					System.out.println("Exception calling method " + currentMethod.getName() + " Exception: " + e);
				}
			}
			//TODO, for debugging, see if there is an entry in valid attributes that is not within the listed methods
		}
	}
	
	public void addValidAttribute(String newAttribute,String value){//working
		if(validAttributes.get(newAttribute) == null){
			validAttributes.put(newAttribute,1);
			attributeValues.put(newAttribute,new Array<String>());
		}
		attributeValues.get(newAttribute).add(value);//adds that value to the list of values that function can get
	}
	
	public Array<String> getAttributes() {
		return attributeValues.keys().toArray();
	}
	
	public ObjectMap<String,Array<String>> getAttributeValues(){
		return attributeValues;
	}

	public int getID() {
		return ID;
	}

	public String getImageFilename() {
		return imageFilename;
	}

	public GridPoint2 getLocation() {
		return location;
	}
	
	//ATTRIBUTES

	public void attribute_health(){
		System.out.println("health called");
		System.out.println(attributeValues.get("health") + "\n");
	}
	
	public void attribute_color(){
		System.out.println("color called");
		System.out.println(attributeValues.get("color") + "\n");
	}
	
	public void attribute_disappearsOnPress(){
		System.out.println("disappearsOnPress called");
		System.out.println(attributeValues.get("disappearsOnPress") + "\n");
	}
	
	public void attribute_movesOnSetPath(){
		System.out.println("movesOnSetPath called");
		System.out.println(attributeValues.get("movesOnSetPath") + "\n");
	}	
}
