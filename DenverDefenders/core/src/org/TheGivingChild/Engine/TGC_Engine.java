package org.TheGivingChild.Engine;

import java.util.Random;

import org.TheGivingChild.Engine.XML.Level;
import org.TheGivingChild.Engine.XML.LevelPacket;
import org.TheGivingChild.Engine.XML.XML_Reader;
import org.TheGivingChild.Screens.ScreenAdapterEnums;
import org.TheGivingChild.Screens.ScreenAdapterManager;

import com.badlogic.gdx.Application.ApplicationType;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

/**
 * This is the main class that is passed between all the screens.
 * It holds the {@link com.badlogic.gdx.assets.AssetManager AssetManager} which allows easy access to all of our assets.
 * It also holds the {@link org.TheGivingChild.Engine.XML.XML_Writer XML Writer} and 
 * {@link org.TheGivingChild.Engine.XML.XML_Reader XML Reader} that some screens use themselves. 
 * 
 * @author Jack Wesley Nelson, Corey Tokunaga-Reichert, Kevin Day, Milton Tzimourakas, Nathaniel Jacobi
 *
 */
public class TGC_Engine extends Game {
	// Global game time clock.  Used primarily for animations
	private float globalClock;
	// Random generator
	private Random rand;
	// Scale that fonts should be drawn at
	private float globalFontScale;
	/**The stage to place actors to.*/
	private TGC_Stage stage;
	/**{@link #bitmapFontButton} is the {@link com.badlogic.gdx.graphics.g2d.BitmapFont BitmapFont} used for our {@link com.badlogic.gdx.scenes.scene2d.ui.Button Buttons}.*/
	private BitmapFont bitmapFontButton; 
    /**{@link #levelSet} is the container for levels to be played during a maze.*/
	private Array<Level> levelSet;
	/**{@link #currentLevel} keeps track of the current level being played.*/
	private Level currentLevel;
	/**
	 * This is the asset manager that will store all of the textures, packs, and sounds.
	 * It is accessed throughout all of the game and can be loaded and accessed at any point.
	 * Use manager.load("name of asset", TypeOfAsset.class) to load an asset into the 
	 * queue to be loaded.
	 * Use manager.update() to load the enqueued assets. This must be called continuously
	 * until the manager is completely loaded, so it is best to be called in the built in
	 * render function.
	 * If you need an asset loaded right away, use manager.finishLoading(). Be aware that
	 * this will cease all other functions until everything in the queue at that point
	 * is loaded. 
	 * @author ctokunag
	 */
	private AssetManager manager;
	/**{@link #reader} allows minigames to be read in.*/
	private XML_Reader reader;
	/**{@link #camera} controls the normal render camera.*/
	private OrthographicCamera camera;
	/**
	 * This viewport is used to scale the game to whatever screen is being used.
	 * When it is constructed, you will need to enter the aspect ratio you wish to maintain
	 * as well as a camera that will be used. You will also need to specify the type of
	 * viewport that you intend to use. ExtendViewport scales the screen to fit in the 
	 * viewport, then extends the shorter dimension to fill the viewport. FillViewport
	 * keeps the aspect ratio of the screen, but will fill the whole screen which can 
	 * result in parts of the viewport being cut off. FitViewport maintains the aspect
	 * ratio of the screen, and scales it to fit the screen. However, if the aspect ratios
	 * of the screen and game are different, then black bars can appear on the sides.
	 * ScreenViewport will always match the window size, but no scaling will happen. 
	 * Everything will be relative to actual pixels as opposed to camera units. Finally,
	 * there is StretchViewport, which stretches the viewport to fit the screen. There 
	 * will be no black bars, but the images may be distorted. We have decided to go with
	 * StretchViewport as most of the aspect ratios we will be dealing with will be fairly
	 * close, so we shouldn't have to deal with too much distortion.
	 */
	private Viewport viewport;
	/**{@link #batch} is used for drawing objects to the screen during {@link #render()};*/
	private Batch batch;
	/**{@link #loadLevelPackets()} loads the minigames into their corresponding packets. Packets are created based on folders in Assets/Levels, and the .xml files within these folders create the games for those packets.*/
	/*
	 * 	MOVE LEVEL PACKET LOADING TO BE A RESPONSIBILITY OF WHOEVER LOADS THE MAZE
	 */
	public void loadLevelPackets() {
		// Array of possible levels to play
		levelSet = new Array<Level>();
		FileHandle dirHandle;
		if (Gdx.app.getType() == ApplicationType.Android) {
			dirHandle = Gdx.files.internal("MazeAssets/UrbanMaze1");
		} else {
			// ApplicationType.Desktop ..
			dirHandle = Gdx.files.internal("./bin/MazeAssets/UrbanMaze1");
		}
		// Grab the tiled map file
		FileHandle mapFile = dirHandle.child(dirHandle.name() + ".tmx");
		// Load level objects
		for (FileHandle levelFile : dirHandle.child("Levels").list()) {
			reader.setupNewFile(levelFile);
			Level level = reader.compileLevel();
			levelSet.add(level);
		}
		System.out.println(levelSet.size);
	}
	/**{@link #selectLevel()} handles setting {@link #currentLevel} to which minigame should be played.*/
	public void selectLevel() {
		// Pick a random level and reset it for play
		currentLevel = levelSet.random();
		currentLevel.resetLevel();
	}

