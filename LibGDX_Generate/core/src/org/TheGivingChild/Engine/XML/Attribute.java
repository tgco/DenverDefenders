package org.TheGivingChild.Engine.XML;

import org.TheGivingChild.Engine.InputListenerEnums;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.utils.Array;

/**
 * Each GameObject holds a list of attributes, these attributes do various operations on the object based on their specific implementations of their methods
 * Each Attribute must implement update, setup, getVariableNames, getValues, and getXMLName
 * @author Kevin D
 */
public enum Attribute {
	/* each type will have a update method and a setValues method which all take in an Array<String>
	 * each type can have private fields
	 */	
	/**
	 * If the object will EVER move, must have moves attribute, it has two values, initial x velocity, and initial y velocity, upon setup, it sets its GameObject's velocity to them
	 */
	MOVES{//velocity is stored in GameObject, but moves actually simulates it moving and updates the location, no other attribute should change location unless you are doing so to make some other crazy stuffs happen
		public void update(GameObject myObject,Array<GameObject> allObjects){
			myObject.setPosition((myObject.getX() + Gdx.graphics.getDeltaTime()*myObject.getVelocity()[0]), (myObject.getY() + Gdx.graphics.getDeltaTime()*myObject.getVelocity()[1]));
		}
		
		public void setup(GameObject myObject){
			myObject.setVelocity(new float[] {Float.parseFloat(myObject.getAttributeData().get(MOVES).get(0)),Float.parseFloat(myObject.getAttributeData().get(MOVES).get(1))});
		}
		public Array<String> getValues(GameObject myObject){
			Array<String> temp = new Array<String>();
			temp.add(myObject.getAttributeData().get(MOVES).get(0));
			temp.add(myObject.getAttributeData().get(MOVES).get(1));
			return temp;
		}
		public Array<String> getVariableNames(){
			Array<String> variableNames = new Array<String>();
			variableNames.add("Initial X Velocity");
			variableNames.add("Initial Y Velocity");
			return variableNames;
		}
		
		public String getXMLName(){return "moves";}
	},
	/**
	 * Causes objects to collide with and bounce off of the edge of the screen, collisions are perfectly ellastic
	 */
	BOUNCEOFFEDGEOFSCREEN{
		public void update(GameObject myObject,Array<GameObject> allObjects){
			if(myObject.getX() <= 0){//left
				float[] temp = myObject.getVelocity();
				temp[0] = Math.abs(temp[0]);
				myObject.setVelocity(temp);
			}else if(myObject.getX() +myObject.getTexture().getWidth() >= Gdx.graphics.getWidth()){//right
				float[] temp = myObject.getVelocity();
				temp[0] = -Math.abs(temp[0]);
				myObject.setVelocity(temp);
			}else if(myObject.getY() <= 0){//bottom
				float[] temp = myObject.getVelocity();
				temp[1] = Math.abs(temp[1]);
				myObject.setVelocity(temp);
			}else if(myObject.getY() + myObject.getTexture().getHeight() >= Gdx.graphics.getHeight()){//top
				float[] temp = myObject.getVelocity();
				temp[1] = -Math.abs(temp[1]);
				myObject.setVelocity(temp);
			}
		}
		public Array<String> getVariableNames(){
			return new Array<String>();
		}
		public void setup(GameObject myObject){}//doesnt need to setup anything		
		public Array<String> getValues(GameObject myObject){
			return myObject.getAttributeData().get(BOUNCEOFFEDGEOFSCREEN);
		}
		public String getXMLName(){return "bounceOffEdgeOfScreen";}		
	},
	HEALTH{
		public void update(GameObject myObject,Array<GameObject> allObjects){
			//System.out.println("\nHealth Update\n" + health);
		}
		public void setup(GameObject myObject){
			
		}
		
		public Array<String> getVariableNames(){
			Array<String> temp = new Array<String>();
			temp.add("Initial Health");
			return temp;
		}
		public String getXMLName(){return "health";}
		@Override
		public Array<String> getValues(GameObject myObject) {
			return myObject.getAttributeData().get(HEALTH);
		}
	},
	SPINS{
		private float rate;
		public void update(GameObject myObject){
			
		}
		public void setValues(Array<String> newValues){
			rate = Float.parseFloat(newValues.get(0));
		}

		public Array<String> getValues(GameObject myObject){//each implementation of get Values translates the values back into the string for writing to .xml purposes
			return myObject.getAttributeData().get(SPINS);
		}
		public String getXMLName(){return "spins";}
		@Override
		public Array<String> getVariableNames() {
			Array<String> variableNames = new Array<String>();
			variableNames.add("Spin Rate");
			return variableNames;
		}
		@Override
		public void setup(GameObject myObject) {
			// TODO Auto-generated method stub
			
		}
		@Override
		public void update(GameObject myObject, Array<GameObject> allObjects) {
			// TODO Auto-generated method stub
			
		}
	},
	/**
	 * An object with this attribute will collide with the objects which it was told to
	 * All objects with this attribute AS WELL AS objects it will be colliding with, must have the MASS Attribute
	 * Do not give this attribute to both objects which will be colliding, for instance, if you want object1 to collide with object2, only give this attribute to object1, and pass it object2's ID
	 * currently buggy, not working as intended.
	 */
	COLLIDESWITHOBJECTSID{//only gonna get square objects working for now, circular objects wont be too hard later
		//data is stored as value1=mass of object, all other values are the objects it can collide with
		public void update(GameObject myObject,Array<GameObject> allObjects){
			Rectangle juan = new Rectangle(myObject.getX(),myObject.getY(),myObject.getWidth(),myObject.getHeight());
			for(int i =0; i < allObjects.size;i++){
				if(myObject.getID() != allObjects.get(i).getID() && myObject.getAttributeData().get(COLLIDESWITHOBJECTSID).contains(allObjects.get(i).getID()+"", false)){//if myObject collides with current object AND they are actually colliding
					Rectangle two = new Rectangle(allObjects.get(i).getX(),allObjects.get(i).getY(),allObjects.get(i).getWidth(),allObjects.get(i).getHeight());
					if(juan.overlaps(two)){
						//System.out.println("COLLISION DETECETED: " + myObject.getID() + ", " + allObjects.get(i).getID());
						float c1 = Float.parseFloat(myObject.getAttributeData().get(COLLIDESWITHOBJECTSID).get(0));
						//float c2 = allObjects.get(i).getAttributeData();
						float m1 = Float.parseFloat(myObject.getAttributeData().get(MASS).get(0));
						float m2 = Float.parseFloat(allObjects.get(i).getAttributeData().get(MASS).get(0));
						float v1ix = myObject.getVelocity()[0];
						float v2ix = allObjects.get(i).getVelocity()[0];
						float v1iy = myObject.getVelocity()[1];
						float v2iy = allObjects.get(i).getVelocity()[1];
						
						myObject.setVelocity(new float[] {c1*((m1-m2)*v1ix + 2*m2*v2ix)/(m1+m2),c1*((m1-m2)*v1iy + 2*m2*v2iy)/(m1+m2)});
						allObjects.get(i).setVelocity(new float[] {c1*((2*m1*v1ix+(m1-m2)*v2ix)/(m1+m2)),c1*(2*m1*v1iy+(m1-m2)*v2iy/(m1+m2))});
						//int[] direction = direction(myObject.getX(),myObject.getY(),allObjects.get(i).getX(),allObjects.get(i).getY());
						//System.out.println(direction[0] + ", " + direction[1]);
					}
				}
			}
		}
		
		private int[] direction(float x1, float y1, float x2, float y2){
			int x = 1;
			int y = 1;
			if(x2-x1 < 0)
				x=-1;
			if(y2-y1 < 0)
				y=-1;				
			return new int[] {x,y};
		}
		
		public void setup(GameObject myObject){
			String[] newValues = myObject.getAttributeData().get(COLLIDESWITHOBJECTSID).get(1).split(",");
			myObject.getAttributeData().get(COLLIDESWITHOBJECTSID).set(1, newValues[0]);
			for(int i = 1; i < newValues.length;i++)
				myObject.getAttributeData().get(COLLIDESWITHOBJECTSID).add(newValues[i]);
		}
		
		public Array<String> getValues(GameObject myObject){
			return myObject.getAttributeData().get(COLLIDESWITHOBJECTSID);
		}
		
		public Array<String> getVariableNames(){
			Array<String> variableNames = new Array<String>();
			variableNames.add("Elasticity constant");
			variableNames.add("Object ID's in a list, delimited by commas");
			return variableNames;
		}
		public String getXMLName(){return "collidesWithObjectsID";}
	},
	/**
	 * mass is used in collisions, if the object will ever have an object colliding with it, it MUST have this attribute
	 */
	MASS{

		@Override
		public Array<String> getVariableNames() {
			Array<String> varName = new Array<String>();
			varName.add("mass");
			return varName;
		}

		@Override
		public Array<String> getValues(GameObject myObject) {
			return myObject.getAttributeData().get(MASS);
		}

		@Override
		public void update(GameObject myObject, Array<GameObject> allObjects) {}
		@Override
		public void setup(GameObject myObject) {}
		@Override
		public String getXMLName() {
			return "mass";
		}
		
	},
	/**
	 * Currently unimplemented, will cause object to follow a specific predefined path at a set speed
	 */
	MOVESONSETPATH{

		@Override
		public Array<String> getVariableNames() {
			Array<String> varName = new Array<String>();
			return varName;
		}

		@Override
		public void setup(GameObject myObject) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public Array<String> getValues(GameObject myObject) {
			return myObject.getAttributeData().get(MOVESONSETPATH);
		}

		@Override
		public void update(GameObject myObject, Array<GameObject> allObjects) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public String getXMLName() {
			// TODO Auto-generated method stub
			return "movesOnSetPath";
		}
		
		private float[] getPoint(int index,GameObject myObject){//FINISH ME SENPAI THIS HAS CHARS NOT FLOATS
			return new float[] {myObject.getAttributeData().get(MOVESONSETPATH).get(0).charAt((index-1)*4),myObject.getAttributeData().get(MOVESONSETPATH).get(0).charAt((index+1)*4)};
		}
		
	};
	/**
	 * @param 	A string(read in from the XML_Reader) to be converted into the associated Attribute
	 * @return	The Attribute associated with the given string
	 */
	public static Attribute newType(String type){
		return valueOf(type.toUpperCase());
	}
	
	//ALL BELOW METHODS MUST BE IMPLEMENTED
	/**
	 * Called once within the GameObject's constructor, used to setup initial values
	 * @param	myObject	the GameObject that currently holds this attribute
	 */
	public abstract void setup(GameObject myObject);
	/**
	 * Used to simulate the specific behavior for each game object, called each frame
	 * @param	myObject	the GameObject that currently holds the given attribute
	 * @param	allObjects	all of the gameObjects within the given level
	 */
	public abstract void update(GameObject myObject,Array<GameObject> allObjects);
	/**
	 * Used by XML_Writer, returns the values associated with the given GameObject's attribute
	 * @param	myObject	the GameObject that currently holds this attribute
	 * @return	A libGDX Array object containing the values to write
	 */
	public abstract Array<String> getValues(GameObject myObject);
	/**
	 * Used by the Editor to tell the user what values they need to input
	 * @return	A libGDX Array object containing descriptions of the values to setup
	 */
	public abstract Array<String> getVariableNames();
	/**
	 * Returns 	A string containing the name to write to the .xml file, this is used by the writer.
	 * @return	The string containing the name to write to the .xml file
	 */
	public abstract String getXMLName();//probably gonna replace this later, but i dont wanna do it right now
}
