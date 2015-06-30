package org.TheGivingChild.Screens;

import java.util.Deque;
import java.util.LinkedList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Rectangle;
/**
 * {@link ChildSprite} controls the behavior for the children within the maze.
 * The main player sprite within the maze is also a ChildSprite, but never utilizes the followSprite method, and has it's own logic for movement within {@link org.TheGivingChild.Screens.ScreenMaze ScreenMaze}
 * @author Jack Wesley Nelson
 */
public class ChildSprite extends Sprite {
	/**{@link #follow} is a boolean to keep track of whether the sprite is following something*/
	private boolean follow;
	/**{@link #saved} is a boolean to keep track of whether the sprite has been saved.*/
	private boolean saved;
	private Rectangle position;
	/**{@link #moveSpeed} keeps track of how fast objects should move around the screen.*/
	private float moveSpeed;
	/**{@link #positionQueue} is a Deque which contains previously visited positions for followers to reference.*/
	private Deque<Float[]> positionQueue;
	/**{@link #nextPosition} is a Float[] that keeps track of where to move to.*/
	Float[] nextPosition;
	/**{@link #isHero} keeps track of whether the sprite is the playerCharacter.*/
	private boolean isHero;
	/**
	 * {@link #ChildSprite(Texture)} is the constructor for {@link ChildSprite}.
	 * @param childTexture The texture that the sprite will draw.
	 */
	public ChildSprite(Texture childTexture) {
		super(childTexture);
		position = new Rectangle();
		follow = false;
		saved = false;
		moveSpeed = 0;
		positionQueue = new LinkedList<Float[]>();
		nextPosition = null;
		isHero = false;
	}
	/**
	 * Sets {@link #position} to the {@link Rectangle} passed.
	 * @param pos {@link #position} will be set to this.
	 */
	public void setRectangle(Rectangle pos)
	{
		position = pos;
	}
	/**
	 * Returns true if {@link #position} overlaps the passed in {@link Rectangle}.
	 * @param test is checked against {@link #position} to see if they overlap.
	 * @return
	 */
	public boolean mySpot(Rectangle test)
	{
		return (position.overlaps(test));
	}
	/**
	 * Moves the {@link ChildSprite} to the position of the {@link MinigameRectangle} passed in.
	 * @param rect contains the new position coordinates.
	 */
	public void moveTo(MinigameRectangle rect) {
		this.setPosition(rect.getX(), rect.getY());
	}
	/**
	 * Returns whether the {@link ChildSprite} is following someone.
	 * @return
	 */
	public boolean getFollow()
	{
		return follow;
	}
	/**
	 * {@link #followSprite(ChildSprite)} takes in a {@link ChildSprite} as someone to follow.
	 * The leader will then add positions to a deque, and the current {@link ChildSprite} will then move to the location.
	 * If the position is too far away, such as when a child is initially picked up, the movement speed is increased to allow the sprite to catch up.
	 * The bufferDistance is meant to scale to the size of the screen.
	 * @param leader contains the {@link #positionQueue} of positions to follow.
	 */
	public void followSprite(ChildSprite leader){
		//make sure that the queue doesn't get too larger
		if(leader.positionQueue.size() >= 20){
			leader.positionQueue.clear();
		}
		//Set a buffer distance dependent on who is being followed.
		float bufferDistance;
		if(leader.isHero){
			bufferDistance = Gdx.graphics.getHeight()*8/576;
		}
		else{
			bufferDistance = Gdx.graphics.getHeight()*16/576;
		}
		//If your leader is far enough away from it's follower
		if((Math.abs(leader.getX() - getX()) > bufferDistance || Math.abs(leader.getY() - getY()) > bufferDistance)){
			//The hero has different sizes, and therefore needs to have different offsets.
			if(leader.isHero){
				leader.positionQueue.add(new Float[]{
						leader.getX()-leader.getWidth()-leader.getHeight()/2,
						leader.getY()-leader.getHeight()+leader.getHeight()/4
				});
			}
			else{
				leader.positionQueue.add(new Float[]{
						leader.getX(),
						leader.getY()
				});
			}
		}
		//If nextPosition is null, then get the nextPosition
		if(nextPosition == null){
			//get the next position out of the queue
			nextPosition = leader.positionQueue.pollFirst();
		}
		//If next position is not null, check if we are within the area we need to be.
		if(nextPosition != null){
			Float xDifference = Math.abs(nextPosition[0] - getX());
			Float yDifference = Math.abs(nextPosition[1] - getY());
			if(xDifference <= 2.5f && yDifference <= 2.5f){
				nextPosition = leader.positionQueue.pollFirst();
			}
		}
		//If your have a position to move to, then move to it.
		if(nextPosition != null){
			//Set childSprite's speed equal to it's leader.
			moveSpeed = leader.moveSpeed;
			//Get the change in time since the last frame.
			Float deltaTime = Gdx.graphics.getDeltaTime();
			//get the distance horizontally and vertically.
			Float xDifference = Math.abs(nextPosition[0] - getX());
			Float yDifference = Math.abs(nextPosition[1] - getY());

			if(yDifference >= bufferDistance*2 || xDifference >= bufferDistance*2){
				moveSpeed = 5*moveSpeed;
			}
			
			//If childSprite is not close enough to target move closer horizontally.
			if(xDifference >= 2.5f){
				//If childSprite is to the right, move left.
				if(getX() > nextPosition[0]){
					setX(getX() - moveSpeed*deltaTime);
				}
				//If childSprite is to the left, move right.
				else if(getX() <= nextPosition[0]){
					setX(getX() + moveSpeed*deltaTime);
				}
			}
			//If childSprite is not close enough to target move closer vertically
			if(yDifference >= 2.5f){
				//If childSprite is above, move down.
				if(getY() > nextPosition[1]){
					setY(getY() - moveSpeed*deltaTime);
				}
				//If childSprite is to the below, move up.
				else if(getY() <= nextPosition[1]){
					setY(getY() + moveSpeed*deltaTime);
				}
			}
		}
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
		follow = false;
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
	/**
	 * set {@link #isHero} to true.
	 */
	public void setHero(){
		isHero = true;
	}
	/**
	 * clears the {@link #positionQueue}.
	 */
	public void clearPositionQueue(){
		positionQueue.clear();
	}
}
