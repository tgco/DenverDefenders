package org.TheGivingChild.Screens;

import org.TheGivingChild.Engine.AudioManager;
import org.TheGivingChild.Engine.MyChangeListener;
import org.TheGivingChild.Engine.TGC_Engine;
import org.TheGivingChild.Engine.Maze.Direction;
import org.TheGivingChild.Screens.UI.ComicScreenTransition;
import org.TheGivingChild.Screens.UI.UIScreenAdapter;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Button.ButtonStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;


class ScreenOptions extends UIScreenAdapter {
	private Table optionsTable;
	private TGC_Engine game;
	private Texture title;
	
	private final static float CHECK_SIZE = Gdx.graphics.getWidth()/20f;
	private static final float RESET_WIDTH = Gdx.graphics.getWidth()/8f;
	private static final float RESET_HEIGHT = Gdx.graphics.getHeight()/10f;

	public ScreenOptions() {
		game = ScreenAdapterManager.getInstance().game;
		background = game.getAssetManager().get("UIBackgrounds/options.png", Texture.class);
		batch = new SpriteBatch();
		optionsTable = createOptionsTable();
		title = game.getAssetManager().get("titleOptionScreen.png", Texture.class);
	}
	
	@Override
	public void render(float delta) {
		// Title + Background
		batch.begin();
		batch.draw(background, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		batch.draw(title, (Gdx.graphics.getWidth()-title.getWidth())/2, Gdx.graphics.getHeight()-2*title.getHeight());
		batch.end();
	}
	
	@Override
	public void show() {
		game.getStage().addActor(optionsTable);
	};
	
	/**
	 * Removes table it does not show on other screens.
	 */
	@Override
	public void hide() {
		optionsTable.remove();
	}
	
	@Override
	public void dispose() {
		batch.dispose();
	}
	
	/**
	 * Creates the table that holds the back button.
	 */
	private Table createOptionsTable() {
		// Create the music and sound check buttons
		ButtonStyle bs = new ButtonStyle();
		Skin skin = new Skin();
		skin.addRegions(game.getAssetManager().get("Packs/CheckBoxes.pack", TextureAtlas.class));
		bs.checked = skin.getDrawable("CheckBox_Checked");
		bs.up = skin.getDrawable("CheckBox");
		Button musicCheck = new Button(bs);
		if (AudioManager.getInstance().musicEnabled)
			musicCheck.setChecked(true);
		else
			musicCheck.setChecked(false);
		musicCheck.addListener(new MyChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				super.changed(event, actor);
				// Set audio setting
				Button thisButton = (Button) actor;
				AudioManager.getInstance().setMusicEnabled(thisButton.isChecked());
			}
		});
		Button soundCheck = new Button(bs);
		if (AudioManager.getInstance().soundEnabled)
			soundCheck.setChecked(true);
		else
			soundCheck.setChecked(false);
		soundCheck.addListener(new MyChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				super.changed(event, actor);
				// Set audio setting
				Button thisButton = (Button) actor;
				AudioManager.getInstance().setSoundEnabled(thisButton.isChecked());
			}
		});
		// Check button labels
		LabelStyle ls = new LabelStyle();
		ls.font = game.getBitmapFontButton();
		Label music = new Label("Music", ls);
		music.setFontScale(game.getGlobalFontScale());
		Label sound = new Label("Sound", ls);
		sound.setFontScale(game.getGlobalFontScale());
		
		// Create the reset button
		bs = new ButtonStyle();
		bs.up = new TextureRegionDrawable(new TextureRegion(game.getAssetManager().get("resetButton.png", Texture.class)));
		bs.down = new TextureRegionDrawable(new TextureRegion(game.getAssetManager().get("resetButtonPressed.png", Texture.class)));
		Button resetButton = new Button(bs);
		resetButton.addListener(new MyChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				super.changed(event, actor);
				// Should set a confirm dialog here or touchup/down to implement holding the button for a while to reset
				Gdx.app.getPreferences("tgc_defenders_save").clear();
				// Flush to persist
				Gdx.app.getPreferences("tgc_defenders_save").flush();
				// Reload
				//game.data.save();
				game.data.load();
			}
		});
		
		// Create main menu button
		skin = new Skin();
		skin.addRegions((TextureAtlas) game.getAssetManager().get("Packs/Buttons.pack"));
		bs = new ButtonStyle();
		bs.up = skin.getDrawable("Button_MainScreen");
		bs.down = skin.getDrawable("ButtonPressed_MainScreen");
		Button main = new Button(bs);
		main.addListener(new MyChangeListener() { 			
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				super.changed(event, actor);
				// Save new audio settings
				game.data.save();
				ComicScreenTransition optionsToMain = new ComicScreenTransition(ScreenAdapterEnums.OPTIONS, ScreenAdapterEnums.MAIN, Direction.LEFT);
				game.setScreen(optionsToMain);
			}
		});
		
		// Create table/rows and add pieces
		Table checkBoxes = new Table();
		checkBoxes.setSize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight()/3f);
		checkBoxes.align(Align.center);
		checkBoxes.add(musicCheck).size(CHECK_SIZE, CHECK_SIZE);
		checkBoxes.add(music).pad(0, 0, 0, CHECK_SIZE*5f);
		checkBoxes.add(soundCheck).size(CHECK_SIZE, CHECK_SIZE);
		checkBoxes.add(sound);
		
		Table reset = new Table();
		reset.setSize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight()/4f);
		reset.align(Align.center);
		reset.add(resetButton).size(RESET_WIDTH, RESET_HEIGHT).pad(RESET_HEIGHT/2f, 0, RESET_HEIGHT/2f, 0);
		
		Table back = new Table();
		back.setSize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight()/4f);
		back.align(Align.bottom);
		back.add(main);
		
		Table table = new Table();
		table.setSize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		table.align(Align.bottom);
		table.add(checkBoxes).pad(0, 0, Gdx.graphics.getHeight()/10f, 0);
		table.row();
		table.add(reset).pad(0, 0, Gdx.graphics.getHeight()/10f, 0);
		table.row();
		table.add(back);

		return table;
	}

	public static void requestAssets(AssetManager manager) {
		manager.load("titleOptionScreen.png", Texture.class);
		manager.load("resetButton.png", Texture.class);
		manager.load("resetButtonPressed.png", Texture.class);
		manager.load("Packs/CheckBoxes.pack", TextureAtlas.class);
		manager.load("UIBackgrounds/options.png", Texture.class);
	}
}
