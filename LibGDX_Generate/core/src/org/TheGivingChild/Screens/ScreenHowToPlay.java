package org.TheGivingChild.Screens;

import org.TheGivingChild.Engine.TGC_Engine;

import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.Actor;

class ScreenHowToPlay extends ScreenAdapter{
	private Texture title;
	private Texture message;
	private Batch batch;
	private Table table;
	private String[] buttonAtlasNamesArray = {"ButtonPressed_MainScreen_Play", "Button_MainScreen_Play", "ButtonPressed_MainScreen_Editor", "Button_MainScreen_Editor", "ButtonPressed_MainScreen_Options", "Button_MainScreen_Options"};
	private float buttonHeight;
	
	private TGC_Engine game;
	
	public ScreenHowToPlay() {
		game = ScreenAdapterManager.getInstance().game;
		title = game.getAssetManager().get("HowToPlay.png");
		message = game.getAssetManager().get("HowToPlayMessage.png");
		batch = new SpriteBatch();
		table = createButtons();
	}
	
	
	//Function for making buttons in the HTP screen
	public Table createButtons() {
		//create table for buttons
		Table t = new Table();
		//set font for buttons
		BitmapFont font = game.getBitmapFontButton();
		//variable to help with table positioning
		int widthDivider = buttonAtlasNamesArray.length;
		//iterates over button names, allows for more buttons to be added
		for(int i = 0; i < buttonAtlasNamesArray.length-1; i += game.getButtonStates()) {
			TextButtonStyle tbs = new TextButtonStyle();
			tbs.font = font;
			tbs.down = game.getButtonAtlasSkin().getDrawable(buttonAtlasNamesArray[i]);
			tbs.up = game.getButtonAtlasSkin().getDrawable(buttonAtlasNamesArray[i+1]);
			TextButton tb = new TextButton("", tbs);
			tb.setSize(Gdx.graphics.getWidth()/widthDivider, Gdx.graphics.getHeight()/3);
			t.add(tb).size(Gdx.graphics.getWidth()/widthDivider, Gdx.graphics.getHeight()/3);
			buttonHeight = tb.getHeight();
			final int j = i/2;
			//listener to change screens on button press
			tb.addListener(new ChangeListener(){
				@Override
				public void changed(ChangeEvent event, Actor actor) {
					if(j == 0)
						ScreenAdapterManager.getInstance().show(ScreenAdapterEnums.LEVEL_PACKETS);
					else if(j == 1)
						ScreenAdapterManager.getInstance().show(ScreenAdapterEnums.EDITOR);
					else
						ScreenAdapterManager.getInstance().show(ScreenAdapterEnums.OPTIONS);
					hide();
				}
			});
		}
		t.setPosition(Gdx.graphics.getWidth()/widthDivider, buttonHeight/3);
		return t;
	}
	
	@Override
	public void hide() {
		game.removeTable(table);
	}
	
	@Override
	public void render(float delta) {
		//creates background color
		Gdx.gl.glClearColor(0, 1, 1, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		//shows HTP title and text
		batch.begin();
		batch.draw(title, (Gdx.graphics.getWidth()-title.getWidth())/2, Gdx.graphics.getHeight()-title.getHeight());
		batch.draw(message, (Gdx.graphics.getWidth()-message.getWidth())/2, Gdx.graphics.getHeight()-message.getHeight()-title.getHeight());
		batch.end();
		
	}
	
	@Override
	public void show() {
		game.addTable(table);
	}
	
}
