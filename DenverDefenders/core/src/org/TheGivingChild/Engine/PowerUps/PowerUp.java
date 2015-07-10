package org.TheGivingChild.Engine.PowerUps;

import org.TheGivingChild.Screens.ScreenMaze;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

// Draws or moves items on the maze/mazescreen every frame when active.
// Extend and add to enum to make a new powerup
public abstract class PowerUp {
	
	// Runs every frame when this powerup is active, draw with batch, modify objects with the screen
	// Return true if this powerup is finished and should be removed
	public abstract boolean update(ScreenMaze mazeScreen, SpriteBatch batch);
}
