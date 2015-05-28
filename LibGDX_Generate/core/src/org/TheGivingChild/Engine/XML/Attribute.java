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
		public void update(GameObject myObject){
			if(!hasRun){
				hasRun = true;
				myObject.setVelocity(initialVelocity);
			}
			myObject.setPosition((myObject.getX() + Gdx.graphics.getDeltaTime()*myObject.getVelocity()[0]), (myObject.getY() + Gdx.graphics.getDeltaTime()*myObject.getVelocity()[1]));
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
	BOUNCEOFFEDGEOFSCREEN{
		public void update(GameObject myObject){
			if(myObject.getX() <= 0 || myObject.getX() + myObject.getTexture().getWidth() >= Gdx.graphics.getWidth()){
				float[] temp = myObject.getVelocity();
				temp[0] = -temp[0];
				myObject.setVelocity(temp);
			}else if(myObject.getY() <= 0 || myObject.getY() + myObject.getTexture().getHeight() >= Gdx.graphics.getHeight()){
				float[] temp = myObject.getVelocity();
				temp[1] = -temp[1];
				myObject.setVelocity(temp);
			}
		}
		public void setValues(Array<String> loldidntread){}
		public Array<String> getValues(){return new Array<String>();}
		public String getXMLName(){return "bounceOffEdgeOfScreen";}		
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
