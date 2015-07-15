package org.TheGivingChild.Engine.Maze.Movement;

import org.TheGivingChild.Engine.Maze.Direction;
import org.TheGivingChild.Engine.Maze.Maze;
import org.TheGivingChild.Engine.Maze.PlayerSprite;
import org.TheGivingChild.Engine.Maze.Vertex;

import com.badlogic.gdx.Gdx;

// Moves towards a target.  New target direction can be set at any time.
public class InputMoveModule implements MoveModule {
	// The direction the player is moving and the direction the player wants to move
	private Direction moveDirection;
	private Direction targetDirection;
	// The target tile the character is moving to
	private Vertex target;

	@Override
	public boolean move(PlayerSprite sprite, Maze maze) {
		boolean moved = false;
		// Calculate amount to move in a frame
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

		// Update position
		if (reached) {
			// New direction check
			if (targetDirection != moveDirection) {
				Vertex next = maze.getTileRelativeTo(target, targetDirection);
				if (next != null) {
					// snap to target and move in new direction
					sprite.setPosition(target.getX(), target.getY());
					target = next;
					moveDirection = targetDirection;
					sprite.setCurrentWalkSequence(targetDirection);
				} else {
					// can't go in target direction yet, continue in move direction
					next = maze.getTileRelativeTo(target, moveDirection);
					if (next != null) {
						target = next;
						sprite.setPosition(sprite.getX() + spriteMoveX, sprite.getY() + spriteMoveY);
						moved = true;
					} else {
						// No where to go, snap to target
						sprite.setPosition(target.getX(), target.getY());
					}
				}
			} else {
				// REFACTOR THESE IF ELSE BLOCKS, CODE IS COPIED BETWEEN THEM
				// Reached target, try to continue in same direction or just snap to target
				Vertex next = maze.getTileRelativeTo(target, moveDirection);
				if (next != null) {
					target = next;
					sprite.setPosition(sprite.getX() + spriteMoveX, sprite.getY() + spriteMoveY);
					moved = true;
				} else {
					// No where to go, snap to target
					sprite.setPosition(target.getX(), target.getY());
				}
			}
		} else {
			// Haven't reached target yet, move toward it
			moved = true;
			sprite.setPosition(sprite.getX() + spriteMoveX, sprite.getY() + spriteMoveY);
		}

		return moved;
	}

	@Override
	public void setMoveDirection(Direction dir) {
		// Allow, this module can respond to input
		this.moveDirection = dir;
	}

	@Override
	public void setTargetDirection(Direction dir) {
		// Allow, this module can respond to input
		this.targetDirection = dir;
	}

	@Override
	public void setTarget(Vertex target) {
		// Allow, this module can respond to input
		this.target = target;
	}

}
