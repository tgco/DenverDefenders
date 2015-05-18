package org.TheGivingChild.Engine;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Array;
import com.sun.xml.internal.ws.api.streaming.XMLStreamReaderFactory.Default;

public class TGC_Engine extends Game {
	final static int DESKTOP_WIDTH = 1024;
	final static int DESKTOP_HEIGHT = 576;
	final static int BUTTON_STATES = 2;//corresponds to how many states each button has for the Buttons.pack textures pack.
	//create the stage for our actors
	private Stage stage;
	//button atlas reference names
	private String[] buttonAtlasNamesArray = {"ButtonPressed_MainScreen_Play", "Button_MainScreen_Play", "ButtonPressed_MainScreen_HowToPlay", "Button_MainScreen_HowToPlay", "ButtonPressed_MainScreen_Editor", "Button_MainScreen_Editor",/* "ButtonPressed_MainScreen_Options", "Button_MainScreen_Options"*/};
	//skin from atlas
	private Skin skin = new Skin();
	//bitmap font for buttons
	private BitmapFont bitmapFontButton;
	
	public ScreenAdapter[] screens;
    //create tables for the UI
    private Table rootTable;
    private Array<Level> levels = new Array<Level>();
    
    private float width;
    private float height;
    
	@Override
	public void create () {
		switch(Gdx.app.getType()){
			case Android:
				
			case Desktop:
				Gdx.graphics.setDisplayMode(DESKTOP_WIDTH, DESKTOP_HEIGHT, false);
			case iOS:
				
		}
		
		//levels for testing packet manager.
		levels.add(new Level("level1", "packet1", "badlogic.jpg", new LevelGoal(), new Array<GameObject>()));
		levels.add(new Level("level2", "packet1", "badlogic.jpg", new LevelGoal(), new Array<GameObject>()));
		levels.add(new Level("level3", "packet2", "badlogic.jpg", new LevelGoal(), new Array<GameObject>()));
		levels.add(new Level("level4", "packet2", "badlogic.jpg", new LevelGoal(), new Array<GameObject>()));
		levels.add(new Level("level5", "packet3", "badlogic.jpg", new LevelGoal(), new Array<GameObject>()));
		levels.add(new Level("level6", "packet3", "badlogic.jpg", new LevelGoal(), new Array<GameObject>()));
		levels.add(new Level("level7", "packet4", "badlogic.jpg", new LevelGoal(), new Array<GameObject>()));
		levels.add(new Level("level8", "packet4", "badlogic.jpg", new LevelGoal(), new Array<GameObject>()));
		
		//button stuff
        bitmapFontButton = new BitmapFont();
        //make an atlas using the button texture pack
        TextureAtlas buttonAtlas = new TextureAtlas("Packs/Buttons.pack");
        //define the regions
        skin.addRegions(buttonAtlas);
		
		createStage();
		width = Gdx.graphics.getWidth();
		height = Gdx.graphics.getHeight();
		screens = new ScreenAdapter[4];
		ScreenAdapter levelManager = new ScreenLevelManager(this);
		screens[0] = levelManager;
		ScreenAdapter howToPlay = new HowToPlay(this);
		screens[1] = howToPlay;
		ScreenAdapter options = new EditorScreen(this);
		screens[2]= options;
		ScreenAdapter mainScreen = new MainScreen(this);
		screens[3] = mainScreen;
		setScreen(screens[3]);
	}

	@Override
	public void render () {
		super.render();
		stage.draw();
	}
	
	public void createStage(){
		stage = new Stage();
		//create main menu images
		Gdx.input.setInputProcessor(stage);
		//initialize root Table
		rootTable = new Table();
	}
	
	public Array<Level> getLevels(){
		return levels;
	}
	
	public void addLevels(Array<Level> levels){
			this.levels.addAll(levels);
	}
	
	public void playLevels(LevelPacket packet){
		//this is where each level in the packet should be played in order.
		for(Level l: packet){
			System.out.println(l.getLevelName());
		}
	}
	
	public void addTable(Table t){
		rootTable.add(t);
	}
	public void removeTable(Table t){
		rootTable.removeActor(t);
	}
	
	public Skin getButtonAtlasSkin(){
		return skin;
	}
	
	public String[] getButtonAtlasNamesArray() {
		return buttonAtlasNamesArray;
	}
	
	public BitmapFont getBitmapFontButton(){
		return bitmapFontButton;
	}
	
	public float getWidth(){
		return width;
	}
	public float getHeight(){
		return height;
	}
	
	public Stage getStage() {
		return stage;
	}
	
	public Table getRootTable() {
		return rootTable;
	}
	
}
