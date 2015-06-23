package org.TheGivingChild.Screens;

import org.TheGivingChild.Engine.MyChangeListener;
import org.TheGivingChild.Engine.TGC_Engine;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
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
											  "ButtonPressed_MainScreen_Editor", 
											  "Button_MainScreen_Editor", 
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
		//set font for buttons
		BitmapFont font = game.getBitmapFontButton();
		skin.addRegions((TextureAtlas) game.getAssetManager().get("Packs/Buttons.pack"));
		//variable to help with table positioning
		int widthDivider = buttonAtlasNamesArray.length;
		//iterates over button names, allows for more buttons to be added
		for(int i = 0; i < buttonAtlasNamesArray.length-1; i += game.getButtonStates()) {
			TextButtonStyle tbs = new TextButtonStyle();
			tbs.font = font;
			tbs.down = skin.getDrawable(buttonAtlasNamesArray[i]);
			tbs.up = skin.getDrawable(buttonAtlasNamesArray[i+1]); // i+1 HERE ELIMINATES THE USEFULNESS OF += BUTTONSTATES IN THE FOR POSTOP
			TextButton tb = new TextButton("", tbs);
			tb.setSize(Gdx.graphics.getWidth()/widthDivider*2, Gdx.graphics.getHeight()/3);
			t.add(tb).size(Gdx.graphics.getWidth()/widthDivider/2, Gdx.graphics.getHeight()/3/2).pad((Gdx.graphics.getWidth()/200)*(buttonAtlasNamesArray.length/2));
			final int j = i/2;
			//listener to change screens on button press
			//CONFUSING USE OF J, CHANGE THIS
			tb.addListener(new MyChangeListener(){
				@Override
				public void changed(ChangeEvent event, Actor actor) {
					super.changed(event, actor);
					ScreenAdapterEnums inScreen;
					if(j == 0)
						inScreen = ScreenAdapterEnums.MAZE;
					else if(j == 1)
						inScreen = ScreenAdapterEnums.EDITOR;
					else if(j == 2)
						inScreen = ScreenAdapterEnums.OPTIONS;
					else
						inScreen = ScreenAdapterEnums.MAIN;
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
		switch(Gdx.app.getType()){
		case Android:
			message.setFontScale(Gdx.graphics.getWidth()/(Gdx.graphics.getPpiX()));
			break;
			//if using the desktop set the width and height to a 16:9 resolution.
		case Desktop:
			message.setFontScale(Gdx.graphics.getWidth()/(Gdx.graphics.getPpiX()*5));
			break;
		case iOS:
			message.setFontScale(Gdx.graphics.getWidth()/(Gdx.graphics.getPpiX()));
			break;
		default:
			message.setFontScale(Gdx.graphics.getWidth()/(Gdx.graphics.getPpiX()*5));
			break;
		}
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
	
}
