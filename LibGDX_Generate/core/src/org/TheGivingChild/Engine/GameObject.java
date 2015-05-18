package org.TheGivingChild.Engine;

import java.awt.Point;
import java.lang.reflect.Method;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ObjectMap;
import com.badlogic.gdx.utils.reflect.*;
import com.badlogic.gdx.utils.reflect.Method.*;

import org.TheGivingChild.Engine.Attributes.*;

public class GameObject extends Actor{//libGDX actors have all the listeners we will need
	private ObjectMap<String,Integer> validAttributes;
	private ObjectMap<String,String> attributeValues;
	private int ID;
	
	public GameObject(int newID){
		ID = newID;
		validAttributes = new ObjectMap<String,Integer>();
		attributeValues = new ObjectMap<String,String>();
		System.out.println("\nNew object, ID: " + ID);
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
	
	public void addValidAttribute(String newAttribute){
		validAttributes.put(newAttribute,1);
		System.out.println(newAttribute);
	}
	
	
	
	
	
	
	
	
	//ATTRIBUTES
	
	public void attribute_health(){
		System.out.println("HEALTH");
	}
	
	public void attribute_color(){
		System.out.println("COLOR");
	}
	
	public void attribute_disappearsOnPress(){
		System.out.println("DISAPPEARSONPRESS");
	}
}
