package org.TheGivingChild.Screens;

import org.TheGivingChild.Engine.MyChangeListener;
import org.TheGivingChild.Engine.TGC_Engine;
import org.TheGivingChild.Screens.UI.CurtainScreenTransition;
import org.TheGivingChild.Screens.UI.UIScreenAdapter;

import com.badlogic.gdx.Application.ApplicationType;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Button.ButtonStyle;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;

public class ScreenMazeSelect extends UIScreenAdapter {
	private TGC_Engine game;
	// List of maze names for the super tots levels
	private static String[] superTotsLevels = { "UrbanTots1", "UrbanTots2", "UrbanTots3" };
	// List of maze names for the regular levels
	private static String[] regularLevels = { "UrbanMaze1", "UrbanMaze2" , "UrbanMaze3"};
	// Array of created buttons for picking tots levels
	private Table superTotsButtons;
	// Array of created buttons for regularLevels
	private Table regularButtons;
	// Table to hold the switch buttons
	private Table switchTable;
	// Size of the select level buttons
	private static final float SELECT_BUTTON_SIZE = 1/2f * Gdx.graphics.getHeight();
	// Size of the switch buttons
	private static final float SWITCH_BUTTON_WIDTH = Gdx.graphics.getWidth()/8f;
	private static final float SWITCH_BUTTON_HEIGHT = Gdx.graphics.getHeight()/8f;
	
	public ScreenMazeSelect() {
		// batch for ui screen transition
		batch = new SpriteBatch();
		game = ScreenAdapterManager.getInstance().game;
		background = game.getAssetManager().get("UIBackgrounds/mazeselect.png", Texture.class);
		// Construct the level selection switch buttons
		switchTable = constructSwitchButtons();
	}
	
	// Creates buttons for each of the passed level names wrapped in a horizontal scroll pane
	public Table constructButtons(final String[] levelNames, final String unlockDataName) {
		Table row = new Table();
		// Get the directory handle to get preview images
		FileHandle dir;
		if (Gdx.app.getType() == ApplicationType.Desktop) {
			dir = Gdx.files.internal("./bin/MazeAssets/");
		} else dir = Gdx.files.internal("MazeAssets/");
		
		// Get the number of buttons that should be enabled
		int unlocked = game.data.getNumberLevelsUnlocked(unlockDataName);
		
		for (int i = 0; i < levelNames.length; i++) {
			// Get the preview texture
			FileHandle location = dir.child(levelNames[i]).child("preview.png");
			Texture tex = game.getAssetManager().get(location.path(), Texture.class);
			// Create button
			ButtonStyle bs = new ButtonStyle();
			bs.up = new TextureRegionDrawable(new TextureRegion(tex));
			bs.down = new TextureRegionDrawable(new TextureRegion(tex));
			bs.disabled = new TextureRegionDrawable(new TextureRegion(game.getAssetManager().get("locked.png", Texture.class)));
			bs.pressedOffsetX = 10;
			bs.pressedOffsetY = -10;
			Button toAdd = new Button(bs);
			toAdd.setSize(SELECT_BUTTON_SIZE, SELECT_BUTTON_SIZE);
			// Set disabled if not unlocked
			if (i >= unlocked) toAdd.setDisabled(true);
			// Set listener
			final int j = i;
			toAdd.addListener(new MyChangeListener() {
				@Override
				public void changed(ChangeEvent event, Actor actor) {
					super.changed(event, actor);
					// Set the active maze
					ScreenMaze.activeMaze = levelNames[j];
					// Set the mazeNumber and type
					ScreenMaze.mazeNumber = j + 1;
					ScreenMaze.mazeType = unlockDataName;
					// Create a transition with a requested maze init call
					CurtainScreenTransition selectToMaze = new CurtainScreenTransition(ScreenAdapterEnums.MAZE_SELECT, ScreenAdapterEnums.MAZE, true);
					game.setScreen(selectToMaze);
				}
			});
			// Add button
			row.add(toAdd).size(SELECT_BUTTON_SIZE, SELECT_BUTTON_SIZE).pad(0, SELECT_BUTTON_SIZE/4, 0, SELECT_BUTTON_SIZE/4);
		}
		// Wrap in a scroll pane
		ScrollPane buttonScrollPane = new ScrollPane(row);
		Table buttons = new Table();
		buttons.add(buttonScrollPane).fill().expand();
		buttons.setSize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight()/2);
		buttons.align(Align.bottomLeft);
		buttons.setPosition(0, Gdx.graphics.getHeight()/4);

