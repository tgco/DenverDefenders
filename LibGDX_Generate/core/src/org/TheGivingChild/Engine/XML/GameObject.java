package org.TheGivingChild.Engine.XML;

import java.lang.reflect.Method;
import java.util.Locale;

import javax.smartcardio.ATR;

import org.TheGivingChild.Engine.InputListenerEnums;
import org.TheGivingChild.Engine.UserInputProcessor;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;

//GameObject is essentially a storage container for all the information associated with each object on the screen
public class GameObject extends Actor implements Disposable{//libGDX actors have all the listeners we will need
	private int ID;
	private String imageFilename;
	private Array<Attribute> attributes = new Array<Attribute>();
	private float[] velocity;
	private boolean disposed = false;
	
	/*	1: All game objects must have 4 attributes, an int ID, a string which lists their attributes(delimited by ','), an image filename, and an initial location(also delimited by a comma)
	 * 	2: Each object's attributes are then elements within the object
	 * 	3: The values(can be zero or any positive amount) must be labelled as value1, value2, value3, etc.
	 */
	//touchable = true;
	
	public GameObject(int newID, String img,float[] newPosition, Array<Attribute> attributesToAdd){
		attributes.addAll(attributesToAdd);
		ID = newID;
		imageFilename = img;
		setPosition(newPosition[0],newPosition[1]);
		
		velocity = new float[] {0,0};
		//should be set using the bounds of the texture rather than a static number
		setBounds(getX(), getY(), 100, 100);
		//add the destroy on click event
		System.out.println();
		for(Attribute a: attributes){
			String name = a.name().toUpperCase(Locale.ENGLISH);
			for(InputListenerEnums ILE: InputListenerEnums.values()){
				if(ILE.name().equals(name)){
					addListener(InputListenerEnums.valueOf(name).getInputListener(this));
					System.out.println(name + " was added to the gameObject: " + this.getID());
				}
			}
//			if(InputListenerEnums.valueOf(name) != null){
//				addListener(InputListenerEnums.valueOf(name).getInputListener(this));
//				System.out.println(name + " was added to the gameObject: " + this.getID());
//			}
			System.out.println(name);
		}

		
		
	}
				
	public void update(){
		for(Attribute currentAttribute:attributes)
			currentAttribute.update(this);
	}

	
	public void input(){
		
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

	
	public void act()
	{
		//System.out.println("I am acting " + this.getName());
	}
	@Override
	public void dispose(){
		imageFilename = null;
		attributes.clear();
		disposed = true;
	};


	public boolean isDisposed(){
		return disposed;
	}
	
	public float[] getVelocity() {
		return velocity;
	}

	public void setVelocity(float[] velocity) {
		this.velocity = velocity;
	}
}
