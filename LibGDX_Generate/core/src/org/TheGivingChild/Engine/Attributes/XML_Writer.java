package org.TheGivingChild.Engine.Attributes;

import java.io.FileWriter;
import java.io.StringWriter;

import com.badlogic.gdx.utils.XmlWriter;
import com.badlogic.gdx.utils.Array;

public class XML_Writer {
	private Level currentLevel;
	
	//main method for testing
	
	public static void main(String cheese[]){
		float temp1[] = {1,1};
		float temp2[] = {2,2};
		float temp3[] = {3,3};
		
		
		GameObject testObj1 = new GameObject(1,"testObj1FILENAME",temp1);
		testObj1.addValidAttribute("health", "100");
		GameObject testObj2 = new GameObject(2,"testObj2FILENAME",temp2);
		GameObject testObj3 = new GameObject(3,"testObj3FILENAME",temp3);
		//testObj3.addValidAttribute("movesOnSetPath", "3,3");//start point
		testObj3.addValidAttribute("movesOnSetPath", "3,3;4,4;5,5;6,6");
		testObj3.addValidAttribute("movesOnSetPath", "100");//speed
		
		Array<GameObject> testObjectArray = new Array<GameObject>();
		testObjectArray.add(testObj1);
		testObjectArray.add(testObj2);
		testObjectArray.add(testObj3);
		
		XML_Writer sally = new XML_Writer();
		//sally.setupNewFile("testOut.xml", "", "testLevel");
		sally.createLevel(new Level("testOut","PLACEHOLDER","PLACEHOLDER",new LevelGoal(),testObjectArray));
	}
	
	public void createLevel(Level newLevel){//this is who you gonna call
		currentLevel = newLevel;
		writeToFile();
	}
	
	public void writeToFile(){//writes whole level to an xml file
		StringWriter stringWriter = new StringWriter();
		XmlWriter writer = new XmlWriter(stringWriter);
		
		try{//compile xml string
			writer.element("level");
			writer.attribute("packageName",currentLevel.getPackageName());//might wanna make this dynamic.
			writer.attribute("levelName", currentLevel.getLevelName());
			writer.attribute("levelImage",currentLevel.getLevelImage());
			for(GameObject currentGameObject:currentLevel.getGameObjects()){
				writer.element("GameObject");
				writer.attribute("ID",currentGameObject.getID());
				writer.attribute("attributes",compileAttributeList(currentGameObject));
				writer.attribute("imageFilename", currentGameObject.getImageFilename());
				writer.attribute("initialLocation", currentGameObject.getX() + "," + currentGameObject.getY());
				for(String currentAttribute:currentGameObject.getAttributes()){//for each attribute, make an element of it and get its values
					writer.element(currentAttribute);
					int count = 1;
					for(String currentValue:currentGameObject.getAttributeValues().get(currentAttribute)){
						writer.attribute("value" + count, currentValue);
						count++;
					}
					writer.pop();
				}
				writer.pop();
			}
			
			//write levelGoal
				writer.element("levelGoal");
				writer.element()
				writer.pop();
			writer.pop();
			
			writer.close();
			//String XML_String = stringWriter.toString();
			//write to file
			FileWriter fileWriter = new FileWriter(currentLevel.getLevelName() + ".xml");
			fileWriter.write(stringWriter.toString());
			fileWriter.close();
		}catch(Exception e){System.out.println("Error writing to file: " + e);}
	}
	
	private String compileAttributeList(GameObject obj){		
		String temp = "";
		for(String currentKey: obj.getAttributeValues().keys().toArray()){
			temp+=currentKey + ",";
		}
		//remove last character
		String temp2="";
		if(temp.length()>1)
			temp2 = temp.substring(0,temp.length()-1);
		return temp2;
	}
}
