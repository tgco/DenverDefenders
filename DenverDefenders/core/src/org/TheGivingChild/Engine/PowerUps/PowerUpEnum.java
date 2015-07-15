package org.TheGivingChild.Engine.PowerUps;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;

// Convenience enum wrapper that can construct correct power up from a string
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
			manager.load("PowerUps/mask/arrow.png", Texture.class);
		}
		
		@Override
		public String description() {
			return "The mask will show you the direction to the closest kid in the maze!";
		}
	},
	CAPE {
		@Override
		public PowerUp construct() {
			return new CapePowerUp();
		}

		@Override
		public void requestAssets(AssetManager manager) {
			manager.load("PowerUps/cape/button.png", Texture.class);
			manager.load("PowerUps/cape/buttonUsed.png", Texture.class);
		}

		@Override
		public String description() {
			return "The cape will let you move fast and see a larger portion of the maze for a while!";
		}
		
	},
	BICYCLE {
		@Override
		public PowerUp construct() {
			return new BicyclePowerUp();
		}

		@Override
		public void requestAssets(AssetManager manager) {
			manager.load("PowerUps/bicycle/button.png", Texture.class);
			manager.load("PowerUps/bicycle/buttonUsed.png", Texture.class);
		}

		@Override
		public String description() {
			return "The bicycle will take you back to the Hero Headquarters automatically!";
		}
		
	};
	
	// Return the correct object
	public abstract PowerUp construct();
	// Request the assets needed to draw the power up and the button for it
	public abstract void requestAssets(AssetManager manager);
	// Return the description of the power for an unlock screen
	public abstract String description();
}
