package org.TheGivingChild.Engine;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;

public class OptionsScreen extends ScreenAdapter {
	private Game mainGame;
	private Texture titleImage;
	private OrthographicCamera camera;
	
	public OptionsScreen(Game mainGame) {
		this.mainGame = mainGame;
		camera = new OrthographicCamera();

		
		//titleImage = new Texture(Gdx.files.internal("optionsTitle.png"));
	}
	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(1,1,0,1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		
	}
}
