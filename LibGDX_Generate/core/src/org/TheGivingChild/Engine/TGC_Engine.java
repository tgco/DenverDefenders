package org.TheGivingChild.Engine;

import org.TheGivingChild.Engine.Attributes.WinEnum;
import org.TheGivingChild.Engine.XML.GameObject;
import org.TheGivingChild.Engine.XML.Level;
import org.TheGivingChild.Engine.XML.LevelGoal;
import org.TheGivingChild.Engine.XML.LoseEnum;
import org.TheGivingChild.Screens.ScreenAdapterEnums;
import org.TheGivingChild.Screens.ScreenAdapterManager;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;

public class TGC_Engine extends Game {
	final static int DESKTOP_WIDTH = 1024;
	final static int DESKTOP_HEIGHT = 576;
	final static int BUTTON_STATES = 2;//corresponds to how many states each button has for the Buttons.pack textures pack.
	//create the stage for our actors
	private Stage stage;
	//button atlas reference names
	private String[] buttonAtlasNamesArray = {"ButtonPressed_MainScreen_Play", "Button_MainScreen_Play", "ButtonPressed_MainScreen_HowToPlay", "Button_MainScreen_HowToPlay", "ButtonPressed_MainScreen_Editor", "Button_MainScreen_Editor", "ButtonPressed_MainScreen_Options", "Button_MainScreen_Options"};
	//skin from atlas
	private Skin skin = new Skin();
	//bitmap font for buttons
	private BitmapFont bitmapFontButton;
	//create tables for the UI
    private Table rootTable;
    private Array<Level> levels = new Array<Level>();
    
    private boolean screenManagerLoaded = false;
    
    private float width;
    private float height;
    private final static float SCREEN_TRANSITION_TIMER = 1.0f;
    private float screenTransitionTimeLeft;
    private SpriteBatch batch;
    //Asset Manager to store assets
    private AssetManager manager = new AssetManager();
    
	public void addLevels(Array<Level> levels){
			this.levels.addAll(levels);
	}

	public void addTable(Table t){
		rootTable.add(t).align(Align.bottomLeft);
	}
	
	@Override
	public void create () {
		switch(Gdx.app.getType()){
			case Android:
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
		//initial update so that the loading screen is loaded before everything
		manager.update();
		manager.load("Packs/Buttons.pack", TextureAtlas.class);
		manager.load("Packs/ButtonsEditor.pack", TextureAtlas.class);
		manager.load("ball.png", Texture.class);
		manager.load("Box.png", Texture.class);
		manager.load("BoxHalf.png", Texture.class);
		manager.load("Grid.png", Texture.class);
		manager.load("HowToPlay.png", Texture.class);
		manager.load("HowToPlayMessage.png", Texture.class);
		manager.load("optionsTitle.png", Texture.class);
		manager.load("editorAssets/ball.png", Texture.class);
		manager.load("editorAssets/ballSelected.png", Texture.class);
		manager.load("editorAssets/Box.png", Texture.class);
		manager.load("editorAssets/BoxHalf.png", Texture.class);
		manager.load("editorAssets/BoxHalfSelected.png", Texture.class);
		manager.load("editorAssets/Grid.png", Texture.class);
		batch = new SpriteBatch();
		//levels for testing packet manager.
		levels.add(new Level("level1", "packet1", "badlogic.jpg", new Array<WinEnum>(), new Array<LoseEnum>(), new Array<GameObject>()));
		levels.add(new Level("level2", "packet1", "badlogic.jpg", new Array<WinEnum>(), new Array<LoseEnum>(), new Array<GameObject>()));
		levels.add(new Level("level3", "packet2", "badlogic.jpg", new Array<WinEnum>(), new Array<LoseEnum>(), new Array<GameObject>()));
		levels.add(new Level("level4", "packet2", "badlogic.jpg", new Array<WinEnum>(), new Array<LoseEnum>(), new Array<GameObject>()));
		levels.add(new Level("level5", "packet3", "badlogic.jpg", new Array<WinEnum>(), new Array<LoseEnum>(), new Array<GameObject>()));
		levels.add(new Level("level6", "packet3", "badlogic.jpg", new Array<WinEnum>(), new Array<LoseEnum>(), new Array<GameObject>()));
		levels.add(new Level("level7", "packet4", "badlogic.jpg", new Array<WinEnum>(), new Array<LoseEnum>(), new Array<GameObject>()));
		levels.add(new Level("level8", "packet4", "badlogic.jpg", new Array<WinEnum>(), new Array<LoseEnum>(), new Array<GameObject>()));
		
		
		//button stuff
        bitmapFontButton = new BitmapFont();
        
		//create the stage
		createStage();
		
		//set the height and width to the Gdx graphics dimensions
		width = Gdx.graphics.getWidth();
		height = Gdx.graphics.getHeight();
		
		
	}
	
	public void createStage(){
		stage = new Stage();
		//create main menu images
		Gdx.input.setInputProcessor(stage);
		//initialize root Table
		rootTable = new Table();
		//add the rootTable to the stage
		stage.addActor(rootTable);
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
	
	public Table getRootTable() {
		return rootTable;
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
	
	public void removeTable(Table t){
		rootTable.removeActor(t);
	}
	
	@Override
	public void render () {
		//if the manager is not done updating, it will display a loading image
		if(!manager.update()) {
			batch.begin();
			batch.draw((Texture) manager.get("MainScreen_Splash.png"), 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
			batch.end();
		}
		//once the manager is done updating, it prepares to switch to the main screen
		else {
			//timer to determine whether to continue displaying loading screen
			//or to switch to displaying the main screen
			if(screenTransitionTimeLeft <= 0){
				super.render();
				stage.draw();
				//if the texture atlas is loaded, then add it to the skin
				if(manager.isLoaded("Packs/Buttons.pack")) {
		        	skin.addRegions((TextureAtlas)(manager.get("Packs/Buttons.pack")));
		        }
				if(manager.isLoaded("Packs/ButtonsEditor.pack")) {
					skin.addRegions((TextureAtlas) manager.get("Packs/ButtonsEditor.pack"));
				}
				//makes sure the main screen is only loaded once
				if(!screenManagerLoaded){
					//initialize the Screen manager, passing the engine to it for reference
					ScreenAdapterManager.getInstance().initialize(this);
					//show the main screen to be displayed first
					ScreenAdapterManager.getInstance().show(ScreenAdapterEnums.MAIN);
					screenManagerLoaded = true;
				}
			}
		}
		//increments the timer to check if we are still delaying the main screen
		if(screenTransitionTimeLeft >= 0){
			screenTransitionTimeLeft -= Gdx.graphics.getDeltaTime();
		}
	}
}
