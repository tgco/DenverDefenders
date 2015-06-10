package org.TheGivingChild.Engine.XML;


import org.TheGivingChild.Engine.MinigameClock;
import org.TheGivingChild.Engine.Attributes.WinEnum;
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
	
	private BitmapFont clockFont;
	private int levelTime = 5;
	
	
	 public Level(String name, String packagename, String levelImage, ObjectMap<WinEnum,Array<String>> newWinData, ObjectMap<LoseEnum,Array<String>> newLoseData, Array<GameObject> objects){ 		//set the level and packageNames
		levelName = name;
		packageName=packagename;
		actors = new Array<GameObject>();
		//add the actors of the level to an array for iteration
		actors.addAll(objects);
		winData = newWinData;
		loseData = newLoseData;
		
		//Set default level length to 10 sec.
		MinigameClock.getInstance().setLevelLength(levelTime);
		
		this.levelImage = levelImage;
		
		completed = false;
		clockFont = new BitmapFont();
		clockFont.setColor(Color.BLACK);
				
	
	}
	
	public void update(){
		//update the state of the actors and clock
		MinigameClock.getInstance().render();
//				
//		clockBatch.begin();
//		clockFont.draw(clockBatch, MinigameClock.getInstance().toString(), Gdx.graphics.getWidth() / 3,Gdx.graphics.getHeight() - 10);
//		clockBatch.end();
//				
		//System.out.println("gameclock is at " + MinigameClock.getInstance().getLevelTime());
		
		for(GameObject currentObject:actors){
			currentObject.update(actors);
		}
		//check the win conditions.
		for(WinEnum winEnum: winData.keys().toArray()){
			winEnum.checkWin(this);
		}
		

		for (LoseEnum loseEnum: loseData.keys().toArray()) {
			loseEnum.checkLose(this);

		}
		
		
	}
	public void resetLevel(){
		//Reset level clock to 10
		MinigameClock.getInstance().setLevelLength(levelTime);
		
		//remove the game objects from the stage
		for(GameObject gameObject: actors){
			gameObject.remove();
		}
		//go to the main screen, will likely need to return to the last maze screen being played
		//ScreenAdapterManager.getInstance().show(ScreenAdapterEnums.MAIN);
	}
	//add the objects to the stage, allowing them to be drawn and have the listeners work
	public void loadObjectsToStage(){
		for(GameObject gameObject: actors){
			ScreenAdapterManager.getInstance().game.getStage().addActor(gameObject);
		}
	}
	
	public boolean checkLose(){
		return MinigameClock.getInstance().outOfTime();
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
}
