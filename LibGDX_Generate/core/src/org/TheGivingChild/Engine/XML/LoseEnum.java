package org.TheGivingChild.Engine.XML;

import org.TheGivingChild.Engine.MinigameClock;

import com.badlogic.gdx.utils.Array;

public enum LoseEnum {//Cannot have any labels in common with WinEnum
	TIMEOUT_LOSE{
		@Override
		public String getXMLDescription(){
			return "timeout";
		}
		@Override
		public Array<String> getValues(Level level){
			return level.getLoseInfo(TIMEOUT_LOSE);
		}
		@Override
		public void checkLose(Level level){
			if(MinigameClock.getInstance().outOfTime() ) {
				level.setCompleted(true);
				level.setWon(false);
			}
		}
		@Override
		public void setValues(Array<String> newValues) {
			// TODO Auto-generated method stub
			
		}
		@Override
		public void setup(Level level) {
			MinigameClock.getInstance().setLevelLength(Long.parseLong(level.getLoseInfo(TIMEOUT_LOSE).get(0)));			
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
		@Override
		public void setup(Level level) {
			// TODO Auto-generated method stub
			
		}
	},
	BELOW_SCREEN_LOSE_ID{

		@Override
		public String getXMLDescription() {
			return "below_screen_lose_ID";
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
			boolean lose = false;
			for(String currentID:level.getLoseInfo(BELOW_SCREEN_LOSE_ID).get(0).split(",")){
				GameObject currentObject = level.getObjectOfID(Integer.parseInt(currentID));
				if(currentObject != null && currentObject.getY() < 0){//if the object exists and it's position is above y=0, don't lose
					lose = true;
					break;
				}
			}
			if(lose) System.out.println("LOSE");
			
			level.setCompleted(lose);
			level.setWon(!lose);
		}

		@Override
		public void setup(Level level) {
			// TODO Auto-generated method stub
			
		}
		
	};
	public abstract String getXMLDescription();
	public abstract Array<String> getValues(Level level);
	public abstract void setValues(Array<String> newValues);
	public abstract void checkLose(Level level);
	public abstract void setup(Level level);
	
	public static LoseEnum newType(String type){
		return valueOf(type.toUpperCase());
	}
}
