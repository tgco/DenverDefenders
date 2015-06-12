package org.TheGivingChild.Engine;

import com.badlogic.gdx.Gdx;

//GameClock for determining various timing things
//GameClock measured in nanoseconds
/**
 * Serves as the gameclock for the minigames
 * Static clock is only created once.
 *<p>
 *-Final to avoid inheritance, static so only one instance is referred to.
 *</p>
 * @author mtzimour
 */

public final class  MinigameClock {
	/**Time remaining in level */
	private long levelLength;
	/**Boolean to keep track of whether time remains*/
	private boolean outOfTime = false;
	
	private static MinigameClock clock;
	
	/**
	 * Gets the static instance of gameclock if it has already been created.
	 * Creates a new instance if there is no clock yet.
	 * @return returns the static clock or a new one.
	 */
	public static MinigameClock getInstance()
	{
		if (null == clock){
			clock = new MinigameClock();
		}
		
		return clock;
	
	}
	
	/**
	 * Sets the length of time allowed to complete a minigame.
	 * @param time Level time allowed in seconds.
	 */
	public void setLevelLength(long time)
	{
		levelLength = time * 1000000000;
		outOfTime = false;
	}
		
	/**
	 * Decrements the level time remaining by time since the last frame.
	 * Checks if level is out of time and raises a flag if it is.
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
	 * Gets the time remaining in the level.
	 * @return total Level time remaining in nanoseconds.
	 */
	public long getLevelTime()
	{
		return levelLength;
	}
	
	/**
	 * Returns true if level is out of time, or false if level still has time remaining.
	 * @return If there is still time remaining or not.
	 */
	public boolean outOfTime()
	{
		return outOfTime;
	}
	/**
	 * Gets the time remaining in the level.
	 * @return total Level time remaining in seconds.
	 */
	public long getLevelTimeInSeconds(){
		return levelLength/1000000000;
	}
	/**
	 * Gets the time remaining in the level.
	 * @return total Level time remaining in milliseconds.
	 */
	public String toString()
	{
		String millis = String.format("%2d", (levelLength % 1000000000)).substring(0, 2);
		return getLevelTimeInSeconds() + "." + millis + " seconds remaining.";
	}
	
	
	
	
	
	
	
	
}
