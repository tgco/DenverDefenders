package org.TheGivingChild.Engine.XML;

import org.TheGivingChild.Engine.MinigameClock;

import com.badlogic.gdx.utils.Array;

public enum LoseEnum {//Cannot have any labels in common with WinEnum
	TIMEOUT{
		private float time;
		public String getXMLDescription(){
			return "timeout";
		}
		public Array<String> getValues(Level level){
			Array<String> temp = new Array<String>();
			temp.add(time+"");
			return temp;
		}
		public void setValues(Array<String> newValues){
			time = Float.parseFloat(newValues.first());
		}
		public void checkLose(Level level){
			if(MinigameClock.getInstance().outOfTime()) {
				level.setCompleted(true);
				level.setWon(false);
			}
		}
	},
	COLLISION_WITH_OBJECTLOSE{//totally not working or anything at all
		private int objectID1;
		private int objectID2;
		public String getXMLDescription(){
			return "collision_With_ObjectLose";
		}
		public void checkLose(Level level){
		}
		public void setValues(Array<String> newValues){
			objectID1 = Integer.parseInt(newValues.get(0));
			objectID2 = Integer.parseInt(newValues.get(1));
		}
		public Array<String> getValues(Level level){
			Array<String> temp = new Array<String>();
			temp.add(objectID1+"");
			temp.add(objectID2+"");
			return temp;
		}
	},
	BELOW_SCREEN_LOSE_ID{

		@Override
		public String getXMLDescription() {
			return "below_screen_lose";
		}

		@Override
		public Array<String> getValues(Level level) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public void setValues(Array<String> newValues) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void checkLose(Level level) {
			boolean win = true;
			for(String currentID:level.getWinInfo(OBJECTS_OF_ID_DESTROYED).get(0).split(",")){
				if(level.getObjectOfID(Integer.parseInt(currentID)) != null){//if the object exists, set win to false, exit loop
					win = false;
					break;
				}
			}
			level.setCompleted(win);
			level.setWon(win);
		}
		
	};
	public abstract String getXMLDescription();
	public abstract Array<String> getValues(Level level);
	public abstract void setValues(Array<String> newValues);
	public abstract void checkLose(Level level);
	
	public static LoseEnum newType(String type){
		return valueOf(type.toUpperCase());
	}
}
