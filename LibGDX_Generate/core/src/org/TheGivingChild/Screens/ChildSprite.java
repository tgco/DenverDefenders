package org.TheGivingChild.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Rectangle;

public class ChildSprite extends Sprite {
	
	private boolean follow, saved;
	private Rectangle position;
	private float moveSpeed, xMove, yMove;
	private float lastX, lastY;
	
	public ChildSprite(Texture childTexture) {
			super(childTexture);
			position = new Rectangle();
			follow = false;
			saved = false;
			moveSpeed = 0;
			xMove = 0;
			yMove = 0;
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
	
	public void followSprite(ChildSprite leader)
	{
//		follow = true;
//		//set sprite to left of leader, set xMove to leaders xMove
//		yMove = leader.yMove;
//		xMove = leader.xMove;
//		
//		if(Math.abs(xMove) > Math.abs(yMove)){
//			
//			this.setY(leader.getY());
//			
//			if(leader.xMove > 0){
//				this.setX(leader.getX()-this.getWidth()/4);
//			}
//			if(leader.xMove <= 0){
//				this.setX(leader.getX()+this.getWidth()/4);
//			}
//		}
//		else{
//			
//			this.setX(leader.getX());
//			
//			//set sprite to below leader, set yMove to leaders yMove
//			if(leader.yMove > 0){
//				this.setY(leader.getY()-this.getHeight()/4);
//			}
//			if(leader.yMove <= 0){
//				this.setY(leader.getY()+this.getHeight()/4);
//			}
//		}

	
		
		
		float xAway = Math.abs(leader.getLastX() - this.getX());
		float yAway = Math.abs(leader.getLastY() - this.getY());
		
		this.setSpeed(leader.getSpeed());
		
		//ifxAway > leader.getWidth()*.25f
		if(xAway > this.getWidth()*.25f)
		{
		
			//if the leader is to your right
			if(leader.getLastX() > this.getX())
			{
				this.xMove = this.getSpeed();
				this.yMove = 0;
			}
			//if(leader.getLastX() <= this.getX())
			else
			{
				this.xMove = -this.getSpeed();
				this.yMove = 0;
	
			}
		}
		
		else if (yAway >  this.getHeight()*.25f) {
			//if leader is above you
			if(leader.getLastY() > this.getY())
			{
				this.yMove = this.getSpeed();
				this.xMove = 0;
			}
			//if(leader.getLastY() <= this.getY())
			else
			{
				this.yMove = -this.getSpeed();
				this.xMove = 0;
			}
		}

		else {
			
		this.xMove = 0;
		this.yMove = 0;
				
	}
		//this.setPosition(this.getX() + xMove*Gdx.graphics.getDeltaTime(), this.getY() +yMove*Gdx.graphics.getDeltaTime());
		this.setX(this.getX() + xMove*Gdx.graphics.getDeltaTime());
		this.setY(this.getY() +yMove*Gdx.graphics.getDeltaTime());
		
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
	public void setMove(float xM, float yM)
	{
		xMove = xM;
		yMove = yM;
				
	}
	
	public boolean getSaved() {
		return saved;
	}
	
	public void setSaved(boolean isSaved) {
		
		saved = isSaved;
		follow = false;
	}
	
	@Override
	public void draw(Batch batch) {
		// TODO Auto-generated method stub
		super.draw(batch);
	}
	
	@Override
	public void setPosition(float x, float y){
		this.lastX = this.getX();
		this.lastY = this.getY();
		super.setPosition(x, y);
		
	}
	
	@Override
	public void setX(float x)
	{
		this.lastX = this.getX();
		super.setX(x);
	}
	
	@Override
	public void setY(float y)
	{
		this.lastY = this.getY();
		super.setY(y);
	}
	
	public float getLastX()
	{
		return lastX;
	}
	
	public float getLastY()
	{
		return lastY;
	}
	
}
