package org.TheGivingChild.Screens;

import org.TheGivingChild.Engine.XML.GameObject;
import org.TheGivingChild.Engine.XML.Level;
import org.TheGivingChild.Engine.XML.XML_Reader;
import org.TheGivingChild.Engine.XML.XML_Writer;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Array;

public class ScreenLevel extends ScreenAdapter{

	private Level level;
	private Array<String> textureFile;
	private SpriteBatch batch;
	private AssetManager manager;
	public ScreenLevel() {

	}

	@Override
	public void hide() {
		level = null;

		manager = null;
		textureFile.clear();
	}

	@Override
	public void show() {
		level = ScreenAdapterManager.getInstance().game.getLevels().get(0);
		manager = ScreenAdapterManager.getInstance().game.getAssetManager();
		manager.load("ball.png", Texture.class);
		manager.load("Box.png", Texture.class);
		manager.load("BoxHalf.png", Texture.class);
		manager.load("Grid.png", Texture.class);
		for(GameObject gameObject: level.getGameObjects()){
			gameObject.resetObject();
		}
		level.loadObjectsToStage();
		textureFile = new Array<String>();
		for (GameObject g : level.getGameObjects()) {
			textureFile.add(g.getImageFilename());
		}
		batch = new SpriteBatch();
	}

	@Override
	public void render(float delta) {
		ScreenAdapterManager.getInstance().screenTransitionOutComplete = ScreenAdapterManager.getInstance().screenTransitionIn();
		
		if(manager.update()) {
			if(ScreenAdapterManager.getInstance().SCREEN_TRANSITION_TIME_LEFT <= 0 && ScreenAdapterManager.getInstance().screenTransitionOutComplete) {
				Gdx.gl.glClearColor(0, 0.2F, 0.5f, 1);
				Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
				batch.begin();
				for (GameObject g : level.getGameObjects()) {
					if(!g.isDisposed()){
						batch.draw((Texture) manager.get(g.getImageFilename()), g.getX(), g.getY());
					}
				}
				level.update();
				batch.end();
			}
		}
		if(ScreenAdapterManager.getInstance().SCREEN_TRANSITION_TIME_LEFT >= 0)
			ScreenAdapterManager.getInstance().SCREEN_TRANSITION_TIME_LEFT -= Gdx.graphics.getDeltaTime();
	}
}
