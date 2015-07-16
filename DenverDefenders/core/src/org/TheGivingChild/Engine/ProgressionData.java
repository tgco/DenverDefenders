package org.TheGivingChild.Engine;

import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ObjectMap;

// Stores all info for what is currently unlocked in the game.  Saved and loaded to maintain player progress.
// Author: Walter Schlosser
public class ProgressionData {
	// Number of mazes unlocked
	private int totsLevelsUnlocked;
	private int kidsLevelsUnlocked;
	
	// Maps the level to the power that is unlocked when said level is beat
	private ObjectMap<Integer, String> kidsPowerUps;
	private ObjectMap<Integer, String> totsPowerUps;
	
	// Array of powerup names that have been unlocked already
	private Array<String> kidsUnlockedPowerUps;
	private Array<String> totsUnlockedPowerUps;
	
	public ProgressionData() {
		totsLevelsUnlocked = 1;
		kidsLevelsUnlocked = 1;
		
		// Construct the map that has powerup unlock info
		// Might be better to read from an init file at app start
		kidsPowerUps = new ObjectMap<Integer, String>();
		totsPowerUps = new ObjectMap<Integer, String>();
		// Level one beat unlocks...
		kidsPowerUps.put(1, "mask");
		totsPowerUps.put(1, "mask");
		
		kidsPowerUps.put(2, "cape");
		totsPowerUps.put(2, "cape");
		
		kidsPowerUps.put(3, "bicycle");
		totsPowerUps.put(3, "bicycle");
		
		kidsPowerUps.put(4, "backpack");
		kidsPowerUps.put(4, "backpack");
		
		// Array of unlocked powers
		kidsUnlockedPowerUps = new Array<String>();
		totsUnlockedPowerUps = new Array<String>();
		
		// FOR TESTING, SET ALL TO UNLOCKED
		kidsUnlockedPowerUps.add("mask");
		totsUnlockedPowerUps.add("mask");
		kidsUnlockedPowerUps.add("cape");
		totsUnlockedPowerUps.add("cape");
		kidsUnlockedPowerUps.add("bicycle");
		totsUnlockedPowerUps.add("bicycle");
		kidsUnlockedPowerUps.add("backpack");
		totsUnlockedPowerUps.add("backpack");
	}
	
	// Returns the number of unlocked levels for the passed mode, "kids" or "tots"
	public int getNumberLevelsUnlocked(String totsOrKids) {
		if (totsOrKids.equals("tots")) {
			return totsLevelsUnlocked;
		} else return kidsLevelsUnlocked;
	}
	
	// Returns array of unlocked power names for the passed mode, "kids" or "tots"
	public Array<String> getUnlockedPowerUps(String totsOrKids) {
		if (totsOrKids.equals("tots")) {
			return totsUnlockedPowerUps;
		} else return kidsUnlockedPowerUps;
	}
	
	// Unlocks a new level and power if highest level previously unlocked was beaten
	// Returns true if a power was unlocked
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
	
	// Returns true and adds the unlocked power to the array of powers unlocked if the passed maze number beat
	// unlocks a power
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
