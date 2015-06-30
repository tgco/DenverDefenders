package org.TheGivingChild.Screens;

import org.TheGivingChild.Engine.MinigameClock;
import org.TheGivingChild.Engine.TGC_Engine;
import org.TheGivingChild.Engine.XML.GameObject;
import org.TheGivingChild.Engine.XML.Level;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
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
	private TGC_Engine game;
	private boolean logicPaused;
	
	public ScreenLevel() {
		batch = new SpriteBatch();
		game = ScreenAdapterManager.getInstance().game;
		currentLevel = null;
		// No logic until show
		logicPaused = true;
	}
	
	/**
	 * Called when the level is complete and is returning to the main screen.
	 * Nulls everything since they are initialized whenever ScreenLevel is shown.
	 */
	@Override
	public void hide() {
		logicPaused = true;
	}
	
	/**
	 * Called when the ScreenLevel is supposed to be shown. It initializes with the given level. 
	 * It resets all objects in the level and loads them.
	 */
	@Override
	public void show() {
		// Load objects to game's stage
		currentLevel.loadObjectsToStage();
		logicPaused = false;
	}
	
	public void setCurrentLevel (Level level) {
		currentLevel = level;
	}
	
	/**
	 * Goes through all of the {@link org.TheGivingChild.Engine.XML.GameObject GameObjects} 
	 * that the game contains and draws them. If the GameObjects are supposed to be destroyed they are ignored. 
	 * After drawing the level is updated. 
	 * @param delta Amount of time passed between each render call. In seconds
	 */
	@Override
	public void render(float delta) {
		batch.begin();
		batch.draw((Texture) game.getAssetManager().get("LevelBackgrounds/" + currentLevel.getLevelImage()),0,0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		for (GameObject g : currentLevel.getGameObjects()) {
			if(!g.isDisposed()){
				batch.draw(g.getTexture(), g.getX(), g.getY(),g.getTextureWidth(), g.getTextureHeight());
			}
		}
		//only draw if there is time remaining in the clock
		if (!MinigameClock.getInstance().outOfTime()) {
			currentLevel.getClockFont().draw(batch, MinigameClock.getInstance().toString(), Gdx.graphics.getWidth() / 3,Gdx.graphics.getHeight() - 10);
		}
		batch.end();
		if (!logicPaused) {
			currentLevel.update();
			if (currentLevel.getCompleted()) {
				String text = buildResponseText(currentLevel.getWon());
				// Alert maze that the minigame was won so the child will follow
				((ScreenMaze) ScreenAdapterManager.getInstance().getScreenFromEnum(ScreenAdapterEnums.MAZE)).setLevelWon(currentLevel.getWon());
				ScreenTransition levelToMaze = new ScreenTransition(ScreenAdapterEnums.LEVEL, ScreenAdapterEnums.MAZE, text);
				game.setScreen(levelToMaze);
			}
		}
	}
	
	// Returns the string that tells the player if they won or lost
	public String buildResponseText(boolean won) {
		if (won)
			return "You won!";
		else
			return "You lost";
	}
}
