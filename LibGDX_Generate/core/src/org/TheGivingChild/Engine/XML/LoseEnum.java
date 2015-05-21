package org.TheGivingChild.Engine.XML;

import com.badlogic.gdx.utils.Array;

public enum LoseEnum {
	TIMEOUT{
		private int time;
		public String getXMLDescription(){
			return "timeout";
		}
		public Array<String> getValues(){
			Array<String> temp = new Array<String>();
			temp.add(time+"");
			return temp;
		}
		public void setValues(Array<String> newValues){
			time = Integer.parseInt(newValues.first());
		}
	};
	public abstract String getXMLDescription();
	public abstract Array<String> getValues();
	public abstract void setValues(Array<String> newValues);
}
