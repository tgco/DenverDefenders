package org.TheGivingChild.Engine.XML;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.Array;


public enum Attribute {
	/* each type will have a update method and a setValues method which all take in an Array<String>
	 * each type can have private fields
	 */
	MOVES{//hooray now things can move and other attributes can affect movement!, it's input arguments only affect initial speed. 
		private boolean hasMoved = false;
		private float[] velocity;
		public void update(GameObject myObject){//gotta do some jank code because other objects need to see it's speed, but dont wanna pass around an Array<String> and convert back and forth with floats
			System.out.println("\nmoves Update");
			if(!hasMoved){
				hasMoved = true;
				myObject.setVelocity(velocity);
			}
			myObject.setPosition(myObject.getX() + (velocity[0]*Gdx.graphics.getDeltaTime()), myObject.getY() + (velocity[1]*Gdx.graphics.getDeltaTime()));
		}
		public void setValues(Array<String> newValues){
			velocity = new float[2];
			velocity[0] = Float.parseFloat(newValues.get(0));//x
			velocity[1] = Float.parseFloat(newValues.get(1));//y
		}
		public Array<String> getValues(){
			Array<String> temp = new Array<String>();
			temp.add(velocity[0]+"");
			temp.add(velocity[1]+"");
			return temp;
		}
		public String getXMLName(){return "moves";}
	},
	HEALTH{
		private int health;
		public void update(GameObject myObject){
			System.out.println("\nHealth Update\n" + health);
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
			System.out.println("\nColor Update\n" + color);
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
		private double tolerance;
		private Array<float[]> path;
		private int currentPoint;
		public void update(GameObject myObject){
			System.out.println("\nMovesOnSetPath Update");
			if(distanceToPoint(myObject.getX(),myObject.getY()) < tolerance){//if 'close enough' to target point, goto next ((x2-x1)^2 + (y2-y1)^2)^.5
				currentPoint++;
				//FINISH ME BRAH I NEED TO GO THE RIGHT DIRECTION, sin(theta) = slope
				
			}
		}
		
		private double distanceToPoint(float x, float y){
			return Math.pow(Math.pow(x-path.get(currentPoint)[0], 2) + Math.pow(y-path.get(currentPoint)[1],2),.5);
		}
		
		public void setValues(Array<String> newValues){
			path = stringToPath(newValues.get(0));
			tolerance = Double.parseDouble(newValues.get(1));
			currentPoint = 0;//initializes for the object to immediately start heading for first point
			//NEEDS TO CALCULATE THE INITIAL DIRECTION TOO, OH BOYYYY
		}
		
		public Array<String> getValues(){
			Array<String> temp = new Array<String>();
			String tempS = "";
			for(float[] point:path)
				tempS+=";" + point[0] + "," + point[1];
			temp.add(tempS.replaceFirst(";",""));
			temp.add(tolerance+"");
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
	DISAPPEARSONPRESS{
		public void update(GameObject myObject){
			System.out.println("\nDisappearsOnPress Update");
		}
		public void setValues(Array<String> newValues){}
		public Array<String> getValues(){return new Array<String>();}//empty might have to deal with it laters
		public String getXMLName(){return "disappearsOnPress";}
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
			System.out.println("\nSpins Update");
			
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
