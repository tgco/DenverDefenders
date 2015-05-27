package org.TheGivingChild.Engine.XML;

import java.lang.reflect.Method;

import org.TheGivingChild.Engine.InputListenerEnums;
import org.TheGivingChild.Engine.TGC_Engine;
import org.TheGivingChild.Engine.UserInputProcessor;
import org.TheGivingChild.Screens.ScreenAdapterManager;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.utils.Array;

//GameObject is essentially a storage container for all the information associated with each object on the screen
public class GameObject extends Actor{//libGDX actors have all the listeners we will need
	private int ID;
	private String imageFilename;
	private Array<Attribute> attributes;
	private float[] velocity;
	private TGC_Engine game;
	private AssetManager manager = new AssetManager();
	
	/*	1: All game objects must have 4 attributes, an int ID, a string which lists their attributes(delimited by ','), an image filename, and an initial location(also delimited by a comma)
	 * 	2: Each object's attributes are then elements within the object
	 * 	3: The values(can be zero or any positive amount) must be labelled as value1, value2, value3, etc.
	 */
	//touchable = true;
	
	public GameObject(int newID, String img,float[] newPosition){
		ID = newID;
		imageFilename = img;
		setPosition(newPosition[0],newPosition[1]);
		attributes = new Array<Attribute>();
		velocity = new float[] {0,0};
		//should be set using the bounds of the texture rather than a static number
		setBounds(getX(), getY(), 100, 100);
		//add the destroy on click event
		addListener(InputListenerEnums.DESTROY_ON_CLICK.getInputListener(this));
		game = ScreenAdapterManager.getInstance().game;
		/*if(!game.getAssetManager().isLoaded(imageFilename)) {
			game.getAssetManager().load(imageFilename, Texture.class);
			game.getAssetManager().update();
		}*/
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
	
	
	


	public float[] getVelocity() {
		return velocity;
	}

	public void setVelocity(float[] velocity) {
		this.velocity = velocity;
	}
}
