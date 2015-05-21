package org.TheGivingChild.Engine.XML;

import org.TheGivingChild.Engine.Attributes.WinEnum;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.Array;

//This clas defines level characteristics.
public class Level {
	private String levelName;
	private String packageName;
	private Texture levelThumbnail;
	private LevelGoal levelGoal;
	private Array<GameObject> actors = new Array<GameObject>();
	private Array<WinEnum> winConditions;
	private Array<LoseEnum> lossConditions;
	
	public Level(String name, String packagename, String levelImage, LevelGoal goal, Array<GameObject> objects){
		levelName = name;
		packageName=packagename;
		//levelThumbnail = new Texture("levelImage.png");
		levelGoal = goal;
		actors.addAll(objects);
		winConditions = new Array<WinEnum>();
	}
	
	public void update(){
		for(GameObject currentObject:actors)
			currentObject.update();
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
}