	/**{@link #create()} is called when the game is initially launched. Initializes files, and variables needed.*/
	@Override
	public void create () {
		Gdx.input.setCatchBackKey(true);
		
		// Init global clock
		globalClock = 0;
		rand = new Random();
		// Calculate font scale
		globalFontScale = Gdx.graphics.getWidth()/Gdx.graphics.getPpiX() * 0.25f;
		
		// Init asset manager and load assets for the splash screen (the first screen), and the screen manager
		manager = new AssetManager();
		// Set custom loader for tiled maps
		manager.setLoader(TiledMap.class, new TmxMapLoader());
		manager.load("MainScreen_Splash.png", Texture.class); // splash background
		manager.load("ColdMountain.png", Texture.class); // main ui background is drawn from the screen manager (refactor this)
		
		manager.finishLoading();
		
		/* These assets will load during the splash screen */
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


		//button stuff
		bitmapFontButton = new BitmapFont();
		//create the stage
		stage = new TGC_Stage();

		//Game input processor
		Gdx.input.setInputProcessor(stage);

		camera = new OrthographicCamera();
		viewport = new StretchViewport(16, 9, camera);
		viewport.apply();
		camera.position.set(camera.viewportWidth/2, camera.viewportHeight/2, 0);

		batch = new SpriteBatch();
		
		//LOAD NOT NECESSARY YET
		loadLevelPackets();
	}

	/**{@link #dispose()} handles the resource disposal when {@link TGC_Engine} exits.*/
	@Override
	public void dispose(){
		super.dispose();
		//dispose the screen manager, and in doing so all screens
		ScreenAdapterManager.getInstance().dispose();
		batch.dispose();
		bitmapFontButton.dispose();
		// may double dispose audio, but runs on app quit.
		AudioManager.getInstance().dispose();
		manager.dispose();
	};
	/**{@link #getBitmapFontButton()} returns {@link #bitmapFontButton}.*/
	public BitmapFont getBitmapFontButton(){
		return bitmapFontButton;
	}
	/**{@link #getStage()} returns {@link #stage}. */
	public Stage getStage() {
		return stage;
	}	
	/**{@link #getAssetManager()} returns {@link #manager}. */
	public AssetManager getAssetManager() {
		return manager;
	}
	/**{@link #getCurrentLevel()} returns {@link #currentLevel}.*/
	public Level getCurrentLevel() {
		return currentLevel;
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
		Gdx.gl.glClearColor(1,1,1,1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		// Calls render on current screen
		super.render();
		
		camera.update();
		globalClock += Gdx.graphics.getDeltaTime();
		
		stage.act();
		stage.draw();
	}

	/**
	 * This overridden resize function is what allows the viewport to scale the screen.
	 * It is constantly called so if the screen is different in any way, it will update
	 * the viewport and reposition the camera accordingly.
	 */
	@Override
	public void resize(int width, int height) {
		viewport.update(width, height);
		camera.position.set(camera.viewportWidth/2, camera.viewportHeight/2, 0);
	}
}
