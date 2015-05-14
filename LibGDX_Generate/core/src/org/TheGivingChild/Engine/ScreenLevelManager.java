package org.TheGivingChild.Engine;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.ui.Table;

public class ScreenLevelManager extends ScreenAdapter{
	private TGC_Engine game;
	public ScreenLevelManager(TGC_Engine game) {
		this.game = game;
	}
	
	public Table createLevelPacketButtons(){
		Table table = new Table();
		BitmapFont font = new BitmapFont();
		
		for(Level l: game.getLevels()){
			
		}
		
		return table;
	}
	
	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(1,0,1,1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		
	}
}
