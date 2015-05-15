package org.TheGivingChild.Engine;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.Array;

//This clas defines level characteristics.
public class Level {
	private String levelName;
	private String packetName;
	private Texture levelThumbnail;
	private LevelGoal levelGoal;
	private Array<GameObject> actors = new Array<GameObject>();
	
	
	public Level(String name, String packetName, String levelImage, LevelGoal goal, Array<GameObject> objects){
		levelName = name;
		this.packetName = packetName;
		//levelThumbnail = new Texture("levelImage.png");
		levelGoal = goal;
		actors.addAll(objects);
	}
	
	public String getLevelName(){
		return levelName;
	}

	public String getPacketName(){
		return packetName;
	}
}
