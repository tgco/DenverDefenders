package org.TheGivingChild.Engine.XML;

import java.io.BufferedReader;
import java.io.FileReader;

import org.TheGivingChild.Engine.Attributes.WinEnum;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.scenes.scene2d.utils.*;
import com.badlogic.gdx.utils.ObjectMap;
import com.badlogic.gdx.utils.XmlReader;
import com.badlogic.gdx.utils.XmlReader.Element;
import com.badlogic.gdx.utils.Array;

public class XML_Reader {
	
	//the main method is for testing only
//	public static void main(String cheese[]){
//		XML_Reader test = new XML_Reader();
//		FileHandle filename = new FileHandle("ball.png");
//		test.setupNewFile(filename);
//		Level larry = test.compileLevel();
//		
//		/*for(GameObject currentObject:larry.getGameObjects()){
//			System.out.println("|" + currentObject);
//			for(Attribute current:currentObject.getAttributes()){
//				System.out.println("\t|" + current.getXMLName());
//				for(String currentValue:current.getValues()){
//					System.out.println("\t\t|" + currentValue);
//				}
//			}
//		}*/
//		larry.update();
//	}
	
	private XmlReader reader = new XmlReader();
	private Element root;//this is the root of the tree that is created by reader.parse(xml_file)
	
	//THIS IS THE METHOD YOU CALL TO READ IN A WHOLE LEVEL
	public void setupNewFile(FileHandle file){//will read in a new XML file as a big string, will try to leave space for the DHD, needs to be called each time you want to read in a minigame
		String XML_Filename = new String();
		XML_Filename = file.name();
		String xml_file ="";
		try{
			xml_file = file.readString();
		}catch(Exception e){System.out.println("Error opening xml file. Filename: " + XML_Filename + "Exception: " + e);}
		root = reader.parse(xml_file);
	}
	
	public Level compileLevel(){		
		return new Level(root.getAttribute("levelName"),
				root.getAttribute("packageName"),
				root.getAttribute("levelImage"),
				compileWinConditions(),
				compileLoseConditions(),
				compileGameObjects());
	}
	
	public Array<GameObject> compileGameObjects(){//will parse through xml_file and get all game objects and their attributes
		Array<GameObject> listOfObjects = new Array<GameObject>();
		for(Element currentObject:root.getChildrenByName("GameObject")){//iterate through game objects
			Array<Attribute> attributesToAdd = new Array<Attribute>();
			Array<String> listenersToAdd = compileListenerNames(currentObject.getAttribute("listeners"));
			//System.out.println(temp.getID());
				for(String currentAttribute:currentObject.getAttribute("attributes").split(",")){//iterate through each GameObject's attributes
					//System.out.println("\t|" + currentAttribute);
					if(!currentObject.getAttribute("attributes").isEmpty()){//look up the object of name currentAttribute and add it to currentObject's list of Attributes
						Array<String> valuesToAdd = new Array<String>();
						if(currentObject.getChildByName(currentAttribute).getAttributes() != null){//check if the attribute even has values
							for(int i = 0; i< currentObject.getChildByName(currentAttribute).getAttributes().size;i++){
								valuesToAdd.add(currentObject.getChildByName(currentAttribute).getAttribute("value" + (i+1)));
								//System.out.println("\t\tValue: " + currentObject.getChildByName(currentAttribute).getAttribute("value" + (i+1)));
							}
						}
						Attribute attribute = Attribute.newType(currentAttribute);
						attribute.setValues(valuesToAdd);
						attributesToAdd.add(attribute);
					}
				}
				GameObject temp = new GameObject(currentObject.getIntAttribute("ID"),currentObject.getAttribute("imageFilename"),stringToPoint(currentObject.getAttribute("initialLocation")), attributesToAdd,listenersToAdd);//hardcoded values which must always be written down in the .xml file
			listOfObjects.add(temp);
		}
		return listOfObjects;
	}
	
	public Array<WinEnum> compileWinConditions(){
		Array<WinEnum> winEnums = new Array<WinEnum>();
		String temp[] = root.getChildByName("levelGoals").getAttribute("win").split(",");
		if(temp.length > 0){//in case of empty list, for whatever reason
			for(String currentWinCondition:temp){//each element in win="stuff,things,morestuff"
				WinEnum tempEnum = WinEnum.newType(currentWinCondition);//yo dawg i herd u liek temps
				ObjectMap<String,String> tempMap = root.getChildByName("levelGoals").getChildByName(currentWinCondition).getAttributes();//saves lookup time if there are multiple conditions
				Array<String> tempValues = new Array<String>();
				for(int i=0;i<root.getChildByName("levelGoals").getChildByName(currentWinCondition).getAttributes().size;i++){
					tempValues.add(tempMap.get("win"+(i+1)));
				}
				winEnums.add(tempEnum);
			}
		}
		return winEnums;
	}
	
	public Array<LoseEnum> compileLoseConditions(){
		Array<LoseEnum> loseEnums = new Array<LoseEnum>();
		String temp[] = root.getChildByName("levelGoals").getAttribute("lose").split(",");
		if(temp.length > 0){//in case of empty list, for whatever reason
			for(String currentLoseCondition:temp){//each element in lose="stuff,things,morestuff"
				LoseEnum tempEnum = LoseEnum.newType(currentLoseCondition);//yo dawg i herd u liek temps
				ObjectMap<String,String> tempMap = root.getChildByName("levelGoals").getChildByName(currentLoseCondition).getAttributes();//saves lookup time if there are multiple conditions
				Array<String> tempValues = new Array<String>();
				for(int i=0;i<root.getChildByName("levelGoals").getChildByName(currentLoseCondition).getAttributes().size;i++){
					tempValues.add(tempMap.get("lose"+(i+1)));
				}
				loseEnums.add(tempEnum);
			}
		}
		return loseEnums;
	}
	
	private static float[] stringToPoint(String toPoint){//takes in a string of form "1.0,1.0" and returns a 2 element array of floats
		float temp[] = {Float.parseFloat(toPoint.substring(0, toPoint.indexOf(","))),Float.parseFloat(toPoint.substring(toPoint.indexOf(",")+1,toPoint.length()-1))};
		return temp;
	}
	
	private LevelGoal compileLevelGoal(){//will parse through xml_file and get the win/loss conditions
		LevelGoal levelGoal = new LevelGoal();
		//that code tho
		return levelGoal;
	}
	
	private Array<String> compileListenerNames(String input){
		Array<String> listeners = new Array<String>();
		if(input.isEmpty())
			return listeners;
		String[] temp = input.split(",");
		for(String currentListenerName:temp)
			listeners.add(currentListenerName);
		return listeners;
	}
}
