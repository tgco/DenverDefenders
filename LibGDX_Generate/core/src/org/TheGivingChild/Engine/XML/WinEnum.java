package org.TheGivingChild.Engine.XML;

import org.TheGivingChild.Engine.MinigameClock;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;

public enum WinEnum {//Cannot have any labels in common with LoseEnum
	
	TIMEOUT_WIN{//completely broken, code has been completely changed since this was added in
		public String getXMLDescription(){
			return "timeout_win";
		}
		public Array<String> getValues(Level level){
			Array<String> temp = new Array<String>();
			//temp.add(winInfo+"");
			return temp;
		}
		@Override
		public void checkWin(Level level) {
			if(MinigameClock.getInstance().outOfTime()){
				level.setCompleted(true);
				level.setWon(true);
			}
		}
		@Override
		public void setup(Level level) {
			// TODO Auto-generated method stub
			
		}
	},
	COLLISION_WITH_OBJECT_WIN{
		public String getXMLDescription(){
			return "collision_With_Object_Win";
		}
		
		public Array<String> getValues(Level level){
			Array<String> temp = new Array<String>();
			temp.add("ASD");
			return temp;
		}
		@Override
		public void checkWin(Level level) {
			boolean win = true;
			Array<String> myInfo = level.getWinInfo(COLLISION_WITH_OBJECT_WIN);
			GameObject obj1 = level.getGameObjects().get(Integer.parseInt(myInfo.get(0)));
			Rectangle r1 = new Rectangle(obj1.getX(),obj1.getY(),obj1.getTextureWidth(),obj1.getTextureHeight());
			String[] IDList = myInfo.get(1).split(",");
			for(int i = 0; i<IDList.length;i++){
				int currentID = Integer.parseInt(IDList[i]);
				//setup new rectangle with dimensions of target object
				//Rectangle r2 = new Rectangle(level.getGameObjects().get(currentID).getX(),level.getGameObjects().get(currentID).getY(),level.getGameObjects().get(currentID).getTexture().getWidth(),level.getGameObjects().get(currentID).getTexture().getHeight());
				GameObject currentObject = level.getObjectOfID(currentID);//have to look it up each time because objects can be created/deleted, not in a set index.
				Rectangle r2 = new Rectangle(currentObject.getX(),currentObject.getY(),currentObject.getTextureWidth(),currentObject.getTextureHeight());
				if(!r1.overlaps(r2))//if the rectangle does not overlap, win condition has not been satisfied
					win = false;
			}
			level.setCompleted(win);
			level.setWon(win);
		}

		@Override
		public void setup(Level level) {
			// TODO Auto-generated method stub
			
		}
	},
	ALL_OBJECTS_DESTROYED_WIN{
		@Override
		public String getXMLDescription() {
			return "all_objects_destroyed_win";
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
				level.setWon(true);
			}
		}
		@Override
		public Array<String> getValues(Level level) {
			return new Array<String>();
		}
		@Override
		public void setup(Level level) {
			// TODO Auto-generated method stub
			
		}		
	},
	OBJECTS_OF_ID_DESTROYED_WIN{
		@Override
		public String getXMLDescription() {
			return "objects_of_ID_destroyed_win";
		}

		@Override
		public void checkWin(Level level) {
			boolean win = true;
			for(String currentID:level.getWinInfo(OBJECTS_OF_ID_DESTROYED_WIN).get(0).split(",")){
				if(level.getObjectOfID(Integer.parseInt(currentID)) != null){//if the object exists, set win to false, exit loop
					win = false;
					break;
				}
			}
			if(win) System.out.println("WIN");
			level.setCompleted(win);
			level.setWon(win);
		}

		@Override
		public Array<String> getValues(Level level) {
			return level.getWinInfo(OBJECTS_OF_ID_DESTROYED_WIN);
		}

		@Override
		public void setup(Level level) {
			// TODO Auto-generated method stub
		}
	},
	COLLIDES_WITH_RIGHT_WIN{

		@Override
		public String getXMLDescription() {
			return "collides_with_right_win";
		}

		@Override
		public void checkWin(Level level) {
			for(String currentID:level.getWinInfo(COLLIDES_WITH_RIGHT_WIN)){
				GameObject currentObject = level.getObjectOfID(Integer.parseInt(currentID));
				if(currentObject.getX() + currentObject.getTextureWidth() > Gdx.graphics.getWidth()){
					level.setCompleted(true);
					level.setWon(true);
				}
			}
		}

		@Override
		public Array<String> getValues(Level level) {
			return level.getWinInfo(COLLIDES_WITH_RIGHT_WIN);
		}

		@Override
		public void setup(Level level) {
			String[] temp = level.getWinInfo(COLLIDES_WITH_RIGHT_WIN).get(0).split(",");
			level.getWinInfo(COLLIDES_WITH_RIGHT_WIN).set(0, temp[0]);
			for(int i = 1;i<temp.length;i++)
				level.getWinInfo(COLLIDES_WITH_RIGHT_WIN).add(temp[i]);
		}
	};
	/**
	 * @return String containing the level name to write to the .xml file
	 */
	public abstract String getXMLDescription();
	/**
	 * checks if the level has been completed
	 * @param The level to check
	 */
	public abstract void checkWin(Level level);
	/**
	 * @param level
	 * @return Array of Strings to write to the values to the .xml file
	 */
	public abstract Array<String> getValues(Level level);
	
	public abstract void setup(Level level);
	
	public static WinEnum newType(String type){
		System.out.println(type);
		return valueOf(type.toUpperCase());
	}
	
}
