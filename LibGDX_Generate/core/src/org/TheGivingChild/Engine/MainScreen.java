package org.TheGivingChild.Engine;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener.ChangeEvent;
import com.badlogic.gdx.utils.Array;

public class MainScreen extends ScreenAdapter{
	private TGC_Engine game;
	private BitmapFont bitmapFontButton;
	private float buttonHeight;
    private Table mainScreenTable;
	
	public MainScreen(TGC_Engine game) {
		this.game = game;
		mainScreenTable = createMainScreenTable();
		showMainScreenTable();
	}

	private Table createMainScreenTable() {
		bitmapFontButton = game.getBitmapFontButton();
		//create a table for the buttons
        Table table = new Table();
        //variable to keep track of button height for table positioning
        int widthDivider = (game.getButtonAtlasNamesArray().length/2);
        //iterate over button pack names in order to check
        for(int i = 0; i < game.getButtonAtlasNamesArray().length-1; i+=game.BUTTON_STATES){
        	TextButtonStyle bs = new TextButtonStyle();
        	bs.font = bitmapFontButton;
        	bs.down = game.getButtonAtlasSkin().getDrawable("Buttons/"+game.getButtonAtlasNamesArray()[i]);
        	bs.up = game.getButtonAtlasSkin().getDrawable("Buttons/"+game.getButtonAtlasNamesArray()[i+1]);
        	TextButton b = new TextButton("", bs);
        	b.setSize(Gdx.graphics.getWidth()/widthDivider, Gdx.graphics.getHeight()/3);
        	table.add(b).size(Gdx.graphics.getWidth()/widthDivider, Gdx.graphics.getHeight()/3);
        	buttonHeight = b.getHeight();
        	final int j = i;
        	//button to transition to different screens.
        	b.addListener(new ChangeListener(){
				@Override
				public void changed(ChangeEvent event, Actor actor) {
					game.setScreen(game.screens[j/2]);
					hideMainScreenTable();
				}
        	});
        }
        table.setPosition(Gdx.graphics.getWidth()/2, buttonHeight/2);
        return table;
	}

	private void showMainScreenTable() {
		game.getRootTable().add(mainScreenTable);
		game.getRootTable().setPosition(Gdx.graphics.getWidth()/2, buttonHeight/2);
		game.getStage().addActor(game.getRootTable());
	}

	protected void hideMainScreenTable() {
		game.getRootTable().removeActor(mainScreenTable);
	}

	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
	}
	
	@Override
	public void show() {
		showMainScreenTable();
	}
}
