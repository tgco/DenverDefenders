package org.TheGivingChild.Screens;

import org.TheGivingChild.Engine.TGC_Engine;

import sun.java2d.pipe.SpanClipRenderer;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener.ChangeEvent;
import com.badlogic.gdx.utils.Align;

class ScreenOptions extends ScreenAdapter {
	private Skin skin;
	private Skin buttonSkin;
	private Table optionsTable;
	private Table topRow;
	private Table bottomRow;
	private BitmapFont font;
	private TextButtonStyle style;
	private TGC_Engine game;
	private AssetManager manager;
	private SpriteBatch batch;
	private Texture title;
	private float screenTransitionTimeLeft = 1.0f;
	private boolean isRendered = false;
	private String[] optionsArray = {"Sounds", 
			  						 "Music", 
			  						 "Animation", 
			  						 "Color"};

	public ScreenOptions() {
		game = ScreenAdapterManager.getInstance().game;
		batch = new SpriteBatch();
		manager = game.getAssetManager();
		manager.load("optionsTitle.png", Texture.class);
		createOptionsTable();
		createRows();
	}
	@Override
	public void render(float delta) {
		if(manager.update()) {
			if(screenTransitionTimeLeft <= 0) {
				if(manager.isLoaded("optionsTitle.png"))
					title = manager.get("optionsTitle.png");
				Gdx.gl.glClearColor(1,1,0,1);
				Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
				batch.begin();
				batch.draw(title, (Gdx.graphics.getWidth()-title.getWidth())/2, Gdx.graphics.getHeight()-title.getHeight());
				batch.end();
				isRendered = true;
				show();
			}
		}
		if(screenTransitionTimeLeft >= 0)
			screenTransitionTimeLeft -= Gdx.graphics.getDeltaTime();
	}
	
	@Override
	public void show() {
		if(isRendered) {
			game.getStage().addActor(optionsTable);
			game.getStage().addActor(topRow);
			game.getStage().addActor(bottomRow);
		}
	};
	
	@Override
	public void hide() {
		optionsTable.remove();
		topRow.remove();
		bottomRow.remove();
	}
	
	private void createOptionsTable() {
		//Sets up the needed variables and parameters
		optionsTable = new Table();
		skin = new Skin();
		skin.addRegions((TextureAtlas) manager.get("Packs/ButtonsEditor.pack"));
		//Creates the buttons and sets table to origin
		createButton();
		optionsTable.setSize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		optionsTable.align(Align.bottom);
	}
	
	private void createRows() {
		topRow = new Table();
		bottomRow = new Table();
		buttonSkin = new Skin();
		buttonSkin.addRegions((TextureAtlas) manager.get("Packs/Buttons.pack"));
		createChoices();
		topRow.setSize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		bottomRow.setSize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		topRow.align(Align.left);
		bottomRow.align(Align.right);
	}

	private void createButton() {
		font = new BitmapFont();
		skin = new Skin();
		skin.addRegions((TextureAtlas) manager.get("Packs/ButtonsEditor.pack"));
		style = new TextButtonStyle();
		style.font = font; 
		style.up = skin.getDrawable("Button_Editor_Back");
		style.down = skin.getDrawable("ButtonPressed_Editor_Back");
		TextButton backButton = new TextButton("", style);

		//Creates the listener for the Back button
		backButton.addListener(new ChangeListener() { 			
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				//Calls the screen manager and has main be the shown screen if Back is hit
				ScreenAdapterManager.getInstance().show(ScreenAdapterEnums.MAIN);
			}
		});
		backButton.setSize(150,300);
		optionsTable.add(backButton);
	}
	
	private void createChoices() {
		font = game.getBitmapFontButton();
		style = new TextButtonStyle();
		style.font = font;
		style.up = buttonSkin.getDrawable("Button_LevelPackIcon");
		style.down = buttonSkin.getDrawable("ButtonChecked_LevelPackIcon");
		for(int i = 0; i < optionsArray.length; i++) {
			TextButton button = new TextButton(optionsArray[i], style);
			button.setSize(200, 200);
			if(i % 2 == 1)
				topRow.add(button);
			else
				bottomRow.add(button);
		}
	}
}
