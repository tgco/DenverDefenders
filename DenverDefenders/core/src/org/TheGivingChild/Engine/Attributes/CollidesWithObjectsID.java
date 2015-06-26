package org.TheGivingChild.Engine.Attributes;

import org.TheGivingChild.Engine.XML.GameObject;

import com.badlogic.gdx.utils.Array;

public class CollidesWithObjectsID extends Attribute {
	static final float MAX_VELOCITY = 400;
	static final float COLLISION_CONSTANT = 5;
	static final float COLLISION_OFFSET = 1;
	public void update(GameObject myObject,Array<GameObject> allObjects) {
		/*
		REWRITE
		
		Rectangle r1 = new Rectangle(myObject.getX(),myObject.getY(),myObject.getWidth(),myObject.getHeight());
		for(int i =0; i < allObjects.size;i++){
			if(myObject.getID() != allObjects.get(i).getID() && myObject.getAttributeData().get(this).contains(allObjects.get(i).getID()+"", false)){//if myObject collides with current object AND they are actually colliding
				Rectangle r2 = new Rectangle(allObjects.get(i).getX(),allObjects.get(i).getY(),allObjects.get(i).getWidth(),allObjects.get(i).getHeight());
				if(r1.overlaps(r2) ){//&& juanSmall.overlaps(tooSmall)
					//collision has been detected, getting needed information for the collision equation(using momentum)
					float c1 = Float.parseFloat(myObject.getAttributeData().get(this).get(0));
					float m1 = Float.parseFloat(myObject.getAttributeData().get(MASS).get(0));
					float m2 = Float.parseFloat(allObjects.get(i).getAttributeData().get(MASS).get(0));
					float v1ix = myObject.getVelocity()[0];
					float v2ix = allObjects.get(i).getVelocity()[0];
					float v1iy = myObject.getVelocity()[1];
					float v2iy = allObjects.get(i).getVelocity()[1];
					
					float[] myObjectVelocity = new float[] {c1*((m1-m2)*v1ix + 2*m2*v2ix)/(m1+m2),c1*((m1-m2)*v1iy + 2*m2*v2iy)/(m1+m2)};
					myObject.setVelocity(myObjectVelocity);
					float mag1 =(float) Math.pow(myObjectVelocity[0]*myObjectVelocity[0] + myObjectVelocity[1]*myObjectVelocity[1],.5);
					float[] myObjectDirection = {myObjectVelocity[0]/mag1,myObjectVelocity[1]/mag1};

					float[] otherObjectVelocity = new float[] {-1*myObjectVelocity[0],-1*myObjectVelocity[1]};//new float[] {c1*((2*m1*v1ix+(m1-m2)*v2ix)/(m1+m2))*.9f,c1*(2*m1*v1iy+(m1-m2)*v2iy/(m1+m2))*.9f};
					allObjects.get(i).setVelocity(otherObjectVelocity);
					float mag2=(float) Math.pow(otherObjectVelocity[0]*otherObjectVelocity[0] + otherObjectVelocity[1]*otherObjectVelocity[1],.5);
					float[] otherObjectDirection = {otherObjectVelocity[0]/mag2,otherObjectVelocity[1]/mag2};
					
					//this loop will make sure the objects aren't overlapping after collision has occured, will likely remove loop and change code if time allows.
					while(r1.overlaps(r2)){
						if(mag1>mag2){//if object 1 is travelling faster than object 2 after the collision, then we want it to travel slightly farther, otherwise collision continually happens and bad things happen :( CHANGE ME AFTER LUNCH
							myObject.moveBy(myObjectDirection[0]*COLLISION_CONSTANT+COLLISION_OFFSET,myObjectDirection[1]*COLLISION_CONSTANT+COLLISION_OFFSET);
							allObjects.get(i).moveBy(otherObjectDirection[0]*COLLISION_CONSTANT,otherObjectDirection[1]*COLLISION_CONSTANT);
						}else{//else object 2 is faster than obj1, repeat.
							myObject.moveBy(myObjectDirection[0]*COLLISION_CONSTANT,myObjectDirection[1]*COLLISION_CONSTANT);
							allObjects.get(i).moveBy(otherObjectDirection[0]*COLLISION_CONSTANT+COLLISION_OFFSET,otherObjectDirection[1]*COLLISION_CONSTANT+COLLISION_OFFSET);
						}
						r1.setPosition(myObject.getX(),myObject.getY());
						r2.setPosition(allObjects.get(i).getX(),allObjects.get(i).getY());
					}
					
					// Play a bounce noise
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
		*/
	}
	
	public void setup(GameObject myObject){
		String[] newValues = myObject.getAttributeData().get(this).get(1).split(",");
		myObject.getAttributeData().get(this).set(1, newValues[0]);
		for(int i = 1; i < newValues.length;i++)
			myObject.getAttributeData().get(this).add(newValues[i]);
	}
	
	public Array<String> getVariableNames(){
		Array<String> variableNames = new Array<String>();
		variableNames.add("Elasticity constant");
		variableNames.add("Object ID's in a list, delimited by commas");
		return variableNames;
	}

}
