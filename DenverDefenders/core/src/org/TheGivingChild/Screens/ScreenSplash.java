package org.TheGivingChild.Screens;

import org.TheGivingChild.Engine.AudioManager;
import org.TheGivingChild.Engine.TGC_Engine;
import org.TheGivingChild.Engine.Maze.Direction;
import org.TheGivingChild.Screens.UI.ComicScreenTransition;
import org.TheGivingChild.Screens.UI.UIScreenAdapter;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;

public class ScreenSplash extends UIScreenAdapter {
	private TGC_Engine game;
	
	/**{@link #splashScreenTimer} is the timer for how long the Title Splash Screen is displayed.*/
	private float splashScreenTimer;
	// true once auto transition has been initiated
	private boolean transitionInit;
	// True once all ui assets have been requested for loading
	private boolean uiRequested;

	public ScreenSplash() {
		game = ScreenAdapterManager.getInstance().game;
		background = game.getAssetManager().get("MainScreen_Splash.png", Texture.class);
		batch = new SpriteBatch();
		// Init time to show splash screen in seconds
		splashScreenTimer = 1.0f;
		transitionInit = false;
		uiRequested = false;
	}

	@Override
	public void render(float delta) {
		batch.begin();
		batch.draw(background, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		batch.end();
		
		// UI asset load
		if (!uiRequested) {
			// UI materials
			game.getAssetManager().load("ColdMountain.png", Texture.class);
			game.getAssetManager().load("SemiTransparentBG.png", Texture.class);
			game.getAssetManager().load("Packs/Buttons.pack", TextureAtlas.class);
			game.getAssetManager().load("loadingButton.png", Texture.class);
			// Needed for screen transition
			game.getAssetManager().load("Packs/ScreenTransitions.pack", TextureAtlas.class);
			// Game audio
			game.getAssetManager().load("sounds/backgroundMusic/adamWestBatman.mp3", Music.class);
			// Pre loaded during splash to smooth out the ui "comic" transitions
			ScreenMain.requestAssets(game.getAssetManager());
			ScreenOptions.requestAssets(game.getAssetManager());
			ScreenHowToPlay.requestAssets(game.getAssetManager());
			ScreenMazeSelect.requestAssets(game.getAssetManager());
			uiRequested = true;
		}
		
		// Decrement timer
		splashScreenTimer -= delta;
		// call update first so short circuiting doesn't prevent asset loading
		if (game.getAssetManager().update() && splashScreenTimer < 0 && !transitionInit) {
			// Initialize audio management since audio is loaded during splash
			AudioManager.getInstance().initialize(game);
			// Start background music
			AudioManager.getInstance().playBackgroundMusic();
			// Load save data
			game.data.load();
			transitionInit = true;
			// Transition to main screen when splash and loading done
			ComicScreenTransition splashToMain = new ComicScreenTransition(ScreenAdapterEnums.SPLASH, ScreenAdapterEnums.MAIN, Direction.RIGHT);
			game.setScreen(splashToMain);
			// Play a punch sound
			AudioManager.getInstance().playPunchSound();
		}
	}
	
	@Override
	public void dispose() {
		batch.dispose();
	}
}
