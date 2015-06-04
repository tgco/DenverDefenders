package org.TheGivingChild.Screens;

import com.badlogic.gdx.ScreenAdapter;
/**
 * Enumerations to return ScreenAdapters for the ScreenAdapterManager
 * 
 * @author janelson
 *
 */
public enum ScreenAdapterEnums {
	/**
	 * The level packet screen, returns a new ScreenLevelPackets
	 */
	LEVEL_PACKETS{
		@Override
		protected ScreenAdapter getScreenInstance() {
			return new ScreenLevelPackets();
		}
	},
	/**
	 * The how to play screen, returns a new ScreenHowToPlay
	 */
	HOW_TO_PLAY{
		@Override
		protected ScreenAdapter getScreenInstance() {
			return new ScreenHowToPlay();
		}
	}, 
	/**
	 * The editor screen, returns a new ScreenEditor
	 */
	EDITOR{
		@Override
		protected ScreenAdapter getScreenInstance() {
			return new ScreenEditor();
		}
	},
	/**
	 * The options screen, returns a new ScreenOptions
	 */
	OPTIONS{
		@Override
		protected ScreenAdapter getScreenInstance() {
			return new ScreenOptions();
		}
	},
	/**
	 * The options screen, returns a new ScreenOptions
	 */
	CHARACTER_CREATOR{
		@Override
		protected ScreenAdapter getScreenInstance() {
            return new ScreenMaze();
        }
	},
	/**
	 * The main screen, returns a new ScreenMain
	 */
	MAIN{
		@Override
		protected ScreenAdapter getScreenInstance() {

			return new ScreenMain();
		}
	},
	/**
	 * The minigame level screen, returns a new ScreenLevel
	 */
	LEVEL{
		@Override
		protected ScreenAdapter getScreenInstance() {
			return new ScreenLevel();
		}
	},
	/**
	 * The maze 'level' screen, returns a new ScreenMaze
	 */
	MAZE{
		@Override
		protected ScreenAdapter getScreenInstance() {
			return new ScreenMaze();
		}
	};
	/**
	 * Abstract protected class that the manager uses to return a ScreenAdapter 
	 */
	protected abstract ScreenAdapter getScreenInstance();
}
