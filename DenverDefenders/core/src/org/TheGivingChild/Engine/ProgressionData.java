package org.TheGivingChild.Engine;

import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ObjectMap;

// This class stores all info for what is currently unlocked in the game
public class ProgressionData {
	// Number of mazes unlocked
	private int totsLevelsUnlocked;
	private int kidsLevelsUnlocked;
	
	// Maps the level to beat to the powerup that is unlocked
	private ObjectMap<Integer, String> kidsPowerUps;
	private ObjectMap<Integer, String> totsPowerUps;
	
	// Array of powerups that have been unlocked
	private Array<String> kidsUnlockedPowerUps;
	private Array<String> totsUnlockedPowerUps;
	
	public ProgressionData() {
		totsLevelsUnlocked = 1;
		kidsLevelsUnlocked = 1;
		
		// Construct the map that has powerups
		// Might be better to read from an init file at app start
		kidsPowerUps = new ObjectMap<Integer, String>();
		totsPowerUps = new ObjectMap<Integer, String>();
		// Level one beat unlocks...
		kidsPowerUps.put(1, "mask");
		totsPowerUps.put(1, "mask");
		
		// Array of unlocked powers
		kidsUnlockedPowerUps = new Array<String>();
		totsUnlockedPowerUps = new Array<String>();
		
		// DEBUG, place unlocked powers
		kidsUnlockedPowerUps.add("mask");
		totsUnlockedPowerUps.add("mask");
	}
	
	public int getNumberLevelsUnlocked(String totsOrKids) {
		if (totsOrKids.equals("tots")) {
			return totsLevelsUnlocked;
		} else return kidsLevelsUnlocked;
	}
	
	public Array<String> getUnlockedPowerUps(String totsOrKids) {
		if (totsOrKids.equals("tots")) {
			return totsUnlockedPowerUps;
		} else return kidsUnlockedPowerUps;
	}
	
	// Unlocks a new level if highest level unlocked was beaten
	// Checks powerups unlocked as well
	public boolean unlockLevelCheck(int mazeNumberBeat, String totsOrKids) {
		if (totsOrKids.equals("tots")) {
			if (mazeNumberBeat >= totsLevelsUnlocked) {
				totsLevelsUnlocked++;
				return unlockPowerUpCheck(mazeNumberBeat, totsOrKids);
			} else return false;
		} else {
			if (mazeNumberBeat >= kidsLevelsUnlocked) {
				kidsLevelsUnlocked++;
				return unlockPowerUpCheck(mazeNumberBeat, totsOrKids);
			} else return false;
		}
	}
	
	// Checks if the level beaten unlocked a powerup
	public boolean unlockPowerUpCheck(int mazeNumberBeat, String totsOrKids) {
		Array<String> powerUps;
		ObjectMap<Integer, String> powerUpMap;
		// Get correct data structures
		if (totsOrKids.equals("tots")) {
			powerUps = totsUnlockedPowerUps;
			powerUpMap = totsPowerUps;
		} else {
			powerUps = kidsUnlockedPowerUps;
			powerUpMap = kidsPowerUps;
		}
		// Check for unlock
		if (powerUpMap.containsKey(mazeNumberBeat)) {
			// Add powerup
			powerUps.add(powerUpMap.get(mazeNumberBeat));
			return true;
		} else return false;
	}
}
