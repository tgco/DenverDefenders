package org.TheGivingChild.Engine.Maze.Movement;

import org.TheGivingChild.Engine.Maze.Direction;
import org.TheGivingChild.Engine.Maze.Maze;
import org.TheGivingChild.Engine.Maze.PlayerSprite;
import org.TheGivingChild.Engine.Maze.Vertex;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.Array;

// Moves according to a list of directions
public class PathMoveModule implements MoveModule {
	// Current move direction.
	private Direction moveDirection;
	// List of directions to follow
	private Array<Direction> path;
	// Tile to move towards
	private Vertex target;
	
	@Override
	public boolean move(PlayerSprite sprite, Maze maze) {
		// initial targ set and path out of directions
		if (moveDirection == null) {
			if (path.size > 0) {
				moveDirection = path.removeIndex(0);
				sprite.setCurrentWalkSequence(moveDirection);
				target = maze.getTileRelativeTo( maze.getTileAt(sprite.getX(), sprite.getY()), moveDirection);
			} else return false; // nowhere else to move
		}
		
		// Find distance to move
		float spriteMoveX = 0;
		float spriteMoveY = 0;
		float delta = 0;
		boolean reached = false;
		switch(moveDirection) {
		case UP:
			spriteMoveY = sprite.getSpeed()*Gdx.graphics.getDeltaTime();
			delta = target.getY() - sprite.getY();
			if (Math.abs(delta) < Math.abs(spriteMoveY)) reached = true;
			break;
		case DOWN:
			spriteMoveY = -sprite.getSpeed()*Gdx.graphics.getDeltaTime();
			delta = target.getY() - sprite.getY();
			if (Math.abs(delta) < Math.abs(spriteMoveY)) reached = true;
			break;
		case RIGHT:
			spriteMoveX = sprite.getSpeed()*Gdx.graphics.getDeltaTime();
			delta = target.getX() - sprite.getX();
			if (Math.abs(delta) < Math.abs(spriteMoveX)) reached = true;
			break;
		case LEFT:
			spriteMoveX = -sprite.getSpeed()*Gdx.graphics.getDeltaTime();
			delta = target.getX() - sprite.getX();
			if (Math.abs(delta) < Math.abs(spriteMoveX)) reached = true;
			break;
		}
		
		if (reached) {
			if (path.size > 0) {
				if (moveDirection != path.get(0)) {
					sprite.setCurrentWalkSequence(path.get(0));
					//snap to targ to allow direction change
					sprite.setX(target.getX());
					sprite.setY(target.getY());
				} else {
					// move
					sprite.setX(sprite.getX() + spriteMoveX);
					sprite.setY(sprite.getY() + spriteMoveY);
				}
				moveDirection = path.removeIndex(0);
				// find new target
				target = maze.getTileRelativeTo(target, moveDirection);
			} else {
				// snap to final target
				sprite.setX(target.getX());
				sprite.setY(target.getY());
				// null to signify path has ended
				moveDirection = null;
			}
		} else {
			sprite.setX(sprite.getX() + spriteMoveX);
			sprite.setY(sprite.getY() + spriteMoveY);
		}

		return true;
	}
	
	// Sets the path to follow automatically
	public void setPath(Array<Direction> path) {
		this.path = path;
	}

	@Override
	public void setMoveDirection(Direction dir) {
		// Squelch, this module auto moves
	}

	@Override
	public void setTargetDirection(Direction dir) {
		// Squelch, this module auto moves
	}

	@Override
	public void setTarget(Vertex target) {
		// Squelch, this module auto moves
	}

}
