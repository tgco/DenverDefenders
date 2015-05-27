package org.TheGivingChild.Engine.Attributes;

import org.TheGivingChild.Engine.XML.Attribute;
import org.TheGivingChild.Engine.XML.GameObject;
import org.TheGivingChild.Engine.XML.Level;

import com.badlogic.gdx.utils.Array;

public enum WinEnum {//Cannot have any labels in common with LoseEnum
	
	TIMEOUT{
		private int winInfo;
		private boolean win = true;
		
		public void setValues(Array<String> newValues){
			winInfo = Integer.parseInt(newValues.first());
		}
		public String getXMLDescription(){
			return "timeout";
		}		
		@Override
		public boolean checkWin(Level level){
			//if gameclock > time, lose.
			return false;
		}
		public Array<String> getValues(){
			Array<String> temp = new Array<String>();
			temp.add(winInfo+"");
			return temp;
		}
	},
	COLLISIONWITHOBJECTWIN{
		private int objectID1;
		private int objectID2;
		private boolean win = true;
		
		public String getXMLDescription(){
			return "collisionWithObjectWin";
		}
		@Override
		public boolean checkWin(Level level){
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
	},
	ALL_OBJECTS_DESTROYED{
		
		@Override
		public void setValues(Array<String> newValues) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public String getXMLDescription() {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public boolean checkWin(Level level) {
			boolean win = level.getWin();
			for(GameObject gameObject: level.getGameObjects()){
				if(!gameObject.isDisposed()){
					win = false;
				}
			}
			
			return win;
		}

		@Override
		public Array<String> getValues() {
			// TODO Auto-generated method stub
			return null;
		}

		
	};
	public abstract void setValues(Array<String> newValues);
	public abstract String getXMLDescription();
	public abstract boolean checkWin(Level level);
	public abstract Array<String> getValues();
	
	public static WinEnum newType(String type){
		return valueOf(type.toUpperCase());
	}
	
}
