package org.TheGivingChild.Engine;

import org.TheGivingChild.Engine.Attributes.WinEnum;
import org.TheGivingChild.Engine.XML.GameObject;
import org.TheGivingChild.Engine.XML.Level;
import org.TheGivingChild.Engine.XML.LoseEnum;
import org.TheGivingChild.Engine.XML.XML_Reader;
import org.TheGivingChild.Engine.XML.XML_Writer;
import org.TheGivingChild.Screens.ScreenAdapterEnums;
import org.TheGivingChild.Screens.ScreenAdapterManager;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.assets.AssetManager;
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
	private String[] buttonAtlasNamesArray = {"ButtonPressed_MainScreen_Play", "Button_MainScreen_Play", "ButtonPressed_MainScreen_HowToPlay", "Button_MainScreen_HowToPlay", "ButtonPressed_MainScreen_Editor", "Button_MainScreen_Editor", "ButtonPressed_MainScreen_Options", "Button_MainScreen_Options", "ButtonPressed_MainScreen_Maze", "Button_MainScreen_Maze"};
	//skin from atlas
	private Skin skin = new Skin();
	//bitmap font for buttons
	private BitmapFont bitmapFontButton;
    private Array<Level> levels = new Array<Level>();
    
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
   
	public void addLevels(Array<Level> levels){
			this.levels.addAll(levels);
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
		manager.load("DenverSkyline.jpg", Texture.class);
		//initial update so that the loading screen is loaded before everything
		manager.update();
		manager.load("Packs/Buttons.pack", TextureAtlas.class);
		manager.load("Packs/ButtonsEditor.pack", TextureAtlas.class);
		manager.load("Packs/CheckBoxes.pack", TextureAtlas.class);
		manager.load("editorAssets/ball.png", Texture.class);
		manager.load("editorAssets/ballSelected.png", Texture.class);
		manager.load("editorAssets/Box.png", Texture.class);
		manager.load("editorAssets/BoxHalf.png", Texture.class);
		manager.load("editorAssets/BoxHalfSelected.png", Texture.class);
		manager.load("editorAssets/Grid.png", Texture.class);
		//manager.load("editorAssets/GridLarge.png", Texture.class);
		manager.load("TEMPORARY_Cartoon_City.png", Texture.class);
		manager.load("TEMPORARY_Cartoon_Forest.jpg", Texture.class);
		manager.finishLoading();
		//levels for testing packet manager.
		levels.add(new Level("level1", "packet1", "badlogic.jpg", new Array<WinEnum>(), new Array<LoseEnum>(), new Array<GameObject>()));
		levels.add(new Level("level2", "packet1", "badlogic.jpg", new Array<WinEnum>(), new Array<LoseEnum>(), new Array<GameObject>()));

		reader = new XML_Reader();
		writer = new XML_Writer();
		
		boolean exists = Gdx.files.internal("testOut.xml").exists();
		System.out.println(exists);
		reader.setupNewFile(Gdx.files.internal("testOut.xml"));
		XML_Reader reader = new XML_Reader();
		
		exists = Gdx.files.internal("testOut.xml").exists();
		System.out.println(exists);
		reader.setupNewFile(Gdx.files.internal("testOut.xml"));

		ScreenAdapterManager.getInstance().initialize(this);
		ScreenAdapterManager.getInstance().game.getLevels().set(0, reader.compileLevel());

		levels.set(0, reader.compileLevel());
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
	@Override
	public void render () {
		camera.update();
		if(!ScreenAdapterManager.getInstance().screenTransitionInComplete && gameStart < 1) {
			batch.begin();
			batch.draw((Texture) manager.get("MainScreen_Splash.png"), 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
			batch.end();
		}
		if(ScreenAdapterManager.getInstance().screenTransitionInComplete)
			gameStart++;
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
		if(ScreenAdapterManager.getInstance().screenTransitionInComplete)
			ScreenAdapterManager.getInstance().screenTransitionOut();

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
	
}
