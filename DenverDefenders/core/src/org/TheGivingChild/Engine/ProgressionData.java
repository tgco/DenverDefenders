package org.TheGivingChild.Engine;

// This class stores all info for what is currently unlocked in the game
public class ProgressionData {
	// Number of mazes unlocked
	private int totsLevelsUnlocked;
	private int kidsLevelsUnlocked;
	
	public ProgressionData() {
		totsLevelsUnlocked = 1;
		kidsLevelsUnlocked = 1;
	}
	
	public int getNumberLevelsUnlocked(String totsOrKids) {
		if (totsOrKids.equals("tots")) {
			return totsLevelsUnlocked;
		} else return kidsLevelsUnlocked;
	}
	
	// Unlocks a new level if highest level unlocked was beaten
	public boolean unlockLevelCheck(int mazeNumberBeat, String totsOrKids) {
		if (totsOrKids.equals("tots")) {
			if (mazeNumberBeat >= totsLevelsUnlocked) {
				totsLevelsUnlocked++;
				return true;
			} else return false;
		} else {
			if (mazeNumberBeat >= kidsLevelsUnlocked) {
				kidsLevelsUnlocked++;
				return true;
			} else return false;
		}
	}
}
