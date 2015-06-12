package org.TheGivingChild.Screens;

import org.TheGivingChild.Engine.MinigameClock;
import org.TheGivingChild.Engine.XML.GameObject;
import org.TheGivingChild.Engine.XML.Level;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

/**
 * Takes a given {@link org.TheGivingChild.Engine.XML.Level Level} and allows the user to play it.
 * It displays all {@link org.TheGivingChild.Engine.XML.GameObject GameObject} in the level continuously as they update.
 * @author njacobi, mtzimour
 *
 */
public class ScreenLevel extends ScreenAdapter{
	private Level currentLevel;
	private SpriteBatch batch;
	private AssetManager manager;
	
	public ScreenLevel() {
		ScreenAdapterManager.getInstance().cb.setChecked(false);
	}
	
	/**
	 * Called when the level is complete and is returning to the main screen.
	 * Nulls everything since they are initialized whenever ScreenLevel is shown.
	 */
	@Override
	public void hide() {
		currentLevel = null;
		manager = null;
		ScreenAdapterManager.getInstance().cb.setChecked(false);;
	}
	
	/**
	 * Called when the ScreenLevel is supposed to be shown. It initializes the level with the given level
	 * and sets its {@link com.badlogic.gdx.assets.AssetManager AssetManager} to the game's. 
	 * It resets all objects in the level and loads them.
	 */
	@Override
	public void show() {
		manager = ScreenAdapterManager.getInstance().game.getAssetManager();
		currentLevel = ScreenAdapterManager.getInstance().game.getCurrentLevel();
		currentLevel.loadObjectsToStage();
		for(GameObject gameObject: currentLevel.getGameObjects()){
			gameObject.resetObject();
		}
		batch = new SpriteBatch();
		
		//make sure to set the level time so it does not go negative
		//MinigameClock.getInstance().setLevelLength(5);
	}
	
	/**
	 * Calls a screen transition effect first.
	 * Once completed, it goes through all of the {@link org.TheGivingChild.Engine.XML.GameObject GameObjects} 
	 * that the game contains and draws them. If the GameObjects are supposed to be destroyed they are ignored. 
	 * After drawing the level is updated. 
	 * @param delta Amount of time passed between each render call. In seconds
	 */
	@Override
	public void render(float delta) {
		ScreenAdapterManager.getInstance().screenTransitionInComplete = ScreenAdapterManager.getInstance().screenTransitionIn();
		
		if(manager.update()) {
			if(ScreenAdapterManager.getInstance().SCREEN_TRANSITION_TIME_LEFT <= 0 && ScreenAdapterManager.getInstance().screenTransitionInComplete) {
				Gdx.gl.glClearColor(0, 0.2F, 0.5f, 1);
				Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
				batch.begin();
				batch.draw((Texture) manager.get(currentLevel.getLevelImage()),0,0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
				for (GameObject g : currentLevel.getGameObjects()) {
					if(!g.isDisposed()){
						batch.draw(g.getTexture(), g.getX(), g.getY(),g.getTextureWidth(), g.getTextureHeight());
					}
				}
//				MinigameClock.getInstance().render();
				//only draw if there is time remaining in the clock
				//This if made the clock never drawn at all
				if(!MinigameClock.getInstance().outOfTime())
				{
				currentLevel.getClockFont().draw(batch, MinigameClock.getInstance().toString(), Gdx.graphics.getWidth() / 3,Gdx.graphics.getHeight() - 10);
				}
				currentLevel.update();
				batch.end();
				if (currentLevel.getCompleted()) {
					ScreenAdapterManager.getInstance().game.levelCompleted(currentLevel.getWon());
					ScreenAdapterManager.getInstance().game.setFromGame(true);
					ScreenAdapterManager.getInstance().show(ScreenAdapterEnums.MAZE);
				}

			}
		}
		if(ScreenAdapterManager.getInstance().SCREEN_TRANSITION_TIME_LEFT >= 0)
			ScreenAdapterManager.getInstance().SCREEN_TRANSITION_TIME_LEFT -= Gdx.graphics.getDeltaTime();
	}
	

}
