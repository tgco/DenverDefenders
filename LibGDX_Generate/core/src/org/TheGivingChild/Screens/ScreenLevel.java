package org.TheGivingChild.Screens;

import org.TheGivingChild.Engine.XML.Level;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class ScreenLevel extends ScreenAdapter{
	
	private Level level;
	private Texture texture; 
	private SpriteBatch batch;
	public ScreenLevel() {
		level = ScreenAdapterManager.getInstance().game.getLevels().first();
		texture = ScreenAdapterManager.getInstance().game.getAssetManager().get("ball.png");
		batch = new SpriteBatch();
	}

	@Override
	public void hide() {
		
	}
	
	@Override
	public void show() {
		
	}
	
	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(1, 0.2F, 0.5f, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		batch.begin();
		batch.draw(texture, 0, 0);
		batch.end();
	}
}
