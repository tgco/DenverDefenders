package org.TheGivingChild.Engine.PowerUps;

import org.TheGivingChild.Engine.Maze.ChildSprite;
import org.TheGivingChild.Engine.Maze.Direction;
import org.TheGivingChild.Engine.Maze.Maze;
import org.TheGivingChild.Engine.Maze.PlayerSprite;
import org.TheGivingChild.Engine.Maze.Vertex;
import org.TheGivingChild.Engine.Maze.Movement.InputMoveModule;
import org.TheGivingChild.Engine.Maze.Movement.PathMoveModule;
import org.TheGivingChild.Screens.ScreenMaze;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

public class BackpackPowerUp implements PowerUp {
	// States for the powerup
	private enum SearchState { SEARCHING, FOLLOWING; }
	private SearchState state = SearchState.SEARCHING;
	private Vertex target;

	@Override
	public boolean update(ScreenMaze mazeScreen, SpriteBatch batch) {
		PlayerSprite player = mazeScreen.getPlayerCharacter();
		switch (state) {
		case SEARCHING:
			// Find and construct the path, set to auto move
			PathMoveModule mod= buildPathModule(player, mazeScreen);
			if (mod == null) return true; // done if no closest kid path
			player.setMoveModule(mod);
			state = SearchState.FOLLOWING;
			break;
		case FOLLOWING:
			// If target reached, set player move to input based again
			if (player.getX() == target.getX() && player.getY() == target.getY()) {
				player.setMoveModule(new InputMoveModule());
				player.setMoveDirection(Direction.DOWN);
				player.setTargetDirection(Direction.DOWN);
				player.setTarget(mazeScreen.getMaze().getTileAt(player.getX(), player.getY()));
				player.setCurrentWalkSequence(Direction.DOWN);
				return true;
			}
			break;
		}
		return false;
	}

	// BFS to find path and construct a path move module
	private PathMoveModule buildPathModule(PlayerSprite player, ScreenMaze mazeScreen) {
		//Find closest kid in maze, save reference for easy path finished check later
		target = closestKidTile(player, mazeScreen);
		if (target == null) return null;
		// BFS from closest child to player
		Maze maze = mazeScreen.getMaze();
		maze.bfSearch(target, maze.getTileAt(player.getX(), player.getY()));
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

	// Returns the tile with the closest child or null if no more kids are left
	private Vertex closestKidTile(PlayerSprite player, ScreenMaze mazeScreen) {
		// Find the closest child to the player
		Vector2 playerPos = new Vector2(player.getX(), player.getY());
		ChildSprite closest = null;
		float closestDist = Float.POSITIVE_INFINITY;
		for (ChildSprite cs : mazeScreen.getMazeChildren()) {
			if (!cs.getFollow() && playerPos.cpy().sub(cs.getX(), cs.getY()).len() < closestDist) {
				closest = cs;
				closestDist = playerPos.cpy().sub(cs.getX(), cs.getY()).len();
			}
		}

		// Return if no children left
		if (closest == null) {
			return null;
		}
		
		// Return the tile the kid is on
		return mazeScreen.getMaze().getTileAt(closest.getX(), closest.getY());
	}
}
