package org.TheGivingChild.Engine;

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
	private double rotation;//RADIANS OR DEGREES?
	
	public GameObject(int newID, String img,GridPoint2 initLoc){
		ID = newID;
		imageFilename = img;
		location = initLoc;
		rotation = 0;
		validAttributes = new ObjectMap<String,Integer>();//map from function name to int representing if it's allowed to be used
		attributeValues = new ObjectMap<String,Array<String>>();//map from function name to the variables it has stored and can use, pseudo OO because java hates reflection and fun and children
		System.out.println("\nNew object, ID: " + ID);
	}
	
	public String toString(){
		return "ID: " + ID + ", Image filename: " + imageFilename;
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
	//ATTRIBUTES, pray to Gaben we can find a way to refactor this.
	
	public void attribute_health(){
		System.out.println("health called");
		System.out.println(attributeValues.get("health"));
	}
	
	public void attribute_color(){
		System.out.println("color called");
		System.out.println(attributeValues.get("color"));
	}
	
	public void attribute_disappearsOnPress(){
		System.out.println("disappearsOnPress called");
		System.out.println(attributeValues.get("disappearsOnPress"));
	}
	
	public void attribute_movesOnSetPath(){
		System.out.println("movesOnSetPath called");
		System.out.println(attributeValues.get("movesOnSetPath"));
	}	
}
