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
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Group;
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
 * @author all of us
 *
 */
public class TGC_Engine extends Game {
	private final static int DESKTOP_WIDTH = 1024;
	private final static int DESKTOP_HEIGHT = 576;
	private final static int BUTTON_STATES = 2;//corresponds to how many states each button has for the Buttons.pack textures pack.
	//create the stage for our actors
	private TGC_Stage stage;
	//button atlas reference names
	private String[] buttonAtlasNamesArray = {"ButtonPressed_MainScreen_Play", "Button_MainScreen_Play", "ButtonPressed_MainScreen_HowToPlay", "Button_MainScreen_HowToPlay", "ButtonPressed_MainScreen_Editor", "Button_MainScreen_Editor", "ButtonPressed_MainScreen_Options", "Button_MainScreen_Options"};
	//skin from atlas
	private Skin skin = new Skin();
	//bitmap font for buttons
	private BitmapFont bitmapFontButton;
    private Array<Level> levels = new Array<Level>();
    private Array<LevelPacket> levelPackets;
    private Level currentLevel;
    private boolean screenManagerLoaded = false;
    
    private float width;
    private float height;
    private final static float SCREEN_TRANSITION_TIMER = 1.0f;
    private float screenTransitionTimeLeft;
    private Group objectGroup;
    //Asset Manager to store assets
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
    
    private XML_Reader reader;
    private XML_Writer writer;
    //Map stuff
    private OrthographicCamera mapCamera;
    
    //Viewport stuff
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
     * @author ctokunag
     */
    private Viewport viewport;
    
    private Batch batch;
    private int gameStart = 0;
   
    private boolean levelWinOrLose;
    private boolean packetCompleted = false;
    private boolean fromGame = false;
    
	public void addLevels(Array<Level> levels){
			this.levels.addAll(levels);
	}
	
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
	
	public void selectLevel() {
		Array<Level> possibleLevels = new Array<Level>();
		for (Level newLevel: levelPackets.get(0).getLevels()) {
			if (!newLevel.getCompleted()) {
				possibleLevels.add(newLevel);
			}
		}
		
		if (possibleLevels.size == 0) {
			loadLevelPackets();
			for (Level newLevel: levelPackets.get(0).getLevels()) {
				if (!newLevel.getCompleted()) {
					possibleLevels.add(newLevel);
				}
			}
		}
		
		Random rand = new Random();
		int newLevelIndex = (rand.nextInt(1000)) % possibleLevels.size;
		currentLevel = possibleLevels.get(newLevelIndex);
		currentLevel.resetLevel();
		System.out.println(currentLevel.getLevelName());
	}
	
	public void levelCompleted(boolean winOrLose) {
		levelWinOrLose = winOrLose;
	}
	
	public boolean levelWin() {
		return levelWinOrLose;
	}
	
	public boolean getPacketCompleted() {
		return packetCompleted;
	}
	
	public void setFromGame(boolean state) {
		fromGame = state;
	}
	
	public void nullCurrentLevel() {
		currentLevel = null;
	}
	
	public boolean getFromGame() {
		return fromGame;
	}
	
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
		System.out.println(manager.isLoaded("SemiTransparentBG.png"));
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
		manager.load("TEMPORARY_Cartoon_City.png", Texture.class);
		manager.load("TEMPORARY_Cartoon_Forest.png", Texture.class);
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
		manager.finishLoading();
		
		reader = new XML_Reader();
		writer = new XML_Writer();
		ScreenAdapterManager.getInstance().initialize(this);
		//ScreenAdapterManager.getInstance().game.getLevels().set(0, reader.compileLevel());

		//levels.set(0, reader.compileLevel());
		//button stuff
        bitmapFontButton = new BitmapFont();
		//create the stage
		createStage();
		
