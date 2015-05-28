package org.TheGivingChild.Engine.XML;

import org.TheGivingChild.Engine.InputListenerEnums;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.utils.Array;


public enum Attribute {
	/* each type will have a update method and a setValues method which all take in an Array<String>
	 * each type can have private fields
	 */
	MOVES{//velocity is stored in GameObject, but moves actually simulates it moving and updates the location, no other attribute should change location unless you are doing so to make some other crazy stuffs happen
		private float[] initialVelocity = new float[2];;
		private boolean hasRun = false;
		public void update(GameObject myObject){
			if(!hasRun){
				hasRun = true;
				myObject.setVelocity(initialVelocity);
			}
			myObject.setPosition((myObject.getX() + Gdx.graphics.getDeltaTime()*myObject.getVelocity()[0]), (myObject.getY() + Gdx.graphics.getDeltaTime()*myObject.getVelocity()[1]));
			System.out.println("X positon: " + myObject.getX() + ", y position: " + myObject.getY());
		}
		
		public void setValues(Array<String> newValues){
			initialVelocity[0] = Float.parseFloat(newValues.get(0));
			initialVelocity[1] = Float.parseFloat(newValues.get(1));
		}
		
		public Array<String> getValues(){
			Array<String> temp = new Array<String>();
			temp.add(initialVelocity[0] + "," + initialVelocity[1]);
			return temp;
		}
		
		public String getXMLName(){return "moves";}
	},
	HEALTH{
		private int health;
		public void update(GameObject myObject){
			//System.out.println("\nHealth Update\n" + health);
		}
		public void setValues(Array<String> newValues){//each implementation of setValues translates the array of strings into whatever datatype it wants
			health = Integer.parseInt(newValues.first().toString());
		}
		
		public Array<String> getValues(){//each implementation of get Values translates the values back into the string for writing to .xml purposes
			Array<String> temp = new Array<String>();
			temp.add(Integer.toString(health));
			return temp;
		}
		public String getXMLName(){return "health";}
	},
	COLOR{
		private String color;
		public void update(GameObject myObject){
			//System.out.println("\nColor Update\n" + color);
		}
		
		public void setValues(Array<String> newValues){
			color = newValues.first();
		}
		
		public Array<String> getValues(){
			Array<String> temp = new Array<String>();
			temp.add(color);
			return temp;
		}
		public String getXMLName(){return "color";}
	},
	MOVESONSETPATH{//requires moves?
		private Array<float[]> path;
		private int currentPoint;//index of current waypoint in path
		private float tolerance;
		private float speed;
		public void update(GameObject myObject){
			System.out.println("\nMovesOnSetPath Update");
			if(calcDistance(myObject.getX(),myObject.getY()) <= tolerance){//close enough to current point, setup next point
				//setup currentPoint
				currentPoint++;
				if(currentPoint >= path.size)
					currentPoint = 0;
				//setup new velocity vector for myObject
				//speed = (float) Math.pow(myObject.getVelocity()[0]*myObject.getVelocity()[0] + myObject.getVelocity()[1]*myObject.getVelocity()[1], .5);
				float[] direction = calcDirection(myObject.getX(),myObject.getY());
				myObject.setVelocity(new float[] {direction[0]*speed,direction[1]*speed});
			}
			//dont need to simulate movement, that is for MOVES to do
		}
		
		private float[] calcDirection(float x, float y){//returns the direction vector(magnitude of 1!) from current position to currentPoint
			return new float[] {(float) Math.asin(x-path.get(currentPoint)[0]),(float) Math.asin(y-path.get(currentPoint)[1])};
		}
		
		private double calcDistance(float x, float y){//double check this is working
			return Math.pow(Math.pow(x-path.get(currentPoint)[0], 2) + Math.pow(y-path.get(currentPoint)[1], 2),0.5);
		}
		
		public void setValues(Array<String> newValues){
			path = stringToPath(newValues.get(0));
			tolerance = Float.parseFloat(newValues.get(1));
			currentPoint = 0;
			//NEEDS TO CALCULATE THE INITIAL DIRECTION TOO, OH BOYYYY
			//TAKE IN AND SET SPEED TOO BRUH
		}
		
		public Array<String> getValues(){
			Array<String> temp = new Array<String>();
			String tempS = "";
			for(float[] point:path)
				tempS+=";" + point[0] + "," + point[1];
			temp.add(tempS.replaceFirst(";",""));
			temp.add(tolerance+"");
			//ADD SPEED OUTPUT HERE BRUH
			return temp;
		}
		
		private Array<float[]> stringToPath(String sPath){//takes in string of format "1.0,1.0;2.0,2.0;3.0,3.0" and returns an Array of 2 element float arrays representing a path
			Array<float[]> newPath = new Array<float[]>();
			String points[] = sPath.split(";");
			for(int i = 0; i < points.length; i++)
				newPath.add(stringToPoint(points[i]));
			return newPath;
		}
		
		private float[] stringToPoint(String toPoint){//takes in a string of form "1.0,1.0" and returns a 2 element array of floats
			float temp[] = {Float.parseFloat(toPoint.substring(0, toPoint.indexOf(","))),Float.parseFloat(toPoint.substring(toPoint.indexOf(",")+1,toPoint.length()-1))};
			return temp;
		}
		public String getXMLName(){return "movesOnSetPath";}
	},
	DESTROY_ON_CLICK{
		public void update(GameObject myObject){
			//System.out.println("\nDisappearsOnPress Update");
		}
		public InputListener getInputListener(GameObject object){
			return InputListenerEnums.valueOf("DESTROY_ON_CLICK").getInputListener(object);
		}
		public void setValues(Array<String> newValues){}
		public Array<String> getValues(){return new Array<String>();}//empty might have to deal with it laters
		public String getXMLName(){return "destroy_on_click";}
	},
	FALLSATSETRATE{
		private int rate;
		public void update(GameObject myObject){//will probably need to change this to something like the moves attribute
			System.out.println("\nfallsAtSetRate Update");
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
	},
	SPINS{
		private float rate;
		public void update(GameObject myObject){
			//System.out.println("\nSpins Update");
			
		}
		public void setValues(Array<String> newValues){
			rate = Float.parseFloat(newValues.get(0));
		}
		public Array<String> getValues(){
			Array<String> temp = new Array<String>();
			temp.add(rate+"");
			return temp;
		}
		public String getXMLName(){return "spins";}
		
	};
	
	public abstract void setValues(Array<String> newValues);
	public abstract Array<String> getValues();
	public abstract void update(GameObject myObject);
	public abstract String getXMLName();//probably gonna replace this later, but i dont wanna do it right now
	
	public static Attribute newType(String type){
		return valueOf(type.toUpperCase());
	}
}
