package org.TheGivingChild.Screens;

import org.TheGivingChild.Engine.TGC_Engine;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class ScreenSplash extends ScreenAdapter {
	private SpriteBatch batch;
	private TGC_Engine game;
	
	/**{@link #splashScreenTimer} is the timer for how long the Title Splash Screen is displayed.*/
	private float splashScreenTimer;

	public ScreenSplash() {
		game = ScreenAdapterManager.getInstance().game;
		// Request load of the splash image
		game.getAssetManager().load("MainScreen_Splash.png", Texture.class);
		game.getAssetManager().finishLoadingAsset("MainScreen_Splash.png");
		
		batch = new SpriteBatch();
		// Init time to show splash screen in seconds
		splashScreenTimer = 5.0f;
	}

	@Override
	public void render(float delta) {
		batch.begin();
		batch.draw(game.getAssetManager().get("MainScreen_Splash.png", Texture.class), 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		batch.end();
		
		// Decrement timer
		splashScreenTimer -= delta;
		if (splashScreenTimer < 0 && game.getAssetManager().update()) {
			// Change to main screen, splash and loading done
			ScreenAdapterManager.getInstance().show(ScreenAdapterEnums.MAIN);
			// Dispose of this screen, it will not be used again
			dispose();
		}
	}
	
	@Override
	public void dispose() {
		batch.dispose();
	}
}