		return buttons;
	}
	
	// Constructs the buttons to switch level types and returns a table of them
	public Table constructSwitchButtons() {
		Table row = new Table();
		// Get textures
		Texture tots = game.getAssetManager().get("totsSwitchButton.png", Texture.class);
		Texture kids = game.getAssetManager().get("kidsSwitchButton.png", Texture.class);
		// Create and add tots button
		ButtonStyle bs = new ButtonStyle();
		bs.up = new TextureRegionDrawable( new TextureRegion(tots) );
		bs.down = new TextureRegionDrawable( new TextureRegion(tots) );
		final Button totsButton = new Button(bs);
		totsButton.setSize(SWITCH_BUTTON_WIDTH, SWITCH_BUTTON_HEIGHT);
		totsButton.addListener(new MyChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				super.changed(event, actor);
				regularButtons.remove();
				ScreenAdapterManager.getInstance().game.getStage().addActor(superTotsButtons);
			}
		});
		row.add(totsButton).size(SWITCH_BUTTON_WIDTH, SWITCH_BUTTON_HEIGHT).pad(SWITCH_BUTTON_WIDTH/2);
		
		// Create and add kids button
		bs = new ButtonStyle();
		bs.up = new TextureRegionDrawable( new TextureRegion(kids) );
		bs.down = new TextureRegionDrawable( new TextureRegion(kids) );
		final Button kidsButton = new Button(bs);
		kidsButton.setSize(SWITCH_BUTTON_WIDTH, SWITCH_BUTTON_HEIGHT);
		kidsButton.addListener(new MyChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				super.changed(event, actor);
				superTotsButtons.remove();
				ScreenAdapterManager.getInstance().game.getStage().addActor(regularButtons);
			}
		});
		row.add(kidsButton).size(SWITCH_BUTTON_WIDTH, SWITCH_BUTTON_HEIGHT).pad(SWITCH_BUTTON_WIDTH/2);
		
		// Table settings
		row.setSize(Gdx.graphics.getWidth()/2, Gdx.graphics.getHeight()/4);
		row.align(Align.bottomLeft);
		row.setPosition(Gdx.graphics.getWidth()/4, Gdx.graphics.getHeight() * 3/4f);
		
		return row;
	}
	
	@Override
	public void render(float delta) {
		batch.begin();
		batch.draw(background, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		batch.end();
	}
	
	@Override
	public void show() {
		// Construct game buttons
		superTotsButtons = constructButtons(superTotsLevels, "tots");
		regularButtons = constructButtons(regularLevels, "kids");
		// Show the buttons groups
		game.getStage().addActor(regularButtons);
		game.getStage().addActor(switchTable);
	}
	
	@Override
	public void hide() {
		//hide the button groups
		regularButtons.remove();
		superTotsButtons.remove();
		switchTable.remove();
	}
	
	@Override
	public void dispose() {
		batch.dispose();
	}
	
	// Request assets for this screen to be loaded.  Called on transition into this screen.
	public static void requestAssets(AssetManager manager) {
		// Load all button previews
		FileHandle dir;
		if (Gdx.app.getType() == ApplicationType.Desktop) {
			dir = Gdx.files.internal("./bin/MazeAssets/");
		} else dir = Gdx.files.internal("MazeAssets/");
		// Regular
		for (int i = 0; i < regularLevels.length; i++) {
			// Get the preview texture
			FileHandle location = dir.child(regularLevels[i]).child("preview.png");
			manager.load(location.path(), Texture.class);	
		}
		// Tots
		for (int i = 0; i < superTotsLevels.length; i++) {
			// Get the preview texture
			FileHandle location = dir.child(superTotsLevels[i]).child("preview.png");
			manager.load(location.path(), Texture.class);
		}
		
		// Load switch buttons
		manager.load("totsSwitchButton.png", Texture.class);
		manager.load("kidsSwitchButton.png", Texture.class);
		// Load locked image
		manager.load("locked.png", Texture.class);
		manager.load("UIBackgrounds/mazeselect.png", Texture.class);
	}
}
