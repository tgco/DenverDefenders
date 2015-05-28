package org.TheGivingChild.Screens;

import org.TheGivingChild.Engine.TGC_Engine;

import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.Align;

class ScreenHowToPlay extends ScreenAdapter{
	private Texture title;
	private Texture message;
	private Batch batch;
	private Table table;
	private String[] buttonAtlasNamesArray = {"ButtonPressed_MainScreen_Play", 
											  "Button_MainScreen_Play", 
											  "ButtonPressed_MainScreen_Editor", 
											  "Button_MainScreen_Editor", 
											  "ButtonPressed_MainScreen_Options", 
											  "Button_MainScreen_Options",
											  "ButtonPressed_MainScreen_CharacterCreator",
											  "Button_MainScreen_CharacterCreator"};
	private Skin skin = new Skin();
	private AssetManager manager = new AssetManager();
	private float screenTransitionTimeLeft = 1.0f;
	private boolean isRendered = false;
	private TGC_Engine game;
	
	public ScreenHowToPlay() {
		game = ScreenAdapterManager.getInstance().game;
		batch = new SpriteBatch();
		table = createButtons();
		manager = game.getAssetManager();
		manager.load("HowToPlay.png", Texture.class);
		manager.load("HowToPlayMessage.png", Texture.class);
	}
	
	//Function for making buttons in the HTP screen
	public Table createButtons() {
		//create table for buttons
		Table t = new Table();
		//set font for buttons
		BitmapFont font = game.getBitmapFontButton();
		//
		skin.addRegions((TextureAtlas) game.getAssetManager().get("Packs/Buttons.pack"));
		//variable to help with table positioning
		int widthDivider = buttonAtlasNamesArray.length;
		//iterates over button names, allows for more buttons to be added
		for(int i = 0; i < buttonAtlasNamesArray.length-1; i += game.getButtonStates()) {
			TextButtonStyle tbs = new TextButtonStyle();
			tbs.font = font;
			tbs.down = skin.getDrawable(buttonAtlasNamesArray[i]);
			tbs.up = skin.getDrawable(buttonAtlasNamesArray[i+1]);
			TextButton tb = new TextButton("", tbs);
			tb.setSize(Gdx.graphics.getWidth()/widthDivider*2, Gdx.graphics.getHeight()/3);
			t.add(tb).size(Gdx.graphics.getWidth()/widthDivider*2, Gdx.graphics.getHeight()/3);
			final int j = i/2;
			//listener to change screens on button press
			tb.addListener(new ChangeListener(){
				@Override
				public void changed(ChangeEvent event, Actor actor) {
					if(j == 0)
						ScreenAdapterManager.getInstance().show(ScreenAdapterEnums.LEVEL_PACKETS);
					else if(j == 1)
						ScreenAdapterManager.getInstance().show(ScreenAdapterEnums.EDITOR);
					else if(j == 2)
						ScreenAdapterManager.getInstance().show(ScreenAdapterEnums.OPTIONS);
					else if(j == 3)
						ScreenAdapterManager.getInstance().show(ScreenAdapterEnums.CHARACTER_CREATOR);
					else
						ScreenAdapterManager.getInstance().show(ScreenAdapterEnums.MAIN);
					hide();
				}
			});
		}
		//t.setPosition(Gdx.graphics.getWidth()/widthDivider, buttonHeight/3);
		t.setSize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		t.align(Align.bottom);
		return t;
	}
	
	@Override
	public void hide() {
		table.remove();
	}
	
	@Override
	public void render(float delta) {
		if(manager.update()) {
			if(screenTransitionTimeLeft <= 0) {
				if(manager.isLoaded("HowToPlay.png"))
					title = manager.get("HowToPlay.png");
				if(manager.isLoaded("HowToPlayMessage.png"))
					message = manager.get("HowToPlayMessage.png");
				//creates background color
				Gdx.gl.glClearColor(0, 1, 1, 1);
				Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
				//shows HTP title and text
				batch.begin();
				batch.draw(title, (Gdx.graphics.getWidth()-title.getWidth())/2, Gdx.graphics.getHeight()-title.getHeight());
				batch.draw(message, (Gdx.graphics.getWidth()-message.getWidth())/2, Gdx.graphics.getHeight()-message.getHeight()-title.getHeight());
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
		if(isRendered)
			game.getStage().addActor(table);
	}
	
}
