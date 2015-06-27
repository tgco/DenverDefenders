package org.TheGivingChild.Screens;

import org.TheGivingChild.Engine.MyChangeListener;
import org.TheGivingChild.Engine.TGC_Engine;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Button.ButtonStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.utils.Align;

/**
 * This class creates the how to play screen. It displays a message that explains how the
 * game will be played. It also allows navigation to any other screen as well.
 * @author ctokunag
 */
class ScreenHowToPlay extends ScreenAdapter{
	private Texture title;
	private Batch batch;
	private Table table;
	private Table messageTable;
	private String[] buttonAtlasNamesArray = {"ButtonPressed_MainScreen_Play", 
											  "Button_MainScreen_Play", 
											  "ButtonPressed_MainScreen_Options", 
											  "Button_MainScreen_Options"};
	private Skin skin;
	private TGC_Engine game;
	
	public ScreenHowToPlay() {
		game = ScreenAdapterManager.getInstance().game;
		batch = new SpriteBatch();
		skin = new Skin();
		table = createButtons();
		messageTable = createMessage();
	}
	
	//Function for making buttons in the HTP screen
	public Table createButtons() {
		//create table for buttons
		Table t = new Table();
		skin.addRegions((TextureAtlas) game.getAssetManager().get("Packs/Buttons.pack"));
		//variable to help with table positioning
		int widthDivider = buttonAtlasNamesArray.length;
		//iterates over button names, allows for more buttons to be added
		for(int i = 0; i < buttonAtlasNamesArray.length-1; i += 2) {
			ButtonStyle tbs = new TextButtonStyle();
			tbs.down = skin.getDrawable(buttonAtlasNamesArray[i]);
			tbs.up = skin.getDrawable(buttonAtlasNamesArray[i+1]);
			Button tb = new Button(tbs);
			tb.setSize(Gdx.graphics.getWidth()/widthDivider*2, Gdx.graphics.getHeight()/3);
			t.add(tb).size(Gdx.graphics.getWidth()/widthDivider/2, Gdx.graphics.getHeight()/3/2).pad((Gdx.graphics.getWidth()/200)*(buttonAtlasNamesArray.length/2));
			// Pick inScreen for button
			int j = i/2;
			ScreenAdapterEnums choice = null;
			if (j == 0) choice = ScreenAdapterEnums.MAZE;
			else if (j == 1) choice = ScreenAdapterEnums.OPTIONS;
			// Wrap in final for the listener 'closure'
			final ScreenAdapterEnums inScreen = choice;
			//listener to change screens on button press
			tb.addListener(new MyChangeListener(){
				@Override
				public void changed(ChangeEvent event, Actor actor) {
					super.changed(event, actor);
					ScreenTransition htpToOther = new ScreenTransition(ScreenAdapterEnums.HOW_TO_PLAY, inScreen);
					game.setScreen(htpToOther);
				}
			});
		}
		t.setSize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		t.align(Align.bottom);
		return t;
	}
	
	public Table createMessage() {
		BitmapFont font = game.getBitmapFontButton();
		LabelStyle ls = new LabelStyle();
		ls.font = font;
		Skin newSkin = new Skin();
		newSkin.add("background", game.getAssetManager().get("SemiTransparentBG.png"));
		ls.background = newSkin.getDrawable("background");
		Label message = new Label("Make your way through the maze to find the kids. "
							+ "Finding a kid will trigger a mini-game. "
							+ "If you complete the mini-game, the kid will follow you. "
							+ "If you lose the mini-game, the kid will go to a different part of the maze. "
							+ "Once all of the kids have been found, return to the start of the maze to win.", ls);
		message.setFontScale(game.getGlobalFontScale());
		message.setColor(Color.WHITE);
		message.setWrap(true);
		Table table = new Table();
		table.add(message).width(Gdx.graphics.getWidth()*2/3);
		table.setSize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		table.align(Align.center);
		return table;
	}
	
	@Override
	public void hide() {
		table.remove();
		messageTable.remove();
	}
	
	@Override
	public void render(float delta) {
		title = game.getAssetManager().get("titleHowToPlayScreen.png");
		Gdx.gl.glClearColor(0, 1, 1, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		ScreenAdapterManager.getInstance().backgroundImage();
		//shows HTP title
		batch.begin();
		batch.draw(title, (Gdx.graphics.getWidth()-title.getWidth())/2, Gdx.graphics.getHeight()-title.getHeight());
		batch.end();
	}
	
	@Override
	public void show() {
		game.getStage().addActor(table);
		game.getStage().addActor(messageTable);
	}
	
	@Override
	public void dispose() {
		skin.dispose();
	}

	public static void requestAssets(AssetManager manager) {
		manager.load("titleHowToPlayScreen.png", Texture.class);
	}
	
}
