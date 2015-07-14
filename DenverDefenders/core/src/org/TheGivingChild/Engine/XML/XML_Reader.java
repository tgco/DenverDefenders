package org.TheGivingChild.Engine.XML;

import org.TheGivingChild.Engine.Attributes.Attribute;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ObjectMap;
import com.badlogic.gdx.utils.XmlReader;
import com.badlogic.gdx.utils.XmlReader.Element;
/**
 * Used to read in an .xml file and compile a Level object from it<br>
 * Call setupNewFile to setup the given level, and compileLevel to get the actual Level object
 * @author Kevin D, Walter Schlosser
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
		} catch(Exception e) { System.out.println("Error opening xml file. Filename: " + XML_Filename + "Exception: " + e); }
		root = reader.parse(xml_file);
	}
	/**
	 * Compiles and returns the new Level object
	 * @return	The Level object from the .xml file
	 * @see		compileLevel
	 */
	public Level compileLevel() {
		return new Level(root.getAttribute("name"),
				root.getAttribute("background"),
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
	public Array<GameObject> compileGameObjects(){
		Array<GameObject> listOfObjects = new Array<GameObject>();
		// Iterate over object definitions
		for(Element objectElement : root.getChildrenByName("object")){
			// Store arguments such as id="1"
			ObjectMap<String, String> objectArgs = objectElement.getAttributes();
			// Create continuous attributes (updated every frame)
			Array<Attribute> continuousAttributes = new Array<Attribute>();
			if (objectElement.getChildByName("continuous") != null) {
				for (Element att : objectElement.getChildByName("continuous").getChildrenByName("att")) {
					String type = att.getAttribute("type");
					continuousAttributes.add( AttributeEnum.valueOf(type.toUpperCase()).construct(att.getAttributes()) );
				}
			}
			// Create triggered attributes (notified to run on condition)
			Array<Attribute> triggeredAttributes = new Array<Attribute>();
			if (objectElement.getChildByName("triggered") != null) {
				for (Element att : objectElement.getChildByName("triggered").getChildrenByName("att")) {
					String type = att.getAttribute("type");
					triggeredAttributes.add( AttributeEnum.valueOf(type.toUpperCase()).construct(att.getAttributes()) );
				}
			}
			// Construct object
			GameObject ob = new GameObject(objectArgs, continuousAttributes, triggeredAttributes);
			// Construct input listeners
			for (Element input : objectElement.getChildrenByName("input")) {
				String type = input.getAttribute("type");
				InputListener listen = InputListenerEnums.valueOf(type.toUpperCase()).construct(ob, input.getAttributes());
				ob.addListener(listen);
			}
			listOfObjects.add(ob);
		}

		return listOfObjects;
	}
	
	/**	
	*	Compiles the win conditions into a map of condition to true/false
	*/
	public ObjectMap<String, Boolean> compileWinConditions() {
		ObjectMap<String, Boolean> winConditions = new ObjectMap<String, Boolean>();
		// Iterate over and add conditions, initialized to false
		for (Element cond : root.getChildByName("win").getChildrenByName("cond")) {
			winConditions.put(cond.getAttribute("val"), false);
		}

		return winConditions;
	}
	
	/**	
	*	Compiles the lose conditions into a map containing the list of lose conditions for the level and the data for each lose condition
	*/	
	public ObjectMap<String, Boolean> compileLoseConditions(){
		ObjectMap<String, Boolean> loseConditions = new ObjectMap<String, Boolean>();
		// Iterate over and add conditions, initialized to false
		for (Element cond : root.getChildByName("lose").getChildrenByName("cond")) {
			loseConditions.put(cond.getAttribute("val"), false);
		}

		return loseConditions;
	}
}
