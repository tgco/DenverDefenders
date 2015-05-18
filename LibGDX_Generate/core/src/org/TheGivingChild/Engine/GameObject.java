package org.TheGivingChild.Engine;

import java.awt.Point;
import java.util.Map;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ObjectMap;
//import org.TheGivingChild.Engine.Attributes.*;

public class GameObject extends Actor{//libGDX actors have all the listeners we will need
	//private Array<Attribute> attributes;
	private Array<String> validAttributes;
	
	public GameObject(){
		
	}
	
	public void update(){
		
	}
	
	public void addValidAttribute(String newAttribute){
		validAttributes.add(newAttribute);
	}
	
	public void attribute_health(GameObject object){
		System.out.println("HEALTH");
	}
	
	public void attribute_color(GameObject object){
		System.out.println("COLOR");
	}
	
	public void attribute_disappearsOnPress(GameObject object){
		System.out.println("DISAPPEARSONPRESS");
	}
}
