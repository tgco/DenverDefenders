package org.TheGivingChild.Engine.XML;

import org.TheGivingChild.Engine.MinigameClock;
import org.TheGivingChild.Engine.Attributes.Attribute;
import org.TheGivingChild.Screens.ScreenAdapterManager;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ObjectMap;

public class Level {
	private String name;
	private String background;
	private Array<GameObject> objects;
	private ObjectMap<String, Boolean> winConditions;
	private ObjectMap<String, Boolean> loseConditions;
	// Maps a condition to the list of Attributes to notify
	private ObjectMap<String, Array<Attribute> > triggeredObservers;
	private boolean completed;
	private boolean won;
	private String description;
	
	// Move into asset manager
	private BitmapFont clockFont;
	// Length for a level in seconds
	private int levelTime = 5;
	
	
	public Level(String name, String background, String description, ObjectMap<String, Boolean> winConditions, ObjectMap<String, Boolean> loseConditions, Array<GameObject> objects) {
		this.name = name;
		this.objects = objects;
		// Have objects register their triggered attributes in the map as observers
		triggeredObservers = new ObjectMap<String, Array<Attribute> >();
		for (GameObject ob : this.objects) {
			ob.register(triggeredObservers);
		}
		this.winConditions = winConditions;
		this.loseConditions = loseConditions;
		
		this.background = background;
		
		completed = false;
		// SHOULD SET FROM ASSET MANAGER
		clockFont = new BitmapFont();
		clockFont.setColor(Color.BLACK);
		
		this.description = description;
	}
	
	public void update(){
		// update clock state, check for out of time
		MinigameClock.getInstance().progress();
		if (MinigameClock.getInstance().outOfTime()) throwCondition("timeout");
		
		// CHECK FOR CONCURRENT MODIFICATION ERRORS
		for (int i = 0; i < objects.size; i++) {
			if(!objects.get(i).isDisposed())
				objects.get(i).update(this);
			else
				objects.removeIndex(i);
		}
		
		// Check if won
		won = allTrueCheck(winConditions);
		// Update completed to won (true if won)
		completed = won;
		// Lose check if still haven't won
		if (!completed)
			completed = allTrueCheck(loseConditions);
	}
	
	// Returns true if all conditions have been met
	public Boolean allTrueCheck(ObjectMap<String, Boolean> conditions) {
		for (Boolean b : conditions.values()) {
			if (!b) return false;
		}
		return true;
	}
	
	// Notifies all observers of the thrown condition, and updates win/lose conditions
	public void throwCondition(String condition) {
		if (condition == null) return;
		Array<Attribute> observers = triggeredObservers.get(condition);
		if (observers != null) {
			for (Attribute att : observers) {
				att.update(this);
			}
		}
		if (winConditions.containsKey(condition)) winConditions.put(condition, true);
		if (loseConditions.containsKey(condition)) loseConditions.put(condition, true);
	}
	
	// Sets the texture for each object from the asset manager
	public void setObjectTextures(AssetManager manager) {
		for (GameObject ob : objects) {
			ob.retrieveTexture(manager);
		}
	}
	
	public void resetLevel(){
		//Reset level clock
		MinigameClock.getInstance().setLevelLength(levelTime);
		
		//remove the game objects from the stage
		for(GameObject gameObject : objects){
			gameObject.remove();
		}
		
		// Reset for next play
		for(GameObject gameObject : objects){
			gameObject.resetObject();
		}
		
		// Reset conditions
		for (String cond : winConditions.keys()) {
			winConditions.put(cond, false);
		}
		for (String cond : loseConditions.keys()) {
			loseConditions.put(cond, false);
		}
		completed = won = false;
	}
	
	//add the objects to the stage, allowing them to be drawn and have the listeners work
	public void loadObjectsToStage() {
		for(GameObject gameObject: objects){
			ScreenAdapterManager.getInstance().game.getStage().addActor(gameObject);
		}
	}
	
	public void setCompleted(boolean state) {
		completed = state;
	}
	
	public void setWon(boolean state) {
		won = state;
	}
	
	public ObjectMap<String, Boolean> getWinConditions() {
		return winConditions;
	}
	
	public ObjectMap<String, Boolean> getLoseConditions() {
		return loseConditions;
	}
	
	public String toString(){
		String levelString="Name: " + name + "\n";
		for(GameObject curObj:objects)
			levelString+="Obj:\t" + curObj.toString() + "\n";
		return levelString;
	}
	
	public String getLevelName(){
		return name;
	}
	
	public String getLevelImage(){
		return background;
	}
	
	public Array<GameObject> getGameObjects(){
		return objects;
	}
	
	public boolean getCompleted() {
		return completed;
	}
	public boolean getWon() {
		return won;
	}
	public BitmapFont getClockFont() {
		return clockFont;
	}
	public String getDescription(){
		return description;
	}
	/**
	 * 
	 * @param int ID of the object to find
	 * @return returns the desired object or null if the object does not exist
	 */
	public GameObject getObjectOfID(int ID){
		for(GameObject currentObject : objects) {
			if(currentObject.getID() == ID) {
				return currentObject;
			}
		}
		return null;
	}
}