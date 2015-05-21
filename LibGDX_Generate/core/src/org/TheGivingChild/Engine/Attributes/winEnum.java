package org.TheGivingChild.Engine.Attributes;

import org.TheGivingChild.Engine.XML.GameObject;

import com.badlogic.gdx.utils.Array;

public enum WinEnum {
	
	TIMEOUT{
		private int winInfo;
		public void setValues(Array<String> newValues){
			winInfo = Integer.parseInt(newValues.first());
		}
		public String getXMLDescription(){
			return "timeout";
		}		
		public boolean checkWin(){
			//if gameclock > time, lose.
			return false;
		}
	},
	COLLISIONWITHOBJECT{
		private int objectID1;
		private int objectID2;
		public String getXMLDescription(){
			return "collisionWithObject";
		}
		public boolean checkWin(){
			return false;
		}
		public void setValues(Array<String> newValues){
			objectID1 = Integer.parseInt(newValues.get(0));
			objectID2 = Integer.parseInt(newValues.get(1));
		}
	};
	public abstract void setValues(Array<String> newValues);
	public abstract String getXMLDescription();
	public abstract boolean checkWin();
	
}
