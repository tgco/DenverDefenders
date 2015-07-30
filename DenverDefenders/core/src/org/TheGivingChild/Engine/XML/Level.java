package org.TheGivingChild.Engine.XML;

import org.TheGivingChild.Engine.Attributes.Attribute;
import org.TheGivingChild.Screens.ScreenAdapterManager;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ObjectMap;

// Contains info from xml level file after compiled into objects.  Progresses the level's gameplay.
// Author: Kevin Day, Walter Schlosser
public class Level {
	private String name;
	// Image name to be drawn as background
	private String background;
	private Array<GameObject> objects;
	// Maps string condition to if it has occured yet
	private ObjectMap<String, Boolean> winConditions;
	private ObjectMap<String, Boolean> loseConditions;
	// Maps a condition to the list of Attributes to notify
	private ObjectMap<String, Array<Attribute> > triggeredObservers;
	// True if level is done
	private boolean completed;
	// True if player won the level
	private boolean won;
	// Description shown on a transition into this level
	private String description;

	// Length for a level in seconds DEFAULT: 5s
	// NOTE: if win/lose conditions do not contain timeout, a clock is not drawn or used
	private int levelTime;
	
	// True if this level is a boss game at the end of a maze
	private boolean bossGame;
	
	public Level(ObjectMap<String, String> args, ObjectMap<String, Boolean> winConditions, ObjectMap<String, Boolean> loseConditions, Array<GameObject> objects) {
		this.name = args.get("name", "");
		this.levelTime = Integer.parseInt(args.get("time", "5"));
		this.objects = objects;
		// Have objects register their triggered attributes in the map as observers
		triggeredObservers = new ObjectMap<String, Array<Attribute> >();
		for (GameObject ob : this.objects) {
			ob.register(triggeredObservers);
		}
		this.winConditions = winConditions;
		this.loseConditions = loseConditions;
		
		this.background = args.get("background", "Table.png");
		
		completed = false;
		this.description = args.get("description", "");
		
		bossGame = false;
	}
	
	public void update(){
		// update clock state, check for out of time
		MinigameClock.getInstance().progress();
		if (MinigameClock.getInstance().outOfTime()) throwCondition("timeout");
		
		// CHECK FOR CONCURRENT MODIFICATION ERRORS
		for (int i = 0; i < objects.size; i++) {
			if(!objects.get(i).isDestroyed())
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
	
	// Notifies observers of input events, args are sent in the string
	public void throwInputCondition(String condition) {
		if (condition == null) return;
		//System.out.println("caught: " + condition);
		// Get observers of input name
		Array<Attribute> observers = triggeredObservers.get(condition.split("_")[0]);
		// Update after setting the argument string
		if (observers != null) {
			for (Attribute att : observers) {
				att.setArgString(condition);
				att.update(this);
			}
		}
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
	
	public void setBossGame(boolean b) {
		bossGame = true;
	}
	
	public boolean isBossGame() {
		return bossGame;
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