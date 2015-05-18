package org.TheGivingChild.Engine;

import java.awt.Point;
import java.io.BufferedReader;
import java.io.FileReader;

import com.badlogic.gdx.utils.XmlReader;
import com.badlogic.gdx.utils.XmlReader.Element;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.reflect.Method;
//import com.badlogic.gdx.utils.reflect.Method;

//Use this to read the XML File into a Level
//will read in XML fle, translate into LevelGoals, GameObjects, and other data, compile them into a level, then pass that level up 
public class XML_Reader {
	
	private XmlReader reader = new XmlReader();
	private String xml_file;
	private Element root;//this is the root of the tree that is created by reader.parse(xml_file)
	
	//the main method is for testing only
	public static void main(String cheese[]){
		XML_Reader test = new XML_Reader();
		String filename = "testMinigame.xml";
		test.setupNewFile(filename);
		Array<GameObject> testObjects = test.compileGameObjects();
		
		
	}
	
	public void setupNewFile(String XML_Filename){//will read in a new XML file as a big string, will try to leave space for the DHD, needs to be called each time you want to read in a minigame
		xml_file ="";
		try{
			BufferedReader fileReader = new BufferedReader(new FileReader(XML_Filename));
			while(fileReader.ready()){
				xml_file+=fileReader.readLine();//might need to clean up the xml here
			}
		}catch(Exception e){
			System.out.println("Error opening xml file. Filename: " + XML_Filename + "Exception: " + e);
		}
		root = reader.parse(xml_file);
	}
	
	private Array<GameObject> compileGameObjects(){//will parse through xml_file and get all game objects and their attributes
		Array<GameObject> listOfObjects = new Array<GameObject>();
		Array<Element> gameObjects = root.getChildrenByName("GameObject");
		for(Element currentObject:gameObjects){
			GameObject temp = new GameObject();
			String attributes[] = currentObject.getAttribute("attributes").split(",");
			for(String currentAttribute:attributes){
				//look up the object of name currentAttribute and add it to currentObject's list of Attributes.
				temp.addValidAttribute(currentAttribute);
				
			}
		}
		return listOfObjects;
	}
	
	//helper method for compileGameObjects
	private Array<Point> stringToPath(String sPath){//Working
		Array<Point> newPath = new Array<Point>();
		String points[] = sPath.split(";");
		String temp[];
		for(int i = 0; i < points.length; i++){
			temp = points[i].split(",");
			Point temp_P = new Point();//herd u liek temps.
			temp_P.setLocation(Integer.parseInt(temp[0]),Integer.parseInt(temp[1]));
			newPath.add(temp_P);
		}
		return newPath;
	}
	
	private LevelGoal compileLevelGoal(){//will parse through xml_file and get the win/loss conditions
		LevelGoal levelGoal = new LevelGoal();
		//that code tho
		return levelGoal;
	}
	
	public Level getLevel(){//compiles all the data into a level and returns it
		return new Level("PLACEHOLDER","PLACEHOLDER","PLACEHOLDER",new LevelGoal(),new Array<GameObject>());
	}
}
