package org.TheGivingChild.Editor;

import java.io.FileWriter;
import java.io.IOException;
import java.io.StringWriter;

import org.TheGivingChild.Engine.*;

import com.badlogic.gdx.math.GridPoint2;
import com.badlogic.gdx.utils.XmlWriter;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.XmlWriter;

public class XML_Writer {
	/* QUICK NOTES ON THE XML WRITER
	 * 1: Setup a StringWriter(called xml in this case)
	 * 2: Setup a XmlWriter and pass the constructor the StringWriter
	 * 3: xml_writer.element adds a new element
	 * 4: xml_writer.attribute("attribute name","attribute value") adds an attribute
	 * 5: xml_writer.pop() goes up a level in the tree
	 * EX: 	xml.element("ELEMENT_JUAN");
	 *		xml.attribute("ATTRIBUTE","VALUE");
	 *		xml.element("ELEMENT_TWO");
	 *		xml.attribute("ATTR","VAL");
	 *		xml.pop();
	 *		xml.pop();
	 *		
	 *		returns a STRING with the value of:
	 *		<ELEMENT_JUAN ATTRIBUTE="VALUE">
	 *			<ELEMENT_TWO ATTR="VAL"/>
	 *		</ELEMENT_JUAN>
	 *	6: then just write the string to the file with a FileWriter
	 */
	private String filename = "";
	private String packageName = "";
	private String levelName = "";
	
	//main method for testing
	public static void main(String cheese[]){
		GameObject testObj1 = new GameObject(1,"testObj1FILENAME",new GridPoint2(1,1));
		testObj1.addValidAttribute("health", "100");
		//GameObject testObj2 = new GameObject(2,"testObj2FILENAME",new GridPoint2(2,2));
		GameObject testObj3 = new GameObject(3,"testObj3FILENAME",new GridPoint2(3,3));
		testObj3.addValidAttribute("health", "9001");
		testObj3.addValidAttribute("health", "9002");
		testObj3.addValidAttribute("health", "o0o0o0o0o");
		testObj3.addValidAttribute("color","fuchesa");
		testObj3.addValidAttribute("color","asehcuf");
		
		Array<GameObject> testObjectArray = new Array<GameObject>();
		testObjectArray.add(testObj1);
		//testObjectArray.add(testObj2);
		testObjectArray.add(testObj3);
		
		XML_Writer sally = new XML_Writer();
		sally.setupNewFile("testOut.xml", "", "testLevel");
		sally.writeToFile(testObjectArray,new Level("PLACEHOLDER","PLACEHOLDER","PLACEHOLDER",new LevelGoal(),new Array<GameObject>()));
	}
	
	public void setupNewFile(String newfilename,String packagename,String newLevelName){
		filename = newfilename;
		packageName = packagename;
		levelName = newLevelName;
	}
	
	public void writeToFile(Array<GameObject> gameObjects, Level level){//sets up .xml file, calls the 2 compile methods, and adds their outputs together
		StringWriter stringWriter = new StringWriter();
		XmlWriter writer = new XmlWriter(stringWriter);
		String XML_String="";
		String Level_String="";
		
		try{//compile xml string
			writer.element("level");
			writer.attribute("packageName",packageName);
			writer.attribute("levelName", levelName);
			for(GameObject currentGameObject:gameObjects){
				writer.element("GameObject");
				writer.attribute("ID",currentGameObject.getID());
				writer.attribute("attributes",compileAttributeList(currentGameObject));
				writer.attribute("imageFilename", currentGameObject.getImageFilename());
				writer.attribute("initialLocation", currentGameObject.getLocation().x + "," + currentGameObject.getLocation().y);
				for(String currentAttribute:currentGameObject.getValidAttributes()){//for each attribute, make an element of it and get its values
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
			writer.pop();
			
			
			XML_String = stringWriter.toString();
			//write to file
			FileWriter fileWriter = new FileWriter(filename);
			fileWriter.write(XML_String);
			fileWriter.close();
		}catch(Exception e){System.out.println("Error writing to file: " + e);}
	}
	/*private String compileGameObjectsToString(Array<GameObject> gameObjects){
		StringWriter stringWriter = new StringWriter();
		XmlWriter writer = new XmlWriter(stringWriter);
		//String XML_String="";
		try{
			for(GameObject currentGameObject:gameObjects){
				writer.element("GameObject");
				writer.attribute("ID",currentGameObject.getID());
				writer.attribute("attributes",compileAttributeList(currentGameObject));
					
					
					writer.pop();
				writer.pop();
			}
			writer.close();
		}catch(Exception e){System.out.println("error: " + e);}
		return stringWriter.toString();
	}*/
	
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
	
	private String compileLevelToString(Level level){
		String levelString = "";
		return levelString;
	}
	
	public void XML_test(){
		
	}
}
