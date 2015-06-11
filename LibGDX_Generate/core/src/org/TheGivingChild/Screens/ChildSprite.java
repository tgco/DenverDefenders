package org.TheGivingChild.Screens;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Rectangle;

public class ChildSprite extends Sprite {
	
	private boolean follow, saved;
	private Rectangle position;
	private float moveSpeed, xMove, yMove;
	
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
		follow = true;
		//set sprite to left of leader, set xMove to leaders xMove
		yMove = leader.yMove;
		xMove = leader.xMove;	
		if(Math.abs(xMove) > Math.abs(yMove)){
			
			this.setY(leader.getY());
			
			if(leader.xMove > 0){
				this.setX(leader.getX()-this.getWidth()/4);
			}
			if(leader.xMove <= 0){
				this.setX(leader.getX()+this.getWidth()/4);
			}
		}
		else{
			
			this.setX(leader.getX());
			
			//set sprite to below leader, set yMove to leaders yMove
			if(leader.yMove > 0){
				this.setY(leader.getY()-this.getHeight()/4);
			}
			if(leader.yMove <= 0){
				this.setY(leader.getY()+this.getHeight()/4);
			}
		}

//		float xAway = Math.abs(leader.getX() - this.getX());
//		float yAway = Math.abs(leader.getY() - this.getY());
//		
//		if(xAway > leader.getWidth()*.25f)
//		{
//		
//		//if the leader is to your right
//		if(leader.getX() > this.getX())
//		{
//			xMove = leader.moveSpeed;
//		}
//		if(leader.getX() <= this.getX())
//		{
//			xMove = -leader.moveSpeed;
//
//		}
//		}
//		
//		else if (yAway >  getHeight()*.25f) {
//		//if leader is above you
//		if(leader.getY() > this.getY())
//		{
//			yMove = leader.moveSpeed;
//		}
//		if(leader.getY() <= this.getY())
//		{
//			yMove = -leader.moveSpeed;
//		}
//		
//		}
		
//		else {
//			xMove = 0;
//			yMove = 0;
//		}
		//this.setPosition(this.getX() + xMove*Gdx.graphics.getDeltaTime(), this.getY() +yMove*Gdx.graphics.getDeltaTime());
		this.setX(this.getX() + xMove*Gdx.graphics.getDeltaTime());
		this.setY(this.getY() +yMove*Gdx.graphics.getDeltaTime());
		
	}
	
	
	
	public void setSpeed(float sp)
	{
		moveSpeed = sp;
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
	}
	
	@Override
	public void draw(Batch batch) {
		// TODO Auto-generated method stub
		super.draw(batch);
	}
	
}
