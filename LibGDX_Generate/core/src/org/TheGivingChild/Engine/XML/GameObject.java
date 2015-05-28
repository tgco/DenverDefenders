package org.TheGivingChild.Engine.XML;

import java.lang.reflect.Method;
import java.util.Locale;

import javax.smartcardio.ATR;

import org.TheGivingChild.Engine.InputListenerEnums;
import org.TheGivingChild.Engine.TGC_Engine;
import org.TheGivingChild.Screens.ScreenAdapterManager;

import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;
import com.sun.javafx.property.adapter.PropertyDescriptor.Listener;

//GameObject is essentially a storage container for all the information associated with each object on the screen
public class GameObject extends Actor implements Disposable{
	private int ID;
	private String imageFilename;
	private Array<Attribute> attributes;
	private float[] velocity;
	private TGC_Engine game;
	private AssetManager manager;
	private boolean disposed;
	private Texture texture;
	private Array<String> listenerNames;
	
	/*	1: All game objects must have 4 attributes, an int ID, a string which lists their attributes(delimited by ','), an image filename, and an initial location(also delimited by a comma)
	 * 	2: Each object's attributes are then elements within the object
	 * 	3: The values(can be zero or any positive amount) must be labelled as value1, value2, value3, etc.
	 */
	

	public GameObject(int newID, String img,float[] newPosition, Array<Attribute> attributesToAdd,Array<String> newListenerNames){
		attributes = new Array<Attribute>();
		listenerNames = new Array<String>();
		manager = new AssetManager();
		disposed = false;
		attributes.addAll(attributesToAdd);
		listenerNames.addAll(newListenerNames);
		//set the id from the xml
		ID = newID;
		//set the imagefilename from the xml
		imageFilename = img;
		//the the initial position from xml
		setPosition(newPosition[0],newPosition[1]);
		//initialize a velocity of 0
		velocity = new float[] {0,0};
		//get the reference to the game
		game = ScreenAdapterManager.getInstance().game;
		//get the manager in game
		manager = game.getAssetManager();
		//check if the Texture has been loaded for this object
		if(!manager.isLoaded(imageFilename)) {
			//the texture has not been loaded, so load it
			manager.load(imageFilename, Texture.class);
			//update the manager
			manager.update();
		}
		//set the Texture from the manager
		if(manager.isLoaded(imageFilename)){
			texture = manager.get(imageFilename);
		}
		else{
			manager.finishLoadingAsset(imageFilename);
			texture = manager.get(imageFilename);
		}
		//set the bounds to be as large as the textures size
		setBounds(getX(), getY(), texture.getWidth(), texture.getHeight());
		//iterate over attributes
		for(String listener: listenerNames){
			//get the name, uppercase it
			String name = listener.toUpperCase(Locale.ENGLISH);
			//iterate over the listeners
			for(InputListenerEnums ILE: InputListenerEnums.values()){
				//if the names of the attribute and listener are equal, add the listener
				if(ILE.name().equals(name)){
					InputListener listen = InputListenerEnums.valueOf(name).getInputListener(this);
					//add the listener
					addListener(listen);
				}
			}
		}
	}
	
	public GameObject GameObject() {
		float position[] = new float[2];
		position[0] = (getOriginX());
		position[1] = (getOriginY());
		GameObject deepCopy = new GameObject(
				getID(), 
				getImageFilename(), 
				position, 
				getAttributes(), 
				getListenerNames());
		return deepCopy;
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

	public int getID() {
		return ID;
	}

	public String getImageFilename() {
		return imageFilename;
	}
	
	public String toString(){
		String att = "";
		for(Attribute a: attributes){
			att+=a.name()+"\n";
		}
		return "Attributes: " +att +"ID: " + ID + ", Image filename: " + imageFilename + " X: " + getX() + " Y: " + getY() + "Is diposed: " + disposed + "\n";
	}

	@Override
	public void dispose(){
		//imageFilename = null;
		//attributes.clear();
		//listenerNames.clear();
		//manager = null;
		//texture = null;
		//game = null;
		//velocity = null;
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
	
	public Array<String> getListenerNames(){
		return listenerNames;
	}
}
