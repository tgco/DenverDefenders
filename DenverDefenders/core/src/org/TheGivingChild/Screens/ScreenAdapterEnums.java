package org.TheGivingChild.Screens;

import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.assets.AssetManager;
/**
 * Enumerations to return ScreenAdapters for the ScreenAdapterManager
 * 
 * The first few enums will be what the main_screen draws buttons for.
 * The number of items being drawn can be changed in the Screen_Main
 * 
 * @author janelson
 *
 */
public enum ScreenAdapterEnums {
	/**
	 * Maze selection screen
	 */
	MAZE_SELECT {
		@Override
		public ScreenAdapter getScreenInstance() {
			return new ScreenMazeSelect();
		}
		
		@Override
		public void requestAssets(AssetManager manager) {
			ScreenMazeSelect.requestAssets(ScreenAdapterManager.getInstance().game.getAssetManager());
		}
	},
	/**
	 * The how to play screen, returns a new ScreenHowToPlay
	 */
	HOW_TO_PLAY{
		@Override
		public ScreenAdapter getScreenInstance() {
			return new ScreenHowToPlay();
		}
		
		@Override
		public void requestAssets(AssetManager manager) {
			ScreenHowToPlay.requestAssets(ScreenAdapterManager.getInstance().game.getAssetManager());
		}
	}, 
	/**
	 * The options screen, returns a new ScreenOptions
	 */
	OPTIONS{
		@Override
		public ScreenAdapter getScreenInstance() {
			return new ScreenOptions();
		}
		
		@Override
		public void requestAssets(AssetManager manager) {
			ScreenOptions.requestAssets(ScreenAdapterManager.getInstance().game.getAssetManager());
		}
	},
	/**
	 * The main screen, returns a new ScreenMain
	 */
	MAIN{
		@Override
		public ScreenAdapter getScreenInstance() {

			return new ScreenMain();
		}
		
		@Override
		public void requestAssets(AssetManager manager) {
			ScreenMain.requestAssets(ScreenAdapterManager.getInstance().game.getAssetManager());
		}
	},
	/**
	 * The maze 'level' screen, returns a new ScreenMaze
	 */
	MAZE{
		@Override
		public ScreenAdapter getScreenInstance() {
			return new ScreenMaze();
		}
		
		@Override
		public void requestAssets(AssetManager manager) {
			ScreenMaze.requestAssets(ScreenAdapterManager.getInstance().game.getAssetManager());
		}
	},
	/**
	 * The minigame level screen, returns a new ScreenLevel
	 */
	LEVEL{
		@Override
		public ScreenAdapter getScreenInstance() {
			return new ScreenLevel();
		}
		
		@Override
		public void requestAssets(AssetManager manager) {
			// Level assets should be loaded by encompassing maze
		}
	},
	SPLASH {
		@Override
		public ScreenAdapter getScreenInstance() {
			return new ScreenSplash();
		}
		
		@Override
		public void requestAssets(AssetManager manager) {
			// Splash screen assets are loaded before shown since it is the first screen
		}
	},
	UNLOCK {
		@Override
		public ScreenAdapter getScreenInstance() {
			return new ScreenUnlock();
		}
		
		@Override
		public void requestAssets(AssetManager manager) {
			ScreenUnlock.requestAssets(ScreenAdapterManager.getInstance().game.getAssetManager());
		}
	};
	
	/**
	 * Abstract protected function that the manager uses to return a ScreenAdapter 
	 */
	public abstract ScreenAdapter getScreenInstance();
	
	// Returns the load request defined for each screen
	// Included in the enum as a workaround to adding the method to the base class of all screens
	public abstract void requestAssets(AssetManager manager);
}
