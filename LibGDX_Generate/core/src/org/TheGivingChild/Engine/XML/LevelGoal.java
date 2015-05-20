package org.TheGivingChild.Engine.XML;

public class LevelGoal {
	boolean goalReached = false;
	public boolean getGoalState(){
		return goalReached;
	}
	public void setGoalFinished(){
		goalReached = true;
	}
}
