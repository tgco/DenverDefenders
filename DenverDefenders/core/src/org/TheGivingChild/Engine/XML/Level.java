package org.TheGivingChild.Engine.XML;

import org.TheGivingChild.Engine.MinigameClock;
import org.TheGivingChild.Screens.ScreenAdapterManager;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ObjectMap;

public class Level {
	private String levelName;
	private String packageName;
	private String levelImage;
	private Array<GameObject> actors;
	private ObjectMap<WinEnum,Array<String>> winData;
	private ObjectMap<LoseEnum,Array<String>> loseData;
	private boolean completed;
	private boolean won;
	private String description;
	
	// Move into asset manager
	private BitmapFont clockFont;
	private int levelTime = 0;
	
	
	public Level(String name, String packagename, String levelImage, String description, ObjectMap<WinEnum,Array<String>> newWinData, ObjectMap<LoseEnum,Array<String>> newLoseData, Array<GameObject> objects){ 		//set the level and packageNames
		levelName = name;
		packageName=packagename;
		actors = new Array<GameObject>();
		//add the actors of the level to an array for iteration
		actors.addAll(objects);
		winData = newWinData;
		loseData = newLoseData;
		
		this.levelImage = levelImage;
		
		completed = false;
		clockFont = new BitmapFont();
		clockFont.setColor(Color.BLACK);
		
		this.description = description;
		
		for(WinEnum current : winData.keys().toArray())
			current.setup(this);
		for(LoseEnum current : loseData.keys().toArray())
			current.setup(this);
	}
	
	public void update(){
		//update the state of the actors and clock
		MinigameClock.getInstance().render();
		
		for(GameObject currentObject : actors){
			if(!currentObject.isDisposed())
				currentObject.update(actors);
			else
				actors.removeValue(currentObject, true);
		}
		
		//check the win/lose conditions.
		for(WinEnum winEnum : winData.keys().toArray()){
			winEnum.checkWin(this);
			if (completed) return;
		}
		
		for (LoseEnum loseEnum : loseData.keys().toArray()) {
			loseEnum.checkLose(this);
			if (completed) return;
		}

	}
	
	public void resetLevel(){
		//Reset level clock
		MinigameClock.getInstance().setLevelLength(levelTime);
		
		//remove the game objects from the stage
		for(GameObject gameObject : actors){
			gameObject.remove();
		}
		
		//go to the main screen, will likely need to return to the last maze screen being played
		for(WinEnum current : winData.keys().toArray())
			current.setup(this);
		
		for(LoseEnum current : loseData.keys().toArray())
			current.setup(this);
	}
	
	//add the objects to the stage, allowing them to be drawn and have the listeners work
	public void loadObjectsToStage() {
		for(GameObject gameObject: actors){
			// COUPLED TO THE MAIN CLASS STAGE, CHANGE THIS
			ScreenAdapterManager.getInstance().game.getStage().addActor(gameObject);
		}
	}
	
	public void setCompleted(boolean state) {
		completed = state;
	}
	
	public void setWon(boolean state) {
		won = state;
	}
	
	public String toString(){
		String levelString="Name: " + levelName + " Package: " + packageName + "\n";
		for(GameObject curObj:actors)
			levelString+="Obj:\t" + curObj.toString() + "\n";
		return levelString+"\n";
	}
	
	public String getLevelName(){
		return levelName;
	}

	public String getPackageName(){
		return packageName;
	}
	
	public String getLevelImage(){
		return levelImage;
	}
	
	public Array<GameObject> getGameObjects(){
		return actors;
	}
	
	public Array<WinEnum> getWinConditions(){
		return winData.keys().toArray();
	}
	
	public Array<LoseEnum> getLoseConditions(){
				return loseData.keys().toArray();
	}
	public Array<String> getWinInfo(WinEnum winEnum){
		return winData.get(winEnum);
	}
	public Array<String> getLoseInfo(LoseEnum loseEnum){
		return loseData.get(loseEnum);
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
	public int getLevelTime() {
		return levelTime;
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
		GameObject targetObject = null;
		for(GameObject currentObject : actors)
			if(currentObject.getID() == ID)
				targetObject = currentObject;
		return targetObject;
	}
}