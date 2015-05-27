package org.TheGivingChild.Engine.XML;

import org.TheGivingChild.Engine.Attributes.WinEnum;
import org.TheGivingChild.Screens.ScreenAdapterEnums;
import org.TheGivingChild.Screens.ScreenAdapterManager;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.Array;


public class Level {
	private String levelName;
	private String packageName;
	//private Texture levelThumbnail;
	private Array<GameObject> actors = new Array<GameObject>();
	private Array<WinEnum> winConditions;
	private Array<LoseEnum> loseConditions;
	private boolean win = true;
	
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
		{
			currentObject.act();
			currentObject.update();
		}
		if(WinEnum.ALL_OBJECTS_DESTROYED.checkWin(this)){
			System.out.println("level win");
			win = false;
			ScreenAdapterManager.getInstance().show(ScreenAdapterEnums.MAIN);
			ScreenAdapterManager.getInstance().dispose(ScreenAdapterManager.getInstance().getCurrentEnum());
			resetWin();
		}
	}
	public boolean getWin(){
		return win;
	}
	public void resetWin(){
		win = true;
	}
	
	//will need to iterate over win conditions here
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
