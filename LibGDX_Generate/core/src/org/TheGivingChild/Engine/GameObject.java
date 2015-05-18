package org.TheGivingChild.Engine;

import java.lang.reflect.Method;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.ObjectMap;

public class GameObject extends Actor{//libGDX actors have all the listeners we will need
	private ObjectMap<String,Integer> validAttributes;
	private ObjectMap<String,String> attributeValues;//attribute_health mapped to whatever it needs to use, it's own storage
	private int ID;
	
	public GameObject(int newID){
		ID = newID;
		validAttributes = new ObjectMap<String,Integer>();
		attributeValues = new ObjectMap<String,String>();
		System.out.println("\nNew object, ID: " + ID);
	}
	
	public void addValidAttribute(String newAttribute,String value){
		validAttributes.put(newAttribute,1);
		attributeValues.put("attribute_" + newAttribute, value);
		System.out.println(newAttribute);
	}
	
	public void attribute_color(){
		System.out.println("COLOR");
	}
	
	//ATTRIBUTES
	
	public void attribute_disappearsOnPress(){
		System.out.println("DISAPPEARSONPRESS");
	}
	
	public void attribute_health(){
		System.out.println("HEALTH");
		System.out.println(attributeValues.get("health"));
	}
	
	public void update(){
		for(Method currentMethod:this.getClass().getMethods()){
			if(validAttributes.containsKey(currentMethod.getName().replaceAll("attribute_",""))){
				try{
					currentMethod.invoke(this);
					System.out.println("current method: " + currentMethod.getName());
				}catch(Exception e){
					System.out.println("Exception calling method " + currentMethod.getName() + " Exception: " + e);
				}
			}
		}
	}
}
