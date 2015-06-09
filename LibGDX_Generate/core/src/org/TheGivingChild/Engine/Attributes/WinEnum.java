package org.TheGivingChild.Engine.Attributes;

import org.TheGivingChild.Engine.MinigameClock;
import org.TheGivingChild.Engine.XML.Attribute;
import org.TheGivingChild.Engine.XML.GameObject;
import org.TheGivingChild.Engine.XML.Level;

import com.badlogic.gdx.math.Rectangle;
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
			if(MinigameClock.getInstance().outOfTime()){
				level.setCompleted(true);
			}
		}
	},
	COLLISIONWITHOBJECTWIN{
		Array<Integer> values = new Array<Integer>();
		public String getXMLDescription(){
			return "collisionWithObjectWin";
		}
		public void setValues(Array<String> newValues){
			values.clear();
			for(String currentValue:newValues)
				values.add(Integer.parseInt(currentValue));
		}
		
		public Array<String> getValues(){
			Array<String> temp = new Array<String>();
			temp.add("ASD");
			return temp;
		}
		@Override
		public void checkWin(Level level) {
//			Rectangle r1 = new Rectangle(level.getGameObjects().get(values.get(0)).getX(),
//					level.getGameObjects().get(values.get(0)).getY(),
//					level.getGameObjects().get(values.get(0)).getTexture().getWidth(),
//					level.getGameObjects().get(values.get(0)).getTexture().getHeight());
//			for(GameObject curObj: level.getGameObjects()){
//				if(values.contains(curObj.getID(),false)){
//					Rectangle r2 = new Rectangle(curObj.getX(),curObj.getY(),curObj.getTexture().getWidth(),curObj.getTexture().getHeight());
//					
//				}					
//			}
//			
		}
	},
	ALL_OBJECTS_DESTROYED{
		@Override
		public void setValues(Array<String> newValues) {
			
		}
		@Override
		public String getXMLDescription() {
			return "all_objects_destroyed";
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
				//level.resetLevel();
				level.setCompleted(true);
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
