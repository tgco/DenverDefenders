package org.TheGivingChild.Engine.Attributes;

import org.TheGivingChild.Engine.XML.GameObject;

import com.badlogic.gdx.utils.Array;

public class SpawnObjectOnTimerAttribute extends Attribute {
	private boolean spawned = false;
	@Override
	public void setup(GameObject myObject) {
		
	}

	@Override
	public void update(GameObject myObject, Array<GameObject> allObjects) {
		/*
			REWRITE (currently broken)
			
		long currentTime = MinigameClock.getInstance().getLevelTimeInSeconds();
		float time = Float.parseFloat(myObject.getAttributeData().get(SPAWNOBJECTONTIMER).get(0));
		if(!myObject.isDisposed() && currentTime - time < 2.0 && !spawned) {
			Array<String> newListeners = myObject.getListenerNames();
			//System.out.println(newListeners.size);
			GameObject newObj = new GameObject(allObjects.size, myObject.getImageFilename(), 
					new float[] {myObject.getX()+300,myObject.getY()+300},
					newListeners, myObject.getAttributeData());
			allObjects.add(newObj);
			spawned=true;
		}
		*/
	}

	@Override
	public Array<String> getVariableNames() {
		Array<String> varNames = new Array<String>();
		varNames.add("Seconds Till is Spawns");
		return varNames;
	}
}
