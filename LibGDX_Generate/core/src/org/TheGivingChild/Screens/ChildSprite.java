package org.TheGivingChild.Screens;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Rectangle;

public class ChildSprite extends Sprite {
	
	private boolean follow;
	private Rectangle position;
	
	public ChildSprite(Texture childTexture) {
			super(childTexture);
			position = new Rectangle();
			follow = false;
	}
	
	public void setRectangle(Rectangle pos)
	{
		position = pos;
	}
	
	public boolean mySpot(Rectangle test)
	{
		return (test.x == position.x && test.y == position.y);
	}
	

	public boolean getFollow()
	{
		return follow;
	}
	
	public void followSprite(Sprite leader)
	{
		follow = true;
		this.setX(leader.getX() - this.getWidth()/2);
		this.setY(leader.getY() - this.getHeight()/2);
	}
	
	
	
}
