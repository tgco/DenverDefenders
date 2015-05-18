package org.TheGivingChild.Engine;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener.ChangeEvent;
import com.badlogic.gdx.scenes.scene2d.Actor;

public class HowToPlay extends ScreenAdapter{
	private TGC_Engine game;
	private Texture title;
	private Texture message;
	private OrthographicCamera camera;
	private Batch batch;
	private Table table;
	private String[] buttonAtlasNamesArray = {"ButtonPressed_MainScreen_Play", "Button_MainScreen_Play", "ButtonPressed_MainScreen_Options", "Button_MainScreen_Options"};
	private float buttonHeight;
	
	
	public HowToPlay(TGC_Engine game) {
		this.game = game;
		camera = new OrthographicCamera();
		title = new Texture(Gdx.files.internal("howToPlay.png"));
		message = new Texture(Gdx.files.internal("HowToPlayMessage.png"));
		batch = new SpriteBatch();
		table = createButtons();
	}
	
	
	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(0, 1, 1, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		batch.begin();
		batch.draw(title, 30, 280);
		//batch.draw(message, 50, 80);
		batch.end();
		
	}
	
	public Table createButtons() {
		Table t = new Table();
		BitmapFont font = game.getBitmapFontButton();
		int widthDivider = buttonAtlasNamesArray.length/2;
		for(int i = 0; i < buttonAtlasNamesArray.length-1; i += game.BUTTON_STATES) {
			TextButtonStyle tbs = new TextButtonStyle();
			tbs.font = font;
			tbs.down = game.getButtonAtlasSkin().getDrawable("Buttons/"+buttonAtlasNamesArray[i]);
			tbs.up = game.getButtonAtlasSkin().getDrawable("Buttons/"+buttonAtlasNamesArray[i+1]);
			TextButton tb = new TextButton("", tbs);
			tb.setSize(Gdx.graphics.getWidth()/widthDivider, Gdx.graphics.getHeight()/3);
			t.add(tb).size(Gdx.graphics.getWidth()/widthDivider, Gdx.graphics.getHeight()/3);
			buttonHeight = tb.getHeight();
			final int j = i;
			tb.addListener(new ChangeListener(){
				@Override
				public void changed(ChangeEvent event, Actor actor) {
					if(j == 0)
						game.setScreen(game.screens[0]);
					else
						game.setScreen(game.screens[2]);
					hide();
				}
			});
		}
		t.setPosition(Gdx.graphics.getWidth()/2, buttonHeight/2);
		return t;
	}
	
	@Override
	public void show() {
		game.addTable(table);
	}
	
	@Override
	public void hide() {
		game.removeTable(table);
	}
	
}
