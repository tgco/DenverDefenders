package org.TheGivingChild.Engine;

import com.badlogic.gdx.Gdx;

/**
 * Serves as the gameclock for the minigames
 * Static clock is only created once.
 *<p>
 *-Final to avoid inheritance
 *</p>
 * @author mtzimour
 */

public final class  MinigameClock {
	/**Time remaining in level s */
	private double levelLength;
	/**Boolean to keep track of whether time remains*/
	private boolean outOfTime = false;
	
	private static MinigameClock clock;
	
	/**
	 * Gets the static instance of gameclock if it has already been created.
	 * Creates a new instance if there is no clock yet.
	 * @return returns the static clock or a new one.
	 */
	public static MinigameClock getInstance() {
		if (null == clock){
			clock = new MinigameClock();
		}
		return clock;
	}
	
	/**
	 * Sets the length of time allowed to complete a minigame.
	 * @param time Level time allowed in seconds.
	 */
	public void setLevelLength(double time) {
		levelLength = time;
		outOfTime = false;
	}
		
	/**
	 * Decrements the level time remaining by time since the last frame.
	 * Checks if level is out of time and raises a flag if it is.
	 */
	public void progress() {
		levelLength -= Gdx.graphics.getDeltaTime();
				
		if(levelLength <= 0) {
			outOfTime = true;
		}
		
	}
		
	/**
	 * Gets the time remaining in the level.
	 * @return total Level time remaining in seconds.
	 */
	public double getLevelTime() {
		return levelLength;
	}
	
	/**
	 * Returns true if level is out of time, or false if level still has time remaining.
	 * @return If there is still time remaining or not.
	 */
	public boolean outOfTime() {
		return outOfTime;
	}

	/**
	 * Gets the time remaining in the level.
	 * @return total Level time remaining in milliseconds.
	 */
	public String toString() {
		String time = String.format("%.2f", getLevelTime());
		return time + " seconds remaining.";
	}

}
