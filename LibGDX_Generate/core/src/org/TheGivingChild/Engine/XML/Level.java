package org.TheGivingChild.Engine.XML;

import org.TheGivingChild.Engine.Attributes.WinEnum;
import org.TheGivingChild.Screens.ScreenAdapterEnums;
import org.TheGivingChild.Screens.ScreenAdapterManager;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.Array;


public class Level {
	private String levelName;
	private String packageName;
	private Array<GameObject> actors;
	private Array<WinEnum> winConditions;
	private Array<LoseEnum> loseConditions;
	public Level(String name, String packagename, String levelImage, Array<WinEnum> newWinConditions, Array<LoseEnum> newLoseConditions, Array<GameObject> objects){
		//set the level and packageNames
		levelName = name;
		packageName=packagename;
		actors = new Array<GameObject>();
		//add the actors of the level to an array for iteration
		actors.addAll(objects);
		winConditions = new Array<WinEnum>();
		loseConditions = new Array<LoseEnum>();
		//add win and lose conditions to iterate over
		winConditions.addAll(newWinConditions);
		loseConditions.addAll(newLoseConditions);
	}
	
	public void update(){
		//update the state of the actors
		for(GameObject currentObject:actors){
			currentObject.update(actors);
		}
		//check the win conditions.
		for(WinEnum winEnum: WinEnum.values()){
			winEnum.checkWin(this);
		}
	}
	public void resetLevel(){
		//remove the game objects from the stage
		for(GameObject gameObject: actors){
			gameObject.remove();
		}
		//go to the main screen, will likely need to return to the last maze screen being played
		ScreenAdapterManager.getInstance().show(ScreenAdapterEnums.MAIN);
	}
	//add the objects to the stage, allowing them to be drawn and have the listeners work
	public void loadObjectsToStage(){
		for(GameObject gameObject: actors){
			ScreenAdapterManager.getInstance().game.getStage().addActor(gameObject);
		}
	}
	
	public boolean checkLose(){
		return false;
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
		return "LEVELIMAGESTRING";
	}
	
	public Array<GameObject> getGameObjects(){
		return actors;
	}
	
	public void addWinCondition(WinEnum newWinCondition){
		winConditions.add(newWinCondition);
	}
	
	public Array<WinEnum> getWinConditions(){
		return winConditions;
	}
	
	public void addLoseCondition(LoseEnum newLoseCondition){
		loseConditions.add(newLoseCondition);
	}
	
	public Array<LoseEnum> getLoseConditions(){
		return loseConditions;
	}
}
