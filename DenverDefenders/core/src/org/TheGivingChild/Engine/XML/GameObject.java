package org.TheGivingChild.Engine.XML;

import java.util.Locale;

import org.TheGivingChild.Engine.InputListenerEnums;
import org.TheGivingChild.Engine.TGC_Engine;
import org.TheGivingChild.Engine.Attributes.Attribute;
import org.TheGivingChild.Screens.ScreenAdapterManager;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.ObjectMap;

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
	private float objectScaleX, objectScaleY;
	/** list of the names of the listeners associated with this object*/
	private Array<String> listenerNames;
	/** A map which contains the Attributes associated with this object, as well as the information mapped to the given attribute */
	private ObjectMap<Attribute,Array<String>> attributeData;
	// List of active attributes for reference by xml write utilities
	private Array<AttributeEnum> attributesList;

	public GameObject(int newID, String img,float[] newPosition, Array<String> newListenerNames,ObjectMap<AttributeEnum,Array<String>> newAttributeData){
		listenerNames = new Array<String>();
		manager = new AssetManager();
		disposed = false;
		listenerNames.addAll(newListenerNames);
		//set the id from the xml
		ID = newID;
		//set the imagefilename from the xml
		imageFilename = "LevelImages/"+img;
		position = newPosition;
		
		//initialize a velocity of 0
		velocity = new float[] {0,0};
		//get the reference to the game
		game = ScreenAdapterManager.getInstance().game;
		//load the required texture for this object
		manager = game.getAssetManager();
		manager.load(imageFilename, Texture.class);
		manager.finishLoadingAsset(imageFilename);
		texture = manager.get(imageFilename, Texture.class);
		//scale the object to the width of the new screen
		objectScaleX = Gdx.graphics.getWidth()/1024f;
		//scale the object to the height of the new screen
		objectScaleY = Gdx.graphics.getHeight()/576f;
		//scale small objects an additional 50%
		if(texture.getWidth() <= 32 && texture.getHeight() <= 32){
			objectScaleX *= 2;
			objectScaleY *= 2;
		}
		
		
		//set the bounds to be as large as the textures size
		setBounds(getX(), getY(), texture.getWidth()*objectScaleX, texture.getHeight()*objectScaleY);
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
		attributeData = new ObjectMap<Attribute, Array<String> >();
		attributesList = new Array<AttributeEnum>();
		//the the initial position from xml
		setPosition(position[0],position[1]);
		initialPosition = position;
		//THIS NEEDS TO BE LAST
		for(AttributeEnum currentAttribute : newAttributeData.keys().toArray()) {
			// construct attributes
			Attribute toAdd = currentAttribute.construct();
			attributeData.put(toAdd, newAttributeData.get(currentAttribute));
			// push to list
			attributesList.add(currentAttribute);
			toAdd.setup(this);
		}
		initialVelocity = new float[] {velocity[0],velocity[1]};
		velocity = initialVelocity;
	}

	public void update(Array<GameObject> allObjects){
		for(Attribute currentAttribute : attributeData.keys().toArray())
			currentAttribute.update(this, allObjects);
	}

	public Array<AttributeEnum> getAttributes(){
		return attributesList;
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
		return texture.getWidth()*objectScaleX;
	}
	public float getTextureHeight(){
		return texture.getHeight()*objectScaleY;
	}
	public float[] getInitialPosition(){
		return initialPosition;
	}
}
