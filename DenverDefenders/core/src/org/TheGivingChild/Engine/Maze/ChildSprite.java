package org.TheGivingChild.Engine.Maze;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
/**
 * {@link ChildSprite} controls the behavior for the children within the maze.
 * The main player sprite within the maze is also a ChildSprite, but never utilizes the followSprite method, and has it's own logic for movement within {@link org.TheGivingChild.Screens.ScreenMaze ScreenMaze}
 * @author Jack Wesley Nelson, Walter Schlosser
 */
public class ChildSprite extends Sprite {
	/**{@link #saved} is a boolean to keep track of whether the sprite has been saved at the headquarters.*/
	private boolean saved;
	// True if this child has begun following the main character
	private boolean follow;
	// Speed to move
	private float speed;

	/**
	 * {@link #ChildSprite(Texture)} is the constructor for {@link ChildSprite}.
	 * @param childTexture The texture that the sprite will draw.
	 */
	public ChildSprite(Texture childTexture) {
		super(childTexture);
		saved = false;
		speed = 0;
		follow = false;
	}
	/**
	 * Moves the {@link ChildSprite} to the position of the {@link Vertex} passed in.
	 * @param tile contains the new position coordinates.
	 */
	public void moveTo(Vertex tile) {
		this.setPosition(tile.getX(), tile.getY());
	}

	// Causes this sprite to follow the passed sprite along the maze path
	public void followSprite(Sprite followMe, Maze maze){
		// Find the path to the target sprite
		Vertex src = maze.getTileAt(followMe.getX(), followMe.getY());
		Vertex dest = maze.getTileAt(this.getX(), this.getY());
		if (src == dest) return;
		// Builds src -> dest tree
		maze.bfSearch(dest, src);
		// Find adjacent tile
		Vertex moveHere = maze.getTileRelativeTo(src, src.getParent());
		// Build dest -> src tree
		maze.bfSearch(src, dest);
		float spriteMoveX = 0;
		float spriteMoveY = 0;
		float delta = 0;
		// Determine target
		Vertex myTile = maze.getTileAt(this.getX(), this.getY());
		Vertex target;
		if (myTile.getX() != getX() || myTile.getY() != getY()) {
			// Make sure snapped to a tile first before finding a new target
			target = myTile;
		}
		else target = maze.getTileRelativeTo(dest, dest.getParent());
		boolean reached = false;
		switch(dest.getParent()) {
		case UP:
			spriteMoveY = speed*Gdx.graphics.getDeltaTime();
			delta = target.getY() - getY();
			if (Math.abs(delta) < Math.abs(spriteMoveY)) reached = true;
			break;
		case DOWN:
			spriteMoveY = -speed*Gdx.graphics.getDeltaTime();
			delta = target.getY() - getY();
			if (Math.abs(delta) < Math.abs(spriteMoveY)) reached = true;
			break;
		case RIGHT:
			spriteMoveX = speed*Gdx.graphics.getDeltaTime();
			delta = target.getX() - getX();
			if (Math.abs(delta) < Math.abs(spriteMoveX)) reached = true;
			break;
		case LEFT:
			spriteMoveX = -speed*Gdx.graphics.getDeltaTime();
			delta = target.getX() - getX();
			if (Math.abs(delta) < Math.abs(spriteMoveX)) reached = true;
			break;
		}
		
		if (reached) {
			// snap to targ
			this.setX(target.getX());
			this.setY(target.getY());
		}
		else {
			// Move towards targ
			this.setX(this.getX() + spriteMoveX);
			this.setY(this.getY() + spriteMoveY);
		}
	}

	public void setSpeed(float f)
	{
		speed = f;
	}

	public float getSpeed()
	{
		return speed;
	}

	public boolean getSaved() {
		return saved;
	}

	public void setSaved(boolean isSaved) {
		saved = isSaved;
	}

	public void setFollow(boolean b) {
		this.follow = b;
	}
	public boolean getFollow() {
		return this.follow;
	}
	/**
	 * returns the scaled width.
	 */
	@Override
	public float getWidth() {
		return super.getWidth()*getScaleX();
	}
	/**
	 * returns the scaled height.
	 */
	@Override
	public float getHeight() {
		return super.getHeight()*getScaleY();
	}
}
