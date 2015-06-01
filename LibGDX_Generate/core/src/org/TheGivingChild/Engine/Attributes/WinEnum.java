package org.TheGivingChild.Engine.Attributes;

import org.TheGivingChild.Engine.XML.Attribute;
import org.TheGivingChild.Engine.XML.GameObject;
import org.TheGivingChild.Engine.XML.Level;

import com.badlogic.gdx.utils.Array;

public enum WinEnum {//Cannot have any labels in common with LoseEnum
	
	TIMEOUT{
		private int winInfo;
		
		public void setValues(Array<String> newValues){
			winInfo = Integer.parseInt(newValues.first());
		}
		public String getXMLDescription(){
			return "timeout";
		}
		public Array<String> getValues(){
			Array<String> temp = new Array<String>();
			temp.add(winInfo+"");
			return temp;
		}
		@Override
		public void checkWin(Level level) {
			// TODO Auto-generated method stub
			
		}
	},
	COLLISIONWITHOBJECTWIN{
		private int objectID1;
		private int objectID2;
		
		public String getXMLDescription(){
			return "collisionWithObjectWin";
		}
		public void setValues(Array<String> newValues){
			objectID1 = Integer.parseInt(newValues.get(0));
			objectID2 = Integer.parseInt(newValues.get(1));
		}
		public Array<String> getValues(){
			Array<String> temp = new Array<String>();
			temp.add(objectID1+"");
			temp.add(objectID2+"");
			return temp;
		}
		@Override
		public void checkWin(Level level) {
			// TODO Auto-generated method stub
			
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
		public void checkWin(Level level) {
			boolean win = true;
			for(GameObject gameObject: level.getGameObjects()){
				if(!gameObject.isDisposed()){
					win = false;
				}
			}
			if(win){
				System.out.println("Level "+level.getLevelName()+" won!");
				level.resetLevel();
			}
		}
		@Override
		public Array<String> getValues() {
			// TODO Auto-generated method stub
			return null;
		}		
	};
	public abstract void setValues(Array<String> newValues);
	public abstract String getXMLDescription();
	public abstract void checkWin(Level level);
	public abstract Array<String> getValues();
	
	public static WinEnum newType(String type){
		return valueOf(type.toUpperCase());
	}
	
}
