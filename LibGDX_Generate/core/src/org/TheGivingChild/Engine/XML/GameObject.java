package org.TheGivingChild.Engine.XML;

import java.lang.reflect.Method;
import java.util.Locale;

import javax.smartcardio.ATR;

import org.TheGivingChild.Engine.InputListenerEnums;
import org.TheGivingChild.Engine.TGC_Engine;
import org.TheGivingChild.Engine.UserInputProcessor;
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

//GameObject is essentially a storage container for all the information associated with each object on the screen
public class GameObject extends Actor implements Disposable{//libGDX actors have all the listeners we will need
	private int ID;
	private String imageFilename;
	private Array<Attribute> attributes = new Array<Attribute>();
	private float[] velocity;
	private TGC_Engine game;
	private AssetManager manager = new AssetManager();
	private boolean disposed = false;
	private Texture texture;
	private Array<String> listenerNames = new Array<String>();
	
	/*	1: All game objects must have 4 attributes, an int ID, a string which lists their attributes(delimited by ','), an image filename, and an initial location(also delimited by a comma)
	 * 	2: Each object's attributes are then elements within the object
	 * 	3: The values(can be zero or any positive amount) must be labelled as value1, value2, value3, etc.
	 */
	

	public GameObject(int newID, String img,float[] newPosition, Array<Attribute> attributesToAdd,Array<String> newListenerNames){
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
			System.out.println("Listener name is: " + name);
			//iterate over the listeners
			for(InputListenerEnums ILE: InputListenerEnums.values()){
				//if the names of the attribute and listener are equal, add the listener
				if(ILE.name().equals(name)){
					//add the listener
					addListener(InputListenerEnums.valueOf(name).getInputListener(this));
				}
			}
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

	public int getID() {
		return ID;
	}

	public String getImageFilename() {
		return imageFilename;
	}
	
	public String toString(){
		return "ID: " + ID + ", Image filename: " + imageFilename + " X: " + getX() + " Y: " + getY();
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
	
	public Array<String> getListenerNames(){
		return listenerNames;
	}
}
