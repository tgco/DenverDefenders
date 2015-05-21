package org.TheGivingChild.Engine.XML;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.badlogic.gdx.utils.Array;

//GameObject is essentially a sotrage container for all the information associated with each object on the screen
public class GameObject extends Actor{//libGDX actors have all the listeners we will need
	private int ID;
	private String imageFilename;
	Array<Attribute> attributes;
	
	/*	1: All game objects must have 4 attributes, an int ID, a string which lists their attributes(delimited by ','), an image filename, and an initial location(also delimited by a comma)
	 * 	2: Each object's attributes are then elements within the object
	 * 	3: The values(can be zero or any positive amount) must be labelled as value1, value2, value3, etc.
	 */
	
	public GameObject(int newID, String img,float[] newPosition){
		ID = newID;
		imageFilename = img;
		setPosition(newPosition[0],newPosition[1]);
		attributes = new Array<Attribute>();
	}
	
	public void update(){
		for(Attribute currentAttribute:attributes)
			currentAttribute.update();
	}
	
	public Array<Attribute> getAttributes(){
		return attributes;
	}
	
	public void addAttribute(String newAttribute,Array<String> newValues){//add an attribute with it's associated values
		Attribute temp = Attribute.newType(newAttribute);
		temp.setValues(newValues);
		attributes.add(temp);
	}	

	public int getID() {
		return ID;
	}

	public String getImageFilename() {
		return imageFilename;
	}
	
	public String getListenersAsString(){//this is not used, will be used at a later time once we get listeners working, leaving as is for now
		String temp="";
		for(EventListener listener:getListeners()){
			temp+=","+listener.toString();
		}
		return temp.replaceFirst(",", "");
	}
	
	public String toString(){
		return "ID: " + ID + ", Image filename: " + imageFilename + " X: " + getX() + " Y: " + getY();
	}
}