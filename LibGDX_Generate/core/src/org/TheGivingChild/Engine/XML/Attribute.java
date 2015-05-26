package org.TheGivingChild.Engine.XML;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.Array;


public enum Attribute {
	/* each type will have a update method and a setValues method which all take in an Array<String>
	 * each type will have a field of variable type that is private
	 */
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
	MOVESONSETPATH{
		private Array<float[]> path;
		public void update(GameObject myObject){
			
			
		}
		
		public void setValues(Array<String> newValues){
			path = stringToPath(newValues.get(0));
		}
		
		public Array<String> getValues(){
			Array<String> temp = new Array<String>();
			String tempS = "";
			for(float[] point:path)
				tempS+=";" + point[0] + "," + point[1];
			temp.add(tempS.replaceFirst(";",""));
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
			//System.out.println("\nDisappearsOnPress Update");
		}
		public void setValues(Array<String> newValues){}
		public Array<String> getValues(){return new Array<String>();}//empty might have to deal with it laters
		public String getXMLName(){return "disappearsOnPress";}
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
