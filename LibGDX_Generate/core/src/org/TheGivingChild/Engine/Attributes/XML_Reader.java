package org.TheGivingChild.Engine.Attributes;

import java.io.BufferedReader;
import java.io.FileReader;

import com.badlogic.gdx.utils.ArrayMap.Values;
import com.badlogic.gdx.utils.XmlReader;
import com.badlogic.gdx.utils.XmlReader.Element;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.reflect.Method;
import com.badlogic.gdx.utils.ObjectMap;
import com.badlogic.gdx.math.GridPoint2;

//Use this to read the XML File into a Level
//will read in XML fle, translate into LevelGoals, GameObjects, and other data, compile them into a level, then pass that level up 
public class XML_Reader {
	
	//the main method is for testing only
	
	public static void main(String cheese[]){
		XML_Reader test = new XML_Reader();
		String filename = "testOut.xml";
		test.setupNewFile(filename);
		Array<GameObject> testObjects = test.compileGameObjects();
		for(GameObject bob: testObjects){
			bob.update();
		}
		Level larry = test.compileLevel();
		
	}
	
	private XmlReader reader = new XmlReader();
	private String xml_file;
	private Element root;//this is the root of the tree that is created by reader.parse(xml_file)
	
	public Level compileLevel(){		
		return new Level(root.getAttribute("levelName"),root.getAttribute("packageName"),root.getAttribute("levelImage"),compileLevelGoal(),compileGameObjects());
	}
	
	public Array<GameObject> compileGameObjects(){//will parse through xml_file and get all game objects and their attributes
		Array<GameObject> listOfObjects = new Array<GameObject>();
		for(Element currentObject:root.getChildrenByName("GameObject")){//iterate through game objects
			GameObject temp = new GameObject(currentObject.getIntAttribute("ID"),currentObject.getAttribute("imageFilename"),stringToPoint(currentObject.getAttribute("initialLocation")));//hardcoded values which must always be written down in the .xml file
			//System.out.println(temp.getID());
			//if(!currentObject.getAttribute("attributes").isEmpty()){
				for(String currentAttribute:currentObject.getAttribute("attributes").split(",")){//iterate through each GameObject's attributes
					//System.out.println("\t|" + currentAttribute);
					if(!currentObject.getAttribute("attributes").isEmpty()){//look up the object of name currentAttribute and add it to currentObject's list of Attributes
						for(int i = 0; i< currentObject.getChildByName(currentAttribute).getAttributes().size;i++){//go to each value in each attribute
							temp.addValidAttribute(currentAttribute, currentObject.getChildByName(currentAttribute).getAttribute("value" + (i+1)));
							//System.out.println("\t\t" + currentAttribute + "; " + currentObject.getChildByName(currentAttribute).getAttribute("value" + (i+1)));
						}
					}else temp.addValidAttribute(currentAttribute, null);
				}
			//}else{System.out.println("NULL ATTRIBUTE LIST");}
			listOfObjects.add(temp);
		}
		return listOfObjects;
	}
	
	public void setupNewFile(String XML_Filename){//will read in a new XML file as a big string, will try to leave space for the DHD, needs to be called each time you want to read in a minigame
		xml_file ="";
		try{
			BufferedReader fileReader = new BufferedReader(new FileReader(XML_Filename));
			while(fileReader.ready()) xml_file+=fileReader.readLine();//might need to clean up the xml here
			fileReader.close();
		}catch(Exception e){System.out.println("Error opening xml file. Filename: " + XML_Filename + "Exception: " + e);}
		root = reader.parse(xml_file);
	}
	
	//helper method for compileGameObjects
	private Array<GridPoint2> stringToPath(String sPath){//Working
		Array<GridPoint2> newPath = new Array<GridPoint2>();
		String points[] = sPath.split(";");
		for(int i = 0; i < points.length; i++){
			newPath.add(stringToPoint(points[i]));
		}
		return newPath;
	}
	
	private GridPoint2 stringToPoint(String toPoint){
		String temp[] = toPoint.split(",");
		return new GridPoint2(Integer.parseInt(temp[0]),Integer.parseInt(temp[1]));
	}
	
	private LevelGoal compileLevelGoal(){//will parse through xml_file and get the win/loss conditions
		LevelGoal levelGoal = new LevelGoal();
		//that code tho
		return levelGoal;
	}
}