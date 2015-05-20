package org.TheGivingChild.Engine.XML;

import com.badlogic.gdx.utils.Array;


public enum Attribute {
	/* each type will have a update method and a setValues method which all take in an Array<String>
	 * each type will have a field of variable type that is private
	 */
	HEALTH{
		private int health;
		public void update(){
			
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
		public void update(){
			
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
		private Array<float[]> points;
		public void update(){
			
		}
		
		public void setValues(Array<String> newValues){
			points = stringToPath(newValues.get(0));
		}
		
		public Array<String> getValues(){
			Array<String> temp = new Array<String>();
			String tempS = "";
			for(float[] point:points)
				tempS+=";" + point[0] + "," + point[1];
			temp.add(tempS.replaceFirst(";",""));
			return temp;
		}
		
		private Array<float[]> stringToPath(String sPath){
			Array<float[]> newPath = new Array<float[]>();
			String points[] = sPath.split(";");
			for(int i = 0; i < points.length; i++){
				newPath.add(stringToPoint(points[i]));
			}
			return newPath;
		}
		
		private float[] stringToPoint(String toPoint){
			System.out.println(toPoint);
			float temp[] = {Float.parseFloat(toPoint.substring(0, toPoint.indexOf(","))),Float.parseFloat(toPoint.substring(toPoint.indexOf(",")+1,toPoint.length()-1))};
			return temp;
		}
		public String getXMLName(){return "movesOnSetPath";}
	},
	DISAPPEARSONPRESS{
		public void update(){
			
		}
		public void setValues(Array<String> newValues){}
		public Array<String> getValues(){return new Array<String>();}//empty might have to deal with it laters
		public String getXMLName(){return "disappearsOnPress";}
	};	
	
	
	
	
	
	
	
	public abstract void setValues(Array<String> newValues);
	public abstract Array<String> getValues();
	public abstract void update();
	public abstract String getXMLName();//probably gonna replace this later, but i dont wanna do it right now
	
	public static Attribute newType(String type){
		return valueOf(type.toUpperCase());
	}
}
