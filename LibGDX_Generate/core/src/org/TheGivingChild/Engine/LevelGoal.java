package org.TheGivingChild.Engine;

public class LevelGoal {
	boolean goalReached;
	public void setGoalFinished(){
		goalReached = true;
	}
	public boolean getGoalState(){
		return goalReached;
	}
}
