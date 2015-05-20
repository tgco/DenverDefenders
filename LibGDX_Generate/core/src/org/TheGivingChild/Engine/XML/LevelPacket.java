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
	
	public void addLevel(Level l){
		levels.add(l);
	}
	public Array<Level> getLevels(){
		return levels;
	}
	public String getPacketName(){
		return packetName;
	}

	//added to be able to iterate over the levels in the packet
	@Override
	public Iterator<Level> iterator() {
		return levels.iterator();
	}
	
}
