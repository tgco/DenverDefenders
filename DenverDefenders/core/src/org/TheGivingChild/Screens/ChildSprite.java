package org.TheGivingChild.Screens;

import java.util.Deque;
import java.util.LinkedList;

import org.TheGivingChild.Engine.Maze.Maze;
import org.TheGivingChild.Engine.Maze.Vertex;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
/**
 * {@link ChildSprite} controls the behavior for the children within the maze.
 * The main player sprite within the maze is also a ChildSprite, but never utilizes the followSprite method, and has it's own logic for movement within {@link org.TheGivingChild.Screens.ScreenMaze ScreenMaze}
 * @author Jack Wesley Nelson
 */
public class ChildSprite extends Sprite {
	/**{@link #saved} is a boolean to keep track of whether the sprite has been saved.*/
	private boolean saved;
	// True if this child has begun following
	private boolean follow;
	/**{@link #moveSpeed} keeps track of how fast objects should move around the screen.*/
	private float moveSpeed;
	/**
	 * {@link #ChildSprite(Texture)} is the constructor for {@link ChildSprite}.
	 * @param childTexture The texture that the sprite will draw.
	 */
	public ChildSprite(Texture childTexture) {
		super(childTexture);;
		saved = false;
		moveSpeed = 0;
		follow = false;
	}
	/**
	 * Moves the {@link ChildSprite} to the position of the {@link MinigameRectangle} passed in.
	 * @param rect contains the new position coordinates.
	 */
	public void moveTo(Vertex tile) {
		this.setPosition(tile.getX(), tile.getY());
	}

	// Sets this sprite one tile away from the passed sprite
	public void followSprite(ChildSprite followMe, Maze maze){
		// BFS to find the sprite
		Vertex dest = maze.getTileAt(followMe.getX(), followMe.getY());
		Vertex src = maze.getTileAt(this.getX(), this.getY());
		// Same tile check
		if (dest == src) return;
		
		maze.bfSearch(src, dest);
		// count distance to dest
		int count = 1;
		Vertex at = dest;
		while (maze.getTileRelativeTo(at, at.getParent()) != src) {
			at = maze.getTileRelativeTo(at, at.getParent());
			count++;
		}
		
		// place one tile away in the direction of the discovery with same offset from corner as the sprite to follow
		Vertex placeAt = maze.getTileRelativeTo(dest, dest.getParent());
		float offsetX = 0;
		float offsetY = 0;
		switch(dest.getParent()) {
		case UP:
			offsetY = followMe.getY() - dest.getY();
			break;
		case DOWN:
			offsetY = followMe.getY() - dest.getY();
			break;
		case RIGHT:
			offsetX = followMe.getX() - dest.getX();
			break;
		case LEFT:
			offsetX = followMe.getX() - dest.getX();
			break;
		}
		
		// Linearly interpolate to the position
		//Vector2 pos = new Vector2(this.getX(), this.getY());
		//pos.lerp(new Vector2(placeAt.getX() + offsetX, placeAt.getY() + offsetY), Gdx.graphics.getDeltaTime());
		this.setX(placeAt.getX() + offsetX);
		this.setY(placeAt.getY() + offsetY);
	}
	/**
	 * Sets the {@link #moveSpeed}.
	 * @param f {@link #moveSpeed} will be set to this.
	 */
	public void setSpeed(float f)
	{
		moveSpeed = f;
	}
	/**
	 * returns {@link #moveSpeed}.
	 * @return {@link #saved}
	 */
	public float getSpeed()
	{
		return moveSpeed;
	}
	/**
	 * returns {@link #saved}.
	 * @return {@link #saved}
	 */
	public boolean getSaved() {
		return saved;
	}
	/**
	 * sets {@link #saved} to the boolean passed in.
	 * Sets follow to false.
	 * @param isSaved {@link #saved} is set to this.
	 */
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
