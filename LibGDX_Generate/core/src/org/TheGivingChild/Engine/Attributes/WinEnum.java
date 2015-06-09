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
		public String getXMLDescription(){
			return "collisionWithObjectWin";
		}
		public void setValues(Array<String> newValues){
			
		}
		
		public Array<String> getValues(){
			Array<String> temp = new Array<String>();
			temp.add("ASD");
			return temp;
		}
		@Override
		public void checkWin(Level level) {
			boolean win = true;
			Array<String> myInfo = level.getWinInfo(COLLISIONWITHOBJECTWIN);
			GameObject obj1 = level.getGameObjects().get(Integer.parseInt(myInfo.get(0)));
			Rectangle r1 = new Rectangle(obj1.getX(),obj1.getY(),obj1.getTexture().getWidth(),obj1.getTexture().getHeight());
			String[] IDList = myInfo.get(1).split(",");
			for(int i = 0; i<IDList.length;i++){
				int currentID = Integer.parseInt(IDList[i]);
				System.out.println("ID:" + currentID);
				//if(currentID == obj1.getID()){
					System.out.println("\tTHERE");
					//setup new rectangle with dimensions of target object
					Rectangle r2 = new Rectangle(level.getGameObjects().get(currentID).getX(),level.getGameObjects().get(currentID).getY(),level.getGameObjects().get(currentID).getTexture().getWidth(),level.getGameObjects().get(currentID).getTexture().getHeight());
					if(!r1.overlaps(r2)){//if the rectangle does not overlap, win condition has not been satisfied
						win = false;
					}
				//}
			}
			level.setCompleted(win);
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