		//set the height and width to the Gdx graphics dimensions
		width = Gdx.graphics.getWidth();
		height = Gdx.graphics.getHeight();
	
		
		//Game input processor
		//Gdx.input.setInputProcessor(input);
		objectGroup = new Group();
		//Iterate through current level and add objects to stage?
//		for(final GameObject o : levels.first().getGameObjects())
//		{
//			objectGroup.addActor(o);
//		}
			
		InputMultiplexer mp = new InputMultiplexer();
		mp.addProcessor(stage);
	//	mp.addProcessor(new UserInputListener());
		
		stage.addActor(objectGroup);
		
		stage.setKeyboardFocus(objectGroup);
		//stage.addTouchFocus(listener, listenerActor, target, pointer, button);
		
		Gdx.input.setInputProcessor(stage);
						
		//System.out.println("stage has this many actors:" + stage.getActors().size);
		//System.out.println("other processor has this many actors" + input.getActors().size);
		
		//Map stuff
		float w = Gdx.graphics.getWidth();
		float h = Gdx.graphics.getHeight();
		mapCamera = new OrthographicCamera();
		mapCamera.setToOrtho(false, w, h);
		
		/*Viewport stuff
		 * Sets up the camera that will keep track of the screen
		 * Creates the viewport to keep track of the aspect ratio
		 * Applies the viewport to the camera
		 * Repositions the camera accordingly
		*/
		camera = new OrthographicCamera();
		viewport = new StretchViewport(16, 9, camera);
		viewport.apply();
		camera.position.set(camera.viewportWidth/2, camera.viewportHeight/2, 0);
		
		batch = new SpriteBatch();
		
		loadLevelPackets();
	}
	public void createStage(){
		stage = new TGC_Stage();
		//create main menu images
		Gdx.input.setInputProcessor(stage);
	}
	//dispose of resources, done when the game is destroyed
	@Override
	public void dispose(){
		super.dispose();
		//dispose the screen manager, and in doing so all screens
		ScreenAdapterManager.getInstance().dispose();
	};
	//getters for accessing variables in other areas of the engine
	public BitmapFont getBitmapFontButton(){
		return bitmapFontButton;
	}
	public String[] getButtonAtlasNamesArray() {
		return buttonAtlasNamesArray;
	}
	public Skin getButtonAtlasSkin(){
		return skin;
	}
	public int getButtonStates(){
		return BUTTON_STATES;
	}
	public float getHeight(){
		return height;
	}
	public Array<Level> getLevels(){
		return levels;
	}
	public Stage getStage() {
		return stage;
	}	
	public float getWidth(){
		return width;
	}
	public AssetManager getAssetManager() {
		return manager;
	}
	public XML_Writer getXML_Writer() {
		return writer;
	}
	public Array<LevelPacket> getLevelPackets() {
		return levelPackets;
	}
	@Override
	public void render () {
		camera.update();
		if(!ScreenAdapterManager.getInstance().screenTransitionInComplete && gameStart < 1) {
			batch.begin();
			batch.draw((Texture) manager.get("MainScreen_Splash.png"), 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
			batch.end();
		}
		if(ScreenAdapterManager.getInstance().screenTransitionInComplete && gameStart < 1) {
			gameStart++;
		}
		/**
		 * This checks if the manager is done updating or not. If the manager is not 
		 * done loading, it will display a transition until it is done loading. Once 
		 * the manager is done updating, it will display another transition and move
		 * to the next screen.
		 * @author ctokunag
		 */
		if(manager.update()) {
			//timer to determine whether to continue displaying loading screen
			//or to switch to displaying the main screen
			if(screenTransitionTimeLeft <= 0){
				//stage.draw();
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
		//decrements the timer to check if we are still delaying the main screen
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
	 * @author ctokunag
	 */
	@Override
	public void resize(int width, int height) {
		viewport.update(width, height);
		camera.position.set(camera.viewportWidth/2, camera.viewportHeight/2, 0);
	}

	public Level getCurrentLevel() {
		// TODO Auto-generated method stub
		return currentLevel;
	}
	
}
