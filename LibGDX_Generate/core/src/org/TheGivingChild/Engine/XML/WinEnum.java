package org.TheGivingChild.Engine.XML;

import org.TheGivingChild.Engine.MinigameClock;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;

public enum WinEnum {//Cannot have any labels in common with LoseEnum
	
	TIMEOUT{//completely broken, code has been completely changed since this was added in
		public String getXMLDescription(){
			return "timeout";
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
	},
	ALL_OBJECTS_DESTROYED{
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
				level.setWon(true);
			}
		}
		@Override
		public Array<String> getValues(Level level) {
			return new Array<String>();
		}		
	},
	OBJECTS_OF_ID_DESTROYED{
		@Override
		public String getXMLDescription() {
			return "objects_of_ID_destroyed";
		}

		@Override
		public void checkWin(Level level) {
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

		@Override
		public Array<String> getValues(Level level) {
			return level.getWinInfo(OBJECTS_OF_ID_DESTROYED);
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
	
	public static WinEnum newType(String type){
		return valueOf(type.toUpperCase());
	}
	
}
