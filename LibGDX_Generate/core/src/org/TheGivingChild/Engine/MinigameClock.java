package org.TheGivingChild.Engine;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.TimeUtils;

//GameClock for determining various timing things
//GameClock measured in nanoseconds
/**
 * Serves as the gameclock for the minigames
 * static clock is only created once
 * @author mtzimour
 *
 */

public final class  MinigameClock {

	private long levelLength;				//Time remaining in level
	private boolean outOfTime = false;
	
	private static MinigameClock clock;
	
	/**
	 * Gets the static instance of gameclock if it has already been created
	 * creates a new instance if there is no clock yet
	 * @return returns the static clock or a new one
	 */
	
	public static MinigameClock getInstance()
	{
		if (null == clock){
			clock = new MinigameClock();
		}
		
		return clock;
	
	}
	
	/**
	 * Sets the length of time allowed to complete a minigame
	 * @param time Level time allowed in seconds
	 */
	
	public void setLevelLength(long time)
	{
		levelLength = time * 1000000000;
		outOfTime = false;
	}
		
	/**
	 * decrements the level time remaining by time since the last frame
	 * Checks if level is out of time and raises a flag if it is
	 */
	public void render()
	{
		levelLength = (long) (levelLength - Gdx.graphics.getDeltaTime()*1000000000);
				
		if(levelLength <=0)
		{
			outOfTime = true;
		}
		
	}
		
	/**
	 * Gets the time remaining in the level
	 * @return total level time remaining in nanoseconds
	 */
	public long getLevelTime()
	{
		return levelLength;
	}
	
	/**
	 * Returns true if level is out of time, or false if level still has time remaining
	 * @return if there is still time remaining or not
	 */
	public boolean outOfTime()
	{
		return outOfTime;
	}
	
	public long getLevelTimeInSeconds(){
		return levelLength/1000000000;
	}
	
	public String toString()
	{
		
		return String.format("%2d", (levelLength % 1000000000));
		//return levelLength % 1000000000;
		//return getLevelTimeInSeconds() + "." + String.format("%02d",levelLength % 1000000000) + " seconds remaining.";
	}
	
	
	
	
	
	
	
	
}
