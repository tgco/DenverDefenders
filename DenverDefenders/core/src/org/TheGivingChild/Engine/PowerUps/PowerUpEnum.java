package org.TheGivingChild.Engine.PowerUps;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;

// Wrapper that can construct correct powerup from a string
public enum PowerUpEnum {
	MASK {
		@Override
		public PowerUp construct() {
			return new MaskPowerUp();
		}
		
		@Override
		public void requestAssets(AssetManager manager) {
			manager.load("PowerUps/mask/button.png", Texture.class);
			manager.load("PowerUps/mask/buttonUsed.png", Texture.class);
		}
	};
	
	public abstract PowerUp construct();
	// Request the assets needed to draw the powerup and the button
	public abstract void requestAssets(AssetManager manager);
}
