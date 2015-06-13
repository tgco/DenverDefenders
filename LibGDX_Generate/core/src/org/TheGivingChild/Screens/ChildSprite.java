package org.TheGivingChild.Screens;

import java.util.Deque;
import java.util.LinkedList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Rectangle;

public class ChildSprite extends Sprite {

	private boolean follow, saved;
	private Rectangle position;
	private float moveSpeed;
	private Deque<Float[]> positionQueue;
	Float[] nextPosition;
	private boolean isHero;

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

	public void setRectangle(Rectangle pos)
	{
		position = pos;
	}

	public boolean mySpot(Rectangle test)
	{
		return (position.overlaps(test));
	}


	public ChildSprite(ChildSprite c)
	{
		this((c.getTexture()));

	}

	public void moveTo(MinigameRectangle rect) {
		this.setPosition(rect.getX(), rect.getY());
	}

	public boolean getFollow()
	{
		return follow;
	}

	public void followSprite(ChildSprite leader){
		//make sure that the queue doesn't get too larger
		if(leader.positionQueue.size() >= 2000){
			leader.positionQueue.clear();
		}
		//get the last value added to the leaders deque
		Float[] leadersNextPosition = leader.positionQueue.peekLast();
		//Set a buffer distance dependent on who is being followed.
		float bufferDistance;
			 bufferDistance = 32;
		//If your leader is too far enough away from it's follower, and it either has no points or is far enough away from it's last point, then add their position to the queue
		if((Math.abs(leader.getX() - getX()) > bufferDistance || Math.abs(leader.getY() - getY()) > bufferDistance) && (leadersNextPosition == null || (Math.abs(leadersNextPosition[0] - leader.getX()) > bufferDistance) || Math.abs(leadersNextPosition[1] - leader.getY()) > bufferDistance)){
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
				System.out.println("Polled new position");
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
			System.out.println("Current position: (" + getX() + ", " +getY() + "), Needed position: (" + nextPosition[0] + ", " +nextPosition[1] + ").");
		}
	}

	public boolean sameDirection(float d1, float d2)
	{
		return((d1 > 0 && d2>0) || (d1 < 0 && d2 < 0) );
	}


	public void setSpeed(float f)
	{
		moveSpeed = f;
	}

	public float getSpeed()
	{
		return moveSpeed;
	}

	public boolean getSaved() {
		return saved;
	}

	public void setSaved(boolean isSaved) {

		saved = isSaved;
		follow = false;
	}
	@Override
	public float getWidth() {
		return super.getWidth()*getScaleX();
	}
	@Override
	public float getHeight() {
		return super.getHeight()*getScaleY();
	}
	public void setHero(){
		isHero = true;
	}
	public void clearPositionQueue(){
		positionQueue.clear();
	}
}
