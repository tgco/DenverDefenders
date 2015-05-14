package org.TheGivingChild.Engine;

import java.awt.Point;
import java.util.ArrayList;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.Array;
import org.TheGivingChild.Engine.Attributes.*;

public class GameObject extends Actor{//libGDX actors have all the listeners we will need
	private Array<Attribute> attributes;
	
	
	public GameObject(){
		
	}
	
	public void update(){
		for(Attribute current:attributes){
			current.update(this);
		}
	}
	
	public void addAttribute(Attribute newAttribute){
		attributes.add(newAttribute);
	}
}
