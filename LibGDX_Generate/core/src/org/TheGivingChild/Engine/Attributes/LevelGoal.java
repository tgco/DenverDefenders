package org.TheGivingChild.Engine.Attributes;

public class LevelGoal {
	boolean goalReached = false;
	public boolean getGoalState(){
		return goalReached;
	}
	public void setGoalFinished(){
		goalReached = true;
	}
}
