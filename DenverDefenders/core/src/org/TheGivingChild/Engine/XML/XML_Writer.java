package org.TheGivingChild.Engine.XML;

import java.io.StringWriter;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.XmlWriter;

// BROKEN, SEE LINE 43

public class XML_Writer {
	private Level currentLevel;
	/**
	*	sets up the writer to write the new level to the file
	*/
	public void createLevel(Level newLevel){//this is who you gonna call
		currentLevel = newLevel;//shallow copy, but das ok
		writeToFile();
	}
	/**
	*	writes the level to an .xml file
	*/
	public void writeToFile(){//writes whole level to an xml file
		StringWriter stringWriter = new StringWriter();
		XmlWriter writer = new XmlWriter(stringWriter);
		
		try{//compile xml string
			writer.element("level");
			writer.attribute("packageName",currentLevel.getPackageName());//might wanna make this dynamic.
			writer.attribute("levelName", currentLevel.getLevelName());
			writer.attribute("levelImage",currentLevel.getLevelImage());
			writer.attribute("description",currentLevel.getDescription());
			for(GameObject currentGameObject : currentLevel.getGameObjects()){//writing game object information
				writer.element("GameObject");//writing game object and it's required attributes
				writer.attribute("ID",currentGameObject.getID());
				writer.attribute("attributes",compileAttributeList(currentGameObject));//writing list of attributes
				writer.attribute("imageFilename", currentGameObject.getImageFilename());
				writer.attribute("initialLocation", currentGameObject.getX() + "," + currentGameObject.getY());//position
				writer.attribute("listeners", compileListenerList(currentGameObject));
				for(AttributeEnum currentAttribute : currentGameObject.getActiveEnumList()){//for each attribute, make an element of it and get its values
					writer.element(currentAttribute.getXMLName());
					int count = 1;
					// Broken since constructs a new instance
					for(String currentValue : currentAttribute.construct().getAttributeData()){//writing the values associated with each attribute
						writer.attribute("value" + count, currentValue);
						count++;
					}
					writer.pop();
				}
				
				writer.pop();
			}
			
			//write levelGoal information, win/lose conditions
			writer.element("levelGoals");
			writer.attribute("win", compileWinList());
			writer.attribute("lose", compileLoseList());
			
			int count = 1;//writing win condition values
			for(WinEnum currentWinCondition : currentLevel.getWinConditions()){
				writer.element(currentWinCondition.getXMLDescription());
				for(String currentValue : currentWinCondition.getValues(currentLevel)){
					writer.attribute("value"+count,currentValue);
					count++;
				}
				writer.pop();
			}
			
			count = 1;//writing lose condition values
			for(LoseEnum currentLoseCondition:currentLevel.getLoseConditions()){
				writer.element(currentLoseCondition.getXMLDescription());
				for(String currentValue:currentLoseCondition.getValues(currentLevel)){
					writer.attribute("value"+count,currentValue);
					count++;
				}
				writer.pop();
			}
			writer.pop(); // levelgoals
			writer.pop(); // level
			
			writer.close();
			//write to file
			FileHandle fileWriter = Gdx.files.local("../android/assets/Levels/created/" + currentLevel.getLevelName() + ".xml");
			fileWriter.writeString(stringWriter.toString(),false);
		} catch(Exception e) { System.out.println("Error writing to file: " + e); }
	}
	/**
	*	Takes a game object and returns a string delimited by commas to write to the file which lists the object's attributes
	*/
	private String compileAttributeList(GameObject obj){		
		String temp = "";
		for(AttributeEnum currentAttribute: obj.getActiveEnumList())
			temp += currentAttribute.getXMLName() + ",";
		//remove last character
		String temp2="";
		if(temp.length() > 1)
			temp2 = temp.substring(0,temp.length()-1);
		return temp2;
	}
	/**
	*	Compiles the list of win enum names delimited by commas
	*/
	private String compileWinList() {
		String winList1 = "";
		String winList2 = "";
		if(currentLevel.getWinConditions().size > 0) {
			for(WinEnum currentWinCondition:currentLevel.getWinConditions()){
				winList1 += "," + currentWinCondition.getXMLDescription();
			}
			winList2 = winList1.substring(1,winList1.length());
		}
		return winList2;
	}
	/**
	*	Compiles the list of lose enum names delimited by commas
	*/
	private String compileLoseList() {
		String loseList1 = "";
		String loseList2 = "";
		if(currentLevel.getLoseConditions().size > 0){
			for(LoseEnum currentLoseCondition:currentLevel.getLoseConditions()){
				loseList1+="," + currentLoseCondition.getXMLDescription();
			}
			loseList2 = loseList1.substring(1,loseList1.length());
		}
		return loseList2;
	}
	
	/**
	*	compiles the list of listener names into a list delimited by commas
	*/
	private String compileListenerList(GameObject myObject){
		if(myObject.getListenerNames().size == 0)
			return "";
		String temp = "";
		for(String currentListenerName : myObject.getListenerNames())
			temp += ","+currentListenerName;
		return temp.substring(1,temp.length());
	}
}
