package org.TheGivingChild.Engine.PowerUps;

import org.TheGivingChild.Engine.Maze.Direction;
import org.TheGivingChild.Engine.Maze.Maze;
import org.TheGivingChild.Engine.Maze.PlayerSprite;
import org.TheGivingChild.Engine.Maze.Vertex;
import org.TheGivingChild.Engine.Maze.Movement.InputMoveModule;
import org.TheGivingChild.Engine.Maze.Movement.PathMoveModule;
import org.TheGivingChild.Screens.ScreenMaze;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Array;

// BFS to find path to headquarters and set player's move module to path following
public class BicyclePowerUp implements PowerUp {
	// States for the powerup
	private enum SearchState { SEARCHING, FOLLOWING; }
	private SearchState state = SearchState.SEARCHING;

	@Override
	public boolean update(ScreenMaze mazeScreen, SpriteBatch batch) {
		PlayerSprite player = mazeScreen.getPlayerCharacter();
		switch (state) {
		case SEARCHING:
			// Find and construct the path, set to auto move
			player.setMoveModule(buildPathModule(player, mazeScreen.getMaze()));
			// Speed increase
			player.setSpeed(1.5f * player.getSpeed());
			state = SearchState.FOLLOWING;
			break;
		case FOLLOWING:
			// True if path is done, set player move to input based again
			if (pathOverCheck(player, mazeScreen.getMaze())) {
				player.setMoveModule(new InputMoveModule());
				player.setMoveDirection(Direction.DOWN);
				player.setTargetDirection(Direction.DOWN);
				player.setTarget(mazeScreen.getMaze().getTileAt(player.getX(), player.getY()));
				player.setCurrentWalkSequence(Direction.DOWN);
				// Reset speed
				player.setSpeed(2f/3f * player.getSpeed());
				return true;
			}
			break;
		}
		return false;
	}

	// BFS to find path and construct a path move module
	private PathMoveModule buildPathModule(PlayerSprite player, Maze maze) {
		// BFS from hq to player
		maze.bfSearch(maze.getHeroHQTile(), maze.getTileAt(player.getX(), player.getY()));
		// Build array from BFS tree
		Array<Direction> path = new Array<Direction>();
		Vertex currentTile = maze.getTileAt(player.getX(), player.getY());
		while (currentTile.getParent() != null) {
			path.add(currentTile.getParent());
			currentTile = maze.getTileRelativeTo(currentTile, currentTile.getParent());
		}
		// Construct move module
		PathMoveModule mod = new PathMoveModule();
		mod.setPath(path);

		return mod;
	}

	// Checks for collision with the destination, returns true if collision
	private boolean pathOverCheck(PlayerSprite player, Maze maze) {
		Vertex hq = maze.getHeroHQTile();
		if (player.getX() == hq.getX() && player.getY() == hq.getY())
			return true;
		return false;
	}
}
