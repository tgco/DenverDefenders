package org.TheGivingChild.Engine;

import org.TheGivingChild.Engine.XML.XML_Reader;
import org.TheGivingChild.Screens.ScreenAdapterEnums;
import org.TheGivingChild.Screens.ScreenAdapterManager;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.FPSLogger;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;

/**
 * This is the main class that is passed between all the screens.
 * It holds the {@link com.badlogic.gdx.assets.AssetManager AssetManager} which allows easy access to all of our assets.
 * It also holds the {@link org.TheGivingChild.Engine.XML.XML_Reader XML Reader} that some screens use themselves. 
 * 
 * @author Jack Wesley Nelson, Corey Tokunaga-Reichert, Kevin Day, Milton Tzimourakas, Nathaniel Jacobi, Walter Schlosser
 *
 */
public class TGC_Engine extends Game {
	// Global game time clock.  Used primarily for animations
	private float globalClock;
	// Scale that fonts should be drawn at
	private float globalFontScale;
	// Progression data used to determine what is unlocked
	public ProgressionData data;
	/**The stage to place actors to.*/
	private TGC_Stage stage;
	/**{@link #bitmapFontButton} is the {@link com.badlogic.gdx.graphics.g2d.BitmapFont BitmapFont} used for labels*/
	private BitmapFont bitmapFontButton; 
	/** Asset manager used for the whole game. It is preferred that all assets use this manager for loading */
	private AssetManager manager;
	/**{@link #reader} allows mini games to be read in.*/
	private XML_Reader reader;
	
	// True if debug bounds drawing should be used
	public boolean debug = false;
	// FPS logger for finding slow operations
	public FPSLogger fps;

	/**{@link #create()} is called when the game is initially launched. Initializes files, and variables needed.*/
	@Override
	public void create () {
		fps = new FPSLogger();
		Gdx.input.setCatchBackKey(true);
		
		// Init progression data
		data = new ProgressionData();
		
		// Init global clock
		globalClock = 0;
		// Calculate font scale, default scale set for a 1024x600 screen (the desktop app size)
		globalFontScale = Gdx.graphics.getWidth()/1024f * 2.5f;
		
		// Init asset manager and completely load assets for the splash screen (the first screen), and the screen manager
		manager = new AssetManager();
		// Set custom loader for tiled maps
		manager.setLoader(TiledMap.class, new TmxMapLoader());
		manager.load("MainScreen_Splash.png", Texture.class); // splash background
		manager.load("ColdMountain.png", Texture.class); // main ui background is drawn from the screen manager (refactor this)
		
		manager.finishLoading();
		
		/* Any following assets in this constructor will load during the splash screen */
		// UI materials
		manager.load("SemiTransparentBG.png", Texture.class);
		manager.load("Packs/Buttons.pack", TextureAtlas.class);
		// Needed for screen transition
		manager.load("Packs/ScreenTransitions.pack", TextureAtlas.class);
		// Game audio
		manager.load("sounds/backgroundMusic/03_Chibi_Ninja.wav", Music.class);
		
		// Initialize screen management
		ScreenAdapterManager.getInstance().initialize(this);
		// Set initial screen to splash
		setScreen(ScreenAdapterManager.getInstance().getScreenFromEnum(ScreenAdapterEnums.SPLASH));

		// READER NOT USED IN THIS CLASS, MOVE THEM TO WHERE THEY ARE NEEDED
		reader = new XML_Reader();

		// Font initialized to generic
		bitmapFontButton = new BitmapFont();
		// Create the stage
		stage = new TGC_Stage();

		// Game input processor
		Gdx.input.setInputProcessor(stage);
	}

	/**{@link #dispose()} handles the resource disposal when {@link TGC_Engine} exits.*/
	@Override
	public void dispose(){
		super.dispose();
		//dispose the screen manager, and in doing so all screens
		ScreenAdapterManager.getInstance().dispose();
		bitmapFontButton.dispose();
		// may double dispose audio, but runs on app quit.
		AudioManager.getInstance().dispose();
		manager.dispose();
	};
	
	public BitmapFont getBitmapFontButton(){
		return bitmapFontButton;
	}

	public Stage getStage() {
		return stage;
	}	

	public AssetManager getAssetManager() {
		return manager;
	}
	
	public XML_Reader getReader() {
		return reader;
	}
	
	// Returns the global game clock time in seconds
	public float getGlobalClock() {
		return globalClock;
	}
	
	// Returns the scale that fonts should be drawn at
	public float getGlobalFontScale() {
		return globalFontScale;
	}
	
	/**{@link #render()} handles rendering the main stage, as well as calling the render of the current {@link ScreenAdapter} being shown.*/
	@Override
	public void render() {
		// FPS check
		fps.log();

		// Clear screen to white
		Gdx.gl.glClearColor(1,1,1,1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		// Calls render on current screen
		super.render();
		globalClock += Gdx.graphics.getDeltaTime();
		// Catch stage input and draw
		stage.act();
		stage.draw();
		// DEBUG BOUNDS DRAWING
		if (debug) {
			ShapeRenderer debug = new ShapeRenderer();
			debug.setAutoShapeType(true);
			debug.begin();
			for (Actor a : stage.getActors() ) {
				debug.rect(a.getX(), a.getY(), a.getWidth(), a.getHeight());
			}
			debug.end();
		}
	}

	@Override
	public void resize(int width, int height) {

	}
}
