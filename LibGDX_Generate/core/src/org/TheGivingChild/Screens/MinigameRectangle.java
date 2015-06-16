package org.TheGivingChild.Screens;

import com.badlogic.gdx.math.Rectangle;
/**
 * Contains positional information where minigames can be triggered.
 * Overrides {@link Rectangle} in order to see whether it is occupied by a {@link org.TheGivingChild.Screens.ChildSprite ChildSprite} or not.
 * @author Jack Wesley Nelson
 */
public class MinigameRectangle extends Rectangle {

	private static final long serialVersionUID = 1L;
	private boolean occupied;
	private ChildSprite occupant;	
	/**
	 * Constructor, calls the constructor of {@link Rectangle}.
	 * @param x
	 * @param y
	 * @param width
	 * @param height
	 */
	public MinigameRectangle(float x, float y, float width, float height) {
		super(x,y,width,height);
		this.x = x;
		this.y = y;
	}
	/**
	 * @returns true if {@link #occupied} == True
	 */
	public boolean isOccupied()
	{
		return occupied;
	}
	/**
	 * @returns {@link #occupant}
	 */
	public ChildSprite getOccupant()
	{
		return occupant;
	}
	/**
	 * Sets the minigameRectangle to contain the ChildSprite passed in
	 * @param s {@link #occupant} is set to this.
	 */
	public void setOccupied(ChildSprite s)
	{
		//System.out.println("Occupied");
		occupant = s;
		occupied = true;
	}
	/**
	 * Emptie's out the miniGame rectangle.
	 */
	public void empty()
	{
		occupied = false;
		occupant = null;
	}
}
