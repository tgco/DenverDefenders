package org.TheGivingChild.Engine.PowerUps;

import org.TheGivingChild.Screens.ScreenMaze;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

// Draws or moves items on the maze/mazescreen every frame when active.
// Extend and add to enum to make a new power
public interface PowerUp {
	// Runs every frame when this power up is active, draw with batch, modify objects with the screen
	// Return true if this power up is finished and should be removed
	public boolean update(ScreenMaze mazeScreen, SpriteBatch batch);
}
