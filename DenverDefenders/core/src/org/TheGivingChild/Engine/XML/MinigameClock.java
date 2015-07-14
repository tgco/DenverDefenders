package org.TheGivingChild.Engine.XML;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

/**
 * Serves as the gameclock for the minigames
 * Singleton
 *<p>
 *-Final to avoid inheritance
 *</p>
 * @author mtzimour
 */

public final class  MinigameClock {
	// Initial length of the level
	private double levelLength;
	// Time remaining in s
	private double remaining;
	/**Boolean to keep track of whether time remains*/
	private boolean outOfTime = false;
	
	// Size to draw the clock image
	private static final float CLOCK_SIZE = Gdx.graphics.getHeight()/5f;
	
	private static MinigameClock clock;
	
	/**
	 * Gets the static instance of gameclock if it has already been created.
	 * Creates a new instance if there is no clock yet.
	 * @return returns the static clock or a new one.
	 */
	public static MinigameClock getInstance() {
		if (null == clock){
			clock = new MinigameClock();
		}
		return clock;
	}
	
	// Draws the clock image from the asset manager
	public void draw(SpriteBatch batch, AssetManager manager) {
		Texture clockface = manager.get("clock.png", Texture.class);
		Texture clockhand = manager.get("clockHand.png", Texture.class);
		// Draw clock face in upper left corner
		batch.draw(clockface, 0, Gdx.graphics.getHeight() - CLOCK_SIZE, CLOCK_SIZE, CLOCK_SIZE);
		// Draw hand, rotated to match the time elapsed
		float rotation = (float) (1f - remaining/levelLength) * 360f;
		// Sprite image is drawn as 45deg, adjust
		rotation += 45;
		batch.draw(new TextureRegion(clockhand), CLOCK_SIZE/2f, Gdx.graphics.getHeight() - CLOCK_SIZE/2f, 0, 0, CLOCK_SIZE/3f, CLOCK_SIZE/3f, 1, 1, rotation);
	}
	
	/**
	 * Sets the length of time allowed to complete a minigame.
	 * @param time Level time allowed in seconds.
	 */
	public void setLevelLength(double time) {
		levelLength = time;
		remaining = time;
		outOfTime = false;
	}
		
	/**
	 * Decrements the level time remaining by time since the last frame.
	 * Checks if level is out of time and sets flag if it is.
	 */
	public void progress() {
		remaining -= Gdx.graphics.getDeltaTime();		
		if(remaining <= 0) {
			outOfTime = true;
		}
	}

	public double getRemainingTime() {
		return remaining;
	}
	
	/**
	 * Returns true if level is out of time, or false if level still has time remaining.
	 * @return If there is still time remaining or not.
	 */
	public boolean outOfTime() {
		return outOfTime;
	}

	/**
	 * Gets the time remaining in the level.
	 * @return total Level time remaining in milliseconds.
	 */
	public String toString() {
		String time = String.format("%.2f", getRemainingTime());
		return time + " seconds remaining.";
	}

}
