package org.TheGivingChild.Engine.XML;

import java.io.StringWriter;

import org.TheGivingChild.Engine.Attributes.WinEnum;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.XmlWriter;

public class XML_Writer {
	private Level currentLevel;
	
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
				writer.attribute("listeners", compileListenerList(currentGameObject));
				for(Attribute currentAttribute:currentGameObject.getAttributes()){//for each attribute, make an element of it and get its values
					writer.element(currentAttribute.getXMLName());
					int count = 1;
					for(String currentValue:currentAttribute.getValues(currentGameObject)){//writing the values associated with each attribute
						System.out.println("here here " + currentValue);
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
			//FileHandle fileWriter = new FileHandle(currentLevel.getLevelName() + ".xml");
			System.out.println(currentLevel.getLevelName());
			FileHandle fileWriter = Gdx.files.local("../android/assets/Levels/created/" +currentLevel.getLevelName() + ".xml");
			//FileHandle fileWriter = new FileHandle("testOutTEST.xml");
			fileWriter.writeString(stringWriter.toString(),false);
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
		String winList1 = "";
		String winList2 = "";
		if(currentLevel.getWinConditions().size > 0){
			for(WinEnum currentWinCondition:currentLevel.getWinConditions()){
				winList1+="," + currentWinCondition.getXMLDescription();
			}
			winList2 = winList1.substring(1,winList1.length());
		}
		return winList2;
	}
	
	private String compileLoseList(){
		String loseList1 = "";
		String loseList2 = "";
		if(currentLevel.getLoseConditions().size > 0){
			for(LoseEnum currentLoseCondition:currentLevel.getLoseConditions()){
				loseList1+="," + currentLoseCondition.getXMLDescription();
			}
			loseList2 = loseList1.substring(1,loseList1.length());
		}
		return loseList2;
	}//GITHUB PUSH MY CHANGES YOU 
	
	private String compileListenerList(GameObject myObject){
		if(myObject.getListenerNames().size == 0)
			return "";
		String temp = "";
		for(String currentListenerName:myObject.getListenerNames())
			temp+=","+currentListenerName;
		return temp.substring(1,temp.length());
	}
}
