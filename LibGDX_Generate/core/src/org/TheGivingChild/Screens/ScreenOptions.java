package org.TheGivingChild.Screens;

import org.TheGivingChild.Engine.TGC_Engine;

import sun.java2d.pipe.SpanClipRenderer;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

class ScreenOptions extends ScreenAdapter {
	private Texture titleImage;
	private OrthographicCamera camera;
	private SpriteBatch batch;
	
	private TGC_Engine game;
	
	public ScreenOptions() {
		game = ScreenAdapterManager.getInstance().game;
		camera = new OrthographicCamera();

		
		titleImage = new Texture(Gdx.files.internal("optionsTitle.png"));
		batch = new SpriteBatch();
	}
	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(1,1,0,1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		batch.begin();
		batch.draw(titleImage, 0, 0);
		batch.end();
	}
}
