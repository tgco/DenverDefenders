package org.TheGivingChild.Engine;

import java.util.Random;

import org.TheGivingChild.Engine.XML.Level;
import org.TheGivingChild.Engine.XML.LevelPacket;
import org.TheGivingChild.Engine.XML.XML_Reader;
import org.TheGivingChild.Engine.XML.XML_Writer;
import org.TheGivingChild.Screens.ScreenAdapterEnums;
import org.TheGivingChild.Screens.ScreenAdapterManager;

import com.badlogic.gdx.Application.ApplicationType;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
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
	/**{@link #DESKTOP_WIDTH} is our width in pixels for desktop testing.*/
	private final static int DESKTOP_WIDTH = 1024;
	/**{@link #DESKTOP_HEIGHT} is our Height in pixels for desktop testing.*/
	private final static int DESKTOP_HEIGHT = 576;
	/**{@link #BUTTON_STATES} is the number of changed states that each button contains in Buttons.pack.*/
	private final static int BUTTON_STATES = 2;
	/**The stage to place actors to.*/
	private TGC_Stage stage;
	/**{@link #buttonAtlasNamesArray} holds reference to the names of elements within Buttons.pack for different buttons and their corresponding states. */
	private String[] buttonAtlasNamesArray = {"ButtonPressed_MainScreen_Play", "Button_MainScreen_Play", "ButtonPressed_MainScreen_HowToPlay", "Button_MainScreen_HowToPlay", "ButtonPressed_MainScreen_Editor", "Button_MainScreen_Editor", "ButtonPressed_MainScreen_Options", "Button_MainScreen_Options"};
	/**{@link #skin} is the {@link com.badlogic.gdx.scenes.scene2d.ui.Skin Skin} created from the Buttons.pack {@link com.badlogic.gdx.scenes.scene2d.ui.Skin TextureAtlas}.*/
	private Skin skin = new Skin();
	/**{@link #bitmapFontButton} is the {@link com.badlogic.gdx.graphics.g2d.BitmapFont BitmapFont} used for our {@link com.badlogic.gdx.scenes.scene2d.ui.Button Buttons}.*/
	private BitmapFont bitmapFontButton; 
    /**{@link #levelPackets} is the container for levels to be played during a maze.*/
	private Array<LevelPacket> levelPackets;
	/**{@link #currentLevel} keeps track of the current level being played.*/
	private Level currentLevel;
	/**{@link #screenManagerLoaded} keeps track of whether the {@link ScreenAdapterManager} has been instantiated.*/
	private boolean screenManagerLoaded = false;
	/**{@link #width} is set equal to Gdx.graphics.getWidth().*/
	private float width;
	/**{@link #height} is set equal to Gdx.graphics.getHeight().*/
	private float height;
	/**{@link #volume} keeps track of the current volume for sounds and music.*/
	public float volume;
	/**{@link #soundEnabled} is used for checking whether to play sounds.*/
	public boolean soundEnabled;
	/**{@link #musicEnabled} is used for checking whether to play music.*/
	public boolean musicEnabled;
	/**{@link #muteAll} will keep sounds and music from playing when set to true.*/
	public boolean muteAll;
	/**{@link #SCREEN_TRANSITION_TIMER} is the initial screen splash length.*/
	private final static float SCREEN_TRANSITION_TIMER = 1.0f;
	/**{@link #screenTransitionTimeLeft} only keeps track of the current state of the initial {@link #SCREEN_TRANSITION_TIMER}.*/
	private float screenTransitionTimeLeft;
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
	private AssetManager manager = new AssetManager();
	/**{@link #reader} allows minigames to be read in.*/
	private XML_Reader reader;
	/**{@link #writer} allows minigames to be written in the editor.*/
	private XML_Writer writer;
	/**{@link #mapCamera} controls the maps camera.*/
	private OrthographicCamera mapCamera;
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
	/**{@link #gameStart} is true if the game has gone past the original splash screen. False otherwise */
	private boolean gameStart = false;
	/**{@link #levelWinOrLose} is true if the minigame was just won, false if the minigame was lost.*/
	private boolean levelWinOrLose;
	/**{@link #currentMazeCompleted} is true if the maze has been completed.*/
	private boolean currentMazeCompleted = false;
	/**{@link #fromGame} is true if the screen transitioned from a minigame.*/
	private boolean fromGame = false;
	/**{@link #backgroundSounds} is an {@link com.badlogic.gdx.utils.Array Array} of {@link com.badlogic.gdx.audio.Music Music} to play in the background.*/
	private Array<Music> backgroundSounds;
	/**{@link #allSaved} is true if all the kids in the maze have been saved.*/
	private boolean allSaved = false;
	/**{@link #backgroundSoundToPlay} is the current {@link com.badlogic.gdx.audio.Music Music} object to be played.*/
	private Music backgroundSoundToPlay;
	/**{@link #splashScreenTimer} is the timer for how long the Title Splash Screen is displayed.*/
	private float splashScreenTimer = 20.0f;
	/**{@link #loadLevelPackets()} loads the minigames into their corresponding packets. Packets are created based on folders in Assets/Levels, and the .xml files within these folders create the games for those packets.*/
	/*
	 * 	MOVE LEVEL PACKET LOADING TO BE A RESPONSIBILITY OF WHOEVER LOADS THE MAZE
	 */
	public void loadLevelPackets() {
		levelPackets = new Array<LevelPacket>();
		FileHandle dirHandle;
		if (Gdx.app.getType() == ApplicationType.Android) {
			dirHandle = Gdx.files.internal("Levels");
		} else {
			// ApplicationType.Desktop ..
			dirHandle = Gdx.files.internal("./bin/Levels");
		}
		for (FileHandle entry: dirHandle.list()) {
			LevelPacket packet = new LevelPacket(entry.name());
			for (FileHandle levelFile: entry.list()) {
				levelFile = Gdx.files.internal("Levels/" + entry.name() + "/" + levelFile.name());
				reader.setupNewFile(levelFile);
				Level level = reader.compileLevel();
				packet.addLevel(level);
			}
			levelPackets.add(packet);
		}
	}
	/**{@link #selectLevel()} handles setting {@link #currentLevel} to which minigame should be played.*/
	/*
	 * LOADS ONLY FROM FIRST PACKET
	 * 
	 */
	public void selectLevel() {
		Array<Level> possibleLevels = new Array<Level>();
		for (Level newLevel: levelPackets.get(0).getLevels()) {
			if (!newLevel.getCompleted()) {
				possibleLevels.add(newLevel);
			}
		}

		if (possibleLevels.size == 0) {
			loadLevelPackets(); //REDUNDANT LOADING
			for (Level newLevel: levelPackets.get(0).getLevels()) {
				if (!newLevel.getCompleted()) {
					possibleLevels.add(newLevel);
				}
			}
		}
		Random rand = new Random();
		int newLevelIndex = (rand.nextInt(possibleLevels.size));
		currentLevel = possibleLevels.get(newLevelIndex);
		currentLevel.resetLevel();
	}
	/**{@link #levelCompleted(boolean)} sets the {@link #levelWinOrLose} to winOrLose.
	 * @param winOrLose {@link #levelWinOrLose} is set to this.*/
	public void levelCompleted(boolean winOrLose) {
		levelWinOrLose = winOrLose;
	}
	/**{@link #levelWin()} returns {@link #levelWinOrLose}.*/
	public boolean levelWin() {
		return levelWinOrLose;
	}
	/**{@link #setMazeCompleted(boolean)} sets the {@link #currentMazeCompleted} to state.
	 * @param state {@link #currentMazeCompleted} is set to this. */
	public void setMazeCompleted(boolean state) {
		currentMazeCompleted = state;
	}
	/**{@link #getMazeCompleted()} returns {@link #currentMazeCompleted}. */
	public boolean getMazeCompleted() {
		return currentMazeCompleted;
	}
	/**{@link #getAllSaved()} returns {@link #allSaved} */
	public boolean getAllSaved() {
		return allSaved;
	}
	/**{@link #getAllSaved()} sets {@link #allSaved} to state.
	 * @param state {@link #allSaved} is set to this.*/
	public void setAllSaved(boolean state) {
		allSaved = state;
	}
	/**
	 * {@link #setFromGame(boolean)} sets {@link #fromGame} state.
	 * @param state {@link #fromGame} is set to this.
	 */
	public void setFromGame(boolean state) {
		fromGame = state;
	}
	/**{@link #nullCurrentLevel()} sets {@link #currentLevel} to null.*/
	public void nullCurrentLevel() {
		currentLevel = null;
	}
	/**{@link #getFromGame()} returns {@link #fromGame}: whether a screen transition came from a minigame.*/
	public boolean getFromGame() {
		return fromGame;
	}

	/**{@link #create()} is called when the game is initially launched. Initializes files, and variables needed.*/
	@Override
	public void create () {
		switch(Gdx.app.getType()){
		case Android:
			Gdx.input.setCatchBackKey(true);
			break;
			//if using the desktop set the width and height to a 16:9 resolution.
		case Desktop:
			Gdx.graphics.setDisplayMode(DESKTOP_WIDTH, DESKTOP_HEIGHT, false);
			break;
		case iOS:
			break;
		default:
			break;
		}
		//Timer for loading screen delay before transition to main screen
		screenTransitionTimeLeft = SCREEN_TRANSITION_TIMER;
		//Assets to be added to the manager
		manager.load("MainScreen_Splash.png", Texture.class);
		manager.load("ColdMountain.png", Texture.class);
		manager.load("SemiTransparentBG.png", Texture.class);
		manager.finishLoadingAsset("SemiTransparentBG.png");
		//System.out.println(manager.isLoaded("SemiTransparentBG.png"));
		//initial update so that the loading screen is loaded before everything
		manager.update();
		manager.load("Packs/Buttons.pack", TextureAtlas.class);
		manager.load("Packs/ButtonsEditor.pack", TextureAtlas.class);
		manager.load("Packs/CheckBoxes.pack", TextureAtlas.class);
		manager.load("ObjectImages/ball.png", Texture.class);
		manager.load("ObjectImages/ballSelected.png", Texture.class);
		manager.load("ObjectImages/Box.png", Texture.class);
		manager.load("ObjectImages/BoxHalf.png", Texture.class);
		manager.load("ObjectImages/BoxHalfSelected.png", Texture.class);
		manager.load("ObjectImages/Grid.png", Texture.class);
		//manager.load("editorAssets/GridLarge.png", Texture.class);
		manager.load("Packs/Slider.pack", TextureAtlas.class);
		manager.load("sounds/backgroundMusic/01_A_Night_Of_Dizzy_Spells.wav", Music.class);
		manager.load("sounds/backgroundMusic/02_Underclocked_underunderclocked_mix_.wav", Music.class);
		manager.load("sounds/backgroundMusic/03_Chibi_Ninja.wav", Music.class);
		manager.load("sounds/backgroundMusic/04_All_of_Us.wav", Music.class);
		manager.load("sounds/backgroundMusic/05_Come_and_Find_Me.wav", Music.class);
		manager.load("sounds/backgroundMusic/06_Searching.wav", Music.class);
		manager.load("sounds/backgroundMusic/07_We_39_re_the_Resistors.wav", Music.class);
		manager.load("sounds/backgroundMusic/08_Ascending.wav", Music.class);
		manager.load("sounds/backgroundMusic/09_Come_and_Find_Me.wav", Music.class);
		manager.load("sounds/backgroundMusic/10_Arpanauts.wav", Music.class);
		manager.load("mapAssets/UrbanMaze1Backdrop.png", Texture.class);

		manager.load("ObjectImages/Banana1.png", Texture.class);
		manager.load("ObjectImages/Cherries1.png", Texture.class);
		manager.load("ObjectImages/Cherries2.png", Texture.class);
		manager.load("ObjectImages/Apple1.png", Texture.class);
		manager.load("ObjectImages/Grapes_dark.png", Texture.class);
		manager.load("ObjectImages/Grapes_light.png", Texture.class);
		manager.load("ObjectImages/Watermellon1.png", Texture.class);
		manager.load("ObjectImages/Icecream1.png", Texture.class);
		manager.load("ObjectImages/Cherry.png", Texture.class);
		manager.load("ObjectImages/basket.png", Texture.class);
		manager.load("ObjectImages/bowl.png", Texture.class);
		manager.load("ObjectImages/basket_new.png", Texture.class);
		manager.load("ObjectImages/Grape.png", Texture.class);
		manager.load("ObjectImages/Candy1.png", Texture.class);
		manager.load("ObjectImages/Candy2.png", Texture.class);
		manager.load("ObjectImages/Kiwi.png", Texture.class);
		manager.load("ObjectImages/Lollipop1.png", Texture.class);
		manager.load("ObjectImages/Lollipop2.png", Texture.class);
		manager.load("ObjectImages/Lollipop3.png", Texture.class);
		manager.load("ObjectImages/Sundae1.png", Texture.class);
		manager.load("ObjectImages/Sundae2.png", Texture.class);
		manager.load("ObjectImages/heart.png", Texture.class);
		manager.load("ObjectImages/Dirt1.png", Texture.class);
		manager.load("ObjectImages/Dirt2.png", Texture.class);
		manager.load("ObjectImages/Dirt3.png", Texture.class);
		manager.load("ObjectImages/Sponge_REPLACEME.png", Texture.class);
		
		manager.load("Backgrounds/black.png", Texture.class);
		manager.load("Backgrounds/Table.png", Texture.class);
		manager.load("Backgrounds/Window.png", Texture.class);
		
		manager.finishLoading();
		backgroundSounds = new Array<Music>();
		backgroundSounds.add(manager.get("sounds/backgroundMusic/01_A_Night_Of_Dizzy_Spells.wav", Music.class));
		backgroundSounds.add(manager.get("sounds/backgroundMusic/02_Underclocked_underunderclocked_mix_.wav", Music.class));
		backgroundSounds.add(manager.get("sounds/backgroundMusic/03_Chibi_Ninja.wav", Music.class));
		backgroundSounds.add(manager.get("sounds/backgroundMusic/04_All_of_Us.wav", Music.class));
		backgroundSounds.add(manager.get("sounds/backgroundMusic/05_Come_and_Find_Me.wav", Music.class));
		backgroundSounds.add(manager.get("sounds/backgroundMusic/06_Searching.wav", Music.class));
		backgroundSounds.add(manager.get("sounds/backgroundMusic/07_We_39_re_the_Resistors.wav", Music.class));
		backgroundSounds.add(manager.get("sounds/backgroundMusic/08_Ascending.wav", Music.class));
		backgroundSounds.add(manager.get("sounds/backgroundMusic/09_Come_and_Find_Me.wav", Music.class));
		backgroundSounds.add(manager.get("sounds/backgroundMusic/10_Arpanauts.wav", Music.class));

		reader = new XML_Reader();
		writer = new XML_Writer();
		ScreenAdapterManager.getInstance().initialize(this);

		//button stuff
		bitmapFontButton = new BitmapFont();
		//create the stage
		stage = new TGC_Stage();

		//Game input processor
		InputMultiplexer mp = new InputMultiplexer();
		mp.addProcessor(stage);
		Gdx.input.setInputProcessor(mp);
		
		//set the height and width to the Gdx graphics dimensions
		width = Gdx.graphics.getWidth();
		height = Gdx.graphics.getHeight();
		//Map stuff
		mapCamera = new OrthographicCamera();
		mapCamera.setToOrtho(false, width, height);

		camera = new OrthographicCamera();
		viewport = new StretchViewport(16, 9, camera);
		viewport.apply();
		camera.position.set(camera.viewportWidth/2, camera.viewportHeight/2, 0);

		batch = new SpriteBatch();
		
		//LOAD NOT NECESSARY YET
		loadLevelPackets();
		
		soundEnabled = true;
		musicEnabled = true;
		muteAll = false;
		volume = .75f;
		backgroundSoundToPlay = backgroundSounds.random();
		if(musicEnabled && !muteAll){
			backgroundSoundToPlay.setVolume(volume);
		}
		else{
			backgroundSoundToPlay.setVolume(0f);
		}
		backgroundSoundToPlay.play();
	}

	/**{@link #dispose()} handles the resource disposal when {@link TGC_Engine} exits.*/
	@Override
	public void dispose(){
		super.dispose();
		//dispose the screen manager, and in doing so all screens
		ScreenAdapterManager.getInstance().dispose();
		batch.dispose();
		for (Music m : backgroundSounds) {
			m.dispose();
		}
	};
	/**{@link #getBitmapFontButton()} returns {@link #bitmapFontButton}.*/
	public BitmapFont getBitmapFontButton(){
		return bitmapFontButton;
	}
	/**{@link #getButtonAtlasNamesArray()} returns {@link #buttonAtlasNamesArray}. */
	public String[] getButtonAtlasNamesArray() {
		return buttonAtlasNamesArray;
	}
	/**{@link #getButtonAtlasSkin()} returns {@link #skin}. */
	public Skin getButtonAtlasSkin(){
		return skin;
	}
	/**{@link #getButtonStates()} returns {@link #BUTTON_STATES}. */
	public int getButtonStates(){
		return BUTTON_STATES;
	}
	/**{@link #getHeight()} returns {@link #height}. */
	public float getHeight(){
		return height;
	}
	/**{@link #getStage()} returns {@link #stage}. */
	public Stage getStage() {
		return stage;
	}	
	/**{@link #getWidth()} returns {@link #width}. */
	public float getWidth(){
		return width;
	}
	/**{@link #getAssetManager()} returns {@link #manager}. */
	public AssetManager getAssetManager() {
		return manager;
	}
	/**{@link #getXML_Writer()} returns {@link #writer}. */
	public XML_Writer getXML_Writer() {
		return writer;
	}
	/**{@link #getLevelPackets()} returns {@link #levelPackets}. */
	public Array<LevelPacket> getLevelPackets() {
		return levelPackets;
	}
	/**{@link #getCurrentLevel()} returns {@link #currentLevel}.*/
	public Level getCurrentLevel() {
		return currentLevel;
	}
	
	/**{@link #render()} handles rendering the main stage, as well as calling the render of the current {@link ScreenAdapter} being shown.*/
	@Override
	public void render () {
		camera.update();
		// DRAWS SPLASH SCREEN
		if(!ScreenAdapterManager.getInstance().screenTransitionInComplete && !gameStart) {
			batch.begin();
			batch.draw((Texture) manager.get("MainScreen_Splash.png"), 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
			batch.end();
			splashScreenTimer -= Gdx.graphics.getDeltaTime();
		}
		// RUNS WHEN SPLASH SCREEN IS DONE
		if(ScreenAdapterManager.getInstance().screenTransitionInComplete && !gameStart && splashScreenTimer <= 0) {
			gameStart = true;
		}
		
		if(manager.update()) {
			//timer to determine whether to continue displaying loading screen
			//or to switch to displaying the main screen
			if(screenTransitionTimeLeft <= 0){
				if(musicEnabled && !muteAll){
					backgroundSoundToPlay.setVolume(volume);
				}
				else{
					backgroundSoundToPlay.setVolume(0f);
				}
				if(!backgroundSoundToPlay.isPlaying() && !muteAll){
					backgroundSoundToPlay = backgroundSounds.random();
					backgroundSoundToPlay.play();
				}
				//working on sound control through options
				super.render();
				if(manager.isLoaded("Packs/Buttons.pack")) {
					skin.addRegions((TextureAtlas)(manager.get("Packs/Buttons.pack")));
				}
				if(manager.isLoaded("Packs/ButtonsEditor.pack")) {
					skin.addRegions((TextureAtlas) manager.get("Packs/ButtonsEditor.pack"));
				}
				//makes sure the main screen is only loaded once
				if(!screenManagerLoaded){
					//show the main screen to be displayed first
					ScreenAdapterManager.getInstance().show(ScreenAdapterEnums.MAIN);
					//boolean = true so the loop is not entered again
					screenManagerLoaded = true;
				}
			}
		}
		//decrements transition time
		if(screenTransitionTimeLeft >= 0){
			screenTransitionTimeLeft -= Gdx.graphics.getDeltaTime();
		}
		
		stage.draw();
		
		if(ScreenAdapterManager.getInstance().screenTransitionInComplete) {
			ScreenAdapterManager.getInstance().screenTransitionOut();	
		}
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
