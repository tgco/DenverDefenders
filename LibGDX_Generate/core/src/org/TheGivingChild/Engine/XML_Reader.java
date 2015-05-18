package org.TheGivingChild.Engine;

import java.awt.Point;
import java.io.BufferedReader;
import java.io.FileReader;

import com.badlogic.gdx.utils.ArrayMap.Values;
import com.badlogic.gdx.utils.XmlReader;
import com.badlogic.gdx.utils.XmlReader.Element;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.reflect.Method;
import com.badlogic.gdx.utils.ObjectMap;

//Use this to read the XML File into a Level
//will read in XML fle, translate into LevelGoals, GameObjects, and other data, compile them into a level, then pass that level up 
public class XML_Reader {
	
	//the main method is for testing only
	public static void main(String cheese[]){
		XML_Reader test = new XML_Reader();
		String filename = "testMinigame.xml";
		test.setupNewFile(filename);
		Array<GameObject> testObjects = test.compileGameObjects();
		System.out.println("\nUPDATING");
		for(GameObject bob: testObjects){
			bob.update();
		}
		/*ObjectMap<String,String> testest = new ObjectMap<String,String>();
		testest.put("1","A");
		testest.put("1","B");
		System.out.println(testest.get("1"));
		System.out.println(testest.get("2"));
		*/
		
	}
	private XmlReader reader = new XmlReader();
	private String xml_file;
	
	private Element root;//this is the root of the tree that is created by reader.parse(xml_file)
	
	private Array<GameObject> compileGameObjects(){//will parse through xml_file and get all game objects and their attributes
		Array<GameObject> listOfObjects = new Array<GameObject>();
		for(Element currentObject:root.getChildrenByName("GameObject")){//iterate through game objects
			GameObject temp = new GameObject(currentObject.getIntAttribute("ID"));
			for(String currentAttribute:currentObject.getAttribute("attributes").split(",")){//iterate through each GameObject's attributes
				//look up the object of name currentAttribute and add it to currentObject's list of Attributes.
				if(currentObject.getChildByName(currentAttribute).getAttributes() != null){
					for(String currentValue:currentObject.getChildByName(currentAttribute).getAttributes().values()){
						temp.addValidAttribute(currentAttribute, currentValue);
						System.out.println(currentAttribute + ", " + currentValue);
					}
				}else{
					temp.addValidAttribute(currentAttribute, null);
				}
			}
			listOfObjects.add(temp);
		}
		return listOfObjects;
	}
	
	private LevelGoal compileLevelGoal(){//will parse through xml_file and get the win/loss conditions
		LevelGoal levelGoal = new LevelGoal();
		//that code tho
		return levelGoal;
	}
	
	public Level getLevel(){//compiles all the data into a level and returns it
		return new Level("PLACEHOLDER","PLACEHOLDER","PLACEHOLDER",new LevelGoal(),new Array<GameObject>());
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
}
