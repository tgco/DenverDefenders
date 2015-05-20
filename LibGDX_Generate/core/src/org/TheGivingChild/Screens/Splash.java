package org.TheGivingChild.Screens;

import org.TheGivingChild.Engine.Assets;
import org.TheGivingChild.Engine.TGC_Engine;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.Game;

public class Splash implements Screen{
	private Texture texture = new Texture(Gdx.files.internal("img/badlogic.jpg"));
	private Image splashImage = new Image(texture);
	private Stage stage = new Stage();
	private Skin skin = Assets.skin;
	
	public boolean animationDone = false;
	
	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		stage.act();
		stage.draw();
		
		if(Assets.update()) {
			if(animationDone) {
				Assets.setSkin();
				((Game)Gdx.app.getApplicationListener()).setScreen(new Splash());
			}
		}
	}

	@Override
	public void show() {
		stage.addActor(splashImage);
		splashImage.addAction(Actions.sequence(Actions.alpha(0), Actions.fadeIn(0.75f), Actions.delay(1.5f), Actions.run(new Runnable() {
			@Override
			public void run() {
				animationDone = true;
			}
		})));
		Assets.queueLoading();
	}

	@Override
	public void resize(int width, int height) {
		
	}

	@Override
	public void pause() {
		
	}

	@Override
	public void resume() {
		
	}

	@Override
	public void hide() {
		dispose();
	}

	@Override
	public void dispose() {
		texture.dispose();
		stage.dispose();
	}	

}
