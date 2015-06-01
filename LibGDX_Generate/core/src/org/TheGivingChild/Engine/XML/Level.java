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
	private Array<GameObject> actors; 
	private Array<GameObject> actorsStatic;
	private Array<WinEnum> winConditions;
	private Array<LoseEnum> loseConditions;
	private boolean win = true;
	
	public Level(String name, String packagename, String levelImage, Array<WinEnum> newWinConditions, Array<LoseEnum> newLoseConditions, Array<GameObject> objects){
		levelName = name;
		packageName=packagename;
		//levelThumbnail = new Texture("levelImage.png");
		actors = new Array<GameObject>();
		actorsStatic = new Array<GameObject>();
		actorsStatic.addAll(objects);
		for(GameObject object: actorsStatic){
			GameObject deepCopy = object.GameObject();
			actors.add(deepCopy);
		}
		winConditions = new Array<WinEnum>();
		loseConditions = new Array<LoseEnum>();
		winConditions.addAll(newWinConditions);
		loseConditions.addAll(newLoseConditions);
	}
	
	public void update(){
		for(GameObject currentObject:actors)
		{
			currentObject.update();
		}
		for(WinEnum winEnum: WinEnum.values()){
			winEnum.checkWin(this);
		}
	}
	public void resetLevel(){
		System.out.println("After level completes:");
		System.out.println(actors.toString());
		System.out.println("Static actors: " +actorsStatic.toString());
		actors.clear();
		for(GameObject object: actorsStatic){
			GameObject deepCopy = object.GameObject();
			actors.add(deepCopy);
		}
		System.out.println("After objects are re deep copied");
		System.out.println(actors.toString());
		System.out.println("Static actors: " +actorsStatic.toString());
		resetWin();
		ScreenAdapterManager.getInstance().show(ScreenAdapterEnums.MAIN);
		ScreenAdapterManager.getInstance().dispose(ScreenAdapterManager.getInstance().getCurrentEnum());
	}
	
	public boolean getWin(){
		return win;
	}
	private void resetWin(){
		win = true;
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
