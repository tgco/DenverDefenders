package org.TheGivingChild.Engine;

import java.util.ArrayList;

import com.badlogic.gdx.graphics.Texture;

//This clas defines level characteristics.
public class Level {
	private String levelName;
	private String packetName;
	private Texture levelThumbnail;
	private LevelGoal levelGoal;
	private ArrayList<GameObject> actors = new ArrayList<GameObject>();
	
	
	public Level(String name, String packetName, String levelImage, LevelGoal goal, ArrayList<GameObject> objects){
		levelName = name;
		this.packetName = name;
		levelThumbnail = new Texture("levelImage.png");
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
