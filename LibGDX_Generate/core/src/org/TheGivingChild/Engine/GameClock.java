package org.TheGivingChild.Engine;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.TimeUtils;

//GameClock for determining various timing things
//GameClock measured in nanoseconds
public final class  GameClock {

	private static long gameStartTime;			
	private long totalGameTime;
	private long levelLength;			
	private long currentLevelTimeRemaining;	
	private boolean outOfTime = false;
	
	private static GameClock clock;
	
	public static GameClock getInstance()
	{
		if (null == clock){
			clock = new GameClock();
			gameStartTime = TimeUtils.nanoTime();
		}
		
		return clock;
	
	}
	
	public void setLevelLength(long time)
	{
		levelLength = time * 1000000000;
		outOfTime = false;
	}
		
	public void render()
	{
		totalGameTime = totalGameTime + TimeUtils.timeSinceNanos(totalGameTime);
		levelLength = levelLength - TimeUtils.timeSinceNanos((long) Gdx.graphics.getDeltaTime() * 1000000000);
		
		if(levelLength <=0)
		{
			outOfTime = true;
		}
		
	}
	
	public long getPlayTime()
	{
		return totalGameTime / 1000000000;
	}
	
	public long getLevelTime()
	{
		return levelLength;
	}
	
	public boolean outOfTime()
	{
		return outOfTime;
	}
	
	

	
	
	
	
	
	
	
}
