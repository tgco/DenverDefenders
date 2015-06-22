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
	// true once auto transition has been initiated
	private boolean transitionInit;

	public ScreenSplash() {
		game = ScreenAdapterManager.getInstance().game;
		// Request load of the splash image
		game.getAssetManager().load("MainScreen_Splash.png", Texture.class);
		game.getAssetManager().finishLoadingAsset("MainScreen_Splash.png");
		
		batch = new SpriteBatch();
		// Init time to show splash screen in seconds
		splashScreenTimer = 1.0f;
		transitionInit = false;
	}

	@Override
	public void render(float delta) {
		batch.begin();
		batch.draw(game.getAssetManager().get("MainScreen_Splash.png", Texture.class), 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		batch.end();
		
		// Decrement timer
		splashScreenTimer -= delta;
		if (splashScreenTimer < 0 && game.getAssetManager().update() && !transitionInit) {
			transitionInit = true;
			// Transition to main screen when splash and loading done
			ScreenTransition splashToMain = new ScreenTransition(ScreenAdapterEnums.SPLASH, ScreenAdapterEnums.MAIN);
			game.setScreen(splashToMain);
		}
	}
	
	// Draws all important images without any logic updating
	public void drawComponents() {
		batch.begin();
		batch.draw(game.getAssetManager().get("MainScreen_Splash.png", Texture.class), 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		batch.end();
	}
	
	@Override
	public void dispose() {
		batch.dispose();
	}
}
