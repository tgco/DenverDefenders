package org.TheGivingChild.Engine.Attributes;

import org.TheGivingChild.Engine.XML.GameObject;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;

public class DestroysObjectsOfIDOnCollisionAttribute extends Attribute {
	@Override
	public void setup(GameObject myObject){
		String[] list = myObject.getAttributeData().get(this).get(0).split(",");
		myObject.getAttributeData().get(this).set(0, list[0]);
		for(int i = 1;i<list.length;i++)
			myObject.getAttributeData().get(this).add(list[i]);
	}

	@Override
	public void update(GameObject myObject, Array<GameObject> allObjects) {
		Rectangle r1 = new Rectangle(myObject.getX(),myObject.getY(),myObject.getTextureWidth(),myObject.getTextureHeight());
		for(int i = 0; i<allObjects.size;i++){
			GameObject currentObject = allObjects.get(i);
			if(myObject.getAttributeData().get(this).contains(currentObject.getID()+"", false)){
				Rectangle r2 = new Rectangle(currentObject.getX(),currentObject.getY(),currentObject.getTextureWidth(),currentObject.getTextureHeight());
				if(r1.overlaps(r2)){
					allObjects.get(i).dispose();
				}
			}
		}
	}

	@Override
	public Array<String> getValues(GameObject myObject) {
		return myObject.getAttributeData().get(this);
	}

	@Override
	public Array<String> getVariableNames(){
		Array<String> names = new Array<String>();
		names.add("TODO");
		return names;
	}
}
