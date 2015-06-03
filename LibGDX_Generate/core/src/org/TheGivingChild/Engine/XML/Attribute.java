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
			Array<String> variableNames = new Array<String>();
			return variableNames;
		}
		@Override
		public void setup(GameObject myObject) {
			// TODO Auto-generated method stub
			
		}
		@Override
		public Array<String> getValues(GameObject myObject) {
			return myObject.getAttributeData().get(FALLSATSETRATE);
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
			Array<String> variableNames = new Array<String>();
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
	COLLIDESWITHOBJECTSID{//only gonna get square objects working for now, circular objects wont be too hard later
		//data is stored as value1=mass of object, all other values are the objects it can collide with
		public void update(GameObject myObject,Array<GameObject> allObjects){
			Rectangle juan = new Rectangle(myObject.getX(),myObject.getY(),myObject.getWidth(),myObject.getHeight());
			for(int i =0; i < allObjects.size;i++){
				if(myObject.getID() != allObjects.get(i).getID() && myObject.getAttributeData().get(COLLIDESWITHOBJECTSID).contains(allObjects.get(i).getID()+"", false)){//if myObject collides with current object AND they are actually colliding
					Rectangle two = new Rectangle(allObjects.get(i).getX(),allObjects.get(i).getY(),allObjects.get(i).getWidth(),allObjects.get(i).getHeight());
					if(juan.overlaps(two)){
						System.out.println("COLLISION DETECETED: " + myObject.getID() + ", " + allObjects.get(i).getID());
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
			Array<String> newValues = new Array<String>();
			for(String current:myObject.getAttributeData().get(COLLIDESWITHOBJECTSID).get(1).split(","))
				newValues.add(current);//FINISH ME SENPAI
		}
		
		public Array<String> getValues(GameObject myObject){
			return myObject.getAttributeData().get(COLLIDESWITHOBJECTSID);
		}
		
		public Array<String> getVariableNames(){
			Array<String> variableNames = new Array<String>();
			variableNames.add("Elasticity constant");//uh oh. can only collide with 1 object. not variable.
			variableNames.add("Object ID's in a list, delimited by commas");
			return variableNames;
		}
		public String getXMLName(){return "collidesWithObjectsID";}
	},
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
	MOVESONSETPATH{

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

		@Override
		public String getXMLName() {
			// TODO Auto-generated method stub
			return null;
		}
		
		private float[] getPoint(int index,GameObject myObject){//FINISH ME SENPAI THIS HAS CHARS NOT FLOATS
			return new float[] {myObject.getAttributeData().get(MOVESONSETPATH).get(0).charAt((index-1)*4),myObject.getAttributeData().get(MOVESONSETPATH).get(0).charAt((index+1)*4)};
		}
		
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
