package org.TheGivingChild.Engine;

import java.util.ArrayList;

import com.badlogic.gdx.graphics.Texture;

//This clas defines level characteristics.
public class Level {
	private String levelName;
	private Texture levelThumbnail;
	private LevelGoal levelGoal;
	private ArrayList<GameObject> actors = new ArrayList<GameObject>();
	
	public Level(String name, String levelImage, LevelGoal goal, ArrayList<GameObject> objects){
		
	}
	
	
}
