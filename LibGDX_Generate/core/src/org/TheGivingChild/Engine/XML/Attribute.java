package org.TheGivingChild.Engine.XML;

import org.TheGivingChild.Engine.InputListenerEnums;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.utils.Array;

public enum Attribute {
	/* each type will have a update method and a setValues method which all take in an Array<String>
	 * each type can have private fields
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
			temp.add(myObject.getAttributeData().get(MOVES).get(0) + "," + myObject.getAttributeData().get(MOVES).get(1));
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
			Array<String> variableNames = new Array<String>();
			variableNames.add("Object ID to collide with");
			return variableNames;
		}
		public void setup(GameObject myObject){}//doesnt need to setup anything		
		public Array<String> getValues(GameObject myObject){return new Array<String>();}
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
		public String getXMLName(){return "color";}
		@Override
		public Array<String> getValues(GameObject myObject) {
			// TODO Auto-generated method stub
			return null;
		}
	},
	FALLSATSETRATE{
		private int rate;
		public void update(GameObject myObject){
		//	System.out.println("\nfallsAtSetRate Update");
			myObject.setPosition(myObject.getX(), myObject.getY() - rate * (Gdx.graphics.getDeltaTime()));
		}
		public void setValues(Array<String> newValues){
			rate = Integer.parseInt(newValues.get(0));
		}
		public Array<String> getValues(){
			Array<String> temp = new Array<String>();
			temp.add(rate+"");
			return temp;
		}
		public String getXMLName(){return "fallsAtSetRate";}
		@Override
		public Array<String> getVariableNames() {
			// TODO Auto-generated method stub
			return null;
		}
		@Override
		public void setup(GameObject myObject) {
			// TODO Auto-generated method stub
			
		}
		@Override
		public Array<String> getValues(GameObject myObject) {
			// TODO Auto-generated method stub
			return null;
		}
		@Override
		public void update(GameObject myObject, Array<GameObject> allObjects) {
			// TODO Auto-generated method stub
			
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
			return myObject.getAttributeData().get(HEALTH);
		}
		public String getXMLName(){return "health";}
		@Override
		public Array<String> getVariableNames() {
			// TODO Auto-generated method stub
			return null;
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
	COLLIDESWITHOBJECTSID{
		public void update(GameObject myObject,Array<GameObject> allObjects){
			Rectangle juan = new Rectangle(myObject.getX(),myObject.getY(),myObject.getWidth(),myObject.getHeight());
			for(int i =0; i < allObjects.size;i++){//myObject.getID() != allObjects.get(i).getID() &&
				System.out.println(myObject.getAttributeData().get(COLLIDESWITHOBJECTSID));
				if(myObject.getAttributeData().get(COLLIDESWITHOBJECTSID).contains(allObjects.get(i).getID()+"", true)){//if myObject collides with current object AND they are actually colliding
					System.out.println(myObject.getAttributeData().get(COLLIDESWITHOBJECTSID));
					Rectangle two = new Rectangle(allObjects.get(i).getX(),allObjects.get(i).getY(),allObjects.get(i).getWidth(),allObjects.get(i).getHeight());
					if(juan.overlaps(two)){
						System.out.println("COLLISION DETECETED");
					}
				}
			}
		}
		
		public void setup(GameObject myObject){
			
		}
		
		public Array<String> getValues(GameObject myObject){
			return myObject.getAttributeData().get(COLLIDESWITHOBJECTSID);
		}
		
		public Array<String> getVariableNames(){
			Array<String> variableNames = new Array<String>();
			variableNames.add("Object ID");
			return variableNames;
		}
		public String getXMLName(){return "collidesWithObjectsID";}
	};
	public abstract Array<String> getVariableNames();
	public abstract void setup(GameObject myObject);
	public abstract Array<String> getValues(GameObject myObject);
	public abstract void update(GameObject myObject,Array<GameObject> allObjects);
	public abstract String getXMLName();//probably gonna replace this later, but i dont wanna do it right now
	
	public static Attribute newType(String type){
		return valueOf(type.toUpperCase());
	}
}
