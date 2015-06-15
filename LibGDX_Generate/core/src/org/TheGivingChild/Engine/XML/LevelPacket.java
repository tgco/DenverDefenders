package org.TheGivingChild.Engine.XML;

import java.util.Iterator;

import com.badlogic.gdx.utils.Array;

public class LevelPacket implements Iterable<Level>{
	private String packetName;
	private Array<Level> levels;
	
	public LevelPacket(String name){
		packetName = name;
		levels = new Array<Level>();
	}
	
	public void addLevel(Level level){
		levels.add(level);
	}
	public Array<Level> getLevels(){
		return levels;
	}
	public String getPacketName(){
		return packetName;
	}
	
	/**
	 * Iterates through all of the levels that the LevelPacket has and checks if they are all completed
	 * 
	 * @return True if all of the levels are completed and False if not
	 */ 
	public boolean allCompleted() {
		for (Level level: levels) {
			if(!level.getCompleted()) 
				return false;
		}
		return true;
	}
	
	//added to be able to iterate over the levels in the packet
	@Override
	public Iterator<Level> iterator() {
		return levels.iterator();
	}
	
}
