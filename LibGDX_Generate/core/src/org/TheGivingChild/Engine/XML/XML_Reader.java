package org.TheGivingChild.Engine.XML;

import java.io.BufferedReader;
import java.io.FileReader;

import com.badlogic.gdx.scenes.scene2d.utils.*;
import com.badlogic.gdx.utils.XmlReader;
import com.badlogic.gdx.utils.XmlReader.Element;
import com.badlogic.gdx.utils.Array;

//Use this to read the XML File into a Level
//will read in XML fle, translate into LevelGoals, GameObjects, and other data, compile them into a level, then pass that level up 
public class XML_Reader {
	
	//the main method is for testing only
	
	
	
	public static void main(String cheese[]){
		XML_Reader test = new XML_Reader();
		String filename = "testOut.xml";
		test.setupNewFile(filename);
		Level larry = test.compileLevel();
		
		for(GameObject currentObject:larry.getGameObjects()){
			System.out.println("|" + currentObject);
			for(Attribute current:currentObject.getAttributes()){
				System.out.println("\t|" + current.getXMLName());
				for(String currentValue:current.getValues()){
					System.out.println("\t\t|" + currentValue);
				}
			}
		}
	}
	
	private XmlReader reader = new XmlReader();
	private Element root;//this is the root of the tree that is created by reader.parse(xml_file)
	//THIS IS THE METHOD YOU CALL TO READ IN A WHOLE LEVEL
	public Level compileLevel(){		
		return new Level(root.getAttribute("levelName"),root.getAttribute("packageName"),root.getAttribute("levelImage"),compileLevelGoal(),compileGameObjects());
	}
	
	public Array<GameObject> compileGameObjects(){//will parse through xml_file and get all game objects and their attributes
		Array<GameObject> listOfObjects = new Array<GameObject>();
		for(Element currentObject:root.getChildrenByName("GameObject")){//iterate through game objects
			GameObject temp = new GameObject(currentObject.getIntAttribute("ID"),currentObject.getAttribute("imageFilename"),stringToPoint(currentObject.getAttribute("initialLocation")));//hardcoded values which must always be written down in the .xml file
			//System.out.println(temp.getID());
				for(String currentAttribute:currentObject.getAttribute("attributes").split(",")){//iterate through each GameObject's attributes
					//System.out.println("\t|" + currentAttribute);
					if(!currentObject.getAttribute("attributes").isEmpty()){//look up the object of name currentAttribute and add it to currentObject's list of Attributes
						Array<String> valuesToAdd = new Array<String>();
						for(int i = 0; i< currentObject.getChildByName(currentAttribute).getAttributes().size;i++)
							valuesToAdd.add(currentObject.getChildByName(currentAttribute).getAttribute("value" + (i+1)));
							//System.out.println("\t\tValue: " + currentObject.getChildByName(currentAttribute).getAttribute("value" + (i+1)));
						temp.addAttribute(currentAttribute, valuesToAdd);
					}
				}
			listOfObjects.add(temp);
		}
		return listOfObjects;
	}
	
	public void setupNewFile(String XML_Filename){//will read in a new XML file as a big string, will try to leave space for the DHD, needs to be called each time you want to read in a minigame
		String xml_file ="";
		try{
			BufferedReader fileReader = new BufferedReader(new FileReader(XML_Filename));
			while(fileReader.ready()) xml_file+=fileReader.readLine();//might need to clean up the xml here
			fileReader.close();
		}catch(Exception e){System.out.println("Error opening xml file. Filename: " + XML_Filename + "Exception: " + e);}
		root = reader.parse(xml_file);
	}
	
	private static float[] stringToPoint(String toPoint){
		float temp[] = {Float.parseFloat(toPoint.substring(0, toPoint.indexOf(","))),Float.parseFloat(toPoint.substring(toPoint.indexOf(",")+1,toPoint.length()-1))};
		return temp;
	}
	
	private LevelGoal compileLevelGoal(){//will parse through xml_file and get the win/loss conditions
		LevelGoal levelGoal = new LevelGoal();
		//that code tho
		return levelGoal;
	}
}
