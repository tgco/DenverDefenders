package org.TheGivingChild.Engine.XML;

import java.io.FileWriter;
import java.io.StringWriter;

import org.TheGivingChild.Engine.Attributes.WinEnum;

import com.badlogic.gdx.utils.XmlWriter;
import com.badlogic.gdx.utils.Array;

public class XML_Writer {
	private Level currentLevel;
	
	//main method for testing
	
	public static void main(String cheese[]){
		float temp1[] = {1,1};
		float temp2[] = {2,2};
		float temp3[] = {3,3};
		
		Array<String> test1 = new Array<String>();
		test1.add("100");
		Array<String> test2 = new Array<String>();
		test2.add("redred");
		Array<String> test3 = new Array<String>();
		test3.add("3.0,3.0;4.0,4.0;5.0,5.0;6.0,6.0");
		Array<String> test4 = new Array<String>();
		test4.add("10");
		
		
		GameObject testObj1 = new GameObject(1,"testObj1FILENAME",temp1);
		testObj1.addAttribute("health", test1);
		GameObject testObj2 = new GameObject(2,"testObj2FILENAME",temp2);
		GameObject testObj3 = new GameObject(3,"testObj3FILENAME",temp3);
		testObj3.addAttribute("movesOnSetPath", test3);
		testObj3.addAttribute("color", test2);
		GameObject testObj4 = new GameObject(4,"testObj4FILENAME",temp3);
		testObj4.addAttribute("disappearsOnPress", new Array<String>());
		GameObject testObj5 = new GameObject(5,"testObj5FILENAME",temp3);
		testObj5.addAttribute("fallsAtSetRate",test4);
		
		
		Array<GameObject> testObjectArray = new Array<GameObject>();
		testObjectArray.add(testObj1);
		testObjectArray.add(testObj2);
		testObjectArray.add(testObj3);
		testObjectArray.add(testObj4);
		testObjectArray.add(testObj5);
		
		WinEnum testWin1 = WinEnum.COLLISIONWITHOBJECTWIN;
		Array<String> testWin1Values = new Array<String>();
		testWin1Values.add("420");
		testWin1Values.add("9001");
		testWin1.setValues(testWin1Values);
		Array<WinEnum> testWinArray = new Array<WinEnum>();
		testWinArray.add(testWin1);
		
		LoseEnum testLose1 = LoseEnum.TIMEOUT;
		Array<String>testLose1Values =  new Array<String>();
		testLose1Values.add("42");
		testLose1.setValues(testLose1Values);
		Array<LoseEnum> testLoseArray = new Array<LoseEnum>();
		testLoseArray.add(testLose1);
		
		LoseEnum testLose2 = LoseEnum.COLLISIONWITHOBJECTLOSE;
		Array<String>testLose2Values =  new Array<String>();
		testLose2Values.add("42");
		testLose2Values.add("72");
		testLose2.setValues(testLose2Values);
		testLoseArray.add(testLose2);
		
		XML_Writer sally = new XML_Writer();
		//sally.setupNewFile("testOut.xml", "", "testLevel");
		sally.createLevel(new Level("testOut","PLACEHOLDER1","PLACEHOLDER2", testWinArray, testLoseArray, testObjectArray));
	}
	
	public void createLevel(Level newLevel){//this is who you gonna call
		currentLevel = newLevel;//shallow copy, but das ok
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
			for(GameObject currentGameObject:currentLevel.getGameObjects()){//writing game object information
				writer.element("GameObject");//writing game object and it's required attributes
				writer.attribute("ID",currentGameObject.getID());
				writer.attribute("attributes",compileAttributeList(currentGameObject));//writing list of attributes
				writer.attribute("imageFilename", currentGameObject.getImageFilename());
				writer.attribute("initialLocation", currentGameObject.getX() + "," + currentGameObject.getY());//position
				for(Attribute currentAttribute:currentGameObject.getAttributes()){//for each attribute, make an element of it and get its values
					writer.element(currentAttribute.getXMLName());
					int count = 1;
					for(String currentValue:currentAttribute.getValues()){//writing the values associated with each attribute
						writer.attribute("value" + count, currentValue);
						count++;
					}
					writer.pop();
				}
				writer.pop();
			}
			
			//write levelGoal information, win/lose conditions
				writer.element("levelGoals");
				for(WinEnum currentWinCondition:currentLevel.getWinConditions()){
					writer.attribute("win", currentWinCondition.getXMLDescription());//these two loops write the lists of condition types for the <levelGoal/> sections
				}
				for(LoseEnum currentLoseCondition:currentLevel.getLoseConditions()){
					writer.attribute("lose", currentLoseCondition.getXMLDescription());
				}
				
				int count=1;//writing win condition values
				for(WinEnum currentWinCondition:currentLevel.getWinConditions()){
					writer.element(currentWinCondition.getXMLDescription());
					for(String currentValue:currentWinCondition.getValues()){
						writer.attribute("win"+count,currentValue);
						count++;
					}
					writer.pop();
				}
				
				count=1;//writing lose condition values
				for(LoseEnum currentLoseCondition:currentLevel.getLoseConditions()){
					writer.element(currentLoseCondition.getXMLDescription());
					for(String currentValue:currentLoseCondition.getValues()){
						writer.attribute("lose"+count,currentValue);
						count++;
					}
					writer.pop();
				}
				writer.pop();
			writer.pop();
			
			writer.close();
			//write to file
			FileWriter fileWriter = new FileWriter(currentLevel.getLevelName() + ".xml");
			fileWriter.write(stringWriter.toString());
			fileWriter.close();
		}catch(Exception e){System.out.println("Error writing to file: " + e);}
	}
	
	private String compileAttributeList(GameObject obj){		
		String temp = "";
		for(Attribute currentAttribute: obj.getAttributes())
			temp+=currentAttribute.getXMLName() + ",";
		//remove last character
		String temp2="";
		if(temp.length()>1)
			temp2 = temp.substring(0,temp.length()-1);
		return temp2;
	}
	
	private String compileWinList(){
		String winList = "";
		
		return winList;
	}
	
	private String compileLoseList(){
		String loseList = "";
		
		return loseList;
	}
}
