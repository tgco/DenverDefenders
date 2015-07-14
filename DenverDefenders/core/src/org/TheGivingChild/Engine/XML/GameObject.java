package org.TheGivingChild.Engine.XML;

import java.util.Random;

import org.TheGivingChild.Engine.Attributes.Attribute;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ObjectMap;

/**
 * Used to store all information on each object within the game<br>
 * @author Mostly Kevin D
 */
public class GameObject extends Actor {
	/** Unique ID assigned to each GameObject*/
	private int id;
	private String imageFilename;
	/** Two element velocity array<br> First element is X velocity, second is Y velocity */
	private float[] velocity;
	private float[] initialVelocity;
	private float[] acceleration; // no initial since only constant accelerations are currently supported
	private float[] position;
	private float[] initialPosition;
	// If true, this object will be removed from the level until reset
	private boolean destroyed;
	private Texture texture;
	// Scale to be applied to image size for drawing
	private float objectScaleX, objectScaleY;
	// Attributes which run every frame
	private Array<Attribute> continuousAttributes;
	// Attributes which update on a triggered condition
	private Array<Attribute> triggeredAttributes;

	public GameObject(ObjectMap<String, String> args, Array<Attribute> continuousAttributes, Array<Attribute> triggeredAttributes){
		destroyed = false;
		//set values from args
		id = Integer.parseInt(args.get("id"));
		imageFilename = args.get("image");
		
		// Set position based on screen pixels, xml is defined based on 1024x600 screen
		float x, y;
		// Random check
		Random rand = new Random();
		if (args.get("x").matches("rand_\\d+_\\d+")) {
			String[] split = args.get("x").split("_");
			int lower = Integer.parseInt(split[1]);
			int upper = Integer.parseInt(split[2]);
			x = rand.nextInt(upper - lower) + lower;
		} 
		else x = Float.parseFloat(args.get("x"));
		if (args.get("y").matches("rand_\\d+_\\d+")) {
			String[] split = args.get("y").split("_");
			int lower = Integer.parseInt(split[1]);
			int upper = Integer.parseInt(split[2]);
			y = rand.nextInt(upper - lower) + lower;
		} 
		else y = Float.parseFloat(args.get("y"));
		
		// Adjust to actual screen size in pixels
		float adjustedX = x/1024f * Gdx.graphics.getWidth();
		float adjustedY = y/600f * Gdx.graphics.getHeight();
		position = new float[] { adjustedX, adjustedY };
		
		this.continuousAttributes = continuousAttributes;
		this.triggeredAttributes = triggeredAttributes;
		
		//initialize a velocity of 0
		velocity = new float[] { 0, 0 };
		acceleration = new float[] { 0, 0 };
		
		setPosition( position[0], position[1]);
		initialPosition = position;
		// Run any setup necessary for attributes
		for(Attribute att : continuousAttributes) {
			att.setup(this);
		}
		for (Attribute att : triggeredAttributes) {
			att.setup(this);
		}
		
		// Save this afterwards in case an attribute set the initial values
		initialVelocity = new float[] {velocity[0],velocity[1]};
	}
	
	// Retrieves the texture from the asset manager and sets scale
	public void retrieveTexture(AssetManager manager) {
		texture = manager.get("LevelImages/" + imageFilename, Texture.class);
		//scale the object to the width of the new screen
		objectScaleX = Gdx.graphics.getWidth()/1024f;
		//scale the object to the height of the new screen
		objectScaleY = Gdx.graphics.getHeight()/600f;
		//scale small objects an additional 100%
		if(texture.getWidth() <= 32 && texture.getHeight() <= 32){
			objectScaleX *= 2;
			objectScaleY *= 2;
		}
		
		//set the bounds to be as large as the textures size
		setBounds(getX(), getY(), texture.getWidth()*objectScaleX, texture.getHeight()*objectScaleY);
		
	}

	// Updates all continuous attributes
	public void update(Level level){
		for (Attribute att : continuousAttributes)
			att.update(level);
	}
	
	// Registers the triggered attributes under the condition in the passed map.  Adds the condition as a key if it is not set yet.
	public void register(ObjectMap<String, Array<Attribute> > observerMap) {
		for (Attribute att : triggeredAttributes) {
			String trigger = att.getTrigger();
			if (!observerMap.containsKey(trigger))
				observerMap.put(trigger, new Array<Attribute>());
			observerMap.get(trigger).add(att);
		}
	}

	public int getID() {
		return id;
	}

	public String getImageFilename() {
		return imageFilename;
	}
	public void destroy(){
		destroyed = true;
	}
	// Resets velocity, position, and destroyed status
	public void resetObject(){
		setVelocity(initialVelocity[0], initialVelocity[1]);
		setPosition(initialPosition[0], initialPosition[1]);
		destroyed = false;
	}
	public boolean isDestroyed(){
		return destroyed;
	}
	public float[] getVelocity() {
		return velocity;
	}
	public float[] getAcceleration() {
		return acceleration;
	}
	public void setVelocity(float vx, float vy) {
		velocity[0] = vx;
		velocity[1] = vy;
	}
	public void setAcceleration(float ax, float ay) {
		acceleration[0] = ax;
		acceleration[1] = ay;
	}
	public Texture getTexture(){
		return texture;
	}
	// Returns the scaled texture width
	public float getTextureWidth(){
		return texture.getWidth()*objectScaleX;
	}
	// Returns the scaled texture height
	public float getTextureHeight(){
		return texture.getHeight()*objectScaleY;
	}
	public float[] getInitialPosition(){
		return initialPosition;
	}
}
