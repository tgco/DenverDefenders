package org.TheGivingChild.Engine;

import java.util.ArrayList;

import com.badlogic.gdx.utils.XmlReader;
import com.badlogic.gdx.utils.XmlReader.Element;
import com.sun.xml.internal.bind.v2.runtime.unmarshaller.XsiNilLoader.*;
import com.sun.xml.internal.bind.v2.runtime.unmarshaller.XsiNilLoader.Array;

//Use this to read the XML File into a Level
//will read in XML fle, translate into LevelGoals, GameObjects, and other data, compile them into a level, then pass that level up 
public class XML_Reader {	
	private XmlReader reader = new XmlReader();
	String xml_file;
	Element root;//this is the root of the tree that is created by reader.parse(xml_file)
	
	public void setupNewFile(String XML_Filename){//will read in a new XML file as a big string, will try to leave space for the DHD, needs to be called each time you want to read in a minigame
		xml_file = XML_Filename;
		root = reader.parse(xml_file);
	}
	
	public Level getLevel(){//compiles all the data into a level and returns it
		return new Level("PLACEHOLDER","PLACEHOLDER",new LevelGoal(),new ArrayList<GameObject>());
	}
	
	private ArrayList<GameObject> compileGameObjects(){//will parse through xml_file and get all game objects and their attributes
		ArrayList<GameObject> listOfObjects = new ArrayList<GameObject>();
		com.badlogic.gdx.utils.Array<Element> gameObjects = root.getChildrenByName("GameObject");
		for(Element child:gameObjects){
			int health = Integer.parseInt(child.getAttribute("health"));
			double speed = Double.parseDouble(child.getAttribute("speed"));
			
			GameObject temp = new GameObject(health,speed,path);
		}
		return listOfObjects;
	}
	
	private LevelGoal compileLevelGoal(){//will parse through xml_file and get the win/loss conditions
		LevelGoal levelGoal = new LevelGoal();
		//that code tho
		return levelGoal;
	}
}
