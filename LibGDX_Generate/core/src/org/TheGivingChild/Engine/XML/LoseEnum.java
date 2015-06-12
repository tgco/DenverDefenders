package org.TheGivingChild.Engine.XML;

import org.TheGivingChild.Engine.MinigameClock;

import com.badlogic.gdx.utils.Array;

public enum LoseEnum {//Cannot have any labels in common with WinEnum
	/**
	 * if timer expires, lose
	 */
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
		public void setup(Level level) {
			MinigameClock.getInstance().setLevelLength(Long.parseLong(level.getLoseInfo(TIMEOUT_LOSE).get(0)));			
		}
	},
	/**
	 * if any object listed in value2 collides with value1, lose
	 */
	COLLISION_WITH_OBJECT_LOSE{
		public String getXMLDescription(){
			return "collision_With_Object_Lose";
		}
		public void checkLose(Level level){
			
		}
		public Array<String> getValues(Level level){
			Array<String> temp = new Array<String>();
			//TODO
			return temp;
		}
		@Override
		public void setup(Level level) {
			// TODO Auto-generated method stub
			
		}
	},
	/**
	 * if any object listed in value1 falls below the screen, lose
	 */
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
		
	},
	/**
	 * if any object of the IDs listed in value1 are destroyed, lose
	 */
	ANY_OBJECTS_OF_ID_DESTROYED_LOSE{//if ANY of these objects are destroyed, lose
		@Override
		public String getXMLDescription() {
			return "objects_of_ID_destroyed_lose";
		}

		@Override
		public Array<String> getValues(Level level) {
			return level.getLoseInfo(ANY_OBJECTS_OF_ID_DESTROYED_LOSE);
		}

		@Override
		public void setup(Level level) {
			// TODO Auto-generated method stub
		}

		@Override
		public void checkLose(Level level) {
			boolean lose = false;
			for(String currentID:level.getLoseInfo(ANY_OBJECTS_OF_ID_DESTROYED_LOSE).get(0).split(",")){
				if(level.getObjectOfID(Integer.parseInt(currentID)) == null){//if any of the objects have been destroyed, lose
					lose = true;
					break;
				}
			}
			if(lose) System.out.println("LOSE");
			level.setCompleted(lose);
			level.setWon(!lose);
		}
	},
	/**
	 * if any objects of ID listed in value1 collide with the left side of the screen, lose(used in PONG)
	 */
	COLLIDES_WITH_LEFT_LOSE{

		@Override
		public String getXMLDescription() {
			return "collides_with_right_win";
		}

		@Override
		public void checkLose(Level level) {
			for(String currentID:level.getLoseInfo(COLLIDES_WITH_LEFT_LOSE)){
				if(level.getObjectOfID(Integer.parseInt(currentID)).getX() <= 0){
					level.setCompleted(true);
					level.setWon(false);
				}
			}
		}

		@Override
		public Array<String> getValues(Level level) {
			return level.getLoseInfo(COLLIDES_WITH_LEFT_LOSE);
		}

		@Override
		public void setup(Level level) {
			String[] temp = level.getLoseInfo(COLLIDES_WITH_LEFT_LOSE).get(0).split(",");
			level.getLoseInfo(COLLIDES_WITH_LEFT_LOSE).set(0, temp[0]);
			for(int i = 1;i<temp.length;i++)
				level.getLoseInfo(COLLIDES_WITH_LEFT_LOSE).add(temp[i]);
		}
	};
	public abstract String getXMLDescription();
	public abstract Array<String> getValues(Level level);
	public abstract void checkLose(Level level);
	public abstract void setup(Level level);
	
	public static LoseEnum newType(String type){
		return valueOf(type.toUpperCase());
	}
}
