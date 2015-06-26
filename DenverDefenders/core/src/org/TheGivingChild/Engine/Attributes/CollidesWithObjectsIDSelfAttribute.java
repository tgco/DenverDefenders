package org.TheGivingChild.Engine.Attributes;

import org.TheGivingChild.Engine.AudioManager;
import org.TheGivingChild.Engine.XML.GameObject;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;

public class CollidesWithObjectsIDSelfAttribute extends Attribute{
	static final float MAX_VELOCITY = 400;
	static final float COLLISION_CONSTANT = 5;//5
	static final float COLLISION_OFFSET = 1;//1
	public void update(GameObject myObject,Array<GameObject> allObjects){
		Rectangle r1 = new Rectangle(myObject.getX(),myObject.getY(),myObject.getWidth(),myObject.getHeight());
		for(int i =0; i < allObjects.size;i++){
			if(myObject.getID() != allObjects.get(i).getID() && data.contains(allObjects.get(i).getID()+"", false)){//if myObject collides with current object AND they are actually colliding
				Rectangle r2 = new Rectangle(allObjects.get(i).getX(),allObjects.get(i).getY(),allObjects.get(i).getWidth(),allObjects.get(i).getHeight());
				if(r1.overlaps(r2)){
					//collision has been detected, getting needed information for the collision equation(using momentum)
					float m2 = 1f;
					float v1ix = myObject.getVelocity()[0];
					float v2ix = allObjects.get(i).getVelocity()[0];
					float v1iy = myObject.getVelocity()[1];
					float v2iy = allObjects.get(i).getVelocity()[1];
					
					float[] myObjectVelocity = new float[] {m2*v2ix - v1ix,m2*v2iy + v1iy};
					myObject.setVelocity(myObjectVelocity);
					float mag1 =(float) Math.pow(myObjectVelocity[0]*myObjectVelocity[0] + myObjectVelocity[1]*myObjectVelocity[1],.5);
					float[] myObjectDirection = {myObjectVelocity[0]/mag1,myObjectVelocity[1]/mag1};
					
					//this loop will make sure the objects aren't overlapping after collision has occurred, will likely remove loop and change code if time allows.
					while(r1.overlaps(r2)){
						myObject.moveBy(myObjectDirection[0]*COLLISION_CONSTANT+COLLISION_OFFSET,myObjectDirection[1]*COLLISION_CONSTANT+COLLISION_OFFSET);
						r1.setPosition(myObject.getX(),myObject.getY());
					}
					
					AudioManager.getInstance().play("sounds/bounce.wav");
					
					//MAX VELOCITY WORKAROUND SO OBJECTS DONT GO WARP SPEED
				if(myObject.getVelocity()[0] > MAX_VELOCITY)
						myObject.setVelocity(new float[] {MAX_VELOCITY,myObject.getVelocity()[1]});
					if(myObject.getVelocity()[1] > MAX_VELOCITY)
						myObject.setVelocity(new float[] {myObject.getVelocity()[0],MAX_VELOCITY});
					if(allObjects.get(i).getVelocity()[0] > MAX_VELOCITY)
						allObjects.get(i).setVelocity(new float[] {MAX_VELOCITY,allObjects.get(i).getVelocity()[1]});
					if(allObjects.get(i).getVelocity()[1] > MAX_VELOCITY)
						allObjects.get(i).setVelocity(new float[] {allObjects.get(i).getVelocity()[0],MAX_VELOCITY});
				}
			}
		}
	}

	public void setup(GameObject myObject){
		String[] newValues = data.get(0).split(",");
		data.set(0, newValues[0]);
		for(int i = 1; i < newValues.length;i++)
			data.add(newValues[i]);
	}
	
	public Array<String> getVariableNames(){
		Array<String> variableNames = new Array<String>();
		variableNames.add("Object ID's in a list, delimited by commas");
		return variableNames;
	}

}
