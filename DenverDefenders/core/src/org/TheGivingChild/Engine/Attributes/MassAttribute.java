package org.TheGivingChild.Engine.Attributes;

import org.TheGivingChild.Engine.XML.GameObject;

import com.badlogic.gdx.utils.Array;

public class MassAttribute extends Attribute {
	@Override
	public Array<String> getVariableNames() {
		Array<String> varName = new Array<String>();
		varName.add("mass");
		return varName;
	}

	@Override
	public Array<String> getValues(GameObject myObject) {
		return myObject.getAttributeData().get(this);
	}

	@Override
	public void update(GameObject myObject, Array<GameObject> allObjects) {}
	@Override
	public void setup(GameObject myObject) {}
}
