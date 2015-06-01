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
		
		public void setup(GameObject myObject){
			
		}
		
		public Array<String> getValues(GameObject myObject){return new Array<String>();}
		public String getXMLName(){return "bounceOffEdgeOfScreen";}		
	},
	HEALTH{
		private int health;
		public void update(GameObject myObject){
			//System.out.println("\nHealth Update\n" + health);
		}
		
		public void setup(GameObject myObject){
			
		}

		public Array<String> getValues(GameObject myObject){//each implementation of get Values translates the values back into the string for writing to .xml purposes
			Array<String> temp = new Array<String>();
			temp.add(Integer.toString(health));
			return temp;
		}
		public String getXMLName(){return "health";}
	};
	
	public abstract void setup(GameObject myObject);
	public abstract Array<String> getValues(GameObject myObject);
	public abstract void update(GameObject myObject);
	public abstract String getXMLName();//probably gonna replace this later, but i dont wanna do it right now
	
	public static Attribute newType(String type){
		return valueOf(type.toUpperCase());
	}
}
