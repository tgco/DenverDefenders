package org.TheGivingChild.Engine.XML;

import org.TheGivingChild.Engine.Attributes.WinEnum;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.Array;


public class Level {
	private String levelName;
	private String packageName;
	//private Texture levelThumbnail;
	private Array<GameObject> actors = new Array<GameObject>();
	private Array<WinEnum> winConditions;
	private Array<LoseEnum> loseConditions;
	
	public Level(String name, String packagename, String levelImage, Array<WinEnum> newWinConditions, Array<LoseEnum> newLoseConditions, Array<GameObject> objects){
		levelName = name;
		packageName=packagename;
		//levelThumbnail = new Texture("levelImage.png");
		actors.addAll(objects);
		winConditions = new Array<WinEnum>();
		loseConditions = new Array<LoseEnum>();
		
		System.out.println(newWinConditions);
		winConditions.addAll(newWinConditions);
		System.out.println(newLoseConditions);
		loseConditions.addAll(newLoseConditions);
	}
	
	public void update(){
		for(GameObject currentObject:actors)
			currentObject.update();
	}
	
	public boolean checkWin(){
		return false;
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
