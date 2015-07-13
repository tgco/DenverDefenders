package org.TheGivingChild.Screens;

import org.TheGivingChild.Engine.AudioManager;
import org.TheGivingChild.Engine.MyChangeListener;
import org.TheGivingChild.Engine.TGC_Engine;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Button.ButtonStyle;
import com.badlogic.gdx.scenes.scene2d.ui.CheckBox;
import com.badlogic.gdx.scenes.scene2d.ui.CheckBox.CheckBoxStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;

/**
 * This is the options screen. It has check boxes to control both sound and music or each individually.
 * It also creates a volume slider to give the user more control.
 * @author ctokunag
 *
 */
class ScreenOptions extends ScreenAdapter {
	private Skin skin;
	private Skin buttonSkin;
	private Table optionsTable;
	private Array<CheckBox> options;
	private Table choicesTable;
	private Table overallTable;
	private BitmapFont font;
	private ButtonStyle style;
	private CheckBoxStyle cbStyle;
	private CheckBox mute;
	private TGC_Engine game;
	private SpriteBatch batch;
	private Texture title;
	private String[] optionsArray = {"   Music   ", 
			  						 "   Sound   "};

	/**
	 * Instantiates the sprite batch and loads the assets it needs and initially creates the tables.
	 * Sets the check boxes to checked.
	 */
	public ScreenOptions() {
		game = ScreenAdapterManager.getInstance().game;
		batch = new SpriteBatch();
		optionsTable = createOptionsTable();
		overallTable = createOverallTable();
	}
	
	/**
	 * Renders the screen transition until it is complete.
	 * Then it renders the two tables that hold all of the buttons. 
	 */
	@Override
	public void render(float delta) {
		title = game.getAssetManager().get("titleOptionScreen.png");
		ScreenAdapterManager.getInstance().backgroundImage();
		
		batch.begin();
		batch.draw(title, (Gdx.graphics.getWidth()-title.getWidth())/2, Gdx.graphics.getHeight()-title.getHeight());
		batch.end();
	
		// NO NEED TO SET BOOLS EVERY FRAME, SHOULD HAPPEN ON CLICK LISTENER
		for(CheckBox c : options) {
			if(c.isChecked()) {
				if(c.equals(options.get(0))){
					AudioManager.getInstance().setMusicEnabled(true);
				}
				else if(c.equals(options.get(1))){
					AudioManager.getInstance().setSoundEnabled(true);
				}
			}
			else if(c.equals(options.get(0))){
				AudioManager.getInstance().setMusicEnabled(false);
			}
			else if(c.equals(options.get(1))){
				AudioManager.getInstance().setSoundEnabled(false);
			}
		}
	}
	
	
	/**
	 * Adds the two tables and resets a boolean so the screen transition is shown
	 */
	@Override
	public void show() {
		game.getStage().addActor(optionsTable);
		game.getStage().addActor(overallTable);
	};
	
	/**
	 * Removes all of the tables so they do not show on other screens.
	 * Resets the check boxes also.
	 */
	@Override
	public void hide() {
		optionsTable.remove();
		overallTable.remove();
	}
	
	@Override
	public void dispose() {
		skin.dispose();
		buttonSkin.dispose();
		batch.dispose();
	}
	
	/**
	 * Creates the table that holds the back button.
	 */
	private Table createOptionsTable() {
		//Sets up the needed variables and parameters
		Table table = new Table();
		skin = new Skin();
		skin.addRegions((TextureAtlas) game.getAssetManager().get("Packs/ButtonsEditor.pack"));
		//Creates the buttons and sets table to origin
		table.add(createButton());
		table.setSize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		table.align(Align.bottom);
		return table;
	}
	
	/**
	 * Creates the table that the sound and music check boxes are added to so they are formatted correctly.
	 */
	private Table createChoices() {
		Table table = new Table();
		buttonSkin = new Skin();
		buttonSkin.addRegions((TextureAtlas) game.getAssetManager().get("Packs/CheckBoxes.pack"));
		createCheckBoxes(table);
		return table;
	}
	
