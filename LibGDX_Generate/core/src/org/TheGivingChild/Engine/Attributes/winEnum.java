package org.TheGivingChild.Engine.Attributes;

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
		public Array<String> getValues(){
			Array<String> temp = new Array<String>();
			temp.add(winInfo+"");
			return temp;
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
			System.out.println(newValues);
			objectID1 = Integer.parseInt(newValues.get(0));
			System.out.println(newValues);
			objectID2 = Integer.parseInt(newValues.get(1));
		}
		public Array<String> getValues(){
			Array<String> temp = new Array<String>();
			temp.add(objectID1+"");
			temp.add(objectID2+"");
			return temp;
		}
	};
	public abstract void setValues(Array<String> newValues);
	public abstract String getXMLDescription();
	public abstract boolean checkWin();
	public abstract Array<String> getValues();
}
