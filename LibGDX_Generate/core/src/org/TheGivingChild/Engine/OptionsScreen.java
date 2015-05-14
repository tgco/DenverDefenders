package org.TheGivingChild.Engine;

import sun.java2d.pipe.SpanClipRenderer;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class OptionsScreen extends ScreenAdapter {
	private Game mainGame;
	private Texture titleImage;
	private OrthographicCamera camera;
	private SpriteBatch batch;
	public OptionsScreen(Game mainGame) {
		this.mainGame = mainGame;
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
