package org.TheGivingChild.Screens;

import org.TheGivingChild.Engine.XML.GameObject;
import org.TheGivingChild.Engine.XML.Level;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Array;

public class ScreenLevel extends ScreenAdapter{
	
	private Level level;
	private Array<String> textureFile; 
	private Array<Texture> texture;
	private SpriteBatch batch;
	public ScreenLevel() {
		level = ScreenAdapterManager.getInstance().game.getLevels().get(0);
		//texture = ScreenAdapterManager.getInstance().game.getAssetManager().get("ball.png");
		textureFile = new Array<String>();
		for (GameObject g : level.getGameObjects()) {
			textureFile.add(g.getImageFilename());
		}
		
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
		Gdx.gl.glClearColor(1, 0.8F, 0.1f, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		batch.begin();
		for (GameObject g : level.getGameObjects()) {
			batch.draw((Texture) ScreenAdapterManager.getInstance().game.getAssetManager().get(g.getImageFilename()), g.getX(), g.getY());
		}
		level.update();
		batch.end();
	}
}
