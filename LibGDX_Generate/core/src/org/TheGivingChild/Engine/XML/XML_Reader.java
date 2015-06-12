package org.TheGivingChild.Engine.XML;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.ObjectMap;
import com.badlogic.gdx.utils.XmlReader;
import com.badlogic.gdx.utils.XmlReader.Element;
import com.badlogic.gdx.utils.Array;
/**
 * Used to read in an .xml file and compile a Level object from it<br>
 * Call setupNewFile to setup the given level, and compileLevel to get the actual Level object
 * @author Kevin D
 */
public class XML_Reader {
	/** The libGDX XmlReader used to parse the .xml file*/
	private XmlReader reader = new XmlReader();
	private Element root;//this is the root of the tree that is created by reader.parse(xml_file)
	
	/**
	 * Receives a FileHandle, reads in the new file, and parses it into a tree to later be compiled into a Level
	 * @param The FileHandle containing the information to read from the .xml file
	 * @see setupNewFile
	 */
	public void setupNewFile(FileHandle file){//will read in a new XML file as a big string, will try to leave space for the DHD, needs to be called each time you want to read in a minigame
		String XML_Filename = new String();
		XML_Filename = file.name();
		String xml_file ="";
		try{
			xml_file = file.readString();
		}catch(Exception e){System.out.println("Error opening xml file. Filename: " + XML_Filename + "Exception: " + e);}
		root = reader.parse(xml_file);
	}
	/**
	 * Compiles and returns the new Level object
	 * @return	The Level object from the .xml file
	 * @see		compileLevel
	 */
	public Level compileLevel(){
		return new Level(root.getAttribute("levelName"),
				root.getAttribute("packageName"),
				root.getAttribute("levelImage"),
				root.getAttribute("description"),
				compileWinConditions(),
				compileLoseConditions(),
				compileGameObjects());
	}
	/**
	 * Compiles a libGDX Array object of GameObjects with their given attributes and returns it
	 * @return	A libGDX Array object of GameObjects
	 * @see compileGameObjects
	 */
	public Array<GameObject> compileGameObjects(){//will parse through xml_file and get all game objects and their attributes
		Array<GameObject> listOfObjects = new Array<GameObject>();
		for(Element currentObject:root.getChildrenByName("GameObject")){//iterate through game objects
			Array<String> listenersToAdd = compileListenerNames(currentObject.getAttribute("listeners"));
			ObjectMap<Attribute,Array<String>> attributeData = new ObjectMap<Attribute,Array<String>>();
			//System.out.println(listenersToAdd);
				for(String currentAttribute:currentObject.getAttribute("attributes").split(",")){//iterate through each GameObject's attributes
					//System.out.println("\t|" + currentAttribute);
					if(!currentObject.getAttribute("attributes").isEmpty()){//look up the object of name currentAttribute and add it to currentObject's list of Attributes
						Array<String> valuesToAdd = new Array<String>();
						if(currentObject.getChildByName(currentAttribute).getAttributes() != null){//check if the attribute even has values
							for(int i = 0; i< currentObject.getChildByName(currentAttribute).getAttributes().size;i++){
								valuesToAdd.add(currentObject.getChildByName(currentAttribute).getAttribute("value" + (i+1)));
								attributeData.put(Attribute.newType(currentAttribute), valuesToAdd);
								//System.out.println("\t\tValue: " + currentObject.getChildByName(currentAttribute).getAttribute("value" + (i+1)));
							}
						}
						attributeData.put(Attribute.newType(currentAttribute), valuesToAdd);
					}
				}
				GameObject temp = new GameObject(currentObject.getIntAttribute("ID"),currentObject.getAttribute("imageFilename"),stringToPoint(currentObject.getAttribute("initialLocation")), listenersToAdd, attributeData);//hardcoded values which must always be written down in the .xml file
			listOfObjects.add(temp);
		}
		return listOfObjects;
	}
	
	public ObjectMap<WinEnum, Array<String>> compileWinConditions(){
		ObjectMap<WinEnum,Array<String>> winData = new ObjectMap<WinEnum,Array<String>>();
		String temp[] = root.getChildByName("LevelGoals").getAttribute("win").split(",");
		if(temp.length > 0){//in case of empty list, for whatever reason
			for(String currentWinCondition:temp){//each element in win="stuff,things,morestuff"
				WinEnum tempEnum = WinEnum.newType(currentWinCondition);//yo dawg i herd u liek temps
				ObjectMap<String,String> tempMap = root.getChildByName("LevelGoals").getChildByName(currentWinCondition).getAttributes();//saves lookup time if there are multiple conditions
				Array<String> tempValues = new Array<String>();
				if(root.getChildByName("LevelGoals").getChildByName(currentWinCondition).getAttributes() != null){
					for(int i=0;i<root.getChildByName("LevelGoals").getChildByName(currentWinCondition).getAttributes().size;i++){
						tempValues.add(tempMap.get("value"+(i+1)));
					}
				}
				winData.put(tempEnum,tempValues);
			}
		}
		return winData;
	}
	
	public ObjectMap<LoseEnum, Array<String>> compileLoseConditions(){
		ObjectMap<LoseEnum,Array<String>> loseData = new ObjectMap<LoseEnum,Array<String>>();
		String temp[] = root.getChildByName("LevelGoals").getAttribute("lose").split(",");
		if(temp.length > 0){//in case of empty list, for whatever reason
			for(String currentLoseCondition:temp){//each element in lose="stuff,things,morestuff"
				LoseEnum tempEnum = LoseEnum.newType(currentLoseCondition);//yo dawg i herd u liek temps
				ObjectMap<String,String> tempMap = root.getChildByName("LevelGoals").getChildByName(currentLoseCondition).getAttributes();//saves lookup time if there are multiple conditions
				Array<String> tempValues = new Array<String>();
				if(root.getChildByName("LevelGoals").getChildByName(currentLoseCondition).getAttributes() != null){
					for(int i=0;i<root.getChildByName("LevelGoals").getChildByName(currentLoseCondition).getAttributes().size;i++){
						tempValues.add(tempMap.get("value"+(i+1)));
					}
				}
				loseData.put(tempEnum,tempValues);
			}
		}
		return loseData;
	}
	
	private static float[] stringToPoint(String toPoint){//takes in a string of form "1.0,1.0" and returns a 2 element array of floats
		float temp[] = {Float.parseFloat(toPoint.substring(0, toPoint.indexOf(","))),Float.parseFloat(toPoint.substring(toPoint.indexOf(",")+1,toPoint.length()-1))};
		temp[0] = temp[0]/1024 * Gdx.graphics.getWidth();
		temp[1] = temp[1]/576 * Gdx.graphics.getWidth();
		
		return temp;
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