	/**
	 * Creates the check boxes used for the sound and the music toggle.
	 */
	private void createCheckBoxes(Table table) {
		options = new Array<CheckBox>();
		font = game.getBitmapFontButton();
		cbStyle = new CheckBoxStyle();
		// Wrap label background in skin to get as a Drawable
		Skin bSkin = new Skin();
		bSkin.add("background", game.getAssetManager().get("SemiTransparentBG.png"));
		cbStyle.font = font;
		cbStyle.checkboxOff = buttonSkin.getDrawable("CheckBox");
		cbStyle.checkboxOn = buttonSkin.getDrawable("CheckBox_Checked");
		for(int i = 0; i < optionsArray.length; i++) {
			CheckBox checkbox = new CheckBox("", cbStyle);
			LabelStyle ls = new LabelStyle();
			ls.font = font;
			ls.background = bSkin.getDrawable("background");
			Label label = new Label(optionsArray[i], ls);
			label.setFontScale(game.getGlobalFontScale());
			checkbox.setSize(0.2f*Gdx.graphics.getWidth(), 0.2f*Gdx.graphics.getHeight());
			checkbox.setScale(Gdx.graphics.getWidth()/(Gdx.graphics.getPpiX()*5));
			checkbox.setChecked(true);
			checkbox.addListener(new MyChangeListener(){
				@Override
				public void changed(ChangeEvent event, Actor actor) {
					super.changed(event, actor);
				}
			});
			table.add(checkbox).width(Gdx.graphics.getWidth()/4).height(Gdx.graphics.getHeight()/4);
			table.add(label);
			options.add(checkbox);
		}
	}
	
	/**
	 * Creates the back button to return to the main screen and it's listener.
	 */
	private Button createButton() {
		skin = new Skin();
		skin.addRegions((TextureAtlas) game.getAssetManager().get("Packs/Buttons.pack"));
		style = new ButtonStyle();
		style.up = skin.getDrawable("Button_MainScreen");
		style.down = skin.getDrawable("ButtonPressed_MainScreen");
		Button backButton = new Button(style);

		/**
		 * Creates the listener for the back button.
		 * Shows the main screen when pushed.
		 */
		backButton.addListener(new MyChangeListener() { 			
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				super.changed(event, actor);
				ScreenTransition optionsToMain = new ScreenTransition(ScreenAdapterEnums.OPTIONS, ScreenAdapterEnums.MAIN);
				game.setScreen(optionsToMain);
			}
		});
		backButton.setSize(Gdx.graphics.getWidth()/10f,Gdx.graphics.getHeight()/10f);
		return backButton;
	}
	 
	 /**
	  * Creates the table that will hold the sound and music table and the volume
	  * slider and adds and aligns them properly. 
	  */
	 private Table createOverallTable() {
		 Table table = new Table();
		 choicesTable = createChoices();
		 table.add(choicesTable);
		 table.row();
		 table.setSize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		 table.align(Align.center);
		 
		 return table;
	 }
	 
	 /**
	  * Getter for the music check box
	  * @return Returns whether or not the music box is checked
	  */
	 public boolean getMusic() {
		 return options.get(0).isChecked();
	 }
	 
	 /**
	  * Getter for the sound check box
	  * @return Returns whether or not the sound box is checked
	  */
	 public boolean getSound() {
		 return options.get(1).isChecked();
	 }
	 
	 /**
	  * Getter for the mute button
	  * @return Returns whether or not the mute button is selected
	  */
	 public boolean getMute() {
		 return mute.isChecked();
	 }

	public static void requestAssets(AssetManager manager) {
		manager.load("titleOptionScreen.png", Texture.class);
		manager.load("Packs/ButtonsEditor.pack", TextureAtlas.class);
		manager.load("Packs/CheckBoxes.pack", TextureAtlas.class);
	}
}
