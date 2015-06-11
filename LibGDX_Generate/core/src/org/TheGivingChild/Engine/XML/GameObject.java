package org.TheGivingChild.Engine.XML;

import java.util.Locale;

import org.TheGivingChild.Engine.InputListenerEnums;
import org.TheGivingChild.Engine.TGC_Engine;
import org.TheGivingChild.Screens.ScreenAdapterManager;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.ObjectMap;

//GameObject is essentially a storage container for all the information associated with each object on the screen
/**
 * Used to store all information on each object within the game<br>
 * extends Actor
 * @author Mostly Kevin D
 */
public class GameObject extends Actor implements Disposable{
	/** Unique ID assigned to each GameObject*/
	private int ID;
	private String imageFilename;
	/** Two element velocity array<br> First element is X velocity, second is Y velocity */
	private float[] velocity;
	private float[] initialVelocity;
	private float[] position;
	private float[] initialPosition;
	private TGC_Engine game;
	private AssetManager manager;
	private boolean disposed;
	private Texture texture;
	private float objectScale;
	/** list of the names of the listeners associated with this object*/
	private Array<String> listenerNames;
	/** A map which contains the Attributes associated with this object, as well as the information mapped to the given attribute */
	private ObjectMap<Attribute,Array<String>> attributeData;

	public GameObject(int newID, String img,float[] newPosition, Array<String> newListenerNames,ObjectMap<Attribute,Array<String>> newAttributeData){
		switch(Gdx.app.getType()){
		case Android:
			objectScale = 3;
			break;
		//if using the desktop set the width and height to a 16:9 resolution.
		case Desktop:
			objectScale = 1;
			break;
		case iOS:
			objectScale = 3;
			break;
		default:
			break;
	}
		listenerNames = new Array<String>();
		manager = new AssetManager();
		disposed = false;
		listenerNames.addAll(newListenerNames);
		//set the id from the xml
		ID = newID;
		//set the imagefilename from the xml
		imageFilename = "ObjectImages/"+img;
		position = newPosition;
		
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
		setBounds(getX(), getY(), texture.getWidth()*objectScale, texture.getHeight()*objectScale);
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
		attributeData = newAttributeData;//shallow copy, should work but might cause problems later on.
		//the the initial position from xml
		setPosition(position[0],position[1]);
		initialPosition = position;
		//THIS NEEDS TO BE LAST
		for(Attribute currentAttribute:attributeData.keys().toArray())
			currentAttribute.setup(this);
		initialVelocity = new float[] {velocity[0],velocity[1]};
	}

	public void update(Array<GameObject> allObjects){
		for(Attribute currentAttribute:attributeData.keys().toArray())
			currentAttribute.update(this, allObjects);
	}

	public Array<Attribute> getAttributes(){
		return attributeData.keys().toArray();
	}

	public int getID() {
		return ID;
	}

	public String getImageFilename() {
		return imageFilename;
	}

	@Override
	public void dispose(){
		disposed = true;
	}
	public void resetObject(){
		setVelocity(initialVelocity);
		setPosition(initialPosition[0], initialPosition[1]);
		disposed = false;
	}
	public boolean isDisposed(){
		return disposed;
	}
	public float[] getVelocity() {
		return velocity;
	}
	public void setVelocity(float[] newVelocity) {
		velocity[0] = newVelocity[0];
		velocity[1] = newVelocity[1];
	}
	public Array<String> getListenerNames(){
		return listenerNames;
	}
	public Texture getTexture(){
		return texture;
	}
	public ObjectMap<Attribute,Array<String>> getAttributeData(){
		return attributeData;
	}
	public float getTextureWidth(){
		return texture.getWidth()*objectScale;
	}
	public float getTextureHeight(){
		return texture.getHeight()*objectScale;
	}
}
