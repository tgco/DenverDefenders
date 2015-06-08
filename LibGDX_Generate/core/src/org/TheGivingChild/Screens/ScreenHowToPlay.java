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
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.Align;

/**
 * This class creates the how to play screen. It displays a message that explains how the
 * game will be played. It also allows navigation to any other screen as well.
 * @author ctokunag
 */
class ScreenHowToPlay extends ScreenAdapter{
	private Texture title;
	private Texture message;
	private TextureRegion titleRegion;
	private TextureRegion messageRegion;
	private Batch batch;
	private Table table;
	private String[] buttonAtlasNamesArray = {"ButtonPressed_MainScreen_Play", 
											  "Button_MainScreen_Play", 
											  "ButtonPressed_MainScreen_Editor", 
											  "Button_MainScreen_Editor", 
											  "ButtonPressed_MainScreen_Options", 
											  "Button_MainScreen_Options"};
	private Skin skin = new Skin();
	private AssetManager manager = new AssetManager();
	private boolean isRendered = false;
	private TGC_Engine game;
	private boolean regionsLoaded = false;
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
			t.add(tb).size(Gdx.graphics.getWidth()/widthDivider/2, Gdx.graphics.getHeight()/3/2).pad((Gdx.graphics.getWidth()/200)*(buttonAtlasNamesArray.length/2));
			final int j = i/2;
			//listener to change screens on button press
			tb.addListener(new ChangeListener(){
				@Override
				public void changed(ChangeEvent event, Actor actor) {
					if(j == 0)
						ScreenAdapterManager.getInstance().show(ScreenAdapterEnums.MAZE);
					else if(j == 1)
						ScreenAdapterManager.getInstance().show(ScreenAdapterEnums.EDITOR);
					else if(j == 2)
						ScreenAdapterManager.getInstance().show(ScreenAdapterEnums.OPTIONS);
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
		//boolean transitionInComplete = ScreenAdapterManager.getInstance().screenTransitionIn();
		ScreenAdapterManager.getInstance().screenTransitionInComplete = ScreenAdapterManager.getInstance().screenTransitionIn();
		
		if(manager.update()) {
			if(ScreenAdapterManager.getInstance().SCREEN_TRANSITION_TIME_LEFT <= 0 && ScreenAdapterManager.getInstance().screenTransitionInComplete) {
				if(manager.isLoaded("HowToPlay.png"))
					title = manager.get("HowToPlay.png");
				if(manager.isLoaded("HowToPlayMessage.png"))
					message = manager.get("HowToPlayMessage.png");
				//creates background color
				if(!regionsLoaded) {
					titleRegion = new TextureRegion(title);
					messageRegion = new TextureRegion(message);
				}
				Gdx.gl.glClearColor(0, 1, 1, 1);
				Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
				ScreenAdapterManager.getInstance().backgroundImage();
				//shows HTP title and text
				batch.begin();
				batch.draw(titleRegion, (Gdx.graphics.getWidth()-title.getWidth())/2, Gdx.graphics.getHeight()-title.getHeight());
				batch.draw(messageRegion, (Gdx.graphics.getWidth()-message.getWidth())/2, Gdx.graphics.getHeight()-message.getHeight()-title.getHeight());
				batch.end();
				isRendered = true;
				show();
			}
		}
		if(ScreenAdapterManager.getInstance().SCREEN_TRANSITION_TIME_LEFT >= 0)
			ScreenAdapterManager.getInstance().SCREEN_TRANSITION_TIME_LEFT -= Gdx.graphics.getDeltaTime();
	}
	
	@Override
	public void show() {
		if(isRendered){
			game.getStage().addActor(table);
			isRendered = false;
		}
	}
	
}
