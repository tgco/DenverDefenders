package org.TheGivingChild.Engine.PowerUps;

import org.TheGivingChild.Screens.ScreenMaze;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class MaskPowerUp extends PowerUp {

	@Override
	public boolean update(ScreenMaze mazeScreen, SpriteBatch batch) {
		System.out.println("Mask power is active");
		return false;
	}

}
