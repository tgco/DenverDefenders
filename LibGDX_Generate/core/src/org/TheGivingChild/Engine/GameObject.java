package org.TheGivingChild.Engine;

import java.lang.reflect.Method;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ObjectMap;

public class GameObject extends Actor{//libGDX actors have all the listeners we will need
	private ObjectMap<String,Integer> validAttributes;
	private ObjectMap<String,Array<String>> attributeValues;//attribute_health mapped to whatever it needs to use, it's own storage
	private int ID;
	
	public GameObject(int newID){
		ID = newID;
		validAttributes = new ObjectMap<String,Integer>();//map from function name to int representing if it's allowed to be used
		attributeValues = new ObjectMap<String,Array<String>>();//map from function name to the variables it has stored and can use, pseudo OO because java hates reflection and fun and children
		System.out.println("\nNew object, ID: " + ID);
	}
	
	public void addValidAttribute(String newAttribute,String value){
		if(validAttributes.get(newAttribute) == null){
			validAttributes.put(newAttribute,1);
			attributeValues.put(newAttribute,new Array<String>());
		}
		//System.out.println("Attribute successfully set to true: " + newAttribute);
		attributeValues.get(newAttribute).add(value);//adds that value to the list of values that function can get
	}
	
	public void attribute_color(){
		System.out.println("color called");
		System.out.println(attributeValues.get("color"));
	}
	
	//ATTRIBUTES	
	public void attribute_disappearsOnPress(){
		System.out.println("disappearsOnPress called");
		System.out.println(attributeValues.get("disappearsOnPress"));
	}
	
	public void attribute_health(){
		System.out.println("health called");
		System.out.println(attributeValues.get("health"));
	}
	public void attribute_movesOnSetPath(){
		System.out.println("movesOnSetPath called");
		System.out.println(attributeValues.get("movesOnSetPath"));
	}
	public void update(){
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
	
}
