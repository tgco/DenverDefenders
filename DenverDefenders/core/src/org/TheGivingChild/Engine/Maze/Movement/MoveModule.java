package org.TheGivingChild.Engine.Maze.Movement;

import org.TheGivingChild.Engine.Maze.Direction;
import org.TheGivingChild.Engine.Maze.Maze;
import org.TheGivingChild.Engine.Maze.PlayerSprite;
import org.TheGivingChild.Engine.Maze.Vertex;

// A move module contains logic for how to move a sprite
public interface MoveModule {
	// Moves the passed sprite through the maze tiles
	// Return true if the sprite moves successfully
	public boolean move(PlayerSprite sprite, Maze maze);
	// Respond to a request to change the current move direction
	public void setMoveDirection(Direction dir);
	// Respond to a request to change the target direction
	public void setTargetDirection(Direction dir);
	// Respond to a request to set the current target tile
	public void setTarget(Vertex target);
}
