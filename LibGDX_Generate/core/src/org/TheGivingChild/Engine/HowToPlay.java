package org.TheGivingChild.Engine;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.Batch;

public class HowToPlay extends ScreenAdapter{
	private Game game;
	private Texture title;
	private OrthographicCamera camera;
	private Batch batch;
	
	public HowToPlay(Game game) {
		this.game = game;
		camera = new OrthographicCamera();
		//title = new Texture(Gdx.files.internal("howToPlay.png"));
	}
	
	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(0, 1, 1, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
	}
}
